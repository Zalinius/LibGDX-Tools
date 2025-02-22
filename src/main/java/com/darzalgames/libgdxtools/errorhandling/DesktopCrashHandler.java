package com.darzalgames.libgdxtools.errorhandling;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;

import com.darzalgames.darzalcommon.time.FileFriendlyTimeFormatter;

public class DesktopCrashHandler extends CrashHandler {
	

	@Override
	public String reportCrash(CrashReport crashReport) {
			String localTime = FileFriendlyTimeFormatter.dateTimeFileFriendlyFormat(crashReport.getInstant(), ZoneId.systemDefault());
			String crashReportFileName = "crash-" + localTime + ".err.txt";
			String crashReportJson = crashReport.toJson();
			
			;
			try(FileWriter fileWriter = new FileWriter(crashReportFileName)){
				fileWriter.write(crashReportJson);
			} catch (IOException e) {
				System.err.println("Couldn't write crash report to file: " + crashReportFileName);
				System.err.println("Error json:\n" + crashReportJson);
				e.printStackTrace();
			}

			return "Wrote crash report to file: " + crashReportFileName;
	}

	@Override
	public void logCrashReportStatus(String status) {
		System.err.println(status);
	}
	
}
