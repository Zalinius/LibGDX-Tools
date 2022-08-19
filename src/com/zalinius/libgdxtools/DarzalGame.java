package com.zalinius.libgdxtools;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.zalinius.libgdxtools.errorhandling.LibGDXFileLoggingUncaughtExceptionHandler;
import com.zalinius.libgdxtools.tools.Assets;

public class DarzalGame implements ApplicationListener {
	private Stage stage;

	private FitViewport viewport;
	private OrthographicCamera camera;
	private InputMultiplexer multiplexer;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		Thread.setDefaultUncaughtExceptionHandler(new LibGDXFileLoggingUncaughtExceptionHandler("err"));

		stage = new Stage();

		multiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(multiplexer);
		multiplexer.addProcessor(stage);
	}

	public void quit() {
		Gdx.app.exit();
	}

	private static void loadAssets() {
		try {
			Assets.initialize();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assert allAssetsAreValid();
	}

	public static String getGameName() {
		return Assets.getGameName();
	}

	public static String getGameVersion() {
		return Assets.getGameVersion();
	}

	private static boolean allAssetsAreValid() {
		boolean allValid = true;

		// Do asset validation needed

		return allValid;
	}

	@Override
	public void resize(final int width, final int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void render() {
		stage.act();
		camera.update();
		stage.draw();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		Assets.dispose();
		stage.dispose();
	}
}
