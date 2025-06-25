package com.darzalgames.libgdxtools.maingame;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public interface StageLikeRenderable extends InputProcessor {

	void act();
	void draw();

	void clear();

	void setShouldDraw(boolean shouldDraw);
	void resize(int width, int height);
	Vector2 screenToStageCoordinates(Vector2 vector2);


	String nameOfThingAtCursorPosition(float x, float y, boolean b);
	String getName();

}
