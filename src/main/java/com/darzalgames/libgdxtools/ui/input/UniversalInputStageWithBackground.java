package com.darzalgames.libgdxtools.ui.input;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;

public class UniversalInputStageWithBackground extends UniversalInputStage {

		private Consumer<Stage> addBackgroundToStage;

		/**
		 * Creates a stage that will ensure that the supplied background is always present
		 */
		public UniversalInputStageWithBackground(final Viewport viewport, Consumer<Stage> addBackgroundToStage, InputStrategySwitcher inputStrategySwitcher, SpriteBatch spriteBatch) {
			super(viewport, inputStrategySwitcher, spriteBatch);

			this.addBackgroundToStage = addBackgroundToStage;
		}


		@Override
		public void clear() {
			super.clear();
			addBackgroundToStage.accept(this); // Always keep the background
		}

}
