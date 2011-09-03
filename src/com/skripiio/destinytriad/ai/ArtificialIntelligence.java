package com.skripiio.destinytriad.ai;

import java.util.ArrayList;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;

import android.util.Log;

import com.skripiio.destinytriad.battle.BattlePlayer;
import com.skripiio.destinytriad.card.Card;

public abstract class ArtificialIntelligence extends BattlePlayer {

	public ArrayList<Card> mCards;

	private Brain mBrain;

	private boolean cardPlayed = false;

	public ArtificialIntelligence(ArrayList<Card> cards, int playerNumber) {
		super(cards, playerNumber);
		mCards = new ArrayList<Card>();
		mCards.addAll(cards);
		mBrain = new Brain();
	}

	 @Override
	public boolean isHuman() {
		return false;
	}
	
	public int cardThinkCounter = 0;
	public boolean brainThinking = false;

	/** Thinks */
	public boolean onDoTurn() {
		if (!cardPlayed) {
			if (!brainThinking) {
				for (int i = cardThinkCounter; i < mCards.size(); i++) {
					if (!mCards.get(i).isPlaced()) {
						brainThinking = true;

						mCards.get(i).selectCard();
						for (int j = 0; j < mCards.size(); j++) {
							if (j != i)
								mCards.get(j).deSelectCard();
						}
						mBrain = new Brain();
						mBrain.start();
						cardThinkCounter++;
						break;
					}
				}

				if (brainThinking == false) {
					cardThinkCounter = 0;
					getCardToPlay().deSelectCard();
					cardPlayed = true;
					playCard(getCardToPlay(), getBoardSquareToPlay());

				}
			}
		}
		return false;

	}

	/** Calculates how well the card would play against the opponent */
	public abstract void analyseCard(Card c);

	/** @return the card the AI player wants to play */
	public abstract Card getCardToPlay();

	/** @return the board square the card should be placed on */
	public abstract int getBoardSquareToPlay();

	/** Plays cards */
	public void playCard(Card cardToPlay, int boardSquareToPlay) {
		Log.v("Artificial Intelligence", "Board Num: " + boardSquareToPlay);
		Rectangle[][] rBoard = getEngine().getBattleScene().getBattleBoard();
		int counter = 0;
		Rectangle r = null;
		for (int i = 0; i < rBoard.length; i++) {
			for (int j = 0; j < rBoard[i].length; j++) {
				if (counter == boardSquareToPlay) {
					r = rBoard[i][j];
				}
				counter++;
			}
		}

		getEngine().placeCard(cardToPlay, boardSquareToPlay, r);
	}

	public class Brain extends Thread {

		private Card thinkingCard;

		public Brain() {

		}

		public void setCardToThinkAbout(Card c) {
			thinkingCard = c;
		}

		@Override
		public void run() {
			super.run();
			analyseCard(thinkingCard);
			brainThinking = false;
		}

	}


	@Override
	public void onStartTurn() {
		this.cardPlayed = false;
		setState(STATE_DO);
	}
}
