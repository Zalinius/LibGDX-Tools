package com.darzalgames.libgdxtools.errorhandling.desktop;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.errorhandling.desktop.DesktopCrashHandler;

public class DesktopCrashHandlerTest {
	
	@Test
	void isHttpCodeSuccess() throws Exception {
		assertTrue(DesktopCrashHandler.isHttpCodeSuccess(200));
		assertTrue(DesktopCrashHandler.isHttpCodeSuccess(204));
		
		assertFalse(DesktopCrashHandler.isHttpCodeSuccess(0));
		assertFalse(DesktopCrashHandler.isHttpCodeSuccess(100));
		assertFalse(DesktopCrashHandler.isHttpCodeSuccess(300));
		assertFalse(DesktopCrashHandler.isHttpCodeSuccess(400));
		assertFalse(DesktopCrashHandler.isHttpCodeSuccess(404));
		assertFalse(DesktopCrashHandler.isHttpCodeSuccess(500));
	}

}
