package com.skripiio.destinytriad.battle.engine;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Random;

import org.anddev.andengine.entity.scene.Scene;

import android.util.Log;

import com.skripiio.destinytriad.battle.Battle;
import com.skripiio.destinytriad.battle.TurnIndicator;
import com.skripiio.destinytriad.battle.rules.RuleSet;
import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.CardFactory;
import com.skripiio.destinytriad.card.IBattleCard;
import com.skripiio.destinytriad.card.selector.GameCardSelector;
import com.skripiio.destinytriad.card.selector.GameCardSelector.IOnCardsSelectedListener;

public class GameBattleEngine implements IBattleEngine {

	/** All 9 of the board squares in the game */
	private BoardSquare[] mBoardSquares;

	/** The 2 players */
	private IBattlePlayer[] mPlayers;

	/** Checker to see if the board is ready to accept a card. */
	private boolean isReadyForCard;

	/** Index of the current player */
	private int mCurrentPlayer;

	/** The Rule Set for this engine */
	private RuleSet mRuleSet;

	/** A counter that details how many cards are in flip */
	private int mCardFlipCounter;

	/** The turn indicator */
	private TurnIndicator mTurnIndicator;

	/** The Card Selector used at the start of the battle */
	private GameCardSelector mCardSelector;

	private Scene mScene;

	private Battle mBattle;

	public GameBattleEngine(Battle pBattle, IBattlePlayer[] pPlayers,
			BoardSquare[] pBoardSquares, RuleSet pRuleSet, TurnIndicator pTurnIndicator,
			Scene pScene, GameCardSelector pCardSelector) {
		mRuleSet = pRuleSet;
		mPlayers = pPlayers;
		mBattle = pBattle;
		mBoardSquares = pBoardSquares;
		mTurnIndicator = pTurnIndicator;
		mCardSelector = pCardSelector;
		mScene = pScene;
		initialize();
	}

