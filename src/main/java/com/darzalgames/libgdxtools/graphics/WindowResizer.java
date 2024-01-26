package com.darzalgames.libgdxtools.graphics;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.data.Coordinate;
import com.darzalgames.libgdxtools.MainGame;
import com.darzalgames.libgdxtools.i18n.TextSupplier;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantRepeatAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.InputPrioritizer;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardSelectBox;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.LabelMaker;

public abstract class WindowResizer {

	protected enum ScreenMode {
		FULLSCREEN,
		BORDERLESS,
		WINDOWED,
	}

	private static final String SCREEN_MODE_KEY = "screenMode";

	private ScreenMode currentScreenMode;
	private ScreenMode previousScreenMode;

	protected abstract void switchToWindowed();
	protected abstract void switchToBorderless();
	protected abstract void switchToFullScreen();
	public abstract void setScreenSize(Coordinate size);

	private KeyboardSelectBox modeSelectBox;
	private Label revertCountdown;

	Function<ScreenMode, String> windowModeOptionTranslator = mode -> TextSupplier.getLine(mode.name().toLowerCase()); 

	public void initialize() {
		String preferredModeString = MainGame.getPreferenceManager().other().getStringPrefValue(SCREEN_MODE_KEY, ScreenMode.BORDERLESS.name());
		setMode(preferredModeString, false);
		previousScreenMode = currentScreenMode;
	}

	public void toggleWindow() {
		if (isWindowed()) {
			setMode(ScreenMode.FULLSCREEN, false);
		} else {
			setMode(ScreenMode.WINDOWED, false);
		}
	}


	private void setMode(String screenMode, boolean offerToRevert) {
		ScreenMode preferredMode = ScreenMode.BORDERLESS;
		for (int i = 0; i < ScreenMode.values().length; i++) {
			if (screenMode.equalsIgnoreCase(ScreenMode.values()[i].name()) //English
					|| screenMode.equalsIgnoreCase(windowModeOptionTranslator.apply(ScreenMode.values()[i]))) { //French
				preferredMode = ScreenMode.values()[i];
			}
		}
		setMode(preferredMode, offerToRevert);
	}

	private void setMode(ScreenMode screenMode, boolean offerToRevert) {
		previousScreenMode = currentScreenMode;
		currentScreenMode = screenMode;
		MainGame.getPreferenceManager().other().setStringPrefValue(SCREEN_MODE_KEY, currentScreenMode.name());

		MainGame.getInputStrategyManager().saveCurrentStrategy();
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
		MainGame.getInputStrategyManager().revertToPreviousStrategy();
		setCurrentlySelectedInBox();
		if (offerToRevert) {

			ConfirmationMenu reverter = new ConfirmationMenu("screen_mode_accept", 
					"accept_control",
					"revert_message",
					() -> revertCountdown.clearActions()) {

				@Override
				public boolean canDismiss() {
					return false;
				}

				@Override
				protected void setUpTable() {
					super.setUpTable();
					IntFunction<String> makeCountdownString = count -> TextSupplier.getLine("screen_mode_revert", count);
					revertCountdown = LabelMaker.getFlavorTextLabel(makeCountdownString.apply(10));
					revertCountdown.setAlignment(Align.center);
					row();
					add(revertCountdown).growX();

					InstantRepeatAction repeatAction = new InstantRepeatAction();
					repeatAction.setTotalCount(10);
					DelayAction delayAction = new DelayAction(1);
					delayAction.setAction(new RunnableActionBest(() -> revertCountdown.setText(makeCountdownString.apply(repeatAction.getRemainingCount() -1))));
					repeatAction.setAction(delayAction);

					SequenceAction sequenceAction = new SequenceAction(repeatAction, new RunnableActionBest(getSecondChoiceRunnable()));

					revertCountdown.addAction(sequenceAction);
				}

				@Override
				protected Runnable getSecondChoiceRunnable() {
					return () -> { 
						WindowResizer.this.revertMode(); 
						hideThis();
					};
				}
			};

			InputPrioritizer.claimPriority(reverter);
		}
	}

	private void revertMode() {
		setMode(previousScreenMode, false);
		previousScreenMode = currentScreenMode;
	}

	public boolean isWindowed() {
		return currentScreenMode.equals(ScreenMode.WINDOWED);
	}

	public KeyboardButton getModeSelectBox() {
		List<String> availableModes = Arrays.asList(ScreenMode.values()).stream().map(mode -> windowModeOptionTranslator.apply(mode)).toList();
		if (SaveManager.getOperatingSystemType().equals(SaveManager.OSType.LINUX)) {
			availableModes = Arrays.asList(ScreenMode.values()).stream().filter(mode -> !mode.equals(ScreenMode.BORDERLESS)).map(mode -> windowModeOptionTranslator.apply(mode)).toList();
		}
		Supplier<String> windowModeLabelSupplier = () -> (TextSupplier.getLine("window_mode_label"));
		modeSelectBox = LabelMaker.getSelectBox(
				windowModeLabelSupplier.get(),
				availableModes,
				selectedNewMode -> {
					String previousMode = MainGame.getPreferenceManager().other().getStringPrefValue(SCREEN_MODE_KEY);
					if (!selectedNewMode.equalsIgnoreCase(previousMode)) {
						setMode(selectedNewMode, true);
						MainGame.getPreferenceManager().other().setStringPrefValue(SCREEN_MODE_KEY, currentScreenMode.name());
					}
				}
				);
		setCurrentlySelectedInBox();
		return modeSelectBox;
	}

	private void setCurrentlySelectedInBox() {
		if (modeSelectBox != null) {
			modeSelectBox.setSelected(windowModeOptionTranslator.apply(currentScreenMode));			
		}
	}
}
