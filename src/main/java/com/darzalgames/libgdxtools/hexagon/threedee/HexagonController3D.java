package com.darzalgames.libgdxtools.hexagon.threedee;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darzalgames.darzalcommon.data.Tuple;
import com.darzalgames.darzalcommon.hexagon.Hexagon;
import com.darzalgames.darzalcommon.hexagon.HexagonMath;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;

public abstract class HexagonController3D implements InputConsumer {

	public static final float HEXAGON_HEIGHT_TO_WIDTH_RATIO = 1.1547005f;

	private final Attribute notSelected;
	private final Attribute highlighted;

	protected final Hexagon hexagon;
	protected ModelInstance hexagonRing;


	public HexagonController3D(Hexagon hexagon, Attribute notSelected, Attribute highlighted, ModelInstance hexagonRing) {
		this.hexagon = hexagon;
		this.notSelected = notSelected;
		this.highlighted = highlighted;
		this.hexagonRing = hexagonRing;

	}

	protected float getPadding() {
		return 0.1f;
	}

	@Override
	public void resizeUI() {
		Tuple<Float, Float> screenCoords = HexagonMath.getScreenPosition(hexagon.getQ(), hexagon.getR(), 1 + getPadding(), 1f/HEXAGON_HEIGHT_TO_WIDTH_RATIO + getPadding());
		Vector3 newPos = new Vector3(screenCoords.e, 0, screenCoords.f);
		hexagonRing.transform.setToTranslation(newPos);
	}

	@Override
	public void focusCurrent() {
		for (Material material : hexagonRing.materials.iterator()) {
			material.set(highlighted);
		}
	}

	@Override
	public void clearSelected() {
		for (Material material : hexagonRing.materials.iterator()) {
			material.set(notSelected);
		}
	}

	@Override
	public void selectDefault() {}

	@Override
	public void setTouchable(Touchable isTouchable) {
		// TODO Auto-generated method stub
	}


	@Override
	public String toString() {
		return hexagon.toString() + " 3D controller";
	}

	protected final Vector3 getCurrentPosition() {
		return hexagonRing.transform.getTranslation(new Vector3());
	}

}
