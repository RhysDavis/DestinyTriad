package com.skripiio.destinytriad.battle.engine;

import java.util.ArrayList;

import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.IBattleCard;

public interface IBattleEngine {

	/** Initializes the engine */
	public void initialize();

	/** Does stuff when the battle is to start */
	public void onBattleStart(IOnActionFinishedListener pOnAnimationFinishedListener);

	public void onTurnStart(IOnActionFinishedListener pOnAnimationFinishedListener);

	public void onTurnFinish(IOnActionFinishedListener pOnAnimationFinishedListener);

	public void endTurn();

	public IBattlePlayer getCurrentPlayer();
	
	public boolean isGameOver();

	public IBoardSquare[] getBoardSquares();
	
	public IBoardSquare getBoardSquare(int pSquareNumber);
	
	public boolean isOpponentHandVisible();
	
	public ArrayList<Card> getOpponentHand();
	
	public void placeCard(IBattleCard pCard, int pSquareNumber);
	
	public boolean readyForCard();
}