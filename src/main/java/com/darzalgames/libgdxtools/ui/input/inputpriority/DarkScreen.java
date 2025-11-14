package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.popup.PopUpMenu;

/**
 * The dark transparent screen which goes behind popups to "focus" them, and dismiss them when clicked
 */
class DarkScreen extends Image implements DarkScreenBehindPopUp {

	public DarkScreen(Runnable onClick) {
		super(ColorTools.getColoredTexture(new Color(0, 0, 0, 0.5f), 1, 1));
		setTouchable(Touchable.disabled);
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

	@Override
	public void fadeIn(Actor actorPopup, boolean isTouchable, StageLikeRenderable stage) {
		clearActions();
		stage.addActor(this);
		UserInterfaceSizer.scaleToFillScreenAndMakeCentered(this); // Set full screen size immediately so tests can click it in the same frame
		int actorIndex = actorPopup.getZIndex();
		setZIndex(actorIndex);
		actorPopup.setZIndex(actorIndex + 1);
		addAction(
				Actions.sequence(
						Actions.parallel(
								Actions.fadeIn(0.25f, Interpolation.circle),
								Actions.delay(PopUpMenu.SLIDE_DURATION)
						),
						Actions.touchable(isTouchable ? Touchable.enabled : Touchable.disabled)
				)
		);
	}

	@Override
	public void fadeOutAndRemove() {
		clearActions();
		addAction(
				new InstantSequenceAction(
						Actions.fadeOut(0.25f, Interpolation.circle),
						Actions.removeActor(this)
				)
		);
		setTouchable(Touchable.disabled);
	}

	@Override
	public String toString() {
		return "Dark screen behind popups";
	}

}
