package com.darzalgames.libgdxtools.graphics.windowresizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer.ScreenMode;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.ControlsGlyph;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalSelectBox;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UniversalTextButton;
import com.darzalgames.zalaudiolibrary.sfx.SoundEffect;

public class WindowResizerSelectBox extends UniversalSelectBox {

	WindowResizer windowResizer;
	private Supplier<ConfirmationMenu> confirmationMenuMaker;

	public WindowResizerSelectBox(String textKey, InputStrategySwitcher inputStrategySwitcher, ButtonStyle style, Consumer<SoundEffect> soundEffectConsumer, SoundEffect soundEffect, ControlsGlyph controlsGlyph) {
		super(textKey, inputStrategySwitcher, style, soundEffectConsumer, soundEffect, controlsGlyph);

		setResizeConfirmationMenuMaker(() -> new WindowRevertCountdownConfirmationMenu(this));
		setEntryButtons(getEntries());
	}

	public void setWindowResizer(WindowResizer windowResizer) {
		this.windowResizer = windowResizer;
	}

	public void setResizeConfirmationMenuMaker(Supplier<ConfirmationMenu> confirmationMenuMaker) {
		this.confirmationMenuMaker = confirmationMenuMaker;
	}

	private List<UniversalTextButton> getEntries() {
		List<ScreenMode> allModes = new ArrayList<>(Arrays.asList(ScreenMode.values()));
		if (!GameInfo.getOperatingSystem().supportsBorderlessFullscreen()) {
			allModes.remove(ScreenMode.BORDERLESS);
		}
		Stream<UniversalTextButton> buttonStream = allModes.stream().map(
				mode -> GameInfo.getUserInterfaceFactory().makeTextButton(
						() -> translateWindowModeOption(mode),
						() -> {
							ScreenMode previousMode = GameInfo.getPreferenceManager().graphics().getPreferredScreenMode();
							if (!mode.equals(previousMode)) {
								windowResizer.setMode(mode, true);
							}
							setSelected(mode.toString());
							closeSelectBox();
						}
				)
		);
		return buttonStream.toList();
	}

	public void setSelectedScreenMode(ScreenMode screenMode) {
		setSelected(translateWindowModeOption(screenMode));
	}

	public final ConfirmationMenu getRevertMenu() {
		return confirmationMenuMaker.get();
	}

	private static String translateWindowModeOption(ScreenMode screenMode) {
		return TextSupplier.getLine(screenMode.name().toLowerCase());
	}
}
