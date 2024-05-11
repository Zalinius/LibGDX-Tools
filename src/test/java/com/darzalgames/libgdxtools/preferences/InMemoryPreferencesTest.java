package com.darzalgames.libgdxtools.preferences;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Preferences;

public class InMemoryPreferencesTest {
	
	@Test
	void floatPrefs() throws Exception {
		Preferences inMemoryPreferences = new InMemoryPreferences();
		
		inMemoryPreferences.putFloat("floatKey", 1.5f);
		
		assertEquals(1.5f, inMemoryPreferences.getFloat("floatKey"));
		assertEquals(2.5f, inMemoryPreferences.getFloat("missingKey", 2.5f));
	}

}
