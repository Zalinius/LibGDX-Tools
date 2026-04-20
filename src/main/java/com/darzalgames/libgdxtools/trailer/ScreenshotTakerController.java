package com.darzalgames.libgdxtools.trailer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ScreenshotTakerController extends Actor {

	private final String screenshotOutputPath;

	private String thisFrameScreenshotName;

	public ScreenshotTakerController(String screenshotOutputPath) {
		super();
		this.screenshotOutputPath = screenshotOutputPath;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (thisFrameScreenshotName != null) {
			Gdx.app.log("Screenshot Taker Main", "Taking sceenshot: " + thisFrameScreenshotName);
			ScreenshotTaker.takeScreenshot(screenshotOutputPath + thisFrameScreenshotName);
			thisFrameScreenshotName = null;
		}
	}

	public void takeScreenshot(String screenshotName) {
		thisFrameScreenshotName = screenshotName + ".png";
	}

}
