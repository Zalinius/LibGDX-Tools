package com.darzalgames.libgdxtools.maingame;

/**
 * A convenient factory for making a Stage to use in tests
 */
public class DummyStageBestFactory {

	public static StageBest make() {
		return make(true);
	}

	public static StageBest make(boolean shouldDrawAndAct) {
		StageBest stage = new StageBest(new DummyViewport(), new DummyBatch()) {
			@Override
			protected boolean isInsideViewport(int screenX, int screenY) {
				// needed for touchDown/Up/etc tests
				return true;
			}
		};
		stage.setShouldDrawAndAct(shouldDrawAndAct);
		return stage;
	}

}
