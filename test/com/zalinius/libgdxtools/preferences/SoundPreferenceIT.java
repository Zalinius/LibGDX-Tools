package com.zalinius.libgdxtools.preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.zalinius.libgdxtools.HeadlessDarzalGame;
import com.zalinius.libgdxtools.preferencemanagers.SoundPreferenceManager;

public class SoundPreferenceIT {
	private static HeadlessApplication application;

	@BeforeAll
	public static void setUp() throws Exception {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		application = new HeadlessApplication(new HeadlessDarzalGame(), config);
	}

	@Test
	void setMusicMuted_whenTrue_isSavedToPreferenceFile() throws Exception {
		boolean shouldBeMuted = true;

		SoundPreferenceManager.setMusicMuted(shouldBeMuted);
		boolean isMuted = SoundPreferenceManager.isMusicMuted();

		assertEquals(shouldBeMuted, isMuted);
	}

}
