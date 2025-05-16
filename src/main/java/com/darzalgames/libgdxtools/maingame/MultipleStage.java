package com.darzalgames.libgdxtools.maingame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.ui.input.OptionalDrawStage;
import com.darzalgames.libgdxtools.ui.input.UniversalInputStage;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Pause;
import com.darzalgames.libgdxtools.ui.input.inputpriority.ScrollingManager;

public class MultipleStage {

	private static final boolean SHOULD_DEBUG_PRINT_ACTOR_UNDER_CURSOR = false;

	private final UniversalInputStage stage;
	private final UniversalInputStage popUpStage;
	private final OptionalDrawStage cursorStage;
	private final OptionalDrawStage inputHandlerStage;

	private Pause pause;
	private final List<DoesNotPause> actorsThatDoNotPause;

	public MultipleStage(UniversalInputStage stage, UniversalInputStage popUpStage, OptionalDrawStage cursorStage, OptionalDrawStage inputHandlerStage) {
		this.stage = stage;
		this.popUpStage = popUpStage;
		this.cursorStage = cursorStage;
		this.inputHandlerStage = inputHandlerStage;
		actorsThatDoNotPause = new ArrayList<>();
		setUpInputMultiplexerForAllStages();
		setShouldRender(true);
	}

	public void setShouldRender(boolean shouldRender) {
		stage.setShouldDraw(shouldRender);
		popUpStage.setShouldDraw(shouldRender);
		cursorStage.setShouldDraw(shouldRender);
		inputHandlerStage.setShouldDraw(shouldRender);
	}

	public void addActorThatDoesNotPause(DoesNotPause actor) {
		actorsThatDoNotPause.add(actor);
	}

	public void clearPopUpStage() {
		popUpStage.clear();
	}

	void update(Runnable furtherRendering) {
		if (SHOULD_DEBUG_PRINT_ACTOR_UNDER_CURSOR) {
			doDebugPrinting();
		}

		if (pause.isPaused()) {
			// skip stage.act() while paused
			stage.draw();
			float delta = Gdx.graphics.getDeltaTime();
			actorsThatDoNotPause.forEach(actor -> actor.actWhilePaused(delta));
		} else {
			updateAndDrawStage(stage);
		}
		furtherRendering.run();

		updateAndDrawStage(popUpStage);
		updateAndDrawStage(cursorStage);
		updateAndDrawStage(inputHandlerStage);
	}

	private void updateAndDrawStage(Stage stageToUpdate) {
		stageToUpdate.act();
		stageToUpdate.draw();
	}

	void resize(int width, int height) {
		resizeStage(width, height, stage);
		resizeStage(width, height, inputHandlerStage);
		resizeStage(width, height, popUpStage);
		resizeStage(width, height, cursorStage);
	}

	private static void resizeStage(int width, int height, Stage stageToResize) {
		stageToResize.getViewport().update(width, height, true);
		stageToResize.getCamera().update();
	}

	void setUpInputHandlersOnStages(KeyboardInputHandler keyboardInputHandler, GamepadInputHandler gamepadInputHandler, ScrollingManager scrollingManager) {
		inputHandlerStage.addActor(keyboardInputHandler);
		actorsThatDoNotPause.add(keyboardInputHandler);

		inputHandlerStage.addActor(gamepadInputHandler);
		actorsThatDoNotPause.add(gamepadInputHandler);

		inputHandlerStage.addActor(scrollingManager);
		actorsThatDoNotPause.add(scrollingManager);

		stage.setKeyboardFocus(keyboardInputHandler);
	}

	void setPause(Pause pause) {
		this.pause = pause;
		inputHandlerStage.addActor(pause);
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
			Gdx.app.log("Stage", stage.getRoot().getName() + ": " + hitActor);
		}
		return hitSomething;
	}

	public UniversalInputStage getStage() {
		return stage;
	}

	public UniversalInputStage getPopUpStage() {
		return popUpStage;
	}


}
