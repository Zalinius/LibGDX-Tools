package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pools;
import com.darzalgames.libgdxtools.ui.CenterActor;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.SkinManager;

public abstract class UniversalDoodad extends Table implements VisibleInputConsumer {

	private ButtonStyle style;
	private boolean disabled;
	private final ClickListener clickListener;
	private final InputStrategySwitcher inputStrategySwitcher;
	private final DoodadBackgroundImage backgroundImage;
	private float focusScaleIncrease;

	public static final float DEFAULT_FOCUS_SCALE_INCREASE = 0.05f;

	protected UniversalDoodad(ButtonStyle buttonStyle, InputStrategySwitcher inputStrategySwitcher) {
		this.inputStrategySwitcher = inputStrategySwitcher;
		setFocusScaleIncrease(DEFAULT_FOCUS_SCALE_INCREASE);
		setStyle(buttonStyle);
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				justPressed();
			}
		};
		addListener(clickListener);

		backgroundImage = new DoodadBackgroundImage();
		backgroundImage.setFillParent(true);
		DoodadBackgroundImage.addScalingClickListener(backgroundImage, this);
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
		DoodadBackgroundImage.setStyleOnDoodadBackground(this, buttonStyle); // sizes the button, very important
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

	/**
	 * Adjust how much the doodad changes size when it's in focus, something near the default value ({@value #DEFAULT_FOCUS_SCALE_INCREASE}) should be right.
	 * NOTE: this is the amount we scale BY, not the value we scale TO. So the doodad will scale to 1 + focusScaleIncrease, then back down to 1 when out of focus.
	 * @param focusScaleIncrease the amount to increase the background's size by when the doodad is in focus
	 */
	public void setFocusScaleIncrease(float focusScaleIncrease) {
		this.focusScaleIncrease = focusScaleIncrease;
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
		backgroundImage.setDrawable(getBackgroundDrawable());
		addActor(backgroundImage);

		validate();

		backgroundImage.toBack();
		CenterActor.centerActorOnParent(backgroundImage);

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

	float getFocusScaleIncrease() {
		return focusScaleIncrease;
	}

}
