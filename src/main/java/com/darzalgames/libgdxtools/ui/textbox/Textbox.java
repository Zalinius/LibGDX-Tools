package com.darzalgames.libgdxtools.ui.textbox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.darzalgames.libgdxtools.maingame.GameInfo;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.InstantSequenceAction;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UserInterfaceFactory;
import com.github.tommyettinger.textra.TypingLabel;

public class Textbox extends Table {

	protected TextboxData currentData;
	protected DialogChoiceData dialogChoiceData;

	protected TypingLabel text;

	private static final float TOTAL_BOX_GROW_TIME = 0.2f;

	public Textbox(TextboxData currentData) {
		this.currentData = currentData;
		this.setBounds(0, 0, GameInfo.getWidth(), GameInfo.getHeight());
		
		NinePatchDrawable background = UserInterfaceFactory.getUIBorderedNine();
		this.setBackground(background);
		this.setOrigin(Align.center);
		this.setTransform(true);
		this.setScale(0);
		setBoxSizeAndPosition();
	}

	protected void setBoxSizeAndPosition() {
		int ySpacing = 10; // magic! Put it where you want, is all. Currently it hides the stuff on the desk
		int textBoxWidth = 250;
		int textBoxHeight = 62;
		this.setPosition(GameInfo.getWidth()/2 - textBoxWidth / 2, ySpacing);
		this.setSize(textBoxWidth, textBoxHeight);
	}

	private void showLine() {
		if (currentData.getSpeaker() != Speaker.BLANK) {
			makeNameTag();
		}
		String currentMessage = currentData.getMessage();
		text = UserInterfaceFactory.getTypingLabel(currentMessage);
		text.setAlignment(Align.topLeft);
		this.add(text).grow();
		makeArrowLabel(currentData.isLastLineByThisSpeaker());

		dialogChoiceData = currentData.getDialogueChoiceData();

		if (!currentMessage.isEmpty()) {
			this.setScale(1);
			this.layout();
		}
	}
	
	private void makeArrowLabel(boolean isEndOfSpeech) {
		Label arrow = isEndOfSpeech ? UserInterfaceFactory.getFlashingArrowLabel() : UserInterfaceFactory.getFlashingContinueArrowLabel();
		// the arrow is manually positioned in the bottom right corner of the this so that it can overlap with dialogue that reaches the bottom line
		// it is not added to a cell because the table does not allow overlapping cells and will instead overflow, no bueno
		arrow.setPosition(this.getWidth() - this.getPadRight() - arrow.getWidth(), this.getPadBottom()); // this position is relative to the this's 0,0
		this.addActor(arrow);
	}


	private void makePortrait() {
		if (currentData.getSpeaker() != Speaker.BLANK) {
			Speaker speaker = currentData.getSpeaker();
			Image portrait = new Image(new Texture(speaker.getTextureAssetPath()));
			this.addActor(portrait);
		}
	}

	private void makeNameTag() {
		Label nameTag = UserInterfaceFactory.getLabelWithBackground(currentData.getSpeakerName());
		nameTag.layout();
		nameTag.setPosition(4, this.getHeight()-2);
		this.addActor(nameTag);
		
	}
	
	boolean hasEnded() {
		return text.hasEnded();
	}

	void skipTextSpool() {
		text.skipToTheEnd();
	}
	
	void grow(boolean instant) {
		this.addAction(new InstantSequenceAction(
				new RunnableActionBest(this::makePortrait),
				getGrowAction(instant),
				new RunnableActionBest(this::showLine)));
	}
	
	void shrink(boolean instant, Runnable doAfter) {
		this.addAction(new InstantSequenceAction(
				getShrinkAction(instant),
				new RunnableActionBest(doAfter)
				));	
	}


	private Action getShrinkAction(boolean instant) {
		return Actions.scaleTo(0, 0, instant ? 0 : TOTAL_BOX_GROW_TIME, Interpolation.slowFast);
	}

	private Action getGrowAction(boolean instant) {
		return Actions.scaleTo(1, 1, instant ? 0 : TOTAL_BOX_GROW_TIME, Interpolation.fastSlow);
	}
	
}