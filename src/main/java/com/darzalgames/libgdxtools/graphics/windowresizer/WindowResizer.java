package com.darzalgames.libgdxtools.graphics.windowresizer;

import com.darzalgames.darzalcommon.data.Coordinate;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardSelectBox;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public abstract class WindowResizer {

	protected enum ScreenMode {
		FULLSCREEN,
		BORDERLESS,
		WINDOWED,
	}

	protected static final String SCREEN_MODE_KEY = "screenMode";

	protected ScreenMode currentScreenMode;
	private ScreenMode previousScreenMode;

	public abstract void setScreenSize(Coordinate size);
	protected abstract void switchToWindowed();
	protected abstract void switchToBorderless();
	protected abstract void switchToFullScreen();

	protected KeyboardSelectBox modeSelectBox;
	private InputStrategyManager inputStrategyManager;
	private WindowResizerSelectBox windowResizerSelectBox;

	/**
	 * Initialize the WindowResizer, setting the window to the preferred mode based on the user's history
	 * We do this in initialize() rather than a constructor because this object is created at the same time as the entire game,
	 * and so LibGDX isn't ready to do any resizing yet
	 */
	public void initialize(InputStrategyManager inputStrategyManager, WindowResizerSelectBox windowResizerSelectBox) {
		this.inputStrategyManager = inputStrategyManager;
		this.windowResizerSelectBox = windowResizerSelectBox;
		WindowResizerSelectBox.setWindowResizer(this);
		String preferredModeString = GameInfo.getPreferenceManager().other().getStringPrefValue(SCREEN_MODE_KEY, ScreenMode.BORDERLESS.name());
		setMode(windowResizerSelectBox.getModeFromPreference(preferredModeString), false);
		previousScreenMode = currentScreenMode;
	}

	/**
	 * @return A {@link KeyboardButton} which, when pressed, opens a {@link KeyboardSelectBox select box} for changing the window mode
	 */
	public KeyboardButton getModeSelectBox() {
		return windowResizerSelectBox;
	}

	/**
	 * Toggle between windowed mode and true full screen mode
	 */
	public void toggleWindow() {
		if (isWindowed()) {
			setMode(ScreenMode.FULLSCREEN, false);
		} else {
			setMode(ScreenMode.WINDOWED, false);
		}
	}


	protected void setMode(ScreenMode screenMode, boolean offerToRevert) {
		previousScreenMode = currentScreenMode;
		currentScreenMode = screenMode;
		GameInfo.getPreferenceManager().other().setStringPrefValue(SCREEN_MODE_KEY, currentScreenMode.name());

		inputStrategyManager.saveCurrentStrategy();
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
		inputStrategyManager.revertToPreviousStrategy();
		windowResizerSelectBox.setSelectedInBox(currentScreenMode);
		if (offerToRevert) {
			InputPriorityManager.claimPriority(windowResizerSelectBox.getRevertMenu());
		}
	}

	/**
	 * @return Whether or not the game is currently in windowed mode ("borderless windowed" returns false for this)
	 */
	public boolean isWindowed() {
		return currentScreenMode.equals(ScreenMode.WINDOWED);
	}

	public void revertMode() {
		WindowResizerSelectBox.windowResizer.setMode(previousScreenMode, false);
		previousScreenMode = currentScreenMode;
	}

}
