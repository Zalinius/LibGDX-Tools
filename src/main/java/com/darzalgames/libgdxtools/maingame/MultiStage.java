package com.darzalgames.libgdxtools.maingame;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.ui.input.OptionalDrawStage;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Pause;
import com.darzalgames.libgdxtools.ui.input.inputpriority.ScrollingManager;

public abstract class MultiStage {

	private static final boolean SHOULD_DEBUG_PRINT_ACTOR_UNDER_CURSOR = false;

	public static final String MAIN_STAGE_NAME = "Main Stage";
	public static final String OPTIONS_STAGE_NAME = "Options Stage";
	public static final String CURSOR_STAGE_NAME = "Cursor Stage";
	public static final String INPUT_HANDLER_STAGE_NAME = "Input Handler Stage";

	protected abstract List<StageLikeRenderable> getGameSpecificStagesInRenderOrder();
	public List<StageLikeRenderable> getAllStagesInOrder() {
		// We don't include cursor and input handler stages here since no one else should be accessing them
		List<StageLikeRenderable> allStages = new ArrayList<>(getGameSpecificStagesInRenderOrder());
		allStages.addFirst(mainStage);
		allStages.addLast(optionsStage);
		return allStages;
	}

	protected Pause pause;
	private final List<DoesNotPause> actorsThatDoNotPause;

	private final UniversalInputStage mainStage;
	private final UniversalInputStage optionsStage;

	private final OptionalDrawStage inputHandlerStage;
	private final OptionalDrawStage cursorStage;

	protected MultiStage(UniversalInputStage mainStage, UniversalInputStage optionsStage, OptionalDrawStage inputHandlerStage, OptionalDrawStage cursorStage) {
		this.mainStage = mainStage;
		this.optionsStage = optionsStage;
		this.inputHandlerStage = inputHandlerStage;

		actorsThatDoNotPause = new ArrayList<>();
		this.cursorStage = cursorStage;
	}

	protected void finishSetup() {
		setShouldRender(true);
		setUpInputMultiplexerForAllStages();
	}

	public void setShouldRender(boolean shouldRender) {
		getAllStagesInOrder().forEach(stage -> stage.setShouldDraw(shouldRender));
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
			getGameSpecificStagesInRenderOrder().forEach(StageLikeRenderable::draw);
		} else {
			updateAndDrawStage(mainStage);
			getGameSpecificStagesInRenderOrder().forEach(this::updateAndDrawStage);
		}

		updateAndDrawStage(optionsStage);
		updateAndDrawStage(cursorStage);
		updateAndDrawStage(inputHandlerStage);
	}


	private void updateAndDrawStage(StageLikeRenderable stageToUpdate) {
		stageToUpdate.act();
		stageToUpdate.draw();
	}

	void resize(int width, int height) {
		getAllStagesInOrder().forEach(stage -> stage.resize(width, height));
	}

	private void doDebugPrinting() {
		List<StageLikeRenderable> allStages = new ArrayList<>(getGameSpecificStagesInRenderOrder().reversed());
		allStages.addFirst(optionsStage);
		allStages.addLast(mainStage);

		Iterator<StageLikeRenderable> stages = allStages.iterator();
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
		inputMultiplexer.addProcessor(optionsStage);
		List<StageLikeRenderable> gameSpecificStages = new ArrayList<>(getGameSpecificStagesInRenderOrder());
		Collections.reverse(gameSpecificStages);
		gameSpecificStages.forEach(inputMultiplexer::addProcessor);
		inputMultiplexer.addProcessor(mainStage);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	public void addActorToMainStage(Actor actor) {
		mainStage.addActor(actor);
	}

	protected UniversalInputStage getMainStage() {
		return mainStage;
	}
	public UniversalInputStage getPauseStage() {
		return optionsStage;
	}

	public void clear() {
		mainStage.clear();
		optionsStage.clear();
		getGameSpecificStagesInRenderOrder().forEach(StageLikeRenderable::clear);
		// We don't clear the cursor stage and input handler stage ever
	}

}