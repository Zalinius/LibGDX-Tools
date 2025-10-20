package com.darzalgames.libgdxtools.maingame;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.controllers.Controller;
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
import com.darzalgames.libgdxtools.assetloading.BlankLoadingScreen;
import com.darzalgames.libgdxtools.assetloading.LoadingScreen;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerDesktop;
import com.darzalgames.libgdxtools.internationalization.BundleManager;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.platform.*;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.ConfirmationMenu;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;
import com.darzalgames.libgdxtools.ui.input.handler.FallbackGamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputPriority;
import com.darzalgames.libgdxtools.ui.input.inputpriority.OptionsMenu;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.MenuOrientation;
import com.darzalgames.libgdxtools.ui.input.navigablemenu.NavigableListMenu;
import com.darzalgames.libgdxtools.ui.input.popup.ChoicePopUp;
import com.darzalgames.libgdxtools.ui.input.popup.PopUp;
import com.darzalgames.libgdxtools.ui.input.popup.SimplePopUp;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.*;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.skinmanager.SkinManager;
import com.darzalgames.libgdxtools.ui.screen.MainMenuScreen;

public class SampleUserInterfaceGame extends MainGame {

	public static final String POP_UP_STAGE_NAME = "PopUp Stage";

	private UniversalButton regainFocusPopup;

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
	protected void beginLoadingAssets() { /* This project is purposefully assetless */}

	@Override
	protected UserInterfaceFactory initializeGameAndUserInterfaceFactory() {
		FallbackGamepadInputHandler fallbackRef = new FallbackGamepadInputHandler(inputStrategySwitcher, null) {
			@Override
			public void setActionSet(Supplier<String> newActionSetKeySupplier) { /* This project is not complex enough to need ActionSets */}

			@Override
			protected List<Input> getTrackedInputs() {
				return List.of();
			}

			@Override
			protected Texture getTextureFromDescriptor(AssetDescriptor<Texture> descriptor) {
				return null;
			}

			@Override
			protected Map<Input, AssetDescriptor<Texture>> makeGlyphMappings() {
				return new HashMap<>();
			}

			@Override
			protected Map<Function<Controller, Integer>, Input> makeButtonMappings() {
				return new HashMap<>();
			}
		};
		UserInterfaceFactory factory = new UserInterfaceFactory(new SkinManager(SkinManager.getDefaultSkin()), inputStrategySwitcher, Runnables.nullRunnable(), fallbackRef) {
			@Override
			protected void addGameSpecificHighlightListener(UniversalDoodad button) { /* do nothing */ }
		};
		TextSupplier.initialize(new BundleManager(null, new ArrayList<>()));
		return factory;
	}

	@Override
	protected DesktopSaveManager makeSaveManager() {
		return new DesktopSaveManager() {
			@Override
			public void save() {/* notYetNeeded */}

			@Override
			public boolean load() {
				return true;
			}
		};
	}

	@Override
	protected void setUpBeforeLoadingSave() {/* notYetNeeded */}

