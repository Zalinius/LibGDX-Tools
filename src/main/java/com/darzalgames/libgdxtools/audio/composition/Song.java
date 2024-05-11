package com.darzalgames.libgdxtools.audio.composition;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import com.darzalgames.darzalcommon.functional.Do;
import com.darzalgames.libgdxtools.audio.composition.track.RepeatingTrack;
import com.darzalgames.libgdxtools.audio.composition.track.Track;
import com.darzalgames.libgdxtools.audio.engine.music.MusicalInstantMaker;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.time.BPSAcceptor;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;

public abstract class Song implements SongInfo, MusicalInstantMaker {
	
	private final String name;
	private final Deque<Runnable> measureRunnables;
	private final Deque<Runnable> beatRunnables;
	private final float initialBPS;
	private final BPSAcceptor bpsAcceptor;

	private final Collection<Track> tracks;

	public Song(String name, BPSAcceptor bpsAcceptor) {
		this(name, bpsAcceptor, 1f);
	}
	
	public Song(String name, BPSAcceptor bpsAcceptor, float initialBps) {
		this.name = name;
		this.tracks = new ArrayList<>();
		this.measureRunnables = new ArrayDeque<>();
		this.beatRunnables = new ArrayDeque<Runnable>();

		this.initialBPS = initialBps;
		this.bpsAcceptor = bpsAcceptor;
	}
	
	public void addTrack(Track track) {
		tracks.add(track);
	}

	protected RepeatingTrack createRepeatingTrack(Instrument instrument, String trackName) {
		RepeatingTrack track = new RepeatingTrack(instrument.synth(), instrument.envelope(), name, trackName);
		tracks.add(track);
		return track;
	}

	/**
	 * Adds a track that starts X Beats later
	 * @param instrument
	 * @param trackName
	 * @param startBeat
	 * @return
	 */
	protected RepeatingTrack createRepeatingTrack(Instrument instrument, String trackName, int startBeat) {
		RepeatingTrack track = createRepeatingTrack(instrument, trackName);
		Do.xTimes(startBeat, () -> track.addSilence(NoteDuration.QUARTER));
		track.setRepetitionPoint();
		return track;
	}
	
	@Override
	public List<TimedMusicalInstant> getMusicalInstantsActiveNow(TimeInstant time) {
		if (time.isStartOfMeasure()) {
			executeMeasureRunnables();
		}
		if(time.isStartOfBeat()) {
			executeBeatRunnables();
		}

		List<TimedMusicalInstant> musicalInstants = new ArrayList<>();

		for (Iterator<Track> it = tracks.iterator(); it.hasNext();) {
			Track track = it.next();

			musicalInstants.addAll(track.getMusicalInstantsActiveNow(time));
		}
		
		return musicalInstants;
	}

	@Override
	public int songLengthInSixteenths() {
		return tracks.stream().map(Track::trackLengthInSixteenths).max(Integer::compare).orElse(0);
	}

	/**
	 * Adds a runnable that will execute once, at the very start of the nextMeasure
	 * 
	 * @param runnable
	 */
	protected void addMeasureRunnable(Runnable runnable) {
		measureRunnables.add(runnable);
	}
	
	/**
	 * Adds a runnable that will execute once, at the very start of the nextBeat
	 * 
	 * @param runnable
	 */
	protected void addBeatRunnable(Runnable runnable) {
		beatRunnables.add(runnable);
	}
	
	private void executeMeasureRunnables() {
		while (!measureRunnables.isEmpty()) {
			Runnable runnable = measureRunnables.pop();
			runnable.run();
		}		
	}

	private void executeBeatRunnables() {
		while (!beatRunnables.isEmpty()) {
			Runnable runnable = beatRunnables.pop();
			runnable.run();
		}		
	}

	protected void setTargetBPS(float newBps) {
		setTargetBPS(newBps, 4/newBps);
	}

	protected void setTargetBPS(float newBps, float transitionTime) {
		bpsAcceptor.requestChangeBPS(newBps, transitionTime);
	}

	protected void setBPSRightNow(float newBps) {
		setTargetBPS(newBps, 0.25f/newBps);
	}

	@Override
	public float getInitialBPS() {
		return initialBPS;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getAmplitude() {
		return 1f;
	}

	public float getDecorativeBPSMultiplier() {
		return 1f;
	}






}
