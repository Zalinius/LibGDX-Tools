package com.darzalgames.libgdxtools.preferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Preferences;

public class InMemoryPreferenceManager extends PreferenceManager {

	public InMemoryPreferenceManager() {
		super(new PreferencesMap());
	}

	private static class PreferencesMap implements Preferences {

		private final Map<String, Boolean> booleans;
		private final Map<String, Integer> integers;
		private final Map<String, Long> longs;
		private final Map<String, Float> floats;
		private final Map<String, String> strings;

		public PreferencesMap() {
			booleans = new HashMap<>();
			integers = new HashMap<>();
			longs = new HashMap<>();
			strings = new HashMap<>();
			floats = new HashMap<>();
		}

		@Override
		public Preferences putBoolean(String key, boolean val) {
			booleans.put(key, val);
			return this;
		}

		@Override
		public Preferences putInteger(String key, int val) {
			integers.put(key, val);
			return this;
		}

		@Override
		public Preferences putLong(String key, long val) {
			longs.put(key, val);
			return this;
		}

		@Override
		public Preferences putFloat(String key, float val) {
			floats.put(key, val);
			return this;
		}

		@Override
		public Preferences putString(String key, String val) {
			strings.put(key, val);
			return this;
		}

		@Override
		public Preferences put(Map<String, ?> vals) {
			// copied from Lwjgl3Preferences
			for (Entry<String, ?> val : vals.entrySet()) {
				if (val.getValue() instanceof Boolean booleanValue) {
					putBoolean(val.getKey(), booleanValue);
				}
				if (val.getValue() instanceof Integer integerValue) {
					putInteger(val.getKey(), integerValue);
				}
				if (val.getValue() instanceof Long longValue) {
					putLong(val.getKey(), longValue);
				}
				if (val.getValue() instanceof String stringValue) {
					putString(val.getKey(), stringValue);
				}
				if (val.getValue() instanceof Float floatValue) {
					putFloat(val.getKey(), floatValue);
				}
			}
			return this;
		}

		@Override
		public boolean getBoolean(String key) {
			return booleans.get(key);
		}

		@Override
		public int getInteger(String key) {
			return integers.get(key);
		}

		@Override
		public long getLong(String key) {
			return longs.get(key);
		}

		@Override
		public float getFloat(String key) {
			return floats.get(key);
		}

		@Override
		public String getString(String key) {
			return strings.get(key);
		}

		@Override
		public boolean getBoolean(String key, boolean defValue) {
			return booleans.getOrDefault(key, defValue);
		}

		@Override
		public int getInteger(String key, int defValue) {
			return integers.getOrDefault(key, defValue);
		}

		@Override
		public long getLong(String key, long defValue) {
			return longs.getOrDefault(key, defValue);
		}

		@Override
		public float getFloat(String key, float defValue) {
			return floats.getOrDefault(key, defValue);
		}

		@Override
		public String getString(String key, String defValue) {
			return strings.getOrDefault(key, defValue);
		}

		@Override
		public Map<String, ?> get() {
			Map<String, Object> map = new HashMap<>();
			booleans.forEach(map::put);
			integers.forEach(map::put);
			longs.forEach(map::put);
			floats.forEach(map::put);
			strings.forEach(map::put);
			return map;
		}

		@Override
		public boolean contains(String key) {
			return booleans.containsKey(key)
					|| integers.containsKey(key)
					|| longs.containsKey(key)
					|| floats.containsKey(key)
					|| strings.containsKey(key);
		}

		@Override
		public void clear() {
			booleans.clear();
			integers.clear();
			longs.clear();
			floats.clear();
			strings.clear();
		}

		@Override
		public void remove(String key) {
			booleans.remove(key);
			integers.remove(key);
			longs.remove(key);
			floats.remove(key);
			strings.remove(key);
		}

		@Override
		public void flush() {
			// not applicable to in-memory prefs
		}

	}

}
