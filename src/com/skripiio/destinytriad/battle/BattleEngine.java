package com.skripiio.destinytriad.battle;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.primitive.Rectangle;

import android.util.Log;
import android.view.animation.Animation.AnimationListener;

import com.skripiio.destinytriad.battle.rules.RuleSet;
import com.skripiio.destinytriad.card.Card;

public class BattleEngine extends Entity {

	public interface BattleOverListener {
		public void onGameOver(int victoriousPlayer);
	}

	private BattleScene mBattleScene;

	private BattlePlayer mPlayer1;
	private BattlePlayer mPlayer2;

	private ArrayList<Card> mPlayer1CardsInGame;
	private ArrayList<Card> mPlayer2CardsInGame;

	/**
	 * A reference to one of the cards each player owns and where they are on the
	 * board
	 */
	public Card[] mCardsOnBoard;

	private int mCurrentPlayer;

	private BattleOverListener mBattleOverListener;

	private RuleSet mRuleSet;

	/** A List of flipped items that need to be checked before a turn ends */
	private ArrayList<Integer> mBoardFlipChecks;

	private TurnIndicator mTurnIndicator;

	/**
	 * @param playerCards
	 *           the cards that the user player is bringing to battle
	 * @param opponentCards
	 *           the cards that the opponent is bringing to the battle
	 */
	public BattleEngine(BattleScene scene, RuleSet ruleSet,
			BattleOverListener bOverListener, BattlePlayer p1, BattlePlayer p2,
			TurnIndicator pTurnIndicator) {
		mTurnIndicator = pTurnIndicator;
		mCardsOnBoard = new Card[9];
		mBoardFlipChecks = new ArrayList<Integer>();
		mBattleOverListener = bOverListener;
		mRuleSet = ruleSet;
		mCurrentPlayer = 0;
		mPlayer1 = p1;
		mPlayer2 = p2;

		mPlayer1CardsInGame = new ArrayList<Card>();
		mPlayer2CardsInGame = new ArrayList<Card>();

		mPlayer1CardsInGame.addAll(mPlayer1.getCards());
		mPlayer2CardsInGame.addAll(mPlayer2.getCards());

		p1.setEngine(this);
		p2.setEngine(this);
		this.mBattleScene = scene;
		
		mBattleScene.attachChild(mTurnIndicator);
		initialize();
	}

	private boolean isFirstTurnDecider;

	/** Initialize the game */
	private void initialize() {
		isFirstTurnDecider = true;
		// place cards in the right places
		for (int i = 0; i < mPlayer1CardsInGame.size(); i++) {
			mPlayer1CardsInGame.get(i).setPosition(
					mBattleScene.getPlayerHandRectangles()[i].getX(),
					mBattleScene.getPlayerHandRectangles()[i].getY());
			mPlayer2CardsInGame.get(i).setPosition(
					mBattleScene.getOpponentHandRectangles()[i].getX(),
					mBattleScene.getOpponentHandRectangles()[i].getY());
			mBattleScene.attachChild(mPlayer1CardsInGame.get(i));
			mBattleScene.attachChild(mPlayer2CardsInGame.get(i));
		}

		// if open rule, flip cards
		if (!mRuleSet.isOpenRule()) {
			for (int i = 0; i < mPlayer2.mPlayerCards.size(); i++) {
				if (!mPlayer2.mPlayerCards.get(i).isPlaced()) {
					mPlayer2.mPlayerCards.get(i).instantFlipToBack(true);
				}
			}
		}

		// if elemental rule, make some squares elemental

	}

	public BattleScene getBattleScene() {
		return this.mBattleScene;
	}

	/** @return the current battle player who's turn it is */
	public BattlePlayer getCurrentBattlePlayer() {
		if (mCurrentPlayer == BattleScene.PLAYER_OPP) {
			return mPlayer2;
		} else {
			return mPlayer1;
		}
	}

	private boolean cardHasBeenPlacedThisTurn = false;

	/** Places a card on the board */
	public void placeCard(final Card card, final int squareNumber, Rectangle r) {
		Log.v("BattleEngine", "Player " + (mCurrentPlayer + 1)
				+ " has placed a card in square " + squareNumber);
		// assert that no card already exists on that square
		if (mCardsOnBoard[squareNumber] != null) {
			throw new InvalidParameterException("A card exists on that square");
		}

		// make sure all other cards are deselected
		for (int i = 0; i < mPlayer1.mPlayerCards.size(); i++) {
			mPlayer1.mPlayerCards.get(i).deSelectCard();
		}
		for (int i = 0; i < mPlayer2.mPlayerCards.size(); i++) {
			mPlayer2.mPlayerCards.get(i).deSelectCard();
		}

		mCardsOnBoard[squareNumber] = card;

	/*	card.placeCardDown(r.getX(), r.getY(), new AnimationListener() {

			@Override
			public void onAnimationFinished() {
				card.setPlaced();
				if (squareNumber == 0 || squareNumber == 5) {
					card.setElementalBonus(false);
				}
				if (squareNumber == 2 || squareNumber == 7) {
					card.setElementalBonus(true);
				}
				cardHasBeenPlacedThisTurn = true;
				determineRuleOutcomes(squareNumber);

			}
		});
		*/
	}

