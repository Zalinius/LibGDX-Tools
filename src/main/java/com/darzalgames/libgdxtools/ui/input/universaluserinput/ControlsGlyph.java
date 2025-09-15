package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.GlyphFactory;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputStrategyObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class ControlsGlyph extends Image implements InputStrategyObserver {

	private Input input;
	private Alignment alignment;
	private final Supplier<Boolean> parentIsEnabled;
	private final InputStrategySwitcher inputStrategySwitcher;

	public ControlsGlyph(Input input, InputStrategySwitcher inputStrategySwitcher, Texture referenceGlyphForSize, Supplier<Boolean> parentIsEnabled) {
		this.parentIsEnabled = parentIsEnabled;
		this.inputStrategySwitcher = inputStrategySwitcher;
		setInput(input);
		inputStrategySwitcher.register(this);
		setSize(referenceGlyphForSize.getWidth(), referenceGlyphForSize.getHeight());
		setTouchable(Touchable.disabled);
		setVisibilityBasedOnCurrentInputStrategy();
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

	@Override
	public void act(float delta) {
		super.act(delta);
		Texture glyph = GlyphFactory.getGlyphForInput(input);
		if (glyph != null) {
			setGlyph(glyph);
			UserInterfaceSizer.scaleToMinimumPercentage(this, 0.05f);
			this.setPosition(0, 0);

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

			setVisibilityBasedOnCurrentInputStrategy();
		}
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		setVisibilityBasedOnCurrentInputStrategy();
	}

	private void setVisibilityBasedOnCurrentInputStrategy() {
		setVisible(!inputStrategySwitcher.isMouseMode() && parentIsEnabled.get());
	}

	@Override
	public boolean shouldBeUnregistered() {
		return getStage() == null;
	}

}
