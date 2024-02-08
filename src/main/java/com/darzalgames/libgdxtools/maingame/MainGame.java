package com.darzalgames.libgdxtools.maingame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.codedisaster.steamworks.SteamAPI;
import com.codedisaster.steamworks.SteamController;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.graphics.ColorTools;
import com.darzalgames.libgdxtools.graphics.WindowResizer;
import com.darzalgames.libgdxtools.preferencemanagers.PreferenceManager;
import com.darzalgames.libgdxtools.save.SaveManager;
import com.darzalgames.libgdxtools.steam.SteamConnection;
import com.darzalgames.libgdxtools.ui.input.CustomCursorImage;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.keyboard.MouseDetector;
import com.darzalgames.libgdxtools.ui.input.keyboard.stage.KeyboardStage;
import com.darzalgames.libgdxtools.ui.input.keyboard.stage.StageWithBackground;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;
import com.darzalgames.libgdxtools.ui.input.strategy.KeyboardInputStrategy;
import com.darzalgames.libgdxtools.ui.input.strategy.MouseInputStrategy;
import com.darzalgames.libgdxtools.ui.screen.GameScreen;
import com.darzalgames.libgdxtools.ui.screen.PixelPerfectViewport;

public abstract class MainGame extends ApplicationAdapter {

	// Values which are statically shared to the rest of the game by {@link GameInfo}
	protected final int width;
	protected final int height;
	protected InputStrategyManager inputStrategyManager;
	protected SaveManager saveManager;
	protected PreferenceManager preferenceManager;

	protected Stage stage;
	protected Stage popUpStage;
	private Stage backgroundStage;
	private Stage cursorStage;
	private Stage mouseDetectorStage;

	protected abstract void initializeAssets();
	protected abstract SaveManager makeSaveManager();
	protected abstract void setUpBeforeLoadingSave();
	protected abstract void launchGame(boolean isNewSave);
	protected abstract KeyboardInputHandler makeKeyboardInputHandler();
	protected abstract GamepadInputHandler makeGamepadInputHandler(SteamController steamController);
	protected abstract void quitGame();

	protected GameScreen currentScreen;
	protected WindowResizer windowResizer;
	private SteamController steamController;
	private List<DoesNotPause> actorsThatDoNotPause;

	/**
	 * @return The background texture to be used in the "gutters" around the game, visible when the window size doesn't match the game's fixed resolution
	 */
	protected abstract Texture getBackgroundStageTexture();
	/**
	 * @return The fallback background texture to be used in the game area, visible when nothing else is covering it
	 */
	protected abstract Texture getMainStageBackgroundTexture();

	// TODO this can be removed once we figure out our long-standing goal of making Assets extendable
	protected abstract String getPreferenceManagerName();


	protected MainGame(int width, int height, WindowResizer windowResizer) {
		super();
		this.width = width;
		this.height = height;
		this.windowResizer = windowResizer;
		actorsThatDoNotPause = new ArrayList<>();
		GameInfo.setMainGame(this);
	}

	@Override
	public void create() {
		initializeAssets();

		this.preferenceManager = new PreferenceManager(getPreferenceManagerName());

		makeBackgroundStage();
		makePopUpStage();
		makeMainStageAndMouseStages();

		setUpInputPrioritizer();
		setUpInputForAllStages();
		actorsThatDoNotPause.add(inputStrategyManager);

		setUpBeforeLoadingSave();
		saveManager = makeSaveManager();
		boolean isNewSave = !saveManager.load();
		windowResizer.initialize();

		launchGame(isNewSave);
	}

	private void makeBackgroundStage() {
		// Set up background texture + stage (fills gutters with a repeating pattern when the window size doesn't match the game)
		Texture backgroundTexture = getBackgroundStageTexture();
		backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		TextureRegion backgroundTextureRegion = new TextureRegion(backgroundTexture);
		backgroundTextureRegion.setRegion(0, 0, (float)backgroundTexture.getWidth(), (float)backgroundTexture.getHeight());
		backgroundStage = new Stage(new ExtendViewport(width, height));
		backgroundStage.addActor(new Image(backgroundTextureRegion));
	}

