package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;


import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Null;

public interface BasicButton {

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

	float getWidth();
	void setWidth(float width);
	float getPrefWidth();
	float getPrefHeight();
	void setSize(float prefWidth, float prefHeight);

	void setName(String string);
	boolean fire(Event event);
	void clearActions();
	void clearChildren();
	void addAction(Action action);
	boolean addListener(EventListener eventListener);
	<T extends Actor> Cell<T> add(@Null T actor);
	Actor getView();
	Stage getStage();
	Label getLabel();
	Cell<Label> getLabelCell();
	String getButtonText();
	float getMinHeight();
}
