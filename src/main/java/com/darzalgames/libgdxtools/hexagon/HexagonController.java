package com.darzalgames.libgdxtools.hexagon;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMath;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

public class HexagonController extends Container<Actor> implements VisibleInputConsumer {

	protected final Hexagon hexagon;
	private final VisibleInputConsumer inputConsumer;
	private final CustomHitbox hitbox;

	public HexagonController(Hexagon hexagon, CustomHitbox hitBox, Function<HexagonController, VisibleInputConsumer> makeInputConsumer) {
		this.hexagon = hexagon;
		this.hitbox = hitBox;
		this.inputConsumer = makeInputConsumer.apply(this);
		this.setActor(inputConsumer.getView());
		this.setSize(inputConsumer.getView().getWidth(), inputConsumer.getView().getHeight());

		setPositionOnScreen();
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		Actor hitActor = super.hit(x, y, touchable);
		if (hitActor == null) {
			return null;
		}

		boolean isInsideHexagon = !hitbox.isHit(x, y);
		if (!isInsideHexagon) {
			return null;
		} else {
			return hitActor;
		}
	}

	private void setPositionOnScreen() {
		Tuple<Float, Float> hexagonPosition =  HexagonMath.getScreenPositionOnStage(hexagon.getQ(), hexagon.getR(),
				inputConsumer.getView().getWidth(), inputConsumer.getView().getHeight(), UserInterfaceSizer.getCurrentHeight());
		this.setOrigin(Align.center);
		this.setPosition(hexagonPosition.e, hexagonPosition.f);
	}

	@Override
	public void consumeKeyInput(Input input) {
		inputConsumer.consumeKeyInput(input);
	}

	@Override
	public void focusCurrent() {
		inputConsumer.focusCurrent();
	}

	@Override
	public void clearSelected() {
		inputConsumer.clearSelected();
	}

	@Override
	public void selectDefault() {
		inputConsumer.selectDefault();
	}

	@Override
	public Actor getView() {
		return inputConsumer.getView();
	}
	
	@Override
	public String toString() {
		return hexagon.toString() + " " + inputConsumer.toString();
	}

	@Override
	public void resizeUI() {
		// TODO Auto-generated method stub
		
	}

}
