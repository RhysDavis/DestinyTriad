package com.skripiio.destinytriad.battle.engine;

import java.util.ArrayList;

import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.IBattleCard;
import com.skripiio.destinytriad.card.selector.GameCardSelector.IOnCardsSelectedListener;

public interface IBattlePlayer {

	public interface IOnCardSelectedListener {
		public void onCardPlaced(IBattleCard pBattleCard, int pBoardSquareNumber);
	}
	
	public ArrayList<Card> getCards();

	public int getPlayerNumber();

	public void setPlayerCards(ArrayList<Card> pPlayerCards);
	
	public void setOnCardPlacedListener(IOnCardSelectedListener pOnCardPlacedListener);
	
	public void onStartTurn(IOnActionFinishedListener pOnEventFinishedListener);

	public void onEndTurn(IOnActionFinishedListener pOnEventFinishedListener);

	public void playCard(IBattleCard pCard, int pSquareNumber);
}
