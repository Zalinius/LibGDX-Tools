package com.darzalgames.libgdxtools.errorhandling;

import java.util.Set;

public class CrashLocalization {

	public static final CrashLocalization ENGLISH_CRASH_LOCALIZATION = new CrashLocalization(
			"{game} - crash reporting",
			"Unfortunately {game} has crashed :(",
			"The following report was saved to: {file}",
			"Crash Report:",

			"Send to DarZal Games",
			"Sending . . .",
			"SENT!  thanks c:",
			"FAILED({httpCode})  :l",

			"Copy crash report",
			"Copied!",

			"Exit"
	);

	public static final CrashLocalization FRENCH_CRASH_LOCALIZATION = new CrashLocalization(
			"{game} - rapport d'erreur",
			"Malheureusement, {game} a rencontré une erreur :(",
			"Le rapport suivant a été sauvegardé à : {file}",
			"Rapport d'erreur :",

			"Envoyer à Jeux DarZal",
			"En cours d'envoi . . .",
			"ENVOYÉ !  merci c:",
			"ÉCHEC({httpCode})  :l",

			"Copier le rapport",
			"Copié !",

			"Quitter"
	);

	public static CrashLocalization getLocalizationFromCode(String languageCode) {
		Set<String> frenchLanguageCodes = Set.of("fr", "fra", "fre");

		if (frenchLanguageCodes.contains(languageCode)) {
			return FRENCH_CRASH_LOCALIZATION;
		} else {
			return ENGLISH_CRASH_LOCALIZATION;
		}
	}

	private final String titleSuffixString;
	private final String reportSituationLabelString;
	private final String reportFileLabelString;
	private final String reportHeaderLabelString;

	private final String sendButtonString;
	private final String sendingButtonString;
	private final String sentSuccessButtonString;
	private final String sentFailedButtonString;

	private final String copyButtonString;
	private final String copiedButtonString;

	private final String exitButtonString;

	public CrashLocalization(
			String titleSuffixString, String reportSituationLabelString, String reportFileLabelString, String reportHeaderLabelString,
			String sendButtonString, String sendingButtonString, String sentSuccessButtonString, String sentFailedButtonString,
			String copyButtonString, String copiedButtonString, String exitButtonString) {
		super();
		this.titleSuffixString = titleSuffixString;
		this.reportSituationLabelString = reportSituationLabelString;
		this.reportFileLabelString = reportFileLabelString;
		this.reportHeaderLabelString = reportHeaderLabelString;
		this.sendButtonString = sendButtonString;
		this.sendingButtonString = sendingButtonString;
		this.sentSuccessButtonString = sentSuccessButtonString;
		this.sentFailedButtonString = sentFailedButtonString;
		this.copyButtonString = copyButtonString;
		this.copiedButtonString = copiedButtonString;
		this.exitButtonString = exitButtonString;
	}

	public String getTitleSuffixString(String game) {
		return titleSuffixString.replace("{game}", game);
	}

	public String getReportSituationLabelString(String game) {
		return reportSituationLabelString.replace("{game}", game);
	}

	public String getReportFileLabelString(String file) {
		return reportFileLabelString.replace("{file}", file);
	}

	public String getReportHeaderLabelString() {
		return reportHeaderLabelString;
	}

	public String getSendButtonString() {
		return sendButtonString;
	}

	public String getSendingButtonString() {
		return sendingButtonString;
	}

	public String getSentSuccessButtonString() {
		return sentSuccessButtonString;
	}

	public String getSentFailedButtonString(String statusCode) {
		return sentFailedButtonString.replace("{httpCode}", statusCode);
	}

	public String getCopyButtonString() {
		return copyButtonString;
	}

	public String getCopiedButtonString() {
		return copiedButtonString;
	}

	public String getExitButtonString() {
		return exitButtonString;
	}

}
