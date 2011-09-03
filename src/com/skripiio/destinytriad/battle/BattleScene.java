package com.skripiio.destinytriad.battle;

import java.util.ArrayList;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import android.util.Log;

import com.skripiio.destinytriad.ai.EasyAI;
import com.skripiio.destinytriad.battle.BattleEngine.BattleOverListener;
import com.skripiio.destinytriad.battle.rules.RuleSet;
import com.skripiio.destinytriad.card.Card;

/** Scene for handling Battles */
public class BattleScene extends Scene implements IOnAreaTouchListener {

	public static final int PLAYER_USER = 0;
	public static final int PLAYER_OPP = 1;
	public static final int DRAW = -1;

	/** The Game Board */
	Rectangle[][] mBoard;

	/** Each players hand */
	Rectangle[] mPlayerHand;
	Rectangle[] mOpponentHand;

	ArrayList<Card> mCards;

	ArrayList<Card> playerCards;
	ArrayList<Card> opponentCards;

	BattleEngine mBattleEngine;

	Battle mBattle;

	TurnIndicator mArrow;

	/**
	 * @param playerCards
	 *           5 card IDs that the player is using
	 * @param opponentCards
	 *           5 cards IDs that the opponent is using
	 */
	public BattleScene(ArrayList<Card> playerCards, ArrayList<Card> opponentCards,
			Battle b, RuleSet rs, TextureRegion pArrowTextureRegion) {
		// assert that each player is using 5 cards
		if (playerCards.size() != 5 || opponentCards.size() != 5) {
			throw new IllegalStateException("Each player must have 5 cards to battle");
		}

		mArrow = new TurnIndicator(Battle.CAMERA_WIDTH / 2 - 50, Battle.CAMERA_HEIGHT / 2 - 50,
				100, 100, pArrowTextureRegion);
		
		mBattle = b;

		this.playerCards = new ArrayList<Card>();
		this.playerCards.addAll(playerCards);

		this.opponentCards = new ArrayList<Card>();
		this.opponentCards.addAll(opponentCards);
		mCards = new ArrayList<Card>();
		mCards.addAll(playerCards);
		mCards.addAll(opponentCards);
		p1 = new EasyAI(playerCards, 0);
		p2 = new EasyAI(opponentCards, 1);

		for (int i = 0; i < mCards.size(); i++) {
			registerTouchArea(mCards.get(i));
		}

		this.setOnAreaTouchListener(this);
		initializeBoard();
		
		mBattleEngine = new BattleEngine(this, rs, new BattleOverListener() {

			@Override
			public void onGameOver(int victoriousPlayer) {
				Log.e("Game", "Game Over!!! Player " + victoriousPlayer + " wins!!!");

				mBattle.finish();
			}
		}, p1, p2, mArrow);

		this.attachChild(mBattleEngine);
	}

	BattlePlayer p1;
	BattlePlayer p2;

	/**
	 * Creates all the primitives necessary for creating and controlling the 3x3
	 * board
	 */
	private void initializeBoard() {
		Log.v("Game", "Initializing Board");
		setBackground(new ColorBackground(1, 1, 1));
		mBoard = new Rectangle[3][3];

		// Set out the board squares
		int counter = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mBoard[i][j] = new Rectangle(
						(Battle.CAMERA_WIDTH / 2f) - (playerCards.get(0).getWidth() / 2f)
								- (playerCards.get(0).getWidth())
								+ (j * playerCards.get(0).getWidth()),
						(Battle.CAMERA_HEIGHT / 2f) - (playerCards.get(0).getHeight() / 2f)
								- (playerCards.get(0).getHeight())
								+ (i * playerCards.get(0).getHeight()), playerCards.get(0)
								.getWidth(), playerCards.get(0).getHeight());
				if (counter % 2 == 0) {
					mBoard[i][j].setColor(165f / 255f, 245f / 255f, 211f / 255f);
				} else {
					mBoard[i][j].setColor(247f / 255f, 255f / 143f, 255f / 99f);
				}
				counter++;
				this.attachChild(mBoard[i][j]);
			}
		}

		// set out player hand squares
		mPlayerHand = new Rectangle[5];
		mOpponentHand = new Rectangle[5];

		float amountOfScreenRealEstate = Battle.CAMERA_HEIGHT
				- ((playerCards.get(0).getHeight() / 2) * 6);
		float verticalBuffer = amountOfScreenRealEstate / 2;

		for (int i = 0; i < 5; i++) {
			mPlayerHand[i] = new Rectangle(verticalBuffer,
					(playerCards.get(i).getHeight() / 2) * i + verticalBuffer, playerCards
							.get(i).getWidth(), playerCards.get(i).getHeight());
			mOpponentHand[i] = new Rectangle(Battle.CAMERA_WIDTH - verticalBuffer
					- opponentCards.get(i).getWidth(), (opponentCards.get(i).getHeight() / 2)
					* i + verticalBuffer, opponentCards.get(i).getWidth(), opponentCards
					.get(i).getHeight());
			attachChild(mPlayerHand[i]);
			attachChild(mOpponentHand[i]);
		}

	}

	public Rectangle[][] getBattleBoard() {
		return mBoard;
	}

	public Rectangle[] getPlayerHandRectangles() {
		return mPlayerHand;
	}

	public Rectangle[] getOpponentHandRectangles() {
		return mOpponentHand;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		// if the current player is human and requires touch input
		if (mBattleEngine.getCurrentBattlePlayer().isHuman()) {
			Log.v("BattleEngine", "Waiting for input...");
		}
		return false;
	}

}
