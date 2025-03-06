package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class UniversalLabel extends Label {
	
	protected Supplier<String> textSupplier;

	protected UniversalLabel(Supplier<String> textSupplier, LabelStyle labelStyle) {
		super(textSupplier.get(), labelStyle);
		this.textSupplier = textSupplier;
		setWrap(true);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		this.setStyle(this.getStyle());
		this.setText(textSupplier.get());
		this.invalidateHierarchy();
		this.layout();
		super.draw(batch, parentAlpha);
	}
	
	public void setTextSupplier(Supplier<String> textSupplier) {
		this.textSupplier = textSupplier;
	}

}
