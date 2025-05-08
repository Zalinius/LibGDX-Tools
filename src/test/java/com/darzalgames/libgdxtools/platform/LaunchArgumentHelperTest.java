package com.darzalgames.libgdxtools.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.errorhandling.DummyCrashHandler;
import com.darzalgames.libgdxtools.errorhandling.desktop.DesktopCrashHandler;

class LaunchArgumentHelperTest {

	@Test
	void getCrashHandler_withoutOptions_returnsDesktopCrashHandler() throws Exception {
		assertEquals(DesktopCrashHandler.class, LaunchArgumentHelper.getCrashHandler(List.of()).getClass());
	}

	@Test
	void getCrashHandler_withNoCrashReportingOption_returnsDummyCrashHandler() throws Exception {
		assertEquals(DummyCrashHandler.class, LaunchArgumentHelper.getCrashHandler(List.of("--no-crash-reporting")).getClass());
	}

	@Test
	void getGamePlatform_withWindowsInArguments_returnsWindows() throws Exception {
		GamePlatform result = LaunchArgumentHelper.getGamePlatform(List.of("windows"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);

		assertEquals(WindowsGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withWindowsAmongOtherArguments_returnsWindows() throws Exception {
		GamePlatform result = LaunchArgumentHelper.getGamePlatform(List.of("skip-menu", "windows", "garbage"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);

		assertEquals(WindowsGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withLinux_returnsLinux() throws Exception {
		GamePlatform result = LaunchArgumentHelper.getGamePlatform(List.of("linux"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);

		assertEquals(LinuxGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withMac_returnsMac() throws Exception {
		GamePlatform result = LaunchArgumentHelper.getGamePlatform(List.of("mac"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);

		assertEquals(MacGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withNoValidPlatform_throwsIllegalArgumentException() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> LaunchArgumentHelper.getGamePlatform(List.of("dos"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new));
	}

}
