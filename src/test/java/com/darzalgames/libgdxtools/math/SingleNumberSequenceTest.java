package com.darzalgames.libgdxtools.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.math.SingleNumberSequence;

class SingleNumberSequenceTest {

	private final int NUMBER = 4;
	
		@Test
		void nextInt_returnsSameNumber() throws Exception {
			SingleNumberSequence singleNumberSequence = new SingleNumberSequence(NUMBER);
			
			int result = singleNumberSequence.nextInt();
			
			assertEquals(NUMBER, result);
		}
		@Test
		void next_returnsSameNumber() throws Exception {
			SingleNumberSequence singleNumberSequence = new SingleNumberSequence(NUMBER);
			
			Integer result = singleNumberSequence.next();
			
			assertEquals(NUMBER, result);
		}
		

		@Test
		void nextInt_withUpperBound_returnsSameNumber() throws Exception {
			SingleNumberSequence singleNumberSequence = new SingleNumberSequence(NUMBER);
			
			int result = singleNumberSequence.next(NUMBER*2);
			
			assertEquals(NUMBER, result);
		}
		@Test
		void next_withUpperBound_returnsSameNumber() throws Exception {
			SingleNumberSequence singleNumberSequence = new SingleNumberSequence(NUMBER);
			
			Integer result = singleNumberSequence.next(NUMBER*2);
			
			assertEquals(NUMBER, result);
		}
		
		@Test
		void nextInt_withBothBounds_returnsSameNumber() throws Exception {
			SingleNumberSequence singleNumberSequence = new SingleNumberSequence(NUMBER);
			
			int result = singleNumberSequence.next(NUMBER/2, NUMBER*2);
			
			assertEquals(NUMBER, result);
		}
		@Test
		void next_withBothBounds_returnsSameNumber() throws Exception {
			SingleNumberSequence singleNumberSequence = new SingleNumberSequence(NUMBER);
			
			Integer result = singleNumberSequence.next(NUMBER/2, NUMBER*2);
			
			assertEquals(NUMBER, result);
		}
		
		@Test
		void hasNext_returnsTrue() throws Exception {
			SingleNumberSequence singleNumberSequence = new SingleNumberSequence(NUMBER);
			
			boolean result = singleNumberSequence.hasNext();
			
			assertTrue(result);
		}
		
		@Test
		void flipCoin_returnsTrue() throws Exception {
			SingleNumberSequence singleNumberSequence = new SingleNumberSequence(NUMBER);
			
			boolean result = singleNumberSequence.flipCoin();
			
			assertTrue(result);
		}
}
