package com.darzalgames.libgdxtools.i18n;

import java.util.*;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.darzalgames.darzalcommon.data.BiMap;
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
	
	public static void initialize(TextSupplier instance) {
		TextSupplier.instance = instance;
		
		displayNames = new BiMap<>();
		for (Locale current : instance.getSupportedLocales()) {
			I18NBundle tempBundle = I18NBundle.createBundle(instance.getBaseBundleFileHandle(), current);
			String displayname = tempBundle.format("language_display_name");
			displayNames.add(displayname, current);
		}
	}

	public static String getCurrentLanguageDisplayName() {
		return displayNames.getFirstValue(locale);
	}

	public static Set<String> getAllDisplayNames() {
		return displayNames.getFirstKeySet();
	}

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

	public static void useTopBundle(FileHandle fileHandle) {
		topBundle = I18NBundle.createBundle(fileHandle, locale);
	}

	public static void setThrowExceptions(boolean throwExceptions) {
		TextSupplier.throwExceptions = throwExceptions;
	}
	/** 
	 * Only to be used by the SaveManager
	 * @return
	 */
	public static Locale getLocale() {
		return locale;
	}

	public static Consumer<String> getLanguageChoiceResponder() {
		return selectedNewLanguage -> {
			useLanguageFromDisplayName(selectedNewLanguage);
			SaveManager.save();
		}; 
	}
	
	/**
	 * To be used for localized images (until I think of a better solution)
	 */
	public static boolean isInEnglish() {
		return getLocale().equals(Locale.ROOT);
	}
}
