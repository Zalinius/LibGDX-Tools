package com.darzalgames.libgdxtools.graphics.windowresizer;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer.ScreenMode;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantRepeatAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalSelectBox;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;

public class WindowResizerSelectBox implements WindowResizerButton {

	private static Function<ScreenMode, String> windowModeOptionTranslator = mode -> TextSupplier.getLine(mode.name().toLowerCase());
	private UniversalSelectBox selectBox;
	private final Supplier<TextButton> textButtonSupplier;
	private final InputStrategySwitcher inputStrategySwitcher;
	
	private WindowResizer windowResizer;
	@Override
	public void setWindowResizer(WindowResizer windowResizer) {
		this.windowResizer = windowResizer;
	}

	public WindowResizerSelectBox(Supplier<TextButton> textButtonSupplier, InputStrategySwitcher inputStrategySwitcher) {
		this.textButtonSupplier = textButtonSupplier;
		this.inputStrategySwitcher = inputStrategySwitcher;
	}
	
	private static Collection<String> getEntries() {
		List<ScreenMode> allModes = new ArrayList<>(Arrays.asList(ScreenMode.values()));
		if (!GameInfo.getGamePlatform().supportsBorderlessFullscreen()) {
			allModes.remove(ScreenMode.BORDERLESS);
		}
		return allModes.stream().map(mode -> windowModeOptionTranslator.apply(mode)).collect(Collectors.toList());
	}

	@Override
	public void setSelected(ScreenMode screenMode) {
		selectBox.setSelected(windowModeOptionTranslator.apply(screenMode));			
	}

	@Override
	public ScreenMode getModeFromPreference(String screenMode) {
		ScreenMode preferredMode = ScreenMode.BORDERLESS;
		for (int i = 0; i < ScreenMode.values().length; i++) {
			String translatedPref = windowModeOptionTranslator.apply(ScreenMode.values()[i]);
			if (screenMode.equalsIgnoreCase(ScreenMode.values()[i].name()) //English
					|| screenMode.equalsIgnoreCase(translatedPref)) { //French
				preferredMode = ScreenMode.values()[i];
			}
		}
		return preferredMode;
	}

	@Override
	public ConfirmationMenu getRevertMenu() {
		return new WindowRevertCountdownConfirmationMenu();
	}
	
	private class WindowRevertCountdownConfirmationMenu extends ConfirmationMenu {

		private Label revertCountdown;

		private WindowRevertCountdownConfirmationMenu() {
			super("screen_mode_accept", 
					"accept_control",
					"revert_message",
					Runnables.nullRunnable());
		}
		
		@Override
		protected void setChosenKey(String chosenKey) {
			revertCountdown.clearActions();
		}

		@Override
		protected void setUpTable() {
			super.setUpTable();
			IntFunction<String> makeCountdownString = count -> TextSupplier.getLine("screen_mode_revert", count);
			revertCountdown = UserInterfaceFactory.getFlavorTextLabel(makeCountdownString.apply(10));
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
				revertMode(); 
				hideThis();
			};
		}

		private void revertMode() {
			windowResizer.revertMode();
		}
	}

	@Override
	public UniversalButton getButton() {
		selectBox = new UniversalSelectBox(getEntries(), textButtonSupplier.get(), inputStrategySwitcher, Runnables.nullRunnable());//TODO replace null runnable?
		
		selectBox.setAction(selectedNewMode -> {
			String previousMode = GameInfo.getPreferenceManager().other().getStringPrefValue(WindowResizer.SCREEN_MODE_KEY);
			if (!selectedNewMode.equalsIgnoreCase(previousMode)) {
				windowResizer.setMode(getModeFromPreference(selectedNewMode), true);
			}
		});
		return selectBox;
	}
}
