package com.darzalgames.libgdxtools.graphics.windowresizer;

import com.darzalgames.darzalcommon.data.Coordinate;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalSelectBox;

public abstract class WindowResizer {

	public enum ScreenMode {
		FULLSCREEN,
		BORDERLESS,
		WINDOWED,
	}


	protected ScreenMode currentScreenMode;
	private ScreenMode previousScreenMode;

	public abstract void setScreenSize(Coordinate size);
	protected abstract void switchToWindowed();
	protected abstract void switchToBorderless();
	protected abstract void switchToFullScreen();

	private InputStrategySwitcher inputStrategySwitcher;
	private WindowResizerButton windowResizerButton;

	/**
	 * Initialize the WindowResizer, setting the window to the preferred mode based on the user's history
	 * We do this in initialize() rather than a constructor because this object is created at the same time as the entire game,
	 * and so LibGDX isn't ready to do any resizing yet
	 * @param inputStrategySwitcher
	 * @param windowResizerButton
	 */
	public void initialize(InputStrategySwitcher inputStrategySwitcher, WindowResizerButton windowResizerButton) {
		this.inputStrategySwitcher = inputStrategySwitcher;
		this.windowResizerButton = windowResizerButton;
		windowResizerButton.setWindowResizer(this);
		getModeSelectBox();
		String preferredModeString = GameInfo.getPreferenceManager().graphics().getPreferredScreenMode();
		setMode(windowResizerButton.getModeFromPreference(preferredModeString), false);
		previousScreenMode = currentScreenMode;
	}

	/**
	 * @return A {@link UniversalButton} which, when pressed, opens a {@link UniversalSelectBox select box} for changing the window mode
	 */
	public UniversalButton getModeSelectBox() {
		UniversalButton button = windowResizerButton.getWindowResizerButton();
		if (currentScreenMode != null) {
			windowResizerButton.setSelectedScreenMode(currentScreenMode);
		}
		return button;
	}

	/**
	 * Toggle between windowed mode and true full screen mode
	 */
	public void toggleWindow() {
		if (isWindowed()) {
			ScreenMode bigMode = ScreenMode.BORDERLESS;
			if (!GameInfo.getGamePlatform().supportsBorderlessFullscreen()) {
				bigMode = ScreenMode.FULLSCREEN;
			}
			setMode(bigMode, true);
		} else {
			setMode(ScreenMode.WINDOWED, true);
		}
	}


	protected void setMode(ScreenMode screenMode, boolean offerToRevert) {
		previousScreenMode = currentScreenMode;
		currentScreenMode = screenMode;
		GameInfo.getPreferenceManager().graphics().setPreferredScreenMode(currentScreenMode.name());

		inputStrategySwitcher.saveCurrentStrategy();
		switch (currentScreenMode) {
		case BORDERLESS:
			switchToBorderless();
			break;
		case FULLSCREEN:
			switchToFullScreen();
			break;
		case WINDOWED:
			switchToWindowed();
			break;
		}
		offerToRevert = offerToRevert && previousScreenMode != currentScreenMode;
		inputStrategySwitcher.revertToPreviousStrategy();
		windowResizerButton.setSelectedScreenMode(currentScreenMode);
		if (offerToRevert) {
			InputPriority.claimPriority(windowResizerButton.getRevertMenu());
		}
	}

	/**
	 * @return Whether or not the game is currently in windowed mode ("borderless windowed" returns false for this)
	 */
	public boolean isWindowed() {
		return ScreenMode.WINDOWED.equals(currentScreenMode);
	}

	public void revertMode() {
		WindowResizer.this.setMode(previousScreenMode, false);
		previousScreenMode = currentScreenMode;
	}

}
