package com.darzalgames.libgdxtools.ui.input;

import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.graphics.WindowResizer;
import com.darzalgames.libgdxtools.maingame.GameInfo;

public class CustomCursorImage extends Image implements DoesNotPause, InputObserver {
	
	private final Supplier<Boolean> checkIsWindowed;
	
	/**
	 * An Image which follows the (hidden) cursor around, allowing you to keep the visible cursor the same pixel resolution regardless of window size.
	 * @param checkIsWindowed A supplier that can tell us whether or not we're in windowed mode ({@link WindowResizer#isWindowed()}, perhaps?)
	 * @param cursorImage
	 */
	public CustomCursorImage(Supplier<Boolean> checkIsWindowed, Texture cursorImage) {
		super(cursorImage);
		setTouchable(Touchable.disabled);
		this.checkIsWindowed = checkIsWindowed;
		GameInfo.getInputStrategyManager().register(this);
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
			x = -100;
			y = -100;
		}
		Vector2 position = getStage().screenToStageCoordinates(new Vector2(x, y));
		setPosition(position.x, position.y - getHeight());
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}

	@Override
	public void inputStrategyChanged() {
		setVisible(GameInfo.getInputStrategyManager().showMouseExclusiveUI());
	}

	@Override
	public boolean shouldBeUnregistered() {
		return false;
	}
}
