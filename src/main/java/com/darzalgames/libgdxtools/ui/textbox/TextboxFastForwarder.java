package com.darzalgames.libgdxtools.ui.textbox;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;

public class TextboxFastForwarder {

	private static KeyboardButton fastForwardButton;
	private static boolean isSkipping = false;

	public static void initialize(Function<Runnable, KeyboardButton> fastForwardButtonFactory) {
		fastForwardButton = fastForwardButtonFactory.apply(() -> { TextboxFastForwarder.setSkipping(!isSkipping); });
		InputPriorityManager.addSpecialButton(Input.SKIP, fastForwardButton);

		Button button = fastForwardButton.getView();
		button.setPosition(GameInfo.getWidth() - button.getWidth() - 2, GameInfo.getHeight() - button.getHeight() - 2);
	}
	
	public static void setSkipping(boolean isSkipping) {
		TextboxFastForwarder.isSkipping = isSkipping;
	}
	
	public static KeyboardButton getButton() {
		return fastForwardButton;
	}
	
	public static void setInteractable(boolean interactable) {
		fastForwardButton.setTouchable(interactable ? Touchable.enabled : Touchable.disabled);
	}
	
	public static boolean isSkipping() {
		return isSkipping;
	}
}
