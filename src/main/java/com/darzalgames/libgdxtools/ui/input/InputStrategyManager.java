package com.darzalgames.libgdxtools.ui.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.darzalgames.darzalcommon.misc.DoesNotPause;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;

public class InputStrategyManager extends InputStrategy implements InputSubject, DoesNotPause {

	public InputStrategy currentInputStrategy;
	public InputStrategy previousInputStrategy;
	private final MouseInputStrategy mouseInputStrategy;
	private final KeyboardInputStrategy keyboardInputStrategy;
	private final PixelCursor pixelCursor;

	public InputStrategyManager(PixelCursor pixelCursor) {
		mouseInputStrategy = new MouseInputStrategy();
		keyboardInputStrategy = new KeyboardInputStrategy();
		this.pixelCursor = pixelCursor;

		observers = new ArrayList<>();
		Gdx.graphics.setSystemCursor(SystemCursor.None);
	}

	public boolean setToKeyboardStrategy() {
		pixelCursor.hide();
		if (currentInputStrategy != keyboardInputStrategy) {
			changeStrategy(keyboardInputStrategy);
			return true;
		}
		return false;
	}

	public boolean setToMouseStrategy() {
		pixelCursor.show();
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
		List<InputObserver> toRemove = observers.stream().filter(o -> o == null || o.shouldBeUnregistered()).toList();
		observers.removeAll(toRemove);
		observers.stream().forEach(InputObserver::inputStrategyChanged);
	}


	@Override
	public boolean shouldFocusFirstButton() {
		return currentInputStrategy.shouldFocusFirstButton();
	}

	@Override
	public boolean shouldFlashButtons() {
		return currentInputStrategy.shouldFlashButtons();
	}

	@Override
	public boolean showMouseExclusiveButtons() {
		return currentInputStrategy.showMouseExclusiveButtons();
	}

	@Override
	public String getRosterButtonInputHint() {
		return currentInputStrategy.getRosterButtonInputHint();
	}

	@Override
	public String getContractButtonInputHint() {
		return currentInputStrategy.getContractButtonInputHint();
	}

	public void saveCurrentStrategy() {
		previousInputStrategy = currentInputStrategy;
	}

	/**
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
