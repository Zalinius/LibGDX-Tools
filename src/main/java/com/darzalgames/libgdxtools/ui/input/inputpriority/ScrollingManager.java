package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darzalgames.libgdxtools.ui.input.Input;

public class ScrollingManager extends Actor {

	private float timeSinceScroll;
	private boolean hasFinishedScrolling;
	
	public ScrollingManager(InputReceiver inputReceiver) {
		this.timeSinceScroll = 0;
		this.hasFinishedScrolling = true;
		
		addListener(new InputListener() {
			@Override
			public boolean scrolled(InputEvent event, float x, float y, float scrollAmountX, float scrollAmountY) {
				// It seems the mouse wheel returns either 1 or -1, and a tablet returns any value between these two. 
				// So, I'm using a threshold of 0.1f for the tablet/touchpad, and an input delay of 0.15f
				if (Math.abs(scrollAmountY) < 0.1f || timeSinceScroll > 0.15f) {
					hasFinishedScrolling = true;
				}

				if (Math.abs(scrollAmountY) > 0.1f && hasFinishedScrolling) {
					timeSinceScroll = 0;
					inputReceiver.processKeyInput(scrollAmountY < 0 ? Input.SCROLL_UP : Input.SCROLL_DOWN);
					hasFinishedScrolling = false;
				}
				return true;
			}
		});
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		this.timeSinceScroll += delta;
		this.getStage().setScrollFocus(this);
	}
	
}
