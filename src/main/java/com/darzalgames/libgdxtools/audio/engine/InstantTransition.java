package com.darzalgames.libgdxtools.audio.engine;

public class InstantTransition extends FadeOutComeIn {
	
	public InstantTransition() {
		super(0);
	}
	
	@Override
	public float getAmplitude() {
		return 1f;
	}
	
	@Override
	public boolean isDone() {
		return true;
	}

}
