package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
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
import com.darzalgames.zalaudiolibrary.sfx.SoundEffect;

public abstract class UniversalDoodad extends Table implements VisibleInputConsumer {

	private ButtonStyle style;
	private boolean disabled;
	private final ClickListener clickListener;
	private final BooleanSupplier isKeyboardMode;

	private final DoodadBackgroundImage scalingBackgroundImage;
	protected final DoodadContentsTable doodadContents;
	private float focusScaleIncrease;

	private final Consumer<SoundEffect> soundEffectConsumer;
	private SoundEffect soundEffect;

	public static final float DEFAULT_FOCUS_SCALE_INCREASE = 0.05f;

	protected UniversalDoodad(ButtonStyle buttonStyle, InputStrategySwitcher inputStrategySwitcher, Consumer<SoundEffect> soundEffectConsumer, SoundEffect soundEffect) {
		this.soundEffectConsumer = soundEffectConsumer;
		this.soundEffect = soundEffect;
		isKeyboardMode = () -> !inputStrategySwitcher.isMouseMode();

		doodadContents = new DoodadContentsTable();
		addDoodadContentsToThis();

		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				justPressed();
			}
		};
		addListener(clickListener);

		scalingBackgroundImage = new DoodadBackgroundImage();
		setFocusScaleIncrease(DEFAULT_FOCUS_SCALE_INCREASE);
		DoodadBackgroundImage.addScalingClickListener(scalingBackgroundImage, this);

		setStyle(buttonStyle);
	}

	public ButtonStyle getStyle() {
		return style;
	}

	public void setStyle(ButtonStyle buttonStyle) {
		style = buttonStyle;
		DoodadBackgroundImage.setStyleOnDoodadBackground(doodadContents, buttonStyle); // sizes the button, very important
		scalingBackgroundImage.setDrawable(getBackgroundDrawable());
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isOver() {
		return clickListener.isOver();
	}

	protected void justPressed() {
		// let subclasses define this if needed
	}

	@Override
	public void setTouchable(Touchable touchable) {
		if (Touchable.childrenOnly.equals(touchable)) {
			super.setTouchable(touchable);
			touchable = Touchable.enabled;
		}
		if (doodadContents != null) {
			// doodadContents is null when called during construction
			doodadContents.setTouchable(touchable);
			scalingBackgroundImage.setTouchable(Touchable.disabled);
		}
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
		doodadContents.setFocused(isFocused, forced);
	}

	/**
	 * Called every frame, objects should resize their UI in case the window/font have changed size<br>
	 * <br>
	 * If this doodad isn't being sized externally by a Table or some other Layout, then in order for the
	 * doodad to know its own size, you must call {@link #pack} after this. (Useful if you're positioning othe
	 * "floating" UI elements relative to this doodad, such as input glyphs or labels)
	 */
	@Override
	public void resizeUI() {
		float horizontalPadding = getWidth() * focusScaleIncrease * 0.5f;
		float verticalPadding = getHeight() * focusScaleIncrease * 0.5f;
		padLeft(horizontalPadding).padRight(horizontalPadding).padTop(verticalPadding).padBottom(verticalPadding);

		scalingBackgroundImage.setDrawable(getBackgroundDrawable());
		scalingBackgroundImage.setSize(doodadContents.getWidth(), doodadContents.getHeight());

		CenterActor.centerActorOnParent(doodadContents);
		doodadContents.addActor(scalingBackgroundImage);
		scalingBackgroundImage.toBack();
		CenterActor.centerActorOnParent(scalingBackgroundImage);
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

	/**
	 * This will play this UniversalDoodad's sound effect
	 */
	protected void requestInteractSound() {
		soundEffectConsumer.accept(soundEffect);
	}

	/**
	 * Sets the sound effect for this UniversalDoodad
	 * @param soundEffect a sound effect that will be played whenever requestInteractSound() is called
	 */
	public void setSoundEffect(SoundEffect soundEffect) {
		this.soundEffect = soundEffect;
	}

	private boolean isPressed() {
		return clickListener.isVisualPressed();
	}

	private void addDoodadContentsToThis() {
		// we call super.add() since this class overrides add() to put Actors into the inner doodadContents Table
		// but the doodadContents have to get into this somehow!
		super.add(doodadContents).grow();
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

	@Override
	public Cell<Actor> add(Actor actor) {
		return doodadContents.add(actor);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Cell row() {
		return doodadContents.row();
	}

	@Override
	public boolean addListener(EventListener listener) {
		return doodadContents.addListener(listener);
	}

	@Override
	public boolean removeListener(EventListener listener) {
		return doodadContents.removeListener(listener);
	}

	@Override
	public float getPrefHeight() {
		return doodadContents.getPrefHeight() * (1 + focusScaleIncrease);
	}

	@Override
	public float getMinHeight() {
		return doodadContents.getMinHeight();
	}

	@Override
	public boolean isTouchable() {
		return doodadContents.getTouchable() != Touchable.disabled;
	}

	@Override
	public void setColor(Color color) {
		scalingBackgroundImage.setColor(color);
	}

	@Override
	public Color getColor() {
		return scalingBackgroundImage.getColor();
	}

	@Override
	public Actor getView() {
		return this;
	}

	protected class DoodadContentsTable extends Table {
		private void setFocused(boolean isFocused, boolean forced) {
			InputEvent event = Pools.obtain(InputEvent.class);
			if (!isFocused) {
				event.setType(InputEvent.Type.exit);
			} else if (isKeyboardMode.getAsBoolean() || forced) {
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
	}

}
