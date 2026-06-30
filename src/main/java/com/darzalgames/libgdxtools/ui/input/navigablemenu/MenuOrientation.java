package com.darzalgames.libgdxtools.ui.input.navigablemenu;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;

public enum MenuOrientation {
	VERTICAL(Input.UP, Input.DOWN, verticalMenuSpacingPolicy(), Cell::expandY),
	HORIZONTAL(Input.LEFT, Input.RIGHT, horizontalMenuSpacingPolicy(), Cell::expandX);

	private final Input backCode;
	private final Input forwardCode;
	private final Consumer<Cell<Actor>> spacingPolicy;
	private final Consumer<Cell<Actor>> spacerExpansionPolicy;

	MenuOrientation(Input backCode, Input forwardCode, Consumer<Cell<Actor>> spacingPolicy, Consumer<Cell<Actor>> spacerExpansionPolicy) {
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

	Consumer<Cell<Actor>> getSpacingPolicy() {
		return spacingPolicy;
	}

	void applySpacerExpansionPolicy(Cell<Actor> cell) {
		spacerExpansionPolicy.accept(cell);
	}

	private static Consumer<Cell<Actor>> horizontalMenuSpacingPolicy() {
		return cell -> {
			float widthPercentage = UserInterfaceSizer.getWidthPercentage(0.0075f);
			cell.spaceLeft(widthPercentage);
			cell.spaceRight(widthPercentage);
		};
	}

	private static Consumer<Cell<Actor>> verticalMenuSpacingPolicy() {
		return cell -> {
			float heightPercentage = UserInterfaceSizer.getHeightPercentage(0.0075f);
			cell.spaceTop(heightPercentage);
			cell.spaceBottom(heightPercentage);
		};
	}
}