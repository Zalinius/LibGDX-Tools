package com.zalinius.libgdxtools.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zalinius.libgdxtools.graphics.SkinManager;

public abstract class StagedScreen implements Screen {
	protected final Stage stage;
	private final Table uiNineTable;

	protected StagedScreen(final Viewport viewport) {
		this.stage = new Stage(viewport);
		uiNineTable = new Table(SkinManager.skin);
		resetUINineTable();
		stage.addActor(uiNineTable);
	}

	private void resetUINineTable() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				uiNineTable.add(new Table(SkinManager.skin));
			}
			uiNineTable.row();
		}
		uiNineTable.setFillParent(true);
	}

	protected void addUIElement(final Actor toPlace, final int alignment) {
		boolean isTop = Align.isTop(alignment);
		boolean isBottom = Align.isBottom(alignment);
		boolean isLeft = Align.isLeft(alignment);
		boolean isRight = Align.isRight(alignment);
		int x = 1;
		int y = 1;

		if (isLeft) {
			x = 0;
		} else if (isRight) {
			x = 2;
		}
		if (isTop) {
			y = 0;
		} else if (isBottom) {
			y = 2;
		}

		Actor innerTable = uiNineTable.getChild(x * 3 + y);
		((Table)innerTable).add(toPlace);
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
