package com.darzalgames.libgdxtools.maingame;

import java.util.function.Consumer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerButton;
import com.darzalgames.libgdxtools.platform.GamePlatform;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.CustomCursorImage;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.OptionalDrawStage;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStageWithBackground;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.MouseInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputSetup;
import com.darzalgames.libgdxtools.ui.input.inputpriority.OptionsMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;
import com.darzalgames.libgdxtools.ui.screen.Fader;
import com.darzalgames.libgdxtools.ui.screen.GameScreen;

public abstract class MainGame extends ApplicationAdapter implements SharesGameInformation {


	// Values which are statically shared to the rest of the game by {@link GameInfo}
	protected SaveManager saveManager;
	protected PreferenceManager preferenceManager;
	protected final GamePlatform gamePlatform;
	protected SteamStrategy steamStrategy;
	protected UserInterfaceFactory userInterfaceFactory;

	// Objects created at initialization, but not widely shared
	protected MultipleStage multipleStage;
	protected InputSetup inputSetup;
	protected WindowResizer windowResizer;
	protected InputStrategySwitcher inputStrategySwitcher;


	// Values which change during gameplay
	protected GameScreen currentScreen;
	private boolean isQuitting = false;



	// The setup process, in order that they are called
	protected abstract UserInterfaceFactory initializeAssetsAndUserInterfaceFactory();
	protected abstract String getPreferenceManagerName();
	protected abstract WindowResizerButton makeWindowResizerButton();

	/**
	 * @return The fallback background to be used in the game area, visible when nothing else is covering it
	 */
	protected abstract Consumer<Stage> makeAddBackgroundToStageRunnable();
	protected abstract Runnable getDrawConsoleRunnable();
	protected abstract OptionsMenu makeOptionsMenu();
	protected abstract KeyboardInputHandler makeKeyboardInputHandler();
	protected abstract SaveManager makeSaveManager();
	protected abstract void setUpBeforeLoadingSave();
	protected abstract void launchGame(boolean isNewSave);
	/**
	 * Anything the game needs to do once launching and initialization is fully complete
	 */
	protected abstract void afterLaunch();

	protected void reactToResize(int width, int height) {}


	/**
	 * Shutdown game-specific objects like the music system, call dispose() on things, etc.
	 */
	protected abstract void quitGame();



	protected MainGame(WindowResizer windowResizer, GamePlatform gamePlatform) {
		this.windowResizer = windowResizer;
		this.gamePlatform = gamePlatform;
		GameInfo.setMainGame(this);
	}

	@Override
	public final void create() {
		makeInputStrategySwitcher();
		userInterfaceFactory = initializeAssetsAndUserInterfaceFactory();
		makePreferenceManager();
		initializeWindowResizer();

		makeAllStages();

		setUpInput();


		setUpBeforeLoadingSave();
		saveManager = makeSaveManager();
		boolean isNewSave = !saveManager.load();
		launchGame(isNewSave);

		afterLaunch();
	}


	protected CustomCursorImage getCustomCursor() {
		return new CustomCursorImage(windowResizer::isWindowed, new TextureRegion(ColorTools.getDefaultCursor()), inputStrategySwitcher);
	}

	protected final void changeScreen(GameScreen gameScreen) {
		if (currentScreen != null) {
			currentScreen.hide();
			currentScreen.remove();
		}
		multipleStage.getStage().clear();
		multipleStage.getPopUpStage().clear();
		currentScreen = gameScreen;
		multipleStage.getStage().addActor(currentScreen);
		currentScreen.show();
	}

	@Override
	public final void render () {
		ScreenUtils.clear(0, 0, 0, 1, true);

		if (!isQuitting) {
			resizeUI();
			multipleStage.update(this::renderInternal);
		}

		steamStrategy.update();
	}

	protected void resizeUI() {
		inputSetup.getInputPriorityStack().resizeStackUI();
		Fader.resizeUI();

		if (inputSetup.getPause().isPaused()) {
			// Lets the game UI behind the options menu update the UI sizing
			multipleStage.getStage().act(0);
		}
	}

	protected void renderInternal() {}

