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
	 * @param bundleFileHandles   An ordered list of the bundles to be checked, first to last ("base" should be the final entry)
	 * @param supportedLocales    The Locales which the game supports
	 * @param usesLanguageFolders whether or not to look for each file within a language folder, e.g. "i18n/fr/base.properties"
	 */
	public BundleManager(List<FileHandle> bundleFileHandles, List<Locale> supportedLocales, boolean usesLanguageFolders) {
		allLocaleBundles = makeBundles(bundleFileHandles, supportedLocales, usesLanguageFolders);
		displayNames = findDisplayNames();

		if (Gdx.app != null) {
			missingKeyOutputReporter = key -> Gdx.app.error("TextSupplier", "Key " + key + " really isn't found anywhere!");
		} else {
			missingKeyOutputReporter = Consumers.nullConsumer(); // otherwise tests crash...
		}

		locale = Locale.ROOT;
	}

	private Map<Locale, List<I18NBundle>> makeBundles(List<FileHandle> bundleFileHandles, List<Locale> supportedLocales, boolean usesLanguageFolders) {
		Map<Locale, List<I18NBundle>> bundles = new HashMap<>();
		for (Locale current : supportedLocales) {
			List<I18NBundle> localeBundleList = new ArrayList<>();
			for (FileHandle file : bundleFileHandles) {
				FileHandle toLoad = file;
				if (usesLanguageFolders) {
					toLoad = getFileHandleInLanguageFolder(file, current);
				}
				localeBundleList.add(I18NBundle.createBundle(toLoad, current));
			}
			bundles.put(current, localeBundleList);
		}
		return bundles;
	}

	private FileHandle getFileHandleInLanguageFolder(FileHandle originalFileHandle, Locale currentLocale) {
		String localeFolder = TextSupplier.getFormattedLocaleForSave(currentLocale);
		if (currentLocale.equals(Locale.ROOT)) {
			localeFolder = "en";
		}
		// Sonar warns against using the hardcoded "/" since "path literals are not always portable across operating systems", but I'm pretty sure Gdx.files.internal makes this fine
		String folderFilePath = originalFileHandle.path().replace(originalFileHandle.name(), localeFolder + "/" + originalFileHandle.name());
		return Gdx.files.internal(folderFilePath);
	}

	private BiMap<String, Locale> findDisplayNames() {
		BiMap<String, Locale> map = new BiMap<>();
		allLocaleBundles.forEach((loc, bundleList) -> {
			String displayname = bundleList.getLast().format("language_display_name");
			map.putPair(displayname, loc);
		});
		return map;
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
