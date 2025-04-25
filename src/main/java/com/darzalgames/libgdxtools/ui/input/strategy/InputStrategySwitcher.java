package com.darzalgames.libgdxtools.ui.input.strategy;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputObserver;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputSubject;

/**
 * Tracks the current input strategy, and handles switching between them as needed.
 */
public class InputStrategySwitcher extends Actor implements InputStrategy, InputSubject, DoesNotPause {

	private InputStrategy currentInputStrategy;
	private InputStrategy previousInputStrategy;
	private final InputStrategy mouseInputStrategy;
	private final InputStrategy keyboardAndGamepadInputStrategy;

	public InputStrategySwitcher() {
		mouseInputStrategy = new MouseInputStrategy();
		keyboardAndGamepadInputStrategy = new KeyboardAndGamepadInputStrategy();

		observers = new ArrayList<>();
		setToMouseStrategy();
	}

	/**
	 * Switch to the keyboard/gamepad strategy, if it's not already the current strategy
	 */
	public void setToKeyboardAndGamepadStrategy() {
		if (currentInputStrategy != keyboardAndGamepadInputStrategy) {
			changeStrategy(keyboardAndGamepadInputStrategy);
		}
	}

	/**
	 * Switch to the mouse strategy, if it's not already the current strategy
	 */
	public void setToMouseStrategy() {
		if (currentInputStrategy != mouseInputStrategy) {
			changeStrategy(mouseInputStrategy);
		}
	}

	private void changeStrategy(InputStrategy newStrategy) {
		currentInputStrategy = newStrategy;
		notifyObservers();
	}


	private final List<InputObserver> observers;
	@Override
	public void register(InputObserver obj) {
		observers.add(obj);
		// Let the new observer know right away what the current input strategy is, since they pretty much always need to know!
		// e.g. The stage needs to know on initialization that everything starts in mouse mode; a newly created input-sensitive label uses the current strategy, etc.
		obj.inputStrategyChanged(this);
	}
	@Override
	public void unregister(InputObserver obj) {
		observers.remove(obj);
	}
	@Override
	public void notifyObservers() {
		List<InputObserver> toRemove = observers.stream().filter(InputObserver::shouldBeUnregistered).toList();
		observers.removeAll(toRemove);
		observers.stream().forEach(observer -> observer.inputStrategyChanged(this));
	}

	@Override
	public boolean shouldFlashButtons() {
		return currentInputStrategy.shouldFlashButtons();
	}

	@Override
	public boolean isMouseMode() {
		return currentInputStrategy.isMouseMode();
	}

	/**
	 * To be used before a call to {@link InputStrategySwitcher#revertToPreviousStrategy()}
	 */
	public void saveCurrentStrategy() {
		previousInputStrategy = currentInputStrategy;
	}

	/**
	 * NOTE: before messing around with something that changes modes, it's essential to call {@link InputStrategySwitcher#saveCurrentStrategy()}
	 * <p>
	 * We revert to the previous input strategy after a very short delay.
	 * This is used when swapping between full screen and windowed mode,
	 * because doing so moves the mouse and can take you out of gamepad/keyboard mode.
	 * The delay is necessary because the mouse movement will only be processed in the next frame.
	 */
	public void revertToPreviousStrategy() {
		if (previousInputStrategy != null) {
			DelayAction delayAction = new DelayAction(0.2f);
			RunnableActionBest resetInputAction = new RunnableActionBest(() -> {
				if (InputStrategySwitcher.this.previousInputStrategy == InputStrategySwitcher.this.keyboardAndGamepadInputStrategy) {
					setToKeyboardAndGamepadStrategy();
				}  else {
					setToMouseStrategy();
				}
			});
			delayAction.setAction(resetInputAction);
			addAction(delayAction);
		}
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}

}