package com.darzalgames.libgdxtools.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.darzalgames.darzalcommon.data.Coordinate;

public class WindowResizerDesktop extends WindowResizer {

	private int defaultWindowWidth;
	private int defaultWindowHeight;

	public WindowResizerDesktop(int defaultWindowWidth, int defaultWindowHeight) {
		super();
		this.defaultWindowWidth = defaultWindowWidth;
		this.defaultWindowHeight = defaultWindowHeight;
	}

	@Override
	protected void switchToWindowed() {
		Gdx.graphics.setUndecorated(false);
		Gdx.graphics.setWindowedMode(defaultWindowWidth, defaultWindowHeight);
	}

	@Override
	protected void switchToBorderless() {
		DisplayMode displayMode = Gdx.graphics.getDisplayMode();
		Gdx.graphics.setUndecorated(true);
		Gdx.graphics.setWindowedMode(displayMode.width, displayMode.height);
	}
	
	@Override
	protected void switchToFullScreen() {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
	}
	
	@Override
	public void setScreenSize(Coordinate size) {
		Gdx.graphics.setUndecorated(false);
		Gdx.graphics.setWindowedMode(size.i, size.j);
	}
}
