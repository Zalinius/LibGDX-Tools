package com.darzalgames.libgdxtools.maingame;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.codedisaster.steamworks.SteamController;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.graphics.WindowResizerDesktop;
import com.darzalgames.libgdxtools.i18n.TextSupplier;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.*;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.skinmanager.SkinManager;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.scrollablemenu.ScrollableMenu;
import com.darzalgames.libgdxtools.ui.optionsmenu.OptionsMenu;
import com.darzalgames.libgdxtools.ui.screen.MainMenuScreen;

public class TestGame {

	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		int width = 1280;
		int height = 720;
		config.setWindowedMode(width, height);
		config.setTitle("Test LibGDXTools Game");
		config.setWindowListener(new Lwjgl3WindowListener() {
			@Override
			public void focusLost() {
				// TODO uncomment these once the audio stuff is in this library
				//QuestGiverGame.music.temporarilyMute();
			}
			@Override
			public void focusGained() {
				//QuestGiverGame.music.untemporarilyMute();
			}
			@Override public void iconified(boolean isIconified) {}
			@Override public void created(Lwjgl3Window window) {}
			@Override public void maximized(boolean isMaximized) {}
			@Override public boolean closeRequested() {return true;}
			@Override public void filesDropped(String[] files) {}
			@Override public void refreshRequested() {}
		});

