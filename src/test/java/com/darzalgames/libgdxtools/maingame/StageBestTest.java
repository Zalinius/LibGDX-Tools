package com.darzalgames.libgdxtools.maingame;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.scenes.scene2d.*;

class StageBestTest {

	@BeforeAll
	public static void setUp() {
		Gdx.graphics = new MockGraphics();
	}

	@BeforeEach
	public void clearStaticGdxGlBetweenTests() {
		Gdx.gl = null;
	}

	@Test
	void testConstructor_withInitializedGdxGl_throwsIllegalStateException() {
		TestViewport testViewport = new TestViewport();
		TestBatch testBatch = new TestBatch();
		Gdx.gl = new TestGl20();

		assertThrows(IllegalStateException.class, () -> new StageBest(testViewport, testBatch));
	}

	@Test
	void unfocusAll_clearsScrollFocus() {
		StageBest stageBest = makeTestStageBest(true);
		Actor listeningActor = new Actor();
		stageBest.setScrollFocus(listeningActor);

		stageBest.unfocusAll();

		assertEquals(null, stageBest.getScrollFocus());
	}

	@Test
	void unfocusAll_doesNotClearKeyboardFocus() {
		StageBest stageBest = makeTestStageBest(true);
		Actor listeningActor = new Actor();
		stageBest.setKeyboardFocus(listeningActor);

		stageBest.unfocusAll();

		assertEquals(listeningActor, stageBest.getKeyboardFocus());
	}

