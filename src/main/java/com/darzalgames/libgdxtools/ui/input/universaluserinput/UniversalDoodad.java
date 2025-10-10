package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pools;
import com.darzalgames.libgdxtools.maingame.StageBest;
import com.darzalgames.libgdxtools.ui.CenterActor;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.DoodadBackgroundImage;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.SkinManager;

public abstract class UniversalDoodad extends Table implements VisibleInputConsumer {

	private ButtonStyle style;
	private boolean disabled;
	private final ClickListener clickListener;
	private final InputStrategySwitcher inputStrategySwitcher;
	private final Image background;

	protected UniversalDoodad(ButtonStyle buttonStyle, InputStrategySwitcher inputStrategySwitcher) {
		this.inputStrategySwitcher = inputStrategySwitcher;
		setStyle(buttonStyle);
		clickListener = new ClickListener() {
			private final float SCALE_TIME = 0.1f;
			private final float SCALE_SIZE = 0.05f;
			private float startScaleX;
			private float startScaleY;
			private Action currentScaleAction;

			@Override
			public void clicked(InputEvent event, float x, float y) {
				justPressed();
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				// TODO focus like this when selecting default in a menu
				super.enter(event, x, y, pointer, fromActor);
				if (StageBest.isHoverEvent(pointer) && UniversalDoodad.this.isTouchable()) {
					startScaleX = background.getScaleX();
					startScaleY = background.getScaleY();
					currentScaleAction = Actions.scaleBy(SCALE_SIZE, SCALE_SIZE, SCALE_TIME);
					background.addAction(currentScaleAction);
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				if (StageBest.isHoverEvent(pointer) && currentScaleAction != null) {
					background.removeAction(currentScaleAction);
					currentScaleAction = Actions.scaleTo(startScaleX, startScaleY, SCALE_TIME);
					background.addAction(currentScaleAction);
				}
			}
		};
		addListener(clickListener);
		background = new DoodadBackgroundImage();
		background.setFillParent(true);
	}

	@Override
	public Actor getView() {
		return this;
	}

	public ButtonStyle getStyle() {
		return style;
	}

	public void setStyle(ButtonStyle buttonStyle) {
		style = buttonStyle;
		setBackground(style.down); // sizes the button, very important
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public boolean isOver() {
		return clickListener.isOver();
	}

	protected void justPressed() {
		// let subclasses define this if needed
	}

	@Override
	public void setTouchable(Touchable touchable) {
		if (Touchable.childrenOnly.equals(touchable)) {
			touchable = Touchable.enabled;
		}
		super.setTouchable(touchable);
	}

	@Override
	public void setFocused(boolean isFocused) {
		setFocused(isFocused, false);
	}

	/**
	 * Sets this button un/focused, generating a mimicked LibGDX mouse enter/exit event
	 * @param isFocused true if it should be focused, false for unfocusing
	 * @param forced    whether or not to force the focus event (they're not normally sent when in mouse mode)
	 */
	public void setFocused(boolean isFocused, boolean forced) {
		InputEvent event = Pools.obtain(InputEvent.class);
		if (!isFocused) {
			event.setType(InputEvent.Type.exit);
		} else if (!inputStrategySwitcher.isMouseMode() || forced) {
			event.setType(InputEvent.Type.enter);
		} else {
			event.setType(null); // Since the events are pooled I think they can come with a type?! (the type of the last event it was used for?)
		}

		if (event.getType() != null) {
			event.setStage(getStage());
			Vector2 localToStageCoordinates = localToStageCoordinates(new Vector2(0, 0));
			event.setStageX(localToStageCoordinates.x);
			event.setStageY(localToStageCoordinates.y);
			event.setPointer(-1);
			fire(event);
			Pools.free(event);
		}
	}

	@Override
	public void resizeUI() {
		invalidate();
		pack();
		invalidateHierarchy();
	}

	@Override
	public void gainFocus() {
		setFocused(true);
	}

	@Override
	public void loseFocus() {
		setFocused(false);
	}

	@Override
	public void focusCurrent() {
		setFocused(true);
	}

	@Override
	public void clearSelected() {
		setFocused(false);
	}

	@Override
	public void selectDefault() { /* A basic doodad doesn't have any nested components to select */ }

	// ----------------- \/ VISUAL STYLING \/ ----------------- //
	/** Returns appropriate background drawable from the style based on the current button state. */
	protected @Null Drawable getBackgroundDrawable() {
		if (isDisabled() && style.disabled != null) {
			return style.disabled;
		}
		if (isPressed() && (style.down != null)) {
			return style.down;
		}
		if (isOver() && (style.over != null)) {
			return style.over;
		}
		boolean focused = hasKeyboardFocus();
		if (focused && style.focused != null) {
			return style.focused;
		}
		return style.up;
	}

	// TODO re-copy both of these functions from Button for checkboxes using isChecked

	@Override
	public void draw(Batch batch, float parentAlpha) {
		validate();

		background.setDrawable(getBackgroundDrawable());
		addActor(background);
		background.toBack();
		CenterActor.centerActorOnParent(background);

		Color labelColor = getColorBasedOnFocus();
		colorOtherComponentsBasedOnFocus(labelColor);

		float offsetX = 0;
		float offsetY = 0;
		if (isPressed() && !isDisabled()) {
			offsetX = style.pressedOffsetX;
			offsetY = style.pressedOffsetY;
		} else {
			offsetX = style.unpressedOffsetX;
			offsetY = style.unpressedOffsetY;
		}
		boolean offset = offsetX != 0 || offsetY != 0;

		Array<Actor> children = getChildren();
		if (offset) {
			for (int i = 0; i < children.size; i++) {
				children.get(i).moveBy(offsetX, offsetY);
			}
		}
		super.draw(batch, parentAlpha);
		if (offset) {
			for (int i = 0; i < children.size; i++) {
				children.get(i).moveBy(-offsetX, -offsetY);
			}
		}

		Stage stage = getStage();
		if (stage != null && stage.getActionsRequestRendering() && isPressed() != clickListener.isPressed()) {
			Gdx.graphics.requestRendering();
		}
	}

	public void colorOtherComponentsBasedOnFocus(Color color) {/* No special inner buttons or whatnot to color by default */}

	private boolean isPressed() {
		return clickListener.isVisualPressed();
	}

	private Color getColorBasedOnFocus() {
		Color textColor = SkinManager.getDefaultColor();
		if (isDisabled()) {
			textColor = SkinManager.getDisabledColor();
		} else if (isPressed()) {
			textColor = SkinManager.getDarkColor();
		}
		return textColor;
	}

}
