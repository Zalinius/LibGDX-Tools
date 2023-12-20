package com.darzalgames.libgdxtools.steam;

import com.darzalgames.libgdxtools.ui.input.handler.InputHandler.InputMethod;

public class SteamStatsManager {
	
	private SteamStatsManager() {}

	public static void setScenarioStartedInLanguage(String scenarioNameKey, String languageSuffix) {
		// Because english is the root file, it has no language suffix
		if (languageSuffix.isBlank()) {
			languageSuffix = "en";
		}
		String statKey = "start_" + scenarioNameKey + "_" + languageSuffix;
		
		int currentStat = SteamConnection.getStat(statKey);
		SteamConnection.setStat(statKey, currentStat + 1);
	}

	public static void setScenarioCompletedWithInput(InputMethod inputMethod) {
		String inputSuffix = "";
		switch (inputMethod) {
		case MOUSE: 
			inputSuffix = "mouse";
			break;
		case GAMEPAD:
			inputSuffix = "gamepad";
			break;
		case KEYBOARD:
			inputSuffix = "keyboard";
			break;
		}
		
		String statKey = "done_scenario_" + inputSuffix;
		
		int currentStat = SteamConnection.getStat(statKey);
		SteamConnection.setStat(statKey, currentStat + 1);
	}

}