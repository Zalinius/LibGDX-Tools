package com.darzalgames.libgdxtools.graphics.windowresizer;

import java.util.function.IntFunction;

import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.maingame.MultipleStage;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.DelayRunnableAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantRepeatAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalLabel;

public class WindowRevertCountdownConfirmationMenu extends ConfirmationMenu {

	private final WindowResizerSelectBox windowResizerSelectBox;

	public WindowRevertCountdownConfirmationMenu(WindowResizerSelectBox windowResizerSelectBox) {
		super(
				"screen_mode_accept",
				"accept_control",
				"revert_message",
				Runnables.nullRunnable(),
				MultipleStage.OPTIONS_STAGE_NAME
		);
		this.windowResizerSelectBox = windowResizerSelectBox;
	}

	@Override
	public boolean slidesInAndOut() {
		// Since the window is resized as this popup comes/goes, it's tricky to center its animation so we don't do it: it appears instantly instead of sliding in/out
		return false;
	}

	@Override
	protected void setUpTable() {
		super.setUpTable();
		IntFunction<String> makeCountdownString = count -> TextSupplier.getLine("screen_mode_revert", count);
		UniversalLabel revertCountdown = GameInfo.getUserInterfaceFactory().getFlavorTextLabel(() -> makeCountdownString.apply(10));
		revertCountdown.setAlignment(Alignment.CENTER);
		row();
		add(revertCountdown).growX();

		InstantRepeatAction repeatAction = new InstantRepeatAction();
		repeatAction.setTotalCount(11);
		DelayAction delayThenCountDownAction = new DelayRunnableAction(1, () -> revertCountdown.setTextSupplier(() -> makeCountdownString.apply(Math.max(0, repeatAction.getRemainingCount() - 1))));
		repeatAction.setAction(delayThenCountDownAction);

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
		windowResizerSelectBox.windowResizer.revertMode();
	}

}