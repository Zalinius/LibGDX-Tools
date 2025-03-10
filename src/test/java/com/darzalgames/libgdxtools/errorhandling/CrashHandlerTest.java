package com.darzalgames.libgdxtools.errorhandling;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	void getMessageAndStackTraceArray_containsMessageInSlot0AndStacktraceFramesInRemainingSlots() throws Exception {
		RuntimeException exception = new RuntimeException("Test Exception");

		String[] stackTraceArray = CrashHandler.getMessageAndStackTraceArray(exception);

		StackTraceElement[] stackTraceFrames = exception.getStackTrace(); 
		assertEquals("java.lang.RuntimeException: Test Exception", stackTraceArray[0]);
		assertEquals(stackTraceFrames.length + 1, stackTraceArray.length);
		for (int i = 0; i < stackTraceFrames.length; i++) {
			assertTrue(stackTraceArray[i+1].contains(stackTraceFrames[i].toString()));
		}
		
	}
}
