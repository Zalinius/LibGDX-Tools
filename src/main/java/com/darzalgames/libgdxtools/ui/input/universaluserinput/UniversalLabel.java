package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.TemporaryStyler;
import com.github.tommyettinger.textra.Styles.LabelStyle;
import com.github.tommyettinger.textra.TypingLabel;

public class UniversalLabel extends TypingLabel {

	private final LabelStyle typingLabelStyle;

	protected Supplier<String> textSupplier;
	private float bounceScaling = 1f;
	private Action currentBounceAction;

	public UniversalLabel(Supplier<String> textSupplier, LabelStyle typingLabelStyle) {
		super(textSupplier.get(), typingLabelStyle);
		this.typingLabelStyle = typingLabelStyle;
		setTextSupplier(textSupplier);
		setWrap(false);
		setAlignment(Alignment.CENTER);
	}

	public void setTextSupplier(Supplier<String> textSupplier) {
		this.textSupplier = () -> TemporaryStyler.make("[@" + typingLabelStyle.font.name + "]" + textSupplier.get());
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
		setText("[%" + bounceScaling * 100 + "]" + textSupplier.get(), true, false);

		skipToTheEnd(); // Only Textra TypingLabel do the special effects, so we skip to the end right away

		invalidateHierarchy();

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
			}

			@Override
			protected void end() {
				bounceScaling = 1;
			}
		};
		addAction(currentBounceAction);
	}

}
