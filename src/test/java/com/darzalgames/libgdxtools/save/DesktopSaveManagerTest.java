package com.darzalgames.libgdxtools.save;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DesktopSaveManagerTest {

	@Test
	void getSaveName_withoutBackup_createsCorrectFileName() {
		String gameName = "Quest Giver";

		String saveFileName = DesktopSaveManager.getSaveName(false, gameName);

		assertEquals("QuestGiverSave.json", saveFileName);
	}

	@Test
	void getSaveName_withBackup_createsCorrectFileName() {
		String gameName = "Quest Giver";

		String saveFileName = DesktopSaveManager.getSaveName(true, gameName);

		assertEquals("QuestGiverSave-backup.json", saveFileName);
	}

	@Test
	void getSubsystemSaveName_withoutBackup_createsCorrectFileName() {
		String gameName = "Quest Giver";
		String subsystem = "User Options";

		String saveFileName = DesktopSaveManager.getSubsystemSaveName(false, gameName, subsystem);

		assertEquals("QuestGiverUserOptionsSave.json", saveFileName);
	}

	@Test
	void getSubsystemSaveName_withBackup_createsCorrectFileName() {
		String gameName = "Quest Giver";
		String subsystem = "User Options";

		String saveFileName = DesktopSaveManager.getSubsystemSaveName(true, gameName, subsystem);

		assertEquals("QuestGiverUserOptionsSave-backup.json", saveFileName);
	}

}
