package com.zalinius.libgdxtools;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.zalinius.libgdxtools.errorhandling.LibGDXFileLoggingUncaughtExceptionHandler;
import com.zalinius.libgdxtools.tools.Assets;

public class HeadlessDarzalGame implements ApplicationListener {

	@Override
	public void create() {
		Thread.setDefaultUncaughtExceptionHandler(new LibGDXFileLoggingUncaughtExceptionHandler("err"));
	}

	public void quit() {
		Gdx.app.exit();
	}

	public static String getGameName() {
		return Assets.getGameName();
	}

	public static String getGameVersion() {
		return Assets.getGameVersion();
	}

	@Override
	public void resize(final int width, final int height) {
	}

	@Override
	public void render() {
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
	}
}
