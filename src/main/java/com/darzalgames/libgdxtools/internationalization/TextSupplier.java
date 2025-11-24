package com.darzalgames.libgdxtools.internationalization;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.save.DesktopSaveManager;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.SelectBoxContentManager;

public abstract class TextSupplier {

	private static BundleManager bundleManager;

	private TextSupplier() {}

	public static void initialize(BundleManager bundleManager) {
		TextSupplier.bundleManager = bundleManager;
	}

	/**
	 * This will check each bundle in the order they were provided, returning the localized line if the key is found in a bundle.
	 * If there is no bundle, the supplied key is returned unchanged.
	 * @param key  The localization key, must be an exact match to a key in the bundle file
	 * @param args Any optional arguments to supply to the localized sentence, e.g. character names, a number for pluralization, etc
	 * @return The localized line of text
	 */
	public static String getLine(String key, Object... args) {
		return bundleManager.getLine(key, args);
	}

	/**
	 * @return All supported locales in alphabetical order, written in their own locales
	 */
	public static List<Supplier<String>> getAllDisplayNames() {
		Set<String> namesUnsorted = bundleManager.displayNames.getFirstKeySet();
		List<String> names = new ArrayList<>(namesUnsorted);
		Collections.sort(names);

		Stream<Supplier<String>> allNameSupplier = names.stream().map(name -> (() -> name));
		return allNameSupplier.toList();
	}

	/**
	 * ONLY TO BE USED BY THE {@link DesktopSaveManager}
	 * @return The locale string for the current locale, this string ain't pretty (e.g. since English is the default bundle, it returns "", French is "fr")
	 */
	public static String getLocaleForSaveManager() {
		return getFormattedLocaleForSave(bundleManager.locale);
	}

	public static String getFormattedLocaleForSave(Locale locale) {
		String base = locale.getLanguage();
		String optionalCountry = locale.getCountry();
		if (!optionalCountry.isBlank()) {
			base += "_" + optionalCountry;
		}
		return base;
	}

	/**
	 * Only to be used when loading a save, otherwise use the SelectBoxContentManager's getChoiceResponder()
	 * @param localeCode the locale code to switch to
	 */
	public static void uselocale(String localeCode) {
		List<Locale> match = bundleManager.displayNames.getSecondKeyset().stream().filter(loc -> getFormattedLocaleForSave(loc).equalsIgnoreCase(localeCode)).toList();
		if (!match.isEmpty()) {
			bundleManager.locale = match.get(0);
		} else {
			bundleManager.locale = Locale.ROOT;
		}

		bundleManager.useLocale();
	}

	/**
	 * Set whether or not to throw exceptions if a key is missing (generally yes during validation, and no during actual gameplay)
	 * @param shouldThrowExceptions Whether exceptions should be thrown
	 */
	public static void setThrowExceptions(boolean shouldThrowExceptions) {
		bundleManager.throwExceptions = shouldThrowExceptions;
	}

	public static SelectBoxContentManager getContentManager() {
		return new SelectBoxContentManager() {
			@Override
			public Supplier<String> getCurrentSelectedDisplayName() {
				return () -> bundleManager.displayNames.getFirstValue(bundleManager.locale);
			}

			@Override
			public String getBoxLabelKey() {
				return "locale_label";
			}

			@Override
			public List<SelectBoxButtonInfo> getOptionButtons() {
				return bundleManager.displayNames.getSecondKeyset().stream().map(
						locale -> new SelectBoxButtonInfo(
								() -> bundleManager.displayNames.getFirstValue(locale),
								() -> {
									bundleManager.locale = locale;
									bundleManager.useLocale();
									GameInfo.getSaveManager().save();
								}
						)
				).toList();
			}
		};
	}
}
