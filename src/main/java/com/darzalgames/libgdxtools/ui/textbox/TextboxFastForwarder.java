package com.darzalgames.libgdxtools.ui.textbox;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.UserInterfaceFactory;

public class TextboxFastForwarder {

	private KeyboardButton fastForwardButton;
	private boolean isSkipping = false;

	public TextboxFastForwarder() {
		fastForwardButton = UserInterfaceFactory.getFastForwardButton(() -> { this.setSkipping(!isSkipping); });
		InputPriorityManager.addSpecialButton(Input.SKIP, fastForwardButton);

		Button button = fastForwardButton.getView();
		button.setPosition(GameInfo.getWidth() - button.getWidth() - 2, GameInfo.getHeight() - button.getHeight() - 2);
	}
	
	public void setSkipping(boolean isSkipping) {
		this.isSkipping = isSkipping;
	}
	
	public KeyboardButton getButton() {
		return fastForwardButton;
	}
	
	public void setInteractable(boolean interactable) {
		fastForwardButton.setTouchable(interactable ? Touchable.enabled : Touchable.disabled);
	}
	
	public boolean isSkipping() {
		return isSkipping;
	}
}
