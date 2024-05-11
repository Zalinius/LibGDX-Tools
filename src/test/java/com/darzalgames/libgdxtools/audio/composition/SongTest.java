package com.darzalgames.libgdxtools.audio.composition;

import static com.darzalgames.libgdxtools.audio.composition.NoteDuration.EIGHTH;
import static com.darzalgames.libgdxtools.audio.composition.NoteDuration.QUARTER;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.B2;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.C3;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.D3;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.F2;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.G2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.darzalgames.libgdxtools.audio.AudioTestUtils;
import com.darzalgames.libgdxtools.audio.composition.track.RepeatingTrack;
import com.darzalgames.libgdxtools.audio.composition.track.SixteenthRhythmTrack;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;
import com.darzalgames.libgdxtools.audio.time.TimeSignature;

class SongTest {
	
	private Song song;
	private RepeatingTrack repeatingTrack;
	private SixteenthRhythmTrack rhythmTrack;
	
	@BeforeEach
	void setup(){
		song = new Song("test song", null) {	};
		repeatingTrack = new RepeatingTrack(AudioTestUtils.testInstrument(), song.getName() , "test track");
		rhythmTrack = new SixteenthRhythmTrack(song.getName(), "test rhythm track", AudioTestUtils.testInstrument());
	}
	
	@Test
	void getName_returnsName() throws Exception {
		assertEquals("test song", song.getName());
	}
	
	@Test
	void getMusicalInstantsActiveNow_rythm_returns2AtAllTimes() throws Exception {
		song.addTrack(rhythmTrack);
		rhythmTrack.setRhythm(SixteenthRhythmTrack.CONSTANT_FAST);
		
		List<TimedMusicalInstant> instantsAt0 = song.getMusicalInstantsActiveNow(new TimeInstant(0));
		List<TimedMusicalInstant> instantsAt1 = song.getMusicalInstantsActiveNow(new TimeInstant(1));
		List<TimedMusicalInstant> instantsAt2 = song.getMusicalInstantsActiveNow(new TimeInstant(2));
		List<TimedMusicalInstant> instantsAt3 = song.getMusicalInstantsActiveNow(new TimeInstant(3));
		List<TimedMusicalInstant> instantsAt4 = song.getMusicalInstantsActiveNow(new TimeInstant(4));
		
		assertEquals(2, instantsAt0.size());
		assertEquals(2, instantsAt1.size());
		assertEquals(2, instantsAt2.size());
		assertEquals(2, instantsAt3.size());
		assertEquals(2, instantsAt4.size());
		assertEquals(4, song.songLengthInSixteenths());
	}
	
	@Test
	void getMusicalInstantsActiveNow_with4QuarterNotesAndSetRepetition_returnsRepeatedInstants() throws Exception {
		song.addTrack(repeatingTrack);
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C1);
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C2);
		repeatingTrack.setRepetitionPoint();
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C3);
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C4);
		
		List<TimedMusicalInstant> instantsAt1Sixteenth = song.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 1));
		List<TimedMusicalInstant> instantsAt5Sixteenth = song.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 5));
		List<TimedMusicalInstant> instantsAt17Sixteenth = song.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 17));
		List<TimedMusicalInstant> instantsAt21Sixteenth = song.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 21));
		List<TimedMusicalInstant> instantsAt161Sixteenth = song.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 161));
		List<TimedMusicalInstant> instantsAt165Sixteenth = song.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 165));
		
		assertEquals(1, instantsAt1Sixteenth.size());
		assertEquals(Pitch.C1, instantsAt1Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt5Sixteenth.size());
		assertEquals(Pitch.C2, instantsAt5Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt17Sixteenth.size());
		assertEquals(Pitch.C3, instantsAt17Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt21Sixteenth.size());
		assertEquals(Pitch.C4, instantsAt21Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt161Sixteenth.size());
		assertEquals(Pitch.C3, instantsAt161Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt165Sixteenth.size());
		assertEquals(Pitch.C4, instantsAt165Sixteenth.get(0).musicalInstant.getNote());
	}
	
	@ParameterizedTest
	@MethodSource("argumentsForSong")
	void testSongWith2Tracks_returnsInstantsWithCorrectStartTimes(int sixteenth, Pitch pitch) throws Exception {
		song.addTrack(repeatingTrack);
		repeatingTrack.addInstant(QUARTER, G2);
		repeatingTrack.addInstant(QUARTER, B2);
		repeatingTrack.addInstant(QUARTER, C3);
		repeatingTrack.addInstant(QUARTER, D3);
		
		List<TimedMusicalInstant> instantsActive = song.getMusicalInstantsActiveNow(new TimeInstant(sixteenth));
		
		assertEquals(1, instantsActive.size());
		assertEquals(pitch, instantsActive.get(0).musicalInstant.getNote());
		assertEquals(sixteenth-1, instantsActive.get(0).startingSixteenth);
	}
	
	private static Stream<Arguments> argumentsForSong(){
		return Stream.of(
				// sixteenth, note
				Arguments.of(1, Pitch.G2),
				Arguments.of(5, Pitch.B2),
				Arguments.of(9, Pitch.C3),
				Arguments.of(13, Pitch.D3),

				Arguments.of(17, Pitch.G2),
				Arguments.of(21, Pitch.B2),
				Arguments.of(25, Pitch.C3),
				Arguments.of(29, Pitch.D3),

				Arguments.of(33, Pitch.G2),
				Arguments.of(37, Pitch.B2),
				Arguments.of(41, Pitch.C3),
				Arguments.of(45, Pitch.D3),

				Arguments.of(49, Pitch.G2),
				Arguments.of(53, Pitch.B2),
				Arguments.of(57, Pitch.C3),
				Arguments.of(61, Pitch.D3)
				
				);
	}
	
	@Test
	void testName() throws Exception {
		Song song = new Song("test", null) {};
		RepeatingTrack rhythm = song.createRepeatingTrack(AudioTestUtils.testInstrument(), "test track", 20);
		rhythm.setAmplitude(.4f);
		rhythm.addInstant(EIGHTH, F2);
		rhythm.addSilence(EIGHTH);
		rhythm.addInstant(EIGHTH, F2);
		rhythm.addInstant(EIGHTH, F2);
		rhythm.addSilence(EIGHTH);

		rhythm.addInstant(EIGHTH, F2);
		rhythm.addInstant(EIGHTH, F2);
		rhythm.addSilence(EIGHTH);
		rhythm.addInstant(EIGHTH, F2);
		rhythm.addSilence(EIGHTH);
		
		for(int i = 0; i != 20*4; i++) {
			List<TimedMusicalInstant> instants = rhythm.getMusicalInstantsActiveNow(new TimeInstant(i));
			
			assertTrue(instants.size() < 3);
		}

	}
	
	

}
