package com.darzalgames.libgdxtools.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.platform.GamePlatform;

class GamePlatformTest {

	@Test
	void getGamePlatform_withWindowsInArguments_returnsWindows() throws Exception {
		GamePlatform result = GamePlatform.getTypeFromArgs(new String[]{"windows"});
		
		assertEquals(GamePlatform.WINDOWS, result);
	}
	
	@Test
	void getGamePlatform_withWindowsAmongOtherArguments_returnsWindows() throws Exception {
		GamePlatform result = GamePlatform.getTypeFromArgs(new String[]{"skip-menu", "windows", "garbage"});
		
		assertEquals(GamePlatform.WINDOWS, result);
	}

	@Test
	void getGamePlatform_withLinux_returnsLinux() throws Exception {
		GamePlatform result = GamePlatform.getTypeFromArgs(new String[]{"linux"});
		
		assertEquals(GamePlatform.LINUX, result);
	}
	
}
