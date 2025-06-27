package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.*;
import java.util.Map.Entry;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.maingame.MultiStage;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * Manages which input consumer (including popups) is in focus and receiving input
 */
public class InputPriorityStack implements InputStrategyObserver, InputPrioritySubject {

	private final LimitedAccessDoubleStack stack;
	private final OptionsMenu pauseMenu;
	private final DarkScreen darkScreen;

	private final List<InputPriorityObserver> inputPriorityObservers;
	private final Map<String, StageLikeRenderable> stageLikeRenderables;

	public InputPriorityStack(List<StageLikeRenderable> stages, OptionsMenu optionsMenu) {
		pauseMenu = optionsMenu;
		stageLikeRenderables = new HashMap<>();
		stages.forEach(stage -> stageLikeRenderables.put(stage.getName(), stage));
		inputPriorityObservers = new ArrayList<>();
		stack = new LimitedAccessDoubleStack(stages);
		clearStackAndPushBlankConsumer();

		darkScreen = new DarkScreen(() -> sendInputToTop(Input.BACK));

		InputPriority.setInputPriorityStack(this);
	}

	void claimPriority(InputConsumer inputConsumer, String stageLikeRenderableName) {
		boolean thisIsDifferentFromTheTop = !stack.isThisOnTop(inputConsumer, stageLikeRenderableName);
		if (thisIsDifferentFromTheTop) {
			boolean isAPopup = inputConsumer.isPopUp();
			if (isAPopup) {
				claimPriorityForPopup(inputConsumer, stageLikeRenderableName);
			} else {
				claimPriorityOnStack(() -> stack.push(inputConsumer, stageLikeRenderableName));
			}
		}
	}

	private void claimPriorityForPopup(InputConsumer inputConsumer, String nameOfStageLikeRenderable) {
		if (!stageLikeRenderables.containsKey(nameOfStageLikeRenderable)) {
			throw new IllegalArgumentException("No stage registered with name: " + nameOfStageLikeRenderable);
		}
		StageLikeRenderable stageLikeRenderable = stageLikeRenderables.get(nameOfStageLikeRenderable);
		darkScreen.remove();
		PopUp popup = inputConsumer.getPopUp();
		Actor actor = popup.getAsActor();
		stageLikeRenderable.addActor(actor);
		actor.toFront();
		darkScreen.fadeIn(actor, popup.canDismiss(), stageLikeRenderable);
		claimPriorityOnStack(() -> stack.push(inputConsumer, nameOfStageLikeRenderable));
		popup.addBackClickListenerIfCanDismiss();
	}

	private void claimPriorityOnStack(Runnable claimPriority) {
		unFocusTop();
		claimPriority.run();
		focusTop(true);
	}

	/**
	 * @param inputConsumer The thing that wants to release its priority
	 */
	void releasePriority(InputConsumer inputConsumer) {
		String nameOfStageThisConsumerIsOn = stack.getNameOfStageThisConsumerIsOn(inputConsumer);
		boolean stageExists = stageLikeRenderables.containsKey(nameOfStageThisConsumerIsOn);
		if (stageExists && stack.isThisOnTop(inputConsumer, nameOfStageThisConsumerIsOn)) {
			darkScreen.fadeOutAndRemove();
			releasePriorityForTop();
			darkScreen.fadeOutAndRemove();
			showDarkScreenIfLandingOnPopup(stageLikeRenderables.get(nameOfStageThisConsumerIsOn));
		} else {
			stack.remove(inputConsumer, nameOfStageThisConsumerIsOn);
		}
	}

	void sendInputToTop(Input input) {
		stack.getTop().consumeKeyInput(input);
	}

	boolean doesTopPauseGame() {
		return stack.getTop().isGamePausedWhileThisIsInFocus();
	}

	String getNameOfPausingStage() {
		return stack.getNameOfTopStage();
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
		boolean isClosingOptionsMenu = stack.isThisOnTop(pauseMenu, MultiStage.OPTIONS_STAGE_NAME);
		unFocusTop();
		stack.popTop();
		if (isClosingOptionsMenu) {
			stack.getTop().setTouchable(Touchable.enabled);
			stack.getTop().focusCurrent();
		} else {
			focusTop(false);
		}
	}

