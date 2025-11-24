package com.darzalgames.libgdxtools.internationalization;

import java.util.*;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.darzalgames.darzalcommon.data.BiMap;
import com.darzalgames.darzalcommon.functional.Consumers;

public class BundleManager {

	private List<I18NBundle> currentBundle;
	private final Map<Locale, List<I18NBundle>> allLocaleBundles;

	protected boolean throwExceptions;
	protected Locale locale;
	protected final BiMap<String, Locale> displayNames;

	private Consumer<String> missingKeyOutputReporter;

	/**
	 * @param baseBundleFileHandle The ONLY bundle in the game, e.g. "base" or "GameName"
	 * @param supportedLocales     The Locales which the game supports
	 */
	public BundleManager(FileHandle baseBundleFileHandle, List<Locale> supportedLocales) {
		this(List.of(baseBundleFileHandle), supportedLocales);
	}

	/**
	 * @param bundleFileHandles An ordered list of the bundles to be checked, first to last ("base" should be the final entry)
	 * @param supportedLocales  The Locales which the game supports
	 */
	public BundleManager(List<FileHandle> bundleFileHandles, List<Locale> supportedLocales) {
		displayNames = new BiMap<>();
		allLocaleBundles = new HashMap<>();
		for (Locale current : supportedLocales) {
			I18NBundle tempBundle = I18NBundle.createBundle(bundleFileHandles.getLast(), current);
			String displayname = tempBundle.format("language_display_name");
			displayNames.putPair(displayname, current);

			List<I18NBundle> localeBundleList = new ArrayList<>();
			for (FileHandle file : bundleFileHandles) {
				localeBundleList.add(I18NBundle.createBundle(file, current));
			}
			allLocaleBundles.put(current, localeBundleList);
		}

		missingKeyOutputReporter = Consumers.nullConsumer(); // otherwise tests crash...
		if (Gdx.app != null) {
			missingKeyOutputReporter = key -> Gdx.app.error("TextSupplier", "Key " + key + " really isn't found anywhere!");
		}

		locale = Locale.ROOT;
	}

	String getLine(String key, Object... args) {
		if (currentBundle == null) {
			// A game that doesn't (yet?) have any bundles just returns the key
			return key;
		}

		if (key.isBlank()) {
			// Makes NullSupplier UI elements work
			return "";
		}

		for (Iterator<I18NBundle> iterator = currentBundle.iterator(); iterator.hasNext();) {
			I18NBundle bundle = iterator.next();
			try {
				return bundle.format(key, args);
			} catch (MissingResourceException e) {
				// This will catch any keys that are in a later file, or any undefined keys
				boolean isOnFinalBundle = !iterator.hasNext();
				if (isOnFinalBundle) {
					missingKeyOutputReporter.accept(key);
					if (throwExceptions) {
						throw e;
					} else {
						return bundle.format("missing_text", key);
					}
				}
			}
		}

		// should never reach this point...
		throw new IllegalStateException("Somehow " + key + " slipped through the i18n system...");
	}

	void useLocale() {
		currentBundle = allLocaleBundles.get(locale);
	}
}
