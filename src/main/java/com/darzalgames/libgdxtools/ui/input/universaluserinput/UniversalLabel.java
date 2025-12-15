package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.function.Supplier;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.darzalgames.libgdxtools.internationalization.LabelEffectsProcessor;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.TemporaryStyler;
import com.github.tommyettinger.textra.Styles.LabelStyle;
import com.github.tommyettinger.textra.TypingConfig;
import com.github.tommyettinger.textra.TypingLabel;

public class UniversalLabel extends TypingLabel {

	private final LabelStyle typingLabelStyle;

	protected Supplier<String> textSupplier;
	private float bounceScaling;
	private Action currentBounceAction;
	private boolean shouldSkipToEnd;

	public UniversalLabel(Supplier<String> textSupplier, LabelStyle typingLabelStyle) {
		super(textSupplier.get(), typingLabelStyle);
		this.typingLabelStyle = typingLabelStyle;
		bounceScaling = 1;
		setTextSupplier(textSupplier);
		setWrap(false);
		setShouldSkipToEnd(true);
		setAlignment(Alignment.CENTER);
	}

	public void setTextSupplier(Supplier<String> textSupplier) {
		this.textSupplier = () -> TemporaryStyler.make("[@" + typingLabelStyle.font.name + "]" + LabelEffectsProcessor.process(textSupplier.get()));
		if (textSupplier.get().isBlank()) {
			this.textSupplier = () -> ""; // doing this gives the labels the correct height for at least a single line, helping with layout later
		}
	}

	public boolean isBlank() {
		return storedText.isBlank();
	}

	public void setShouldSkipToEnd(boolean shouldSkipToEnd) {
		this.shouldSkipToEnd = shouldSkipToEnd;
	}

	@Override
	public void act(float delta) {
		// resize in act() so that by the time we reach draw(), the parent has the correct layout information for this label
		resizeUI();
		super.act(delta);
	}

	public void resizeUI() {
		setFont(typingLabelStyle.font); // updates us to the resized font size
		String currentText = getOriginalText().toString();
		String newText = "[%" + bounceScaling * 100 + "]" + textSupplier.get();
		if (!currentText.equals(newText) && shouldSkipToEnd) {
			// only update when there's a change: this allows us to use the fancy Textra animations
			setSize(0, 0); // the documentation suggests doing this before calling restart()
			restart(newText);
		}

		if (shouldSkipToEnd) {
			skipToTheEnd(false); // Only Textra TypingLabel do the special effects, so we skip to the end right away
		} else {
			setTextSpeed(TypingConfig.DEFAULT_SPEED_PER_CHAR / 3f);
		}

		if (!wrap) {
			pack();
		} else {
			setHeight(getPrefHeight());
		}
	}

	public void setAlignment(Alignment alignment) {
		super.setAlignment(alignment.getAlignment());
	}

	public void addBounceAction(float scaleBy, float duration) {
		removeAction(currentBounceAction);
		bounceScaling = 1 + scaleBy;
		currentBounceAction = new TemporalAction(duration, Interpolation.exp5Out) {
			@Override
			protected void update(float percent) {
				bounceScaling = 1 + scaleBy * (1 - percent);
				setText("[%" + bounceScaling * 100 + "]" + textSupplier.get(), true, false);
			}

			@Override
			protected void end() {
				update(1);
			}
		};
		addAction(currentBounceAction);
	}

}
