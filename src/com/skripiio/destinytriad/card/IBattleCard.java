package com.skripiio.destinytriad.card;


public interface IBattleCard {
	
	/** @return the west value of this card */
	public int getWest();

	/** @return the east value of this card */
	public int getEast();

	/** @return the north value of this card */
	public int getNorth();

	/** @return the south value of this card */
	public int getSouth();

	/** @return the element of the card. -1 if no element */
	public int getElement();

	/**
	 * @return true if the card has been placed on the board
	 */
	public boolean isPlaced();

	public boolean isFaceDown();

	public int getPlayerID();
	
	

}
