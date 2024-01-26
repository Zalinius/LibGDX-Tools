package com.darzalgames.libgdxtools.ui.input.keyboard.button;

import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Pools;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.MainGame;
import com.darzalgames.libgdxtools.ui.GameObjectView;
import com.darzalgames.libgdxtools.ui.ListableAsButton;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumerWrapper;

public class KeyboardButton implements GameObjectView, InputConsumerWrapper, ListableAsButton {
	private TextButton button;
	private Supplier<Label> labelSupplier;
	private Supplier<Cell<Label>> cellSupplier;
	private Runnable buttonRunnable;
	private Image image;
	private int alignment;
	private boolean wrap;
	private boolean doesSoundOnInteract = true;
	
	static void setUpForLabelMaker() {
		LabelMaker.setPrivateKeyboardButtonConstructor(KeyboardButton::new);
	}

	KeyboardButton(TextButton button) {
		this(button, Runnables.nullRunnable());
	}
	
	KeyboardButton(TextButton button, Runnable runnable) { 
		this(button, null, runnable);
	}
	
	KeyboardButton(TextButton button, Image image, Runnable runnable) {
		this.button = button;
		this.labelSupplier = button::getLabel;
		this.cellSupplier = button::getLabelCell;
		this.buttonRunnable = runnable;
		this.image = image;
		this.wrap = true;

		if (image != null) {
			float startWidth = button.getWidth();
			Label label = labelSupplier.get();
			button.clearChildren();

			button.add(image).padRight(3);
			button.add(label);
			
			button.setWidth(startWidth + image.getWidth() + 3);
		}
		
		labelSupplier.get().setTouchable(Touchable.disabled);
		

		button.addListener(new ChangeListener() {
			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				if (button.isTouchable() && button.isChecked() && !button.isDisabled()) {
					buttonRunnable.run();
					setUnchecked();
				}
			}
		});
	}

	/**
	 * This will play the interaction sound immediately if this button has {@link KeyboardButton#doesSoundOnInteract} set to true (generally the default)
	 */
	protected void requestInteractSound() {
		if (doesSoundOnInteract) {
			// TODO uncomment this once the audio bits are in this library (libgdx tools) 
//			QuestGiverGame.music.playSound(SoundEffects.inputSoundEffect());
		}
	}
	
	@Override
	public TextButton getView() {
		labelSupplier.get().setWrap(wrap);
		labelSupplier.get().setAlignment(alignment, alignment);
		if (cellSupplier.get() != null) {
			cellSupplier.get().grow();
		}
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

	public void setFocused(boolean isFocused) {
		InputEvent event = Pools.obtain(InputEvent.class);
		if (!isFocused) {
			event.setType(InputEvent.Type.exit);
		}
		if (isFocused && MainGame.getInputStrategyManager().shouldFlashButtons()) {
			event.setType(InputEvent.Type.enter);
		}
		
		if (event.getType() != null) {
			event.setStage(button.getStage());
			event.setStageX(0);
			event.setStageY(0);
			event.setRelatedActor(button);
			event.setPointer(-1);
			button.fire(event);
			Pools.free(event);
		}
	}

	public void setDisabled(boolean disabled) {
		button.setDisabled(disabled);
	}

	@Override
	public void setTouchable(Touchable isTouchable) {
		button.setTouchable(isTouchable);
	}

	public void updateText(String newText) {
		labelSupplier.get().setText(newText);
	}

	public String getButtonText() {
		return labelSupplier.get().getText().toString();
	}

	public void setButtonRunnable(Runnable buttonRunnable) {
		this.buttonRunnable = buttonRunnable;
	}

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	public void setWrap(boolean wrap) {
		this.wrap = wrap;
	}
	
	public void updateLabels(final String newText, final Image image) {
		updateText(newText);
		this.image.setDrawable(image.getDrawable());
	}

	@Override
	public KeyboardButton getListableButton() {
		return this;
	}

	public void setDoesSoundOnInteract(boolean doesSoundOnInteract) {
		this.doesSoundOnInteract = doesSoundOnInteract;
	}
}
