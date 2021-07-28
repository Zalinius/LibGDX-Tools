package com.zalinius.libgdxtools.graphics;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.graphics.Color;

public class GameBoyPalletTest {
	
	@Test
	void darkestGreen_colorValues_areCorrect() throws Exception {
		Color darkestGreenFromFloat = new Color(15/255f, 56/255f, 15/255f, 255/255f);
		
		assertEquals(darkestGreenFromFloat, GameBoyPallet.DARKEST_GREEN);
	}

}
