package com.darzalgames.libgdxtools.audio.engine;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.darzalgames.libgdxtools.audio.composition.NoteDuration;
import com.darzalgames.libgdxtools.audio.composition.Song;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.engine.samples.SampleMaker;
import com.darzalgames.libgdxtools.audio.engine.samples.SampleMakerImpl;
import com.darzalgames.libgdxtools.audio.engine.sound.SimpleSound;
import com.darzalgames.libgdxtools.audio.engine.sound.SimpleSoundMaker;
import com.darzalgames.libgdxtools.audio.engine.sound.SimpleSoundMakerImpl;
import com.darzalgames.libgdxtools.audio.engine.sound.TimedSimpleSound;
import com.darzalgames.libgdxtools.audio.time.BPSAcceptor;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;
import com.darzalgames.libgdxtools.preferences.SoundPreference;

public class MusicThread<GameSong extends Song> extends Thread implements ControlledProgrammedMusic<GameSong>, BPSAcceptor {
	
	private final SoundPreference soundPreferenceManager;
	private AtomicBoolean shouldStop;
	
	private GameSong song;
	private AtomicReference<GameSong> queuedSong;
	private SimpleSoundMaker simpleSoundMaker;
	private SampleMaker sampler; //Creates Samples from Simple Sounds
	private AudioDevice audioDevice; //receives Samples
	
	private final Queue<List<SimpleSound>> queuedSoundEffects;
	private List<TimedSimpleSound> activeSoundEffect;
	private float activeSoundEffectAge;

	
	private PeakingTracker peakingTracker;

	private float sixteenthProgress;
	private float timeInSong;
	private BPSController bpsController;
	private FadeOutComeIn fadeOutComeIn;
	private boolean mute;
	
	private float relativeSoundEffectVolume;
	private float relativeMusicVolume;
	
	public MusicThread(final SoundPreference soundPreferenceManager) {
		this(soundPreferenceManager, Gdx.audio.newAudioDevice(AudioConstants.SAMPLING_RATE, true));
	}

	public MusicThread(final SoundPreference soundPreferenceManager, AudioDevice audioDevice) {
		this.soundPreferenceManager = soundPreferenceManager;
		
		shouldStop = new AtomicBoolean(false);
		this.song = null;
		this.queuedSong = new AtomicReference<>();
		this.simpleSoundMaker = new SimpleSoundMakerImpl();
		this.sampler = new SampleMakerImpl();
		this.audioDevice = audioDevice;
		
		this.bpsController = new BPSController(0);
		this.peakingTracker = new PeakingTracker();
		this.fadeOutComeIn = new FadeOutComeIn(AudioConstants.STEP_DURATION);
		this.queuedSoundEffects = new ConcurrentLinkedQueue<>();
		this.activeSoundEffect = null;
		this.activeSoundEffectAge = 0f;
		
//		this.relativeSoundEffectVolume = DEFAULT_RELATIVE_SOUND_EFFECT_VOLUME; //TODO
//		this.relativeMusicVolume = DEFAULT_RELATIVE_MUSIC_VOLUME; //TODO
		
		setDaemon(true);
	}
	
	@Override
	public void queueSong(GameSong song) {
		this.queuedSong.set(song);
	}

	public void queueSongInstantly(GameSong song) {
		this.queuedSong.set(song);
		this.fadeOutComeIn = new InstantTransition();
		checkChangeSong();
	}
	
	protected GameSong getSong() {
		return song;
	}
	
	@Override
	public void playSound(List<SimpleSound> simpleSoundEffect) {
		this.queuedSoundEffects.add(simpleSoundEffect);
	}
	
	@Override
	public float getMusicVolume() {
		return soundPreferenceManager.getMusicVolume();
	}
	
	@Override
	public void setMusicVolume(float volume) {
		soundPreferenceManager.setMusicVolume(volume);
	}

	@Override
	public float getSoundEffectVolume() {
		return soundPreferenceManager.getSoundEffectVolume();
	}
	
	@Override
	public void setSoundEffectVolume(float volume) {
		soundPreferenceManager.setSoundEffectVolume(volume);
	}

	@Override
	public void temporarilyMute() {
		if (soundPreferenceManager.shouldMuteSoundWhenOutOfFocus()) {
			this.mute = true;
		}
	}

	@Override
	public void untemporarilyMute() {
		if (soundPreferenceManager.shouldMuteSoundWhenOutOfFocus()) {
			this.mute = false;
		}
	}
	
	@Override
	public void setShouldTemporarilyMute(boolean showTemporarilyMute) {
		soundPreferenceManager.setShouldMuteSoundWhenOutOfFocus(showTemporarilyMute);
	}
	
	@Override
	public boolean getShouldTemporarilyMute() {
		return soundPreferenceManager.shouldMuteSoundWhenOutOfFocus();
	}

	@Override
	public void run() {
		threadStarted();
		
		while (!shouldStop.get()) {
			processMusicStep();
		}

		dispose();
		threadStopped();
	}

