package com.skripiio.destinytriad.card;

public class AbstractCard {

	private int[] mCardValuesNSEW;
	private int mElement;
	private int mPoints;
	private int mLevel;

	/** Creates an abstract card with all the cards information on it */
	public AbstractCard(int cardID) {
		int[] sides = { CardList.mCardSideValues[cardID][0],
				CardList.mCardSideValues[cardID][1], CardList.mCardSideValues[cardID][2],
				CardList.mCardSideValues[cardID][3] };
		mCardValuesNSEW = sides;
		mElement = CardList.mCardSideValues[cardID][4];
		mLevel = CardList.mCardSideValues[cardID][5];
		mPoints = CardList.mCardSideValues[cardID][6];
	}

	public int[] getCardValuesNSEW() {
		return mCardValuesNSEW;
	}

	public int getElement() {
		return mElement;
	}

	public int getPoints() {
		return mPoints;
	}

	public int getLevel() {
		return mLevel;
	}

}
