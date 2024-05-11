package com.darzalgames.libgdxtools.audio.engine.sound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.darzalgames.libgdxtools.audio.composition.NoteDuration;
import com.darzalgames.libgdxtools.audio.engine.music.MusicalInstant;
import com.darzalgames.libgdxtools.audio.engine.music.TimedMusicalInstant;

public class SimpleSoundMakerImpl implements SimpleSoundMaker {

	@Override
	public List<TimedSimpleSound> makeSimpleSounds(List<TimedMusicalInstant> musicalInstants, float bps, float sixteenthProgress, float currentTime) {
		
		List<TimedSimpleSound> timedSimpleSounds = new ArrayList<>();
		
		for (Iterator<TimedMusicalInstant> iterator = musicalInstants.iterator(); iterator.hasNext();) {
			TimedMusicalInstant timedMusicalInstant = iterator.next();
			MusicalInstant musicalInstant = timedMusicalInstant.musicalInstant;
			
			float simpleSoundStartTime = computeStartTime(timedMusicalInstant.startingSixteenth, sixteenthProgress, currentTime, bps);
			
			SimpleSound simpleSound = new SimpleSound(musicalInstant.getSynth(), musicalInstant.getDuration().getDuration() / bps, musicalInstant.getAmplitude(), musicalInstant.getNote().getFrequency(), musicalInstant.getEnvelope(), musicalInstant.getId());
			
			TimedSimpleSound timedSimpleSound = new TimedSimpleSound(simpleSoundStartTime, simpleSound);
			timedSimpleSounds.add(timedSimpleSound);
		}
		
		return timedSimpleSounds;		
	}
	
	public static float computeStartTime(float startingSixteenth, float sixteenthProgress, float currentTime, float bps) {
		return currentTime - (sixteenthProgress - startingSixteenth) / NoteDuration.QUARTER.getDurationInSixteenths() / bps;
	}

}
