package com.darzalgames.libgdxtools.save;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DesktopSaveManagerTest {

	@Test
	void getSubsystemSaveFileName_withoutBackup_createsCorrectFileName() {
		String gameName = "Quest Giver";
		DesktopSaveManager desktopSaveManager = makeAbstractDesktopSaveManager(gameName);
		String saveFileName = desktopSaveManager.getSubsystemSaveFileName("Graphics Options");

		assertEquals("QuestGiverGraphicsOptions.json", saveFileName);
	}

	@Test
	void getSubsystemBackupSaveFileName_withBackup_createsCorrectFileName() {
		String gameName = "Quest Giver";
		DesktopSaveManager desktopSaveManager = makeAbstractDesktopSaveManager(gameName);

		String saveFileName = desktopSaveManager.getSubsystemBackupSaveFileName("Options");

		assertEquals("QuestGiverOptions-backup.json", saveFileName);
	}

	private static DesktopSaveManager makeAbstractDesktopSaveManager(String gameName) {
		return new DesktopSaveManager(gameName, "DarZal Games", null) {

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

}
