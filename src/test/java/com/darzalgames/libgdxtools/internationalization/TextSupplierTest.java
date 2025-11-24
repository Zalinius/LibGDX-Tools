package com.darzalgames.libgdxtools.internationalization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.files.FileHandle;

class TextSupplierTest {

	private final String key = "test_key";

	@Test
	void getLine_withoutAnyBundles_returnsTheKeyAsProvided() {
		makeTextSupplier();

		String result = TextSupplier.getLine(key);

		assertEquals(key, result);
	}

	@Test
	void getLine_withOnlyBaseBundle_returnsTheLocalizedKey() {
		makeTextSupplier();
		TextSupplier.useLanguage("");

		String result = TextSupplier.getLine(key);

		assertEquals("Base expected", result);
	}

	@Test
	void getLine_withMissingKey_returnsTheMissingKeyText() {
		makeTextSupplier();
		TextSupplier.useLanguage("");

		String result = TextSupplier.getLine("non-existent key");

		assertEquals("(*Missing text!*) non-existent key", result);
	}

	@Test
	void getLine_withBlankKey_inEnAndFr_returnsEmptyString() {
		makeTextSupplier();

		TextSupplier.useLanguage("");
		String englishResult = TextSupplier.getLine("");
		TextSupplier.useLanguage("fr");
		String frenchResult = TextSupplier.getLine("");

		assertEquals("", englishResult);
		assertEquals("", frenchResult);
	}

	@Test
	void getLine_withMissingKey_inFrench_returnsTheMissingKeyText() {
		makeTextSupplier();
		TextSupplier.useLanguage("fr");

		String result = TextSupplier.getLine("non-existent clé");

		assertEquals("(*texte manquant!*) non-existent clé", result);
	}

	@Test
	void getLine_withTopAndBaseBundle_returnsTheLocalizedKey() {
		makeTextSupplierWith2Layers();
		TextSupplier.useLanguage("");

		String result = TextSupplier.getLine(key);

		assertEquals("Top expected", result);
	}

	@Test
	void getLine_withMissingKeyAndThrowsExceptionsOn_throwsException() {
		makeTextSupplierWith2Layers();
		TextSupplier.useLanguage("");
		TextSupplier.setThrowExceptions(true);

		assertThrows(MissingResourceException.class, () -> TextSupplier.getLine("non-existent key"));
	}

	@Test
	void getAllDisplayNames_withFrAndEn_containsExpectedLanguageNameString() {
		makeTextSupplierWith2Layers();
		TextSupplier.useLanguage("");

		List<Supplier<String>> result = TextSupplier.getAllDisplayNames();
		List<String> names = result.stream().map(Supplier::get).toList();

		assertTrue(names.contains("English"));
		assertTrue(names.contains("Français"));
	}

	@Test
	void getLine_withFrenchBaseBundle_returnsTheLocalizedKey() {
		makeTextSupplier();
		TextSupplier.useLanguage("fr");

		String result = TextSupplier.getLine(key);

		assertEquals("Bonjour base", result);
	}

	@Test
	void getLine_withFrenchTopAndBaseBundle_returnsTheLocalizedKey() {
		makeTextSupplierWith2Layers();
		TextSupplier.useLanguage("fr");

		String result = TextSupplier.getLine(key);

		assertEquals("Bonjour top", result);
	}

	@Test
	void getFormattedLocaleForSave_withOurUsualLocales_returnsExpectedStrings() {
		String englishResult = TextSupplier.getFormattedLocaleForSave(Locale.ROOT);
		String frenchResult = TextSupplier.getFormattedLocaleForSave(Locale.FRENCH);
		String frenchCanResult = TextSupplier.getFormattedLocaleForSave(Locale.CANADA_FRENCH);

		assertEquals("", englishResult);
		assertEquals("fr", frenchResult);
		assertEquals("fr_CA", frenchCanResult);
	}

	private static void makeTextSupplier() {
		TextSupplier.initialize(
				new BundleManager(
						new FileHandle("./src/test/resources/com/darzalgames/libgdxtools/internationalization/Base"),
						getSupportedLocales()
				)
		);
	}

	private static void makeTextSupplierWith2Layers() {
		TextSupplier.initialize(
				new BundleManager(
						List.of(
								new FileHandle("./src/test/resources/com/darzalgames/libgdxtools/internationalization/Top"),
								new FileHandle("./src/test/resources/com/darzalgames/libgdxtools/internationalization/Base")
						),
						getSupportedLocales()
				)
		);
	}

	protected static ArrayList<Locale> getSupportedLocales() {
		ArrayList<Locale> supportedLocales = new ArrayList<>();
		supportedLocales.add(Locale.ROOT);
		supportedLocales.add(Locale.FRENCH);
		return supportedLocales;
	}
}
