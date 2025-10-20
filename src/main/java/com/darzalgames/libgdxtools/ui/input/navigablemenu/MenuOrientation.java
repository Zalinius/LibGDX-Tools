package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;

public enum MenuOrientation {
	VERTICAL(Input.UP, Input.DOWN, () -> UserInterfaceSizer.getHeightPercentage(0.0075f), Cell::expandY),
	HORIZONTAL(Input.LEFT, Input.RIGHT, () -> UserInterfaceSizer.getWidthPercentage(0.0075f), Cell::expandX);

	private final Input backCode;
	private final Input forwardCode;
	private final Supplier<Float> spacingPolicy;
	private final Consumer<Cell<Actor>> spacerExpansionPolicy;

	MenuOrientation(Input backCode, Input forwardCode, Supplier<Float> spacingPolicy, Consumer<Cell<Actor>> spacerExpansionPolicy) {
		this.backCode = backCode;
		this.forwardCode = forwardCode;
		this.spacingPolicy = spacingPolicy;
		this.spacerExpansionPolicy = spacerExpansionPolicy;
	}

	Input getBackCode() {
		return backCode;
	}

	Input getForwardCode() {
		return forwardCode;
	}

	Supplier<Float> getSpacingPolicy() {
		return spacingPolicy;
	}

	void applySpacerExpansionPolicy(Cell<Actor> cell) {
		spacerExpansionPolicy.accept(cell);
	}
}