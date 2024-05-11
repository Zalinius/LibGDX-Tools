package com.darzalgames.libgdxtools.audio.synth;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SynthTest {
	
	@Test
	void f_throwsIllegalArgumentExceptionOutsideDomain() throws Exception {
		Synth synth = new Synth(x -> x);
		
		assertThrows(IllegalArgumentException.class, () -> synth.f(-0.1f));
		assertDoesNotThrow(() ->  synth.f(0.0f));
		assertDoesNotThrow(() ->  synth.f(0.5f));
		assertDoesNotThrow(() ->  synth.f(1.0f));
		assertThrows(IllegalArgumentException.class, () -> synth.f( 1.1f));
	}

}
