package com.darzalgames.libgdxtools.hexagon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMath;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

public abstract class HexagonController extends Container<Actor> implements VisibleInputConsumer {
	
	public static final String HEXAGON_MASK_KEY = "Hexagon";

	protected final Hexagon hexagon;
	private final VisibleInputConsumer inputConsumer;
	private final CustomHitbox hitBox;
	
	protected HexagonController(Hexagon hexagon, CustomHitbox hitBox) {
		this.hexagon = hexagon;
		this.hitBox = hitBox;
		this.inputConsumer = makeInputConsumer();
		this.setActor(inputConsumer.getView());
		this.setSize(inputConsumer.getView().getWidth(), inputConsumer.getView().getHeight());
		
		setPositionOnScreen();
	}

	protected abstract VisibleInputConsumer makeInputConsumer();

	public void setButtonFocused(boolean focused) {
		if (focused) {
			inputConsumer.regainFocus();
		} else {
			inputConsumer.loseFocus();
		}
	}

	
	public void press() {
		inputConsumer.consumeKeyInput(Input.ACCEPT);
	}

	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		Actor hitActor = super.hit(x, y, touchable);
		if (hitActor == null) {
			return null;
		}

		boolean isInsideHexagon = !hitBox.isHit(x, y);
		if (!isInsideHexagon) {
			return null;
		} else {
			return hitActor;
		}
	}

	private void setPositionOnScreen() {
		Tuple<Float, Float> hexagonPosition =  HexagonMath.getScreenPositionOnStage(hexagon.getQ(), hexagon.getR(),
				inputConsumer.getView().getWidth(), inputConsumer.getView().getHeight(), GameInfo.getHeight());
		this.setOrigin(Align.center);
		this.setPosition(hexagonPosition.e, hexagonPosition.f);
	}

	@Override
	public void consumeKeyInput(Input input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusCurrent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearSelected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectDefault() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Actor getView() {
		// TODO Auto-generated method stub
		return null;
	}

}
