package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.github.tommyettinger.textra.Styles.CheckBoxStyle;
import com.github.tommyettinger.textra.TextraCheckBox;

public class UniversalCheckbox extends UniversalButton {

	private final TextraCheckBox box;
	private final Supplier<String> uncheckedLabel;
	private final Supplier<String> checkedLabel;

	public UniversalCheckbox(BasicButton basicButton, Supplier<String> uncheckedLabel, Supplier<String> checkedLabel, Consumer<Boolean> consumer, CheckBoxStyle style, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(basicButton, Suppliers.emptyString(), Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener);
		this.uncheckedLabel = uncheckedLabel;
		this.checkedLabel = checkedLabel;

		// YOU MUST SET THE MIN WIDTH & HEIGHT OF THIS DRAWABLE FOR RESIZING TO WORK
		float originalWidth = style.checkboxOff.getMinWidth();
		float originalHeight = style.checkboxOff.getMinHeight();

		// It doesn't matter which label we initialize with, as the button resizes every frame based on the contents

		box = new TextraCheckBox(uncheckedLabel.get(), style) {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				setStyle(getStyle());
				float minimum = 0.05f;
				UserInterfaceSizer.scaleToMinimumPercentage(getStyle().checkboxOn, minimum, originalWidth, originalHeight);
				UserInterfaceSizer.scaleToMinimumPercentage(getStyle().checkboxOff, minimum, originalWidth, originalHeight);
				if (getStyle().checkboxOnOver != null) {
					UserInterfaceSizer.scaleToMinimumPercentage(getStyle().checkboxOnOver, minimum, originalWidth, originalHeight);
				}
				if (getStyle().checkboxOver != null) {
					UserInterfaceSizer.scaleToMinimumPercentage(getStyle().checkboxOver, minimum, originalWidth, originalHeight);
				}

				super.draw(batch, parentAlpha);
			}
		};
		box.getImageCell().padRight(UserInterfaceSizer.getWidthPercentage(0.01f));

		basicButton.clearChildren();
		basicButton.setWidth(basicButton.getWidth() + box.getPrefWidth());
		basicButton.add(box).center().growX();
		basicButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				initializeAsChecked(!box.isChecked());
				basicButton.setChecked(false);
				consumer.accept(box.isChecked());
				UniversalCheckbox.this.setFocused(true);
			}
		});
	}
	@Override
	public void setFocused(boolean isFocused) {
		super.setFocused(isFocused);
		if (isFocused) {
			box.getClickListener().enter(null, 0, 0, -1, box);
		} else {
			box.getClickListener().exit(null, 0, 0, -1, box);
		}

	}

	/**
	 * To be used when setting up a menu: this sets the box as checked (or not) without firing an interaction event
	 * @param shouldBeChecked
	 */
	public void initializeAsChecked(boolean shouldBeChecked) {
		box.setProgrammaticChangeEvents(false);
		box.setChecked(shouldBeChecked);
		box.setProgrammaticChangeEvents(false);
		box.setText(box.isChecked() ? checkedLabel.get() : uncheckedLabel.get());
	}

	@Override
	public void resizeUI() {
		super.resizeUI();
		box.setText(box.isChecked() ? checkedLabel.get() : uncheckedLabel.get());
	}

}
