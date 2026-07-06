package com.darzalgames.libgdxtools.preferences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InMemoryPreferenceManagerTest {

	@Test
	void savePrefValue_getBooleanPrefValue_storedAndReturnedTheValueSuccessfully() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("Boolean", true);

		boolean value = prefs.getBooleanPrefValue("Boolean");
		assertTrue(value);
	}

	@Test
	void savePrefValue_getIntegerPrefValue_storedAndReturnedTheValueSuccessfully() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("Integer", 4);

		int value = prefs.getIntegerPrefValue("Integer");
		assertEquals(4, value);
	}

	@Test
	void savePrefValue_getLongPrefValue_storedAndReturnedTheValueSuccessfully() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("Long", 60L);

		long value = prefs.getLongPrefValue("Long");
		assertEquals(60L, value);
	}

	@Test
	void savePrefValue_getFloatPrefValue_storedAndReturnedTheValueSuccessfully() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("Float", 3.2f);

		float value = prefs.getFloatPrefValue("Float");
		assertEquals(3.2f, value);
	}

	@Test
	void savePrefValue_getStringPrefValue_storedAndReturnedTheValueSuccessfully() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("String", "test");

		String value = prefs.getStringPrefValue("String");
		assertEquals("test", value);
	}

//	 ---------------

	@Test
	void getBoolean_withDefaultValueAndNoStoredEntry_returnsDefault() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();

		boolean value = prefs.getBooleanPrefValue("Boolean", true);
		assertTrue(value);
	}

	@Test
	void getInteger_withDefaultValueAndNoStoredEntry_returnsDefault() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();

		int value = prefs.getIntegerPrefValue("Integer", 12);
		assertEquals(12, value);
	}

	@Test
	void getLong_withDefaultValueAndNoStoredEntry_returnsDefault() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();

		long value = prefs.getLongPrefValue("Long", 123L);
		assertEquals(123L, value);
	}

	@Test
	void getFloat_withDefaultValueAndNoStoredEntry_returnsDefault() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();

		float value = prefs.getFloatPrefValue("Float", 1.2f);
		assertEquals(1.2f, value);
	}

	@Test
	void getString_withDefaultValueAndNoStoredEntry_returnsDefault() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();

		String value = prefs.getStringPrefValue("String", "test");
		assertEquals("test", value);
	}

//	 ---------------

	@Test
	void getBoolean_withDefaultValueAndStoredEntry_returnsStoredEntry() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("Boolean", false);

		boolean value = prefs.getBooleanPrefValue("Boolean", true);
		assertFalse(value);
	}

	@Test
	void getInteger_withDefaultValueAndStoredEntry_returnsStoredEntry() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("Integer", 6);

		int value = prefs.getIntegerPrefValue("Integer", 12);
		assertEquals(6, value);
	}

	@Test
	void getLong_withDefaultValueAndStoredEntry_returnsStoredEntry() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("Long", 17L);

		long value = prefs.getLongPrefValue("Long", 123L);
		assertEquals(17L, value);
	}

	@Test
	void getFloat_withDefaultValueAndStoredEntry_returnsStoredEntry() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("Float", 41.7f);

		float value = prefs.getFloatPrefValue("Float", 1.2f);
		assertEquals(41.7f, value);
	}

	@Test
	void getString_withDefaultValueAndStoredEntry_returnsStoredEntry() {
		InMemoryPreferenceManager prefs = new InMemoryPreferenceManager();
		prefs.savePrefValue("String", "stored");

		String value = prefs.getStringPrefValue("String", "test");
		assertEquals("stored", value);
	}

}
