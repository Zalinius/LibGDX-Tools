package com.darzalgames.libgdxtools.internationalization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.files.FileHandle;

class TextSupplierTest {

	private String key = "test_key";
	
	@Test
	void getLine_withoutAnyBundles_returnsTheKeyAsProvided() throws Exception {
		makeTextSupplier();

		String result = TextSupplier.getLine(key);

		assertEquals(key, result);
	}

	@Test
	void getLine_withOnlyBaseBundle_returnsTheLocalizedKey() throws Exception {
		makeTextSupplier();
		TextSupplier.useLanguage("");

		String result = TextSupplier.getLine(key);

		assertEquals("Base expected", result);
	}

	@Test
	void getLine_withTopAndBaseBundle_returnsTheLocalizedKey() throws Exception {
		makeTextSupplier();
		TextSupplier.useLanguage("");
		TextSupplier.useTopBundle(new FileHandle("./src/test/resources/com/darzalgames/libgdxtools/internationalization/Top"));

		String result = TextSupplier.getLine(key);

		assertEquals("Top expected", result);
	}

	@Test
	void getLine_withFrenchBaseBundle_returnsTheLocalizedKey() throws Exception {
		makeTextSupplier();
		TextSupplier.useLanguage("fr");

		String result = TextSupplier.getLine(key);

		assertEquals("Bonjour base", result);
	}

	@Test
	void getLine_withFrenchTopAndBaseBundle_returnsTheLocalizedKey() throws Exception {
		makeTextSupplier();
		TextSupplier.useLanguage("fr");
		TextSupplier.useTopBundle(new FileHandle("./src/test/resources/com/darzalgames/libgdxtools/internationalization/Top"));

		String result = TextSupplier.getLine(key);

		assertEquals("Bonjour top", result);
	}

	private static void makeTextSupplier() {
		TextSupplier.initialize(new BundleManager(
				new FileHandle("./src/test/resources/com/darzalgames/libgdxtools/internationalization/Base"),
				getSupportedLocales()));
	}

	protected static ArrayList<Locale> getSupportedLocales() {
		ArrayList<Locale> supportedLocales = new ArrayList<>(); 
		supportedLocales.add(Locale.ROOT);
		supportedLocales.add(Locale.FRENCH);
		return supportedLocales;
	}
}
