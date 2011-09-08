package com.skripiio.destinytriad.battle.player;

import com.skripiio.destinytriad.battle.engine.AI;
import com.skripiio.destinytriad.battle.engine.BattleEngine;
import com.skripiio.destinytriad.battle.engine.BattlePlayer;
import com.skripiio.destinytriad.card.Card;

public class ComputerBattlePlayer extends BattlePlayer {

	private AI mAI;

	private ControllerBattleView mBattleView;

	private Card[] mPlayerCards;

	public ComputerBattlePlayer(int pPlayerNumber, AI pAI, Card[] pPlayerCards,
			BattleEngine pBattleEngine, ControllerBattleView pBattleView) {
		super(pPlayerNumber, pPlayerCards, pBattleEngine);
		mPlayerCards = pPlayerCards;
		mAI = pAI;
	}

	public ControllerBattleView getBattleView() {
		return this.mBattleView;
	}

	@Override
	public void setTurn(boolean isTurn) {
		if (isTurn)
			mAI.onStartTurn();
		super.setTurn(isTurn);
	}

	/** Animates a card that it 'checks' as well as gets it's info */
	public Card checkCard(int pCardNumber) {
		return mPlayerCards[pCardNumber];
	}

}