	boolean doing = false;

	/** Updates Shit */
	public void onManagedUpdate(float pSecondsElapsed) {
		if (!isFirstTurnDecider) {
			getCurrentBattlePlayer().onMananagedUpdate(pSecondsElapsed);

			if (cardHasBeenPlacedThisTurn) {
				if (mBoardFlipChecks.size() == 0) {
					endTurn();
				}
			}
		} else {
			if (!doing) {
				doing = true;
			//	mTurnIndicator.onFirstTurn(new IOnAnimationFinishedListener() {

			//		@Override
			//		public void onAnimationFinished() {
		//				mCurrentPlayer = mTurnIndicator.getPlayerTurn();
//
				//	}
				//});
			}
		}
	}

	/** Ends the Game */
	private void doGameOver() {
		// count up cards
		int playerCards = 0;
		int opponentCards = 0;

		for (int i = 0; i < mPlayer1.mPlayerCards.size(); i++) {
			if (mPlayer1.mPlayerCards.get(i).getPlayerID() == BattleScene.PLAYER_OPP) {
				opponentCards++;
			} else {
				playerCards++;
			}
		}
		for (int i = 0; i < mPlayer2.mPlayerCards.size(); i++) {
			if (mPlayer2.mPlayerCards.get(i).getPlayerID() == BattleScene.PLAYER_OPP) {
				opponentCards++;
			} else {
				playerCards++;
			}
		}
		int victoriousPlayer;
		if (playerCards > opponentCards) {
			victoriousPlayer = BattleScene.PLAYER_USER;
		} else if (playerCards < opponentCards) {
			victoriousPlayer = BattleScene.PLAYER_OPP;
		} else {
			victoriousPlayer = BattleScene.DRAW;
			if (mRuleSet.isSuddenDeathRule()) {
				// reset and do again!
			}
		}

		mBattleOverListener.onGameOver(victoriousPlayer);
	}

	/** Ends the current turn, and allows the next player to make a turn */
	private void endTurn() {
		if (isGameOver()) {
			doGameOver();
		}
		/*
		mTurnIndicator.onChangeTurn(new IOnAnimationFinishedListener() {

			@Override
			public void onAnimationFinished() {
				mCurrentPlayer = (mCurrentPlayer + 1) % 2;
				Log.v("BattleEngine", "End Turn");
				Log.v("BattleEngine", "Player " + (mCurrentPlayer + 1) + "'s turn");

				cardHasBeenPlacedThisTurn = false;
				getCurrentBattlePlayer().onStartTurn();
			}
		});
*/
	}

	/**
	 * Goes through all active rules and calculates the outcome of a card being
	 * placed at the square number provided.
	 * 
	 * @param squareNumber
	 *           the square number on the board to calculate the rule outcomes
	 */
	private void determineRuleOutcomes(int squareNumber) {
		checkForCardFlip(squareNumber, false);

		if (mRuleSet.isSameRule()) {
			Log.d("BattleEngine", "Checking Same Rule");
	//		checkSameRule(squareNumber);
		}

		if (mRuleSet.isPlusRule()) {
			checkPlusRule(squareNumber);
		}

	}

	/**
	 * Checks the surrounding cards to see if they can be flipped due to
	 * attacking number being higher than defenders number
	 */
	private void checkForCardFlip(int squareNumber, boolean combo) {
		checkWest(squareNumber, combo);
		checkEast(squareNumber, combo);
		checkSouth(squareNumber, combo);
		checkNorth(squareNumber, combo);
	}

	/** Checks the plus rule . This is a combo rule */
	private void checkPlusRule(int squareNumber) {
		Card west = getWestCard(squareNumber);
		Card east = getEastCard(squareNumber);
		Card north = getNorthCard(squareNumber);
		Card south = getSouthCard(squareNumber);

		int numPlussed = 0;
		boolean westPlussed = false;
		boolean eastPlussed = false;
		boolean southPlussed = false;
		boolean northPlussed = false;

		ArrayList<Integer> plusValues = new ArrayList<Integer>();

		if (west != null) {
			plusValues.add((mCardsOnBoard[squareNumber].getWest() + west.getEast()));
		}

		if (east != null) {
			plusValues.add((mCardsOnBoard[squareNumber].getEast() + east.getWest()));
		}

		if (north != null) {
			plusValues.add((mCardsOnBoard[squareNumber].getNorth() + north.getSouth()));
		}

		if (south != null) {
			plusValues.add((mCardsOnBoard[squareNumber].getSouth() + south.getNorth()));
		}

		if (numPlussed >= 2) {
			if (westPlussed) {
				checkForCardFlip(getBoardNum(squareNumber, DIRECTION_WEST), true);
			}
			if (eastPlussed) {
				checkForCardFlip(getBoardNum(squareNumber, DIRECTION_EAST), true);
			}
			if (northPlussed) {
				checkForCardFlip(getBoardNum(squareNumber, DIRECTION_NORTH), true);
			}
			if (southPlussed) {
				checkForCardFlip(getBoardNum(squareNumber, DIRECTION_SOUTH), true);
			}
		}

	}

