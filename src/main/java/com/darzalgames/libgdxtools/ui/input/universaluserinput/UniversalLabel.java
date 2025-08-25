package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.github.tommyettinger.textra.Styles.LabelStyle;
import com.github.tommyettinger.textra.TypingLabel;

public class UniversalLabel extends TypingLabel {

	private final LabelStyle typingLabelStyle;

	protected Supplier<String> textSupplier;

	public UniversalLabel(Supplier<String> textSupplier, LabelStyle typingLabelStyle) {
		super(textSupplier.get(), typingLabelStyle);
		this.typingLabelStyle = typingLabelStyle;
		setTextSupplier(textSupplier);
		setWrap(false);
		skipToTheEnd();  // Only Textra TypingLabel do the special effects, so we skip to the end right away
		setAlignment(Alignment.CENTER);
	}

	public void setTextSupplier(Supplier<String> textSupplier) {
		this.textSupplier =  () ->  "[@" + typingLabelStyle.font.name + "]" + textSupplier.get() + "[@]";
	}

	public boolean isBlank() {
		return storedText.isBlank();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		resizeUI();
		super.draw(batch, parentAlpha);
	}

	public void resizeUI() {
		setFont(typingLabelStyle.font); // updates us to the resized font size
		setText(textSupplier.get(), true, false);
		skipToTheEnd();  // Only Textra TypingLabel do the special effects, so we skip to the end right away
		invalidateHierarchy();
		//		if (!label.isWrap()) {// && label.getStyle().background != null) {
		//			//TODO Makes backgrounds scale with text properly, but doesn't preserve Align.center...? Shucks
		//			setSize(getPrefWidth(), getPrefHeight());
		//		}
	}

	public void setAlignment(Alignment alignment) {
		super.setAlignment(alignment.getAlignment());
	}

}
