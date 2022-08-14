package com.zalinius.libgdxtools.tooltip;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.zalinius.libgdxtools.tools.StyleManager;

public class Tooltip extends Group {
	protected static Table tableToDisplay;
	protected static final int width = 425, height = 350, x = 750, y = 50;
	protected static LabelStyle style;

	private static boolean isEnabled;

	public Tooltip() {
		isEnabled = true;

		style = StyleManager.style;
		tableToDisplay = new Table();
		//tableToDisplay.background(new DrawableNinePatch(Assets.get(Assets.gray)));

		resetDisplay();
		hideMenu();

		addActor(tableToDisplay);
	}

	// Basic template for a setToDisplayMethod
	public static void setToDisplay(final Object o) {

		if (isEnabled)
		{
			resetDisplay();

			//Create the labels, images, etc. to be displayed

			//Add the created UI elements to the table
			tableToDisplay.row();
			tableToDisplay.add();

			fillTableNicely();
			showMenu();
		}
	}

	public static void closeDisplay() {
		hideMenu();
	}

	public static void resetDisplay() {
		tableToDisplay.clear();

		tableToDisplay.defaults().left().top().width(width-20).height(height/4f);
		tableToDisplay.pad(5);

		tableToDisplay.setBounds(x, y, width, height);
	}

	private static void fillTableNicely() {
		tableToDisplay.pack();
		tableToDisplay.padLeft(30);
		tableToDisplay.setHeight(height);
		if (tableToDisplay.getWidth() < width) {
			tableToDisplay.setWidth(width);
		}
	}

	protected static void hideMenu() {
		VisibleAction visibleAction = new VisibleAction();
		visibleAction.setVisible(false);
		tableToDisplay.addAction(new SequenceAction(new DelayAction(0.07f), visibleAction));
	}

	protected static void showMenu() {
		tableToDisplay.clearActions();
		tableToDisplay.setVisible(true);
	}

	public static void setEnabled(final boolean enabled) {
		isEnabled = enabled;
	}

	//Draw the tooltip, following the cursor
	@Override
	public void draw(final Batch batch, final float parentAlpha) {
		float offset = Gdx.graphics.getWidth() * 0.01f;
		tableToDisplay.setTransform(true);
		tableToDisplay.setScale(1.0f);
		Vector2 followPos = screenToLocalCoordinates(new Vector2(Gdx.input.getX() + offset,
				Gdx.input.getY() - offset));
		tableToDisplay.setPosition(followPos.x, followPos.y);
		super.draw(batch, parentAlpha);
	}

}
