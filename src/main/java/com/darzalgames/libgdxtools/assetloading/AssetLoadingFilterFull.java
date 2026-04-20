package com.darzalgames.libgdxtools.assetloading;

import java.util.Collection;

import com.badlogic.gdx.assets.AssetDescriptor;

public class AssetLoadingFilterFull implements AssetLoadingFilter {

	@Override
	public void filter(Collection<AssetDescriptor<?>> allAssets) {
		// no filtering: the full game uses all assets
	}

}
