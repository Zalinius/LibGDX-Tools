package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * Manages which input consumer (including popups) is in focus and receiving input
 */
public class InputPriorityStack implements InputStrategyObserver, InputPrioritySubject {

	private final LimitedAccessDoubleStack stack;
	private final DarkScreen darkScreen;
	private final Stage popUpStage;
	private final OptionsMenu optionsMenu;

	private final List<InputPriorityObserver> inputPriorityObservers;

	public InputPriorityStack(Stage popUpStage, OptionsMenu optionsMenu) {
		this.popUpStage = popUpStage;
		this.optionsMenu = optionsMenu;
		inputPriorityObservers = new ArrayList<>();
		stack = new LimitedAccessDoubleStack();
		clearStackAndPushBlankConsumer();

		darkScreen = new DarkScreen(popUpStage, () -> sendInputToTop(Input.BACK));

		InputPriority.setInputPriorityStack(this);
	}

	void claimPriority(InputConsumer inputConsumer) {
		boolean thisIsDifferentFromTheTop = !stack.isThisOnTop(inputConsumer);
		if (thisIsDifferentFromTheTop) {
			boolean isAPopup = inputConsumer.isPopUp();
			if (isAPopup) {
				claimPriorityForPopup(inputConsumer);
			} else {
				claimPriorityOnStack(() -> stack.push(inputConsumer));
			}
		}
	}

	private void claimPriorityForPopup(InputConsumer inputConsumer) {
		darkScreen.remove();
		PopUp popup = inputConsumer.getPopUp();
		Actor actor = popup.getAsActor();
		popUpStage.addActor(actor);
		actor.toFront();
		darkScreen.fadeIn(actor, popup.canDismiss());
		claimPriorityOnStack(() -> stack.pushPopup(inputConsumer));
		popup.addBackClickListenerIfCanDismiss();
	}

	private void claimPriorityOnStack(Runnable claimPriority) {
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
		if (isFirstFocus) {
			stack.getTop().gainFocus();
		} else {
			stack.getTop().regainFocus();
		}
		stack.getTop().setTouchable(Touchable.enabled);
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
	}

	private void releasePriorityForTop() {
		boolean isClosingOptionsMenu = stack.isThisOnTop(optionsMenu);
		unFocusTop();
		stack.popTop();
		if (isClosingOptionsMenu) {
			stack.getTop().setTouchable(Touchable.enabled);
			stack.getTop().focusCurrent();
		} else {
			focusTop(false);
		}
	}

	private boolean showDarkScreenIfLandingOnPopup() {
		if (stack.isLandingOnPopup()) {
			InputConsumer actorPopUp = stack.getCurrentPopup();
			PopUp popUp = actorPopUp.getPopUp();
			Actor actor = popUp.getAsActor();
			darkScreen.fadeIn(actor, popUp.canDismiss());
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

	public void resizeStackUI() {
		stack.resizeUI();
	}

	@Override
	public void register(InputPriorityObserver observer) {
		inputPriorityObservers.add(observer);
	}

	@Override
	public void notifyInputPriorityObservers() {
		inputPriorityObservers.stream().forEach(InputPriorityObserver::inputPriorityAboutToChange);
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
			@Override public void resizeUI() {/*not needed*/}
		});
	}

	private class LimitedAccessDoubleStack {
		private final ArrayDeque<InputConsumer> inputConsumerStack;
		private final ArrayDeque<InputConsumer> popupInputConsumerStack;

		public LimitedAccessDoubleStack() {
			inputConsumerStack = new ArrayDeque<>();
			popupInputConsumerStack = new ArrayDeque<>();
		}

		private void push(InputConsumer inputConsumer) {
			notifyInputPriorityObservers();
			inputConsumerStack.push(inputConsumer);
		}

		private void pushPopup(InputConsumer inputConsumer) {
			notifyInputPriorityObservers();
			popupInputConsumerStack.push(inputConsumer);
		}

		private boolean isThisOnTop(InputConsumer inputConsumer) {
			return inputConsumer.equals(getTop()) || getTop().equals(inputConsumer);
		}

		private InputConsumer getTop() {
			if (!popupInputConsumerStack.isEmpty()) {
				return popupInputConsumerStack.peek();
			}
			return inputConsumerStack.peek();
		}

		private void popTop() {
			notifyInputPriorityObservers();
			if (!popupInputConsumerStack.isEmpty()) {
				popupInputConsumerStack.pop();
			} else {
				inputConsumerStack.pop();
			}
		}

		private boolean isLandingOnPopup() {
			return !popupInputConsumerStack.isEmpty();
		}

		private InputConsumer getCurrentPopup() {
			return popupInputConsumerStack.peek().getPopUp();
		}

		private void clear() {
			inputConsumerStack.clear();
			popupInputConsumerStack.clear();
		}

		private void resizeUI() {
			inputConsumerStack.forEach(InputConsumer::resizeUI);
			popupInputConsumerStack.forEach(actorPopup -> actorPopup.getPopUp().resizeUI());
		}
	}

	// The compiler insists that we have one of these for proper de/serialization, it generated this number. (Not that this class should ever be serialized...)
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 8021674885789936648L;
}
