package com.darzalgames.libgdxtools.platform;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class WebGamePlatformTest {
	
	@Test
	void isDevMode_false() throws Exception {
		GamePlatform webGamePlatform = new WebGamePlatform();
		
		assertFalse(webGamePlatform.isDevMode());
	}

}
