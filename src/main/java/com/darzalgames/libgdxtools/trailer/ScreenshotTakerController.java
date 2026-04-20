package com.darzalgames.libgdxtools.trailer;

import com.badlogic.gdx.Gdx;

/**
 * A tidy system for flagging the current frame as one to screenshot, and later taking said screenshot after rendering
 */
public class ScreenshotTakerController {

	private final String screenshotOutputPath;

	private String thisFrameScreenshotName;

	/**
	 * @param screenshotOutputPath where to output screenshots to
	 */
	public ScreenshotTakerController(String screenshotOutputPath) {
		this.screenshotOutputPath = screenshotOutputPath;
	}

	/**
	 * @param screenshotName Set the file name for the screenshot to be taken this frame
	 */
	public void setThisFrameScreenshotName(String screenshotName) {
		thisFrameScreenshotName = screenshotName + ".png";
	}

	/**
	 * Takes a screenshot if {@link #setThisFrameScreenshotName(String screenshotName)} was called this frame.
	 * This must be called AFTER everything in the screenshot has been drawn, aka at the end of render(), and is safe to call every frame.
	 */
	public void takeScreenshotIfQueuedUp() {
		if (thisFrameScreenshotName != null) {
			Gdx.app.log(ScreenshotTakerController.class.getSimpleName().toUpperCase(), "Taking sceenshot: " + thisFrameScreenshotName);
			ScreenshotTaker.takeScreenshot(screenshotOutputPath + thisFrameScreenshotName);
			thisFrameScreenshotName = null;
		}
	}

}
