package com.darzalgames.libgdxtools.audio.engine;

import com.badlogic.gdx.math.Interpolation;

public class BPSController {
	
	private float targetBPS;
	
	private float lastBPS;
	private float transitionTime;
	private float timeInTransition;
	
	public BPSController(float initialBPS) {
		this.targetBPS = initialBPS;
		this.lastBPS = initialBPS;
		this.transitionTime = 0f;
		this.timeInTransition = 0f;
	}
	
	public synchronized void setTargetBPS(float newTargetBPS, float transitionTime) {
		this.lastBPS = this.targetBPS;
		this.targetBPS = newTargetBPS;
		this.transitionTime = transitionTime;
		this.timeInTransition = 0f;
	}
	
	public synchronized float updateAndGetBPS(float delta) {
		if(timeInTransition != transitionTime) {
			
			timeInTransition += delta;
			if(timeInTransition > transitionTime) {
				timeInTransition = transitionTime;
			}
			float interpolant = timeInTransition / transitionTime;
			return Interpolation.linear.apply(lastBPS, targetBPS, interpolant);
		}
		else {
			return targetBPS;
		}
	}
	
	public synchronized void resetBPS(float bps) {
		setTargetBPS(bps, 0);
	}

	public synchronized float getBPS() {
		return updateAndGetBPS(0);
	}

}
