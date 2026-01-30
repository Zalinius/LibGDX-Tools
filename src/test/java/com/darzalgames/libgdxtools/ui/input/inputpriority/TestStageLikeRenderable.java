package com.darzalgames.libgdxtools.ui.input.inputpriority;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.maingame.StageLikeRenderable;

public class TestStageLikeRenderable implements StageLikeRenderable {

	private final String name;

	public TestStageLikeRenderable(String name) {
		this.name = name;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		/* Not yet needed for testing */
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		/* Not yet needed for testing */
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		/* Not yet needed for testing */
		return false;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		/* Not yet needed for testing */
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		/* Not yet needed for testing */
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		/* Not yet needed for testing */
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		/* Not yet needed for testing */
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		/* Not yet needed for testing */
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		/* Not yet needed for testing */
		return false;
	}

	@Override
	public void resize(int width, int height) {
		/* Not yet needed for testing */
	}

	@Override
	public String nameOfActorUnderCursor() {
		/* Not yet needed for testing */
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void draw() {
		/* Not yet needed for testing */
	}

	@Override
	public void clear() {
		/* Not yet needed for testing */
	}

	@Override
	public void addActor(Actor view) {
		/* Not yet needed for testing */
	}

	@Override
	public void act(float delta) {
		/* Not yet needed for testing */
	}

	@Override
	public void act() {
		/* Not yet needed for testing */
	}
}
