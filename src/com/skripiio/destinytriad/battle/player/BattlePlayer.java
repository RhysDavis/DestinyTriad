package com.skripiio.destinytriad.battle.player;

import android.util.Log;

import com.skripiio.destinytriad.battle.engine.BattleEngine;
import com.skripiio.destinytriad.battle.engine.IBattlePlayer;
import com.skripiio.destinytriad.battle.engine.IBoardSquare;
import com.skripiio.destinytriad.battle.rules.Rule;
import com.skripiio.destinytriad.card.Card;

public class BattlePlayer implements IBattlePlayer {

	private BattleEngine mBattleEngine;

	private Card[] mPlayerCards;

	private int mPlayerNumber;

	private boolean isMyTurn = false;

	public BattlePlayer(int pPlayerNumber, Card[] pPlayerCards, BattleEngine pBattleEngine) {
		mPlayerNumber = pPlayerNumber;
		mPlayerCards = pPlayerCards;
		mBattleEngine = pBattleEngine;
	}

	public Card[] getCards() {
		return mPlayerCards;
	}

	/**
	 * @return your opponents cards if the open rule is used. otherwise returns
	 *         no cards
	 */
	public Card[] getOpponentCards() {
		// check for open rule
		for (int i = 0; i < mBattleEngine.getRules().length; i++) {
			if (mBattleEngine.getRules()[i].equals(Rule.Open)) {
				return mBattleEngine.getPlayers()[mPlayerNumber + 1 % 2].getCards();

			}
		}

		// if there's no open rule, return an empty array
		return new Card[] {};
	}

	/** @return the players number */
	public int getPlayerNumber() {
		return mPlayerNumber;
	}

	/** Sets this players turn */
	public void setTurn(boolean isTurn) {
		Log.d("BattleEngine", "Battle Player " + mPlayerNumber + "! Setting your turn to "
				+ isTurn);
		isMyTurn = isTurn;
	}

	/** @return true if it's this players turn */
	public boolean isMyTurn() {
		return isMyTurn;
	}

	/** Plays a card onto the battle engine */
	@Override
	public boolean playCard(int pCardNumInHand, int pSquareNumber) {
		Log.d("BattleEngine", "Battle Player " + mPlayerNumber + " playing card "
				+ pCardNumInHand + " to board square " + pSquareNumber);
		mBattleEngine.placeCard(getCards()[pCardNumInHand], pSquareNumber);
		return true;
	}

	/** @return the battle engine */
	public BattleEngine getBattleEngine() {
		return mBattleEngine;
	}

	/**
	 * "Selects" a card in the battle players hand, animating the selection and
	 * returning true if the card is actually in his hand and not on the board
	 */
	public boolean selectCard(Card c) {
		boolean found = false;
		for (int i = 0; i < mPlayerCards.length; i++) {
			if (mPlayerCards[i] == c) {
				c.selectCard();
				found = true;
			} else {
				c.deSelectCard();
			}
		}
		return found;
	}

	@Override
	public IBoardSquare[] getBoardSquares() {
		return mBattleEngine.getBoardSquares();
	}

}