	@Override
	public final void dispose() {
		isQuitting = true;

		saveManager.save();

		steamStrategy.dispose();

		quitGame();

		super.dispose();
	}

	@Override
	public final void resize (int width, int height) {
		if (windowResizer.isWindowed()) {
			preferenceManager.graphics().setPreferredWindowSize(width, height);
		}
		multipleStage.resize(width, height);
		reactToResize(width, height);
	}

	@Override
	public SaveManager getSaveManager() {
		return saveManager;
	}

	@Override
	public PreferenceManager getPreferenceManager() {
		return preferenceManager;
	}

	@Override
	public GamePlatform getGamePlatform() {
		return gamePlatform;
	}

	@Override
	public SteamStrategy getSteamStrategy() {
		return steamStrategy;
	}

	@Override
	public UserInterfaceFactory getUserInterfaceFactory() {
		return userInterfaceFactory;
	}

	private void makeInputStrategySwitcher() {
		inputStrategySwitcher = new InputStrategySwitcher();
	}

	private void makePreferenceManager() {
		preferenceManager = new PreferenceManager(getPreferenceManagerName());
	}

	private void makeAllStages() {
		UniversalInputStage mainStage = makeMainStage();
		UniversalInputStage popUpStage = makePopUpStage();
		OptionalDrawStage cursorStage = makeCursorStage();
		OptionalDrawStage inputHandlerStage = makeInputHandlerStage();
		UserInterfaceSizer.setStage(mainStage);
		multipleStage = new MultipleStage(mainStage, popUpStage, cursorStage, inputHandlerStage);
		Fader.initialize(popUpStage);
	}

	private UniversalInputStage makePopUpStage() {
		// The options menu and other popups have their own stage so it can still receive mouse enter/exit events when the main stage is paused
		UniversalInputStage popUpStage = new UniversalInputStage(new ScreenViewport(), inputStrategySwitcher);
		popUpStage.getRoot().setName("PopUp Stage");
		return popUpStage;
	}

	private OptionalDrawStage makeInputHandlerStage() {
		OptionalDrawStage inputHandlerStage = new OptionalDrawStage(new ScreenViewport());
		MouseInputHandler mouseInputHandler = new MouseInputHandler(inputStrategySwitcher);
		inputHandlerStage.addActor(mouseInputHandler);
		inputHandlerStage.addActor(inputStrategySwitcher);
		inputHandlerStage.addActor(new Actor() {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				getDrawConsoleRunnable().run();
			}
		});
		return inputHandlerStage;
	}

	private UniversalInputStage makeMainStage() {
		// Set up main game stage
		UniversalInputStage stage = new UniversalInputStageWithBackground(
				new ScreenViewport(),
				makeAddBackgroundToStageRunnable(),
				inputStrategySwitcher);
		stage.getRoot().setName("Main Stage");
		return stage;
	}

	private OptionalDrawStage makeCursorStage() {
		OptionalDrawStage cursorStage = new OptionalDrawStage(new ScreenViewport());
		cursorStage.addActor(getCustomCursor());
		return cursorStage;
	}

	private void setUpInput() {
		// Set up input processing for all strategies
		inputSetup = new InputSetup(inputStrategySwitcher, makeOptionsMenu(), windowResizer::toggleWindow, multipleStage.getPopUpStage());
		multipleStage.setPause(inputSetup.getPause());
		multipleStage.addActorThatDoesNotPause(inputStrategySwitcher);

		makeSteamStrategy();
		makeKeyboardAndGamepadInputHandlers();
	}

	private void makeSteamStrategy() {
		steamStrategy = gamePlatform.getSteamStrategy(inputStrategySwitcher, inputSetup.getInputReceiver());
	}

	private void makeKeyboardAndGamepadInputHandlers() {
		KeyboardInputHandler keyboardInputHandler = makeKeyboardInputHandler();
		GamepadInputHandler gamepadInputHandler = steamStrategy.getGamepadInputHandler();
		multipleStage.setUpInputHandlersOnStages(keyboardInputHandler, gamepadInputHandler, inputSetup.getScrollingManager());
	}

	private void initializeWindowResizer() {
		windowResizer.initialize(inputStrategySwitcher, makeWindowResizerButton());
	}

}
