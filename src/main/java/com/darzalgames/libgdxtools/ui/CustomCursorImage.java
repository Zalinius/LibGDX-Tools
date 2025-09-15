package com.darzalgames.libgdxtools.ui;

import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darzalgames.libgdxtools.graphics.windowresizer.WindowResizer;
import com.darzalgames.libgdxtools.ui.input.inputpriority.InputStrategyObserver;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class CustomCursorImage extends Image implements InputStrategyObserver {

	private final Supplier<Boolean> checkIsWindowed;
	private final TextureRegion cursorTexture;
	private final TextureRegion clickedCursorTexture;

	/**
	 * An Image which follows the (hidden) cursor around, allowing you to keep the visible cursor the same pixel resolution regardless of window size.
	 * @param checkIsWindowed A supplier that can tell us whether or not we're in windowed mode ({@link WindowResizer#isWindowed()}, perhaps?)
	 */
	public CustomCursorImage(Supplier<Boolean> checkIsWindowed, TextureRegion cursorTexture, InputStrategySwitcher inputStrategySwitcher) {
		this(checkIsWindowed, cursorTexture, cursorTexture, inputStrategySwitcher);
	}

	/**
	 * An Image which follows the (hidden) cursor around, allowing you to keep the visible cursor the same pixel resolution regardless of window size.
	 * @param checkIsWindowed      A supplier that can tell us whether or not we're in windowed mode ({@link WindowResizer#isWindowed()}, perhaps?)
	 * @param clickedCursorTexture The texture shown while the left mouse button is held down
	 */
	public CustomCursorImage(Supplier<Boolean> checkIsWindowed, TextureRegion cursorTexture, TextureRegion clickedCursorTexture, InputStrategySwitcher inputStrategySwitcher) {
		super();
		this.cursorTexture = cursorTexture;
		this.clickedCursorTexture = clickedCursorTexture;
		this.checkIsWindowed = checkIsWindowed;
		setTouchable(Touchable.disabled);
		inputStrategySwitcher.register(this);
	}

	private void setCursorImage(TextureRegion texture) {
		Gdx.graphics.setSystemCursor(SystemCursor.None);
		setDrawable(new Image(texture).getDrawable());
		pack();
		UserInterfaceSizer.scaleToMinimumPercentage(this, 0.05f);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		toFront();
		float x = Gdx.input.getX();
		float y = Gdx.input.getY();
		if (x == 0 && y == 0 && Boolean.TRUE.equals(checkIsWindowed.get())) {
			// When you start with the cursor off-screen, the position returned is 0,0 and the cursor is drawn in the top-left corner
			// so whenever it's there, we hide it off-screen. Technically if a player jams the mouse into that corner it'll hide it too,
			// but that's not a huge deal especially since we only pull this trick in windowed mode.

			// If ever we figure out detecting that the cursor is off-screen then this won't be necessary, but last time I looked into that
			// it seemed like a total hassle. -Darz
			// https://stackoverflow.com/questions/30593479/libgdx-on-desktop-determine-if-mouse-outside-window
			x = -100;
			y = -100;
		}
		Vector2 position = getStage().screenToStageCoordinates(new Vector2(x, y));
		setPosition(position.x, position.y - getHeight());

		setCursorImage(Gdx.input.isButtonPressed(Buttons.LEFT) ? clickedCursorTexture : cursorTexture);
	}

	@Override
	public void inputStrategyChanged(InputStrategySwitcher inputStrategySwitcher) {
		setVisible(inputStrategySwitcher.isMouseMode());
	}

	@Override
	public boolean shouldBeUnregistered() {
		return false;
	}
}
