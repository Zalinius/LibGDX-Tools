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

public class AmplitudeDecoratorTest {
	
	private AmplitudeDecorator amplitudeDecorator;
	
	@BeforeEach
	void setup() {
		RepeatingTrack repeatingTrack = new RepeatingTrack(AudioTestUtils.testInstrument(), "song name", "track");
		repeatingTrack.setAmplitude(0.8f);
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C4);
		amplitudeDecorator = new AmplitudeDecorator(repeatingTrack, "song name");
	}
	
	@Test
	void noTranspose_leavesAmplitudeUnchanged() throws Exception {
		List<TimedMusicalInstant> timedMusicalInstants = amplitudeDecorator.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		
		assertEquals(1, timedMusicalInstants.size());
		assertEquals(0.8f, timedMusicalInstants.get(0).musicalInstant.getAmplitude());
	}
	
	@Test
	void setTransposeTo0_amplitudeIs0() throws Exception {
		amplitudeDecorator.setAmplitudeMultiplier(0);
		List<TimedMusicalInstant> timedMusicalInstants = amplitudeDecorator.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		
		assertEquals(1, timedMusicalInstants.size());
		assertEquals(0f, timedMusicalInstants.get(0).musicalInstant.getAmplitude());
	}
	
	@Test
	void setTransposerToHalf_halvesTrackAmplitude() throws Exception {
		amplitudeDecorator.setAmplitudeMultiplier(0.5f);
		List<TimedMusicalInstant> timedMusicalInstants = amplitudeDecorator.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		
		assertEquals(1, timedMusicalInstants.size());
		assertEquals(0.4f, timedMusicalInstants.get(0).musicalInstant.getAmplitude());
	}
	


}
