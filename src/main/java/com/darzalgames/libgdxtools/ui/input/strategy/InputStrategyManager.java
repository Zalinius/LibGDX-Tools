package com.darzalgames.libgdxtools.ui.input.strategy;

import java.util.ArrayList;
import java.util.List;import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.InputObserver;
import com.darzalgames.libgdxtools.ui.input.InputSubject;

/**
 * Tracks the current input strategy, and handles switching between them as needed.
 * @author DarZal
 */
public class InputStrategyManager extends Actor implements InputStrategy, InputSubject, DoesNotPause {

	protected InputStrategy currentInputStrategy;
	protected InputStrategy previousInputStrategy;
	protected final InputStrategy mouseInputStrategy;
	protected final InputStrategy keyboardInputStrategy;

	public InputStrategyManager(InputStrategy mouseInputStrategy, InputStrategy keyboardInputStrategy) {
		this.mouseInputStrategy = mouseInputStrategy;
		this.keyboardInputStrategy = keyboardInputStrategy;

		observers = new ArrayList<>();
		setToMouseStrategy();
	}

	/**
	 * Switch to the keyboard strategy, if it's not already the current strategy
	 * @return Whether or not the change happened
	 */
	public boolean setToKeyboardStrategy() {
		if (currentInputStrategy != keyboardInputStrategy) {
			changeStrategy(keyboardInputStrategy);
			return true;
		}
		return false;
	}

	/**
	 * Switch to the mouse strategy, if it's not already the current strategy
	 * @return Whether or not the change happened
	 */
	public boolean setToMouseStrategy() {
		if (currentInputStrategy != mouseInputStrategy) {
			changeStrategy(mouseInputStrategy);
			return true;
		}
		return false;
	}

	private void changeStrategy(InputStrategy newStrategy) {
		currentInputStrategy = newStrategy;
		notifyObservers();
	}


	private List<InputObserver> observers;
	@Override
	public void register(InputObserver obj) {
		observers.add(obj);
	}
	@Override
	public void unregister(InputObserver obj) {
		observers.remove(obj);		
	}
	@Override
	public void notifyObservers() {
		List<InputObserver> toRemove = observers.stream().filter(InputObserver::shouldBeUnregistered).collect(Collectors.toList());
		observers.removeAll(toRemove);
		observers.stream().forEach(observer -> observer.inputStrategyChanged(this));
	}

	@Override
	public boolean shouldFlashButtons() {
		return currentInputStrategy.shouldFlashButtons();
	}

	@Override
	public boolean showMouseExclusiveUI() {
		return currentInputStrategy.showMouseExclusiveUI();
	}
	
	/**
	 * To be used before a call to {@link InputStrategyManager#revertToPreviousStrategy()}
	 */
	public void saveCurrentStrategy() {
		previousInputStrategy = currentInputStrategy;
	}

	/**
	 * NOTE: before messing around with something that changes modes, it's essential to call {@link InputStrategyManager#saveCurrentStrategy()}
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
				if (InputStrategyManager.this.previousInputStrategy == InputStrategyManager.this.keyboardInputStrategy) {
					setToKeyboardStrategy();
				}  else {
					setToMouseStrategy();
				}
			});
			delayAction.setAction(resetInputAction);
			this.addAction(delayAction);
		}
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}

}
