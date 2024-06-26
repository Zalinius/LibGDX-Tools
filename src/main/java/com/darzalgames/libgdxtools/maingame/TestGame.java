package com.darzalgames.libgdxtools.maingame;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerButton;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerDesktop;
import com.darzalgames.libgdxtools.internationalization.BundleManager;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.platform.DesktopGamePlatformHelper;
import com.darzalgames.libgdxtools.platform.LinuxGamePlatform;
import com.darzalgames.libgdxtools.platform.WindowsGamePlatform;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.*;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.skinmanager.SkinManager;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.optionsmenu.OptionsMenu;
import com.darzalgames.libgdxtools.ui.screen.MainMenuScreen;

public class TestGame extends MainGame {

	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		int width = 1280;
		int height = 720;
		config.setWindowedMode(width, height);
		config.setTitle("Test LibGDXTools Game");
		config.setWindowListener(makeWindowListener());
		new Lwjgl3Application(new TestGame(width, height, args), config);
	}


	public TestGame(int width, int height, String[] args) {
		super(width/2, height/2, new WindowResizerDesktop(width, height),
				DesktopGamePlatformHelper.getTypeFromArgs(args, WindowsGamePlatform::new, LinuxGamePlatform::new));
	}

	@Override
	protected void initializeAssets() {/* notYetNeeded */}
	@Override
	protected DesktopSaveManager makeSaveManager() {
		return new DesktopSaveManager() {
			@Override
			public void save() {/* notYetNeeded */}
			@Override
			public boolean load() { return true; }
		};
	}
	@Override
	protected void setUpBeforeLoadingSave() {/* notYetNeeded */}

	@Override
	protected void launchGame(boolean isNewSave) {
		TextSupplier.initialize(new BundleManager(null, new ArrayList<>()));
		UserInterfaceFactory.initialize(new SkinManager(SkinManager.getDefaultSkin()), inputStrategyManager, () -> 2.5f, Runnables.nullRunnable());
		changeScreen(new MainMenuScreen(new NavigableListMenu(true, getMenuEntries()) {

			@Override
			protected void setUpTable() {
				defaults().align(Align.bottom);
				setBounds(0, 0, GameInfo.getWidth(), GameInfo.getHeight() - 25f);

				menu.setSpacing(1);
				menu.setAlignment(Alignment.CENTER, Alignment.BOTTOM);
				add(menu.getView()).growX().align(Align.center);


				// Options button
				TestOptionsMenu optionsMenu = new TestOptionsMenu(windowResizer::getModeSelectBox);
				addActor(optionsMenu.getButton().getView());
				optionsMenu.getButton().getView().setPosition(3, GameInfo.getHeight() - optionsMenu.getButton().getView().getHeight() - 3);
				InputPriorityManager.setPauseUI(optionsMenu);
			}
		}));
	}

	@Override
	protected KeyboardInputHandler makeKeyboardInputHandler() {
		return new KeyboardInputHandler(inputStrategyManager) {
			@Override
			protected Input remapInputIfNecessary(Input input, int keycode) {
				return input;
			}
			@Override
			protected List<Input> getKeyWhitelist() {
				List<Input> keysToAllow = new ArrayList<>();
				Input.ACCEPT.replaceKey(Keys.ENTER);
				keysToAllow.add(Input.ACCEPT);
				keysToAllow.add(Input.BACK);
				keysToAllow.add(Input.UP);
				keysToAllow.add(Input.DOWN);
				keysToAllow.add(Input.LEFT);
				keysToAllow.add(Input.RIGHT);
				keysToAllow.add(Input.PAUSE);
				keysToAllow.add(Input.TOGGLE_FULLSCREEN);
				return keysToAllow;
			}

			@Override
			public Texture getGlyphForInput(Input input) {
				return null;
			}
			@Override
			protected Map<Input, AssetDescriptor<Texture>> makeButtonMappings() {
				return Collections.emptyMap();
			}
		};
	}

	@Override
	protected void quitGame() {/* notYetNeeded */}

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
			optionsButton = UserInterfaceFactory.getInGamesSettingsButton(() -> toggleScreenVisibility(true));
			optionsButton.getView().setWidth(optionsButton.getView().getHeight());
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

		@Override
		protected KeyboardButton makeQuitButton() {
			return UserInterfaceFactory.getQuitGameButton();
		}

	}

	private static Lwjgl3WindowListener makeWindowListener() {
		return new Lwjgl3WindowListener() {
			@Override
			public void focusLost() {
				// TODO uncomment these once the audio stuff is in this library
				//QuestGiverGame.music.temporarilyMute();
			}
			@Override
			public void focusGained() {
				//QuestGiverGame.music.untemporarilyMute();
			}
			@Override public void iconified(boolean isIconified) {/* notYetNeeded */}
			@Override public void created(Lwjgl3Window window) {/* notYetNeeded */}
			@Override public void maximized(boolean isMaximized) {/* notYetNeeded */}
			@Override public boolean closeRequested() {return true;}
			@Override public void filesDropped(String[] files) {/* notYetNeeded */}
			@Override public void refreshRequested() {/* notYetNeeded */}
		};
	}


	@Override
	protected WindowResizerButton makeWindowResizerButton() {
		return UserInterfaceFactory.getWindowModeTextSelectBox();
	}

}
