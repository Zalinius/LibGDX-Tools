package com.darzalgames.libgdxtools.errorhandling.data;

import java.util.Optional;

public class UserReport {

	private final Optional<String> contactEmail;
	private final Optional<String> playerMessage;

	public UserReport(String contactEmail, String playerMessage) {
		this.contactEmail = initializeOptionalString(contactEmail);
		this.playerMessage = initializeOptionalString(playerMessage);
	}

	public static Optional<String> initializeOptionalString(String string) {
		if (string == null || string.isBlank()) {
			return Optional.empty();
		} else {
			return Optional.of(string);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (contactEmail.isPresent()) {
			sb.append("Email: " + contactEmail.get());
		}
		if (playerMessage.isPresent()) {
			sb.append("Player Message:" + playerMessage.get());
		}

		return sb.toString();
	}

//TODO json, using jackson? how do they deal with Optional values

}
