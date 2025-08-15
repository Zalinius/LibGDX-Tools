package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Pools;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

/**
 * A minimalist re-implementation of a LibGDX Button, but without graphics (for testing)
 * I'm adding functionality as needed for testing so there are many empty methods, especially those to do with visual layout
 */
public class TestButton implements VisibleInputConsumer {

	private final Runnable pressRunnable;
	private boolean isBlank;

	protected TestButton() {
		this(Runnables.nullRunnable());
	}
	protected TestButton(Runnable pressRunnable) {
		this.pressRunnable = pressRunnable;
		setBlank(true);
	}

	private boolean isOver = false;
	private boolean isDisabled = false;
	private Touchable touchable = Touchable.enabled;
	private final DelayedRemovalArray<EventListener> listeners = new DelayedRemovalArray<>(0);

	public boolean isTouchable() {
		return Touchable.enabled.equals(touchable);
	}

	protected void setBlank(boolean isBlank) {
		this.isBlank = isBlank;
	}

	@Override
	public boolean isDisabled() {
		return isDisabled;
	}

	@Override
	public boolean isOver() {
		return isOver;
	}

	@Override
	public void setDisabled(boolean disabled) {
		isDisabled = disabled;
	}

	@Override
	public void setTouchable(Touchable touchable) {
		this.touchable = touchable;
	}


	//	@Override
	//	public void setChecked(boolean checked) {
	//		isChecked = checked;
	//
	//		if (fireProgrammaticChangeEvents) {
	//			ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
	//			if (fire(changeEvent)) {
	//				isChecked = !checked;
	//			}
	//			Pools.free(changeEvent);
	//		}
	//	}
	//
	//	@Override
	//	public void setStyle(ButtonStyle style) {
	//
	//	}

	public boolean fire(Event event) {
		if (event instanceof InputEvent inputEvent) {
			isOver = InputEvent.Type.enter.equals(inputEvent.getType());
		}

		// ---------------------------------------------------------------
		// Below here is copied from LibGDX button
		if (event.getStage() == null) {
			event.setStage(getStage());
		}
		event.setTarget(getView());

		// Collect ascendants so event propagation is unaffected by hierarchy changes.
		@SuppressWarnings("unchecked")
		Array<Group> ascendants = Pools.obtain(Array.class);
		Group parent = getView().getParent();
		while (parent != null) {
			ascendants.add(parent);
			parent = parent.getParent();
		}

		try {
			// Notify ascendants' capture listeners, starting at the root. Ascendants may stop an event before children receive it.
			Object[] ascendantsArray = ascendants.items;
			for (int i = ascendants.size - 1; i >= 0; i--) {
				Group currentTarget = (Group)ascendantsArray[i];
				currentTarget.notify(event, true);
				if (event.isStopped()) {
					return event.isCancelled();
				}
			}

			// Notify the target listeners.
			notify(event);
			if (!event.getBubbles()) {
				return event.isCancelled();
			}
			if (event.isStopped()) {
				return event.isCancelled();
			}

			// Notify ascendants' actor listeners, starting at the target. Children may stop an event before ascendants receive it.
			for (int i = 0, n = ascendants.size; i < n; i++) {
				((Group)ascendantsArray[i]).notify(event, false);
				if (event.isStopped()) {
					return event.isCancelled();
				}
			}

			return event.isCancelled();
		} finally {
			ascendants.clear();
			Pools.free(ascendants);
		}
	}

	private boolean notify (Event event) {
		// ---------------------------------------------------------------
		// Below here is copied from LibGDX button
		if (event.getTarget() == null) {
			throw new IllegalArgumentException("The event target cannot be null.");
		}

		DelayedRemovalArray<EventListener> listeners = this.listeners;
		if (listeners.size == 0) {
			return event.isCancelled();
		}

		event.setListenerActor(getView());
		event.setCapture(false);
		if (event.getStage() == null) {
			event.setStage(getStage());
		}

		try {
			listeners.begin();
			for (int i = 0, n = listeners.size; i < n; i++) {
				if (listeners.get(i).handle(event)) {
					event.handle();
				}
			}
			listeners.end();
		} catch (RuntimeException ex) {
			String context = toString();
			throw new RuntimeException("Actor: " + context.substring(0, Math.min(context.length(), 128)), ex);
		}

		return event.isCancelled();
	}

	public boolean addListener(EventListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener cannot be null.");
		}
		if (!listeners.contains(listener, true)) {
			listeners.add(listener);
			return true;
		}
		return false;
	}

	public DelayedRemovalArray<EventListener> getListeners() {
		return listeners;
	}

	public <T extends Actor> Cell<T> add(T actor) {
		return null;
	}

	@Override
	public Actor getView() {
		return new Actor();
	}

	public Stage getStage() {
		return null;
	}

	@Override
	public float getMinHeight() {
		return 0;
	}

	public float getHeight() {
		return 0;
	}

	public void addActor(Actor actor) {

	}

	public boolean removeActor(Actor actor) {
		return false;
	}

	@Override
	public boolean isBlank() {
		return isBlank;
	}

	@Override
	public void setAlignment(Alignment alignment) {
		// TODO Auto-generated method stub

	}
	@Override
	public void consumeKeyInput(Input input) {
		if (input.equals(Input.ACCEPT)) {
			pressRunnable.run();
		}
	}
	@Override
	public void focusCurrent() {
		// TODO Auto-generated method stub

	}
	@Override
	public void clearSelected() {
		// TODO Auto-generated method stub

	}
	@Override
	public void selectDefault() {
		// TODO Auto-generated method stub

	}
	@Override
	public void setFocused(boolean focused) {
		isOver = focused;
	}
	@Override
	public void resizeUI() {
		// TODO Auto-generated method stub

	}

}
