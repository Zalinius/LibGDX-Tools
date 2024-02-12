package com.darzalgames.libgdxtools.graphics.windowresizer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer.ScreenMode;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.save.OSType;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantRepeatAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.UserInterfaceFactory;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class WindowResizerTextSelectBox extends WindowResizerSelectBox {

	private static Function<ScreenMode, String> windowModeOptionTranslator = mode -> TextSupplier.getLine(mode.name().toLowerCase()); 
	private Label revertCountdown;

	public WindowResizerTextSelectBox(TextButton textButton, InputStrategyManager inputStrategyManager) {
		super(getEntries(), textButton, getAction(), inputStrategyManager);
	}
	
	private static Consumer<String> getAction() {
		return selectedNewMode -> {
			String previousMode = GameInfo.getPreferenceManager().other().getStringPrefValue(WindowResizer.SCREEN_MODE_KEY);
			if (!selectedNewMode.equalsIgnoreCase(previousMode)) {
				windowResizer.setMode(getModeFromPreferenceString(selectedNewMode), true);
			}
		};
	}
	
	private static Collection<String> getEntries() {
		List<ScreenMode> allModes = Arrays.asList(ScreenMode.values());
		if (OSType.getOperatingSystemType().equals(OSType.LINUX)) {
			allModes.remove(ScreenMode.BORDERLESS);
		}
		return allModes.stream().map(mode -> windowModeOptionTranslator.apply(mode)).toList();
	}

	@Override
	public void setSelectedInBox(ScreenMode screenMode) {
		this.setSelected(windowModeOptionTranslator.apply(screenMode));			
	}

	@Override
	protected ScreenMode getModeFromPreference(String screenMode) {
		return getModeFromPreferenceString(screenMode);
	}
	
	private static ScreenMode getModeFromPreferenceString(String screenMode) {
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
	protected ConfirmationMenu getRevertMenu() {
		return new WindowRevertCountdownConfirmationMenu();
	}

	private class WindowRevertCountdownConfirmationMenu extends ConfirmationMenu {

		private WindowRevertCountdownConfirmationMenu() {
			super("screen_mode_accept", 
					"accept_control",
					"revert_message",
					() -> revertCountdown.clearActions());
		}

		@Override
		public boolean canDismiss() {
			return false;
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
			WindowResizerSelectBox.windowResizer.revertMode();
		}
	}
}
