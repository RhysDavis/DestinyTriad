package com.skripiio.destinytriad.card.selector;

import java.util.ArrayList;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.input.touch.TouchEvent;

import com.skripiio.destinytriad.battle.BattleActivity;
import com.skripiio.destinytriad.card.Card;

public class CardSelectorScene extends Scene implements IOnSceneTouchListener {

	public interface CardSelectedListener {
		public void onCardsSelected(Card[] pPlayerCards);
	}

	public static final int CAMERA_WIDTH = 720;
	public static final int CAMERA_HEIGHT = 480;

	private CardSelectedListener mListener;

	private Rectangle mCardSeperator;

	Rectangle mPlayerHand[];
	ArrayList<Card> mPlayerCards;

	/** Creates a CardSelectorScene */
	public CardSelectorScene(CardSelectedListener pListener, ArrayList<Card> pPlayerCards) {
		mListener = pListener;
		mPlayerCards = new ArrayList<Card>();
		mPlayerCards.addAll(pPlayerCards);
		for (int i = 0; i < mPlayerCards.size(); i++) {
			mPlayerCards.get(i).setScale(0.8f);
			attachChild(mPlayerCards.get(i));
		}

		setBackground(new ColorBackground(1, 1, 1));
		float amountOfScreenRealEstate = BattleActivity.CAMERA_HEIGHT
				- ((Card.CARD_HEIGHT / 2) * 6);
		float verticalBuffer = amountOfScreenRealEstate / 2;
		mPlayerHand = new Rectangle[5];
		for (int i = 0; i < 5; i++) {
			mPlayerHand[i] = new Rectangle(verticalBuffer, (Card.CARD_HEIGHT / 2) * i
					+ verticalBuffer, Card.CARD_WIDTH, Card.CARD_HEIGHT);
			attachChild(mPlayerHand[i]);

		}
		mCardSeperator = new Rectangle(mPlayerHand[0].getX() + mPlayerHand[0].getWidth()
				+ 10, 0, 2, CAMERA_HEIGHT);
		mCardSeperator.setColor(0, 0, 0);
		setTouchAreaBindingEnabled(true);
		setOnSceneTouchListener(this);
		attachChild(mCardSeperator);
	}

	private void toggleNewCards() {

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		for (int i = 0; i < mPlayerCards.size(); i++) {
			if (mPlayerCards.get(i).contains(pSceneTouchEvent.getX(),
					pSceneTouchEvent.getY())) {
				mPlayerCards.get(i).setPosition(
						pSceneTouchEvent.getX() - mPlayerCards.get(i).getWidth() / 2,
						pSceneTouchEvent.getY() - mPlayerCards.get(i).getHeight() / 2);
				return true;
			}

		}
		return false;
	}
}
