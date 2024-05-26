package com.darzalgames.libgdxtools.platform;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class DesktopGamePlatformHelper {

	/**
	 * Determines the correct platform based on launch arguments
	 * @param args java main arguments
	 * @return The corresponding platform
	 */
	public static GamePlatform getTypeFromArgs(String[] args) {
		List<String> argsList = Arrays.asList(args);
		
		if(argsList.contains("windows")) {
			return new WindowsGamePlatform();
		}
		else if(argsList.contains("linux")) {
			return new LinuxGamePlatform();
		}
		else {
			throw new IllegalArgumentException("Args :" + Arrays.toString(args) + " does not contain a valid Game Platform");
		}
	}

}
