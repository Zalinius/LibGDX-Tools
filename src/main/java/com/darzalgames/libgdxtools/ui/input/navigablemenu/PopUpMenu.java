package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.function.Consumer;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
 * It's everything a navigable menu needs to become a pop up!
 */
public interface PopUpMenu extends VisibleInputConsumer {

	float SLIDE_DURATION = 0.25f;

	default void addBackClickListenerIfCanDismiss() {
		if (canDismiss()) {
			ClickListener rightClickBack = new ClickListener(Buttons.RIGHT) {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					PopUpMenu.this.removeListener(this);
					PopUpMenu.this.consumeKeyInput(Input.BACK);
				}
			};
			addListener(rightClickBack);
		}
	}

	// These are implemented by default in Actor, which all of our PopUp objects are.
	boolean addListener(EventListener listener);

	boolean removeListener(EventListener listener);

	/**
	 * @return Whether or not the popup can be dismissed by pressing "back", or if one of the options must be chosen
	 */
	default boolean canDismiss() {
		return true;
	}

	@Override
	default boolean isGamePausedWhileThisIsInFocus() {
		return true;
	}

	@Override
	default boolean isPopUp() {
		return true;
	}

	@Override
	default PopUpMenu getPopUp() {
		return this;
	}

	Actor getAsActor();

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
			setTouchable(Touchable.disabled);
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

	/**
	 * Handles hiding the pop up and unregistering it from the input system
	 */
	default void hideThis() {
		releasePriority();
		if (slidesInAndOut()) {
			setTouchable(Touchable.disabled);
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
