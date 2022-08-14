package com.zalinius.libgdxtools.tooltip;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Null;

public abstract class TooltipActor extends Image {

	public TooltipActor(final Texture tex) {
		super(tex);
		setUpInputListener();
	}

	protected void setUpInputListener() {
		addListener(new InputListener() {
			@Override
			public void enter(final InputEvent event, final float x, final float y, final int pointer,
					@Null final Actor fromActor) {
				getTooltipRunnable().run();
			}

			@Override
			public void exit(final InputEvent event, final float x, final float y, final int pointer,
					@Null final Actor toActor) {
				if(pointer != -1) {
					return;
				}
				
				Tooltip.closeDisplay();
			}
		});
	}
	
	protected abstract Runnable getTooltipRunnable();
	
}
