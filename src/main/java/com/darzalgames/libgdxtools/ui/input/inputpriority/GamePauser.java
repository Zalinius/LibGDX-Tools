package com.darzalgames.libgdxtools.ui.input.inputpriority;

public class GamePauser {

	private static Runnable pauseGameRunnable;

	public static void setPauseGameRunnable(Runnable pauseGameRunnable) {
		GamePauser.pauseGameRunnable = pauseGameRunnable;
	}

	/**
	 * Will show the options menu if the game is not already paused
	 */
	public static void pause() {
		pauseGameRunnable.run();
	}
}
