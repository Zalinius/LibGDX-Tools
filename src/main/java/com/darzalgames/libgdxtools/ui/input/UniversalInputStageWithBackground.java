package com.darzalgames.libgdxtools.ui.input;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darzalgames.libgdxtools.ui.input.inputpriority.ScrollingManager;
import com.darzalgames.libgdxtools.ui.input.strategy.InputStrategySwitcher;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;

public class UniversalInputStageWithBackground extends UniversalInputStage {

		private Image background;

		/**
		 * Creates a stage that will ensure that the supplied background texture is always present
		 * @param viewport
		 * @param backgroundTex
		 */
		public UniversalInputStageWithBackground(final Viewport viewport, Texture backgroundTex, InputStrategySwitcher inputStrategySwitcher, ScrollingManager scrollingManager) {
			super(viewport, inputStrategySwitcher, scrollingManager);

			background = new Image(backgroundTex);
			UserInterfaceFactory.makeActorCentered(background);
			background.setTouchable(Touchable.disabled);
			addActor(background);
		}

		@Override
		public void clear() {
			super.clear();
			addActor(background); // Always keep the background, just in case
		}

}
