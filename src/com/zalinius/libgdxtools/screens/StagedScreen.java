package com.zalinius.libgdxtools.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class StagedScreen implements Screen {
	protected final Stage stage;

	protected StagedScreen(final Viewport viewport) {
		this.stage = new Stage(viewport);
	}

	public void addScreenAsInputProcessor(final InputMultiplexer inputMultiplexer) {
		inputMultiplexer.addProcessor(stage);
	}

	public void removeScreenAsInputProcessor(final InputMultiplexer inputMultiplexer) {
		inputMultiplexer.removeProcessor(stage);
	}

	@Override
	public void resize(final int width, final int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void render(final float delta) {
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void show() {
	}
}
