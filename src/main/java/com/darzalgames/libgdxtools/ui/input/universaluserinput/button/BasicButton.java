package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;


import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Null;

public interface BasicButton {

	static boolean isSpacer(UniversalButton button) {
		return button.getButton().isDisabled() && button.isBlank();
	}

	boolean isTouchable();
	boolean isChecked();
	boolean isDisabled();
	boolean isOver();
	void toggle();
	void setDisabled(boolean disabled);
	void setTouchable(Touchable isTouchable);
	void setProgrammaticChangeEvents(boolean fireProgrammaticChangeEvents);
	void setChecked(boolean checked);

	TextButtonStyle getStyle();
	void setStyle(ButtonStyle style);

	void setWidth(float width);
	float getWidth();
	float getPrefWidth();
	float getPrefHeight();
	float getMinHeight();
	float getHeight();
	void setSize(float prefWidth, float prefHeight);

	void setName(String string);
	boolean fire(Event event);
	void clearActions();
	void clearChildren();
	void addAction(Action action);
	boolean addListener(EventListener eventListener);
	DelayedRemovalArray<EventListener> getListeners();
	<T extends Actor> Cell<T> add(@Null T actor);
	void addActor(Actor actor);
	boolean removeActor(Actor actor);
	Actor getView();
	Stage getStage();
	Label getLabel();
	Cell<Label> getLabelCell();
	String getButtonText();
}
