package com.darzalgames.libgdxtools.ui.textbox;

import com.darzalgames.libgdxtools.internationalization.TextSupplier;

public class Speaker {
	
	public static final Speaker BLANK = new Speaker("blank");

	private final String assetPath;

	private Speaker(String assetPath) {
		this.assetPath = assetPath;
	}

	public String getTextureAssetPath() {
		// TODO texture atlases
		return "Speaker/" + assetPath + ".png";
	}

	public String getLocalizedName() {
		return TextSupplier.getLine(this.toString().toLowerCase() + "_name");
	}

}
