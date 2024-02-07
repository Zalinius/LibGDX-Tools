package com.darzalgames.libgdxtools.internationalization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.files.FileHandle;

class TextSupplierTest {
	
	@Test
	void getLine_withoutAnyBundles_returnsTheKeyAsProvided() throws Exception {
		TextSupplier.clearBundles();
		String key = "test_key";

		String result = TextSupplier.getLine(key);

		assertEquals(key, result);
	}

	@Test
	void getLine_withOnlyBaseBundle_returnsTheLocalizedKey() throws Exception {
		TextSupplier.clearBundles();
		String key = "test_key";
		makeTextSupplier();
		TextSupplier.useLanguage("");

		String result = TextSupplier.getLine(key);

		assertEquals("Base expected", result);
	}

	@Test
	void getLine_withTopAndBaseBundle_returnsTheLocalizedKey() throws Exception {
		TextSupplier.clearBundles();
		String key = "test_key";
		makeTextSupplier();
		TextSupplier.useLanguage("");
		TextSupplier.useTopBundle(new FileHandle("./src/test/java/com/darzalgames/libgdxtools/internationalization/top"));

		String result = TextSupplier.getLine(key);

		assertEquals("Top expected", result);
	}
	
	private static TextSupplier makeTextSupplier() {
		TextSupplier textSupplier = new TextSupplier() {
			@Override protected FileHandle getBaseBundleFileHandle() {
				return new FileHandle("./src/test/java/com/darzalgames/libgdxtools/internationalization/base");
			}
			@Override protected ArrayList<Locale> getSupportedLocales() {
				ArrayList<Locale> supportedLocales = new ArrayList<>(); 
				supportedLocales.add(Locale.ROOT);
				return supportedLocales;
			}
			
		};
		TextSupplier.initialize(textSupplier);
		return textSupplier;
	}
}
