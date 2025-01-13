package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMath;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.InputConsumerWrapper;
import com.darzalgames.libgdxtools.ui.input.Interactable;

public abstract class HexagonController extends Container<Actor> implements InputConsumerWrapper {

	protected final Hexagon hexagon;
	private final Interactable interactable;
	
	protected HexagonController(Hexagon hexagon) {
		this.hexagon = hexagon;
		interactable = makeHexagonButton();
		this.setActor(interactable.getView());
		this.setSize(interactable.getView().getWidth(), interactable.getView().getHeight());
		
		setPositionOnScreen();
	}

	protected abstract Interactable makeHexagonButton();

	public void setButtonFocused(boolean focused) {
		if (focused) {
			interactable.focus();
		} else {
			interactable.unfocus();
		}
	}

	
	public void press() {
		interactable.press();
	}

	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		Actor hitActor = super.hit(x, y, touchable);
		if (hitActor == null) {
			return null;
		}

		boolean isInsideHexagon = !CustomButtonHitBox.isHit(x, y);
		if (!isInsideHexagon) {
			return null;
		} else {
			return hitActor;
		}
	}

	private void setPositionOnScreen() {
		Tuple<Float, Float> hexagonPosition =  HexagonMath.getScreenPositionOnStage(hexagon.getQ(), hexagon.getR(),
				interactable.getView().getWidth(), interactable.getView().getHeight(), GameInfo.getHeight());
		this.setOrigin(Align.center);
		this.setPosition(hexagonPosition.e, hexagonPosition.f);
	}

}
