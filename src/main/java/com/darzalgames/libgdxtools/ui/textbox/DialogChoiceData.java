package com.darzalgames.libgdxtools.ui.textbox;

import com.darzalgames.darzalcommon.functional.Runnables;

public class DialogChoiceData {
	private final String firstChoiceKey, secondChoiceKey;
	private final Runnable firstChoiceRunnable, secondChoiceRunnable;
	private String promptKey;
	private String chosenKey = null;
	private boolean isWarning;

	public DialogChoiceData(String firstChoiceKey, String secondChoiceKey,
			Runnable firstChoiceRunnable, Runnable secondChoiceRunnable) {
		this.firstChoiceKey = firstChoiceKey;
		this.secondChoiceKey = secondChoiceKey;
		this.firstChoiceRunnable = firstChoiceRunnable;
		this.secondChoiceRunnable = secondChoiceRunnable;
		this.promptKey = "say_what";
	}
	
	public DialogChoiceData(String firstChoiceKey, String secondChoiceKey, Runnable outcomeRunnable) {
		this(firstChoiceKey, secondChoiceKey, outcomeRunnable, outcomeRunnable);
	}

	public DialogChoiceData(String firstChoiceKey, String secondChoiceKey) {
		this(firstChoiceKey, secondChoiceKey, Runnables.nullRunnable(), Runnables.nullRunnable());
	}


	public String getFirstChoiceKey() {
		return firstChoiceKey;
	}

	public String getSecondChoiceKey() {
		return secondChoiceKey;
	}

	public Runnable getFirstChoiceRunnable() {
		return firstChoiceRunnable;
	}

	public Runnable getSecondChoiceRunnable() {
		return secondChoiceRunnable;
	}
	
	public String getChosenKey() {
		return chosenKey;
	}
	
	public boolean choseFirstOption() {
		return getChosenKey().equals(firstChoiceKey);
	}
	
	public boolean choseSecondOption() {
		return getChosenKey().equals(secondChoiceKey);
	}
	
	public void setChosenKey(String chosenKey) {
		this.chosenKey = chosenKey;
	}

	public String getPromptKey() {
		return promptKey;
	}

	public void setPromptKey(String promptKey, boolean isWarning) {
		this.promptKey = promptKey;
		this.isWarning = isWarning;
	}

	public boolean isWarning() {
		return isWarning;
	}
}