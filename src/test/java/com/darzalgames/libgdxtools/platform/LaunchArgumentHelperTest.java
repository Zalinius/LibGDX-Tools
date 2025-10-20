package com.darzalgames.libgdxtools.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.errorhandling.DummyCrashHandler;
import com.darzalgames.libgdxtools.errorhandling.desktop.DesktopCrashHandler;

class LaunchArgumentHelperTest {

	@Test
	void getCrashHandler_withoutOptions_returnsDesktopCrashHandler()  {
		assertEquals(DesktopCrashHandler.class, LaunchArgumentHelper.getCrashHandler(List.of()).getClass());
	}

	@Test
	void getCrashHandler_withNoCrashReportingOption_returnsDummyCrashHandler()  {
		assertEquals(DummyCrashHandler.class, LaunchArgumentHelper.getCrashHandler(List.of("--no-crash-reporting")).getClass());
	}

	@Test
	void getGamePlatform_withWindowsInArguments_returnsWindows()  {
		GamePlatform result = LaunchArgumentHelper.getGamePlatform(List.of("windows"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);

		assertEquals(WindowsGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withWindowsAmongOtherArguments_returnsWindows()  {
		GamePlatform result = LaunchArgumentHelper.getGamePlatform(List.of("skip-menu", "windows", "garbage"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);

		assertEquals(WindowsGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withLinux_returnsLinux()  {
		GamePlatform result = LaunchArgumentHelper.getGamePlatform(List.of("linux"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);

		assertEquals(LinuxGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withMac_returnsMac()  {
		GamePlatform result = LaunchArgumentHelper.getGamePlatform(List.of("mac"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new);

		assertEquals(MacGamePlatform.class, result.getClass());
	}

	@Test
	void getGamePlatform_withNoValidPlatform_throwsIllegalArgumentException()  {
		assertThrows(IllegalArgumentException.class, () -> LaunchArgumentHelper.getGamePlatform(List.of("dos"), WindowsGamePlatform::new, LinuxGamePlatform::new, MacGamePlatform::new));
	}

}
