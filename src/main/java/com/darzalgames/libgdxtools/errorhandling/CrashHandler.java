package com.darzalgames.libgdxtools.errorhandling;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.maingame.GameInfo;

public class CrashHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread thread, Throwable e) {
		String gameName = tryGetString(GameInfo::getGameName);
		String gameVersion = tryGetString(GameInfo::getGameVersion);
		String platform = tryGetString(() -> GameInfo.getGamePlatform().getPlatformName());
		Instant instant = Instant.now();
		UUID id = UUID.randomUUID();
		String stackTrace = getStackTrace(e);
		
		CrashReport crashReport = new CrashReport(gameName, gameVersion, platform, instant, id, stackTrace);
		String crashReportFileName = "crash-" + instant.toString() + ".err.txt";
		FileHandle crashReportFile = tryGetReportingFileHandle(crashReportFileName, gameName);
		String crashReportJson = crashReport.toJson();
		
		crashReportFile.writeString(crashReportJson, false);
		Gdx.app.error("CRASH", "crash report written to " + crashReportFile.file().getAbsolutePath());
		e.printStackTrace();
		throw new RuntimeException(e);
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
	
	public static String getStackTrace(Throwable e) {
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    final String utf8 = StandardCharsets.UTF_8.name();
	    String data = null;
	    try (PrintStream ps = new PrintStream(baos, true, utf8)) {
	    	e.printStackTrace(ps);
		    data = baos.toString(utf8);
	    } catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
	    return data;
	}
	
	public static FileHandle tryGetReportingFileHandle(String crashReportFileName, String gameName) {
		FileHandle crashReportFile = Gdx.files.local(crashReportFileName);
		
		try {
			String crashReportFileLocalPath = gameName + "/" + GameInfo.getSteamStrategy().getSteamID() + "/" + crashReportFileName;
			crashReportFile = GameInfo.getGamePlatform().getSaveFileLocation(crashReportFileLocalPath);
		} catch (Exception e) {
			//swallow the exception, since we are already handling a game crash
		}
		
		return crashReportFile;
	}
	
	
}
