package com.darzalgames.libgdxtools;

import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.codedisaster.steamworks.SteamController;
import com.darzalgames.darzalcommon.misc.DoesNotPause;
import com.darzalgames.libgdxtools.graphics.WindowResizer;
import com.darzalgames.libgdxtools.preferencemanagers.PreferenceManager;
import com.darzalgames.libgdxtools.ui.input.InputStrategyManager;
import com.darzalgames.libgdxtools.ui.screen.GameScreen;

public abstract class MainGame extends ApplicationAdapter {

	private final int WIDTH;
	private final int HEIGHT;

	private InputStrategyManager inputStrategyManager;
	
	private Stage stage;
	private Stage backgroundStage;
	private Stage popUpStage;
	private Stage cursorStage;
	private Stage mouseDetectorStage;

	private GameScreen currentScreen;

	private WindowResizer windowResizer;

	private PreferenceManager preferenceManager;

	private SteamController steamController;

	private List<DoesNotPause> actorsThatDoNotPause;
	
	private static MainGame instance;
	
	protected MainGame(int width, int height) {
		super();
		setInstance(this);
		WIDTH = width;
		HEIGHT = height;
	}
	
	private static void setInstance(MainGame mainGame) {
		MainGame.instance = mainGame;
	}

	public static int getWidth() {
		return instance.WIDTH;
	}

	public static int getHeight() {
		return instance.HEIGHT;
	}

	public static InputStrategyManager getInputStrategyManager() {
		return instance.inputStrategyManager;
	}
	
	
	
}
