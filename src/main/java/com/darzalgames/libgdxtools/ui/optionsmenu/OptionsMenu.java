package com.darzalgames.libgdxtools.ui.optionsmenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.darzalcommon.state.DoesNotPause;
import com.darzalgames.libgdxtools.MainGame;
import com.darzalgames.libgdxtools.i18n.TextSupplier;
import com.darzalgames.libgdxtools.ui.PopUp;
import com.darzalgames.libgdxtools.ui.input.InputPrioritizer;
import com.darzalgames.libgdxtools.ui.input.handler.SteamControllerManager;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.LabelMaker;
import com.darzalgames.libgdxtools.ui.input.scrollablemenu.PopUpMenu;


public abstract class OptionsMenu extends PopUpMenu implements DoesNotPause {

	protected KeyboardButton optionsButton;
	private final Supplier<KeyboardButton> makeWindowModeSelectBox;


	protected abstract void setUpBackground();
	protected abstract PopUp makeControlsPopUp();
	protected abstract String getGameVersion();
	protected abstract Collection<KeyboardButton> makeMiddleButtons();
	protected abstract List<KeyboardButton> makeTextOptionButtons(PopUpMenu innerMenu);
	
	protected OptionsMenu(Supplier<KeyboardButton> makeWindowModeSelectBox) {
		super(true);
		this.makeWindowModeSelectBox = makeWindowModeSelectBox;
		setBounds(0, 0, MainGame.getWidth(), MainGame.getHeight());
		defaults().padBottom(5);
	}
	
	public KeyboardButton getButton() {
		return optionsButton;
	}

	protected void toggleScreenVisibility(boolean show) {
		if (show) {
			InputPrioritizer.pauseIfNeeded();
		} else {
			hideThis();
		}
	}

	@Override
	public void regainFocus() {
		focusCurrent();
		optionsButton.setTouchable(Touchable.enabled);
	}

	@Override
	protected void setUpTable() {
		setUpBackground();

		padTop(7);
		padBottom(3);

		// ALL SELECTABLE MENU BUTTONS
		List<KeyboardButton> menuButtons = new ArrayList<>();

//		KeyboardSlider musicVolume = LabelMaker.getSlider(TextSupplier.getLine("music_option") + " ", newVolume -> { QuestGiverGame.music.setMusicVolume(newVolume); });
//		musicVolume.setSliderPosition(QuestGiverGame.music.getMusicVolume());
//		menuButtons.add(musicVolume);
//
//		KeyboardCheckbox focusMute = LabelMaker.getCheckbox(
//				TextSupplier.getLine("focus_sounds_setting", TextSupplier.getLine("focus_dont_mute")),
//				TextSupplier.getLine("focus_sounds_setting", TextSupplier.getLine("focus_do_mute")),
//				isChecked -> { QuestGiverGame.music.setShouldTemporarilyMute(isChecked); });
//		focusMute.initializeAsChecked(QuestGiverGame.music.getShouldTemporarilyMute());
//		menuButtons.add(focusMute);

		KeyboardButton reportBugButton = LabelMaker.getButton(
				TextSupplier.getLine("report_bug_message"),
				() -> {
					String form = "https://forms.gle/j1CjPH8xdJskiYe19";
					Gdx.net.openURI(form);
				});
		menuButtons.add(reportBugButton);
		
		KeyboardButton controlsButton = LabelMaker.getButton(TextSupplier.getLine("controls_message"), () -> {
			InputPrioritizer.claimPriority(makeControlsPopUp());
			SteamControllerManager.openControlsOverlay();
		});
		menuButtons.add(controlsButton);

		menuButtons.add(makeInnerTextButtonMenu());

		// Window mode select box
		KeyboardButton windowModeSelectBox = makeWindowModeSelectBox.get();
		menuButtons.add(windowModeSelectBox);
		
		menuButtons.addAll(makeMiddleButtons());

		// Back button
		KeyboardButton backButton = LabelMaker.getButton(TextSupplier.getLine("back_message"), () -> {toggleScreenVisibility(false);});
		menuButtons.add(backButton);

		menu.setAlignment(Align.center, Align.top);
		menu.replaceContents(menuButtons, backButton);
		add(menu.getView()).grow().top();

		// Set up the version tag to be in its own inner table, so that the actual buttons can still reach the bottom of the main table
		Table versionTable = new Table();
		versionTable.setFillParent(true);
		Label versionLabel = LabelMaker.getFlavorTextLabel(getGameVersion());
		versionLabel.setAlignment(Align.bottomRight);
		versionTable.setTouchable(Touchable.disabled);
		addActor(versionTable);
		versionTable.add(versionLabel).bottom().grow().padBottom(getPadBottom() + 4).padRight(getPadRight());
	}
	
	private KeyboardButton makeInnerTextButtonMenu() {
		InnerMenu innerMenu = new InnerMenu();
		List<KeyboardButton> options = new ArrayList<>();
		
		options.addAll(makeTextOptionButtons(innerMenu));
		
		innerMenu.replaceContents(options);
		return LabelMaker.getButton(TextSupplier.getLine(accessibilityOptionsKey), () -> { InputPrioritizer.claimPriority(innerMenu);});
	}
	
	protected final String accessibilityOptionsKey = "accessibility_options";

	@Override
	public void actWhilePaused(float delta) {
		act(delta);
	}
	
	private class InnerMenu extends PopUpMenu implements DoesNotPause {
		
		public InnerMenu() {
			super(true);
		}

		@Override
		public void actWhilePaused(float delta) {
			act(delta);
		}

		@Override
		protected void setUpTable() {
			setSize(250, 125);
	        NinePatchDrawable back = LabelMaker.getUIBorderedNine();
	        background(back);
	        LabelMaker.makeActorCentered(this);

			menu.setAlignment(Align.center, Align.top);
			add(menu.getView()).growX().top();
		}
		
		private void replaceContents(final List<KeyboardButton> newEntries) {
			menu.replaceContents(newEntries, LabelMaker.getButton(TextSupplier.getLine("back_message"), () -> { hideThis();}));
		}
	};
}

