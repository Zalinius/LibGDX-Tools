package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

public abstract class UniversalDoodad extends Table implements VisibleInputConsumer {

	private TextButtonStyle style;
	private boolean disabled;
	private final boolean isAClickableDoodad;
	private final ClickListener clickListener;

	protected UniversalDoodad(TextButtonStyle textButtonStyle, boolean isAClickableDoodad) {
		setStyle(textButtonStyle);
		setSize(textButtonStyle.up.getMinWidth(), textButtonStyle.up.getMinHeight());
		this.isAClickableDoodad = isAClickableDoodad;
		setDisabled(false);
		clickListener = new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				justPressed();
			}
		};
		addListener(clickListener);
	}

	@Override
	public Actor getView() {
		return this;
	}

	public TextButtonStyle getStyle() {
		return style;
	}

	public void setStyle(TextButtonStyle newStyle) {
		style = newStyle;
		setBackground(style.down);
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

	protected boolean isAClickableDoodad() {
		return isAClickableDoodad;
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
	public void resizeUI() {
		invalidate();
		pack();
	}


	// ----------------- \/  VISUAL STYLING  \/  ----------------- //
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

		setBackground(getBackgroundDrawable());

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

	private boolean isPressed() {
		return clickListener.isVisualPressed();
	}

}
