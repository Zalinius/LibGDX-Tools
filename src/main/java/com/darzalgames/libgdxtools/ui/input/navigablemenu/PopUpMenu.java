package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.function.Consumer;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalButton;

/**
 * It's a navigable menu, and it's a pop up!
 */
public interface PopUpMenu extends PopUp, VisibleInputConsumer {

	// TODO just merge all this into PopUp interface

	float SLIDE_DURATION = 0.25f;

	default Runnable getJustBeforeRemoveRunnable() {
		return Runnables.nullRunnable();
	}

	void setUpDesiredSize();

	default UniversalButton makeDefaultBackButton() {
		return GameInfo.getUserInterfaceFactory().makeBackButton(this::hideThis);
	}

	default UniversalButton makeCustomFinalButton(String finalButtonMessageKey) {
		return GameInfo.getUserInterfaceFactory().makeTextButton(() -> TextSupplier.getLine(finalButtonMessageKey), this::hideThis, Input.BACK);
	}

	default boolean slidesInAndOut() {
		return true;
	}

	default void slideIn(Runnable superGainFocus, Runnable menuFocusCurrent) {
		superGainFocus.run();
		if (slidesInAndOut()) {
			float startX = getAsActor().getX();
			float startY = getAsActor().getY();
			getAsActor().setY(UserInterfaceSizer.getCurrentHeight());
			getAsActor().addAction(
					new InstantSequenceAction(
							Actions.moveTo(startX, startY, SLIDE_DURATION, Interpolation.circle),
							Actions.touchable(Touchable.enabled),
							Actions.run(menuFocusCurrent)
					)
			);
		}
	}

	@Override
	default void hideThis() {
		releasePriority();
		if (slidesInAndOut()) {
			getAsActor().addAction(
					Actions.sequence(
							Actions.moveTo(getAsActor().getX(), UserInterfaceSizer.getCurrentHeight(), SLIDE_DURATION, Interpolation.circle),
							new RunnableActionBest(getJustBeforeRemoveRunnable()),
							new RunnableActionBest(getAsActor()::remove)
					)
			);

			// continue to resize ui as the popup slides out
			getAsActor().addAction(new TemporalAction(SLIDE_DURATION) {
				@Override
				protected void update(float percent) {
					resizeUI();
				}
			});
			getAsActor().toFront();
		} else {
			getAsActor().remove();
		}
	}

	default void possiblyDismiss(Input input, Consumer<Input> superConsumeKeyInput) {
		if (canDismiss() && input == Input.PAUSE) {
			input = Input.BACK;
		}
		superConsumeKeyInput.accept(input);
	}

	@Override
	default void resizeUI() {
		setUpDesiredSize();
		resizeUI();
	}
}
