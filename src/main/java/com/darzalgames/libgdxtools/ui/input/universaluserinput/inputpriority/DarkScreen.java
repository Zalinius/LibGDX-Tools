package com.darzalgames.libgdxtools.ui.input.universaluserinput.inputpriority;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;

class DarkScreen extends Image {

	private final Stage popupStage;

	public DarkScreen(Stage popupStage, Runnable onClick) {
		super(ColorTools.getColoredTexture(new Color(0, 0, 0, 0.5f), GameInfo.getWidth(), GameInfo.getHeight()));

		this.popupStage = popupStage;

		addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				fadeOutAndRemove();
				onClick.run();
				return true;
			}
		});
	}

	void fadeIn(int actorIndex, boolean isTouchable) {
		clearActions();
		popupStage.addActor(this);
		setZIndex(actorIndex);
		// They used to be a 1 second delay here before setting the dark screen touchable, I'm not sure why.
		setTouchable(isTouchable ? Touchable.enabled : Touchable.disabled);
		addAction(Actions.fadeIn(0.25f, Interpolation.circle));
	}

	void fadeOutAndRemove() {
		clearActions();
		addAction(new InstantSequenceAction(
				Actions.fadeOut(0.25f, Interpolation.circle),
				Actions.removeActor(this)
				));
	}

}
