package com.darzalgames.libgdxtools.os;

import java.util.List;
import java.util.function.Supplier;

import com.darzalgames.libgdxtools.errorhandling.CrashHandler;
import com.darzalgames.libgdxtools.errorhandling.DummyCrashHandler;
import com.darzalgames.libgdxtools.errorhandling.desktop.DesktopCrashHandler;

/**
 * Some helper functions for managing different desktop OSes
 */
public class LaunchArgumentHelper {

	private static final String OPTION_NO_CRASH_REPORTING = "--no-crash-reporting";

	private LaunchArgumentHelper() {}

	/**
	 * Determines the correct Crash Handler based on launch options
	 * @param launchArgs java main arguments. If it constains --no-crash-reporting, it will return a dummy handler
	 * @return The corresponding CrashHandler
	 */
	public static CrashHandler getCrashHandler(List<String> launchArgs) {
		if (launchArgs.contains(OPTION_NO_CRASH_REPORTING)) {
			return new DummyCrashHandler();
		} else {
			return new DesktopCrashHandler();
		}
	}

	/**
	 * Determines the correct OS based on launch arguments
	 * @param argsList java main arguments
	 * @return The corresponding OS
	 */
	public static GameOperatingSystem getGameOperatingSystem(
			List<String> argsList,
			Supplier<GameOperatingSystem> makeWindowsOS,
			Supplier<GameOperatingSystem> makeLinuxOS,
			Supplier<GameOperatingSystem> makeMacOS) {
		if (listContainsIgnoreCase(argsList, GameOperatingSystem.WINDOWS)) {
			return makeWindowsOS.get();
		} else if (listContainsIgnoreCase(argsList, GameOperatingSystem.LINUX)) {
			return makeLinuxOS.get();
		} else if (listContainsIgnoreCase(argsList, GameOperatingSystem.MAC)) {
			return makeMacOS.get();
		} else {
			throw new IllegalArgumentException("Args :" + argsList.toString() + " does not contain a valid Game Operating System");
		}
	}

	private static boolean listContainsIgnoreCase(List<String> argsList, String toCheckFor) {
		return argsList.stream().anyMatch(argument -> argument.equalsIgnoreCase(toCheckFor));
	}

}
