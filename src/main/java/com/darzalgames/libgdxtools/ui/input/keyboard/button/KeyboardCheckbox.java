package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.darzalgames.darzalcommon.functional.Runnables;

public class KeyboardCheckbox extends KeyboardButton {
	
	private final CheckBox box;
	private final String uncheckedLabel;
	private final String checkedLabel;
	
	public KeyboardCheckbox(TextButton textButton, String uncheckedLabel, String checkedLabel, Consumer<Boolean> consumer, CheckBoxStyle style) {
		super(textButton, Runnables.nullRunnable());
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
	
	public void initializeAsChecked(boolean shouldBeChecked) {
		box.setProgrammaticChangeEvents(false);
		box.setChecked(shouldBeChecked);
		box.setProgrammaticChangeEvents(false);
		box.setText(box.isChecked() ? checkedLabel : uncheckedLabel);
	}

}
