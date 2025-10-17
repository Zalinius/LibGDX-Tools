package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;

public interface DarkScreenBehindPopUp {

	boolean remove();

	void fadeIn(Actor actor, boolean canDismiss, StageLikeRenderable stageLikeRenderable);

	void fadeOutAndRemove();

}
