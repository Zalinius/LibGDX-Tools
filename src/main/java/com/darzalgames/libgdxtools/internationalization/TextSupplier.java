package com.darzalgames.libgdxtools.internationalization;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
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
	 * @return All supported languages in alphabetical order, written in their own locales
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
	 * @return The language string for the current locale, this string ain't pretty (e.g. since English is the default bundle, it returns "", French is "fr")
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
	 * @param languageCode
	 */
	public static void useLanguage(String languageCode) {
		List<Locale> match = bundleManager.displayNames.getSecondKeyset().stream().filter(loc -> getFormattedLocaleForSave(loc).equalsIgnoreCase(languageCode)).toList();
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


	public static SelectBoxContentManager getContentManager() {
		return new SelectBoxContentManager() {
			@Override
			public Supplier<String> getCurrentSelectedDisplayName() {
				return () -> bundleManager.displayNames.getFirstValue(bundleManager.locale);
			}

			@Override
			public String getBoxLabelKey() {
				return "language_label";
			}

			@Override
			public List<SelectBoxButtonInfo> getOptionButtons() {
				return bundleManager.displayNames.getSecondKeyset().stream().map(locale ->
				new SelectBoxButtonInfo(
						() -> bundleManager.displayNames.getFirstValue(locale),
						() -> {
							bundleManager.locale = locale;
							bundleManager.useLocale();
							GameInfo.getSaveManager().save();
						})
						).toList();
			}
		};
	}
}
