package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;

/**
 * The dark transparent screen which goes behind popups to "focus" them, and dismiss them when clicked
 */
class DarkScreen extends Image {

	private final Stage popupStage;

	public DarkScreen(Stage popupStage, Runnable onClick) {
		super(ColorTools.getColoredTexture(new Color(0, 0, 0, 0.5f), 1, 1));
		this.setSize(GameInfo.getWidth()*50, GameInfo.getHeight()*50);
		UserInterfaceFactory.makeActorCentered(this);

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

	void fadeIn(Actor actorPopup, boolean isTouchable) {
		clearActions();
		popupStage.addActor(this);
		int actorIndex = actorPopup.getZIndex();
		setZIndex(actorIndex);
		actorPopup.setZIndex(actorIndex+1);
		// There used to be a 1 second delay here before setting the dark screen touchable, I'm not sure why.
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

	@Override
	public String toString() {
		return "Dark screen behind popups";
	}

}
