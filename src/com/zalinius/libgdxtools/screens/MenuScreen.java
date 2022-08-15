package com.zalinius.libgdxtools.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zalinius.libgdxtools.DarzalGame;
import com.zalinius.libgdxtools.tools.Assets;
import com.zalinius.libgdxtools.tools.StyleManager;

public class MenuScreen extends StagedScreen {

	public MenuScreen(final Viewport viewport, final DarzalGame game) {
		super(viewport);
		setUpStageAndTable(game);
	}

	private void setUpStageAndTable(final DarzalGame darzalGame) {
		Table menuTable = new Table();
		menuTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		menuTable.pad(100);
		menuTable.defaults().width(400).height(150).center();
		menuTable.setFillParent(true);

		TextButton newGameButton = ScreenElementFactory.makeTitleButton("New Game", () -> {});
		TextButton quitButton = ScreenElementFactory.makeTitleButton("Quit", darzalGame::quit);

		Label gameName = new Label(DarzalGame.getGameName().toUpperCase(), StyleManager.titleStyle);
		gameName.setAlignment(Align.center);
		menuTable.add(gameName);

		menuTable.row();
		menuTable.add().height(50);

		menuTable.row();
		menuTable.add(newGameButton);

		menuTable.row();
		menuTable.add(quitButton);

		stage.addActor(menuTable);

		Actor backgroundActor = ScreenElementFactory.makeBackground(Assets.exampleTexture);
		stage.addActor(backgroundActor);
		backgroundActor.toBack();


		Table headerTable = new Table();
		headerTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		headerTable.setFillParent(true);
		headerTable.top();

		Label credits = new Label("By Zalinius and Darzington", StyleManager.style);
		credits.setFontScale(StyleManager.MINI_FONT_SCALE);
		headerTable.add(credits).left();

		headerTable.add().expand(true, false);

		Label gameVersion = new Label(DarzalGame.getGameVersion(), StyleManager.style);
		gameVersion.setFontScale(StyleManager.MINI_FONT_SCALE);
		headerTable.add(gameVersion).right();

		headerTable.row();

		stage.addActor(headerTable);
	}


}
