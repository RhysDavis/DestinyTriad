package com.skripiio.destinytriad.battle.engine;

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

	public int getPlayerNumber() {
		return mPlayerNumber;
	}

	public void setTurn(boolean isTurn) {
		isMyTurn = isTurn;
	}

	public boolean isMyTurn() {
		return isMyTurn;
	}

	@Override
	public boolean playCard(int pCardNumInHand, int pSquareNumber) {
		mBattleEngine.placeCard(getCards()[pCardNumInHand], pSquareNumber);
		return true;
	}

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

}
