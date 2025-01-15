package com.darzalgames.libgdxtools.ui.textbox;

import com.darzalgames.libgdxtools.internationalization.TextSupplier;

/**
 * @author DarZal
 *
 */
public class TextboxData {
	
	private String messageKey;
	private Object[] args;
	private DialogChoiceData dialogueChoiceData;
	private Speaker speaker;

	public TextboxData(String messageKey, Speaker speaker, Object... args) {
		this.messageKey = messageKey;
		this.args = args;
		this.speaker = speaker;
	}

	public static TextboxData getBlankTextbox() {
		return getBlankTextbox("blank_label");
	} 

	public static TextboxData getBlankTextbox(String key) {
		return new TextboxData(key, Speaker.BLANK);
	}

	@Override
	public String toString() {
		return "Textbox [message=" + getMessage() + ", speaker=" + speaker + "]";
	}

	public String getMessage() {
		return TextSupplier.getLine(messageKey, args);
	}

	public Speaker getSpeaker() {
		return speaker;
	}

	public DialogChoiceData getDialogueChoiceData() {
		return dialogueChoiceData;
	}

	public void setDialogueChoiceData(DialogChoiceData dialogueChoiceData) {
		this.dialogueChoiceData = dialogueChoiceData;
	}

	public String getSpeakerName() {
		return speaker.getLocalizedName();
	}

}
