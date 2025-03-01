package com.darzalgames.libgdxtools.errorhandling;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.darzalgames.darzalcommon.time.FileFriendlyTimeFormatter;

public class DesktopCrashHandler extends CrashHandler {
	

	@Override
	public List<String> reportCrash(CrashReport crashReport) {
			String localTime = FileFriendlyTimeFormatter.dateTimeFileFriendlyFormat(crashReport.getInstant(), ZoneId.systemDefault());
			String crashReportFileName = "crash-" + localTime + ".err.txt";
			String universalTime = FileFriendlyTimeFormatter.dateTimeFileFriendlyFormat(crashReport.getInstant());
			String crashReportFileNameAnonymous = "crash-" + universalTime + ".err.json";
			String crashReportJson = crashReport.toJson();
			
			List<String> reportingStatuses = new ArrayList<>();
			reportingStatuses.add(reportCrashToFile(crashReportFileName, crashReportJson));
			
			Runnable reportCrashOnline = () -> {
				String status = reportCrashToDarBot5000(crashReport.getGameName(), crashReportFileNameAnonymous, crashReportJson);
				reportingStatuses.add(status);
			};
			DesktopCrashPopup crashPopup = new DesktopCrashPopup(crashReport, reportCrashOnline);

			return reportingStatuses;
	}

	@Override
	public void logCrashReportStatus(List<String> statuses) {
		System.err.println("Crash reporting statuses: " + statuses.size());
		statuses.forEach(status -> System.err.println("  " + status));
	}
	
	private String reportCrashToFile(String crashReportFileName, String crashReportJson) {
		try(FileWriter fileWriter = new FileWriter(crashReportFileName)){
			fileWriter.write(crashReportJson);
		} catch (IOException e) {
			System.err.println("Couldn't write crash report to file: " + crashReportFileName);
			System.err.println("Error json:\n" + crashReportJson);
			e.printStackTrace();
			return "FAILED - Did not write crash report to file: " + crashReportFileName;
		}
		
		return "SUCCESS - Wrote crash report to file: " + crashReportFileName;

	}
	
	public static String reportCrashToDarBot5000(String gameName, String crashReportFileName, String crashReportJson) {
		gameName = gameName.toLowerCase().replace(' ', '-');
		String urlString = "https://api.darzalgames.com/crash/" + gameName + "/" + crashReportFileName;
		
		try {
			HttpClient httpClient = HttpClient.newHttpClient();
			HttpRequest httpRequest = HttpRequest.newBuilder()
					.uri(URI.create(urlString))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(crashReportJson))
					.build();
			
			HttpResponse<Void> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
			System.out.println("Response: " + response.toString());
		} catch (IOException | InterruptedException e) {
			System.err.println("Couldn't send crash report to: " + urlString);
			System.err.println("Error json:\n" + crashReportJson);
			e.printStackTrace();
			return "FAILED - Did not send crash report to DarBot 5000: " + crashReportFileName;
		}


		return "SUCCESS - Sent crash report to DarBot 5000: " + crashReportFileName;
	}
	
}
