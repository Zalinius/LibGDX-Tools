package com.darzalgames.libgdxtools.errorhandling;

import java.time.Instant;
import java.util.UUID;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class CrashReport {
	
	private final String gameName;
	private final String gameVersion;
	private final String platformName;

	private final String time;
	private final String id;
	
	private final String stackTrace;
	
	private final transient Instant timeInstant;
	
	public CrashReport(String gameName, String gameVersion, String platformName, Instant utcTime, UUID id, String stackTrace) {
		this.gameName = gameName;
		this.gameVersion = gameVersion;
		this.platformName = platformName;
		this.time = utcTime.toString();
		this.id = id.toString();
		this.stackTrace = stackTrace;
		
		this.timeInstant = utcTime;
	}
	
	public String toJson() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		
		return json.prettyPrint(this);
	}
	
	public String getGameName() {
		return gameName;
	}
	public String getGameVersion() {
		return gameVersion;
	}
	public String getPlatformName() {
		return platformName;
	}
	public String getTime() {
		return time.toString();
	}
	public String getId() {
		return id;
	}
	public String getStackTrace() {
		return stackTrace;
	}
	
	public Instant getInstant() {
		return timeInstant;
	}
}
