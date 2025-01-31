package com.darzalgames.libgdxtools.maingame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.ui.CustomCursorImage;
import com.darzalgames.libgdxtools.ui.input.handler.GamepadInputHandler;
import com.darzalgames.libgdxtools.ui.input.handler.KeyboardInputHandler;
import com.darzalgames.libgdxtools.ui.input.inputpriority.Pause;

public class MultipleStage {

	Stage stage;
	protected Stage popUpStage;
	private Stage backgroundStage;
	private Stage cursorStage;
	private Stage inputHandlerStage;
	
	private Pause pause;
	protected List<DoesNotPause> actorsThatDoNotPause = new ArrayList<>();

	public void addActorToMainStage(Actor actor) {
		stage.addActor(actor);
	}
	
	public void addActorThatDoesNotPause(DoesNotPause actor) {
		actorsThatDoNotPause.add(actor);
	}
	
	public void clearPopUpStage() {
		popUpStage.clear();
	}

	void render() {
		backgroundStage.getViewport().apply();
		backgroundStage.draw();

		stage.getViewport().apply();
		if (pause.isPaused()) {
			stage.draw();
			float delta = Gdx.graphics.getDeltaTime();
			actorsThatDoNotPause.forEach(actor -> actor.actWhilePaused(delta));
		} else {
			stage.act();
			stage.draw();
		}

		popUpStage.getViewport().apply();
		popUpStage.act();
		popUpStage.draw();

		cursorStage.getViewport().apply();
		cursorStage.act();
		cursorStage.draw();

		inputHandlerStage.getViewport().apply();
		inputHandlerStage.act();
		inputHandlerStage.draw();
	}
	
	void resize (int width, int height) {
		backgroundStage.getViewport().update(width, height, true);
		backgroundStage.getCamera().update();
		stage.getViewport().update(width, height, true);
		stage.getCamera().update();
		inputHandlerStage.getViewport().update(width, height, true);
		inputHandlerStage.getCamera().update();
		popUpStage.getViewport().update(width, height, true);
		popUpStage.getCamera().update();
		cursorStage.getViewport().update(width, height, true);
		cursorStage.getCamera().update();
	}

	void setUpInputMultiplexerForAllStages() {
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(inputHandlerStage);
		inputMultiplexer.addProcessor(cursorStage);
		inputMultiplexer.addProcessor(popUpStage);
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	void setUpInputHandlersOnStages(KeyboardInputHandler keyboardInputHandler, GamepadInputHandler gamepadInputHandler, CustomCursorImage customCursorImage) {
		inputHandlerStage.addActor(keyboardInputHandler);
		actorsThatDoNotPause.add(keyboardInputHandler);
		
		inputHandlerStage.addActor(gamepadInputHandler);
		actorsThatDoNotPause.add(gamepadInputHandler);
		
		stage.setKeyboardFocus(keyboardInputHandler);
		
		cursorStage.addActor(customCursorImage);
	}


	void setMainStage(Stage stage) {
		this.stage = stage;
	}

	void setPopUpStage(Stage popUpStage) {
		this.popUpStage = popUpStage;
	}

	void setBackgroundStage(Stage backgroundStage) {
		this.backgroundStage = backgroundStage;
	}

	void setCursorStage(Stage cursorStage) {
		this.cursorStage = cursorStage;
	}

	void setInputHandlerStage(Stage inputHandlerStage) {
		this.inputHandlerStage = inputHandlerStage;
	}

	void setPause(Pause pause) {
		this.pause = pause;
		addActorToMainStage(pause);
	}
	
}
