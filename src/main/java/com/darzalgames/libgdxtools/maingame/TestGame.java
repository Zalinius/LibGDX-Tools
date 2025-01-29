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
import com.darzalgames.darzalcommon.functional.Consumers;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerButton;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerDesktop;
import com.darzalgames.libgdxtools.internationalization.BundleManager;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.platform.*;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.PauseMenu;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Priority;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.*;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.SkinManager;
import com.darzalgames.libgdxtools.ui.screen.MainMenuScreen;

public class TestGame extends MainGame {
	
	private final Consumer<TestGame> todo;
	
	
	protected UniversalButton quitButton;

	public static void main(String[] args) {
		TestGame.testLauncher(args, Consumers.nullConsumer());
	}

	static void testLauncher(String[] args, Consumer<TestGame> todo) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		int width = 1280;
		int height = 720;
		config.setWindowedMode(width, height);
		config.setTitle("Test LibGDXTools UI");
		config.setWindowListener(makeWindowListener());
		new Lwjgl3Application(new TestGame(width, height, args, todo), config);
	}


	public TestGame(int width, int height, String[] args, Consumer<TestGame> todo) {
		super(width/2, height/2, new WindowResizerDesktop(width, height),
				DesktopGamePlatformHelper.getTypeFromArgs(args, WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new));
		this.todo = todo;
	}

	@Override
	protected void initializeAssets() {
		TextSupplier.initialize(new BundleManager(null, new ArrayList<>()));
		UserInterfaceFactory.initialize(new SkinManager(SkinManager.getDefaultSkin()), inputStrategySwitcher, () -> 2.5f, Runnables.nullRunnable(), () -> pause.isPaused());
	}
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
		pause.showPauseButton(true);
		changeScreen(new MainMenuScreen(new NavigableListMenu(true, getMenuEntries()) {

			@Override
			protected void setUpTable() {
				defaults().align(Align.bottom);
				setBounds(0, 0, GameInfo.getWidth(), GameInfo.getHeight() - 25f);

				menu.setSpacing(1);
				menu.setAlignment(Alignment.CENTER, Alignment.BOTTOM);
				add(menu.getView()).grow().align(Align.center);
			}
		}, inputPriorityStack));
	}

	@Override
	protected KeyboardInputHandler makeKeyboardInputHandler() {
		return new KeyboardInputHandler(inputStrategySwitcher, inputReceiver) {
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

	protected List<UniversalButton> getMenuEntries() {
		List<UniversalButton> menuButtons = new ArrayList<>();

		UniversalSlider basicSlider = UserInterfaceFactory.getSlider("Slider with a label", newValue -> {});
		basicSlider.setSliderPosition(0.5f, false);
		menuButtons.add(basicSlider);

		UniversalCheckbox focusMute = UserInterfaceFactory.getCheckbox(
				"I am NOT checked!",
				"I am checked!",
				isChecked -> {});
		focusMute.initializeAsChecked(true);
		menuButtons.add(focusMute);

		String sliderInfo = "The below slider is at ";
		UniversalButton sliderInfoLabel = UserInterfaceFactory.getListableLabel(sliderInfo + "0.5");
		UniversalSlider funSlider = UserInterfaceFactory.getSlider("", newValue -> sliderInfoLabel.updateText(sliderInfo + String.format("%.1f", newValue)));
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
		Supplier<String> exampleSelectBoxLabelSupplier = () -> (TextSupplier.getLine("An option select box")); 
		Consumer<String> choiceResponder = selectedValue -> {};
		UniversalSelectBox exampleSelectBox = UserInterfaceFactory.getSelectBox( 
				exampleSelectBoxLabelSupplier.get(),
				List.of(instant, fast),
				choiceResponder
				);
		menuButtons.add(exampleSelectBox);
		
		menuButtons.add(UserInterfaceFactory.getSpacer());

		// Quit button
		quitButton = UserInterfaceFactory.getQuitGameButton();
		menuButtons.add(quitButton);

		return menuButtons;
	}



	@Override
	protected WindowResizerButton makeWindowResizerButton() {
		return UserInterfaceFactory.getWindowModeTextSelectBox();
	}
	
	private class TestOptionsMenu extends PauseMenu {

		protected TestOptionsMenu(Supplier<UniversalButton> makeWindowModeSelectBox) {
			super(makeWindowModeSelectBox, 0);
			optionsButton = UserInterfaceFactory.getSettingsButton(this::toggleScreenVisibility);
			optionsButton.getView().setWidth(optionsButton.getView().getHeight());
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
		protected Collection<UniversalButton> makeMiddleButtons() {
			return new ArrayList<>();
		}

		@Override
		protected UniversalButton makeButtonAboveQuitButton() {return null;}

		@Override
		protected PopUp makeControlsPopUp() {
			return new ConfirmationMenu("Did you really just press this?", "Sure did.", "My bad!", Runnables.nullRunnable());
		}

		@Override protected UniversalButton makeReportBugButton() {return UserInterfaceFactory.getButton("One could report a bug here", Runnables.nullRunnable());}
		@Override protected UniversalButton makeControlsButton() {
			return UserInterfaceFactory.getButton("This is where one could theoretically view controls", () -> Priority.claimPriority(makeControlsPopUp()));
		}

		@Override
		protected UniversalButton makeQuitButton() {
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
	protected Runnable drawConsole() {
		return Runnables.nullRunnable();
	}

	@Override
	protected void afterLaunch() {
		this.todo.accept(this);
	}

	@Override
	protected PauseMenu makePauseMenu() {
		return new TestOptionsMenu(windowResizer::getModeSelectBox);
	}

}
