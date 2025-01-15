package com.darzalgames.libgdxtools.ui.textbox;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darzalgames.libgdxtools.ui.input.popup.TextChoicePopUp;

public class DialogueChoicePopUp extends TextChoicePopUp {

	private final DialogChoiceData dialogChoiceData;
	
	public DialogueChoicePopUp(DialogChoiceData dialogChoiceData) {
		super(dialogChoiceData.getPromptKey(), dialogChoiceData.getFirstChoiceKey(), dialogChoiceData.getFirstChoiceRunnable(), dialogChoiceData.getSecondChoiceKey(),
				true, false, dialogChoiceData.isWarning());
		this.dialogChoiceData = dialogChoiceData;
	}
	
	@Override
	public boolean canDismiss() {
		return false;
	}

	@Override
	protected Table getMessage() {
		Table table = super.getMessage();
		this.moveBy(57, 37); // TODO magic numbers!
		return table;
	}

	@Override
	protected Runnable getSecondChoiceRunnable() {
		return dialogChoiceData.getSecondChoiceRunnable();
	}
	
	@Override
	protected void setChosenKey(String chosenKey) {
		dialogChoiceData.setChosenKey(chosenKey);
	}

}
