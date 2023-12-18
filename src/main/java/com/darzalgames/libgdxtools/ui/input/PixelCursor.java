package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.darzalgames.darzalcommon.misc.DoesNotPause;
import com.darzalgames.libgdxtools.graphics.WindowResizer;

public class PixelCursor extends Image implements DoesNotPause {
	
	private final WindowResizer windowResizer;
	
	public PixelCursor(WindowResizer windowResizer, Drawable pixelCursorImage) {
		super(pixelCursorImage);
		setTouchable(Touchable.disabled);
		this.windowResizer = windowResizer;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		toFront();
		float x = Gdx.input.getX();
		float y = Gdx.input.getY();
		if (x == 0 && y == 0 && windowResizer.isWindowed()) {
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

	public void show() {
		this.setVisibility(true);
	}

	public void hide() {
		this.setVisibility(false);
	}
	

	private void setVisibility(boolean shouldBeVisible) {
		setVisible(shouldBeVisible);
	}

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}
}
