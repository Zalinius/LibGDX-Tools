package com.darzalgames.libgdxtools.trailer;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.darzalgames.darzalcommon.filehandling.DirectoryClearer;

/**
 * A system which records every frame of the running application to the specified location.
 * Frames are numbered sequentially.
 */
public class TrailerFrameRecorder {

	private int frameCount = 0;

	private final String framesPath;

	/**
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

	/**
	 * Records the current frame. This must be called AFTER everything in the scene has been drawn, aka at the end of render()
	 */
	public void recordFrame() {
		ScreenshotTaker.takeScreenshot(framesPath + frameCount + ".png");
		frameCount++;
	}

	private void clearPreviousRecording() throws IOException {
		DirectoryClearer.clear(Gdx.files.external(framesPath).file());
	}

}
