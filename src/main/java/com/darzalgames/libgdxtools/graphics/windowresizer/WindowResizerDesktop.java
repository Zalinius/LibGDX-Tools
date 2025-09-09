package com.darzalgames.libgdxtools.graphics.windowresizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.darzalgames.darzalcommon.data.Coordinate;
import com.darzalgames.libgdxtools.maingame.GameInfo;

public class WindowResizerDesktop extends WindowResizer {

	@Override
	protected void switchToWindowed() {
		Coordinate preferredWindowSize = GameInfo.getPreferenceManager().graphics().getPreferredWindowSize();
		Gdx.graphics.setUndecorated(false);
		Gdx.graphics.setWindowedMode(preferredWindowSize.i(), preferredWindowSize.j());
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
		Gdx.graphics.setWindowedMode(size.i(), size.j());
	}
}
