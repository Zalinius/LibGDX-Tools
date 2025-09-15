package com.darzalgames.libgdxtools.assetloading;

public interface LoadingScreen {

	void update(float completion);

	void startExitAnimation();

	boolean hasFinishedAnimating();

}
