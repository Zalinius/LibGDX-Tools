package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalDoodad;
import com.github.tommyettinger.textra.Styles.LabelStyle;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;
import com.github.tommyettinger.textra.TypingLabel;

public class UniversalLabel extends UniversalDoodad {

	private final TypingLabel label;

	protected Supplier<String> textSupplier;

	protected UniversalLabel(Supplier<String> textSupplier, LabelStyle typingLabelStyle, TextButtonStyle style) {
		super(style, false);
		label = new TypingLabel(textSupplier.get(), typingLabelStyle) {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				resizeUI(); // TODO will this be covered elsewhere with composite doodads?
				super.draw(batch, parentAlpha);
			}
		};
		this.textSupplier = textSupplier;
		label.setWrap(false);
	}

	public void setTextSupplier(Supplier<String> textSupplier) {
		this.textSupplier = textSupplier;
	}

	public void setWrap(boolean wrap) {
		label.setWrap(wrap);
	}

	@Override
	public float getMinHeight() {
		return label.getMinHeight();
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
		return false;
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
	}

	@Override
	public void setFocused(boolean focused) {
		// does nothing
	}

	@Override
	public void resizeUI() {
		label.setText(textSupplier.get());
		label.skipToTheEnd();  // Only Textra TypingLabel do the special effects, so we skip to the end right away
		//		setStyle(getStyle());
		//		if (!getWrap() && getStyle().background != null) {
		//			//TODO Makes backgrounds scale with text properly, but doesn't preserve Align.center...? Shucks
		//			setSize(getPrefWidth(), getPrefHeight());
		//		}
	}

}
