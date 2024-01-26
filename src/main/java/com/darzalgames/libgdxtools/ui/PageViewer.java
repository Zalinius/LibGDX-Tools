package com.darzalgames.libgdxtools.ui;

import java.util.List;
import java.util.function.Supplier;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darzalgames.libgdxtools.MainGame;
import com.darzalgames.libgdxtools.scenes.scene2d.actions.RunnableActionBest;
import com.darzalgames.libgdxtools.ui.input.Input;
import com.darzalgames.libgdxtools.ui.input.InputConsumer;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.KeyboardButton;
import com.darzalgames.libgdxtools.ui.input.keyboard.button.LabelMaker;

public abstract class PageViewer extends Group implements InputConsumer {

	private int viewingIndex = 0;
	private List<Table> pageList;
	private KeyboardButton leftArrow;
	private KeyboardButton rightArrow;
	private KeyboardButton lastButton;
	private Table lastButtonTable;

	protected PageViewer(Supplier<String> lastButtonText) {
		lastButton = LabelMaker.getButton(lastButtonText.get(), this::finish);
		makeArrows();
	}
	
	protected abstract <E extends Table> List<E> makePageTables();
	protected abstract void finish();
	protected abstract Texture getArrowTexture();
	protected abstract Texture getArrowHoveredTexture();

	private void makeArrows() {
		int arrowPadding = 6;
		TextureRegionDrawable arrowDrawable = new TextureRegionDrawable(getArrowTexture());
		TextureRegionDrawable arrowHoveredDrawable = new TextureRegionDrawable(getArrowHoveredTexture());
		rightArrow = LabelMaker.getBlankButton(arrowDrawable, arrowHoveredDrawable, arrowDrawable, () -> turnPage(true));
		LabelMaker.makeActorCentered(rightArrow.getView());
		rightArrow.getView().setX(MainGame.getWidth() - rightArrow.getView().getWidth() - arrowPadding);

		leftArrow = LabelMaker.getBlankButton(arrowDrawable, arrowHoveredDrawable, arrowDrawable, () -> turnPage(false));
		leftArrow.getView().setTransform(true);
		leftArrow.getView().setScaleX(-1);
		LabelMaker.makeActorCentered(leftArrow.getView());
		leftArrow.getView().setX(leftArrow.getView().getWidth() + arrowPadding);	
	}

	@Override
	public void consumeKeyInput(final Input input) {
		if(viewingIndex == pageList.size() - 1
				&& input == Input.ACCEPT) {		
			lastButton.consumeKeyInput(input);
		}
		else if (input == Input.RIGHT
				|| input == Input.ACCEPT) {			
			turnPage(true);
		}
		else if(input == Input.LEFT
				|| input == Input.BACK) {	
			turnPage(false);
		}
	}

	private void turnPage(final boolean toRight) {
		boolean toDoShift = false;
		int initialIndex = viewingIndex;
		if (toRight) {
			if (viewingIndex < pageList.size() - 1) {
				++viewingIndex;
				toDoShift = true;
			}
		} else {
			if (viewingIndex - 1 >= 0) {
				--viewingIndex;
				toDoShift = true;
			}
		}
		if (toDoShift) {
			leftArrow.getView().setVisible(false);
			rightArrow.getView().setVisible(false);

			if (initialIndex >= 0 && initialIndex < pageList.size()) {
				Table toSendOut = pageList.get(initialIndex);
				float startY = toSendOut.getY();
				float finalX = (toRight ? -toSendOut.getWidth() : MainGame.getWidth()); 
				toSendOut.addAction(Actions.moveTo(finalX, startY, 0.25f, Interpolation.circle));
			}

			Table toBringIn = pageList.get(viewingIndex);
			float startY = toBringIn.getY();
			float finalX = MainGame.getWidth() / 2f - toBringIn.getWidth() / 2f;
			toBringIn.addAction(Actions.sequence(Actions.moveTo(finalX, startY, 0.25f, Interpolation.circle), new RunnableActionBest(this::updateArrows)));
			selectDefault();
		} 
	}

	private void updateArrows() {
		int currentIndex = viewingIndex + 1;
		leftArrow.getView().setVisible(currentIndex > 1);
		rightArrow.getView().setVisible(currentIndex < pageList.size());
	}

	@Override
	public void gainFocus() {
		this.clearActions();
		this.clearChildren();
		viewingIndex = -1;

		pageList = makePageTables();
		lastButtonTable = new Table();
		lastButtonTable.setSize(150, 60);
		lastButtonTable.setBackground(LabelMaker.getUIBorderedNine());
		lastButtonTable.add(lastButton.getView()).prefWidth(lastButton.getView().getWidth());
		pageList.add(lastButtonTable);
		pageList.forEach(table -> {
			LabelMaker.makeActorCentered(table);
			table.setX(MainGame.getWidth());
			this.addActor(table);
		});

		this.addActor(leftArrow.getView());
		this.addActor(rightArrow.getView());	
		
		consumeKeyInput(Input.RIGHT);
	}

	@Override
	public void focusCurrent() {
		selectDefault();
	}

	@Override
	public void clearSelected() {
		lastButton.setFocused(false);
	}

	@Override
	public void selectDefault() {
		if (viewingIndex == pageList.size() - 1) {
			lastButton.setFocused(true);
		} else {
			clearSelected();
		}
	}
	
	public void skipFirstSlideIn() {
		viewingIndex = 0;
		LabelMaker.makeActorCentered(pageList.get(viewingIndex));
		this.leftArrow.getView().setVisible(false);
		this.lastButtonTable.setVisible(false);
	}

}
