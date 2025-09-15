package com.darzalgames.libgdxtools.errorhandling;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class CrashReportTest {

	@Test
	void getters() throws Exception {
		Instant instant = Instant.parse("2025-02-09T18:40:36.949177809Z");
		UUID uuid = UUID.fromString("8c6b106b-e613-4cdd-b75b-2b6592c6806d");
		CrashReport crashReport = new CrashReport("gameName", "version", "platformName", instant, uuid, new String[] { "stackTrace" });

		assertEquals("gameName", crashReport.getGameName());
		assertEquals("version", crashReport.getGameVersion());
		assertEquals("platformName", crashReport.getPlatformName());
		assertEquals("2025-02-09T18:40:36.949177809Z", crashReport.getTime());
		assertEquals("8c6b106b-e613-4cdd-b75b-2b6592c6806d", crashReport.getId());
		assertArrayEquals(new String[] { "stackTrace" }, crashReport.getStackTrace());
	}

	@Test
	void toJson() throws Exception {
		Instant instant = Instant.parse("2025-02-09T18:40:36.949177809Z");
		UUID uuid = UUID.fromString("8c6b106b-e613-4cdd-b75b-2b6592c6806d");
		CrashReport crashReport = new CrashReport("game", "version", "platform", instant, uuid, new String[] { "java.lang.RuntimeException", "caused by:" });

		assertEquals(expectedJson(), crashReport.toJson());
	}

	private static String expectedJson() {
		return """
				{
				"gameName": "game",
				"gameVersion": "version",
				"platformName": "platform",
				"time": "2025-02-09T18:40:36.949177809Z",
				"id": "8c6b106b-e613-4cdd-b75b-2b6592c6806d",
				"stackTrace": [
					"java.lang.RuntimeException",
					"caused by:"
				]
				}""";
	}

}
