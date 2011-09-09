package com.skripiio.destinytriad.battle.player;

import com.skripiio.destinytriad.card.IBattleCard;

public class EasyAI extends AI {

	public EasyAI() {
		super();
	}

	@Override
	public void onDoTurn() {

	}

	@Override
	public void analyzeCard(IBattleCard pCard) {
		// spam analysis to appear card is 'thinking'
		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 100; j++) {
				
			}
		}

	}

	@Override
	public void onAnalysisComplete() {
		int card = 0;
		int boardNum = 0;
		// get first card in hand
		for (int i = 0; i < getBattlePlayer().getCards().length; i++) {
			if (!getBattlePlayer().getCards()[i].isPlaced()) {
				card = i;
			}
		}
		
		for (int i = 0; i < getBattlePlayer().getBoardSquares().length; i++) {
			if (!getBattlePlayer().getBoardSquares()[i].isCardHere()) {
				boardNum = i;
			}
		}
		
		getBattlePlayer().playCard(card, boardNum);
	}

}
