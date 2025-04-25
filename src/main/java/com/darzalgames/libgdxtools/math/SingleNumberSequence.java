package com.darzalgames.libgdxtools.math;

import com.darzalgames.darzalcommon.random.IntegerSequence;

public class SingleNumberSequence implements IntegerSequence {

	private final int number;

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
		return Math.clamp(number, Integer.MIN_VALUE, upperBound - 1);
	}

	@Override
	public Integer next(int lowerBound, int upperBound) {
		return Math.clamp(number, lowerBound, upperBound - 1);
	}

	@Override
	public boolean hasNext() {
		return true;
	}

}
