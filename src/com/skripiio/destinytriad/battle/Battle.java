package com.skripiio.destinytriad.battle;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;

import com.skripiio.destinytriad.battle.engine.BattleEngine;
import com.skripiio.destinytriad.battle.engine.BoardSquare;
import com.skripiio.destinytriad.battle.rules.RuleSet;
import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.selector.GameCardSelector;

public class Battle extends Scene {

	public static final int PLAYER_USER = 0;
	public static final int PLAYER_OPP = 1;

	/** All 9 of the board squares in the game */
	private BoardSquare[] mBoardSquares;

	private Rectangle[] player0Squares;
	private Rectangle[] player1Squares;

	private BattleActivity mBattle;

	public Battle(BattleActivity pBattle, RuleSet pRuleSet) {
		mBattle = pBattle;

		player1Squares = new Rectangle[5];

		float amountOfScreenRealEstate = BattleActivity.CAMERA_HEIGHT
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
			player1Squares[i] = new Rectangle(BattleActivity.CAMERA_WIDTH - verticalBuffer
					- Card.CARD_WIDTH, (Card.CARD_HEIGHT / 2) * i + verticalBuffer,
					Card.CARD_WIDTH, Card.CARD_HEIGHT);
			attachChild(player1Squares[i]);

		}

		mBoardSquares = new BoardSquare[9];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mBoardSquares[j + 3 * i] = new BoardSquare((BattleActivity.CAMERA_WIDTH / 2f)
						- (Card.CARD_WIDTH / 2f) - (Card.CARD_WIDTH) + (j * Card.CARD_WIDTH),
						(BattleActivity.CAMERA_HEIGHT / 2f) - (Card.CARD_HEIGHT / 2f)
								- (Card.CARD_HEIGHT) + (i * Card.CARD_HEIGHT), Card.CARD_WIDTH,
						Card.CARD_HEIGHT, j + 3 * i, mBattle.mCardBackTextureRegion);
				attachChild(mBoardSquares[j + 3 * i]);
				registerTouchArea(mBoardSquares[j + 3 * i]);
				setOnAreaTouchListener(mBoardSquares[j + 3 * i]);
			}
		}

		setBackground(new ColorBackground(1, 1, 1));

		new BattleEngine(mBattle, mBoardSquares, pRuleSet, mBattle.mTurnIndicator, this,
				new GameCardSelector(mBattle, this));

	}

}
