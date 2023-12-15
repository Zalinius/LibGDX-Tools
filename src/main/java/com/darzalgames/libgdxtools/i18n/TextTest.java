package com.darzalgames.libgdxtools.i18n;

import java.util.ArrayList;
import java.util.Locale;

import com.badlogic.gdx.files.FileHandle;

// TODO Try this out in Quest Giver before making it part of a library
public class TextTest extends TextSupplier {

	public TextTest() {
		initialize(this);
	}

	@Override
	protected FileHandle getBaseBundleFileHandle() {
		return null;
	}

	@Override
	protected ArrayList<Locale> getSupportedLocales() {
		ArrayList<Locale> supportedLocales = new ArrayList<>(); 
		supportedLocales.add(Locale.ROOT);
		supportedLocales.add(Locale.FRENCH); // I'm sure someday we'll want to load this in from the files, but for now that feels like overkill
		
		return supportedLocales;
	}
	
}
