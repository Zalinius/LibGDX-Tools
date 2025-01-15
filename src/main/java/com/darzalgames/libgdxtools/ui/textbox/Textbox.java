package com.darzalgames.libgdxtools.ui.textbox;

import java.util.*;
import java.util.function.Supplier;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumerWrapper;
import com.darzalgames.libgdxtools.ui.input.InputPriorityManager;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.UserInterfaceFactory;
import com.github.tommyettinger.textra.TypingLabel;

public abstract class Textbox extends Group implements InputConsumerWrapper {

	private final Supplier<Boolean> shouldDoInstantTextSupplier;

	private Iterator<TextboxData> lines;
	protected DialogChoiceData dialogChoiceData;
	private TextboxData previousData;
	protected TextboxData currentData;

	protected Table table;
	protected TypingLabel text;
	protected Actor portrait;
	private Label nameTag;

	private float totalBoxGrowTime = 0.2f;

	public Textbox(Collection<TextboxData> lines, Supplier<Boolean> shouldDoInstantTextSupplier) {
		this.shouldDoInstantTextSupplier = shouldDoInstantTextSupplier;
		this.setBounds(0, 0, GameInfo.getWidth(), GameInfo.getHeight());
		table = new Table();

		NinePatchDrawable background = UserInterfaceFactory.getUIBorderedNine();
		table.setBackground(background);
		table.setOrigin(Align.center);
		table.setTransform(true);
		setBoxSizeAndPosition();
		this.addActor(table);
		table.setScale(0);
		addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				consumeKeyInput(Input.ACCEPT);
				return true;
			}
		});
	}

	protected void setBoxSizeAndPosition() {
		int ySpacing = 10; // magic! Put it where you want, is all. Currently it hides the stuff on the desk
		int textBoxWidth = 250;
		int textBoxHeight = 62;
		table.setPosition(GameInfo.getWidth()/2 - textBoxWidth / 2, ySpacing);
		table.setSize(textBoxWidth, textBoxHeight);
	}

	private void updatePortrait() {
		table.removeActor(nameTag);
		this.removeActor(portrait);
		if (currentData.getSpeaker() != Speaker.BLANK) {
			makePortrait();

			nameTag = UserInterfaceFactory.getLabelWithBackground(currentData.getSpeakerName());
			nameTag.layout();
			nameTag.setPosition(4, table.getHeight()-2);
			table.addActor(nameTag);
		}
	}

	private void updateLine() {
		table.clear();
		if (nameTag != null && currentData.getSpeaker() != Speaker.BLANK) {
			table.addActor(nameTag);
		}
		String currentMessage = currentData.getMessage();
		text = getTypingLabel(currentMessage);
		text.setAlignment(Align.topLeft);
		table.add(text).grow();
		table.addActor(getArrowLabel(lines.hasNext()));
		table.toFront();

		dialogChoiceData = currentData.getDialogueChoiceData();

		if (!currentMessage.isEmpty()) {
			table.setScale(1);
			table.layout();
			if (shouldDoInstantTextSupplier.get()) {
				skipTextSpool();
			}
		}
	}


	// the arrow is manually positioned in the bottom right corner of the table so that it can overlap with dialogue that reaches the bottom line
	// it is not added to a cell because the table does not like overlapping cells and will instead overflow, no bueno
	private Label getArrowLabel(final boolean toContinue) {
		Label arrow = toContinue ? getFlashingContinueArrowLabel() : getFlashingArrowLabel();
		arrow.setPosition(table.getWidth() - table.getPadRight() - arrow.getWidth(), table.getPadBottom()); // this position is relative to the table's 0,0
		return arrow;
	}

	@Override
	public void consumeKeyInput(final Input input) {
		if (table.getActions().isEmpty() && (input == Input.ACCEPT || TextboxFastForwarder.isSkipping())) {
			if (text != null && !text.hasEnded()) {
				// If the text in the text box is currently spooling, we cancel that and show the full message right away
				skipTextSpool();
			} else  {
				if (dialogChoiceData != null) {
					new DialogueChoicePopUp(dialogChoiceData);
				} else {
					showNextLineOrReleasePriority();
				}
			} 
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (TextboxFastForwarder.isSkipping()) {
			consumeKeyInput(Input.SKIP);
		}
	}

	@Override
	public void gainFocus() {
		previousData = null;
		showNextLineOrReleasePriority();
	}

	@Override
	public void regainFocus() {
		TextboxFastForwarder.setInteractable(true);
		if (dialogChoiceData != null) {
			dialogChoiceData = null;
			showNextLineOrReleasePriority();
		}
	}

	@Override
	public void loseFocus() {
		TextboxFastForwarder.setInteractable(false);
		TextboxFastForwarder.setSkipping(false);
	}

	protected void showNextLineOrReleasePriority() {
		table.clearChildren();
		if (!lines.hasNext()) {
			if (currentData == null //e.g. a MIDNIGHT textbox with no talking in it, which happens most days
					|| currentData.getMessage().isBlank()) {
				releasePriority();
			} else {
				table.addAction(new InstantSequenceAction(
						getShrinkAction(),
						new RunnableActionBest(this::releasePriority)
						));
			}
		} else {
			boolean isFirstLine = currentData == null;
			currentData = lines.next();
			if (!lines.hasNext()) {
				//bail now if the final line was invalid
				releasePriority();
			} else {
				Speaker currentSpeaker = currentData.getSpeaker();
				Speaker previousSpeaker = null;
				if (previousData != null) {
					previousSpeaker = previousData.getSpeaker();
				}

				if ((previousData == null)
						|| (previousData != null && previousSpeaker != currentSpeaker)) {
					speakerChanged(currentSpeaker);
				}
				previousData = currentData;

				boolean isBlank = currentSpeaker.equals(Speaker.BLANK) && currentData.getMessage().isBlank();
				boolean isLastLine = isBlank && !lines.hasNext();
				if (TextboxFastForwarder.isSkipping() || (isFirstLine && isBlank)) {
					updateLine();
					updatePortrait();
				} else if (isFirstLine) {
					table.addAction(new InstantSequenceAction(
							new RunnableActionBest(this::updatePortrait),
							getGrowAction(),
							new RunnableActionBest(this::updateLine)));
				} else if (isLastLine || isBlank) {
					table.addAction(new InstantSequenceAction(
							getShrinkAction(),
							new RunnableActionBest(this::updatePortrait),
							new RunnableActionBest(this::updateLine)));
				} else {
					table.addAction(new InstantSequenceAction(
							getShrinkAction(),
							new RunnableActionBest(this::updatePortrait),
							getGrowAction(),
							new RunnableActionBest(this::updateLine)));
				}	
			}
		}
	}

	private void releasePriority() {
		InputPriorityManager.releasePriority(this);
	}

	private Action getShrinkAction() {
		return Actions.scaleTo(0, 0, shouldDoInstantTextSupplier.get() ? 0 : totalBoxGrowTime, Interpolation.slowFast);
	}

	private Action getGrowAction() {
		return Actions.scaleTo(1, 1, shouldDoInstantTextSupplier.get() ? 0 : totalBoxGrowTime, Interpolation.fastSlow);
	}

	private void makePortrait() {
		Speaker speaker = currentData.getSpeaker();
		portrait = new Image(new Texture(speaker.getTextureAssetPath()));
		this.addActor(portrait);
	}

	private void skipTextSpool() {
		text.skipToTheEnd();
	}
	

	protected abstract Label getFlashingArrowLabel();
	protected abstract Label getFlashingContinueArrowLabel();
	protected abstract TypingLabel getTypingLabel(String currentMessage);


	// Speaker listener functions
	private static List<SpeakerListener> speakerListeners = new ArrayList<>();
	private void speakerChanged(Speaker newSpeaker) {
		speakerListeners.forEach(listener -> listener.onSpeakerChanged(newSpeaker));
	}

	public static void registerSpeakerListener(SpeakerListener speakerListener) {
		speakerListeners.add(speakerListener);
	}
	public static void removeSpeakerListener(SpeakerListener speakerListener) {
		speakerListeners.remove(speakerListener);
	}

}