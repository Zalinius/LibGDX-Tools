package com.darzalgames.libgdxtools.ui.input.popup;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;

public interface PopUp extends InputConsumer {

	/**
	 * @return Whether or not the popup can be dismissed by pressing "back", or if one of the options must be chosen
	 */
	public default boolean canDismiss() {
		return true;
	}

	/**
	 * Handles hiding the pop up and unregistering it from the input system
	 */
	public default void hideThis() {}

	@Override
	public default boolean isGamePausedWhileThisIsInFocus() {
		return true;
	}

	public default void addBackClickListenerIfCanDismiss() {
		if (canDismiss()) {
			ClickListener rightClickBack = new ClickListener(Buttons.RIGHT) {
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					PopUp.this.removeListener(this);
					PopUp.this.consumeKeyInput(Input.BACK);
				}
			};
			addListener(rightClickBack);
		}
	}

	// These are implemented by default in Actor, which all of our PopUp objects are.
	public boolean addListener(EventListener listener);
	public boolean removeListener(EventListener listener);
	

	@Override
	default Tuple<Actor, PopUp> getIfPopUp() {
		return new Tuple<>(getAsActor(), this);
	}
	public Actor getAsActor();
}
