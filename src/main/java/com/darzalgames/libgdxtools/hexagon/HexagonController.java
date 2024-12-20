package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMath;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.InputConsumerWrapper;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategyManager;

public abstract class HexagonController extends Group implements InputConsumerWrapper {

	private final Hexagon hexagon;
	private final Image hexagonOutline;
	protected final InputStrategyManager inputStrategyManager;
	private final KeyboardButton keyboardButton;
	
	protected HexagonController(Hexagon hexagon, InputStrategyManager inputStrategyManager, boolean mini, String atlasPath) {
		this.hexagon = hexagon;
		this.inputStrategyManager = inputStrategyManager;
		hexagonOutline = makeHexagonOutline();
		keyboardButton = makeHexagonButton();
		keyboardButton.getView().setVisible(false);
		this.addActor(keyboardButton.getView());
		
		Tuple<Float, Float> hexagonPosition = getScreenPositionCoordinates();
		this.setOrigin(Align.center);
		this.setPosition(hexagonPosition.e, hexagonPosition.f);
	}

	protected abstract KeyboardButton makeHexagonButton();
	protected abstract Image makeHexagonOutline();

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

	public void setButtonFocused(boolean focused) {
		keyboardButton.setFocused(focused);
	}

	private Tuple<Float, Float> getScreenPositionCoordinates() {
		return HexagonMath.getScreenPositionCoordinatesOnStage(7, 8, hexagon.getQ(), hexagon.getR(), hexagonOutline.getWidth(), hexagonOutline.getHeight(), GameInfo.getHeight());
	}

}
