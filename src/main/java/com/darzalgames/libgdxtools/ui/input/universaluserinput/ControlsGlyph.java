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
import com.darzalgames.libgdxtools.maingame.GetOnStage;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.GlyphFactory;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class ControlsGlyph extends Image {

	private Input input;
	private Alignment alignment;
	private final InputStrategySwitcher inputStrategySwitcher;
	private Actor parentActor;

	public ControlsGlyph(Input input, InputStrategySwitcher inputStrategySwitcher, Texture referenceGlyphForSize) {
		this.inputStrategySwitcher = inputStrategySwitcher;
		setInput(input);
		setSize(referenceGlyphForSize.getWidth(), referenceGlyphForSize.getHeight());
		setTouchable(Touchable.disabled);
		setAlignment(Alignment.BOTTOM_LEFT);
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

	/**
	 * @param parentButton the Button which gets pressed when this glyph's input is pressed
	 */
	public void updatePosition(UniversalButton parentButton) {
		updatePosition(parentButton, parentButton::isDisabled);
	}

	/**
	 * @param parentActor     the Actor which this glyph is paired to, it may be a Button or a more complex structure (e.g. this glyph represents an input which puts a menu into focus)
	 * @param shouldHideGlyph policy for when the glyph should be hidden according to the parentActor, no need to account for mouse/keyboard mode in this check
	 */
	public void updatePosition(Actor parentActor, BooleanSupplier shouldHideGlyph) {
		this.parentActor = parentActor;
		Texture glyph = GlyphFactory.getGlyphForInput(input);
		if (glyph != null) {
			setGlyph(glyph);
			UserInterfaceSizer.scaleToMinimumPercentage(this, 0.05f);
			Vector2 localToStageCoordinates = parentActor.localToStageCoordinates(new Vector2());
			setPosition(localToStageCoordinates.x, localToStageCoordinates.y);

			float xOffset = switch (alignment) {
			case BOTTOM_LEFT, LEFT, TOP_LEFT -> -getWidth() * 0.55f;
			case BOTTOM_RIGHT, RIGHT, TOP_RIGHT -> parentActor.getWidth() - getWidth() * 0.55f;
			default -> (parentActor.getWidth() - getWidth()) / 2f;
			};

			float yOffset = switch (alignment) {
			case BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT -> -getHeight() * 0.45f;
			case TOP_LEFT, TOP, TOP_RIGHT -> parentActor.getHeight() - getHeight() * 0.15f;
			default -> (parentActor.getHeight() - getHeight()) / 2f;
			};
			moveBy(xOffset, yOffset);
		}

		boolean isKeyboardMode = !inputStrategySwitcher.isMouseMode();
		boolean parentSaysToShow = !shouldHideGlyph.getAsBoolean();
		setVisible(isKeyboardMode && parentSaysToShow);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (parentActor != null) {
			if (parentActor.getStage() == null) {
				remove();
			} else {
				GetOnStage.addActorToStage(this, parentActor);
				toFront();
				super.draw(batch, parentAlpha);
			}
		}
	}

}
