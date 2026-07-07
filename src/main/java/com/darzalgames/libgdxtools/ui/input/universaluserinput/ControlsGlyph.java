package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.function.BooleanSupplier;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.GlyphFactory;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class ControlsGlyph extends Image {

	private Input input;
	private Alignment alignment;
	private BooleanSupplier parentIsEnabled;
	private final InputStrategySwitcher inputStrategySwitcher;
	private Actor parentButton;

	public ControlsGlyph(Input input, InputStrategySwitcher inputStrategySwitcher, Texture referenceGlyphForSize) {
		this.inputStrategySwitcher = inputStrategySwitcher;
		setInput(input);
		setSize(referenceGlyphForSize.getWidth(), referenceGlyphForSize.getHeight());
		setTouchable(Touchable.disabled);
		setAlignment(Alignment.BOTTOM_LEFT);
	}

	public void setParentButton(UniversalButton parentButton) {
		this.parentButton = parentButton;
		parentIsEnabled = () -> !parentButton.isDisabled();
	}

	public void setInput(Input input) {
		this.input = input;
	}

	private void setGlyph(Texture texture) {
		if (texture != null) {
			this.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
		}
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	@Override
	public void act(float delta) {
		if (parentButton != null && parentButton.getStage() == null) {
			remove();
		} else {
			super.act(delta);
			toFront();
			Texture glyph = GlyphFactory.getGlyphForInput(input);
			if (glyph != null) {
				setGlyph(glyph);
				UserInterfaceSizer.scaleToMinimumPercentage(this, 0.05f);
				Vector2 localToStageCoordinates = parentButton.localToStageCoordinates(new Vector2());
				setPosition(localToStageCoordinates.x, localToStageCoordinates.y);

				Actor parent = getParent();
				float xOffset = switch (alignment) {
				case BOTTOM_LEFT, LEFT, TOP_LEFT -> -getWidth() * 0.55f;
				case BOTTOM_RIGHT, RIGHT, TOP_RIGHT -> parent.getWidth() - getWidth() * 0.55f;
				default -> (parent.getWidth() - getWidth()) / 2f;
				};

				float yOffset = switch (alignment) {
				case BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT -> -getHeight() * 0.45f;
				case TOP_LEFT, TOP, TOP_RIGHT -> parent.getHeight() - getHeight() * 0.15f;
				default -> (parent.getHeight() - getHeight()) / 2f;
				};
				moveBy(xOffset, yOffset);

				setVisibilityBasedOnCurrentInputStrategy(); // act() but not draw() is called when the glyph is not visible
			}
		}
	}

	// we set the visibility based on the current input strategy in both act() and draw() since there are valid cases where only one of the two is being called and an update is needed

	@Override
	public void draw(Batch batch, float parentAlpha) {
		setVisibilityBasedOnCurrentInputStrategy(); // draw() but not act() is called when the game is paused
		if (isVisible()) {
			// libgdx checks visibility before calling draw(), so since I interrupted their draw() and may be toggling visibility here, we check again
			super.draw(batch, parentAlpha);
		}
	}

	private void setVisibilityBasedOnCurrentInputStrategy() {
		setVisible(!inputStrategySwitcher.isMouseMode() && parentIsEnabled.getAsBoolean());
	}

}
