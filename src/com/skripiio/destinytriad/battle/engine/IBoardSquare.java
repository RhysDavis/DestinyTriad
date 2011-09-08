package com.skripiio.destinytriad.battle.engine;

import com.skripiio.destinytriad.card.Card;

/** The players interface for the board square */
public interface IBoardSquare {

	/** @return the element that this square contains if it has an element */
	public int getElement();

	/** @return true if this square is elemental */
	public boolean isElement();

	/** @return true if a card is on this board square */
	public boolean isCardHere();

	/** @return the card that is on this square */
	public Card getCard();

	/** @return the number of this board square, 0-9 inclusive*/
	public int getBoardSquareNumber();
	
}