	private void showDarkScreenIfLandingOnPopup(StageLikeRenderable stageLikeRenderable) {
		InputConsumer currentTop = stack.getTop();
		if (currentTop != null && currentTop.isPopUp()) {
			PopUp popUp = currentTop.getPopUp();
			Actor actor = popUp.getAsActor();
			darkScreen.fadeIn(actor, popUp.canDismiss(), stageLikeRenderable);
		}
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
		stack.push(makeBlankConsumer(), MultiStage.MAIN_STAGE_NAME);
	}

	private InputConsumer makeBlankConsumer() {
		return new InputConsumer() {
			@Override public void consumeKeyInput(Input input) {/*not needed*/}
			@Override public void setTouchable(Touchable isTouchable)  {/*not needed*/}
			@Override public void focusCurrent()  {/*not needed*/}
			@Override public void clearSelected()  {/*not needed*/}
			@Override public void selectDefault()  {/*not needed*/}
			@Override public void loseFocus() {/*not needed*/}
			@Override public String toString() { return "Blank base"; }
			@Override public void resizeUI() {/*not needed*/}
		};
	}

	private class LimitedAccessDoubleStack {
		private final LinkedHashMap<String, ArrayDeque<InputConsumer>> inputConsumerStacks;

		public LimitedAccessDoubleStack(List<StageLikeRenderable> stages) {
			inputConsumerStacks = new LinkedHashMap<>();
			stages.forEach(stage -> inputConsumerStacks.put(stage.getName(), new ArrayDeque<>()));
		}

		public String getNameOfStageThisConsumerIsOn(InputConsumer inputConsumer) {
			// instead of checking that the deque contains() the value, we check equality in a specific direction (so that GameUIWrapper equals() can get called instead of basic Object's)
			Optional<Entry<String, ArrayDeque<InputConsumer>>> optional = inputConsumerStacks.entrySet().stream().filter(pair -> pair.getValue().stream().anyMatch(entry -> entry.equals(inputConsumer))).findFirst();
			if (optional.isEmpty()) {
				return "";
			}
			return optional.get().getKey();
		}

		public void remove(InputConsumer inputConsumer, String nameOfStageThisConsumerIsOn) {
			if (inputConsumerStacks.containsKey(nameOfStageThisConsumerIsOn)) {
				inputConsumerStacks.get(nameOfStageThisConsumerIsOn).remove(inputConsumer);
			}
		}

		private void push(InputConsumer inputConsumer, String nameOfStageThisConsumerIsOn) {
			notifyInputPriorityObservers();
			inputConsumerStacks.get(nameOfStageThisConsumerIsOn).push(inputConsumer);
		}

		private boolean isThisOnTop(InputConsumer inputConsumer, String nameOfStageThisConsumerIsOn) {
			if (nameOfStageThisConsumerIsOn == null) {
				return false;
			}
			InputConsumer top = getTopForStage(nameOfStageThisConsumerIsOn);
			return top != null && (inputConsumer.equals(top) || top.equals(inputConsumer));
		}

		private InputConsumer getTopForStage(String nameOfStageThisConsumerIsOn) {
			return inputConsumerStacks.get(nameOfStageThisConsumerIsOn).peek();
		}

		private String getNameOfTopStage() {
			return getNameOfStageThisConsumerIsOn(getTop());
		}

		private ArrayDeque<InputConsumer> getTopStack() {
			List<ArrayDeque<InputConsumer>> allStacks = new ArrayList<>(inputConsumerStacks.values());
			Iterator<ArrayDeque<InputConsumer>> allStacksIterator = allStacks.reversed().iterator();
			while (allStacksIterator.hasNext()) {
				ArrayDeque<InputConsumer> topStack = allStacksIterator.next();
				if (topStack.peek() != null) {
					return topStack;
				}
			}
			return inputConsumerStacks.get(MultiStage.MAIN_STAGE_NAME);
		}

		private InputConsumer getTop() {
			return getTopStack().peek();
		}

		private void popTop() {
			notifyInputPriorityObservers();
			getTopStack().pop();
		}

		private void clear() {
			inputConsumerStacks.entrySet().forEach(pair -> pair.getValue().clear());
		}

		private void resizeUI() {
			inputConsumerStacks.entrySet().forEach(pair -> pair.getValue().forEach(InputConsumer::resizeUI));
		}
	}

	// The compiler insists that we have one of these for proper de/serialization, it generated this number. (Not that this class should ever be serialized...)
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 8021674885789936648L;
}
