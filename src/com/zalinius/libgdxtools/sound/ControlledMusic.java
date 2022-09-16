package com.zalinius.libgdxtools.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.zalinius.libgdxtools.preferencemanagers.SoundPreferenceManager;

public class ControlledMusic extends Actor {

	private Music music;
	private final float fadeTime;
	private final SoundPreferenceManager soundPreferenceManager;

	//Since music is game specific, we don't set it in the constructor
	public ControlledMusic(final float fadeTime, final SoundPreferenceManager soundPreferenceManager) {
		this.fadeTime = fadeTime;
		this.soundPreferenceManager = soundPreferenceManager;
	}

	public ControlledMusic(final SoundPreferenceManager soundPreferenceManager) {
		this.fadeTime = 0.75f;
		this.soundPreferenceManager = soundPreferenceManager;
	}

	public void setMusic(final Music musicToUse) {
		TemporalAction fadeIn = new TemporalAction(fadeTime) {
			@Override
			protected void update(final float percent) {
				music.setVolume(percent * soundPreferenceManager.getMaxMusicVol());
			}
		};

		Action createMusic = new Action() {
			@Override
			public boolean act(final float delta) {
				music = musicToUse;
				music.setLooping(true);
				music.play();
				return true;
			}
		};

		SequenceAction createAndFadeIn = new SequenceAction(createMusic, fadeIn);

		if (music != null ) {

			TemporalAction fadeOut = new TemporalAction(fadeTime) {
				@Override
				protected void update(final float percent) {
					music.setVolume((1 - percent) * soundPreferenceManager.getMaxMusicVol());
				}
			};

			Action disposeOldMusic = new Action() {
				@Override
				public boolean act(final float delta) {
					ControlledMusic.this.dispose();
					return true;
				}
			};

			this.addAction(new SequenceAction(fadeOut, disposeOldMusic, createAndFadeIn));
		} else {
			this.addAction(createAndFadeIn);
		}
	}

	public void setMuted(final boolean isMuted) {
		soundPreferenceManager.setMusicMuted(isMuted);
		music.setVolume(soundPreferenceManager.getMaxMusicVol());
	}

	public void dispose() {
		music.dispose();
	}
}