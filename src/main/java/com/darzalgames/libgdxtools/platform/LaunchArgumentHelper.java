package com.darzalgames.libgdxtools.platform;

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
	 * Determines the correct platform based on launch arguments
	 * @param argsList java main arguments
	 * @return The corresponding platform
	 */
	public static GamePlatform getGamePlatform(List<String> argsList, Supplier<GamePlatform> makeWindowsPlatform, Supplier<GamePlatform> makeLinuxPlatform, Supplier<GamePlatform> makeMacPlatform) {

		if (listContainsIgnoreCase(argsList, GamePlatform.WINDOWS)) {
			return makeWindowsPlatform.get();
		} else if (listContainsIgnoreCase(argsList, GamePlatform.LINUX)) {
			return makeLinuxPlatform.get();
		} else if (listContainsIgnoreCase(argsList, GamePlatform.MAC)) {
			return makeMacPlatform.get();
		} else {
			throw new IllegalArgumentException("Args :" + argsList.toString() + " does not contain a valid Game Platform");
		}
	}

	private static boolean listContainsIgnoreCase(List<String> argsList, String toCheckFor) {
		return argsList.stream().anyMatch(argument -> argument.equalsIgnoreCase(toCheckFor));
	}

}
