package com.darzalgames.libgdxtools.trailer;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.darzalgames.darzalcommon.filehandling.DirectoryClearer;

public class TrailerFrameRecorder {

	private int frameCount = 0;

	private final String framesPath;

	/**
	 * A system which records every frame of your trailer to the specified location, numbered sequentially
	 * @param externalFramesPath the external location to output the frames to, e.g. "Downloads/trailer/frames/"
	 */
	public TrailerFrameRecorder(String externalFramesPath) {
		framesPath = externalFramesPath;
		Gdx.graphics.setForegroundFPS(60);

		try {
			clearPreviousRecording();
		} catch (IOException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	}

	private void clearPreviousRecording() throws IOException {
		DirectoryClearer.clear(Gdx.files.external(framesPath).file());
	}

	public void recordFrame() {
		ScreenshotTaker.takeScreenshot(framesPath + frameCount + ".png");
		frameCount++;
	}

}
