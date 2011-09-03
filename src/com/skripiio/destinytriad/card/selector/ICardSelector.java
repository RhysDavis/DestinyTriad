package com.skripiio.destinytriad.card.selector;

import java.util.ArrayList;

import com.skripiio.destinytriad.battle.engine.IOnActionFinishedListener;
import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.selector.GameCardSelector.IOnCardsSelectedListener;

public interface ICardSelector {

	/** Retrieve the cards the player selected */
	public ArrayList<Card> getSelectedCards();

	/** On Open the screen */
	public void onOpen(IOnCardsSelectedListener pOnCardsSelectedListener);

	/** On Close the screen */
	public void onClose(IOnActionFinishedListener pOnAnimationFinishedListener);

}
