package com.darzalgames.libgdxtools.i18n;

import java.util.*;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.darzalgames.darzalcommon.data.BiMap;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.save.SaveManager;

public abstract class TextSupplier {

	private static I18NBundle baseBundle;
	private static I18NBundle topBundle;
	protected static Locale locale;

	private static BiMap<String, Locale> displayNames;
	
	private static boolean throwExceptions;
	
	private static TextSupplier instance;
	protected abstract FileHandle getBaseBundleFileHandle();
	protected abstract ArrayList<Locale> getSupportedLocales();


	protected TextSupplier() {}
	
	protected static void initialize(TextSupplier instance) {
		TextSupplier.instance = instance;
		
		displayNames = new BiMap<>();
		for (Locale current : instance.getSupportedLocales()) {
			I18NBundle tempBundle = I18NBundle.createBundle(instance.getBaseBundleFileHandle(), current);
			String displayname = tempBundle.format("language_display_name");
			displayNames.addPair(displayname, current);
		}
	}

	/**
	 * @return The current language's display name, in the current locale
	 */
	public static String getCurrentLanguageDisplayName() {
		return displayNames.getFirstValue(locale);
	}

	/**
	 * @return All supported languages, written in their own locales
	 */
	public static Set<String> getAllDisplayNames() {
		return displayNames.getFirstKeySet();
	}

	/**
	 * This will first check the more transitive top bundle (e.g. a scenario in Quest Giver), then the base bundle (text used all the time in the game, such as menus)
	 * @param key The localization key, must be an exact match to a key in the bundle file
	 * @param args Any optional arguments to supply to the localized sentence, e.g. character names, a number for pluralization, etc
	 * @return The localized line of text
	 */
	public static String getLine(String key, Object... args) {
		try { 
			if (topBundle != null) {
				return topBundle.format(key, args).toUpperCase();
			}
		} catch (MissingResourceException e) {
			// This will catch any keys that belong in the baseBundle (text common to all adventures)
		}
		
		try {
			return baseBundle.format(key, args).toUpperCase();
		} catch (MissingResourceException e) {
			Gdx.app.error("TextSupplier", "Key " + key + " really isn't found anywhere!");
			if (throwExceptions) {
				throw e;
			} else {
				return baseBundle.format("missing_text", key);
			}
		}
	}

	/** 
	 * Only to be used during validation, otherwise use TextSupplier.getLanguageChoiceResponder()
	 * @param languageDisplayName
	 */
	public static void useLanguageFromDisplayName(String languageDisplayName) {
		locale = displayNames.getSecondValue(languageDisplayName);

		if(locale == null) {
			locale = Locale.ROOT;
		}
		useLocale(locale);
	}
	
	/** 
	 * Only to be used when loading a save, otherwise use TextSupplier.getLanguageChoiceResponder()
	 * @param languageCode
	 */
	public static void useLanguage(String languageCode) {
		List<Locale> match = displayNames.getSecondKeyset().stream().filter(loc -> loc.getLanguage().equalsIgnoreCase(languageCode)).toList();
		if (!match.isEmpty()) {
			locale = match.get(0);
		} else {
			locale = Locale.ROOT;
		}

		useLocale(locale);
	}

	private static void useLocale(Locale useLocale) {
		if(useLocale == null) {
			useLocale = Locale.ROOT;
		}
		locale = useLocale;
		baseBundle = I18NBundle.createBundle(instance.getBaseBundleFileHandle(), locale);
	}

	/**
	 * The "top bundle" to be used, this is a temporary bundle meant to be changed as you go to different parts of a game (e.g. a scenario in Quest Giver)
	 * @param fileHandle The file handle from Assets
	 */
	public static void useTopBundle(FileHandle fileHandle) {
		topBundle = I18NBundle.createBundle(fileHandle, locale);
	}

	/**
	 * Set whether or not to throw exceptions if a key is missing (generally yes during validation, and no during actual gameplay)
	 * @param throwExceptions 
	 */
	public static void setThrowExceptions(boolean throwExceptions) {
		TextSupplier.throwExceptions = throwExceptions;
	}
	
	/** 
	 * ONLY TO BE USED BY THE {@link SaveManager}
	 * @return
	 */
	public static Locale getLocale() {
		return locale;
	}

	/**
	 * @return A Consumer that dictates how this system responds to a change in game language
	 */
	public static Consumer<String> getLanguageChoiceResponder() {
		return selectedNewLanguage -> {
			useLanguageFromDisplayName(selectedNewLanguage);
			GameInfo.getSaveManager().save();
		}; 
	}
	
	/**
	 * To be used for localized images (until I think of a better solution)
	 */
	public static boolean isInEnglish() {
		return getLocale().equals(Locale.ROOT);
	}
}
