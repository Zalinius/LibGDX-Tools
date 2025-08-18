package com.darzalgames.libgdxtools.maingame;

import java.util.*;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.functional.Runnables;
import com.darzalgames.darzalcommon.functional.Suppliers;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerButton;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerDesktop;
import com.darzalgames.libgdxtools.internationalization.BundleManager;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.platform.*;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.inputpriority.OptionsMenu;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;
import com.darzalgames.libgdxtools.ui.input.popup.ChoicePopUp;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.popup.SimplePopUp;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.*;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.SkinManager;
import com.darzalgames.libgdxtools.ui.screen.MainMenuScreen;

public class SampleUserInterfaceGame extends MainGame {

	public static final String POP_UP_STAGE_NAME = "PopUp Stage";

	private UniversalButton regainFocusPopup;
	private UniversalButton quitButton;

	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1280, 720);
		config.setTitle("Test LibGDXTools UI");
		config.setWindowListener(makeWindowListener());
		new Lwjgl3Application(new SampleUserInterfaceGame(Arrays.asList(args)), config);
	}



	public SampleUserInterfaceGame(List<String> args) {
		super(new WindowResizerDesktop(), LaunchArgumentHelper.getGamePlatform(args, WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new));
	}

	@Override
	protected Screen startLoadingGameAssets() {
		return null;
	}
	@Override
	protected boolean loadGameAssets() {
		return true;
	}

	@Override
	protected UserInterfaceFactory initializeGameAndUserInterfaceFactory() {
		UserInterfaceFactory factory = new UserInterfaceFactory(new SkinManager(SkinManager.getDefaultSkin()), inputStrategySwitcher, () -> 2.5f, Runnables.nullRunnable(), () -> pause.isPaused());
		TextSupplier.initialize(new BundleManager(null, new ArrayList<>()));
		return factory;
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
		pause.showOptionsButton(true);
		changeScreen(new MainMenuScreen(new NavigableListMenu(true, getMenuEntries()) {

			@Override
			protected void setUpTable() {
				defaults().align(Align.bottom);
				setBounds(0, 0, UserInterfaceSizer.getCurrentWidth(), UserInterfaceSizer.getCurrentHeight() - 25f);

				menu.setSpacing(1);
				menu.setAlignment(Alignment.CENTER, Alignment.BOTTOM);
				add(menu.getView()).grow().align(Align.center);
			}

			@Override
			public void resizeUI() {
				setBounds(0, 0, UserInterfaceSizer.getCurrentWidth(), UserInterfaceSizer.getCurrentHeight() - 25f);
			}
		}, inputSetup.getInputPriorityStack()));
	}

	@Override
	protected KeyboardInputHandler makeKeyboardInputHandler() {
		return new KeyboardInputHandler(inputStrategySwitcher, inputSetup.getInputReceiver()) {
			@Override
			protected Input remapInputIfNecessary(Input input, int keycode) {
				return input;
			}
			@Override
			protected List<Input> getKeyWhitelist() {
				List<Input> keysToAllow = new ArrayList<>();
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
	protected Actor makeBackground() {
		NinePatchDrawable gray = new NinePatchDrawable(new NinePatch(ColorTools.getColoredTexture(Color.LIGHT_GRAY, 3), 1, 1, 1, 1));
		Image grayImage = new Image(gray);
		grayImage.setFillParent(true);
		return grayImage;
	}

	@Override
	protected String getPreferenceManagerName() {
		return "com.darzalgames.libgdxtools.preferences";
	}

	protected List<UniversalButton> getMenuEntries() {
		List<UniversalButton> menuButtons = new ArrayList<>();

		UniversalSlider basicSlider = GameInfo.getUserInterfaceFactory().getSlider(() -> "Slider with a label", newValue -> {});
		basicSlider.setSliderPosition(0.5f, false);
		menuButtons.add(basicSlider);

		UniversalCheckbox focusMute = GameInfo.getUserInterfaceFactory().getCheckbox(
				() -> "I am NOT checked!",
				() -> "I am checked!",
				isChecked -> {});
		focusMute.initializeAsChecked(true);
		menuButtons.add(focusMute);

		String sliderInfo = "The below slider is at ";
		UniversalButton sliderInfoLabel = GameInfo.getUserInterfaceFactory().getListableLabel(() -> sliderInfo + "0.5");
		UniversalSlider funSlider = GameInfo.getUserInterfaceFactory().getSlider(Suppliers.emptyString(), newValue -> sliderInfoLabel.updateText(sliderInfo + String.format("%.1f", newValue)));
		funSlider.setSliderPosition(0.5f, false);
		menuButtons.add(sliderInfoLabel);
		menuButtons.add(funSlider);

		String logOrigin = "LibGDXTools Test Game";
		menuButtons.add(GameInfo.getUserInterfaceFactory().getButton(() -> "Text button!", () -> Gdx.app.log(logOrigin, "You pressed the text button")));

		menuButtons.add(GameInfo.getUserInterfaceFactory().getButton(new Image(ColorTools.getColoredTexture(Color.GOLD, 50, 12)), () -> Gdx.app.log(logOrigin, "You pressed the image button")));

		menuButtons.add(GameInfo.getUserInterfaceFactory().getButton(() -> "Image Text button!", new Image(ColorTools.getColoredTexture(Color.CHARTREUSE, 50, 12)),
				() -> Gdx.app.log(logOrigin, "You pressed the image text button")));

		Supplier<String> option1 = () -> TextSupplier.getLine("option 1");
		Supplier<String> option2 = () -> TextSupplier.getLine("option 2");
		Supplier<String> exampleSelectBoxLabelSupplier = () -> (TextSupplier.getLine("An option select box"));
		UniversalSelectBox exampleSelectBox = GameInfo.getUserInterfaceFactory().getSelectBox(
				exampleSelectBoxLabelSupplier,
				List.of(userInterfaceFactory.getButton(option1, Runnables.nullRunnable()), userInterfaceFactory.getButton(option2, Runnables.nullRunnable()))
				);
		menuButtons.add(exampleSelectBox);

		ChoicePopUp choicePopup = new ChoicePopUp(this::showInnerPopUp, true, true) {

			boolean showedRegainFocusPopup = false;

			@Override
			public void regainFocus() {
				if (!showedRegainFocusPopup) {
					showRegainFocusPopup();
					showedRegainFocusPopup = true;
				}
			}

			@Override
			protected UniversalButton getFirstChoiceButton() {
				return GameInfo.getUserInterfaceFactory().getButton(() -> "Click to go deeper!", SampleUserInterfaceGame.this::showInnerPopUp);
			}

			@Override
			protected UniversalButton getSecondChoiceButton() {
				return GameInfo.getUserInterfaceFactory().getButton(() -> "Goodbye!", this::hideThis);
			}

			@Override
			protected Runnable getSecondChoiceRunnable() {
				return this::hideThis;
			}

			@Override
			protected Table getMessage() {
				return new Table();
			}

			@Override
			protected void setUpDesiredSize() {
				UserInterfaceSizer.sizeToPercentage(this, 0.25f);
			}
		};
		UniversalButton popUpButton = GameInfo.getUserInterfaceFactory().getButton(() -> "Open a popup!", () -> InputPriority.claimPriority(choicePopup, POP_UP_STAGE_NAME));
		menuButtons.add(popUpButton);


		menuButtons.add(GameInfo.getUserInterfaceFactory().getSpacer());

		// Quit button
		quitButton = GameInfo.getUserInterfaceFactory().getQuitGameButton();
		menuButtons.add(quitButton);

		return menuButtons;
	}

	private void showInnerPopUp() {
		SimplePopUp innerPopup = new SimplePopUp() {
			@Override
			protected void setUpTable() {
				UniversalButton popup = GameInfo.getUserInterfaceFactory().getButton(() -> "Goodbye!", this::hideThis);
				popup.getView().setSize(180, 100);
				UserInterfaceSizer.makeActorCentered(popup.getView());
				addActor(popup.getView());
			}

			@Override
			public void resizeUI() { /*Not needed*/ }
		};
		InputPriority.claimPriority(innerPopup, POP_UP_STAGE_NAME);
	}

	private void showRegainFocusPopup() {
		SimplePopUp innerPopup = new SimplePopUp() {
			@Override
			protected void setUpTable() {
				regainFocusPopup = GameInfo.getUserInterfaceFactory().getButton(() -> "You Made It!", this::hideThis);
				regainFocusPopup.getView().setSize(200, 130);
				UserInterfaceSizer.makeActorCentered(regainFocusPopup.getView());
				addActor(regainFocusPopup.getView());
			}

			@Override
			public void resizeUI() { /*Not needed*/ }
		};
		InputPriority.claimPriority(innerPopup, POP_UP_STAGE_NAME);
	}



	@Override
	protected WindowResizerButton makeWindowResizerButton() {
		return GameInfo.getUserInterfaceFactory().getWindowModeTextSelectBox();
	}

	private class TestOptionsMenu extends OptionsMenu {

		protected TestOptionsMenu(Supplier<UniversalButton> makeWindowModeSelectBox) {
			super(makeWindowModeSelectBox, 0);
			optionsButton = GameInfo.getUserInterfaceFactory().getOptionsButton(this::toggleScreenVisibility);
			optionsButton.getView().setWidth(optionsButton.getView().getHeight());
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
			return new ConfirmationMenu("Did you really just press this?", "Sure did.", "My bad!", Runnables.nullRunnable(), POP_UP_STAGE_NAME);
		}

		@Override protected UniversalButton makeReportBugButton() {return GameInfo.getUserInterfaceFactory().getButton(() -> "One could report a bug here", Runnables.nullRunnable());}
		@Override protected UniversalButton makeControlsButton() {
			return GameInfo.getUserInterfaceFactory().getButton(() -> "This is where one could theoretically view controls", () -> InputPriority.claimPriority(makeControlsPopUp(), MultipleStage.OPTIONS_STAGE_NAME));
		}

		@Override
		protected UniversalButton makeQuitButton() {
			return GameInfo.getUserInterfaceFactory().getQuitGameButton();
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
	protected Runnable getDrawConsoleRunnable() {
		return Runnables.nullRunnable();
	}

	@Override
	protected OptionsMenu makeOptionsMenu() {
		return new TestOptionsMenu(windowResizer::getModeSelectBox);
	}

	@Override
	public String getGameName() {
		return "Test LibGDXTools UI";
	}

	@Override
	public String getGameVersion() {
		return "game version";
	}

	@Override
	public GameEdition getGameEdition() {
		return GameEdition.FULL;
	}

	@Override
	protected List<StageLikeRenderable> makeGameSpecificStages() {
		return List.of(makeAllPurposeStage(POP_UP_STAGE_NAME));
	}

	@Override
	protected void resizeGameSpecificUI() {
		// N/A
	}

}