	@Override
	protected void launchGame(boolean isNewSave) {
		pause.showOptionsButton(true);
		changeScreen(new MainMenuScreen(new NavigableListMenu(MenuOrientation.VERTICAL, getMenuEntries()) {

			@Override
			protected void setUpTable() {
				defaults().align(Align.bottom);
				setBounds(0, 0, UserInterfaceSizer.getCurrentWidth(), UserInterfaceSizer.getCurrentHeight() - 25f);

				menu.setAlignment(Alignment.CENTER, Alignment.BOTTOM);
				add(menu.getView()).grow().align(Align.center);
			}

			@Override
			public void resizeUI() {
				super.resizeUI();
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

	protected List<VisibleInputConsumer> getMenuEntries() {
		UniversalButton quitButton;
		List<VisibleInputConsumer> menuButtons = new ArrayList<>();

		UniversalSlider basicSlider = GameInfo.getUserInterfaceFactory().getSlider(() -> "Slider with a label", newValue -> {});
		basicSlider.setSliderPosition(0.5f, false);
		menuButtons.add(basicSlider);

		UniversalCheckbox focusMute = GameInfo.getUserInterfaceFactory().getCheckbox(
				() -> "I am NOT checked!",
				() -> "I am checked!",
				isChecked -> {}
		);
		focusMute.initializeAsChecked(true);
		menuButtons.add(focusMute);

		String sliderInfo = "The below slider is at ";
		UniversalLabel sliderInfoLabel = GameInfo.getUserInterfaceFactory().getLabel(() -> sliderInfo + "0.5");
		UniversalSlider funSlider = GameInfo.getUserInterfaceFactory().getSlider(Suppliers.emptyString(), newValue -> sliderInfoLabel.setTextSupplier(() -> sliderInfo + String.format("%.1f", newValue)));
		funSlider.setSliderPosition(0.5f, false);
		menuButtons.add(funSlider);

		String logOrigin = "LibGDXTools Test Game";
		menuButtons.add(GameInfo.getUserInterfaceFactory().makeTextButton(() -> "Text button!", () -> Gdx.app.log(logOrigin, "You pressed the text button")));

		menuButtons.add(GameInfo.getUserInterfaceFactory().getImageButton(new Image(ColorTools.getColoredTexture(Color.GOLD, 50, 12)), () -> Gdx.app.log(logOrigin, "You pressed the image button")));

		Supplier<String> option1 = () -> TextSupplier.getLine("option 1");
		Supplier<String> option2 = () -> TextSupplier.getLine("looooong option 2");
		String exampleSelectBoxLabelSupplier = "An option select box";
		SelectBoxContentManager selectBoxContents = new SelectBoxContentManager() {
			@Override
			public List<SelectBoxButtonInfo> getOptionButtons() {
				return List.of(
						new SelectBoxButtonInfo(option1, () -> Gdx.app.log("Select Box", "You picked " + option1.get())),
						new SelectBoxButtonInfo(option2, () -> Gdx.app.log("Select Box", "You picked " + option2.get()))
				);
			}

			@Override
			public Supplier<String> getCurrentSelectedDisplayName() {
				return option1;
			}

			@Override
			public String getBoxLabelKey() {
				return exampleSelectBoxLabelSupplier;
			}

		};
		UniversalSelectBox exampleSelectBox = GameInfo.getUserInterfaceFactory().getSelectBox(selectBoxContents);
		menuButtons.add(exampleSelectBox);

		ChoicePopUp choicePopup = new ChoicePopUp(this::showInnerPopUp, MenuOrientation.VERTICAL) {

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
				return GameInfo.getUserInterfaceFactory().makeTextButton(() -> "Click to go deeper!", SampleUserInterfaceGame.this::showInnerPopUp);
			}

			@Override
			protected UniversalButton getSecondChoiceButton() {
				return GameInfo.getUserInterfaceFactory().makeTextButton(() -> "Goodbye!", this::hideThis);
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

			@Override
			protected boolean doesBackInputPressSecondButton() {
				return true;
			}
		};
		UniversalButton popUpButton = GameInfo.getUserInterfaceFactory().makeTextButton(() -> "Open a popup!", () -> InputPriority.claimPriority(choicePopup, POP_UP_STAGE_NAME));
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
				UniversalButton popup = GameInfo.getUserInterfaceFactory().makeTextButton(() -> "Goodbye!", this::hideThis);
				popup.getView().setSize(180, 100);
				UserInterfaceSizer.makeActorCentered(popup.getView());
				addActor(popup.getView());
			}

			@Override
			public void resizeUI() { /* Not needed */ }

			@Override
			public boolean isDisabled() {
				return false;
			}

			@Override
			public boolean isBlank() {
				return false;
			}

			@Override
			public void setAlignment(Alignment alignment) { /* Not needed */ }

			@Override
			public void setFocused(boolean focused) { /* Not needed */ }

			@Override
			public void setDisabled(boolean disabled) { /* Not needed */ }
		};
		InputPriority.claimPriority(innerPopup, POP_UP_STAGE_NAME);
	}

	private void showRegainFocusPopup() {
		SimplePopUp innerPopup = new SimplePopUp() {
			@Override
			protected void setUpTable() {
				regainFocusPopup = GameInfo.getUserInterfaceFactory().makeTextButton(() -> "You Made It!", this::hideThis);
				regainFocusPopup.getView().setSize(200, 130);
				UserInterfaceSizer.makeActorCentered(regainFocusPopup.getView());
				addActor(regainFocusPopup.getView());
			}

			@Override
			public void resizeUI() { /* Not needed */ }

			@Override
			public boolean isDisabled() {
				return false;
			}

			@Override
			public boolean isBlank() {
				return false;
			}

			@Override
			public void setAlignment(Alignment alignment) { /* Not needed */ }

			@Override
			public void setFocused(boolean focused) { /* Not needed */ }

			@Override
			public void setDisabled(boolean disabled) { /* Not needed */ }
		};
		InputPriority.claimPriority(innerPopup, POP_UP_STAGE_NAME);
	}

	private class TestOptionsMenu extends OptionsMenu {

		protected TestOptionsMenu() {
			super(0, windowResizer);
			optionsButton = GameInfo.getUserInterfaceFactory().getOptionsButton(this::toggleScreenVisibility);
			optionsButton.getView().setWidth(optionsButton.getView().getHeight());
		}

		@Override
		protected Alignment getEntryAlignment() {
			return Alignment.CENTER;
		}

		@Override
		protected Alignment getMenuAlignment() {
			return Alignment.CENTER;
		}

		@Override
		protected String getGameVersion() {
			return "version goes here";
		}

		@Override
		protected Collection<VisibleInputConsumer> makeMiddleButtons() {
			return new ArrayList<>();
		}

		@Override
		protected UniversalButton makeButtonAboveQuitButton() {
			return null;
		}

		@Override
		protected PopUp makeControlsPopUp() {
			return new ConfirmationMenu("Did you really just press this?", "Sure did.", "My bad!", Runnables.nullRunnable(), POP_UP_STAGE_NAME);
		}

		@Override
		protected UniversalButton makeReportBugButton() {
			return GameInfo.getUserInterfaceFactory().makeTextButton(() -> "One could report a bug here", Runnables.nullRunnable());
		}

		@Override
		protected UniversalButton makeControlsButton() {
			return GameInfo.getUserInterfaceFactory().makeTextButton(() -> "This is where one could theoretically view controls", () -> InputPriority.claimPriority(makeControlsPopUp(), MultipleStage.OPTIONS_STAGE_NAME));
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
				// QuestGiverGame.music.temporarilyMute();
			}

			@Override
			public void focusGained() {
				// QuestGiverGame.music.untemporarilyMute();
			}

			@Override
			public void iconified(boolean isIconified) {/* notYetNeeded */}

			@Override
			public void created(Lwjgl3Window window) {/* notYetNeeded */}

			@Override
			public void maximized(boolean isMaximized) {/* notYetNeeded */}

			@Override
			public boolean closeRequested() {
				return true;
			}

			@Override
			public void filesDropped(String[] files) {/* notYetNeeded */}

			@Override
			public void refreshRequested() {/* notYetNeeded */}
		};
	}

	@Override
	protected Runnable getDrawConsoleRunnable() {
		return Runnables.nullRunnable();
	}

	@Override
	protected OptionsMenu makeOptionsMenu() {
		return new TestOptionsMenu();
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

	@Override
	protected boolean isDoneLoading() {
		// this is an asset-free project
		return true;
	}

	@Override
	protected float doLoadingFrame() {
		// the completion number isn't used by the blank loading screen
		return 1;
	}

	@Override
	protected LoadingScreen makeLoadingScreen() {
		return new BlankLoadingScreen();
	}

}
