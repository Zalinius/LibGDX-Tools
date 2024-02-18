package com.darzalgames.libgdxtools.internationalization;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;

public abstract class TextSupplier {

	private static BundleManager bundleManager;
	
	private TextSupplier() {}
	
	public static void initialize(BundleManager bundleManager) {
		TextSupplier.bundleManager = bundleManager;
	}

	/**
	 * This will first check the more transitive top bundle (e.g. a scenario in Quest Giver), then the base bundle (text used all the time in the game, such as menus).
	 * If there is no bundle, the supplied key is returned unchanged.
	 * @param key The localization key, must be an exact match to a key in the bundle file
	 * @param args Any optional arguments to supply to the localized sentence, e.g. character names, a number for pluralization, etc
	 * @return The localized line of text
	 */
	public static String getLine(String key, Object... args) {
		return bundleManager.getLine(key, args);
	}

	/**
	 * @return All supported languages, written in their own locales
	 */
	public static Set<String> getAllDisplayNames() {
		return bundleManager.displayNames.getFirstKeySet();
	}
	/**
	 * @return The current language's display name, in the current locale
	 */
	public static String getCurrentLanguageDisplayName() {
		return bundleManager.displayNames.getFirstValue(bundleManager.locale);
	}

	/**
	 * @return A Consumer that dictates how this system responds to a change in the game language
	 */
	public static Consumer<String> getLanguageChoiceResponder() {
		return selectedNewLanguage -> {
			TextSupplier.useLanguageFromDisplayName(selectedNewLanguage);
			GameInfo.getSaveManager().save();
		}; 
	}
	
	/** 
	 * ONLY TO BE USED BY THE {@link DesktopSaveManager}
	 * @return The language string for the current locale, this string ain't pretty (e.g. since English is the default bundle, it returns "", French is "fr")
	 */
	public static String getLocaleForSaveManager() {
		return bundleManager.locale.getLanguage();
	}
	
	/** 
	 * Only to be used when loading a save, otherwise use TextSupplier.getLanguageChoiceResponder()
	 * @param languageCode
	 */
	public static void useLanguage(String languageCode) {
		List<Locale> match = bundleManager.displayNames.getSecondKeyset().stream().filter(loc -> loc.getLanguage().equalsIgnoreCase(languageCode)).collect(Collectors.toList());
		if (!match.isEmpty()) {
			bundleManager.locale = match.get(0);
		} else {
			bundleManager.locale = Locale.ROOT;
		}

		bundleManager.useLocale();
	}

	/**
	 * The "top bundle" to be used: this is a temporary bundle meant to be changed as you go to different parts of a game (e.g. different scenarios in Quest Giver)
	 * @param fileHandle The file handle from Assets
	 */
	public static void useTopBundle(FileHandle fileHandle) {
		bundleManager.topBundle = I18NBundle.createBundle(fileHandle, bundleManager.locale);
	}

	/**
	 * Set whether or not to throw exceptions if a key is missing (generally yes during validation, and no during actual gameplay)
	 * @param throwExceptions 
	 */
	public static void setThrowExceptions(boolean throwExceptions) {
		bundleManager.throwExceptions = throwExceptions;
	}

	/** 
	 * Only to be used during validation, otherwise use TextSupplier.getLanguageChoiceResponder()
	 * @param languageDisplayName
	 */
	public static void useLanguageFromDisplayName(String languageDisplayName) {
		bundleManager.locale = bundleManager.displayNames.getSecondValue(languageDisplayName);

		bundleManager.useLocale();
	}
}
