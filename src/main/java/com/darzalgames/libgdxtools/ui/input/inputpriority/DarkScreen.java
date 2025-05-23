package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;

/**
 * The dark transparent screen which goes behind popups to "focus" them, and dismiss them when clicked
 */
class DarkScreen extends Image {

	private final Stage popupStage;

	public DarkScreen(Stage popupStage, Runnable onClick) {
		super(ColorTools.getColoredTexture(new Color(0, 0, 0, 0.5f), 1, 1));

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

	@Override
	public void draw(Batch batch, float parentAlpha) {
		UserInterfaceSizer.scaleToFillScreenAndMakeCentered(this);
		super.draw(batch, parentAlpha);
	}

	void fadeIn(Actor actorPopup, boolean isTouchable) {
		clearActions();
		popupStage.addActor(this);
		UserInterfaceSizer.scaleToFillScreenAndMakeCentered(this); // Set full screen size immediately so tests can click it in the same frame
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
