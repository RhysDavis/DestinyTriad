package com.skripiio.destinytriad.battle.player;

import com.skripiio.destinytriad.battle.engine.BattleEngine;
import com.skripiio.destinytriad.battle.engine.BoardSquare;
import com.skripiio.destinytriad.battle.engine.BoardSquare.IOnBoardSquareSelectedListener;
import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.Card.IOnCardSelectedListener;

public class HumanBattlePlayer extends BattlePlayer {

	private IOnCardSelectedListener mCardSelectedListener;
	private IOnBoardSquareSelectedListener mBoardSquareSelectedListener; 
	
	private boolean isCardSelected = false;
	private int mCardSelected = 0;
	private int mBoardSquareSelected = 0;
	
	
	public HumanBattlePlayer(int pPlayerNumber, Card[] pPlayerCards,
			BattleEngine pBattleEngine) {
		super(pPlayerNumber, pPlayerCards, pBattleEngine);
		mCardSelectedListener = new IOnCardSelectedListener() {

			@Override
			public void onCardSelect(Card pCard) {
				if (isMyTurn()) {
					if (!pCard.isPlaced()) {
					isCardSelected = true;
					pCard.selectCard();
					for (int i = 0; i < getCards().length; i++) {
						if (getCards()[i] != pCard) {
							pCard.deSelectCard();
						} else {
							mCardSelected = i;
						}
					}
					}
				}
			}
		};
		
		for (int i = 0; i < getCards().length; i++) {
			getCards()[i].setOnSelectListener(mCardSelectedListener);
		}
		
		
		mBoardSquareSelectedListener = new IOnBoardSquareSelectedListener() {
			
			@Override
			public void onBoardSquareSelected(BoardSquare pBoardSquare) {
				if (isCardSelected) {
					mBoardSquareSelected = pBoardSquare.getBoardSquareNumber();
					playCard(mCardSelected, mBoardSquareSelected);
				}
			}
		};
		
		// attach listener to all the board squares
		for (int i = 0; i < getBattleEngine().getBoardSquares().length; i++) {
			getBattleEngine().getBoardSquares()[i].setOnBoardSelectedListener(mBoardSquareSelectedListener);
		}
	}
	
	@Override
	public void setTurn(boolean isTurn) {
		isCardSelected = false;
		super.setTurn(isTurn);
	}

}