	/**
	 * Checks the same rule. Also checks the same wall rule if it is enabled in
	 * this game. This is a combo rule.
	 */
/*
	private void checkSameRule(final int squareNumber) {
		Card west = getWestCard(squareNumber);
		Card east = getEastCard(squareNumber);
		Card north = getNorthCard(squareNumber);
		Card south = getSouthCard(squareNumber);

		int numSame = 0;
		boolean westSame = false;
		boolean eastSame = false;
		boolean southSame = false;
		boolean northSame = false;

		if (west != null) {
			if (mCardsOnBoard[squareNumber].getWest() == west.getEast()) {
				numSame++;
				westSame = true;
			}
		}

		if (east != null) {
			if (mCardsOnBoard[squareNumber].getEast() == east.getWest()) {
				numSame++;
				eastSame = true;
			}
		}

		if (north != null) {
			if (mCardsOnBoard[squareNumber].getNorth() == north.getSouth()) {
				numSame++;
				northSame = true;
			}
		}

		if (south != null) {
			if (mCardsOnBoard[squareNumber].getSouth() == south.getNorth()) {
				numSame++;
				southSame = true;
			}
		}

		if (numSame >= 2) {
			if (westSame) {
				if (west.getPlayerID() != getCurrentPlayer()) {
					west.flipCard(new CardFlipListener() {

						@Override
						public void onCardFlipped() {
							checkForCardFlip(getBoardNum(squareNumber, DIRECTION_WEST), true);

						}
					});
				}
			}
			if (eastSame) {
				if (east.getPlayerID() != getCurrentPlayer()) {
					east.flipCard(new CardFlipListener() {

						@Override
						public void onCardFlipped() {
							checkForCardFlip(getBoardNum(squareNumber, DIRECTION_EAST), true);

						}
					});
				}
			}
			if (northSame) {
				if (north.getPlayerID() != getCurrentPlayer()) {
					north.flipCard(new CardFlipListener() {

						@Override
						public void onCardFlipped() {
							checkForCardFlip(getBoardNum(squareNumber, DIRECTION_NORTH), true);

						}
					});
				}
			}
			if (southSame) {
				if (south.getPlayerID() != getCurrentPlayer()) {
					south.flipCard(new CardFlipListener() {

						@Override
						public void onCardFlipped() {
							checkForCardFlip(getBoardNum(squareNumber, DIRECTION_SOUTH), true);

						}
					});
				}
			}
		}

	}
*/
	private static final int DIRECTION_NORTH = 0;
	private static final int DIRECTION_SOUTH = 1;
	private static final int DIRECTION_EAST = 2;
	private static final int DIRECTION_WEST = 3;

	/**
	 * @return the number of the square to the direction specified. @return -1 if
	 *         no square exists
	 */

	
	private int getBoardNum(int squareNumber, int direction) {
		if (direction == DIRECTION_NORTH) {
			if (squareNumber - 3 >= 0) {
				return (squareNumber - 3);
			}
		} else if (direction == DIRECTION_SOUTH) {
			if (squareNumber + 3 < this.mCardsOnBoard.length) {
				return (squareNumber + 3);
			}

		} else if (direction == DIRECTION_EAST) {
			if (squareNumber % 3 != 2) {
				return (squareNumber + 1);
			}
		} else if (direction == DIRECTION_WEST) {
			if (squareNumber % 3 != 0) {
				return (squareNumber - 1);
			}
		}

		return -1;
	}

	/** Gets the card to the west of the square number */
	private Card getWestCard(int squareNumber) {
		if (squareNumber % 3 != 0) {
			return this.mCardsOnBoard[squareNumber - 1];
		} else {
			return null;
		}
	}

	private Card getEastCard(int squareNumber) {
		if (squareNumber % 3 != (2)) {
			return this.mCardsOnBoard[squareNumber + 1];
		} else {
			return null;
		}
	}

	private Card getNorthCard(int squareNumber) {
		if (squareNumber - 3 >= 0) {
			return this.mCardsOnBoard[squareNumber - 3];
		} else {
			return null;
		}
	}

