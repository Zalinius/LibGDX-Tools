package com.zalinius.libgdxtools.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.zalinius.libgdxtools.preferencemanagers.SoundPreferenceManager;

public class ControlledMusic extends Actor {

	protected Music music;

	public ControlledMusic() {
	}

	public void setMusic(final Music musicToUse) {
		TemporalAction fadeIn = new TemporalAction(1f) {
			@Override
			protected void update(final float percent) {
				music.setVolume(percent * SoundPreferenceManager.getMaxMusicVol());
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

			TemporalAction fadeOut = new TemporalAction(0.75f) {
				@Override
				protected void update(final float percent) {
					music.setVolume((1 - percent) * SoundPreferenceManager.getMaxMusicVol());
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
		SoundPreferenceManager.setMusicMuted(isMuted);
		music.setVolume(SoundPreferenceManager.getMaxMusicVol());
	}

	public void play() {
		music.play();
	}


	public void dispose() {
		music.dispose();
	}
}