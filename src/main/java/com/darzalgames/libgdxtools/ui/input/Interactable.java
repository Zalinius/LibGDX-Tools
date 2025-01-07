package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.scenes.scene2d.Actor;

public interface Interactable {

	public void focus();
	public void unfocus();
	public void press();
	
	public Actor getView();
	
}
