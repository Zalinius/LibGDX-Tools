package com.darzalgames.libgdxtools.errorhandling.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserReportTest {

	@Test
	void toString_withNullEmailAndMessage_isBlank() {
		UserReport userReport = new UserReport(null, null);

		String report = userReport.toString();

		assertTrue(report.isBlank());
	}

	@Test
	void toString_withBlankEmailAndMessage_isBlank() {
		UserReport userReport = new UserReport("", "");

		String report = userReport.toString();

		assertTrue(report.isBlank());
	}

	@Test
	void toString_withEmailButNoMessage_containsEmail() {
		UserReport userReport = new UserReport("test@email.com", "");

		String report = userReport.toString();

		assertEquals("Email: test@email.com\n", report);
	}

	@Test
	void toString_withNoEmailButWithMessage_containsMessage() {
		UserReport userReport = new UserReport("", "Lorum Ipsum");

		String report = userReport.toString();

		assertEquals("Player Message:Lorum Ipsum\n", report);
	}

	@Test
	void toString_withEmailAndWithMessage_containsBoth() {
		UserReport userReport = new UserReport("test@email.com", "Lorum Ipsum");

		String report = userReport.toString();

		assertEquals("Email: test@email.com\nPlayer Message:Lorum Ipsum\n", report);
	}

}
