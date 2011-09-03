package com.skripiio.destinytriad.battle;

import java.util.ArrayList;

import com.skripiio.destinytriad.ai.ArtificialIntelligence;
import com.skripiio.destinytriad.card.Card;

public class MockPlayer extends ArtificialIntelligence {

	public MockPlayer(ArrayList<Card> cards, int playerNumber) {
		super(cards, playerNumber);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void analyseCard(Card c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Card getCardToPlay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBoardSquareToPlay() {
		// TODO Auto-generated method stub
		return 0;
	}

}
