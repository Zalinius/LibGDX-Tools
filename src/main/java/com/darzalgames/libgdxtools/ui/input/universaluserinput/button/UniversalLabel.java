package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.tommyettinger.textra.Styles.LabelStyle;
import com.github.tommyettinger.textra.TypingLabel;

public class UniversalLabel extends TypingLabel {

	protected Supplier<String> textSupplier;

	protected UniversalLabel(Supplier<String> textSupplier, LabelStyle style) {
		super(textSupplier.get(), style);
		this.textSupplier = textSupplier;
		setWrap(false);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		this.setText(textSupplier.get());
		skipToTheEnd();	 // Only Textra TypingLabel do the special effects, so we skip to the end right away
		//		setStyle(getStyle());
		//		if (!getWrap() && getStyle().background != null) {
		//			//TODO Makes backgrounds scale with text properly, but doesn't preserve Align.center...? Shucks
		//			setSize(getPrefWidth(), getPrefHeight());
		//		}
		super.draw(batch, parentAlpha);
		debug();
	}

	public void setTextSupplier(Supplier<String> textSupplier) {
		this.textSupplier = textSupplier;
	}

}
