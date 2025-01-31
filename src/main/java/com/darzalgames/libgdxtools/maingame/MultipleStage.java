package com.darzalgames.libgdxtools.maingame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Pause;

public class MultipleStage {

	private static boolean SHOULD_DEBUG_PRINT_ACTOR_UNDER_CURSOR = false;

	public UniversalInputStage stage;
	public UniversalInputStage popUpStage;
	private Stage backgroundStage;
	private Stage cursorStage;
	private Stage inputHandlerStage;

	private Pause pause;
	private List<DoesNotPause> actorsThatDoNotPause;

	public MultipleStage(UniversalInputStage stage, UniversalInputStage popUpStage, Stage backgroundStage, Stage cursorStage, Stage inputHandlerStage) {
		this.stage = stage;
		this.popUpStage = popUpStage;
		this.backgroundStage = backgroundStage;
		this.cursorStage = cursorStage;
		this.inputHandlerStage = inputHandlerStage;
		this.actorsThatDoNotPause = new ArrayList<>();
		setUpInputMultiplexerForAllStages();
	}

	public void addActorThatDoesNotPause(DoesNotPause actor) {
		actorsThatDoNotPause.add(actor);
	}

	public void clearPopUpStage() {
		popUpStage.clear();
	}

	void render() {
		if (SHOULD_DEBUG_PRINT_ACTOR_UNDER_CURSOR) {
			doDebugPrinting();
		}

		updateAndDrawStage(backgroundStage);

		if (pause.isPaused()) {
			stage.getViewport().apply();
			// skip stage.act() while paused
			stage.draw();
			float delta = Gdx.graphics.getDeltaTime();
			actorsThatDoNotPause.forEach(actor -> actor.actWhilePaused(delta));
		} else {
			updateAndDrawStage(stage);
		}

		updateAndDrawStage(popUpStage);
		updateAndDrawStage(cursorStage);
		updateAndDrawStage(inputHandlerStage);
	}
	
	private static void updateAndDrawStage(Stage stageToUpdate) {
		stageToUpdate.getViewport().apply();
		stageToUpdate.act();
		stageToUpdate.draw();
	}

	void resize(int width, int height) {
		resizeStage(width, height, backgroundStage);
		resizeStage(width, height, stage);
		resizeStage(width, height, inputHandlerStage);
		resizeStage(width, height, popUpStage);
		resizeStage(width, height, cursorStage);
	}
	
	private static void resizeStage(int width, int height, Stage stageToResize) {
		stageToResize.getViewport().update(width, height, true);
		stageToResize.getCamera().update();
	}

	void setUpInputHandlersOnStages(KeyboardInputHandler keyboardInputHandler, GamepadInputHandler gamepadInputHandler) {
		inputHandlerStage.addActor(keyboardInputHandler);
		actorsThatDoNotPause.add(keyboardInputHandler);

		inputHandlerStage.addActor(gamepadInputHandler);
		actorsThatDoNotPause.add(gamepadInputHandler);

		stage.setKeyboardFocus(keyboardInputHandler);
	}

	void setPause(Pause pause) {
		this.pause = pause;
		stage.addActor(pause);
	}

	private void setUpInputMultiplexerForAllStages() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(inputHandlerStage);
		inputMultiplexer.addProcessor(cursorStage);
		inputMultiplexer.addProcessor(popUpStage);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	private void doDebugPrinting() {
		boolean hitPopUpStage = tryToPrintADebugHit(popUpStage);
		if (!hitPopUpStage) {
			tryToPrintADebugHit(stage);
		}
	}

	private boolean tryToPrintADebugHit(Stage stage) {
		Vector2 cursor = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		Actor hitActor = stage.hit(cursor.x, cursor.y, true);
		boolean hitSomething = hitActor != null;
		if (hitSomething) {
			System.out.println(stage.getRoot().getName() + ": " + hitActor);				
		}
		return hitSomething;
	}

}
