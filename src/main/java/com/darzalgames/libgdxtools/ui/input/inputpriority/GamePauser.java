package com.darzalgames.libgdxtools.ui.input.inputpriority;

public class GamePauser {

	private static Runnable pauseGameIfNeededRunnable;

	public static void setPauseGameIfNeededRunnable(Runnable pauseGameIfNeededRunnable) {
		GamePauser.pauseGameIfNeededRunnable = pauseGameIfNeededRunnable;
	}
	
	public static void pauseIfNeeded() {
		pauseGameIfNeededRunnable.run();
	}
}
