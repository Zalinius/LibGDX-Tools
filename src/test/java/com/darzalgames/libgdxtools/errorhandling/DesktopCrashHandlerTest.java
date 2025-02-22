package com.darzalgames.libgdxtools.errorhandling;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class DesktopCrashHandlerTest {

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
