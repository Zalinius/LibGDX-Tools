package com.zalinius.libgdxtools.screens;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Collections;

public abstract class StagedScreen extends Stage implements Screen {

	public StagedScreen (final Viewport viewport) {
		super(viewport);
	}

	public void addScreenAsInputProcessor(final InputMultiplexer inputMultiplexer) {
		inputMultiplexer.addProcessor(this);
		for (Iterator<InputProcessor> it = getInputProcessors().iterator(); it.hasNext();) {
			InputProcessor inputProcessor = it.next();
			inputMultiplexer.addProcessor(inputProcessor);
		}
	}

	public void removeScreenAsInputProcessor(final InputMultiplexer inputMultiplexer) {
		inputMultiplexer.removeProcessor(this);
		for (Iterator<InputProcessor> it = getInputProcessors().iterator(); it.hasNext();) {
			InputProcessor inputProcessor = it.next();
			inputMultiplexer.removeProcessor(inputProcessor);
		}
	}

	protected List<InputProcessor> getInputProcessors() {
		return Collections.emptyList();
	}

	@Override
	public void resize(final int width, final int height) {

	}

	@Override
	public void render(final float delta) {
		act(delta);
		draw();
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

	public void act3D(final float deltaTime) {
		//expose optional 3D functionality for inherited classes
	}

	public void render3D(final ModelBatch modelBatch, final Environment environment) {
		//expose optional 3D functionality for inherited classes
	}

	@Override
	public void show() {
	}
}
