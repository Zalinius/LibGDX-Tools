package com.darzalgames.libgdxtools.ui.input.inputpriority;

import java.util.*;
import java.util.Map.Entry;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.maingame.MultipleStage;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * Manages which input consumer (including popups) is in focus and receiving input
 */
public class InputPriorityStack implements InputStrategyObserver, InputPrioritySubject {

	private final LimitedAccessMultiStack multiStack;
	private final OptionsMenu optionsMenu;
	private final DarkScreen darkScreen;

	private final InputStrategySwitcher inputStrategySwitcher;
	private final List<InputPriorityObserver> inputPriorityObservers;
	private final Map<String, StageLikeRenderable> stageLikeRenderables;

	public InputPriorityStack(List<StageLikeRenderable> allStagesInOrderForInput, OptionsMenu optionsMenu, InputStrategySwitcher inputStrategySwitcher) {
		this.optionsMenu = optionsMenu;

		stageLikeRenderables = new HashMap<>();
		allStagesInOrderForInput.forEach(stage -> stageLikeRenderables.put(stage.getName(), stage));

		inputPriorityObservers = new ArrayList<>();

		multiStack = new LimitedAccessMultiStack(allStagesInOrderForInput);
		clearStackAndPushBlankConsumer();

		darkScreen = new DarkScreen(() -> sendInputToTop(Input.BACK));

		InputPriority.setInputPriorityStack(this);

		this.inputStrategySwitcher = inputStrategySwitcher;
		inputStrategySwitcher.register(this);
	}

	void claimPriority(InputConsumer inputConsumer, String stageLikeRenderableName) {
		boolean thisIsDifferentFromTheTop = !multiStack.isThisOnTop(inputConsumer, stageLikeRenderableName);
		if (thisIsDifferentFromTheTop) {
			boolean isAPopup = inputConsumer.isPopUp();
			if (isAPopup) {
				claimPriorityForPopup(inputConsumer, stageLikeRenderableName);
			} else {
				claimPriorityOnStack(() -> multiStack.push(inputConsumer, stageLikeRenderableName));
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
		claimPriorityOnStack(() -> multiStack.push(inputConsumer, nameOfStageLikeRenderable));
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
		String nameOfStageThisConsumerIsOn = multiStack.getNameOfStageThisConsumerIsOn(inputConsumer);
		boolean stageExists = stageLikeRenderables.containsKey(nameOfStageThisConsumerIsOn);
		if (stageExists && multiStack.isThisOnTop(inputConsumer, nameOfStageThisConsumerIsOn)) {
			darkScreen.fadeOutAndRemove();
			releasePriorityForTop();
			darkScreen.fadeOutAndRemove();
			showDarkScreenIfLandingOnPopup(stageLikeRenderables.get(multiStack.getNameOfTopStage()));
		} else {
			multiStack.remove(inputConsumer, nameOfStageThisConsumerIsOn);
		}
	}

	void sendInputToTop(Input input) {
		multiStack.getTop().consumeKeyInput(input);
	}

	boolean doesTopPauseGame() {
		return multiStack.getTop().isGamePausedWhileThisIsInFocus();
	}

	String getNameOfPausingStage() {
		if (!doesTopPauseGame()) {
			return "";
		}
		return multiStack.getNameOfTopStage();
	}

	private void focusTop(boolean isFirstFocus) {
		if (isFirstFocus) {
			multiStack.getTop().gainFocus();
		} else {
			multiStack.getTop().regainFocus();
		}
		multiStack.getTop().setTouchable(Touchable.enabled);
			multiStack.getTop().focusCurrent();
		}

	private void unFocusTop() {
		InputConsumer top = multiStack.getTop();
		top.setTouchable(Touchable.disabled);
		top.loseFocus();
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		if (inputStrategySwitcher.isMouseMode()) {
			multiStack.getTop().clearSelected();
		} else {
			multiStack.getTop().selectDefault();
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
		boolean isClosingOptionsMenu = multiStack.isThisOnTop(optionsMenu, MultipleStage.OPTIONS_STAGE_NAME);
		unFocusTop();
		multiStack.popTop();
		if (isClosingOptionsMenu) {
			multiStack.getTop().setTouchable(Touchable.enabled);
				multiStack.getTop().focusCurrent();
		} else {
			focusTop(false);
		}
	}

	private void showDarkScreenIfLandingOnPopup(StageLikeRenderable stageLikeRenderable) {
		InputConsumer currentTop = multiStack.getTop();
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
		multiStack.resizeUI();
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
		multiStack.clear();
		multiStack.push(makeBlankConsumer(), MultipleStage.MAIN_STAGE_NAME);
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
			@Override public boolean isDisabled() { return false; }
			@Override public boolean isBlank() { return true; }
			@Override public void setAlignment(Alignment alignment) {/*not needed*/}
			@Override public void setFocused(boolean focused) {/*not needed*/}
			@Override public void setDisabled(boolean disabled) {/*not needed*/}
		};
	}

	private class LimitedAccessMultiStack {
		private final Map<String, ArrayDeque<InputConsumer>> inputConsumerStacks;

		public LimitedAccessMultiStack(List<StageLikeRenderable> allStagesInOrderForInput) {
			inputConsumerStacks = new LinkedHashMap<>();
			allStagesInOrderForInput.forEach(stage -> inputConsumerStacks.put(stage.getName(), new ArrayDeque<>()));
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
			return inputConsumerStacks.get(MultipleStage.MAIN_STAGE_NAME);
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

}
