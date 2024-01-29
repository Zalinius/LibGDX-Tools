package com.darzalgames.libgdxtools.ui.input.keyboard;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.libgdxtools.ui.input.InputPrioritizer;
import com.darzalgames.libgdxtools.ui.input.handler.InputHandler;

public class MouseDetector extends InputHandler {

	public MouseDetector() {
		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				setLatestInputMethod(InputMethod.MOUSE);
				InputPrioritizer.enterMouseMode();
				return false;
			}
		});
		this.setFillParent(true);
		this.setTouchable(Touchable.enabled);
	}

}
