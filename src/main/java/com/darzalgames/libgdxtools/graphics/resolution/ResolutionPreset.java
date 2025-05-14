package com.darzalgames.libgdxtools.graphics.resolution;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.darzalgames.libgdxtools.internationalization.TextSupplier;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.SelectBoxContentManager;

public enum ResolutionPreset {
	X_1920X1080(1920, 1080),
	X_1920X1200(1920, 1200),
	X_2560X1440(2560, 1440),
	X_2560X1600(2560, 1600),
	X_3000X1920(3000, 1920),
	X_3440X1440(3440, 1440),
	X_3840X1080(3840, 1080),
	X_3840X2160(3840, 2160),
	X_4480X1440(4480, 1440),
	X_1366X768(1366, 768)
	;

	public final int width;
	public final int height;

	private static final Map<String, ResolutionPreset> displayNameToEnum = initializeDisplayNameMap();

	ResolutionPreset(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public String getDisplayName() {
		return name().replace("X_", "").replace("X", "x").toLowerCase();
	}

	private static ResolutionPreset getPresetFromDisplayName(String name) {
		return displayNameToEnum.getOrDefault(name, ResolutionPreset.X_1920X1080);
	}

	private static Map<String, ResolutionPreset> initializeDisplayNameMap() {
		Map<String, ResolutionPreset> displayNameToEnum = new HashMap<>();
		Arrays.asList(ResolutionPreset.values()).forEach(preset -> displayNameToEnum.put(preset.getDisplayName(), preset));
		return displayNameToEnum;
	}

	public static void enterPreferredResolution() {
		enterResolution(GameInfo.getPreferenceManager().graphics().getPreferredResolution());
	}

	private static void enterResolution(ResolutionPreset preset) {

		// TODO in theory this should go through the window resizer I think
		Gdx.graphics.setUndecorated(false);
		Gdx.graphics.setWindowedMode(preset.width, preset.height);
		GameInfo.getPreferenceManager().graphics().setPreferredResoluton(preset);
	}

	public static SelectBoxContentManager getContentManager() {
		return new SelectBoxContentManager() {
			@Override
			public Supplier<String> getCurrentSelectedDisplayName() {
				return () -> GameInfo.getPreferenceManager().graphics().getPreferredResolution().getDisplayName();
			}

			@Override
			public Consumer<String> getChoiceResponder() {
				return choice -> {
					ResolutionPreset preset = ResolutionPreset.getPresetFromDisplayName(choice);
					enterResolution(preset);
				};
			}

			@Override
			public Collection<Supplier<String>> getAllDisplayNames() {
				Stream<Supplier<String>> allPresetsNameStream = Arrays.asList(ResolutionPreset.values()).stream().map(preset -> (preset::getDisplayName));
				return allPresetsNameStream.toList();
			}

			@Override
			public Supplier<String> getBoxLabelSupplier() {
				return () -> (TextSupplier.getLine("resolution_label"));
			}
		};
	}
}
