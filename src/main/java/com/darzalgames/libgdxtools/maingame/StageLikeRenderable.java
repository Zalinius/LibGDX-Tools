package com.darzalgames.libgdxtools.maingame;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;

public interface StageLikeRenderable extends InputProcessor {

	void act();

	void act(float delta);

	void draw();

	void clear();

	void resize(int width, int height);

	String nameOfActorUnderCursor();

	String getName();

	void addActor(Actor view);

}
