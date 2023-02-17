package com.zalinius.libgdxtools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
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
import com.zalinius.libgdxtools.preferencemanagers.SoundPreference;
import com.zalinius.libgdxtools.screens.MenuScreen;
import com.zalinius.libgdxtools.screens.StagedScreen;
import com.zalinius.libgdxtools.sound.ControlledMusic;

public abstract class DarzalGame extends HeadlessDarzalGame {

	private FitViewport viewport;
	private ControlledMusic music;
	private StagedScreen currentScreen;
	private Stage stage;
	private OrthographicCamera allScreensUIcamera;
	private InputMultiplexer multiplexer;
	private SkinManager skinManager;
	private ScreenElementFactory screenElementFactory;

	private ModelBatch modelBatch;


	//TODO find a way to replace abstract function these with Assets class or something like that 
	public abstract String getGameName();
	public abstract String getGameVersion();
	protected abstract FileHandle getMainFontFilePath();
	protected abstract FileHandle getTitleFontFilePath();
	protected abstract FileHandle getSkinFilePath();
	protected abstract FileHandle getSkinAtlasFilePath();
	protected abstract String getPreferencesName();


	public abstract ScreenElementFactory makeScreenElementFactory(final SkinManager skinManager);
	public abstract Runnable getPlayButtonRunnable();
	public abstract void initialize();
	public abstract void cleanUp();

	@Override
	public void create() {
		loadAssets();
		skinManager = new SkinManager(getSkinFilePath(), getSkinAtlasFilePath(), getMainFontFilePath(), getTitleFontFilePath());
		screenElementFactory = makeScreenElementFactory(skinManager);
		SoundPreference soundPreferenceManager = new PreferenceManager(getPreferencesName()).sound();
		allScreensUIcamera = new OrthographicCamera();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), allScreensUIcamera);
		Thread.setDefaultUncaughtExceptionHandler(new LibGDXFileLoggingUncaughtExceptionHandler("err"));

		stage = new Stage();
		music = new ControlledMusic(soundPreferenceManager);
		music.setMusic(getMenuMusic());
		CheckBox musicToggle = screenElementFactory.makeCheckBox(!soundPreferenceManager.isMusicMuted());
		musicToggle.addListener(new ChangeListener() {

			@Override
			public void changed(final ChangeEvent event, final Actor actor) {
				music.setMuted(!musicToggle.isChecked());
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

		modelBatch = new ModelBatch();

		openMainMenu();
		initialize();
	}

	protected abstract Environment getEnvironment();

	public void openMainMenu() {
		changeScreens(new MenuScreen(viewport, this, screenElementFactory));
		music.setMusic(getMenuMusic());
	}

	protected abstract Music getMenuMusic();

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
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		float delta = Gdx.graphics.getDeltaTime();
		currentScreen.act3D(delta);
		stage.act();
		allScreensUIcamera.update();

		currentScreen.render3D(modelBatch, getEnvironment());
		currentScreen.render(delta);
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

	public SkinManager getSkinManager() {
		return skinManager;
	}

	protected FitViewport getViewport() {
		return viewport;
	}

	@Override
	public void dispose() {
		if (currentScreen != null) {
			currentScreen.dispose();
		}
		cleanUp();
		skinManager.dispose();
		stage.dispose();
	}
}
