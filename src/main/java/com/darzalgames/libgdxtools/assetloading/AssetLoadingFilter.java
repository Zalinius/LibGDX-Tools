package com.darzalgames.libgdxtools.assetloading;

import java.util.Collection;

import com.badlogic.gdx.assets.AssetDescriptor;

public interface AssetLoadingFilter {

	void filter(Collection<AssetDescriptor<?>> allAssets);

}
