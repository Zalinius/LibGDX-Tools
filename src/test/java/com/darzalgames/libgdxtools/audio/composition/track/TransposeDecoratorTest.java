package com.darzalgames.libgdxtools.audio.composition.track;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.audio.AudioTestUtils;
import com.darzalgames.libgdxtools.audio.composition.NoteDuration;
import com.darzalgames.libgdxtools.audio.composition.Pitch;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;
import com.darzalgames.libgdxtools.audio.time.TimeSignature;

class TransposeDecoratorTest {
	
	private TransposeDecorator transposeDecorator;
	
	@BeforeEach
	void setup() {
		RepeatingTrack repeatingTrack = new RepeatingTrack(AudioTestUtils.testInstrument(), "test song", "test track");
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C4);
		transposeDecorator = new TransposeDecorator(repeatingTrack, "test song");
	}
	
	@Test
	void noTranspose_leavesNoteUnchanged() throws Exception {
		List<TimedMusicalInstant> timedMusicalInstants = transposeDecorator.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		
		assertEquals(1, timedMusicalInstants.size());
		assertEquals(Pitch.C4, timedMusicalInstants.get(0).musicalInstant.getNote());
	}
	
	@Test
	void setTransposerUp_transposesUpAWholeNote() throws Exception {
		transposeDecorator.setTransposer(p -> p.up());
		List<TimedMusicalInstant> timedMusicalInstants = transposeDecorator.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		
		assertEquals(1, timedMusicalInstants.size());
		assertEquals(Pitch.D4, timedMusicalInstants.get(0).musicalInstant.getNote());
	}
	
	@Test
	void setTransposerOctaveUp_transposesUpAWholeOctave() throws Exception {
		transposeDecorator.setTransposer(p -> p.octaveUp());
		List<TimedMusicalInstant> timedMusicalInstants = transposeDecorator.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		
		assertEquals(1, timedMusicalInstants.size());
		assertEquals(Pitch.C5, timedMusicalInstants.get(0).musicalInstant.getNote());
	}

}
