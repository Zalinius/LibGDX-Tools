package com.darzalgames.libgdxtools.save;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.edition.GameEdition;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.maingame.SharesGameInformation;
import com.darzalgames.libgdxtools.os.*;
import com.darzalgames.libgdxtools.platform.PlatformStrategy;
import com.darzalgames.libgdxtools.preferences.PreferenceManager;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.UserInterfaceFactory;

class DesktopSaveManagerIT {

	@BeforeAll
	static void setupAll() {
		Gdx.files = new Lwjgl3Files();
		GameInfo.setMainGame(makeDummyGameInfo());
	}

	@Test
	void getSubsystemFileSaveFile_onWindows_createsCorrectFile() {
		DesktopSaveManager saveManager = makeAbstractDesktopSaveManager("Cosmic Cult", makeDummyWindows());
		String subsystem = "Game Specific Options";

		FileHandle saveFile = saveManager.getSubsystemFileSaveFile(subsystem);
		FileHandle backupSaveFile = saveManager.getSubsystemFileBackupSaveFile(subsystem);

		assertEquals("C:/Users/user/AppData/Local/CosmicCult-DarZalGames/dev/CosmicCultGameSpecificOptions.json", saveFile.toString());
		assertEquals("C:/Users/user/AppData/Local/CosmicCult-DarZalGames/dev/CosmicCultGameSpecificOptions-backup.json", backupSaveFile.toString());
	}

	@Test
	void getSubsystemFileSaveFile_onLinux_createsCorrectFile() {
		DesktopSaveManager saveManager = makeAbstractDesktopSaveManager("Cosmic Cult", new LinuxGameOperatingSystem());
		String subsystem = "Game Specific Options";

		FileHandle saveFile = saveManager.getSubsystemFileSaveFile(subsystem);
		FileHandle backupSaveFile = saveManager.getSubsystemFileBackupSaveFile(subsystem);

		assertEquals(".local/share/CosmicCult-DarZalGames/dev/CosmicCultGameSpecificOptions.json", saveFile.toString());
		assertEquals(".local/share/CosmicCult-DarZalGames/dev/CosmicCultGameSpecificOptions-backup.json", backupSaveFile.toString());
	}

	@Test
	void getSubsystemFileSaveFile_onMac_createsCorrectFile() {
		DesktopSaveManager saveManager = makeAbstractDesktopSaveManager("Cosmic Cult", new MacGameOperatingSystem());
		String subsystem = "Game Specific Options";

		FileHandle saveFile = saveManager.getSubsystemFileSaveFile(subsystem);
		FileHandle backupSaveFile = saveManager.getSubsystemFileBackupSaveFile(subsystem);

		assertEquals("Library/Application Support/CosmicCult-DarZalGames/dev/CosmicCultGameSpecificOptions.json", saveFile.toString());
		assertEquals("Library/Application Support/CosmicCult-DarZalGames/dev/CosmicCultGameSpecificOptions-backup.json", backupSaveFile.toString());
	}

	@AfterAll
	static void teardownAll() {
		Gdx.files = null;
		GameInfo.setMainGame(null);
	}

	private static DesktopSaveManager makeAbstractDesktopSaveManager(String gameName, GameOperatingSystem os) {
		return new DesktopSaveManager(gameName, "DarZal Games", os) {

			@Override
			public void save() {}

			@Override
			public boolean loadOptions() {
				return false;
			}

			@Override
			public boolean loadGame() {
				return false;
			}
		};
	}

	private static SharesGameInformation makeDummyGameInfo() {
		return new SharesGameInformation() {

			@Override
			public UserInterfaceFactory getUserInterfaceFactory() {
				return null;
			}

			@Override
			public SaveManager getSaveManager() {
				return null;
			}

			@Override
			public PreferenceManager getPreferenceManager() {
				return null;
			}

			@Override
			public PlatformStrategy getPlatformStrategy() {
				return new DeveloperGamePlatformStrategy();
			}

			@Override
			public GameOperatingSystem getOperatingSystem() {
				return null;
			}

			@Override
			public String getGameVersion() {
				return null;
			}

			@Override
			public String getGameName() {
				return null;
			}

			@Override
			public GameEdition getGameEdition() {
				return null;
			}

			@Override
			public String getDeveloperName() {
				return null;
			}
		};
	}

	// We need this since Windows requires the APPDATALOCAL environment variable which won't be present on Jenkins, Mac or Linux
	private static WindowsGameOperatingSystem makeDummyWindows() {
		return new WindowsGameOperatingSystem() {
			@Override
			public FileHandle getSaveFileLocation(String fullGameAndSaveName) {
				return Gdx.files.absolute("C:/Users/user/AppData/Local" + "/" + fullGameAndSaveName);
			}

		}

		;
	}

}
