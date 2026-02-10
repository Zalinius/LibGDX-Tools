package com.darzalgames.libgdxtools.platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.errorhandling.DummyCrashHandler;
import com.darzalgames.libgdxtools.errorhandling.desktop.DesktopCrashHandler;
import com.darzalgames.libgdxtools.os.*;

class LaunchArgumentHelperTest {

	@Test
	void getCrashHandler_withoutOptions_returnsDesktopCrashHandler() {
		assertEquals(DesktopCrashHandler.class, LaunchArgumentHelper.getCrashHandler(List.of()).getClass());
	}

	@Test
	void getCrashHandler_withNoCrashReportingOption_returnsDummyCrashHandler() {
		assertEquals(DummyCrashHandler.class, LaunchArgumentHelper.getCrashHandler(List.of("--no-crash-reporting")).getClass());
	}

	@Test
	void getGameOperatingSystem_withWindowsInArguments_returnsWindows() {
		GameOperatingSystem result = LaunchArgumentHelper.getGameOperatingSystem(List.of("windows"), WindowsGameOperatingSystem::new, LinuxGameOperatingSystem::new, MacGameOperatingSystem::new);

		assertEquals(WindowsGameOperatingSystem.class, result.getClass());
	}

	@Test
	void getGameOperatingSystem_withWindowsAmongOtherArguments_returnsWindows() {
		GameOperatingSystem result = LaunchArgumentHelper.getGameOperatingSystem(List.of("skip-menu", "windows", "garbage"), WindowsGameOperatingSystem::new, LinuxGameOperatingSystem::new, MacGameOperatingSystem::new);

		assertEquals(WindowsGameOperatingSystem.class, result.getClass());
	}

	@Test
	void getGameOperatingSystem_withLinux_returnsLinux() {
		GameOperatingSystem result = LaunchArgumentHelper.getGameOperatingSystem(List.of("linux"), WindowsGameOperatingSystem::new, LinuxGameOperatingSystem::new, MacGameOperatingSystem::new);

		assertEquals(LinuxGameOperatingSystem.class, result.getClass());
	}

	@Test
	void getGameOperatingSystem_withMac_returnsMac() {
		GameOperatingSystem result = LaunchArgumentHelper.getGameOperatingSystem(List.of("mac"), WindowsGameOperatingSystem::new, LinuxGameOperatingSystem::new, MacGameOperatingSystem::new);

		assertEquals(MacGameOperatingSystem.class, result.getClass());
	}

	@Test
	void getGameOperatingSystem_withNoValidPlatform_throwsIllegalArgumentException() {
		List<String> args = List.of("dos");

		assertThrows(IllegalArgumentException.class, () -> LaunchArgumentHelper.getGameOperatingSystem(args, WindowsGameOperatingSystem::new, LinuxGameOperatingSystem::new, MacGameOperatingSystem::new));
	}

}
