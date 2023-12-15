package com.darzalgames.libgdxtools.ui.input.keyboard;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darzalgames.libgdxtools.MainGame;
import com.darzalgames.libgdxtools.ui.input.InputObserver;

public class InputSensitiveLabel extends Label implements InputObserver {
	
	private final Supplier<String> textSupplier;

	public InputSensitiveLabel(Supplier<String> textSupplier, LabelStyle style) {
		super(textSupplier.get(), style);
		this.textSupplier = textSupplier;
		MainGame.getInputStrategyManager().register(this);
	}

	@Override
	public void inputStrategyChanged() {
		this.setText(textSupplier.get());
	}
	
	@Override
	public boolean remove() {
		MainGame.getInputStrategyManager().unregister(this);
		return super.remove();
	}
	
	@Override
	public void clear() {
		MainGame.getInputStrategyManager().unregister(this);
		super.clear();
	}
	
	@Override
	protected void setParent(Group parent) {
		if (parent == null) {
			MainGame.getInputStrategyManager().unregister(this);
			
		}
		super.setParent(parent);
	}

	@Override
	public boolean shouldBeUnregistered() {
		return this.getStage() == null;
	}

}
