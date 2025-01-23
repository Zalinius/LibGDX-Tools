package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class InputPriorityStack extends ArrayDeque<InputConsumer> implements InputObserver {

	private final DarkScreen darkScreen;
	private final Stage popUpStage;

	public InputPriorityStack(Stage popUpStage) {
		this.popUpStage = popUpStage;
		clearStackAndAddBlankConsumer();

		// Set up the dark background screen that goes behind popups
		darkScreen = new DarkScreen(popUpStage, () -> {
			sendInputToTop(Input.BACK); 
		});
	}

	/**
	 * @param inputConsumer The thing to be put at the top of the input stack
	 */
	public void claimPriority(InputConsumer inputConsumer) {
		boolean thisIsDifferentFromTheTop = !isThisOnTop(inputConsumer);
		if (thisIsDifferentFromTheTop) {
			unFocusTop(peek());
			push(inputConsumer);
			focusTop(true);
		}
	}

	/**
	 * @param inputConsumer The thing that wants to release its priority, this only works if the requester is currently at the top of the stack
	 */
	public void releasePriority(InputConsumer inputConsumer) {
		if (isThisOnTop(inputConsumer)) {
			darkScreen.fadeOutAndRemove();
			releasePriorityForTop();
			darkScreen.fadeOutAndRemove();
			showDarkScreenIfLandingOnPopup();
		}
	}	

	void sendInputToTop(Input input) {
		peek().consumeKeyInput(input); 
	}

	boolean doesTopPauseGame() {
		return peek().isGamePausedWhileThisIsInFocus();
	}

	private void focusTop(boolean isFirstFocus) {
		peek().setTouchable(Touchable.enabled);
		if (isFirstFocus) {
			peek().gainFocus();				
		} else {
			peek().regainFocus();
		}
		peek().focusCurrent();
	}

	private void unFocusTop(InputConsumer top) {
		top.setTouchable(Touchable.disabled);
		top.loseFocus();
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		if (inputStrategySwitcher.shouldFlashButtons()) {
			peek().selectDefault();
		} else { // Mouse mode
			peek().clearSelected();
		}
	}


	/**
	 * Used to completely clear the current input receivers (useful when changing game screens
	 * to make sure any stray actors are TOTALLY cleared)
	 */
	public void clearChildren() {
		clearStackAndAddBlankConsumer();
		darkScreen.remove();

		popUpStage.clear();
	}

	void releasePriorityForTop() { 
		unFocusTop(pop());
		boolean isClosingPauseMenu = peek() instanceof PauseMenu; // TODO I kind of hate this but I'm not sure how else to connect these two classes
		if (isClosingPauseMenu) {
			peek().setTouchable(Touchable.enabled);
			peek().focusCurrent();
		} else {
			focusTop(false);
		}
	}
	// Comparing Actors with InputConsumers, it ain't pretty but it works.
	// I wish that Actor were an interface so that InputConsumer could extend it, but alas.
	private Tuple<Actor, PopUp> getCurrentPopup() {
		if (popUpStage.getRoot().getChildren().size > 0) {
			Actor[] popups = popUpStage.getRoot().getChildren().toArray();
			Optional<Actor> popupMatch = Arrays.stream(popups).filter(a -> {
				if (a instanceof InputConsumer) {
					return isThisOnTop((InputConsumer)a);
				}
				return false;
			}).findFirst(); 
			if (popupMatch.isPresent()) {
				// We are landing back on to a popup
				Actor actor = popupMatch.get();
				PopUp popUp = (PopUp)actor;
				return new Tuple<>(actor, popUp);
			}
		}
		return null;
	}

	boolean isLandingOnPopup() {
		return getCurrentPopup() != null;
	}

	private boolean showDarkScreenIfLandingOnPopup() {
		if (isLandingOnPopup()) {
			Tuple<Actor, PopUp> actorPopUp = getCurrentPopup();
			Actor actor = actorPopUp.e;
			PopUp popUp = actorPopUp.f;
			darkScreen.fadeIn(actor.getZIndex(), popUp.canDismiss());
		}
		return false;
	}

	/**
	 * A pop up should request this when claiming input priority, since they're handled a bit differently
	 * @param <A>
	 * @param popup
	 */
	public <A extends Actor & PopUp> void showPopup(A popup) {
		darkScreen.remove();
		popUpStage.addActor(popup);
		popup.toFront();
		popup.addBackClickListenerIfCanDismiss();
		darkScreen.fadeIn(popup.getZIndex(), popup.canDismiss());
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

	private boolean isThisOnTop(InputConsumer inputConsumer) {
		return inputConsumer.equals(peek());
	}

	private void clearStackAndAddBlankConsumer() {
		clear();
		add(new InputConsumer() {
			@Override public void consumeKeyInput(Input input) {/*not needed*/}
			@Override public void setTouchable(Touchable isTouchable)  {/*not needed*/}
			@Override public void focusCurrent()  {/*not needed*/}
			@Override public void clearSelected()  {/*not needed*/}
			@Override public void selectDefault()  {/*not needed*/}
			@Override public void loseFocus() {/*not needed*/}
		});
	}

	// The compiler insists that we have one of these for proper de/serialization, it generated this number. (Not that this class should ever be serialized...)
	private static final long serialVersionUID = 8021674885789936648L;
}
