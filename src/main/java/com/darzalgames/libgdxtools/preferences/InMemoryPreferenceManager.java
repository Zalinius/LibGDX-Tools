package com.darzalgames.libgdxtools.preferences;

import com.darzalgames.darzalcommon.data.Coordinate;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer.ScreenMode;

public class InMemoryPreferenceManager implements PreferenceManager {

	private boolean shouldPauseGameWhenOutOfFocus;
	private final InMemorySoundPreference soundPreference;
	private final InMemoryGraphicsPreference graphicsPreference;

	public InMemoryPreferenceManager() {
		shouldPauseGameWhenOutOfFocus = true;
		soundPreference = new InMemorySoundPreference();
		graphicsPreference = new InMemoryGraphicsPreference();
	}

	@Override
	public SoundPreference sound() {
		return soundPreference;
	}

	@Override
	public GraphicsPreference graphics() {
		return graphicsPreference;
	}

	@Override
	public boolean shouldPauseGameWhenOutOfFocus() {
		return shouldPauseGameWhenOutOfFocus;
	}

	@Override
	public void setShouldPauseGameWhenOutOfFocus(boolean shouldPauseGameWhenOutOfFocus) {
		this.shouldPauseGameWhenOutOfFocus = shouldPauseGameWhenOutOfFocus;
	}

	private static class InMemorySoundPreference implements SoundPreference {

		private float musicVolume;
		private float soundVolume;
		private boolean shouldMuteWhenOutOfFocus;

		public InMemorySoundPreference() {
			musicVolume = 0.5f;
			soundVolume = 0.5f;
			shouldMuteWhenOutOfFocus = false;
		}

		@Override
		public void setMusicVolume(float volume) {
			musicVolume = volume;
		}

		@Override
		public void setSoundEffectVolume(float volume) {
			soundVolume = volume;
		}

		@Override
		public float getMusicVolume() {
			return musicVolume;
		}

		@Override
		public float getSoundEffectVolume() {
			return soundVolume;
		}

		@Override
		public void setShouldMuteSoundWhenOutOfFocus(boolean shouldMute) {
			shouldMuteWhenOutOfFocus = shouldMute;
		}

		@Override
		public boolean shouldMuteSoundWhenOutOfFocus() {
			return shouldMuteWhenOutOfFocus;
		}
	}

	private static class InMemoryGraphicsPreference implements GraphicsPreference {

		private float userInterfaceScaling;
		private ScreenMode preferredScreenMode;
		private Coordinate preferredWindowSize;

		public InMemoryGraphicsPreference() {
			userInterfaceScaling = 1f;
			preferredScreenMode = ScreenMode.WINDOWED;
			preferredWindowSize = new Coordinate(800, 450);
		}

		@Override
		public float getUserInterfaceScaling() {
			return userInterfaceScaling;
		}

		@Override
		public ScreenMode getPreferredScreenMode() {
			return preferredScreenMode;
		}

		@Override
		public Coordinate getPreferredWindowSize() {
			return preferredWindowSize;
		}

		@Override
		public void setUserInterfaceScaling(float newScaling) {
			userInterfaceScaling = newScaling;
		}

		@Override
		public void setPreferredScreenMode(ScreenMode preferredScreenMode) {
			this.preferredScreenMode = preferredScreenMode;
		}

		@Override
		public void setPreferredWindowSize(Coordinate coordinate) {
			preferredWindowSize = coordinate;
		}

	}

}
