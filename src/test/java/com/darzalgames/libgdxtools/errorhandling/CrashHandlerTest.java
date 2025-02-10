package com.darzalgames.libgdxtools.errorhandling;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.platform.LinuxGamePlatform;

public class CrashHandlerTest {

	@Test
	void handleUncaughtException() throws Exception {
		Instant instant = Instant.parse("2025-02-09T18:40:36.949177809Z");
		UUID uuid = UUID.randomUUID();
		try (MockedStatic<GameInfo> gameInfoMock = Mockito.mockStatic(GameInfo.class);
				MockedStatic<Instant> instantMock = Mockito.mockStatic(Instant.class);
				MockedStatic<UUID> uuidMock = Mockito.mockStatic(UUID.class);) {
			gameInfoMock.when(() -> GameInfo.getGameName()).thenReturn("name");
			gameInfoMock.when(() -> GameInfo.getGameVersion()).thenReturn("version");
			gameInfoMock.when(() -> GameInfo.getGamePlatform()).thenReturn(new LinuxGamePlatform());
			instantMock.when(() -> Instant.now()).thenReturn(instant);
			uuidMock.when(() -> UUID.randomUUID()).thenReturn(uuid);
			Files filesMock = Mockito.mock(Files.class);
			FileHandle fileHandleMock = Mockito.mock(FileHandle.class);
			when(fileHandleMock.file()).thenReturn(new File("."));
			when(filesMock.local("crash-2025-02-09T18:40:36.949177809Z.err.txt")).thenReturn(fileHandleMock);
			Gdx.files = filesMock;
			Application applicationMock = Mockito.mock(Application.class);
			Gdx.app = applicationMock;

			CrashHandler crashHandler = new CrashHandler();
			RuntimeException runtimeException = new RuntimeException("TestException");

			assertThrows(RuntimeException.class, () -> crashHandler.uncaughtException(null, runtimeException), runtimeException.getMessage());
			verify(fileHandleMock).writeString(anyString(), anyBoolean());
			verify(applicationMock).error(eq("CRASH"), contains("crash report written to "));
		}

	}

	@Test
	void tryGetString_returnsNullAndSwallowsExceptionWhenExceptionThrown() throws Exception {
		Supplier<String> supplier = () -> {
			throw new RuntimeException("lol");
		};

		assertDoesNotThrow(() -> CrashHandler.tryGetString(supplier));
		assertNull(CrashHandler.tryGetString(supplier));
	}

	@Test
	void tryGetString_returnsStringSuppliedWhenNoException() throws Exception {
		Supplier<String> supplier = () -> "testString";

		assertEquals("testString", CrashHandler.tryGetString(supplier));
	}

}
