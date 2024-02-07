package com.darzalgames.libgdxtools.save;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.github.glytching.junit.extension.system.SystemProperty;

class OSTypeTest {

	@Test
	@SystemProperty(name = "os.name", value = "Windows")
	void getOperatingSystemType_withWindows_returnsWindows() throws Exception {
		OSType result = OSType.getOperatingSystemType();
		
		assertEquals(OSType.WINDOWS, result);
	}
	
	@Test
	@SystemProperty(name = "os.name", value = "Linux")
	void getOperatingSystemType_withLinux_returnsLinux() throws Exception {
		OSType result = OSType.getOperatingSystemType();
		
		assertEquals(OSType.LINUX, result);
	}
	
}
