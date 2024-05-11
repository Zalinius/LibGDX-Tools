package com.darzalgames.libgdxtools.audio.composition;

import com.darzalgames.libgdxtools.audio.amplitude.Envelope;
import com.darzalgames.libgdxtools.audio.synth.Synth;

public record Instrument(Synth synth, Envelope envelope) {

}
