package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMath;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.InputConsumerWrapper;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public abstract class HexagonController extends Group implements InputConsumerWrapper {

	protected final Hexagon hexagon;
	protected final InputStrategyManager inputStrategyManager;
	private final KeyboardButton keyboardButton;
	
	protected HexagonController(Hexagon hexagon, InputStrategyManager inputStrategyManager) {
		this.hexagon = hexagon;
		this.inputStrategyManager = inputStrategyManager;
		keyboardButton = makeHexagonButton();
		this.addActor(keyboardButton.getView());
		this.setSize(keyboardButton.getView().getPrefWidth(), keyboardButton.getView().getPrefHeight());
		this.debugAll();
		
		setPositionOnScreen();
	}

	protected abstract KeyboardButton makeHexagonButton();

	public void setButtonFocused(boolean focused) {
		keyboardButton.setFocused(focused);
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		Actor hitActor = super.hit(x, y, touchable);
		if (hitActor == null) {
			return null;
		}

		boolean isInsideHexagon = !HexagonButtonHitBox.isPixelTransparentOnMask(x, y);
		if (!isInsideHexagon) {
			return null;
		} else {
			return hitActor;
		}
	}

	private void setPositionOnScreen() {
		// TODO Allow for different ratios
		Tuple<Float, Float> hexagonPosition =  HexagonMath.getScreenPositionOnStage(7, 8, hexagon.getQ(), hexagon.getR(),
				keyboardButton.getView().getPrefWidth(), keyboardButton.getView().getPrefHeight(), GameInfo.getHeight());
		this.setOrigin(Align.center);
		this.setPosition(hexagonPosition.e, hexagonPosition.f);
	}

}
