package com.darzalgames.libgdxtools.internationalization;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.function.UnaryOperator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.darzalgames.darzalcommon.data.BiMap;

public class BundleManager {

	private I18NBundle baseBundle;
	protected I18NBundle topBundle;

	protected boolean throwExceptions;
	protected Locale locale;
	protected final BiMap<String, Locale> displayNames;

	private final FileHandle baseBundleFileHandle;
	private final UnaryOperator<String> modifierStrategy;


	public BundleManager(FileHandle baseBundleFileHandle, List<Locale> supportedLocales) {
		this(baseBundleFileHandle, supportedLocales, String::toString);
	}

	public BundleManager(FileHandle baseBundleFileHandle, List<Locale> supportedLocales, UnaryOperator<String> modifierStrategy) {
		this.baseBundleFileHandle = baseBundleFileHandle;
		this.modifierStrategy = modifierStrategy;

		displayNames = new BiMap<>();
		for (Locale current : supportedLocales) {
			I18NBundle tempBundle = I18NBundle.createBundle(baseBundleFileHandle, current);
			String displayname = tempBundle.format("language_display_name");
			displayNames.addPair(displayname, current);
		}
	}


	/**
	 * This will first check the more transitive top bundle (e.g. a scenario in Quest Giver), then the base bundle (text used all the time in the game, such as menus).
	 * If there is no bundle, the supplied key is returned unchanged.
	 * @param key The localization key, must be an exact match to a key in the bundle file
	 * @param args Any optional arguments to supply to the localized sentence, e.g. character names, a number for pluralization, etc
	 * @return The localized line of text
	 */
	public String getLine(String key, Object... args) {
		try {
			if (topBundle != null) {
				return modifierStrategy.apply(topBundle.format(key, args));
			}
		} catch (MissingResourceException e) {
			// This will catch any keys that belong in the baseBundle, or any undefined keys
		}

		try {
			return modifierStrategy.apply(baseBundle.format(key, args));
		} catch (NullPointerException e) {
			// A game that doesn't (yet?) have any bundles just returns the key
			if (throwExceptions) {
				throw e;
			} else {
				return modifierStrategy.apply(key);
			}
		} catch (MissingResourceException e) {
			Gdx.app.error("TextSupplier", "Key " + key + " really isn't found anywhere!");
			if (throwExceptions) {
				throw e;
			} else {
				return modifierStrategy.apply(baseBundle.format("missing_text", key));
			}
		}
	}

	void useLocale() {
		if(locale == null) {
			locale = Locale.ROOT;
		}
		baseBundle = I18NBundle.createBundle(baseBundleFileHandle, locale);
	}
}