	private Card getSouthCard(int squareNumber) {
		if (squareNumber + 3 < this.mCardsOnBoard.length) {
			return this.mCardsOnBoard[squareNumber + 3];
		} else {
			return null;
		}
	}

	/**
	 * Checks card ownership on the west
	 * 
	 * @param combo
	 */
	private boolean checkWest(final int squareNumber, boolean combo) {
		// if there is a west card
/*
		Card westOf = getWestCard(squareNumber);
		if (westOf != null) {
			if (westOf.getPlayerID() != mCurrentPlayer) {
				if (this.mCardsOnBoard[squareNumber].getWest() > westOf.getEast()) {
					mBoardFlipChecks.add(squareNumber);
					if (combo) {
						westOf.flipCard(new CardFlipListener() {

							@Override
							public void onCardFlipped() {
								mBoardFlipChecks.remove((Integer) squareNumber);
								checkForCardFlip(getBoardNum(squareNumber, DIRECTION_WEST), true);
							}
						});
					} else {
						westOf.flipCard(new CardFlipListener() {

							@Override
							public void onCardFlipped() {
								mBoardFlipChecks.remove((Integer) squareNumber);
							}
						});
					}
					return true;
				}
			}
		}
*/
		return false;
	}

	/** Checks card ownership on the east */
	private boolean checkEast(final int squareNumber, boolean combo) {

		/*Card eastOf = getEastCard(squareNumber);
		if (eastOf != null) {
			if (eastOf.getPlayerID() != mCurrentPlayer) {
				if (this.mCardsOnBoard[squareNumber].getEast() > eastOf.getWest()) {
					mBoardFlipChecks.add(squareNumber);
					if (combo) {
						eastOf.flipCard(new CardFlipListener() {

							@Override
							public void onCardFlipped() {
								mBoardFlipChecks.remove((Integer) squareNumber);
								checkForCardFlip(getBoardNum(squareNumber, DIRECTION_EAST), true);

							}
						});
					} else {
						eastOf.flipCard(new CardFlipListener() {

							@Override
							public void onCardFlipped() {
								mBoardFlipChecks.remove((Integer) squareNumber);

							}
						});
					}
					return true;
				}
				
			}
			
		}
	*/
		return false;
	
	}

	/** Checks the card ownership on the south */
	private boolean checkSouth(final int squareNumber, boolean combo) {
		/*
		Card southOf = getSouthCard(squareNumber);
		if (southOf != null) {
			if (southOf.getPlayerID() != mCurrentPlayer) {
				if (mCardsOnBoard[squareNumber].getSouth() > southOf.getNorth()) {
					mBoardFlipChecks.add(squareNumber);
					if (combo) {
						southOf.flipCard(new CardFlipListener() {

							@Override
							public void onCardFlipped() {
								mBoardFlipChecks.remove((Integer) squareNumber);
								checkForCardFlip(getBoardNum(squareNumber, DIRECTION_SOUTH), true);

							}
						});
					} else {
						southOf.flipCard(new CardFlipListener() {

							@Override
							public void onCardFlipped() {
								mBoardFlipChecks.remove((Integer) squareNumber);
							}
						});
					}
					return true;
				}
			}
		}
*/
		return false;
	}

	/**
	 * Checks the card ownership on the north.
	 */
	private boolean checkNorth(final int squareNumber, boolean combo) {
		/*
		Card northOf = getNorthCard(squareNumber);
		if (northOf != null) {
			if (northOf.getPlayerID() != mCurrentPlayer) {
				if (mCardsOnBoard[squareNumber].getNorth() > northOf.getSouth()) {
					mBoardFlipChecks.add(squareNumber);
					if (combo) {
						// add that a card has been flipped
						northOf.flipCard(new CardFlipListener() {

							@Override
							public void onCardFlipped() {
								mBoardFlipChecks.remove((Integer) squareNumber);
								checkForCardFlip(getBoardNum(squareNumber, DIRECTION_NORTH), true);
							}
						});
					} else {
						northOf.flipCard(new CardFlipListener() {

							@Override
							public void onCardFlipped() {
								mBoardFlipChecks.remove((Integer) squareNumber);
							}
						});
					}
					return true;
				}
			}
		}
		*/
		return false;
	}

	/** Returns the cards on the board */
	public Card[] getCardsOnBoard() {
		return mCardsOnBoard;
	}

	/** @return true if the game is over */
	private boolean isGameOver() {
		for (int i = 0; i < 9; i++) {
			if (mCardsOnBoard[i] == null) {
				return false;
			}
		}
		return true;
	}

	/** @return the ID of the current player */
	public int getCurrentPlayer() {
		return this.mCurrentPlayer;
	}

}
