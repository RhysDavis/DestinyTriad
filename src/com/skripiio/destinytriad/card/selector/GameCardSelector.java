package com.skripiio.destinytriad.card.selector;

import java.util.ArrayList;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.IModifier.IModifierListener;

import android.util.Log;

import com.skripiio.destinytriad.battle.BattleActivity;
import com.skripiio.destinytriad.battle.engine.IOnActionFinishedListener;
import com.skripiio.destinytriad.card.AbstractCard;
import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.CardFactory;
import com.skripiio.destinytriad.card.IBattleCard;
import com.skripiio.destinytriad.card.TempCardDatabase;

public class GameCardSelector extends Rectangle implements ICardSelector,
		IOnAreaTouchListener {

	private IOnCardsSelectedListener mOnCardsSelectedListener;
	private BattleActivity mEngine;
	private Scene mScene;

	private Rectangle[] mPlayerHandLocations;

	private ArrayList<Card> mCardsOnScreen;

	private int mCardsInHandCounter = 0;
	
	public GameCardSelector(BattleActivity pEngine, Scene pScene) {
		super(0, 0, BattleActivity.CAMERA_WIDTH, BattleActivity.CAMERA_HEIGHT);
		this.setColor(0, 0, 0, 0.8f);
		mEngine = pEngine;
		mScene = pScene;
		mPlayerHandLocations = new Rectangle[5];
		mCards = new ArrayList<Card>();
		float vVerticalSpaceBuffer = ((BattleActivity.CAMERA_HEIGHT - ((Card.CARD_HEIGHT) / 2f) * 6f) / 2f);

		Rectangle vSeperator = new Rectangle(BattleActivity.CAMERA_WIDTH - vVerticalSpaceBuffer * 2
				- Card.CARD_WIDTH, 0, 3, BattleActivity.CAMERA_HEIGHT);
		vSeperator.setColor(0, 0, 0);
		this.attachChild(vSeperator);

		for (int i = 0; i < mPlayerHandLocations.length; i++) {
			mPlayerHandLocations[i] = new Rectangle(BattleActivity.CAMERA_WIDTH
					- vVerticalSpaceBuffer - Card.CARD_WIDTH, (Card.CARD_HEIGHT / 2) * i
					+ vVerticalSpaceBuffer, Card.CARD_WIDTH, Card.CARD_HEIGHT);
			mPlayerHandLocations[i].setColor(1f, 1f, 1f);
			attachChild(mPlayerHandLocations[i]);
		}

		ArrayList<AbstractCard> vAllPlayerCards = new ArrayList<AbstractCard>();
		// pull all the cards that the player owns
		for (int i = 0; i < TempCardDatabase.PLAYER_CARDS.length; i++) {
			vAllPlayerCards.add(new AbstractCard(TempCardDatabase.PLAYER_CARDS[i]));
		}

		float cardWidthSeperator = ((vSeperator.getX() - Card.CARD_WIDTH * 4) / 5f);

		mCardsOnScreen = new ArrayList<Card>();

		int j = 0;
		for (int i = 0; i < 12; i++) {
			mCardsOnScreen.add(CardFactory.createCard(mEngine.getEngine(), Card.CARD_WIDTH,
					Card.CARD_HEIGHT, TempCardDatabase.PLAYER_CARDS[i], 0, mEngine.mCardFont,
					mEngine.mCardRedFont, mEngine.mCardRedFont,
					mEngine.mCardBorderTextureRegion, mEngine.mCardBackTextureRegion,
					mEngine.mCardBorderTextureRegion));
			attachChild(mCardsOnScreen.get(i));

			mCardsOnScreen.get(i).setPosition(
					cardWidthSeperator + (i % 4) * cardWidthSeperator + (i % 4)
							* Card.CARD_WIDTH, 20 + j * Card.CARD_HEIGHT + j * 60);
			if (i % 4 == 3) {
				j++;
			}
		}

		mScene.registerTouchArea(this);
		mScene.setOnAreaTouchListener(this);
	}

	@Override
	public ArrayList<Card> getSelectedCards() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onOpen(IOnCardsSelectedListener pOnCardsSelectedListener) {
		mOnCardsSelectedListener = pOnCardsSelectedListener;

	}

	public interface IOnCardsSelectedListener {
		public void onCardsSelected(Card[] pPlayerCards);
	}

	@Override
	public void onClose(final IOnActionFinishedListener pOnAnimationFinishedListener) {
		Log.v("BattleEngine", "Card Selector - Closing...");
		AlphaModifier am = new AlphaModifier(0.5f, 0.8f, 0);
		am.addModifierListener(new IModifierListener<IEntity>() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				pOnAnimationFinishedListener.OnAnimationFinished();
				

			}
		});
		registerEntityModifier(am);
	}

	public void updateCardsOnScreen() {

	}

	public void unloadAllUnnecesaryResources() {

	}

	private float beginY = 0;
	private float actionStartY = 0;
	private float actionEnd = 0;

	private boolean moved = false;
	private int moveAmount = 0;

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, ITouchArea pTouchArea,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if (pSceneTouchEvent.isActionDown()) {
			actionStartY = pSceneTouchEvent.getY();
			beginY = actionStartY;
			moved = false;
		}

		if (pSceneTouchEvent.isActionMove()) {
			if (beginY - pSceneTouchEvent.getY() > 20
					|| beginY - pSceneTouchEvent.getY() < -20) {
				moved = true;
			}
			mEngine.runOnUpdateThread(new Runnable() {

				@Override
				public void run() {
					float difference = pSceneTouchEvent.getY() - actionStartY;
					for (int i = 0; i < mCardsOnScreen.size(); i++) {
						mCardsOnScreen.get(i).setPosition(mCardsOnScreen.get(i).getX(),
								mCardsOnScreen.get(i).getY() + difference);
					}
					actionStartY = pSceneTouchEvent.getY();

				}
			});

		}

		if (pSceneTouchEvent.isActionUp()) {
			for (int i = 0; i < mCardsOnScreen.size(); i++) {
				if (mCardsOnScreen.get(i).contains(pSceneTouchEvent.getX(),
						pSceneTouchEvent.getY())) {
					if (!moved) {
						Log.v("BattleEngine", "Moving Card Over");
						mCardsOnScreen.get(i).registerEntityModifier(
								new MoveModifier(0.2f, mCardsOnScreen.get(i).getX(),
										mPlayerHandLocations[mCardsInHandCounter].getX(), mCardsOnScreen.get(i)
												.getY(), mPlayerHandLocations[mCardsInHandCounter].getY()));
						mCards.add(mCardsOnScreen.get(i));
						mCardsOnScreen.remove(i);
						mCardsInHandCounter++;
						if (mCardsInHandCounter == 5) {
							Card[] cards = new Card[5];
							for (int j = 0; j < mCards.size(); j++) {
								cards[j] = mCards.get(j);
							}
							mOnCardsSelectedListener.onCardsSelected(cards);
						}
						break;
					}
				}
			}
		}

		return true;
	}
	
	private ArrayList<Card> mCards;
}