		new Lwjgl3Application(new MainGame(width/2, height/2, new WindowResizerDesktop(width, height)) {

			@Override
			protected void initializeAssets() {}
			@Override
			protected SaveManager makeSaveManager() {
				return new SaveManager() {
					@Override
					public void save() {}
					@Override
					public boolean load() { return true; }
				};
			}
			@Override
			protected void setUpBeforeLoadingSave() {}

			@Override
			protected void launchGame(boolean isNewSave) {
				TextSupplier.setThrowExceptions(false); // To get direct text back rather than using them as keys
				UserInterfaceFactory.initialize(new SkinManager(SkinManager.getDefaultSkin()));
				changeScreen(new MainMenuScreen(new ScrollableMenu(true, getMenuEntries()) {

					@Override
					protected void setUpTable() {
						defaults().align(Align.bottom);
						setBounds(0, 0, GameInfo.getWidth(), GameInfo.getHeight() - 25);

						menu.setSpacing(1);
						menu.setAlignment(Alignment.CENTER, Alignment.BOTTOM);
						add(menu.getView()).growX().align(Align.center);
					}
				}));
			}

			@Override
			protected KeyboardInputHandler makeKeyboardInputHandler() {
				return new KeyboardInputHandler() {
					@Override
					protected Input remapInputIfNecessary(Input input, int keycode) {
						return input;
					}
					@Override
					protected List<Input> getKeyWhitelist() {
						List<Input> keysToAllow = new ArrayList<>();
						keysToAllow.addAll(Arrays.asList(Input.values()));
						keysToAllow.remove(Input.NONE);
						return keysToAllow;
					}
				};
			}

			@Override
			protected GamepadInputHandler makeGamepadInputHandler(SteamController steamController) {
				return new GamepadInputHandler() {
					@Override
					protected List<Input> getTrackedInputs() {
						return new ArrayList<>();
					}
				};
			}

			@Override
			protected void quitGame() {}

			@Override
			protected Texture getBackgroundStageTexture() {
				return ColorTools.getColoredTexture(Color.PINK, 100);
			}

			@Override
			protected Texture getMainStageBackgroundTexture() {
				return ColorTools.getColoredTexture(Color.LIGHT_GRAY, width, height);
			}

			@Override
			protected String getPreferenceManagerName() {
				return "com.darzalgames.libgdxtools.preferences";
			}

		}, config);
	}

	protected static List<KeyboardButton> getMenuEntries() {
		List<KeyboardButton> menuButtons = new ArrayList<>();

		KeyboardSlider basicSlider = UserInterfaceFactory.getSlider("Slider with a label", newValue -> {});
		basicSlider.setSliderPosition(0.5f, false);
		menuButtons.add(basicSlider);

		KeyboardCheckbox focusMute = UserInterfaceFactory.getCheckbox(
				"I am NOT checked!",
				"I am checked!",
				isChecked -> {});
		focusMute.initializeAsChecked(true);
		menuButtons.add(focusMute);

		String sliderInfo = "The below slider is at ";
		KeyboardButton sliderInfoLabel = UserInterfaceFactory.getListableLabel(sliderInfo + "0.5");
		KeyboardSlider funSlider = UserInterfaceFactory.getSlider("", newValue -> sliderInfoLabel.updateText(sliderInfo + String.format("%.1f", newValue)));
		funSlider.setSliderPosition(0.5f, false);
		menuButtons.add(sliderInfoLabel);
		menuButtons.add(funSlider);

		String logOrigin = "LibGDXTools Test Game";
		menuButtons.add(UserInterfaceFactory.getButton("Text button!", () -> Gdx.app.log(logOrigin, "You pressed the text button")));

		menuButtons.add(UserInterfaceFactory.getButton(new Image(ColorTools.getColoredTexture(Color.GOLD, 50, 12)), () -> Gdx.app.log(logOrigin, "You pressed the image button")));

		menuButtons.add(UserInterfaceFactory.getButton("Image Text button!", new Image(ColorTools.getColoredTexture(Color.CHARTREUSE, 50, 12)), 
				() -> Gdx.app.log(logOrigin, "You pressed the image text button")));

		// Options button
		TestOptionsMenu optionsMenu = new TestOptionsMenu(() -> UserInterfaceFactory.getButton("window resize", Runnables.nullRunnable()));
		menuButtons.add(optionsMenu.getButton());

		// the textSpeed selectbox
		String instant = TextSupplier.getLine("option 1");
		String fast = TextSupplier.getLine("option 2");
		Supplier<String> textSpeedLabelSupplier = () -> (TextSupplier.getLine("An option select box")); 
		Consumer<String> choiceResponder = selectedNewtextSpeed -> {};
		KeyboardSelectBox textSpeedSelectBox = UserInterfaceFactory.getSelectBox( 
				textSpeedLabelSupplier.get(),
				List.of(instant, fast),
				choiceResponder
				);
		menuButtons.add(textSpeedSelectBox);

		// Quit button
		menuButtons.add(UserInterfaceFactory.getQuitGameButton());

		return menuButtons;
	}

	private static class TestOptionsMenu extends OptionsMenu {

		protected TestOptionsMenu(Supplier<KeyboardButton> makeWindowModeSelectBox) {
			super(makeWindowModeSelectBox, 0);
			optionsButton = UserInterfaceFactory.getButton("options", () -> toggleScreenVisibility(true));
			InputPriorityManager.setPauseUI(this);
		}

		@Override
		protected void setUpBackground() {
			this.setBackground(new Image(ColorTools.getColoredTexture(Color.BROWN, 1)).getDrawable());
			this.setSize(500, 300);
			UserInterfaceFactory.makeActorCentered(this);
		}

		@Override protected Alignment getEntryAlignment() {return Alignment.CENTER;}
		@Override protected Alignment getMenuAlignment() {return Alignment.CENTER;}
		@Override protected String getGameVersion() {return "version goes here";}

		@Override
		protected Collection<KeyboardButton> makeMiddleButtons() {
			return new ArrayList<>();
		}

		@Override
		protected KeyboardButton makeButtonAboveQuitButton() {return null;}

		@Override
		protected PopUp makeControlsPopUp() {
			return new ConfirmationMenu("Did you really just press this?", "Sure did.", "My bad!", Runnables.nullRunnable());
		}

		@Override protected KeyboardButton makeReportBugButton() {return UserInterfaceFactory.getButton("One could report a bug here", Runnables.nullRunnable());}
		@Override protected KeyboardButton makeControlsButton() {
			return UserInterfaceFactory.getButton("This is where one could theoretically view controls", () -> InputPriorityManager.claimPriority(makeControlsPopUp()));
		}

	}

}
