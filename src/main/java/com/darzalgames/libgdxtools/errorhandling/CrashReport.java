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
	
	private final String[] stackTrace;
	
	private final transient Instant timeInstant;
	
	public CrashReport(String gameName, String gameVersion, String platformName, Instant utcTime, UUID id, String[] stackTrace) {
		this.gameName = gameName;
		this.gameVersion = gameVersion;
		this.platformName = platformName;
		this.time = utcTime.toString();
		this.id = id.toString();
		this.stackTrace = stackTrace;
		
		this.timeInstant = utcTime;
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
		return time;
	}
	public String getId() {
		return id;
	}
	public String[] getStackTrace() {
		return stackTrace;
	}
	
	public Instant getInstant() {
		return timeInstant;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Game    : ").append(gameName).append('\n');
		sb.append("Version : ").append(gameVersion).append('\n');
		sb.append("Platform: ").append(platformName).append('\n');
		sb.append("Time    : ").append(time).append('\n');
		sb.append("ID      : ").append(id).append('\n');
		
		sb.append("Trace   : ").append(stackTrace[0]).append('\n');
		for (int i = 1; i < stackTrace.length; i++) {
			sb.append("            ").append(stackTrace[i]).append("\n");
		}
		
		return sb.toString();
	}
	
	public String toJson() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		
		return json.prettyPrint(this);
	}
	

	
}
