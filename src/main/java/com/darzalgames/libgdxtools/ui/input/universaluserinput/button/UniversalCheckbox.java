package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class UniversalCheckbox extends UniversalButton {
	
	private final CheckBox box;
	private final Supplier<String> uncheckedLabel;
	private final Supplier<String> checkedLabel;
	
	public UniversalCheckbox(TextButton textButton, Supplier<String> uncheckedLabel, Supplier<String> checkedLabel, Consumer<Boolean> consumer, CheckBoxStyle style, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		super(textButton, () -> "", Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener);
		this.uncheckedLabel = uncheckedLabel;
		this.checkedLabel = checkedLabel;
		// TODO what the heck is this length check?!
		box = new CheckBox(uncheckedLabel.get().length() > checkedLabel.get().length() ? uncheckedLabel.get() : checkedLabel.get(), style) {
			@Override
			public void act(float delta) {
				super.act(delta);
				this.setStyle(this.getStyle());
				setSize(getPrefWidth(), getPrefHeight());
			}
		};
		box.getImageCell().padRight(3);

		textButton.clearChildren();
		textButton.setWidth(textButton.getWidth() + box.getPrefWidth());
		textButton.add(box).center().growX();
		textButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				initializeAsChecked(!box.isChecked());
				textButton.setChecked(false);
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
