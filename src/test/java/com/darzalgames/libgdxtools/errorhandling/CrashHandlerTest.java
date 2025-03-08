package com.darzalgames.libgdxtools.errorhandling;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class CrashHandlerTest {

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
	
	@Test
	void getStackTraceArray() throws Exception {
		RuntimeException exception = new RuntimeException("Test Exception");

		String[] stackTraceArray = CrashHandler.getStackTraceArray(exception);
		
		assertEquals(exception.getStackTrace().length + 1, stackTraceArray.length);
		for (int i = 0; i < stackTraceArray.length; i++) {
			assertTrue(stackTraceArray[i].equals(stackTraceArray[i].trim()));
		}
	}

}
