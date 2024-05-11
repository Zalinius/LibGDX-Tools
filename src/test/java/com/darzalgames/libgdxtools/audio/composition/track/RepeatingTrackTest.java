package com.darzalgames.libgdxtools.audio.composition.track;

import static com.darzalgames.libgdxtools.audio.composition.NoteDuration.EIGHTH;
import static com.darzalgames.libgdxtools.audio.composition.NoteDuration.HALF;
import static com.darzalgames.libgdxtools.audio.composition.NoteDuration.QUARTER;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.A3;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.C4;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.D3;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.D4;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.E3;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.E4;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.F2;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.F3;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.F4;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.G3;
import static com.darzalgames.libgdxtools.audio.composition.Pitch.G4;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.darzalgames.darzalcommon.functional.Do;
import com.darzalgames.libgdxtools.audio.AudioTestUtils;
import com.darzalgames.libgdxtools.audio.composition.NoteDuration;
import com.darzalgames.libgdxtools.audio.composition.Pitch;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;
import com.darzalgames.libgdxtools.audio.time.TimeInstant;
import com.darzalgames.libgdxtools.audio.time.TimeSignature;

class RepeatingTrackTest {
	
	private RepeatingTrack repeatingTrack;
	
	@BeforeEach
	void setup() {
		repeatingTrack = new RepeatingTrack(AudioTestUtils.testInstrument(), "test song", "test track");
	}
	
	@Test
	void trackLengthInSixteenths_onEmptyTrack_is0() throws Exception {
		int sixteenths = repeatingTrack.trackLengthInSixteenths();
		
		assertEquals(0, sixteenths);
	}

	@Test
	void trackLengthInSixteenths_onTrackWithQuarterInstant_is4() throws Exception {
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C4);
		int sixteenths = repeatingTrack.trackLengthInSixteenths();
		
		assertEquals(4, sixteenths);
	}

	@Test
	void trackLengthInSixteenths_onTrackWithManyInstants_isCorrect() throws Exception {
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C4);
		repeatingTrack.addInstant(NoteDuration.EIGHTH, Pitch.C4);
		repeatingTrack.addInstant(NoteDuration.SIXTEENTH, Pitch.C4);
		int sixteenths = repeatingTrack.trackLengthInSixteenths();
		
		assertEquals(7, sixteenths);
	}

	@Test
	void trackLengthInSixteenths_onTrackWithManyInstantsAndSilences_isCorrect() throws Exception {
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C4);
		repeatingTrack.addInstant(NoteDuration.EIGHTH, Pitch.C4, Pitch.E4);
		repeatingTrack.addSilence(NoteDuration.HALF);
		repeatingTrack.addInstant(NoteDuration.SIXTEENTH, Pitch.C4);
		repeatingTrack.addSilence(NoteDuration.EIGHTH_DOT);
		int sixteenths = repeatingTrack.trackLengthInSixteenths();
		
		assertEquals(18, sixteenths);
	}
	
	@Test
	void getMusicalInstantsActiveNow_withEmptyTrack_throwsException() throws Exception {
		TimeInstant timeInstant = new TimeInstant(0);
		assertThrows(IllegalStateException.class, () -> repeatingTrack.getMusicalInstantsActiveNow(timeInstant));
	}

	@Test
	void getMusicalInstantsActiveNow_withSixteenthNote_returns1InstantAtStartTime() throws Exception {
		repeatingTrack.addInstant(NoteDuration.SIXTEENTH, Pitch.C4);

		List<TimedMusicalInstant> instantsAt0 = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 0));
		
		assertEquals(1, instantsAt0.size());
	}

	@Test
	void getMusicalInstantsActiveNow_withTwoSixteenthNote_returns1InstantsAtStartTimeAnd2Thereafter() throws Exception {
		repeatingTrack.addInstant(NoteDuration.SIXTEENTH, Pitch.C4);
		repeatingTrack.addInstant(NoteDuration.SIXTEENTH, Pitch.C5);

//		List<TimedMusicalInstant> instantsAt0 = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(0));
		List<TimedMusicalInstant> instantsAt1 = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(1));
		List<TimedMusicalInstant> instantsAt2 = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(2));