	@Override
	public void shutdown() {
		shouldStop.set(true);
		try {
			join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void requestChangeBPS(float newBPS, float transitionTime) {
		bpsController.setTargetBPS(newBPS, transitionTime);
	}
	
	@Override
	public float getDecorativeBPS() {
		return bpsController.getBPS() * song.getDecorativeBPSMultiplier();
	}

	public float getBPS() {
		return bpsController.getBPS();
	}
	
	public void setRelativeVolumes(float soundEffectVolume, float musicVolume) {
		this.relativeSoundEffectVolume = soundEffectVolume;
		this.relativeMusicVolume = musicVolume;
	}

	/**
	 * processes and plays a small amount of music. The duration should be quite short to keep the thread responsive, much less than 100 ms
	 */
	public void processMusicStep() {
		checkChangeSong();
		
		//UPDATE BPS
		float stepBPS = bpsController.updateAndGetBPS(AudioConstants.STEP_DURATION);
		
		int startOfTheSixteenth = (int) sixteenthProgress;
		float sixteenthIncrementInTimeStep = sixteenthIncrementInTimeStep(stepBPS);
		
		List<TimedMusicalInstant> activeMusicalInstants = song.getMusicalInstantsActiveNow(new TimeInstant(startOfTheSixteenth));

		List<TimedSimpleSound> activeSimpleSounds = simpleSoundMaker.makeSimpleSounds(activeMusicalInstants, stepBPS, sixteenthProgress, timeInSong);
		
		float musicVolume = mute ? 0f : getMusicVolume() * song.getAmplitude() * relativeMusicVolume;
		musicVolume *= fadeOutComeIn == null ? 1f : fadeOutComeIn.getAmplitude();
		float[] activeAudioSamples = sampler.makeSamples(activeSimpleSounds, musicVolume, AudioConstants.SAMPLES_PER_STEP, AudioConstants.STEP_DURATION, timeInSong);
		
		//Sound Effects
		List<SimpleSound> potentialSimpleSound = queuedSoundEffects.poll();
		if (potentialSimpleSound != null) {
			this.activeSoundEffect = potentialSimpleSound.stream().map(sound -> new TimedSimpleSound(0, sound)).collect(Collectors.toList());
			this.activeSoundEffectAge = 0;
		}
		if(activeSoundEffect != null) {
			float soundEffectVolume = mute ? 0f : getSoundEffectVolume() * relativeSoundEffectVolume;
			float[] soundEffectSamples = sampler.makeSamples(activeSoundEffect, soundEffectVolume, AudioConstants.SAMPLES_PER_STEP, AudioConstants.STEP_DURATION, activeSoundEffectAge);
			for (int i = 0; i < activeAudioSamples.length; i++) {
				activeAudioSamples[i] += soundEffectSamples[i];
			}
			
			this.activeSoundEffectAge += AudioConstants.STEP_DURATION;
			if(activeSoundEffectAge >= activeSoundEffect.get(0).simpleSound.getDuration()) {
				
			}
		}

		
		peakingTracker.checkForPeaking(activeAudioSamples, activeSimpleSounds.stream().map(activeSound -> activeSound.simpleSound.getId()).collect(Collectors.toSet()));
		
		audioDevice.writeSamples(activeAudioSamples, 0, AudioConstants.SAMPLES_PER_STEP);
		
		timeInSong += AudioConstants.STEP_DURATION;
		sixteenthProgress += sixteenthIncrementInTimeStep;
	}
	
	private void checkChangeSong() {
		GameSong potentialQueuedSong = queuedSong.get();
		if(potentialQueuedSong != null) {
			if(fadeOutComeIn == null) {
				fadeOutComeIn = new FadeOutComeIn(NoteDuration.SIXTEENTH.getDuration() / potentialQueuedSong.getInitialBPS());
			}
			else {
				fadeOutComeIn.update(AudioConstants.STEP_DURATION);
				if(fadeOutComeIn.isDone()) {
					this.fadeOutComeIn = null;
					this.song = queuedSong.getAndSet(null);
					this.timeInSong = 0f;
					this.sixteenthProgress = 0f;
					this.bpsController.resetBPS(potentialQueuedSong.getInitialBPS());	
				}
			}
		}

	}
	
	private float sixteenthIncrementInTimeStep(float bps) {
		return NoteDuration.QUARTER.getDurationInSixteenths() * AudioConstants.STEP_DURATION * bps;
	}
	
	public float getSixteenthProgress() {
		return sixteenthProgress;
	}

	public float getEstimatedNextSixteenthProgress() {
		return sixteenthProgress + sixteenthIncrementInTimeStep(bpsController.getBPS());
	}

	@Override
	public void dispose() {
		audioDevice.dispose();
	}	
	
	private void threadStarted() {
		Gdx.app.log("Music", "Music Thread Started");		
	}
	
	private void threadStopped() {
		Gdx.app.log("Music", "Music Thread Stopped. " + (peakingTracker.hasPeaked() ? "Peaking detected: " : "No Peaking :D"));
		if(peakingTracker.hasPeaked()) {
			for (Iterator<Entry<Set<String>, Float>> it = peakingTracker.getPeakingInstances().entrySet().iterator(); it.hasNext();) {
				Entry<Set<String>, Float> peakingEntry = it.next();
				Gdx.app.log("Music","Peak from " + peakingEntry.getKey() + ": " + peakingEntry.getValue());
			}
		}
	}
	
	public void checkPeaking() {
		System.out.println((peakingTracker.hasPeaked() ? "Peaking detected: " : "No Peaking :D"));
		if(peakingTracker.hasPeaked()) {
			for (Iterator<Entry<Set<String>, Float>> it = peakingTracker.getPeakingInstances().entrySet().iterator(); it.hasNext();) {
				Entry<Set<String>, Float> peakingEntry = it.next();
				System.out.println("Peak from " + peakingEntry.getKey() + ": " + peakingEntry.getValue());
			}
		}		
	}





}