	@Override
	public void initialize() {
		Log.v("BattleEngine", "Initializing.");
		isReadyForCard = false;
		onCardSelect(new IOnCardsSelectedListener() {

			@Override
			public void onCardsSelected(final ArrayList<Card> pPlayerCards) {
				// attach the cards onto the scene
				mBattle.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						for (int i = 0; i < pPlayerCards.size(); i++) {
							pPlayerCards.get(i).detachSelf();
							mScene.attachChild(pPlayerCards.get(i));
						}
					}
				});

				mPlayers[0].setPlayerCards(pPlayerCards);

				Random r = new Random();
				ArrayList<Card> mOpponentCards = new ArrayList<Card>();
				for (int i = 0; i < 5; i++) {
					Card c = CardFactory.createCard(mBattle.getEngine(), 105, 140,
							r.nextInt(19), 1, mBattle.mCardFont, mBattle.mCardGreenFont,
							mBattle.mCardRedFont, mBattle.mCardBorderTextureRegion.clone(),
							mBattle.mCardBackTextureRegion, mBattle.mCardBorderTextureRegion);
					mOpponentCards.add(c);
					mScene.attachChild(c);
				}
				
				mPlayers[1].setPlayerCards(mOpponentCards);
				// close card selector
				mCardSelector.onClose(new IOnActionFinishedListener() {

					@Override
					public void OnAnimationFinished() {
						mBattle.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								Log.v("BattleEngine", "Detaching Card Selector");
								mCardSelector.detachSelf();

							}
						});
						// do battle start
						onBattleStart(new IOnActionFinishedListener() {

							@Override
							public void OnAnimationFinished() {
								// do start turn
								// Do engines start turn sequence
								onTurnStart(new IOnActionFinishedListener() {

									@Override
									public void OnAnimationFinished() {
										// Do players start turn sequence
										mPlayers[mCurrentPlayer]
												.onStartTurn(new IOnActionFinishedListener() {

													@Override
													public void OnAnimationFinished() {
														isReadyForCard = true;
													}
												});
									}
								});
							}
						});

					}
				});

			}
		});

	}

	/** Selects the cards */
	public void onCardSelect(IOnCardsSelectedListener pOnCardsSelectedListener) {
		mScene.attachChild(mCardSelector);
		Log.v("BattleEngine", "Opening Card Selector");
		mCardSelector.onOpen(pOnCardsSelectedListener);
	}

	@Override
	public void onBattleStart(
			final IOnActionFinishedListener pOnAnimationFinishedListener) {
		Log.v("BattleEngine", "Starting Battle");
		// makes the turn indicator spin & decide which player goes first
		mTurnIndicator.onFirstTurn(new IOnActionFinishedListener() {

			@Override
			public void OnAnimationFinished() {
				// get the current player from the turn indicator, and finish
				// onBattleStart sequence
				mCurrentPlayer = mTurnIndicator.getPlayerTurn();
				pOnAnimationFinishedListener.OnAnimationFinished();
			}
		});
	}

	@Override
	public void onTurnStart(IOnActionFinishedListener pOnAnimationFinishedListener) {
		Log.v("BattleEngine", "Starting Turn...");
	}

	@Override
	public void onTurnFinish(
			final IOnActionFinishedListener pOnAnimationFinishedListener) {
		Log.v("BattleEngine", "Finishing Turn...");
		// make the turn indicator swap players
		mTurnIndicator.onChangeTurn(new IOnActionFinishedListener() {

			@Override
			public void OnAnimationFinished() {
				// finish onTurnFinish sequence
				pOnAnimationFinishedListener.OnAnimationFinished();
			}
		});

	}

	/** Checks to see if the turn should end */
	public void checkForEndTurn() {
		// if no cards are being flipped, end the turn
		if (mCardFlipCounter == 0) {
			endTurn();
		}
	}

	/**
	 * Ends the current turn & blocks players from making a move. Plays the
	 * onTurnFinish animation and the next players onStartAnimation. Then
	 * unblocks the pipeline for players to make a move
	 */
	@Override
	public void endTurn() {
		Log.v("BattleEngine", "Player's turn complete. Blocking play pipeline");
		// block all players moves from occuring during the animations
		isReadyForCard = false;
		if (!isGameOver()) {
			onTurnFinish(new IOnActionFinishedListener() {

				@Override
				public void OnAnimationFinished() {
					mCurrentPlayer = getNextPlayerNumber();
					Log.v("BattleEngine", "Finished Turn. Player " + mCurrentPlayer + " turn");
					// Do engines start turn sequence
					onTurnStart(new IOnActionFinishedListener() {

						@Override
						public void OnAnimationFinished() {
							Log.v("BattleEngine", "Starting players turn");
							// Do players start turn sequence
							mPlayers[mCurrentPlayer]
									.onStartTurn(new IOnActionFinishedListener() {

										@Override
										public void OnAnimationFinished() {
											Log.v("BattleEngine", "Opening play pipeline");
											// set the engine ready to take a card from a
											// player
											isReadyForCard = true;
										}
									});
						}
					});

				}
			});
		}
	}

	/** @return the index of the next player */
	public int getNextPlayerNumber() {
		return ((mCurrentPlayer + 1) % 2);
	}

	@Override
	public boolean isGameOver() {
		// checks to see if any board squares are empty
		for (int i = 0; i < mBoardSquares.length; i++) {
			if (!mBoardSquares[i].isTaken()) {
				// if a board square isn't taken, the game isn't over
				return false;
			}
		}
		return true;
	}

	@Override
	public IBoardSquare getBoardSquare(int pSquareNumber) {
		return mBoardSquares[pSquareNumber];
	}

	@Override
	public boolean isOpponentHandVisible() {
		return mRuleSet.isOpenRule();
	}

	@Override
	public ArrayList<Card> getOpponentHand() {
		return mPlayers[getNextPlayerNumber()].getCards();
	}

	@Override
	public void placeCard(IBattleCard pCard, final int pSquareNumber) {
		Log.v("BattleEngine", "Player " + mCurrentPlayer + " placing card in square "
				+ pSquareNumber);
		if (!isReadyForCard) {
			// player made an illegal move
			throw new InvalidParameterException("Engine is not ready for a card.");
		}

		// place the card on the board
		pCard.placeCard(mBoardSquares[pSquareNumber], new IOnActionFinishedListener() {

			@Override
			public void OnAnimationFinished() {
				// determine the rule outcomes after a card is placed
				determineRuleOutcomes(pSquareNumber);
			}
		});
	}

	@Override
	public IBattlePlayer getCurrentPlayer() {
		return mPlayers[mCurrentPlayer];
	}

	@Override
	public IBoardSquare[] getBoardSquares() {
		return mBoardSquares;
	}

	@Override
	public boolean readyForCard() {
		return isReadyForCard;
	}

	/**
	 * Goes through all active rules and calculates the outcome of a card being
	 * placed at the square number provided.
	 * 
	 * @param pSquareNumber
	 *           the square number on the board to calculate the rule outcomes
	 */
	private void determineRuleOutcomes(int pSquareNumber) {
		Log.v("BattleEngine", "Checking for flips");
		// check to see if any adjacent cards have been flipped without a combo
		checkForCardFlip(pSquareNumber, false);

		// check the same rule
		if (mRuleSet.isSameRule()) {
			Log.v("BattleEngine", "Checking for Same");
			checkSameRule(pSquareNumber);
		}

		// check the plus rule
		if (mRuleSet.isPlusRule()) {
			Log.v("BattleEngine", "Checking for Plus");
			checkPlusRule(pSquareNumber);
		}

	}

	/**
	 * Checks the surrounding cards to see if they can be flipped due to
	 * attacking number being higher than defenders number.
	 * 
	 * @param pSquareNumber
	 *           the square number to check the adjacent cards of
	 * @param pFlipComboActive
	 *           true if the cards should trigger combo after being flipped
	 */
	private void checkForCardFlip(int pSquareNumber, boolean pFlipComboActive) {
		checkWest(pSquareNumber, pFlipComboActive);
		checkEast(pSquareNumber, pFlipComboActive);
		checkSouth(pSquareNumber, pFlipComboActive);
		checkNorth(pSquareNumber, pFlipComboActive);
	}

	/** Checks the plus rule. This is a combo rule */
	private void checkPlusRule(int pSquareNumber) {
		IBattleCard west = getWestCard(pSquareNumber);
		IBattleCard east = getEastCard(pSquareNumber);
		IBattleCard north = getNorthCard(pSquareNumber);
		IBattleCard south = getSouthCard(pSquareNumber);

		int numPlussed = 0;
		boolean westPlussed = false;
		boolean eastPlussed = false;
		boolean southPlussed = false;
		boolean northPlussed = false;

		ArrayList<Integer> plusValues = new ArrayList<Integer>();

		if (west != null) {
			plusValues.add((mBoardSquares[pSquareNumber].getCard().getWest() + west
					.getEast()));
		}

		if (east != null) {
			plusValues.add((mBoardSquares[pSquareNumber].getCard().getEast() + east
					.getWest()));
		}

		if (north != null) {
			plusValues.add((mBoardSquares[pSquareNumber].getCard().getNorth() + north
					.getSouth()));
		}

		if (south != null) {
			plusValues.add((mBoardSquares[pSquareNumber].getCard().getSouth() + south
					.getNorth()));
		}

		if (numPlussed >= 2) {
			if (westPlussed) {
				checkForCardFlip(getBoardNum(pSquareNumber, DIRECTION_WEST), true);
			}
			if (eastPlussed) {
				checkForCardFlip(getBoardNum(pSquareNumber, DIRECTION_EAST), true);
			}
			if (northPlussed) {
				checkForCardFlip(getBoardNum(pSquareNumber, DIRECTION_NORTH), true);
			}
			if (southPlussed) {
				checkForCardFlip(getBoardNum(pSquareNumber, DIRECTION_SOUTH), true);
			}
		}

	}

	/**
	 * Checks the same rule. Also checks the same wall rule if it is enabled in
	 * this game. This is a combo rule.
	 */
	private void checkSameRule(final int squareNumber) {
		IBattleCard west = getWestCard(squareNumber);
		IBattleCard east = getEastCard(squareNumber);
		IBattleCard north = getNorthCard(squareNumber);
		IBattleCard south = getSouthCard(squareNumber);

		int numSame = 0;
		boolean westSame = false;
		boolean eastSame = false;
		boolean southSame = false;
		boolean northSame = false;

		if (west != null) {
			if (mBoardSquares[squareNumber].getCard().getWest() == west.getEast()) {
				numSame++;
				westSame = true;
			}
		}

		if (east != null) {
			if (mBoardSquares[squareNumber].getCard().getEast() == east.getWest()) {
				numSame++;
				eastSame = true;
			}
		}

		if (north != null) {
			if (mBoardSquares[squareNumber].getCard().getNorth() == north.getSouth()) {
				numSame++;
				northSame = true;
			}
		}

		if (south != null) {
			if (mBoardSquares[squareNumber].getCard().getSouth() == south.getNorth()) {
				numSame++;
				southSame = true;
			}
		}

		if (numSame >= 2) {
			if (westSame) {
				if (west.getPlayerID() != mCurrentPlayer) {
					west.flip(new IOnActionFinishedListener() {

						@Override
						public void OnAnimationFinished() {
							checkForCardFlip(getBoardNum(squareNumber, DIRECTION_WEST), true);

						}
					});
				}
			}
			if (eastSame) {
				if (east.getPlayerID() != mCurrentPlayer) {
					east.flip(new IOnActionFinishedListener() {

						@Override
						public void OnAnimationFinished() {
							checkForCardFlip(getBoardNum(squareNumber, DIRECTION_EAST), true);

						}
					});
				}
			}
			if (northSame) {
				if (north.getPlayerID() != mCurrentPlayer) {
					north.flip(new IOnActionFinishedListener() {

						@Override
						public void OnAnimationFinished() {
							checkForCardFlip(getBoardNum(squareNumber, DIRECTION_NORTH), true);

						}
					});
				}
			}
			if (southSame) {
				if (south.getPlayerID() != mCurrentPlayer) {
					south.flip(new IOnActionFinishedListener() {

						@Override
						public void OnAnimationFinished() {
							checkForCardFlip(getBoardNum(squareNumber, DIRECTION_SOUTH), true);
						}
					});
				}
			}
		}

	}

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
			if (squareNumber + 3 < this.mBoardSquares.length) {
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
	private IBattleCard getWestCard(int squareNumber) {
		if (squareNumber % 3 != 0) {
			return this.mBoardSquares[squareNumber - 1].getCard();
		} else {
			return null;
		}
	}

	private IBattleCard getEastCard(int squareNumber) {
		if (squareNumber % 3 != (2)) {
			return this.mBoardSquares[squareNumber + 1].getCard();
		} else {
			return null;
		}
	}

	private IBattleCard getNorthCard(int squareNumber) {
		if (squareNumber - 3 >= 0) {
			return this.mBoardSquares[squareNumber - 3].getCard();
		} else {
			return null;
		}
	}

	private IBattleCard getSouthCard(int squareNumber) {
		if (squareNumber + 3 < this.mBoardSquares.length) {
			return this.mBoardSquares[squareNumber + 3].getCard();
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
		IBattleCard westOf = getWestCard(squareNumber);
		if (westOf != null) {
			if (westOf.getPlayerID() != mCurrentPlayer) {
				if (this.mBoardSquares[squareNumber].getCard().getWest() > westOf.getEast()) {
					mCardFlipCounter++;
					if (combo) {
						westOf.flip(new IOnActionFinishedListener() {

							@Override
							public void OnAnimationFinished() {
								mCardFlipCounter--;
								checkForCardFlip(getBoardNum(squareNumber, DIRECTION_WEST), true);
								checkForEndTurn();
							}
						});
					} else {
						westOf.flip(new IOnActionFinishedListener() {

							@Override
							public void OnAnimationFinished() {
								mCardFlipCounter--;
								checkForEndTurn();
							}
						});
					}
					return true;
				}
			}
		}

		return false;
	}

	/** Checks card ownership on the east */
	private boolean checkEast(final int squareNumber, boolean combo) {
		IBattleCard eastOf = getEastCard(squareNumber);
		if (eastOf != null) {
			if (eastOf.getPlayerID() != mCurrentPlayer) {
				if (this.mBoardSquares[squareNumber].getCard().getEast() > eastOf.getWest()) {
					mCardFlipCounter++;
					if (combo) {
						eastOf.flip(new IOnActionFinishedListener() {

							@Override
							public void OnAnimationFinished() {
								mCardFlipCounter--;
								checkForCardFlip(getBoardNum(squareNumber, DIRECTION_EAST), true);
								checkForEndTurn();
							}
						});
					} else {
						eastOf.flip(new IOnActionFinishedListener() {

							@Override
							public void OnAnimationFinished() {
								mCardFlipCounter--;
								checkForEndTurn();
							}
						});
					}
					return true;
				}
			}
		}
		return false;
	}

	/** Checks the card ownership on the south */
	private boolean checkSouth(final int squareNumber, boolean combo) {
		IBattleCard southOf = getSouthCard(squareNumber);
		if (southOf != null) {
			if (southOf.getPlayerID() != mCurrentPlayer) {
				if (mBoardSquares[squareNumber].getCard().getSouth() > southOf.getNorth()) {
					mCardFlipCounter++;
					if (combo) {
						southOf.flip(new IOnActionFinishedListener() {

							@Override
							public void OnAnimationFinished() {
								mCardFlipCounter--;
								checkForCardFlip(getBoardNum(squareNumber, DIRECTION_SOUTH), true);
								checkForEndTurn();
							}
						});
					} else {
						southOf.flip(new IOnActionFinishedListener() {

							@Override
							public void OnAnimationFinished() {
								mCardFlipCounter--;
								checkForEndTurn();
							}
						});
					}
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks the card ownership on the north.
	 */
	private boolean checkNorth(final int squareNumber, boolean combo) {
		IBattleCard northOf = getNorthCard(squareNumber);
		if (northOf != null) {
			if (northOf.getPlayerID() != mCurrentPlayer) {
				if (mBoardSquares[squareNumber].getCard().getNorth() > northOf.getSouth()) {
					mCardFlipCounter++;
					if (combo) {
						// add that a card has been flipped
						northOf.flip(new IOnActionFinishedListener() {

							@Override
							public void OnAnimationFinished() {
								mCardFlipCounter--;
								checkForCardFlip(getBoardNum(squareNumber, DIRECTION_NORTH), true);
								checkForEndTurn();
							}
						});
					} else {
						northOf.flip(new IOnActionFinishedListener() {

							@Override
							public void OnAnimationFinished() {
								mCardFlipCounter--;
								checkForEndTurn();
							}
						});
					}
					return true;
				}
			}
		}
		return false;
	}
}
