package com.darzalgames.libgdxtools.platform;

public enum GameEdition {
	DEMO("-demo"),
	FULL("");
	

	private final String versionSuffix;
	
	private GameEdition(String versionSuffix) {
		this.versionSuffix = versionSuffix;
	}

	public String getVersionSuffix() {
		return versionSuffix;
	}
}
