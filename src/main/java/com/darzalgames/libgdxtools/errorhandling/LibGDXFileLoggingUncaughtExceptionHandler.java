package com.darzalgames.libgdxtools.errorhandling;

import java.io.PrintStream;
import java.time.ZoneId;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.darzalcommon.errorhandling.LoggingUncaughtExceptionHandler;
import com.darzalgames.darzalcommon.errorhandling.TimestampedThrowable;
import com.darzalgames.darzalcommon.time.FileFriendlyTimeFormatter;

/**
 * An uncaught exception handler that writes to a file, using the GDX internal FileHandle
 */
public class LibGDXFileLoggingUncaughtExceptionHandler extends LoggingUncaughtExceptionHandler{
	private final String workingDirectory;

	public LibGDXFileLoggingUncaughtExceptionHandler() {
		this.workingDirectory = "";
	}	
	public LibGDXFileLoggingUncaughtExceptionHandler(String workingDirectory) {
		this.workingDirectory =  workingDirectory;
	}

	@Override
	protected PrintStream getPrintStream(TimestampedThrowable report) {
		String reportFileName = prepareLogFileName(report);
		FileHandle reportDirectory = Gdx.files.local(workingDirectory);
		FileHandle reportFile = reportDirectory.child(reportFileName);

		Gdx.app.error("Game", "An unexpected error has occurred. The error has been logged to " + reportFile);

		return new PrintStream(reportFile.write(false));
	}
	
	private static String prepareLogFileName(TimestampedThrowable log) {
		return FileFriendlyTimeFormatter.dateTimeFileFriendlyFormat(log.timestamp, ZoneId.systemDefault()) + ".err";
	}
}
