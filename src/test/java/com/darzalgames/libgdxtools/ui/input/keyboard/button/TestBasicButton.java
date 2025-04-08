package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Pools;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.BasicButton;

/**
 * A minimalist re-implementation of a LibGDX Button, but without graphics (for testing)
 */
public class TestBasicButton implements BasicButton {
	
	private boolean isOver = false;
	private boolean isChecked = false;
	private boolean isDisabled = false;
	private boolean fireProgrammaticChangeEvents = true;
	private Touchable touchable = Touchable.enabled;
	private final DelayedRemovalArray<EventListener> listeners = new DelayedRemovalArray<>(0);

	@Override
	public boolean isTouchable() {
		return touchable.equals(Touchable.enabled);
	}

	@Override
	public boolean isChecked() {
		return isChecked;
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
	public void toggle() {
		setChecked(!isChecked());
	}

	@Override
	public void setDisabled(boolean disabled) {
		isDisabled = disabled;
	}

	@Override
	public void setTouchable(Touchable touchable) {
		this.touchable = touchable;
	}

	@Override
	public void setProgrammaticChangeEvents(boolean fireProgrammaticChangeEvents) {
		this.fireProgrammaticChangeEvents = fireProgrammaticChangeEvents;
	}

	@Override
	public void setChecked(boolean checked) {
		this.isChecked = checked;

		if (fireProgrammaticChangeEvents) {
			ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
			if (fire(changeEvent)) this.isChecked = !checked;
			Pools.free(changeEvent);
		}
	}

	@Override
	public TextButtonStyle getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStyle(ButtonStyle style) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWidth(float width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getPrefWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getPrefHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSize(float prefWidth, float prefHeight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean fire(Event event) {
		if (event instanceof InputEvent) {
			InputEvent inputEvent = (InputEvent)event;
			isOver = inputEvent.getType().equals(InputEvent.Type.enter);
		}
		if (event.getStage() == null) event.setStage(getStage());
		event.setTarget(this.getView());

		// Collect ascendants so event propagation is unaffected by hierarchy changes.
		@SuppressWarnings("unchecked")
		Array<Group> ascendants = Pools.obtain(Array.class);
		Group parent = this.getView().getParent();
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
				if (event.isStopped()) return event.isCancelled();
			}

			// Notify the target listeners.
			notify(event);
			if (!event.getBubbles()) return event.isCancelled();
			if (event.isStopped()) return event.isCancelled();

			// Notify ascendants' actor listeners, starting at the target. Children may stop an event before ascendants receive it.
			for (int i = 0, n = ascendants.size; i < n; i++) {
				((Group)ascendantsArray[i]).notify(event, false);
				if (event.isStopped()) return event.isCancelled();
			}

			return event.isCancelled();
		} finally {
			ascendants.clear();
			Pools.free(ascendants);
		}
	}
	
	/** Notifies this actor's listeners of the event. The event is not propagated to any ascendants. The event
	 * {@link Event#setTarget(Actor) target} must be set before calling this method. Before notifying the listeners, this actor is
	 * set as the {@link Event#getListenerActor() listener actor}. If this actor is not in the stage, the stage must be set before
	 * calling this method.
	 * @param capture If true, the capture listeners will be notified instead of the regular listeners.
	 * @return true if the event was {@link Event#cancel() cancelled}. */
	private boolean notify (Event event) {
		if (event.getTarget() == null) throw new IllegalArgumentException("The event target cannot be null.");

		DelayedRemovalArray<EventListener> listeners = this.listeners;
		if (listeners.size == 0) return event.isCancelled();

		event.setListenerActor(this.getView());
		event.setCapture(false);
		if (event.getStage() == null) event.setStage(this.getStage());

		try {
			listeners.begin();
			for (int i = 0, n = listeners.size; i < n; i++)
				if (listeners.get(i).handle(event)) event.handle();
			listeners.end();
		} catch (RuntimeException ex) {
			String context = toString();
			throw new RuntimeException("Actor: " + context.substring(0, Math.min(context.length(), 128)), ex);
		}

		return event.isCancelled();
	}

	@Override
	public void clearActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearChildren() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAction(Action action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addListener(EventListener listener) {
		if (listener == null) throw new IllegalArgumentException("listener cannot be null.");
		if (!listeners.contains(listener, true)) {
			listeners.add(listener);
			return true;
		}
		return false;
	}

	@Override
	public <T extends Actor> Cell<T> add(T actor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Actor getView() {
		return new Actor();
	}

	@Override
	public Stage getStage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Label getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cell<Label> getLabelCell() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getButtonText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getMinHeight() {
		return 0;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addActor(Actor actor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeActor(Actor actor) {
		// TODO Auto-generated method stub
		return false;
	}

}
