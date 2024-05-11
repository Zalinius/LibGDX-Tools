package com.darzalgames.libgdxtools.audio.engine;

import com.badlogic.gdx.math.MathUtils;

public class FadeOutComeIn {
	
	private final float fadeOutTime;
	private float age;
	
	public FadeOutComeIn(float fadeOutTime) {
		this.age = 0;
		this.fadeOutTime = fadeOutTime;
	}
	
	public void update(float dt) {
		this.age += dt;
	}

	
	public boolean isDone() {
		return age >= fadeOutTime;
	}
	
	public float getAmplitude() {
		float time = age;
		float value = 0;
		if(!isDone()) {
			value = -(time/fadeOutTime) + 1;
		}
		else {
			value = 1f;
		}
		
		value = MathUtils.clamp(value, 0, 1);
		return value;
	}
	
}