//		List<TimedMusicalInstant> instantsAt3 = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(3));
//		
//		assertEquals(2, instantsAt0.size());
		assertEquals(2, instantsAt1.size());
		assertEquals(2, instantsAt2.size());
//		assertEquals(2, instantsAt3.size());
	}
	
	@Test
	void getMusicalInstantsActiveNow_with4QuarterNotesAndDefaultRepetition_returnsRepeatedInstants() throws Exception {
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C1);
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C2);
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C3);
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C4);
		
		List<TimedMusicalInstant> instantsAt1Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 1));
		List<TimedMusicalInstant> instantsAt5Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 5));
		List<TimedMusicalInstant> instantsAt17Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 17));
		List<TimedMusicalInstant> instantsAt21Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 21));
		
		assertEquals(1, instantsAt1Sixteenth.size());
		assertEquals(Pitch.C1, instantsAt1Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt5Sixteenth.size());
		assertEquals(Pitch.C2, instantsAt5Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt17Sixteenth.size());
		assertEquals(Pitch.C1, instantsAt17Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt21Sixteenth.size());
		assertEquals(Pitch.C2, instantsAt21Sixteenth.get(0).musicalInstant.getNote());
	}

	@Test
	void getMusicalInstantsActiveNow_with4QuarterNotesAndSetRepetition_returnsRepeatedInstants() throws Exception {
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C1);
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C2);
		repeatingTrack.setRepetitionPoint();
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C3);
		repeatingTrack.addInstant(NoteDuration.QUARTER, Pitch.C4);
		
		List<TimedMusicalInstant> instantsAt1Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 1));
		List<TimedMusicalInstant> instantsAt5Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 5));
		List<TimedMusicalInstant> instantsAt17Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 17));
		List<TimedMusicalInstant> instantsAt21Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 21));
		List<TimedMusicalInstant> instantsAt161Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 161));
		List<TimedMusicalInstant> instantsAt165Sixteenth = repeatingTrack.getMusicalInstantsActiveNow(new TimeInstant(TimeSignature.TIME_4_4, 165));
		
		assertEquals(1, instantsAt1Sixteenth.size());
		assertEquals(0, instantsAt1Sixteenth.get(0).startingSixteenth);
		assertEquals(Pitch.C1, instantsAt1Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt5Sixteenth.size());
		assertEquals(4, instantsAt5Sixteenth.get(0).startingSixteenth);
		assertEquals(Pitch.C2, instantsAt5Sixteenth.get(0).musicalInstant.getNote());

		assertEquals(1, instantsAt17Sixteenth.size());
		assertEquals(Pitch.C3, instantsAt17Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(16, instantsAt17Sixteenth.get(0).startingSixteenth);
		assertEquals(1, instantsAt21Sixteenth.size());
		assertEquals(Pitch.C4, instantsAt21Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(20, instantsAt21Sixteenth.get(0).startingSixteenth);

		assertEquals(1, instantsAt161Sixteenth.size());
		assertEquals(160, instantsAt161Sixteenth.get(0).startingSixteenth);
		assertEquals(Pitch.C3, instantsAt161Sixteenth.get(0).musicalInstant.getNote());
		assertEquals(1, instantsAt165Sixteenth.size());
		assertEquals(164, instantsAt165Sixteenth.get(0).startingSixteenth);
		assertEquals(Pitch.C4, instantsAt165Sixteenth.get(0).musicalInstant.getNote());
	}
	
	@Test
	void slimeRhythm_returnsNoNotesEarly() throws Exception {
		RepeatingTrack rhythm = new RepeatingTrack(AudioTestUtils.testInstrument(), "test song", "test track");
		Do.xTimes(20, () -> rhythm.addSilence(NoteDuration.QUARTER));
		rhythm.setRepetitionPoint();

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
	
	@ParameterizedTest
	@MethodSource("argumentsForSong")
	void repetitionTest(int sixteenth, Pitch pitch) throws Exception {
		RepeatingTrack main = new RepeatingTrack(AudioTestUtils.testInstrument(), "test song", "test track");
		
		//Intro
		main.addInstant(HALF, C4);
		main.addInstant(QUARTER, D4);
		main.addInstant(QUARTER, G4);
		main.addInstant(QUARTER, F4);
		main.addInstant(QUARTER, E4);
		main.addInstant(HALF, C4);
		
		main.setRepetitionPoint();

		//main bit
		main.addInstant(QUARTER, C4);
		main.addSilence(QUARTER);
		main.addInstant(QUARTER, D4);
		main.addInstant(QUARTER, E4);
		main.addInstant(QUARTER, F4);
		main.addInstant(QUARTER, E4);
		main.addInstant(QUARTER, D4);
		main.addSilence(QUARTER);

		List<TimedMusicalInstant> instantsActive = main.getMusicalInstantsActiveNow(new TimeInstant(sixteenth));
		
		assertEquals(1, instantsActive.size());
		assertEquals(pitch, instantsActive.get(0).musicalInstant.getNote());
		assertEquals(sixteenth-1, instantsActive.get(0).startingSixteenth);

	}
	
	private static Stream<Arguments> argumentsForSong(){
		return Stream.of(
				// sixteenth, note
				//intro
				Arguments.of(1, Pitch.C4),
				Arguments.of(9, Pitch.D4),
				Arguments.of(13, Pitch.G4),
				Arguments.of(17, Pitch.F4),
				Arguments.of(21, Pitch.E4),
				Arguments.of(25, Pitch.C4),

				//loop 0
				Arguments.of(33, Pitch.C4),
				Arguments.of(37, Pitch.NONE),
				Arguments.of(41, Pitch.D4),
				Arguments.of(45, Pitch.E4),
				Arguments.of(49, Pitch.F4),
				Arguments.of(53, Pitch.E4),
				Arguments.of(57, Pitch.D4),
				Arguments.of(61, Pitch.NONE),
				
				//loop 1
				Arguments.of(65, Pitch.C4),
				Arguments.of(69, Pitch.NONE),
				Arguments.of(73, Pitch.D4),
				Arguments.of(77, Pitch.E4),
				Arguments.of(81, Pitch.F4),
				Arguments.of(85, Pitch.E4),
				Arguments.of(89, Pitch.D4),
				Arguments.of(93, Pitch.NONE),
				
				//loop 2
				Arguments.of(97, Pitch.C4),
				Arguments.of(101, Pitch.NONE),
				Arguments.of(105, Pitch.D4),
				Arguments.of(109, Pitch.E4),
				Arguments.of(113, Pitch.F4),
				Arguments.of(117, Pitch.E4),
				Arguments.of(121, Pitch.D4),
				Arguments.of(125, Pitch.NONE)
				
				//				Arguments.of(33, Pitch.G2),
//				Arguments.of(37, Pitch.B2),
//				Arguments.of(41, Pitch.C3),
//				Arguments.of(45, Pitch.D3),
//
//				Arguments.of(49, Pitch.G2),
//				Arguments.of(53, Pitch.B2),
//				Arguments.of(57, Pitch.C3),
//				Arguments.of(61, Pitch.D3)
				
				);
	}
	
	
	@Test
	void getLengthInBeats_getsLength() throws Exception {
		RepeatingTrack main = new RepeatingTrack(AudioTestUtils.testInstrument(), "test song", "test track");

		//INTRO
		main.addInstant(QUARTER, F3);
		main.addInstant(QUARTER, G3);
		main.addInstant(QUARTER, A3);
		main.addInstant(HALF, G3, C4);
		
		main.addInstant(QUARTER, F4);
		main.addInstant(QUARTER, E4);
		main.addInstant(QUARTER, D4);
		main.addInstant(HALF, C4, E4);
		
		main.addInstant(QUARTER, D4);
		main.addInstant(QUARTER, C4);
		main.addInstant(QUARTER, A3);
		main.addInstant(HALF, E3, G3);

		main.addInstant(QUARTER, C4);
		main.addInstant(QUARTER, A3);
		main.addInstant(QUARTER, G3);
		main.addInstant(HALF, D3, F3);
		
		assertEquals(20, main.lengthInBeats());
		assertEquals(80, main.trackLengthInSixteenths());

	}




}
