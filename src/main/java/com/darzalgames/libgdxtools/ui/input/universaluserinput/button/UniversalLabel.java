package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Supplier;

import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalDoodad;
import com.github.tommyettinger.textra.Styles.LabelStyle;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;
import com.github.tommyettinger.textra.TypingLabel;

public class UniversalLabel extends UniversalDoodad {

	private final TypingLabel label;
	//	private final LabelStyle typingLabelStyle;

	protected Supplier<String> textSupplier;

	public UniversalLabel(Supplier<String> textSupplier, LabelStyle typingLabelStyle, TextButtonStyle style) {
		super(style, false);
		label = new TypingLabel(textSupplier.get(), typingLabelStyle);
		this.textSupplier = textSupplier;
		label.setWrap(false);
		label.skipToTheEnd();  // Only Textra TypingLabel do the special effects, so we skip to the end right away
		add(label);
		setDisabled(true);
	}

	public void setTextSupplier(Supplier<String> textSupplier) {
		this.textSupplier = textSupplier;
	}

	public void setWrap(boolean wrap) {
		label.setWrap(wrap);
	}

	@Override
	public void consumeKeyInput(Input input) {
		// does nothing
	}

	@Override
	public void focusCurrent() {
		// does nothing
	}

	@Override
	public void clearSelected() {
		// does nothing
	}

	@Override
	public void selectDefault() {
		// does nothing
	}

	@Override
	public boolean isDisabled() {
		return isBlank() || super.isDisabled();
	}

	@Override
	public boolean isBlank() {
		return label.storedText.isBlank();
	}

	/**
	 * Set both alignments for the button's label. Quoting from the LibGDX documentation:
	 * 		labelAlign Aligns all the text within the label (default left center).
	 * 		lineAlign Aligns each line of text horizontally (default left).
	 */
	@Override
	public void setAlignment(Alignment alignment) {
		label.setAlignment(alignment.getAlignment());
		getCell(label).align(alignment.getAlignment());

	}

	@Override
	public void setFocused(boolean focused) {
		// does nothing
	}

	@Override
	public void resizeUI() {
		label.setFont(getStyle().font); // updates us to the resized font size
		label.setText(textSupplier.get(), true, false);
		label.skipToTheEnd();  // Only Textra TypingLabel do the special effects, so we skip to the end right away
		label.pack();
		//		setStyle(getStyle());
		//		if (!label.isWrap()) {// && label.getStyle().background != null) {
		//			//TODO Makes backgrounds scale with text properly, but doesn't preserve Align.center...? Shucks
		//			setSize(getPrefWidth(), getPrefHeight());
		//		}
		super.resizeUI();
	}


	@Override
	public float getMinHeight() {
		return label.getMinHeight();
	}
	@Override
	public float getHeight() {
		return label.getHeight();
	}
	@Override
	public float getPrefHeight() {
		return label.getPrefHeight();
	}

	@Override
	public float getMinWidth() {
		return label.getMinWidth();
	}
	@Override
	public float getWidth() {
		return label.getWidth();
	}
	@Override
	public float getPrefWidth() {
		return label.getPrefWidth();
	}

}
