package com.darzalgames.libgdxtools.errorhandling;

public class ReportStatus {
	private final boolean successful;
	private final String shortMessage;
	private final String longMessage;

	public ReportStatus(boolean successful, String shortMessage, String longMessage) {
		this.successful = successful;
		this.shortMessage = shortMessage;
		this.longMessage = longMessage;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public String getShortMessage() {
		return shortMessage;
	}

	public String getLongMessage() {
		return longMessage;
	}

	@Override
	public String toString() {
		return shortMessage + " - " + longMessage;
	}

}
