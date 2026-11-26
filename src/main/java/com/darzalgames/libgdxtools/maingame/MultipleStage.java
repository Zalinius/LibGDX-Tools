package com.darzalgames.libgdxtools.maingame;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darzalgames.libgdxtools.trailer.TrailerFrameRecorder;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Pause;

public final class MultipleStage {

	private static final boolean SHOULD_DEBUG_PRINT_ACTOR_UNDER_CURSOR = false;

	private boolean forceFramerateForTrailerRecording;

	public static final String MAIN_STAGE_NAME = "Main Stage";
	public static final String OPTIONS_STAGE_NAME = "Options Stage";
	public static final String CURSOR_STAGE_NAME = "Cursor Stage";
	public static final String INPUT_HANDLER_STAGE_NAME = "Input Handler Stage";

	private final UniversalInputStage mainStage;
	private final UniversalInputStage optionsStage;
	private final StageBest cursorStage;
	private final StageBest inputHandlerStage;

	private final Pause pause;
	private final List<StageLikeRenderable> gameSpecificStages;

	private TrailerFrameRecorder trailerFrameRecorder;

	MultipleStage(UniversalInputStage mainStage, List<StageLikeRenderable> gameSpecificStages, UniversalInputStage optionsStage, StageBest cursorStage, StageBest inputHandlerStage, Pause pause) {
		this.mainStage = mainStage;
		this.optionsStage = optionsStage;
		this.cursorStage = cursorStage;
		this.inputHandlerStage = inputHandlerStage;

		this.pause = pause;
		inputHandlerStage.addActor(pause);

		this.gameSpecificStages = gameSpecificStages;
		GetOnStage.setAddActorToStageFunction(this::addActorToStage);
	}

	public void addActorToStage(Actor actor, String stageName) {
		findStageByName(stageName).ifPresent(stage -> stage.addActor(actor));
	}

	public void clearStage(String stageName) {
		findStageByName(stageName).ifPresent(StageLikeRenderable::clear);
	}

	public void clearAllGameStages() {
		getGameStagesInOrder().forEach(StageLikeRenderable::clear);
		// We don't clear the cursor stage and input handler stage ever
	}

	void update() {
		if (SHOULD_DEBUG_PRINT_ACTOR_UNDER_CURSOR) {
			doDebugPrinting();
		}

		float minimumFPS = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
		if (forceFramerateForTrailerRecording) {
			minimumFPS = 1 / 60f;
		}
		float preferredFPS = minimumFPS;

		List<StageLikeRenderable> allPausableStages = new ArrayList<>(gameSpecificStages);
		allPausableStages.addFirst(mainStage);
		if (!pause.isPaused()) {
			allPausableStages.forEach(stage -> updateAndDrawStage(stage, preferredFPS));
		} else {
			resizeUIWhilePaused();
			boolean hitPausingStage = false;
			for (StageLikeRenderable stageLike : allPausableStages) {
				if (stageLike.getName().equals(pause.getNameOfPausingStage())) {
					hitPausingStage = true;
				}

				if (hitPausingStage) {
					// Once we find the stage which is pausing the game, update and draw it and all stages above it like normal
					updateAndDrawStage(stageLike, preferredFPS);
				} else {
					// skip stage.act() while paused
					stageLike.draw();
				}
			}
		}

		updateAndDrawStage(optionsStage, preferredFPS);
		updateAndDrawStage(cursorStage, preferredFPS);
		updateAndDrawStage(inputHandlerStage, preferredFPS);

		if (forceFramerateForTrailerRecording) {
			trailerFrameRecorder.recordFrame();
		}
	}

	private void updateAndDrawStage(StageLikeRenderable stageToUpdate, float delta) {
		stageToUpdate.act(delta);
		stageToUpdate.draw();
	}

	void resize(int width, int height) {
		getAllStagesInOrder().forEach(stage -> stage.resize(width, height));
	}

	private void doDebugPrinting() {
		Iterator<StageLikeRenderable> stages = getGameStagesInOrder().reversed().iterator();
		boolean hitSomething = false;
		while (!hitSomething && stages.hasNext()) {
			hitSomething = tryToPrintADebugHit(stages.next());
		}
	}

	private boolean tryToPrintADebugHit(StageLikeRenderable stage) {
		String hit = stage.nameOfActorUnderCursor();
		boolean hitSomething = hit != null && !hit.isBlank();
		if (hitSomething) {
			Gdx.app.log("MultiStage", stage.getName() + ": " + hit);
		}
		return hitSomething;
	}

	void setUpInputHandlersOnStages(KeyboardInputHandler keyboardInputHandler, GamepadInputHandler gamepadInputHandler) {
		inputHandlerStage.addActor(keyboardInputHandler);
		inputHandlerStage.addActor(gamepadInputHandler);

		mainStage.setKeyboardFocus(keyboardInputHandler);
	}

	List<StageLikeRenderable> getGameStagesInOrder() {
		// We don't include cursor and input handler stages here since no one else should be accessing them
		List<StageLikeRenderable> allStages = new ArrayList<>(gameSpecificStages);
		allStages.addFirst(mainStage);
		allStages.addLast(optionsStage);
		return allStages;
	}

	private List<StageLikeRenderable> getAllStagesInOrder() {
		List<StageLikeRenderable> allStages = new ArrayList<>(getGameStagesInOrder());
		allStages.add(cursorStage);
		allStages.add(inputHandlerStage);
		return allStages;
	}

	void setUpInputMultiplexerForAllStages() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		getAllStagesInOrder().reversed().forEach(inputMultiplexer::addProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	private Optional<StageLikeRenderable> findStageByName(String stageName) {
		return getAllStagesInOrder().stream().filter(stage -> stage.getName().equals(stageName)).findFirst();
	}

	private void resizeUIWhilePaused() {
		getGameStagesInOrder().forEach(stage -> stage.act(0));
	}

	public void forceFramerateForTrailerRecording(TrailerFrameRecorder trailerFrameRecorder) {
		this.trailerFrameRecorder = trailerFrameRecorder;
		forceFramerateForTrailerRecording = true;
	}

}