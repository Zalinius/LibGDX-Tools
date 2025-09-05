package com.darzalgames.libgdxtools.assetloading;

/**
 * A blank loading screen (no animations or loading bar), ideal for new projects or ones which load very fast
 */
public class BlankLoadingScreen implements LoadingScreen {

	@Override
	public void update(float completion) {
		// No visuals, the screen stays blank
	}

	@Override
	public void startExitAnimation() {
		// No visuals, the screen stays blank
	}

	@Override
	public boolean hasFinishedAnimating() {
		return true;
	}

}
