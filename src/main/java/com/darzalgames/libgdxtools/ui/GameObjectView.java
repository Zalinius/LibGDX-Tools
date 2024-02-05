package com.darzalgames.libgdxtools.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Kind of my own interface that mimics {@link Actor}, but is generally used by an object that holds
 * one or more actors rather than being one.
 * @author DarZal
 */
public interface GameObjectView {
	public Actor getView();

}
