package com.darzalgames.libgdxtools.audio.engine.music;

import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.composition.NoteDuration;
import com.darzalgames.libgdxtools.audio.composition.Pitch;
import com.darzalgames.libgdxtools.audio.synth.Synth;

public class MusicalInstant {
	
	private final NoteDuration duration;
	private final Pitch note;
	private final Synth synth;
	private final Envelope envelope;
	private final float amplitude;
	private final String id;
	
	public MusicalInstant(NoteDuration duration, Pitch note, Synth synth, Envelope envelope, float amplitude, String id) {
		this.duration = duration;
		this.note = note;
		this.synth = synth;
		this.envelope = envelope;
		this.amplitude = amplitude;
		this.id = id;
	}

	public NoteDuration getDuration() {
		return duration;
	}

	public Pitch getNote() {
		return note;
	}

	public Synth getSynth() {
		return synth;
	}
	
	public Envelope getEnvelope() {
		return envelope;
	}
	
	public float getAmplitude() {
		return amplitude;
	}
	
	public String getId() {
		return id;
	}
}
