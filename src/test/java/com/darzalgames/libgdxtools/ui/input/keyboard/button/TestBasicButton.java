package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.BasicButton;

public class TestBasicButton implements BasicButton {
	
	private boolean isOver = false;

	@Override
	public boolean isTouchable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOver() {
		return isOver;
	}

	@Override
	public void toggle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDisabled(boolean disabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTouchable(Touchable isTouchable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProgrammaticChangeEvents(boolean fireProgrammaticChangeEvents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TextButtonStyle getStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStyle(ButtonStyle style) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWidth(float width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getPrefWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getPrefHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSize(float prefWidth, float prefHeight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean fire(Event event) {
		if (event instanceof InputEvent) {
			InputEvent inputEvent = (InputEvent)event;
			isOver = inputEvent.getType().equals(InputEvent.Type.enter);
		}
		return false;
	}

	@Override
	public void clearActions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearChildren() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAction(Action action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addListener(EventListener eventListener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T extends Actor> Cell<T> add(T actor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Actor getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stage getStage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Label getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cell<Label> getLabelCell() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getButtonText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getMinHeight() {
		return 0;
	}

}
