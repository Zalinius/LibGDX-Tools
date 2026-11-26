package com.darzalgames.libgdxtools.trailer;

/**
 * A dummy trailer frame recorder to be used during the audio pass or when viewing and editing the trailer.
 * This DOES NOT capture a screenshot every frame, it records nothing!
 */
public class DummyTrailerFrameRecorder extends TrailerFrameRecorder {

	public DummyTrailerFrameRecorder() {
		super("");
	}

	@Override
	public void recordFrame() {
		// Do not record the frame
	}

	@Override
	protected void clearPreviousRecording() {
		// Do not clear the previously captured frames
	}

}
