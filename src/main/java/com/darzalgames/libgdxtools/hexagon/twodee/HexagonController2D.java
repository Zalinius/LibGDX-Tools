package com.darzalgames.libgdxtools.hexagon.twodee;

import java.util.function.Function;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMath;
import com.darzalgames.libgdxtools.ui.Alignment;
import com.darzalgames.libgdxtools.ui.CustomHitbox;
import com.darzalgames.libgdxtools.ui.UserInterfaceSizer;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.VisibleInputConsumer;

public class HexagonController2D extends Container<Actor> implements VisibleInputConsumer {

	protected final Hexagon hexagon;
	private final VisibleInputConsumer inputConsumer;
	private final CustomHitbox hitbox;

	public HexagonController2D(Hexagon hexagon, CustomHitbox hitBox, Function<HexagonController2D, VisibleInputConsumer> makeInputConsumer) {
		this.hexagon = hexagon;
		hitbox = hitBox;
		inputConsumer = makeInputConsumer.apply(this);
		setActor(inputConsumer.getView());
		this.setOrigin(Align.center);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		Actor hitActor = super.hit(x, y, touchable);
		if (hitActor == null) {
			return null;
		}

		boolean isInsideHexagon = !hitbox.isHit(x, y, getWidth(), getHeight());
		if (!isInsideHexagon) {
			return null;
		} else {
			return hitActor;
		}
	}

	private void setPositionOnScreen() {
		Tuple<Float, Float> hexagonPosition = HexagonMath.getScreenPositionOnStage(
				hexagon.getQ(), hexagon.getR(),
				getWidth(), getHeight(), UserInterfaceSizer.getCurrentHeight()
		);
		this.setPosition(hexagonPosition.e(), hexagonPosition.f());
		inputConsumer.getView().setPosition(0, 0);
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
		inputConsumer.resizeUI();
		setSize(inputConsumer.getView().getWidth(), inputConsumer.getView().getHeight());
		setPositionOnScreen();
	}

	public Hexagon getHexagon() {
		return hexagon;
	}

	@Override
	public boolean isDisabled() {
		return inputConsumer.isDisabled();
	}

	@Override
	public boolean isBlank() {
		return inputConsumer.isBlank();
	}

	@Override
	public void setAlignment(Alignment alignment) {
		inputConsumer.setAlignment(alignment);
	}

	@Override
	public void setFocused(boolean focused) {
		inputConsumer.setFocused(focused);
	}

	@Override
	public void setDisabled(boolean disabled) {
		inputConsumer.setDisabled(disabled);
	}

	@Override
	public boolean isOver() {
		return inputConsumer.isOver();
	}

}
