package com.zalinius.libgdxtools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.zalinius.libgdxtools.errorhandling.LibGDXFileLoggingUncaughtExceptionHandler;
import com.zalinius.libgdxtools.graphics.ScreenElementFactory;
import com.zalinius.libgdxtools.graphics.SkinManager;
import com.zalinius.libgdxtools.preferencemanagers.PreferenceManager;
import com.zalinius.libgdxtools.preferencemanagers.SoundPreferenceManager;
import com.zalinius.libgdxtools.screens.MenuScreen;
import com.zalinius.libgdxtools.screens.StagedScreen;
import com.zalinius.libgdxtools.sound.ControlledMusic;

public abstract class DarzalGame extends HeadlessDarzalGame {
	private StagedScreen gameScreen;
	private StagedScreen menuScreen;
	private StagedScreen specificScreen;
	private StagedScreen currentScreen;

	protected ControlledMusic music;
	private Stage stage;

	protected FitViewport viewport;
	private OrthographicCamera camera;
	private InputMultiplexer multiplexer;

	public abstract String getGameName();
	public abstract String getGameVersion();
	protected abstract FileHandle getSkinFilePath();
	protected abstract Preferences getPreferencesFile();
	public abstract Texture getMenuBackgroundTexture();
	public abstract Runnable getPlayButtonRunnable();

	@Override
	public void create() {
		loadAssets();
		SkinManager.create(getSkinFilePath());
		PreferenceManager.create(getPreferencesFile());
		//ShaderFactory.create();
		camera = new OrthographicCamera();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		Thread.setDefaultUncaughtExceptionHandler(new LibGDXFileLoggingUncaughtExceptionHandler("err"));

		stage = new Stage();
		music = new ControlledMusic();
		setMenuMusic();
		CheckBox musicToggle = ScreenElementFactory.makeCheckBox(SoundPreferenceManager.isMusicMuted());
		musicToggle.addListener(new ChangeListener() {

			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				music.setMuted(musicToggle.isChecked());
			}
		});
		musicToggle.setBounds(10, 10, 75, 75);
		musicToggle.getImage().setScaling(Scaling.fill);
		musicToggle.getImageCell().size(75);
		stage.addActor(musicToggle);
		stage.addActor(music);

		multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);
		multiplexer.addProcessor(stage);

		openMainMenu();
	}

	public void goToGameScreen(final StagedScreen gameScreen) {
		this.gameScreen = gameScreen;
		changeScreens(gameScreen);
		if (menuScreen != null) {
			menuScreen.dispose();
			menuScreen = null;
		}
	}

	public void openMainMenu() {
		menuScreen = new MenuScreen(viewport, this);
		changeScreens(menuScreen);
		if (gameScreen != null) {
			setMenuMusic();
			gameScreen.dispose();
			gameScreen = null;
		}
		if (specificScreen != null) {
			specificScreen.dispose();
			specificScreen = null;
		}
	}

	protected abstract void setMenuMusic();

	protected void changeScreens(final StagedScreen nextScreen) {
		if (currentScreen != null) {
			currentScreen.hide();
			currentScreen.removeScreenAsInputProcessor(multiplexer);
			currentScreen.dispose();
		}

		currentScreen = nextScreen;
		currentScreen.addScreenAsInputProcessor(multiplexer);
		currentScreen.show();
	}

	@Override
	public void quit() {
		Gdx.app.exit();
	}

	private void loadAssets() {
		try {
			initializeAssets();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		assert areAssetsValid();
	}

	protected abstract boolean areAssetsValid();
	protected abstract void initializeAssets() throws IllegalArgumentException, IllegalAccessException;

	@Override
	public void resize(final int width, final int height) {
		viewport.update(width, height, true);
		currentScreen.resize(width, height);
	}

	@Override
	public void render() {
		stage.act();
		camera.update();
		currentScreen.render(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void pause() {
		currentScreen.pause();
	}

	@Override
	public void resume() {
		currentScreen.resume();
	}

	@Override
	public void dispose() {
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		if (menuScreen != null) {
			menuScreen.dispose();
		}
		if (specificScreen != null) {
			specificScreen.dispose();
		}
		//ShaderFactory.dispose();
		SkinManager.dispose();
		stage.dispose();
	}
}
