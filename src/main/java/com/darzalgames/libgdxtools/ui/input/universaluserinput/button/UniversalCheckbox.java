package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.github.tommyettinger.textra.Styles.CheckBoxStyle;
import com.github.tommyettinger.textra.Styles.TextButtonStyle;

public class UniversalCheckbox extends UniversalButton {

	private final Image box;
	private final UniversalLabel label;
	private final Supplier<String> uncheckedLabel;
	private final Supplier<String> checkedLabel;
	private boolean checked;

	public UniversalCheckbox(Supplier<String> uncheckedLabel, Supplier<String> checkedLabel, Consumer<Boolean> consumer, CheckBoxStyle style, TextButtonStyle buttonStyle, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener, buttonStyle);
		this.uncheckedLabel = () -> " " + uncheckedLabel.get();
		this.checkedLabel = () -> " " + checkedLabel.get();
		label = GameInfo.getUserInterfaceFactory().getLabel(uncheckedLabel);

		// YOU MUST SET THE MIN WIDTH & HEIGHT OF THIS DRAWABLE FOR RESIZING TO WORK
		float originalWidth = style.checkboxOff.getMinWidth();
		float originalHeight = style.checkboxOff.getMinHeight();

		// It doesn't matter which label we initialize with, as the button resizes every frame based on the contents
		box = new Image() {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				Drawable checkbox = null;
				if (isDisabled()) {
					if (isChecked() && style.checkboxOnDisabled != null) {
						checkbox = style.checkboxOnDisabled;
					} else {
						checkbox = style.checkboxOffDisabled;
					}
				}
				if (checkbox == null) {
					boolean over = isOver() && !isDisabled();
					if (isChecked() && style.checkboxOn != null) {
						checkbox = over && style.checkboxOnOver != null ? style.checkboxOnOver : style.checkboxOn;
					} else if (over && style.checkboxOver != null) {
						checkbox = style.checkboxOver;
					} else {
						checkbox = style.checkboxOff;
					}
				}
				setDrawable(checkbox);


				// mine below
				float minimum = 0.05f;
				UserInterfaceSizer.scaleToMinimumPercentage(style.checkboxOn, minimum, originalWidth, originalHeight);
				UserInterfaceSizer.scaleToMinimumPercentage(style.checkboxOff, minimum, originalWidth, originalHeight);
				if (style.checkboxOnOver != null) {
					UserInterfaceSizer.scaleToMinimumPercentage(style.checkboxOnOver, minimum, originalWidth, originalHeight);
				}
				if (style.checkboxOver != null) {
					UserInterfaceSizer.scaleToMinimumPercentage(style.checkboxOver, minimum, originalWidth, originalHeight);
				}

				super.draw(batch, parentAlpha);
			}
		};
		//		add(box).padRight(calculatePadding());
		add(box);
		add(label).growX();
		debug();
		addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				initializeAsChecked(!isChecked());
				consumer.accept(isChecked());
				UniversalCheckbox.this.setFocused(true);
			}
		});
		setButtonRunnable(() -> {
			initializeAsChecked(!isChecked());
			consumer.accept(isChecked());
			UniversalCheckbox.this.setFocused(true);
		});
	}
	@Override
	public void setFocused(boolean isFocused) {
		super.setFocused(isFocused);
		//		if (isFocused) {
		//			box.getClickListener().enter(null, 0, 0, -1, box);
		//		} else {
		//			box.getClickListener().exit(null, 0, 0, -1, box);
		//		}

	}

	/**
	 * To be used when setting up a menu: this sets the box as checked (or not) without firing an interaction event
	 * @param shouldBeChecked
	 */
	public void initializeAsChecked(boolean shouldBeChecked) {
		setChecked(shouldBeChecked);
		resizeUI();
	}

	@Override
	public void resizeUI() {
		super.resizeUI();
		label.setTextSupplier(isChecked() ? checkedLabel : uncheckedLabel);
		label.resizeUI();
		//		displayLabel.setColor(isOver() ? getStyle().overFontColor : getStyle().fontColor);
		pack();
	}

	@Override
	public float getPrefWidth() {
		return box.getWidth() + calculatePadding() + label.getWidth();
	}

	private float calculatePadding() {
		return UserInterfaceSizer.getWidthPercentage(0.005f);
	}

	@Override
	public boolean isBlank() {
		// what would a blank checkbox even mean
		return false;
	}

	@Override
	public void setAlignment(Alignment alignment) {
		getCell(box).align(alignment.getAlignment());
		label.setAlignment(alignment);
		// TODO uh any more?
	}

	protected boolean isChecked() {
		return checked;
	}
	protected void setChecked(boolean checked) {
		this.checked = checked;
	}

}
