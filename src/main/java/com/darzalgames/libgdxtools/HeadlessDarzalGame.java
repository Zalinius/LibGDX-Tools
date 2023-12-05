package com.darzalgames.libgdxtools;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.darzalgames.libgdxtools.errorhandling.LibGDXFileLoggingUncaughtExceptionHandler;

public class HeadlessDarzalGame implements ApplicationListener {

	@Override
	public void create() {
		Thread.setDefaultUncaughtExceptionHandler(new LibGDXFileLoggingUncaughtExceptionHandler("err"));
	}

	public void quit() {
		Gdx.app.exit();
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

	}

}
