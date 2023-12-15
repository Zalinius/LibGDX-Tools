package com.darzalgames.libgdxtools.math;

import com.badlogic.gdx.math.MathUtils;
import com.darzalgames.darzalcommon.random.IntegerSequence;

public class SingleNumberSequence implements IntegerSequence {
	
	private int number;
	
	public SingleNumberSequence() {
		this(0);
	}
	public SingleNumberSequence(int number) {
		this.number = number;
	}

	@Override
	public Integer next() {
		return number;
	}

	@Override
	public Integer next(int upperBound) {
		return MathUtils.clamp(number, Integer.MIN_VALUE, upperBound);
	}

	@Override
	public Integer next(int lowerBound, int upperBound) {
		return MathUtils.clamp(number, lowerBound, upperBound);
	}

	@Override
	public int nextInt() {
		return number;
	}

	@Override
	public int nextInt(int upperBound) {
		return MathUtils.clamp(number, Integer.MIN_VALUE, upperBound);
	}

	@Override
	public int nextInt(int lowerBound, int upperBound) {
		return MathUtils.clamp(number, lowerBound, upperBound);
	}

	@Override
	public boolean hasNext() {
		return true;
	}
	
	@Override
	public boolean flipCoin() {
		return true;
	}

}
