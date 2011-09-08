package com.skripiio.destinytriad.battle.engine;

import com.skripiio.destinytriad.card.IBattleCard;

public interface IBattlePlayer {

	public IBattleCard[] getCards();

	public boolean playCard(int pCardNumInHand, int pSquareNumber);
}
