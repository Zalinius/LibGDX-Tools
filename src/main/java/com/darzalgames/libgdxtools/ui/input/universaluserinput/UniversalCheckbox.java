package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.github.tommyettinger.textra.Styles.CheckBoxStyle;

public class UniversalCheckbox extends UniversalTextButton {

	private final Image box;
	private final Supplier<String> uncheckedLabel;
	private final Supplier<String> checkedLabel;
	private boolean checked;
	private final float widthOverHeight;

	public UniversalCheckbox(Supplier<String> uncheckedLabel, Supplier<String> checkedLabel, Consumer<Boolean> consumer, CheckBoxStyle style, ButtonStyle buttonStyle, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(GameInfo.getUserInterfaceFactory().getLabel(uncheckedLabel), Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener, buttonStyle);
		this.uncheckedLabel = () -> " " + uncheckedLabel.get();
		this.checkedLabel = () -> " " + checkedLabel.get();

		// YOU MUST SET THE MIN WIDTH & HEIGHT OF THIS DRAWABLE FOR RESIZING TO WORK
		float originalWidth = style.checkboxOff.getMinWidth();
		float originalHeight = style.checkboxOff.getMinHeight();
		widthOverHeight = originalWidth / originalHeight;

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

				super.draw(batch, parentAlpha);
			}
		};
		box.setScaling(Scaling.fit);
		clearChildren();
		add(box);
		add(label);

		setButtonRunnable(() -> {
			initializeAsChecked(!isChecked());
			consumer.accept(isChecked());
			addAction(Actions.run(() -> setFocused(true, true)));
		});
	}

	/**
	 * To be used when setting up a menu: this sets the box as checked (or not) without firing an interaction event
	 * @param shouldBeChecked whether it should be checked initially
	 */
	public void initializeAsChecked(boolean shouldBeChecked) {
		setChecked(shouldBeChecked);
		resizeUI();
	}

	@Override
	public void resizeUI() {
		label.setTextSupplier(isChecked() ? checkedLabel : uncheckedLabel);
		super.resizeUI();
		box.setHeight(label.getHeight() * 1.25f);
		box.setWidth(widthOverHeight * box.getHeight());
		getCell(box).width(box.getWidth()).height(box.getHeight());
	}

	@Override
	public boolean isBlank() {
		// what would a blank checkbox even mean
		return false;
	}

	@Override
	public void setAlignment(Alignment alignment) {
		box.setAlign(alignment.getAlignment());
		super.setAlignment(alignment);
	}

	protected boolean isChecked() {
		return checked;
	}

	protected void setChecked(boolean checked) {
		this.checked = checked;
	}

}
