package com.darzalgames.libgdxtools.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GamePlatformHelperTest {

	@Test
	void getGamePlatform_withWindowsInArguments_returnsWindows() throws Exception {
		GamePlatform result = DesktopGamePlatformHelper.getTypeFromArgs(new String[]{"windows"});
		
		assertEquals(WindowsGamePlatform.class, result.getClass());
	}
	
	@Test
	void getGamePlatform_withWindowsAmongOtherArguments_returnsWindows() throws Exception {
		GamePlatform result = DesktopGamePlatformHelper.getTypeFromArgs(new String[]{"skip-menu", "windows", "garbage"});
		
		assertEquals(WindowsGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withLinux_returnsLinux() throws Exception {
		GamePlatform result = DesktopGamePlatformHelper.getTypeFromArgs(new String[]{"linux"});
		
		assertEquals(LinuxGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withNoValidPlatform_throwsIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> DesktopGamePlatformHelper.getTypeFromArgs(new String[]{"dos"}));
	}

}
