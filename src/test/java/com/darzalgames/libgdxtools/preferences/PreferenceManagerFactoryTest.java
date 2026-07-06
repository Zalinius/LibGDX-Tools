package com.darzalgames.libgdxtools.preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PreferenceManagerFactoryTest {

	@Test
	void create_noFileAccessBecauseThisIsATestEnvironment_makesInMemoryPreferenceManager() {
		PreferenceManager preferenceManager = PreferenceManagerFactory.create("com.testo");

		assertEquals(InMemoryPreferenceManager.class, preferenceManager.getClass());
	}

}