	private void makePopUpStage() {
		// The pause menu and other popups have their own stage so it can still receive mouse enter/exit events when the main stage is paused
		popUpStage = new KeyboardStage(new PixelPerfectViewport(width, height));
	}

	private void makeMainStageAndMouseStages() {
		mouseDetectorStage = new Stage(new ScreenViewport());
		mouseDetectorStage.addActor(new MouseDetector());

		// Set up main game stage
		stage = new StageWithBackground(new PixelPerfectViewport(width, height), getMainStageBackgroundTexture());

		cursorStage = new Stage(new ExtendViewport(width, height));
	}

	private void setUpInputForAllStages() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(mouseDetectorStage);
		inputMultiplexer.addProcessor(cursorStage);
		inputMultiplexer.addProcessor(popUpStage);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	protected Texture getCursorTexture() {
		return ColorTools.getDefaultCursor();
	}

	/**
	 * @return An InputStrategyManager, in case the base class wants to extend its functionality (e.g. Quest Giver adds
	 * button hints, and more specific gamepad handling may be wanted in the future)
	 */
	protected InputStrategyManager makeInputStrategyManager() {
		return new InputStrategyManager(new MouseInputStrategy(), new KeyboardInputStrategy());
	}

	private void setUpInputPrioritizer() {
		SteamConnection.initialize();

		// Set up input processing for all strategies
		steamController = new SteamController();
		if (SteamAPI.isSteamRunning()) {
			steamController.init();
		}
		inputStrategyManager = makeInputStrategyManager();
		stage.addActor(inputStrategyManager);
		KeyboardInputHandler keyboardInputHandler = makeKeyboardInputHandler();
		GamepadInputHandler gamepadInputHandler = makeGamepadInputHandler(steamController);
		actorsThatDoNotPause.add(keyboardInputHandler);
		actorsThatDoNotPause.add(gamepadInputHandler);
		CustomCursorImage customCursor = new CustomCursorImage(windowResizer::isWindowed, getCursorTexture(), inputStrategyManager);
		cursorStage.addActor(customCursor);
		InputPriorityManager.initialize(stage, popUpStage, windowResizer::toggleWindow, gamepadInputHandler, keyboardInputHandler);
	}

	protected void changeScreen(GameScreen gameScreen) {
		if (currentScreen != null) {
			currentScreen.hide();
			currentScreen.remove();
		}
		currentScreen = gameScreen;
		currentScreen.show();
		stage.addActor(currentScreen);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!isQuitting) {
			backgroundStage.getViewport().apply();
			backgroundStage.draw();

			stage.getViewport().apply();
			if (InputPriorityManager.isPaused()) {
				stage.draw();
				float delta = Gdx.graphics.getDeltaTime();
				actorsThatDoNotPause.forEach(actor -> actor.actWhilePaused(delta));
			} else {
				stage.act();
				stage.draw();
			}

			popUpStage.getViewport().apply();
			popUpStage.act();
			popUpStage.draw();

			cursorStage.getViewport().apply();
			cursorStage.act();
			cursorStage.draw();

			mouseDetectorStage.getViewport().apply();
			mouseDetectorStage.act();
			mouseDetectorStage.draw();
		}

		SteamConnection.update();
	}

	private boolean isQuitting = false;
	@Override
	public void dispose() {
		isQuitting = true;

		saveManager.save();

		if (SteamAPI.isSteamRunning()) {
			steamController.shutdown();
		}
		SteamConnection.dispose();

		quitGame();

		super.dispose();
	}

	@Override
	public void resize (int width, int height) {
		backgroundStage.getViewport().update(width, height, true);
		backgroundStage.getCamera().update();
		stage.getViewport().update(width, height, true);
		stage.getCamera().update();
		mouseDetectorStage.getViewport().update(width, height, true);
		mouseDetectorStage.getCamera().update();
		popUpStage.getViewport().update(width, height, true);
		popUpStage.getCamera().update();
		cursorStage.getViewport().update(width, height, true);
		cursorStage.getCamera().update();
	}

}
