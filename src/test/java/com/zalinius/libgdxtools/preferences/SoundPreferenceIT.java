package com.zalinius.libgdxtools.preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.zalinius.libgdxtools.HeadlessDarzalGame;
import com.zalinius.libgdxtools.preferencemanagers.PreferenceManager;
import com.zalinius.libgdxtools.preferencemanagers.SoundPreference;

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
		SoundPreference soundPreferenceManager = new PreferenceManager("com.zalinius.LibGDXTools.preferences").sound();
		soundPreferenceManager.setMusicMuted(shouldBeMuted);
		boolean isMuted = soundPreferenceManager.isMusicMuted();

		assertEquals(shouldBeMuted, isMuted);
	}

}
