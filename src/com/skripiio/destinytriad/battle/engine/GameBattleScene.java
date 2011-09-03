package com.skripiio.destinytriad.battle.engine;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.input.touch.TouchEvent;

import com.skripiio.destinytriad.battle.Battle;
import com.skripiio.destinytriad.battle.rules.RuleSet;
import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.selector.GameCardSelector;

public class GameBattleScene extends Scene implements IOnSceneTouchListener {

	/** All 9 of the board squares in the game */
	private BoardSquare[] mBoardSquares;

	private Rectangle[] player0Squares;
	private Rectangle[] player1Squares;

	/** The 2 players */
	private BattlePlayer[] mPlayers;

	private IBattleEngine mBattleEngine;

	private Battle mBattle;

	public GameBattleScene(Battle pBattle, RuleSet pRuleSet) {
		mBattle = pBattle;

		mPlayers = new BattlePlayer[2];

		mPlayers[0] = new HumanBattlePlayer(0);
		mPlayers[1] = new HumanBattlePlayer(1);

		player1Squares = new Rectangle[5];

		float amountOfScreenRealEstate = Battle.CAMERA_HEIGHT
				- ((Card.CARD_HEIGHT / 2) * 6);
		float verticalBuffer = amountOfScreenRealEstate / 2;

		player0Squares = new Rectangle[5];
		for (int i = 0; i < 5; i++) {
			player0Squares[i] = new Rectangle(verticalBuffer, (Card.CARD_HEIGHT / 2) * i
					+ verticalBuffer, Card.CARD_WIDTH, Card.CARD_HEIGHT);
			attachChild(player0Squares[i]);

		}

		player1Squares = new Rectangle[5];
		for (int i = 0; i < 5; i++) {
			player1Squares[i] = new Rectangle(Battle.CAMERA_WIDTH - verticalBuffer
					- Card.CARD_WIDTH, (Card.CARD_HEIGHT / 2) * i + verticalBuffer,
					Card.CARD_WIDTH, Card.CARD_HEIGHT);
			attachChild(player1Squares[i]);

		}

		mBoardSquares = new BoardSquare[9];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mBoardSquares[j + 3 * i] = new GameBoardSquare((Battle.CAMERA_WIDTH / 2f)
						- (Card.CARD_WIDTH / 2f) - (Card.CARD_WIDTH) + (j * Card.CARD_WIDTH),
						(Battle.CAMERA_HEIGHT / 2f) - (Card.CARD_HEIGHT / 2f)
								- (Card.CARD_HEIGHT) + (i * Card.CARD_HEIGHT), Card.CARD_WIDTH,
						Card.CARD_HEIGHT, mBattle.mCardBackTextureRegion);
				attachChild(mBoardSquares[j + 3 * i]);
			}
		}

		mBattleEngine = new GameBattleEngine(mBattle, mPlayers, mBoardSquares, pRuleSet,
				mBattle.mTurnIndicator, this, new GameCardSelector(mBattle, this));

		mPlayers[0].setBattleEngine(mBattleEngine);
		mPlayers[1].setBattleEngine(mBattleEngine);

		setBackground(new ColorBackground(1, 1, 1));
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (mBattleEngine.readyForCard()) {
			//if (mBattleEngine.getCurrentPlayer().isHuman()) {
				// check if you've touched any cards
				for (int i = 0; i < mBattleEngine.getCurrentPlayer().getCards().size(); i++) {
					mBattleEngine.getCurrentPlayer().getCards().get(i)
							.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				}
			//}
		}
		return false;
	}
}
