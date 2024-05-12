package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public class KeyboardCheckbox extends KeyboardButton {
	
	private final CheckBox box;
	private final String uncheckedLabel;
	private final String checkedLabel;
	
	protected KeyboardCheckbox(TextButton textButton, String uncheckedLabel, String checkedLabel, Consumer<Boolean> consumer, CheckBoxStyle style, InputStrategyManager inputStrategyManager, Runnable soundInteractListener) {
		super(textButton, Runnables.nullRunnable(), inputStrategyManager, soundInteractListener);
		this.uncheckedLabel = uncheckedLabel;
		this.checkedLabel = checkedLabel;
		box = new CheckBox(uncheckedLabel.length() > checkedLabel.length() ? uncheckedLabel : checkedLabel, style);
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
				KeyboardCheckbox.this.setFocused(true);
			}
		});
	}
	
	/**
	 * To be used when setting up a menu: this sets the box as checked (or not) without firing an interaction event
	 * @param shouldBeChecked
	 */
	public void initializeAsChecked(boolean shouldBeChecked) {
		box.setProgrammaticChangeEvents(false);
		box.setChecked(shouldBeChecked);
		box.setProgrammaticChangeEvents(false);
		box.setText(box.isChecked() ? checkedLabel : uncheckedLabel);
	}

}