	@Test
	void unfocusAll_clearsTouchFocus() {
		StageBest stageBest = makeTestStageBest(true);
		AtomicBoolean receivedTouchDragged = new AtomicBoolean();
		Actor listeningActor = makeTestListeningActor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, float screenX, float screenY, int pointer, int button) {
				return button == 14;
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				receivedTouchDragged.set(true);
			}
		});
		stageBest.addActor(listeningActor);
		stageBest.setShouldDrawAndAct(true);
		stageBest.touchDown(0, 0, 1, 14); // this adds listeningActor as a touchFocus

		stageBest.unfocusAll();
		stageBest.touchDragged(0, 0, 1);

		assertFalse(receivedTouchDragged.get());
	}

	@Test
	void draw_withShouldDrawAndActSetToTrue_callsSuperDrawAndThrowsBecauseThisIsATestEnvironment() {
		StageBest stageBest = makeTestStageBest(true);

		assertThrows(Exception.class, () -> stageBest.draw());
	}

	@Test
	void draw_withShouldDrawAndActSetToFalse_doesNotCallSuperDrawAndSoDoesNotThrow() {
		StageBest stageBest = makeTestStageBest(false);

		assertDoesNotThrow(() -> stageBest.draw());
	}

	@Test
	void act_withShouldDrawAndActSetToTrue_callsSuperActAndThrowsInATestEnvironment() {
		StageBest stageBest = makeTestStageBest(true);

		assertThrows(Exception.class, () -> stageBest.act());
	}

	@Test
	void act_withShouldDrawAndActSetToFalse_doesNotCallSuperDrawAndSoDoesNotThrow() {
		StageBest stageBest = makeTestStageBest(false);

		assertDoesNotThrow(() -> stageBest.act());
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void setShouldDrawAndAct_setsIt(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(false);

		stageBest.setShouldDrawAndAct(shouldActAndDraw);

		assertEquals(shouldActAndDraw, stageBest.shouldDrawAndAct());
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void keyDown_whenShouldOrShouldNotDrawAndAct_returnsExpectedValue(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(shouldActAndDraw);
		Actor listeningActor = new Actor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				return keycode == 12;
			}
		});
		stageBest.setKeyboardFocus(listeningActor);

		assertEquals(shouldActAndDraw, stageBest.keyDown(12));
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void keyUp_whenShouldOrShouldNotDrawAndAct_returnsExpectedValue(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(shouldActAndDraw);
		Actor listeningActor = makeTestListeningActor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				return keycode == 12;
			}
		});
		stageBest.setKeyboardFocus(listeningActor);

		assertEquals(shouldActAndDraw, stageBest.keyUp(12));
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void keyTyped_whenShouldOrShouldNotDrawAndAct_returnsExpectedValue(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(shouldActAndDraw);
		Actor listeningActor = makeTestListeningActor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean keyTyped(InputEvent event, char character) {
				return character == '3';
			}
		});
		stageBest.setKeyboardFocus(listeningActor);

		assertEquals(shouldActAndDraw, stageBest.keyTyped('3'));
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void touchDown_whenShouldOrShouldNotDrawAndAct_returnsExpectedValue(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(shouldActAndDraw);
		Actor listeningActor = makeTestListeningActor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, float screenX, float screenY, int pointer, int button) {
				return button == 14;
			}
		});
		stageBest.addActor(listeningActor);

		assertEquals(shouldActAndDraw, stageBest.touchDown(0, 0, 1, 14));
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void touchUp_whenShouldOrShouldNotDrawAndAct_returnsExpectedValue(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(shouldActAndDraw);
		Actor listeningActor = makeTestListeningActor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, float screenX, float screenY, int pointer, int button) {
				return button == 14;
			}

			@Override
			public void touchUp(final InputEvent event, float screenX, float screenY, int pointer, int button) {
				// the stage itself returns the boolean
			}
		});
		stageBest.addActor(listeningActor);
		stageBest.setShouldDrawAndAct(true);
		stageBest.touchDown(0, 0, 1, 14);
		stageBest.setShouldDrawAndAct(shouldActAndDraw);

		assertEquals(shouldActAndDraw, stageBest.touchUp(0, 0, 1, 14));
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void touchCancelled_whenShouldOrShouldNotDrawAndAct_doesOrDoesNotCancelTheTouchFocus(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(shouldActAndDraw);
		AtomicBoolean receivedTouchCancelled = new AtomicBoolean();
		Actor listeningActor = makeTestListeningActor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, float screenX, float screenY, int pointer, int button) {
				return button == 14;
			}

			@Override
			public void touchUp(final InputEvent event, float screenX, float screenY, int pointer, int button) {
				receivedTouchCancelled.set(true);
			}
		});
		stageBest.addActor(listeningActor);
		stageBest.setShouldDrawAndAct(true);
		stageBest.touchDown(0, 0, 1, 14); // this adds listeningActor as a touchFocus
		stageBest.setShouldDrawAndAct(shouldActAndDraw);

		stageBest.touchCancelled(0, 0, 1, 14);

		assertEquals(shouldActAndDraw, receivedTouchCancelled.get());
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void touchDragged_whenShouldOrShouldNotDrawAndAct_doesOrDoesNotCancelTheTouchFocus(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(shouldActAndDraw);
		AtomicBoolean receivedTouchDragged = new AtomicBoolean();
		Actor listeningActor = makeTestListeningActor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, float screenX, float screenY, int pointer, int button) {
				return button == 14;
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				receivedTouchDragged.set(true);
			}
		});
		stageBest.addActor(listeningActor);
		stageBest.setShouldDrawAndAct(true);
		stageBest.touchDown(0, 0, 1, 14); // this adds listeningActor as a touchFocus
		stageBest.setShouldDrawAndAct(shouldActAndDraw);

		stageBest.touchDragged(0, 0, 1);

		assertEquals(shouldActAndDraw, receivedTouchDragged.get());
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void mouseMoved_whenShouldOrShouldNotDrawAndAct_returnsExpectedValue(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(shouldActAndDraw);
		Actor listeningActor = makeTestListeningActor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean mouseMoved(InputEvent event, float x, float y) {
				return x == 1 && y == 14;
			}
		});
		stageBest.addActor(listeningActor);

		assertEquals(shouldActAndDraw, stageBest.mouseMoved(1, 14));
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void scrolled_whenShouldOrShouldNotDrawAndAct_returnsExpectedValue(boolean shouldActAndDraw) {
		StageBest stageBest = makeTestStageBest(shouldActAndDraw);
		Actor listeningActor = makeTestListeningActor();
		listeningActor.addListener(new InputListener() {
			@Override
			public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
				return amountX == 1 && amountY == 14;
			}
		});
		stageBest.setScrollFocus(listeningActor);

		assertEquals(shouldActAndDraw, stageBest.scrolled(1, 14));
	}

	private static StageBest makeTestStageBest(boolean shouldDrawAndAct) {
		StageBest stage = new StageBest(new TestViewport(), new TestBatch()) {
			@Override
			protected boolean isInsideViewport(int screenX, int screenY) {
				// needed for touchDown/Up/etc tests
				return true;
			}
		};
		stage.setShouldDrawAndAct(shouldDrawAndAct);
		return stage;
	}

	private static Actor makeTestListeningActor() {
		return new Actor() {
			@Override
			public Actor hit(float x, float y, boolean touchable) {
				return this;
			}
		};
	}

	@Test
	void isInTouchableBranch_TouchableEnabledInParentGroup_returnsTrue() {
		Group group = new Group();
		Actor actor = new Actor();
		group.addActor(actor);

		group.setTouchable(Touchable.enabled);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableChildrenOnlyInParentGroup_returnsTrue() {
		Group group = new Group();
		Actor actor = new Actor();
		group.addActor(actor);

		group.setTouchable(Touchable.childrenOnly);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableDisabledInParentGroup_returnsFalse() {
		Group group = new Group();
		Actor actor = new Actor();
		group.addActor(actor);

		group.setTouchable(Touchable.disabled);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertFalse(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableEnabled_returnsTrue() {
		Actor actor = new Actor();

		actor.setTouchable(Touchable.enabled);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableDisabled_returnsFalse() {
		Actor actor = new Actor();

		actor.setTouchable(Touchable.disabled);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertFalse(isInTouchableBranch);
	}

	@Test
	void isInTouchableBranch_TouchableChildrenOnly_returnsTrueButTheActorItselfIsNotTouchable() {
		Actor actor = new Actor();

		actor.setTouchable(Touchable.childrenOnly);
		boolean isInTouchableBranch = StageBest.isInTouchableBranch(actor);

		assertTrue(isInTouchableBranch);
		assertFalse(actor.isTouchable());
	}
}
