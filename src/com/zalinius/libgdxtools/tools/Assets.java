package com.zalinius.libgdxtools.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class Assets {

	private Assets() {}

	private static AssetManager assetManager;
	private static Properties gameProperties;

	public static void initialize() throws IllegalArgumentException, IllegalAccessException {
		//Create AssetManager
		assetManager = new AssetManager();

		//Put all resources into AssetManager
		Class<Assets> assetsClass = Assets.class; // Can we rewrite this to read in from files? (or an inherited class?)
		Field[] fields = assetsClass.getFields();
		for (Field field : fields) {
			if(field.getType().equals(AssetDescriptor.class)) {
				AssetDescriptor<?> assetDescriptor = (AssetDescriptor<?>) field.get(null);
				assetManager.load(assetDescriptor);
			}
		}

		try (InputStream is = new ByteArrayInputStream(Gdx.files.internal(propertiesFile).readString().getBytes())){
			gameProperties = new Properties();
			gameProperties.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Load all assets
		assetManager.finishLoading();
	}

	public static Texture get(final AssetDescriptor<Texture> assetDescriptor) {
		return assetManager.get(assetDescriptor);
	}

	public static List<AtlasRegion> getAtlas(final AssetDescriptor<TextureAtlas> assetDescriptor) {
		TextureAtlas atlas = assetManager.get(assetDescriptor);
		String name = assetDescriptor.fileName.substring(assetDescriptor.fileName.lastIndexOf("/")+1, assetDescriptor.fileName.indexOf("."));
		List<AtlasRegion> textures = new ArrayList<>();
		Array<Sprite> sprites = atlas.createSprites();
		for (int i = 0; i < sprites.size; i++) {
			if(sprites.get(i) != null) {
				textures.add(atlas.findRegion(name, i));
			}
		}
		return textures;
	}

	public static String getGameName() { return gameProperties.getProperty("name");}
	public static String getGameVersion() { return gameProperties.getProperty("version");}

	protected static String preferenceFile = "com.zalinius.LibGDXTools.preferences";
	protected static String propertiesFile; //e.g. "game.properties"

	public static String getPreferenceFile() {
		return preferenceFile;
	}



	public static final FileHandle outlineVertexShader = Gdx.files.internal("shader/outline.vertex.glsl");
	public static final FileHandle outlineFragmentShader = Gdx.files.internal("shader/outline.fragment.glsl");
	public static final FileHandle outlineDottedVertexShader = Gdx.files.internal("shader/outlineDotted.vertex.glsl");
	public static final FileHandle outlineDottedFragmentShader = Gdx.files.internal("shader/outlineDotted.fragment.glsl");

	public static final FileHandle skinPath = Gdx.files.internal("defaultSkin/skin/uiskin.json");
	public static final FileHandle exampleFontPath = Gdx.files.internal("fonts/example.ttf");
	public static final AssetDescriptor<Texture> exampleTexture = new AssetDescriptor<>("gfx/textures/example.png", Texture.class);
	public static final AssetDescriptor<TextureAtlas> exampleTextureAtlas = new AssetDescriptor<>("gfx/atlases/example.atlas", TextureAtlas.class);
	public static final AssetDescriptor<Music> exampleMusic = new AssetDescriptor<>("music/example.mp3", Music.class);

	public static void dispose() {
		assetManager.dispose();
	}

	public static Music getMusic(final AssetDescriptor<Music> assetDescriptor) {
		return assetManager.get(assetDescriptor);
	}

}
