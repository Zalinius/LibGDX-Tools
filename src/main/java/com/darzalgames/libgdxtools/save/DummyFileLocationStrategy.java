package com.darzalgames.libgdxtools.save;

public class DummyFileLocationStrategy implements FileLocationStrategy {

	@Override
	public String getPlayersSaveFolder() {
		return "dev";
	}

}
