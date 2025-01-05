package com.darzalgames.libgdxtools.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DesktopGamePlatformHelperTest {

	@Test
	void getGamePlatform_withWindowsInArguments_returnsWindows() throws Exception {
		GamePlatform result = DesktopGamePlatformHelper.getTypeFromArgs(new String[]{"windows"}, WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);
		
		assertEquals(WindowsGamePlatform.class, result.getClass());
	}
	
	@Test
	void getGamePlatform_withWindowsAmongOtherArguments_returnsWindows() throws Exception {
		GamePlatform result = DesktopGamePlatformHelper.getTypeFromArgs(new String[]{"skip-menu", "windows", "garbage"}, WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);
		
		assertEquals(WindowsGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withLinux_returnsLinux() throws Exception {
		GamePlatform result = DesktopGamePlatformHelper.getTypeFromArgs(new String[]{"linux"}, WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);
		
		assertEquals(LinuxGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withMac_returnsMac() throws Exception {
		GamePlatform result = DesktopGamePlatformHelper.getTypeFromArgs(new String[]{"mac"}, WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);
		
		assertEquals(MacGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withNoValidPlatform_throwsIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> DesktopGamePlatformHelper.getTypeFromArgs(new String[]{"dos"}, WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new));
	}

}
