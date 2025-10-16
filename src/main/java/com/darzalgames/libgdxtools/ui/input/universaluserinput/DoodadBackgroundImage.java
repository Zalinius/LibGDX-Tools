package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.darzalgames.libgdxtools.maingame.StageBest;

public class DoodadBackgroundImage extends Image {

	/**
	 * A scaled TenPatch calls a draw() overload that isn't implemented, but this seems to do the job just fine?
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		validate();

		float x = getX();
		float y = getY();
		float scaleX = getScaleX();
		float scaleY = getScaleY();
		getDrawable().draw(batch, x + getImageX(), y + getImageY(), getImageWidth() * scaleX, getImageHeight() * scaleY);
	}

	/**
	 * @param toScale         the background art to be scaled up when the doodad is in focus
	 * @param listeningDoodad the doodad to animate
	 */
	public static void addScalingClickListener(Actor toScale, UniversalDoodad listeningDoodad) {
		listeningDoodad.addListener(new ClickListener() {
			private final float SCALE_TIME = 0.1f;
			private Action currentScaleAction;

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				if (StageBest.isHoverEvent(pointer) && listeningDoodad.isTouchable() && !listeningDoodad.isDisabled()) {
					if (currentScaleAction != null) {
						toScale.removeAction(currentScaleAction);
						toScale.setScale(1);
					}
					currentScaleAction = Actions.scaleBy(listeningDoodad.getFocusScaleIncrease(), listeningDoodad.getFocusScaleIncrease(), SCALE_TIME);
					toScale.addAction(currentScaleAction);
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if (StageBest.isHoverEvent(pointer)) {
					toScale.removeAction(currentScaleAction);
					currentScaleAction = Actions.scaleTo(1, 1, SCALE_TIME);
					toScale.addAction(currentScaleAction);
				}
			}
		});
	}

	/**
	 * This is a bit of a hack: setting the background of a doodad with an invisible copy of the 'up' style defines the doodad's size,
	 * which we need to do in order to have a pulse animation on the doodad's visible background WITHOUT affecting the doodad's true size
	 * (since this ruins menu layout, jiggling around all neighboring doodads)
	 * @param doodad the doodad being given a new size
	 * @param style  the doodad's new style
	 */
	public static void setStyleOnDoodadBackground(UniversalDoodad doodad, ButtonStyle style) {
		if (style.up == null) {
			throw new IllegalArgumentException("Can't style a doodad when the 'up' style is undefined!");
		}
		doodad.setBackground(new Drawable() {

			@Override
			public void draw(Batch batch, float x, float y, float width, float height) { /* DO NOT DRAW THIS */ }

			@Override
			public float getLeftWidth() {
				return style.up.getLeftWidth();
			}

			@Override
			public void setLeftWidth(float leftWidth) { /* irrelevant */ }

			@Override
			public float getRightWidth() {
				return style.up.getRightWidth();
			}

			@Override
			public void setRightWidth(float rightWidth) { /* irrelevant */ }

			@Override
			public float getTopHeight() {
				return style.up.getTopHeight();
			}

			@Override
			public void setTopHeight(float topHeight) { /* irrelevant */ }

			@Override
			public float getBottomHeight() {
				return style.up.getBottomHeight();
			}

			@Override
			public void setBottomHeight(float bottomHeight) { /* irrelevant */ }

			@Override
			public float getMinWidth() {
				return style.up.getMinWidth();
			}

			@Override
			public void setMinWidth(float minWidth) { /* irrelevant */ }

			@Override
			public float getMinHeight() {
				return style.up.getMinHeight();
			}

			@Override
			public void setMinHeight(float minHeight) { /* irrelevant */ }
		});
	}

}
