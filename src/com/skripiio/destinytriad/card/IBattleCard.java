package com.skripiio.destinytriad.card;

import com.skripiio.destinytriad.battle.engine.BoardSquare;
import com.skripiio.destinytriad.battle.engine.IOnActionFinishedListener;

public interface IBattleCard {

	/**
	 * Flips the card on the game board, changing the cards ownership to the
	 * current players turn
	 * 
	 * @param onAnimationFinishedListener
	 *           the listener that fires after the animation is finished
	 */
	public void flip(final IOnActionFinishedListener onAnimationFinishedListener);

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

	/**
	 * Places the card on the game board
	 * 
	 * @param placeX
	 *           the x location of the square that the card is being placed onto
	 * @param placeY
	 *           the y location of the square that the card is being placed onto
	 * @param onAnimationFinishedListener
	 *           the listener that fires after the place animation has finished
	 */
	public void placeCard(final BoardSquare pBoardSquare,
			final IOnActionFinishedListener onAnimationFinishedListener);

	public void setFaceDown(boolean faceDown, final IOnActionFinishedListener listener);

	public void reset();

	public boolean isFaceDown();

	public void selectCard(final IOnActionFinishedListener listener);

	public void deSelectCard(final IOnActionFinishedListener listener);

	public int getPlayerID();

}
