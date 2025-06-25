package com.darzalgames.libgdxtools.maingame;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.ui.input.OptionalDrawStage;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Pause;
import com.darzalgames.libgdxtools.ui.input.inputpriority.ScrollingManager;

public abstract class MultiStage {

	private static final boolean SHOULD_DEBUG_PRINT_ACTOR_UNDER_CURSOR = false;

	protected abstract List<StageLikeRenderable> getOtherStagesInRenderOrder();

	protected Pause pause;
	private final List<DoesNotPause> actorsThatDoNotPause;

	private final UniversalInputStage mainStage;
	private final UniversalInputStage pauseStage;

	private final OptionalDrawStage inputHandlerStage;
	private final OptionalDrawStage cursorStage;

	protected MultiStage(UniversalInputStage mainStage, UniversalInputStage pauseStage, OptionalDrawStage inputHandlerStage, OptionalDrawStage cursorStage) {
		this.mainStage = mainStage;
		this.pauseStage = pauseStage;
		this.inputHandlerStage = inputHandlerStage;

		actorsThatDoNotPause = new ArrayList<>();
		this.cursorStage = cursorStage;

		setShouldRender(true);
		setUpInputMultiplexerForAllStages();
	}

	public void setShouldRender(boolean shouldRender) {
		mainStage.setShouldDraw(shouldRender);
		pauseStage.setShouldDraw(shouldRender);
		getOtherStagesInRenderOrder().forEach(stage -> stage.setShouldDraw(shouldRender));
		cursorStage.setShouldDraw(shouldRender);
		inputHandlerStage.setShouldDraw(shouldRender);
	}

	public void addActorThatDoesNotPause(DoesNotPause actor) {
		actorsThatDoNotPause.add(actor);
	}

	protected void setPause(Pause pause) {
		this.pause = pause;
		inputHandlerStage.addActor(pause);
	}

	void setUpInputHandlersOnStages(KeyboardInputHandler keyboardInputHandler, GamepadInputHandler gamepadInputHandler, ScrollingManager scrollingManager) {
		inputHandlerStage.addActor(keyboardInputHandler);
		actorsThatDoNotPause.add(keyboardInputHandler);

		inputHandlerStage.addActor(gamepadInputHandler);
		actorsThatDoNotPause.add(gamepadInputHandler);

		inputHandlerStage.addActor(scrollingManager);
		actorsThatDoNotPause.add(scrollingManager);

		mainStage.setKeyboardFocus(keyboardInputHandler);
	}

	void update() {
		if (SHOULD_DEBUG_PRINT_ACTOR_UNDER_CURSOR) {
			doDebugPrinting();
		}

		if (pause.isPaused()) {
			// skip stage.act() while paused
			mainStage.draw();
			float delta = Gdx.graphics.getDeltaTime();
			actorsThatDoNotPause.forEach(actor -> actor.actWhilePaused(delta));
			getOtherStagesInRenderOrder().forEach(StageLikeRenderable::draw);
		} else {
			updateAndDrawStage(mainStage);
			getOtherStagesInRenderOrder().forEach(this::updateAndDrawStage);
		}

		updateAndDrawStage(pauseStage);
		updateAndDrawStage(cursorStage);
		updateAndDrawStage(inputHandlerStage);
	}


	private void updateAndDrawStage(StageLikeRenderable stageToUpdate) {
		stageToUpdate.act();
		stageToUpdate.draw();
	}

	void resize(int width, int height) {
		mainStage.resize(width, height);
		pauseStage.resize(width, height);
		getOtherStagesInRenderOrder().forEach(stage -> stage.resize(width, height));
		inputHandlerStage.resize(width, height);
		cursorStage.resize(width, height);
	}

	private void doDebugPrinting() {
		List<StageLikeRenderable> allStages = new ArrayList<>(getOtherStagesInRenderOrder());
		Collections.reverse(allStages);
		allStages.addFirst(pauseStage);
		allStages.addLast(mainStage);

		Iterator<StageLikeRenderable> stages = getOtherStagesInRenderOrder().iterator();
		boolean hitSomething = false;
		while (!hitSomething && stages.hasNext()) {
			hitSomething = tryToPrintADebugHit(stages.next());
		}

	}

	private boolean tryToPrintADebugHit(StageLikeRenderable stage) {
		Vector2 cursor = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		String hit = stage.nameOfThingAtCursorPosition(cursor.x, cursor.y, true);
		boolean hitSomething = hit != null && !hit.isBlank();
		if (hitSomething) {
			Gdx.app.log("MultiStage", stage.getName() + ": " + hit);
		}
		return hitSomething;
	}


	private void setUpInputMultiplexerForAllStages() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(inputHandlerStage);
		inputMultiplexer.addProcessor(cursorStage);
		inputMultiplexer.addProcessor(pauseStage);
		List<StageLikeRenderable> gameSpecificStages = new ArrayList<>(getOtherStagesInRenderOrder());
		Collections.reverse(gameSpecificStages);
		gameSpecificStages.forEach(inputMultiplexer::addProcessor);
		inputMultiplexer.addProcessor(mainStage);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public UniversalInputStage getStage() {
		return mainStage;
	}

	protected UniversalInputStage getPauseStage() {
		return pauseStage;
	}

	public void clear() {
		mainStage.clear();
		pauseStage.clear();
		getOtherStagesInRenderOrder().forEach(StageLikeRenderable::clear);
	}

}