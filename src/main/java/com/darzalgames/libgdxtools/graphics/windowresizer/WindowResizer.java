package com.darzalgames.libgdxtools.graphics.windowresizer;

import com.darzalgames.darzalcommon.data.Coordinate;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.maingame.MultipleStage;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableLayout;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

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
	private WindowResizerSelectBox windowResizerButton;
	private NavigableLayout revertConfirmationMenu;

	/**
	 * Initialize the WindowResizer, setting the window to the preferred mode based on the user's history
	 */
	public void initialize(InputStrategySwitcher inputStrategySwitcher) {
		this.inputStrategySwitcher = inputStrategySwitcher;
	}

	public void setWindowResizerButton(WindowResizerSelectBox windowResizerButton) {
		this.windowResizerButton = windowResizerButton;
		windowResizerButton.setWindowResizer(this);
		windowResizerButton.setSelectedScreenMode(currentScreenMode);
	}

	/**
	 * Toggle between windowed mode and true full screen mode
	 */
	public void toggleWindow() {
		if (!isToggleConfirmationInProgress()) {
			if (isWindowed()) {
				ScreenMode bigMode = ScreenMode.BORDERLESS;
				if (!GameInfo.getOperatingSystem().supportsBorderlessFullscreen()) {
					bigMode = ScreenMode.FULLSCREEN;
				}
				setMode(bigMode, true);
			} else {
				setMode(ScreenMode.WINDOWED, true);
			}
		}
	}

	protected void setMode(ScreenMode screenMode, boolean offerToRevert) {
		previousScreenMode = currentScreenMode;
		currentScreenMode = screenMode;
		GameInfo.getPreferenceManager().graphics().setPreferredScreenMode(currentScreenMode);

		inputStrategySwitcher.saveCurrentStrategy();
		switch (currentScreenMode) {
		case BORDERLESS -> switchToBorderless();
		case FULLSCREEN -> switchToFullScreen();
		case WINDOWED -> switchToWindowed();
		}

		offerToRevert = offerToRevert && previousScreenMode != currentScreenMode;
		inputStrategySwitcher.revertToPreviousStrategy();
		windowResizerButton.setSelectedScreenMode(currentScreenMode);
		if (offerToRevert) {
			revertConfirmationMenu = windowResizerButton.getRevertMenu();
			InputPriority.claimPriority(revertConfirmationMenu, MultipleStage.OPTIONS_STAGE_NAME);
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

	public void setModeFromPreferences() {
		currentScreenMode = GameInfo.getPreferenceManager().graphics().getPreferredScreenMode();
		switch (currentScreenMode) {
		case BORDERLESS -> switchToBorderless();
		case FULLSCREEN -> switchToFullScreen();
		case WINDOWED -> switchToWindowed();
		}
		previousScreenMode = currentScreenMode;
	}

	public void cancelToggle() {
		if (isToggleConfirmationInProgress()) {
			revertConfirmationMenu.consumeKeyInput(Input.BACK);
		}
	}

	public boolean isToggleConfirmationInProgress() {
		return revertConfirmationMenu != null && revertConfirmationMenu.getStage() != null;
	}
}
