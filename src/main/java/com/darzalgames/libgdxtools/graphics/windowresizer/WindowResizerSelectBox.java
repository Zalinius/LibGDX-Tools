package com.darzalgames.libgdxtools.graphics.windowresizer;

import java.util.Collection;
import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer.ScreenMode;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardSelectBox;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public abstract class WindowResizerSelectBox extends KeyboardSelectBox {

	protected static WindowResizer windowResizer;
	
	protected WindowResizerSelectBox(Collection<String> entries, TextButton textButton, Consumer<String> action,
			InputStrategyManager inputStrategyManager) {
		super(entries, textButton, action, inputStrategyManager);
	}
	
	public static void setWindowResizer(WindowResizer windowResizer) {
		WindowResizerSelectBox.windowResizer = windowResizer;
	}
	
	protected abstract void setSelectedInBox(ScreenMode screenMode);
	protected abstract ScreenMode getModeFromPreference(String screenMode);
	protected abstract ConfirmationMenu getRevertMenu();
}
