package com.skripiio.destinytriad.battle.engine;

import com.skripiio.destinytriad.card.IBattleCard;

public interface IBoardSquare {

	/** @return the element that this square contains if it has an element */
	public int getElement();

	/** @return true if this square is elemental */
	public boolean isElement();

	/** @return true if a card is on this board square */
	public boolean isTaken();

	/** @return the card that is on this square */
	public IBattleCard getCard();
	
	/** Sets a card on the square */
	public void setCard(IBattleCard pCard);
	
}
