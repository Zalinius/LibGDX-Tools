package com.darzalgames.libgdxtools.errorhandling.desktop;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.darzalgames.darzalcommon.time.FileFriendlyTimeFormatter;
import com.darzalgames.libgdxtools.errorhandling.*;
import com.darzalgames.libgdxtools.platform.GamePlatform;

public class DesktopCrashHandler extends CrashHandler {

	@Override
	public List<ReportStatus> reportCrash(CrashReport crashReport) {
		String localTime = FileFriendlyTimeFormatter.dateTimeFileFriendlyFormat(crashReport.getInstant(), ZoneId.systemDefault());
		String crashReportLocalFileName = "crash-" + localTime + ".err.txt";
		File crashReportLocalFile = new File(crashReportLocalFileName);

		String universalTime = FileFriendlyTimeFormatter.dateTimeFileFriendlyFormat(crashReport.getInstant());
		String crashReportWebName = "crash-" + universalTime + ".err.json";

		String crashReportJson = crashReport.toJson();

		List<ReportStatus> reportingStatuses = new ArrayList<>();
		reportingStatuses.add(reportCrashToFile(crashReportLocalFile, crashReportJson));

		Supplier<ReportStatus> reportCrashOnline = () -> {
			ReportStatus status = reportCrashToDarBot5000(crashReport.getGameName(), crashReportWebName, crashReportJson);
			reportingStatuses.add(status);
			return status;
		};

		if (shouldHavePopup(crashReport)) {
			JFrame.setDefaultLookAndFeelDecorated(false);
			CrashLocalization crashLocalization = CrashLocalization.getLocalizationFromCode(Locale.getDefault().getLanguage());
			DesktopCrashPopup crashPopup = new DesktopCrashPopup(crashReport, reportCrashOnline, crashReportLocalFile.getAbsolutePath(), crashLocalization);
			crashPopup.setVisible(true);
			crashPopup.requestFocusOnSendButton();
		}

		return reportingStatuses;
	}

	private boolean shouldHavePopup(CrashReport crashReport) {
		return !GamePlatform.MAC.equalsIgnoreCase(crashReport.getPlatformName());
	}

	@Override
	public void logCrashReportStatus(List<ReportStatus> statuses) {
		logError("Crash reporting statuses: " + statuses.size());
		statuses.forEach(status -> logError("  " + status));
	}

	private ReportStatus reportCrashToFile(File crashReportFile, String crashReportJson) {
		try (FileWriter fileWriter = new FileWriter(crashReportFile)) {
			fileWriter.write(crashReportJson);
		} catch (IOException e) {
			logError("Couldn't write crash report to file: " + crashReportFile.getName());
			logError("Error json:\n" + crashReportJson);
			e.printStackTrace();
			return new ReportStatus(false, "FAILED", "Did not write crash report to file: " + crashReportFile.getAbsolutePath());
		}
		return new ReportStatus(true, "SUCCESS", "Wrote crash report to file: " + crashReportFile.getAbsolutePath());
	}

	public static ReportStatus reportCrashToDarBot5000(String gameName, String crashReportFileName, String crashReportJson) {
		gameName = gameName.toLowerCase().replace(' ', '-');
		String urlString = "https://api.darzalgames.com/crash/" + gameName + "/" + crashReportFileName;
		HttpResponse<Void> response;
		try (HttpClient httpClient = HttpClient.newHttpClient()) {
			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(urlString))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(crashReportJson))
					.timeout(Duration.ofSeconds(10))
					.build();

			response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
		} catch (IOException | InterruptedException e) {
			logError("Couldn't send crash report to: " + urlString);
			logError("Error json:\n" + crashReportJson);
			e.printStackTrace();
			return new ReportStatus(false, "ERR", "Did not send crash report to DarBot 5000: " + crashReportFileName);
		}

		if (response != null && isHttpCodeSuccess(response.statusCode())) {
			return new ReportStatus(true, Integer.toString(response.statusCode()), "SUCCESS - Sent crash report to DarBot 5000: " + crashReportFileName);
		} else if (response != null) {
			return new ReportStatus(false, Integer.toString(response.statusCode()), "FAIL - Did not send crash report to DarBot 5000: " + crashReportFileName);
		} else {
			return new ReportStatus(false, "ERR", "Did not send crash report to DarBot 5000: " + crashReportFileName);
		}
	}

	public static final int HTTP_SUCCESS_PREFIX = 2;
	public static final int HTTP_CODE_FAMILY_SIZE = 100;

	public static boolean isHttpCodeSuccess(int httpCode) {
		return httpCode / HTTP_CODE_FAMILY_SIZE == HTTP_SUCCESS_PREFIX;
	}

	private static void logError(String errorMessage) {
		Logger.getGlobal().severe(errorMessage);
	}

}
