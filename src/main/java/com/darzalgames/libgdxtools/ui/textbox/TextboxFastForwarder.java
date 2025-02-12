package com.darzalgames.libgdxtools.ui.textbox;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;

public class TextboxFastForwarder {

	private UniversalButton fastForwardButton;
	private boolean isSkipping = false;

	public TextboxFastForwarder() {
		fastForwardButton = UserInterfaceFactory.getFastForwardButton(() -> { this.setSkipping(!isSkipping); });

		Button button = fastForwardButton.getView();
		button.setPosition(GameInfo.getWidth() - button.getWidth() - 2, GameInfo.getHeight() - button.getHeight() - 2);
	}
	
	public void setSkipping(boolean isSkipping) {
		this.isSkipping = isSkipping;
	}
	
	void pressSkipButton() {
		fastForwardButton.consumeKeyInput(Input.ACCEPT);
	}
	
	public UniversalButton getButton() {
		return fastForwardButton;
	}
	
	public void setInteractable(boolean interactable) {
		fastForwardButton.setTouchable(interactable ? Touchable.enabled : Touchable.disabled);
	}
	
	public boolean isSkipping() {
		return isSkipping;
	}
}
