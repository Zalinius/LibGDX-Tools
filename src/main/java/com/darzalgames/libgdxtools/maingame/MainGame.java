package com.darzalgames.libgdxtools.maingame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizerButton;
import com.darzalgames.libgdxtools.platform.GamePlatform;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.agnostic.SteamStrategy;
import com.darzalgames.libgdxtools.ui.CustomCursorImage;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStageWithBackground;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.MouseInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputSetup;
import com.darzalgames.libgdxtools.ui.input.inputpriority.PauseMenu;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.strategy.KeyboardInputStrategy;
import com.darzalgames.libgdxtools.ui.input.strategy.MouseInputStrategy;
import com.darzalgames.libgdxtools.ui.screen.GameScreen;
import com.darzalgames.libgdxtools.ui.screen.PixelPerfectViewport;

public abstract class MainGame extends ApplicationAdapter implements SharesGameInformation {


	// Values which are statically shared to the rest of the game by {@link GameInfo}
	protected final int width;
	protected final int height;
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


	
	// The setup process, in order that they are called
	protected abstract void initializeAssetsAndUserInterfaceFactory();
	protected abstract String getPreferenceManagerName(); // TODO this can be removed once we figure out our long-standing goal of making Assets extendable
	protected abstract WindowResizerButton makeWindowResizerButton();
	/**
	 * @return The background texture to be used in the "gutters" around the game, visible when the window size doesn't match the game's fixed resolution
	 */
	protected abstract Texture getBackgroundStageTexture();
	/**
	 * @return The fallback background texture to be used in the game area, visible when nothing else is covering it
	 */
	protected abstract Texture getMainStageBackgroundTexture();
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


	/**
	 * Shutdown game-specific objects like the music system, call dispose() on things, etc.
	 */
	protected abstract void quitGame();



	protected MainGame(int width, int height, WindowResizer windowResizer, GamePlatform gamePlatform) {
		this.width = width;
		this.height = height;
		this.windowResizer = windowResizer;
		this.gamePlatform = gamePlatform;
		multipleStage = new MultipleStage();
		GameInfo.setMainGame(this);
	}

	@Override
	public final void create() {
		makeInputStrategySwitcher();
		initializeAssetsAndUserInterfaceFactory();
		makePreferenceManager();
		initializeWindowResizer();

		makeBackgroundStage();
		makePopUpStage();
		makeMainStageAndMouseStages();

		setUpInput();
		makeSteamStrategy();
		makeKeyboardAndGamepadInputHandlers();
		multipleStage.setUpInputMultiplexerForAllStages();


		setUpBeforeLoadingSave();
		saveManager = makeSaveManager();
		boolean isNewSave = !saveManager.load();
		launchGame(isNewSave);

		afterLaunch();
	}


	protected CustomCursorImage getCustomCursor() {
		return new CustomCursorImage(windowResizer::isWindowed, ColorTools.getDefaultCursor(), inputStrategySwitcher);
	}

	protected final void changeScreen(GameScreen gameScreen) {
		if (currentScreen != null) {
			currentScreen.hide();
			currentScreen.remove();
		}
		currentScreen = gameScreen;
		multipleStage.addActorToMainStage(currentScreen);
		currentScreen.show();
	}

	@Override
	public final void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!isQuitting) {
			multipleStage.render();
		}

		steamStrategy.update();
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
		multipleStage.resize(width, height);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
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



	private void makeInputStrategySwitcher() {
		inputStrategySwitcher = new InputStrategySwitcher(new MouseInputStrategy(), new KeyboardInputStrategy());
		multipleStage.addActorThatDoesNotPause(inputStrategySwitcher);
	}

	private void makePreferenceManager() {
		this.preferenceManager = new PreferenceManager(getPreferenceManagerName());
	}

	private void makeBackgroundStage() {
		// Set up background texture + stage (fills gutters with a repeating pattern when the window size doesn't match the game)
		Texture backgroundTexture = getBackgroundStageTexture();
		backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		TextureRegion backgroundTextureRegion = new TextureRegion(backgroundTexture);
		backgroundTextureRegion.setRegion(0, 0, (float)backgroundTexture.getWidth(), (float)backgroundTexture.getHeight());
		Stage backgroundStage = new Stage(new ExtendViewport(width, height));
		backgroundStage.addActor(new Image(backgroundTextureRegion));
		multipleStage.setBackgroundStage(backgroundStage);
	}

	private void makePopUpStage() {
		// The pause menu and other popups have their own stage so it can still receive mouse enter/exit events when the main stage is paused
		UniversalInputStage popUpStage = new UniversalInputStage(new PixelPerfectViewport(width, height), inputStrategySwitcher);
		popUpStage.getRoot().setName("PopUp Stage");
		multipleStage.setPopUpStage(popUpStage);
	}

	private void makeMainStageAndMouseStages() {
		Stage inputHandlerStage = new Stage(new ScreenViewport());
		MouseInputHandler mouseInputHandler = new MouseInputHandler(inputStrategySwitcher);
		inputHandlerStage.addActor(mouseInputHandler);
		inputHandlerStage.addActor(new Actor() {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				getDrawConsoleRunnable().run();
			}
		});
		multipleStage.setInputHandlerStage(inputHandlerStage);

		// Set up main game stage
		UniversalInputStage stage = new UniversalInputStageWithBackground(
				new PixelPerfectViewport(width, height),
				getMainStageBackgroundTexture(),
				inputStrategySwitcher);
		stage.getRoot().setName("Main Stage");
		multipleStage.setMainStage(stage);

		Stage cursorStage = new Stage(new ExtendViewport(width, height));
		multipleStage.setCursorStage(cursorStage);
	}

	private void setUpInput() {
		// Set up input processing for all strategies
		inputSetup = new InputSetup(inputStrategySwitcher, makePauseMenu(), windowResizer::toggleWindow, gamePlatform.toggleFullScreenWithF11(), multipleStage.popUpStage);
		multipleStage.setPause(inputSetup.getPause());
		multipleStage.addActorToMainStage(inputStrategySwitcher);
		multipleStage.addActorToMainStage(inputSetup.getScrollingManager());
	}

	private void makeSteamStrategy() {
		this.steamStrategy = gamePlatform.getSteamStrategy(inputStrategySwitcher, inputSetup.getInputReceiver());
	}

	private void makeKeyboardAndGamepadInputHandlers() {
		KeyboardInputHandler keyboardInputHandler = makeKeyboardInputHandler();
		GamepadInputHandler gamepadInputHandler = steamStrategy.getGamepadInputHandler();
		multipleStage.setUpInputHandlersOnStages(keyboardInputHandler, gamepadInputHandler, getCustomCursor());
	}

	private void initializeWindowResizer() {
		windowResizer.initialize(inputStrategySwitcher, makeWindowResizerButton());
	}

}
