package com.darzalgames.libgdxtools.ui.textbox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.LogicalInputConsumer;

public class DialogueScene extends Group implements LogicalInputConsumer {

	private final Supplier<Boolean> shouldDoInstantTextSupplier;
	private final TextboxFastForwarder textboxFastForwarder;

	private Iterator<TextboxData> lines;
	protected DialogChoiceData dialogChoiceData;
	private TextboxData previousData;
	
	private Textbox currentTextbox;

	public DialogueScene(List<TextboxData> lines, Supplier<Boolean> shouldDoInstantTextSupplier) {
		flagLastLinesForEachSpeaker(lines);
		this.lines = lines.iterator();
		this.shouldDoInstantTextSupplier = shouldDoInstantTextSupplier;
		this.textboxFastForwarder = new TextboxFastForwarder();
		
		this.setBounds(0, 0, GameInfo.getWidth(), GameInfo.getHeight());
		addListener(new InputListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				consumeKeyInput(Input.ACCEPT);
				return true;
			}
		});
		
	}
	

	private void flagLastLinesForEachSpeaker(List<TextboxData> conversation) {
		for (int i = 0; i < conversation.size() - 1; i++) {
			TextboxData thisLine = conversation.get(i);
			boolean differentSpeakersBetweenThisAndNextLine = !thisLine.getSpeaker().equals(conversation.get(i+1).getSpeaker());
			if (differentSpeakersBetweenThisAndNextLine) {
				thisLine.setLastLineByThisSpeaker(true);
			}
		}
		conversation.get(conversation.size() - 1).setLastLineByThisSpeaker(true);
	}


	@Override
	public void consumeKeyInput(final Input input) {
		if (input == Input.SKIP && !textboxFastForwarder.isSkipping()) {
			textboxFastForwarder.pressSkipButton();
		} else if (this.getActions().isEmpty() && (input == Input.ACCEPT || textboxFastForwarder.isSkipping())) {
			if (currentTextbox != null && !currentTextbox.hasEnded()) {
				// If the text in the text box is currently spooling, we cancel that and show the full message right away
				currentTextbox.skipTextSpool();
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
		if (textboxFastForwarder.isSkipping()) {
			consumeKeyInput(Input.SKIP);
		}
	}

	@Override
	public void gainFocus() {
		showNextLineOrReleasePriority();
	}

	@Override
	public void regainFocus() {
		textboxFastForwarder.setInteractable(true);
		if (dialogChoiceData != null) {
			dialogChoiceData = null;
			showNextLineOrReleasePriority();
		}
	}

	@Override
	public void loseFocus() {
		textboxFastForwarder.setInteractable(false);
		textboxFastForwarder.setSkipping(false);
	}

	protected void showNextLineOrReleasePriority() {
		this.clearChildren();
		if (!lines.hasNext()) {
			if (currentTextbox.currentData == null //e.g. a MIDNIGHT textbox with no talking in it, which happens most days
					|| currentTextbox.currentData.getMessage().isBlank()) { // TODO this blank check...?
				releasePriority();
			} else {
				currentTextbox.shrink(shouldDoInstantTextSupplier.get(), this::releasePriority);
			}
		} else {
			boolean isFirstLine = currentTextbox == null;
			currentTextbox = new Textbox(lines.next());
			if (!lines.hasNext()) {
				//bail now if the final line was invalid
				releasePriority();
			} else {
				Speaker currentSpeaker = currentTextbox.currentData.getSpeaker();
				Speaker previousSpeaker = null;
				if (previousData != null) {
					previousSpeaker = previousData.getSpeaker();
				}

				if ((previousData == null)
						|| (previousData != null && previousSpeaker != currentSpeaker)) {
					speakerChanged(currentSpeaker);
				}
				previousData = currentTextbox.currentData;

				boolean isBlank = currentSpeaker.equals(Speaker.BLANK) && currentTextbox.currentData.getMessage().isBlank();
				boolean isLastLine = isBlank && !lines.hasNext();
				if (textboxFastForwarder.isSkipping() || (isFirstLine && isBlank)) {
//					showLine();
//					updatePortrait();
					currentTextbox.skipTextSpool();
				} else if (isFirstLine) {
					currentTextbox.grow(shouldDoInstantTextSupplier.get());
				// May be a redundant case?
//				} else if (isLastLine || isBlank) {
//					this.addAction(new InstantSequenceAction(
//							getShrinkAction(),
//							new RunnableActionBest(this::updatePortrait),
//							new RunnableActionBest(this::showLine)));
				} else {
					currentTextbox.shrink(shouldDoInstantTextSupplier.get(), this::showNextLineOrReleasePriority);
				}	
			}
		}
	}
	


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
