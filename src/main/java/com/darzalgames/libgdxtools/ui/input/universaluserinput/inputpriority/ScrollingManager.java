package com.darzalgames.libgdxtools.ui.input.universaluserinput.inputpriority;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.darzalgames.libgdxtools.ui.input.Input;

public class ScrollingManager extends Actor {

	private float timeSinceScroll;
	private boolean hasFinishedScrolling;
	
	private final Consumer<Input> processScrollingInput;
	
	public ScrollingManager(Consumer<Input> processScrollingInput) {
		this.timeSinceScroll = 0;
		this.hasFinishedScrolling = true;
		this.processScrollingInput = processScrollingInput;
		
		addAction(Actions.forever(new Action() {
			@Override
			public boolean act(float delta) {
				ScrollingManager.this.timeSinceScroll += delta;
				return false;
			}
		}));
	}

	/**
	 * This can handle scrolling both from a mouse wheel or something more like a tablet or touchpad.
	 * @param amount The amount of scrolling on the y-axis
	 */
	void receiveScrollInput(float amount) {
		// It seems the mouse wheel returns either 1 or -1, and a tablet returns any value between these two. 
		// So, I'm using a threshold of 0.1f for the tablet/touchpad, and an input delay of 0.15f
		if (Math.abs(amount) < 0.1f || timeSinceScroll > 0.15f) {
			hasFinishedScrolling = true;
		}

		if (Math.abs(amount) > 0.1f && hasFinishedScrolling) {
			timeSinceScroll = 0;
			processScrollingInput.accept(amount < 0 ? Input.SCROLL_UP : Input.SCROLL_DOWN);
			hasFinishedScrolling = false;
		}
	}
}
