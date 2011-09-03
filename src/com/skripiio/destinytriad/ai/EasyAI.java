package com.skripiio.destinytriad.ai;

import java.util.ArrayList;

import com.skripiio.destinytriad.card.Card;

public class EasyAI extends ArtificialIntelligence {	
	
	public EasyAI(ArrayList<Card> cards, int playerNum) {
		super(cards, playerNum);
		
	}

	@Override
	public void analyseCard(Card c) {
		for (int i = 0; i < 99999; i++) {
			for (int j = 0; j < 99; j++) {
				
			}
		}
	}

	@Override
	public Card getCardToPlay() {
		for (int i = 0; i < mCards.size(); i++) {
			if (!mCards.get(i).isPlaced()) {
				return mCards.get(i);
			}
		}
		return null;
	}

	@Override
	public int getBoardSquareToPlay() {
		for (int i = 0; i <getEngine().getCardsOnBoard().length; i++) {
			
				if (getEngine().getCardsOnBoard()[i] == null) {
					return i;
				}
			
		}
		return 0;
	}

}
