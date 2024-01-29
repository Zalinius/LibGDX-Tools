package com.darzalgames.libgdxtools.save;

import java.util.Locale;

public enum OSType { 
	WINDOWS,
	LINUX,
	;
	
	// thanks https://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
	/**
	 * Determine the operating system from the os.name System property. Can be used for the very occasional OS-specific
	 * features such as determining the save file location, or graphics options (e.g. disabling the broken borderless windowed on Linux)
	 * @return the current operating system
	 */
	public static OSType getOperatingSystemType() {
		OSType detectedOS = OSType.WINDOWS;
		String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
			// This is Mac, which we don't support
		} else if (os.indexOf("win") >= 0) {
			// This was our default assumption
		} else if (os.indexOf("nux") >= 0) {
			detectedOS = OSType.LINUX;
		}
		return detectedOS;
	}
}