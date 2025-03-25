package com.darzalgames.libgdxtools.maingame;

import java.util.function.Consumer;

import org.lwjgl.opengl.GL20;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStageWithBackground;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.MouseInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputSetup;
import com.darzalgames.libgdxtools.ui.input.inputpriority.PauseMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.strategy.KeyboardAndGamepadInputStrategy;
import com.darzalgames.libgdxtools.ui.input.strategy.MouseInputStrategy;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;
import com.darzalgames.libgdxtools.ui.screen.GameScreen;

public abstract class MainGame extends ApplicationAdapter implements SharesGameInformation {


	// Values which are statically shared to the rest of the game by {@link GameInfo}
	protected SaveManager saveManager;
	protected PreferenceManager preferenceManager;
	protected final GamePlatform gamePlatform;
	protected SteamStrategy steamStrategy;

	// Objects created at initialization, but not widely shared
	protected MultipleStage multipleStage;
	protected InputSetup inputSetup;
	protected WindowResizer windowResizer;
	protected InputStrategySwitcher inputStrategySwitcher;

	
	// Values which change during gameplay
	protected GameScreen currentScreen;
	private boolean isQuitting = false;
	

	protected static UserInterfaceFactory userInterfaceFactory;


	
	// The setup process, in order that they are called
	protected abstract UserInterfaceFactory initializeAssetsAndUserInterfaceFactory();
	protected abstract String getPreferenceManagerName(); // TODO this can be removed once we figure out our long-standing goal of making Assets extendable
	protected abstract WindowResizerButton makeWindowResizerButton();

	/**
	 * @return The fallback background to be used in the game area, visible when nothing else is covering it
	 */
	protected abstract Consumer<Stage> makeAddBackgroundToStageRunnable();
	protected abstract Runnable getDrawConsoleRunnable();
	protected abstract PauseMenu makePauseMenu();
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
		MainGame.userInterfaceFactory = initializeAssetsAndUserInterfaceFactory();
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
		multipleStage.stage.clear(); // TODO is there a reason not to do this?
		multipleStage.popUpStage.clear(); // TODO is there a reason not to do this?
		currentScreen = gameScreen;
		multipleStage.stage.addActor(currentScreen);
		currentScreen.show();
	}

	@Override
	public final void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		// LibGDX 1.13 gives us this, I think the boolean at the end lets us toggle applying AA? NOTE: the number of samples must still be set in the launch config
		// ScreenUtils.clear (float r, float g, float b, float a, boolean clearDepth, boolean applyAntialiasing) {

		if (!isQuitting) {
			resizeUI();
			multipleStage.render(this::renderInternal);
		}

		steamStrategy.update();
	}

	protected void resizeUI() {
		inputSetup.getInputPriorityStack().resizeStackUI();

		if (inputSetup.getPause().isPaused()) {
			// Lets the game UI behind the options menu update the UI sizing
			multipleStage.stage.act(0);
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

	public static UserInterfaceFactory getUserInterfaceFactory() {
		return userInterfaceFactory;
	}
	
	private void makeInputStrategySwitcher() {
		inputStrategySwitcher = new InputStrategySwitcher(new MouseInputStrategy(), new KeyboardAndGamepadInputStrategy());
	}

	private void makePreferenceManager() {
		this.preferenceManager = new PreferenceManager(getPreferenceManagerName());
	}
	
	private void makeAllStages() {
		UniversalInputStage mainStage = makeMainStage();
		UniversalInputStage popUpStage = makePopUpStage();
		Stage cursorStage = makeCursorStage();
		Stage inputHandlerStage = makeInputHandlerStage();
		UserInterfaceSizer.setStage(mainStage);
		multipleStage = new MultipleStage(mainStage, popUpStage, cursorStage, inputHandlerStage);
	}

	private UniversalInputStage makePopUpStage() {
		// The pause menu and other popups have their own stage so it can still receive mouse enter/exit events when the main stage is paused
		UniversalInputStage popUpStage = new UniversalInputStage(new ScreenViewport(), inputStrategySwitcher);
		popUpStage.getRoot().setName("PopUp Stage");
		return popUpStage;
	}
	
	private Stage makeInputHandlerStage() {
		Stage inputHandlerStage = new Stage(new ScreenViewport());
		MouseInputHandler mouseInputHandler = new MouseInputHandler(inputStrategySwitcher);
		inputHandlerStage.addActor(mouseInputHandler);
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
	
	private Stage makeCursorStage() {
		Stage cursorStage = new Stage(new ScreenViewport());
		cursorStage.addActor(getCustomCursor());
		return cursorStage;
	}

	private void setUpInput() {
		// Set up input processing for all strategies
		inputSetup = new InputSetup(inputStrategySwitcher, makePauseMenu(), windowResizer::toggleWindow, gamePlatform.toggleFullScreenWithF11(), multipleStage.popUpStage);
		multipleStage.setPause(inputSetup.getPause());
		multipleStage.stage.addActor(inputStrategySwitcher);
		multipleStage.addActorThatDoesNotPause(inputStrategySwitcher);
		multipleStage.stage.addActor(inputSetup.getScrollingManager());

		setUpCatchKeys();
		makeSteamStrategy();
		makeKeyboardAndGamepadInputHandlers();
	}
	
	private void setUpCatchKeys() {
		Gdx.input.setCatchKey(Input.Keys.DOWN, true);
		Gdx.input.setCatchKey(Input.Keys.UP, true);
		Gdx.input.setCatchKey(Input.Keys.LEFT, true);
		Gdx.input.setCatchKey(Input.Keys.RIGHT, true);
		// TODO F11 in browser?
		// itch.io handles catching mouse scrolling and spacebar while the game is in focus
	}
	
	private void makeSteamStrategy() {
		this.steamStrategy = gamePlatform.getSteamStrategy(inputStrategySwitcher, inputSetup.getInputReceiver());
	}

	private void makeKeyboardAndGamepadInputHandlers() {
		KeyboardInputHandler keyboardInputHandler = makeKeyboardInputHandler();
		GamepadInputHandler gamepadInputHandler = steamStrategy.getGamepadInputHandler();
		multipleStage.setUpInputHandlersOnStages(keyboardInputHandler, gamepadInputHandler);
	}

	private void initializeWindowResizer() {
		windowResizer.initialize(inputStrategySwitcher, makeWindowResizerButton());
	}

}
