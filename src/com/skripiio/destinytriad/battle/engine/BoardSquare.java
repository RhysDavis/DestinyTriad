package com.skripiio.destinytriad.battle.engine;

import java.util.ArrayList;

import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.skripiio.destinytriad.card.Card;

public class BoardSquare extends Sprite implements IBoardSquare, IOnAreaTouchListener {

	public interface IOnBoardSquareSelectedListener {
		public void onBoardSquareSelected(BoardSquare pBoardSquare);
	}

	private int mBoardSquareNumber;

	private int mElement;

	private Card mCard;

	private ArrayList<IOnBoardSquareSelectedListener> mListeners;
	
	/** Creates a normal Board Square */
	public BoardSquare(float pX, float pY, float pWidth, float pHeight,
			int pBoardSquareNumber, TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		mElement = -1;
		mBoardSquareNumber = pBoardSquareNumber;
		initialize();
	}

	/** Creates a board square with an element */
	public BoardSquare(float pX, float pY, float pWidth, float pHeight,
			int pBoardSquareNumber, TextureRegion pTextureRegion, int pElement) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		this.mElement = pElement;
		mBoardSquareNumber = pBoardSquareNumber;
		initialize();

	}

	private void initialize() {
		mListeners = new ArrayList<IOnBoardSquareSelectedListener>();
	}
	
	@Override
	public boolean isElement() {
		return (mElement != -1);
	}

	@Override
	public int getElement() {
		return mElement;
	}

	@Override
	public boolean isCardHere() {
		return (mCard != null);
	}

	public Card getCard() {
		return mCard;
	};

	public void setCard(Card pCard) {
		mCard = pCard;
	}

	@Override
	public int getBoardSquareNumber() {
		return mBoardSquareNumber;
	}

	public void setOnBoardSelectedListener(IOnBoardSquareSelectedListener pListener) {
		mListeners.add(pListener);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		for (int i = 0; i < mListeners.size(); i++) {
			mListeners.get(i).onBoardSquareSelected(this);
		}
		return false;
	}
	
}
