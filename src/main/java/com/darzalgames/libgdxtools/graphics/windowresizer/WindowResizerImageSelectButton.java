package com.darzalgames.libgdxtools.graphics.windowresizer;

import java.util.function.IntFunction;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantRepeatAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.UserInterfaceFactory;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public abstract class WindowResizerImageSelectButton extends KeyboardButton implements WindowResizerButton {

	private WindowResizer windowResizer;

	protected WindowResizerImageSelectButton(TextButton button, Image image, Runnable runnable,
			InputStrategyManager inputStrategyManager) {
		super(button, image, runnable, inputStrategyManager);
	}

	@Override
	public void setWindowResizer(WindowResizer windowResizer) {
		this.windowResizer = windowResizer;
	}

	@Override
	public KeyboardButton getButton() {
		return this;
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

}
