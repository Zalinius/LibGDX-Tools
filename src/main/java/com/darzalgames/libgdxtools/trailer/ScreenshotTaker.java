package com.darzalgames.libgdxtools.trailer;

import java.nio.ByteBuffer;
import java.util.zip.Deflater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

public class ScreenshotTaker {

	private ScreenshotTaker() {
		// utility class
	}

	public static void takeScreenshot(String outputPath) {
		Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
		ByteBuffer pixels = pixmap.getPixels();

		// This loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
		int size = Gdx.graphics.getBackBufferWidth() * Gdx.graphics.getBackBufferHeight() * 4;
		for (int i = 3; i < size; i += 4) {
			pixels.put(i, (byte) 255);
		}

		FileHandle frameFile = Gdx.files.external(outputPath);
		PixmapIO.writePNG(frameFile, pixmap, Deflater.DEFAULT_COMPRESSION, true);
		pixmap.dispose();
	}

}
