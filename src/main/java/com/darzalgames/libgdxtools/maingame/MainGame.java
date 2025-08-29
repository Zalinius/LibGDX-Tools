package com.darzalgames.libgdxtools.maingame;

import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer;
import com.darzalgames.libgdxtools.platform.GamePlatform;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.CustomCursorImage;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStageWithBackground;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.MouseInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputSetup;
import com.darzalgames.libgdxtools.ui.input.inputpriority.OptionsMenu;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Pause;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UserInterfaceFactory;
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
	protected Pause pause;


	// Values which change during gameplay
	protected GameScreen currentScreen;
	private boolean hasProcessedFinishedLoading = false;
	private boolean isQuitting = false;

	// loading functionality
	protected abstract boolean isDoneLoading();
	protected abstract void beginLoadingAssets();
	protected abstract void doLoadingFrame();
	protected void onLoadingFinished() {}


	// The setup process, in order that they are called
	protected abstract UserInterfaceFactory initializeGameAndUserInterfaceFactory();
	protected abstract String getPreferenceManagerName();

	/**
	 * @return The fallback background to be used in the game area, visible when nothing else is covering it
	 */
	protected abstract Actor makeBackground();
	protected abstract Runnable getDrawConsoleRunnable();
	protected abstract OptionsMenu makeOptionsMenu();
	protected abstract KeyboardInputHandler makeKeyboardInputHandler();
	protected abstract SaveManager makeSaveManager();
	protected abstract List<StageLikeRenderable> makeGameSpecificStages();
	protected abstract void setUpBeforeLoadingSave();
	protected abstract void launchGame(boolean isNewSave);

	protected void reactToResize(int width, int height) {}
	protected abstract void resizeGameSpecificUI();


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
		makePreferenceManager();
		windowResizer.setModeFromPreferences();
		beginLoadingAssets();
	}

	private void afterLoadingComplete() {
		makeInputStrategySwitcher();
		userInterfaceFactory = initializeGameAndUserInterfaceFactory();
		initializeWindowResizer();

		makeAllStages();

		setUpInput();

		setUpBeforeLoadingSave();
		saveManager = makeSaveManager();
		boolean isNewSave = !saveManager.load();
		launchGame(isNewSave);
	}


	protected CustomCursorImage getCustomCursor() {
		return new CustomCursorImage(windowResizer::isWindowed, new TextureRegion(ColorTools.getDefaultCursor()), inputStrategySwitcher);
	}

	protected final void changeScreen(GameScreen gameScreen) {
		if (currentScreen != null) {
			currentScreen.hide();
			currentScreen.remove();
		}
		multipleStage.clearAllGameStages();
		currentScreen = gameScreen;
		GetOnStage.addActorToStage(gameScreen, MultipleStage.MAIN_STAGE_NAME);
		currentScreen.show();
	}

	@Override
	public final void render() {
		ScreenUtils.clear(0, 0, 0, 1, true);

		boolean gameIsLoadedAndRunning = hasProcessedFinishedLoading;
		boolean justFinishedLoading = isDoneLoading() && !hasProcessedFinishedLoading;

		if (gameIsLoadedAndRunning) {
			if (!isQuitting) {
				resizeUI();
				multipleStage.update();
			}

			steamStrategy.update();
		} else if (justFinishedLoading) {
			hasProcessedFinishedLoading = true;
			onLoadingFinished();
			afterLoadingComplete();
		} else {
			doLoadingFrame();
		}
	}

	private void resizeUI() {
		inputSetup.getInputPriorityStack().resizeStackUI();
		Fader.resizeUI();
		resizeGameSpecificUI();
	}

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
		if (hasProcessedFinishedLoading) {
			multipleStage.resize(width, height);
			reactToResize(width, height);
		}
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
		UniversalInputStage optionsStage = makeAllPurposeStage(MultipleStage.OPTIONS_STAGE_NAME);
		StageBest cursorStage = makeCursorStage();
		StageBest inputHandlerStage = makeInputHandlerStage();
		UserInterfaceSizer.setStage(mainStage);
		Fader.initialize(MultipleStage.CURSOR_STAGE_NAME);

		pause = new Pause(makeOptionsMenu());
		multipleStage = new MultipleStage(mainStage, makeGameSpecificStages(), optionsStage, cursorStage, inputHandlerStage, pause);
	}

	protected UniversalInputStage makeAllPurposeStage(String name) {
		return new UniversalInputStage(name, new ScreenViewport(), inputStrategySwitcher);
	}

	private StageBest makeInputHandlerStage() {
		StageBest inputHandlerStage = new StageBest(MultipleStage.INPUT_HANDLER_STAGE_NAME, new ScreenViewport());
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
		return new UniversalInputStageWithBackground(
				MultipleStage.MAIN_STAGE_NAME,
				new ScreenViewport(),
				makeBackground(),
				inputStrategySwitcher);
	}

	private StageBest makeCursorStage() {
		StageBest cursorStage = new StageBest(MultipleStage.CURSOR_STAGE_NAME, new ScreenViewport());
		cursorStage.addActor(getCustomCursor());
		return cursorStage;
	}

	private void setUpInput() {
		// Set up input processing for all strategies
		inputSetup = new InputSetup(inputStrategySwitcher, windowResizer::toggleWindow, multipleStage.getGameStagesInOrder(), pause);

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
		windowResizer.initialize(inputStrategySwitcher);
	}

}
