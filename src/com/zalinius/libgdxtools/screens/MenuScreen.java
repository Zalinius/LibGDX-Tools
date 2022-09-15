package com.zalinius.libgdxtools.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zalinius.libgdxtools.DarzalGame;
import com.zalinius.libgdxtools.graphics.ScreenElementFactory;

public class MenuScreen extends StagedScreen {

	public MenuScreen(final Viewport viewport, final DarzalGame darzalGame, final ScreenElementFactory screenElementFactory) {
		super(viewport);
		Table menuTable = new Table();
		menuTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		menuTable.pad(100);
		menuTable.defaults().width(400).height(150).center();
		menuTable.setFillParent(true);

		TextButton newGameButton = screenElementFactory.makeRegularButton("New Game", darzalGame.getPlayButtonRunnable());
		TextButton quitButton = screenElementFactory.makeRegularButton("Quit", darzalGame::quit);

		Label gameName = screenElementFactory.makeTitleLabel(darzalGame.getGameName().toUpperCase());
		gameName.setAlignment(Align.center);
		menuTable.add(gameName);

		menuTable.row();
		menuTable.add().height(50);

		menuTable.row();
		menuTable.add(newGameButton);

		menuTable.row();
		menuTable.add(quitButton);

		stage.addActor(menuTable);

		Actor backgroundActor = screenElementFactory.makeBackground(darzalGame.getMenuBackgroundTexture());
		stage.addActor(backgroundActor);
		backgroundActor.toBack();


		Table headerTable = new Table();
		headerTable.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		headerTable.setFillParent(true);
		headerTable.top();

		Label credits = screenElementFactory.makeRegularLabel("By Zalinius and Darzington");
		credits.setFontScale(screenElementFactory.MINI_FONT_SCALE);
		headerTable.add(credits).left();

		headerTable.add().expand(true, false);

		Label gameVersion = screenElementFactory.makeRegularLabel(darzalGame.getGameVersion());
		gameVersion.setFontScale(screenElementFactory.MINI_FONT_SCALE);
		headerTable.add(gameVersion).right();

		headerTable.row();

		stage.addActor(headerTable);
	}


}
