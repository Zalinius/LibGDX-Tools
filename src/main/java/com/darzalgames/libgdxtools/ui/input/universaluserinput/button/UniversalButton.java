package com.darzalgames.libgdxtools.ui.input.universaluserinput.button;

import java.util.function.Supplier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Pools;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

/**
 * Our very own custom button class that works with keyboard input!
 * This is also the base class for other keyboard *buttons* such as checkboxes and sliders,
 * which allows them all to be put in a navigable menu together and treated the same
 */
public class UniversalButton implements VisibleInputConsumer {
	private final BasicButton button;
	private Supplier<String> textSupplier;
	private final Supplier<Label> labelSupplier;
	private final Supplier<Cell<Label>> cellSupplier;
	private Runnable buttonRunnable;
	private final Image image;
	private Alignment alignment;
	private boolean wrap;
	private boolean doesSoundOnInteract = true;
	private final InputStrategySwitcher inputStrategySwitcher;
	private final Runnable soundInteractListener;

	public UniversalButton(BasicButton button, Supplier<String> textSupplier, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		this(button, textSupplier, Runnables.nullRunnable(), inputStrategySwitcher, soundInteractListener);
	}

	public UniversalButton(BasicButton button, Supplier<String> textSupplier, Runnable runnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		this(button, textSupplier, null, runnable, inputStrategySwitcher, soundInteractListener);
	}

	public UniversalButton(BasicButton button, Supplier<String> textSupplier, Image image, Runnable runnable, InputStrategySwitcher inputStrategySwitcher, Runnable soundInteractListener) {
		this.button = button;
		this.textSupplier = textSupplier;
		labelSupplier = button::getLabel;
		cellSupplier = button::getLabelCell;
		buttonRunnable = runnable;
		this.inputStrategySwitcher = inputStrategySwitcher;
		this.image = image;
		alignment = Alignment.CENTER;
		this.soundInteractListener = soundInteractListener;

		if (image != null) {
			Label label = labelSupplier.get();
			button.clearChildren();

			int sidePadding = label.getText().toString().isBlank() ? 0 : 3;
			float startWidth = label.getText().toString().isBlank() ? 0 : button.getWidth();

			button.add(image).padRight(sidePadding);
			button.add(label);

			button.setWidth(startWidth + image.getWidth() + sidePadding);
		}

		button.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				if (button.isTouchable() && button.isChecked() && !button.isDisabled()) {
					buttonRunnable.run();
					setUnchecked();
					requestInteractSound();
				}
			}
		});
	}

	/**
	 * This will play the interaction sound immediately if this button has {@link UniversalButton#doesSoundOnInteract} set to true (generally the default)
	 */
	protected void requestInteractSound() {
		if (doesSoundOnInteract) {
			soundInteractListener.run();
		}
	}

	@Override
	public Actor getView() {
		labelSupplier.get().setWrap(wrap);
		labelSupplier.get().setAlignment(alignment.getAlignment(), alignment.getAlignment());
		if (cellSupplier.get() != null) {
			cellSupplier.get().grow();
		}
		return button.getView();
	}

	public BasicButton getButton() {
		return button;
	}

	@Override
	public void consumeKeyInput(Input input) {
		if (input == Input.ACCEPT) {
			button.toggle();
		}
	}

	private void setUnchecked() {
		button.setProgrammaticChangeEvents(false);
		button.setChecked(false);
		button.setProgrammaticChangeEvents(true);
		setFocused(false);
	}

	/**
	 * Sets this button un/focused, generating a mimicked LibGDX mouse enter/exit event
	 * @param isFocused
	 */
	public void setFocused(boolean isFocused) {
		InputEvent event = Pools.obtain(InputEvent.class);
		if (!isFocused) {
			event.setType(InputEvent.Type.exit);
		}
		else if (inputStrategySwitcher.shouldFlashButtons()) {
			event.setType(InputEvent.Type.enter);
		}
		else {
			event.setType(null); // Since the events are pooled I think they can come with a type?! (the type of the last event it was used for?)
		}

		if (event.getType() != null) {
			event.setStage(button.getStage());
			Vector2 localToStageCoordinates = button.getView().localToStageCoordinates(new Vector2(0, 0));
			event.setStageX(localToStageCoordinates.x);
			event.setStageY(localToStageCoordinates.y);
			event.setPointer(-1);
			button.fire(event);
			Pools.free(event);
		}
	}

	/**
	 * Set whether or not this button can be interacted with
	 * @param disabled
	 */
	public void setDisabled(boolean disabled) {
		button.setDisabled(disabled);
	}

	@Override
	public void setTouchable(Touchable isTouchable) {
		button.setTouchable(isTouchable);
	}

	/**
	 * Replace the text on the button
	 * @param newText
	 */
	public void updateText(String newText) {
		textSupplier = () -> newText;
		labelSupplier.get().setText(newText);
	}

	/**
	 * @return Whether or not the button is blank
	 */
	public boolean isBlank() {
		return textSupplier.get().isBlank() && image == null;
	}

	/**
	 * Useful for trying to navigate to a particular button in a menu based on its text
	 * (e.g. defaulting to the current setting in a drop-down menu via string matching)
	 * @param value
	 * @return Whether or not this button has text that matches the supplied value
	 */
	public boolean doesTextMatch(String value) {
		return textSupplier.get().equalsIgnoreCase(value);
	}

	/**
	 * Set what to do when the button is pressed
	 * @param buttonRunnable
	 */
	public void setButtonRunnable(Runnable buttonRunnable) {
		this.buttonRunnable = buttonRunnable;
	}

	/**
	 * Set both alignments for the button's label. Quoting from the LibGDX documentation:
	 * 		labelAlign Aligns all the text within the label (default left center).
	 * 		lineAlign Aligns each line of text horizontally (default left).
	 * @param alignment The new alignment
	 */
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	/**
	 * Set whether or not the text in the button's label should wrap
	 * @param wrap
	 */
	public void setWrap(boolean wrap) {
		this.wrap = wrap;
	}

	/**
	 * Update both the button's text and image in one go
	 * @param textSupplier
	 * @param image
	 */
	public void updateLabels(Supplier<String> textSupplier, final Image image) {
		this.textSupplier = textSupplier;
		this.image.setDrawable(image.getDrawable());
	}

	/**
	 * Set whether or not this button should make a sound when interacted with
	 * @param doesSoundOnInteract
	 */
	public void setDoesSoundOnInteract(boolean doesSoundOnInteract) {
		this.doesSoundOnInteract = doesSoundOnInteract;
	}

	@Override
	public void gainFocus() {
		setFocused(true);
	}

	@Override
	public void loseFocus() {
		setFocused(false);
	}

	@Override
	public void focusCurrent() {
		setFocused(true);
	}

	@Override
	public void clearSelected() {
		setFocused(false);
	}

	@Override
	public void selectDefault() { /*A basic button doesn't have any nested components to select*/ }

	@Override
	public void resizeUI() {
		button.setStyle(button.getStyle());
		button.setSize(button.getPrefWidth(), button.getPrefHeight());
		labelSupplier.get().setText(textSupplier.get());
	}

}
