package com.darzalgames.libgdxtools.platform;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Some helper functions for managing different desktop OSes
 */
public class DesktopGamePlatformHelper {

	/**
	 * Determines the correct platform based on launch arguments
	 * @param args java main arguments
	 * @param makeWindowsPlatform 
	 * @param makeLinuxPlatform 
	 * @param makeMacPlatform 
	 * @return The corresponding platform
	 */
	public static GamePlatform getTypeFromArgs(String[] args, Supplier<GamePlatform> makeWindowsPlatform, Supplier<GamePlatform> makeLinuxPlatform,
			Supplier<GamePlatform> makeMacPlatform) {
		List<String> argsList = Arrays.asList(args);
		
		if(listContainsIgnoreCase(argsList, GamePlatform.WINDOWS)) {
			return makeWindowsPlatform.get();
		}
		else if(listContainsIgnoreCase(argsList, GamePlatform.LINUX)) {
			return makeLinuxPlatform.get();
		}
		else if(listContainsIgnoreCase(argsList, GamePlatform.MAC)) {
			return makeMacPlatform.get();
		}
		else {
			throw new IllegalArgumentException("Args :" + Arrays.toString(args) + " does not contain a valid Game Platform");
		}
	}
	
	private static boolean listContainsIgnoreCase(List<String> argsList, String toCheckFor) {
		return argsList.stream().anyMatch(argument -> argument.equalsIgnoreCase(toCheckFor));
	}

}
