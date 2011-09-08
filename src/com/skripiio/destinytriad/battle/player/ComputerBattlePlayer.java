package com.skripiio.destinytriad.battle.player;

import com.skripiio.destinytriad.battle.engine.BattleEngine;
import com.skripiio.destinytriad.card.Card;

public class ComputerBattlePlayer extends BattlePlayer {

	private AI mAI;

	private Card[] mPlayerCards;

	public ComputerBattlePlayer(int pPlayerNumber, AI pAI, Card[] pPlayerCards,
			BattleEngine pBattleEngine) {
		super(pPlayerNumber, pPlayerCards, pBattleEngine);
		mPlayerCards = pPlayerCards;
		mAI = pAI;
		mAI.setBattlePlayer(this);
	}

	@Override
	public void setTurn(boolean isTurn) {
		if (isTurn)
			mAI.onDoTurn();
		super.setTurn(isTurn);
	}

	/** Animates a card that it 'checks' as well as gets it's info */
	public Card checkCard(int pCardNumber) {
		return mPlayerCards[pCardNumber];
	}

	public class BrainHandler implements Runnable {

		@Override
		public void run() {
			// check all the cards
			for (int i = 0; i < mPlayerCards.length; i++) {
				// if the card isn't placed, allow the AI to analyze it!
				if (!mPlayerCards[i].isPlaced()) {
					mPlayerCards[i].selectCard();
					mAI.analyzeCard(mPlayerCards[i]);
				}
			}
		}

	}

}
