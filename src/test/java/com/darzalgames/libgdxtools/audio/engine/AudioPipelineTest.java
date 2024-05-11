package com.darzalgames.libgdxtools.audio.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.darzalgames.libgdxtools.audio.AudioTestUtils;
import com.darzalgames.libgdxtools.audio.composition.NoteDuration;
import com.darzalgames.libgdxtools.audio.composition.Pitch;
import com.darzalgames.libgdxtools.audio.composition.Song;
import com.darzalgames.libgdxtools.audio.composition.track.RepeatingTrack;
import com.darzalgames.libgdxtools.audio.time.BPSAcceptor;

class AudioPipelineTest {
	
	//TODO make  IT test?
	
	@Test
	void audioPipeline_producesContinousWaveSamples() throws Exception {
		float[] writtenSamples = new float[2*AudioConstants.SAMPLES_PER_STEP];
		MusicThread musicThread = new MusicThread(AudioTestUtils.soundPreferenceDummy(), AudioTestUtils.audioDeviceSpy(writtenSamples));
		musicThread.queueSong(oneNoteSong(musicThread));
		
		musicThread.processMusicStep();
		musicThread.processMusicStep();
		
		float[] firstSample = Arrays.copyOfRange(writtenSamples, 0, AudioConstants.SAMPLES_PER_STEP);
		float[] secondSample = Arrays.copyOfRange(writtenSamples, AudioConstants.SAMPLES_PER_STEP, 2*AudioConstants.SAMPLES_PER_STEP);
		assertEquals(firstSample[firstSample.length-1], secondSample[0], 0.1f);
	}

	@Test
	void audioPipeline_whenBPSChanges_producesContinousWaveSamples() throws Exception {
		float[] writtenSamples = new float[2*AudioConstants.SAMPLES_PER_STEP];
		MusicThread musicThread = new MusicThread(AudioTestUtils.soundPreferenceDummy(), AudioTestUtils.audioDeviceSpy(writtenSamples));
		musicThread.queueSong(oneNoteSong(musicThread));
		
		musicThread.processMusicStep();
		musicThread.requestChangeBPS(1.5f*musicThread.getBPS(), 0);
		musicThread.processMusicStep();
		
		float[] firstSample = Arrays.copyOfRange(writtenSamples, 0, AudioConstants.SAMPLES_PER_STEP);
		float[] secondSample = Arrays.copyOfRange(writtenSamples, AudioConstants.SAMPLES_PER_STEP, 2*AudioConstants.SAMPLES_PER_STEP);
		assertEquals(firstSample[firstSample.length-1], secondSample[0], 0.1f);
	}
	
	private static Song oneNoteSong(BPSAcceptor bpsAcceptor) {
		Song questGiverSong = new Song("Test song", bpsAcceptor) {
		};
		RepeatingTrack track = new RepeatingTrack(AudioTestUtils.testInstrument(), "song name", "track");
		questGiverSong.addTrack(track);
		track.addInstant(NoteDuration.WHOLE, Pitch.C4);
		return questGiverSong;
	}

	private static Song oneNoteSongWithBPSChange(BPSAcceptor bpsAcceptor) {
		Song questGiverSong = new Song("Test song", bpsAcceptor) {
		};
		RepeatingTrack track = new RepeatingTrack(AudioTestUtils.testInstrument(), "bps change song name", "track");
		questGiverSong.addTrack(track);
		track.addInstant(NoteDuration.WHOLE, Pitch.C4);
		return questGiverSong;
	}
}
