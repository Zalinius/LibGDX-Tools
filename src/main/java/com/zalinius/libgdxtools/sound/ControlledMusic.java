package com.zalinius.libgdxtools.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.zalinius.libgdxtools.preferencemanagers.SoundPreference;

public class ControlledMusic extends Actor {

	private Music currentMusic;
	private final float fadeTime;
	private final SoundPreference soundPreferenceManager;

	//Since music is game specific, we don't set it in the constructor
	public ControlledMusic(final float fadeTime, final SoundPreference soundPreferenceManager) {
		this.fadeTime = fadeTime;
		this.soundPreferenceManager = soundPreferenceManager;
	}

	public ControlledMusic(final SoundPreference soundPreferenceManager) {
		this.fadeTime = 0.75f;
		this.soundPreferenceManager = soundPreferenceManager;
	}


	public float getMusicVolume() {
		return soundPreferenceManager.getMusicVolume();
	}

	public void setMusicVolume(float newVolume) {
		soundPreferenceManager.setMusicVolume(newVolume);
		currentMusic.setVolume(newVolume);
	}

	public void setMusic(final Music musicToUse) {
		setMusic(musicToUse, true);
	}

	public void setMusic(final Music musicToUse, boolean shouldFade) {
		TemporalAction fadeIn = new TemporalAction(fadeTime) {
			@Override
			protected void update(final float percent) {
				currentMusic.setVolume(percent * getMusicVolume());
			}
		};

		Action createMusic = new Action() {
			@Override
			public boolean act(final float delta) {
				createAndStartMusic(musicToUse);
				return true;
			}
		};

		SequenceAction createAndFadeIn = new SequenceAction(createMusic, fadeIn);

		TemporalAction fadeOut = new TemporalAction(fadeTime) {
			@Override
			protected void update(final float percent) {
				currentMusic.setVolume((1 - percent) * getMusicVolume());
			}
		};

		Action disposeOldMusic = new Action() {
			@Override
			public boolean act(final float delta) {
				ControlledMusic.this.dispose();
				return true;
			}
		};

		if (shouldFade) {
			if (currentMusic != null) {
				this.addAction(new SequenceAction(fadeOut, disposeOldMusic, createAndFadeIn));
			} else {
				this.addAction(new SequenceAction(disposeOldMusic, createAndFadeIn));
			}
		} else {
			if (currentMusic != null) {
				this.dispose();
			}
			createAndStartMusic(musicToUse);
			currentMusic.setVolume(getMusicVolume());
		}
	}
	
	private void createAndStartMusic(Music musicToUse) {
		currentMusic = musicToUse;
		currentMusic.play();
	}

	public void dispose() {
		currentMusic.dispose();
	}
}