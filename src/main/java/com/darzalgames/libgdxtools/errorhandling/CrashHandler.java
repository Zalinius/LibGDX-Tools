package com.darzalgames.libgdxtools.errorhandling;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CrashHandler {
	
	
	public void handleException(Exception exception, String[] programArguments) throws Exception {
		CrashReport crashReport = buildCrashReport(exception, programArguments);

		Function<CrashReport, String> errorReportConsumer = getCrashReportConsumer();
		Consumer<String> errorLogConsumer = getErrorLogConsumer();

		String errorReportingResult = errorReportConsumer.apply(crashReport);
		errorLogConsumer.accept(errorReportingResult);
		
		throw exception;
	}
	
	/**
	 * @return A function that consumers an error report, and then returns a string detailing what happened
	 */
	public abstract Function<CrashReport, String> getCrashReportConsumer();
	
	/**
	 * @return A function that consumers an error log, and then returns true if it successfuly consumed the log
	 */
	public abstract Consumer<String> getErrorLogConsumer();
	
	
	public static CrashReport buildCrashReport(Exception exception, String[] args) {
		Properties gameProperties = getGameProperties("data/game.properties");
		String gameName = gameProperties.getProperty("gameName");
		String gameVersion = gameProperties.getProperty("version");
		String platform = tryGetString(() -> args[0]);
		Instant utcTime = Instant.now();
		UUID id = UUID.randomUUID();
		String stackTrace = getStackTraceString(exception);
		return new CrashReport(gameName, gameVersion, platform, utcTime, id, stackTrace);
	}
	
	public static Properties getGameProperties(String propertiesFile) {
		Properties gameProperties = new Properties();

		try {
			String propertiesPath = Thread.currentThread().getContextClassLoader().getResource(propertiesFile).getPath();
			gameProperties.load(new FileInputStream(propertiesPath));
		} catch (Exception e) {
			System.err.println("Couldn't find or open properties file: " + propertiesFile + "(" + e.getMessage() + ")");
		}
		
		return gameProperties;
	}
	
	public static String tryGetString(Supplier<String> stringGetter) {
		String string = null;
		try {
			string = stringGetter.get();
		}
		catch (Exception e) {
			//swallow the exception, since we are already handling a game crash
		}
		return string;
	}
	
	public static String getStackTraceString(Exception exception) {
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    final Charset utf8 = StandardCharsets.UTF_8;
	    String data = null;
	    try (PrintStream ps = new PrintStream(baos, true, utf8)) {
	    	exception.printStackTrace(ps);
		    data = baos.toString(utf8);
	    }
	    return data;
	}
	
}
