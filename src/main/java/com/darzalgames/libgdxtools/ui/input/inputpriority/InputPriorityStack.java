package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.ArrayDeque;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * Manages which input consumer (including popups) is in focus and receiving input
 */
public class InputPriorityStack implements InputObserver {

	private final LimitedAccessDoubleStack stack;
	private final DarkScreen darkScreen;
	private final Stage popUpStage;
	private final Supplier<Boolean> isPauseMenuOpen;

	public InputPriorityStack(Stage popUpStage, Supplier<Boolean> isPauseMenuOpen) {
		this.popUpStage = popUpStage;
		this.isPauseMenuOpen = isPauseMenuOpen;
		stack = new LimitedAccessDoubleStack();
		clearStackAndPushBlankConsumer();

		darkScreen = new DarkScreen(popUpStage, () -> {
			sendInputToTop(Input.BACK); 
		});
		
		Priority.setInputPriorityStack(this);
	}

	void claimPriority(InputConsumer inputConsumer) {
		boolean thisIsDifferentFromTheTop = !stack.isThisOnTop(inputConsumer);
		if (thisIsDifferentFromTheTop) {
			Tuple<Actor, PopUp> popup = inputConsumer.getIfPopUp();
			boolean isAPopup = popup != null;
			if (isAPopup) {
				claimPriorityForPopup(popup);
			} else {
				claimPriorityOnStack(inputConsumer, () -> stack.push(inputConsumer));
			}
		}
	}

	private void claimPriorityForPopup(Tuple<Actor, PopUp> actorPopup) {
		darkScreen.remove();
		Actor actor = actorPopup.e;
		PopUp popup = actorPopup.f;
		popUpStage.addActor(actor);
		actor.toFront();
		darkScreen.fadeIn(actor.getZIndex(), popup.canDismiss());
		claimPriorityOnStack(popup, () -> stack.push(new Tuple<>(actor, popup)));
		popup.addBackClickListenerIfCanDismiss();
	}

	private void claimPriorityOnStack(InputConsumer inputConsumer, Runnable claimPriority) {
		unFocusTop();
		claimPriority.run();
		focusTop(true);
	}

	/**
	 * @param inputConsumer The thing that wants to release its priority, this only works if the requester is currently at the top of the stack
	 */
	void releasePriority(InputConsumer inputConsumer) {
		if (stack.isThisOnTop(inputConsumer)) {
			darkScreen.fadeOutAndRemove();
			releasePriorityForTop();
			darkScreen.fadeOutAndRemove();
			showDarkScreenIfLandingOnPopup();
		}
	}	

	void sendInputToTop(Input input) {
		stack.getTop().consumeKeyInput(input); 
	}

	boolean doesTopPauseGame() {
		return stack.getTop().isGamePausedWhileThisIsInFocus();
	}

	private void focusTop(boolean isFirstFocus) {
		stack.getTop().setTouchable(Touchable.enabled);
		if (isFirstFocus) {
			stack.getTop().gainFocus();				
		} else {
			stack.getTop().regainFocus();
		}
		stack.getTop().focusCurrent();
	}

	private void unFocusTop() {
		InputConsumer top = stack.getTop();
		top.setTouchable(Touchable.disabled);
		top.loseFocus();
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		if (inputStrategySwitcher.shouldFlashButtons()) {
			stack.getTop().selectDefault();
		} else { // Mouse mode
			stack.getTop().clearSelected();
		}
	}

	/**
	 * Used to completely clear the current input consumers (useful when changing game screens to make sure any stray actors are TOTALLY cleared)
	 */
	public void clearChildren() {
		clearStackAndPushBlankConsumer();
		darkScreen.remove();
		popUpStage.clear();
	}

	private void releasePriorityForTop() { 
		unFocusTop();
		boolean isClosingPauseMenu = isPauseMenuOpen.get();
		stack.popTop();
		if (isClosingPauseMenu) {
			stack.getTop().setTouchable(Touchable.enabled);
			stack.getTop().focusCurrent();
		} else {
			focusTop(false);
		}
	}

	private boolean showDarkScreenIfLandingOnPopup() {
		if (stack.isLandingOnPopup()) {
			Tuple<Actor, PopUp> actorPopUp = stack.getCurrentPopup();
			Actor actor = actorPopUp.e;
			PopUp popUp = actorPopUp.f;
			darkScreen.fadeIn(actor.getZIndex(), popUp.canDismiss());
		}
		return false;
	}

	/**
	 * To be used in exceptional circumstances only (e.g. better visuals for an in-engine trailer)
	 */
	public void hideDarkScreenForVeryExceptionalCircumstances() {
		darkScreen.remove();
	}

	@Override
	public boolean shouldBeUnregistered() {
		return false;
	}



	private void clearStackAndPushBlankConsumer() {
		stack.clear();
		stack.push(new InputConsumer() {
			@Override public void consumeKeyInput(Input input) {/*not needed*/}
			@Override public void setTouchable(Touchable isTouchable)  {/*not needed*/}
			@Override public void focusCurrent()  {/*not needed*/}
			@Override public void clearSelected()  {/*not needed*/}
			@Override public void selectDefault()  {/*not needed*/}
			@Override public void loseFocus() {/*not needed*/}
			@Override public String toString() { return "Blank base"; }
		});
	}

	private class LimitedAccessDoubleStack {
		private final ArrayDeque<InputConsumer> inputConsumerStack;
		private final ArrayDeque<Tuple<Actor, PopUp>> popupInputConsumerStack;

		public LimitedAccessDoubleStack() {
			inputConsumerStack = new ArrayDeque<>();
			popupInputConsumerStack = new ArrayDeque<>();
		}

		private void push(InputConsumer inputConsumer) {
			inputConsumerStack.push(inputConsumer);
		}

		private void push(Tuple<Actor, PopUp> actorPopup) {
			Actor actor = actorPopup.e;
			PopUp popup = actorPopup.f;
			popupInputConsumerStack.push(new Tuple<>(actor, popup));
		}

		private boolean isThisOnTop(InputConsumer inputConsumer) {
			return inputConsumer.equals(getTop());
		}

		private InputConsumer getTop() {
			if (!popupInputConsumerStack.isEmpty()) {
				return popupInputConsumerStack.peek().f;
			}
			return inputConsumerStack.peek();
		}

		private void popTop() {
			if (!popupInputConsumerStack.isEmpty()) {
				popupInputConsumerStack.pop();
			} else {
				inputConsumerStack.pop();
			}
		}

		private boolean isLandingOnPopup() {
			return !popupInputConsumerStack.isEmpty();
		}

		private Tuple<Actor, PopUp> getCurrentPopup() {
			return popupInputConsumerStack.peek();
		}

		private void clear() {
			inputConsumerStack.clear();
			popupInputConsumerStack.clear();
		}
	}

	// The compiler insists that we have one of these for proper de/serialization, it generated this number. (Not that this class should ever be serialized...)
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 8021674885789936648L;
}
