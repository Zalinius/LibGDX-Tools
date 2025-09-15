package com.darzalgames.libgdxtools.errorhandling;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.logging.Logger;

public abstract class CrashHandler {

	public final void handleException(Exception exception, List<String> programArguments) throws Exception {
		CrashReport crashReport = buildCrashReport(exception, programArguments);
		List<ReportStatus> statuses = reportCrash(crashReport);
		logCrashReportStatus(statuses);

		throw exception;
	}

	/**
	 * @param crashReport
	 * @return The result of the crash reporting
	 */
	protected abstract List<ReportStatus> reportCrash(CrashReport crashReport);

	/**
	 * @param statuses What the results of reporting the crash was
	 */
	protected abstract void logCrashReportStatus(List<ReportStatus> statuses);

	static CrashReport buildCrashReport(Exception exception, List<String> args) {
		Properties gameProperties = tryGetGameProperties("data/game.properties");
		String gameName = gameProperties.getProperty("gameName", "nameNotFound");
		String gameVersion = gameProperties.getProperty("version", "versionNotFound");
		String platform = tryGetString(() -> args.get(0));
		Instant utcTime = Instant.now();
		UUID id = UUID.randomUUID();
		String[] stackTrace = getMessageAndStackTraceArray(exception);
		return new CrashReport(gameName, gameVersion, platform, utcTime, id, stackTrace);
	}

	public static Properties tryGetGameProperties(String propertiesFile) {
		Properties gameProperties = new Properties();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		try (InputStream inputStream = cl.getResourceAsStream(propertiesFile)) {
			gameProperties.load(inputStream);
		} catch (Exception e) {
			Logger.getGlobal().severe("Couldn't find or open properties file: " + propertiesFile + "(" + e.getMessage() + ")");
			return gameProperties;
		}

		return gameProperties;
	}

	public static String tryGetString(Supplier<String> stringGetter) {
		String string = null;
		try {
			string = stringGetter.get();
		} catch (Exception e) {
			// swallow the exception, since we are already handling a game crash
		}
		return string;
	}

	public static String[] getMessageAndStackTraceArray(Exception exception) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final Charset utf8 = StandardCharsets.UTF_8;
		String data = null;
		try (PrintStream ps = new PrintStream(baos, true, utf8)) {
			exception.printStackTrace(ps);
			data = baos.toString(utf8);
		}

		String[] stackTraceArray = data.split("\n");
		for (int i = 0; i < stackTraceArray.length; i++) {
			stackTraceArray[i] = stackTraceArray[i].trim();
		}
		return stackTraceArray;
	}

}
