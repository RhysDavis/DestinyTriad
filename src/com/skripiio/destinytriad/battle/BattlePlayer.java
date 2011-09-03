package com.skripiio.destinytriad.battle;

import java.util.ArrayList;

import org.anddev.andengine.entity.Entity;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;

import com.skripiio.destinytriad.card.Card;

public abstract class BattlePlayer extends Entity {

	public static final int STATE_START = 0;
	public static final int STATE_DO = 1;

	private BattleEngine mBattleEngine;
	public ArrayList<Card> mPlayerCards;

	private int mState = 0;

	private int mPlayerNumber;

	public BattlePlayer(ArrayList<Card> playerCards, int playerNumber) {
		mPlayerCards = playerCards;
		mPlayerNumber = playerNumber;
	}

	public ArrayList<Card> getCards() {
		return mPlayerCards;
	}

	public abstract boolean isHuman();

	public int getPlayerNumber() {
		return this.mPlayerNumber;
	}

	public BattleEngine getEngine() {
		return this.mBattleEngine;
	}

	public void setEngine(BattleEngine engine) {
		this.mBattleEngine = engine;
	}

	public void onMananagedUpdate(float pSecondsElapsed) {
		switch (mState) {
			case (STATE_START):
				onStartTurn();
				break;
			case (STATE_DO):
				onDoTurn();
				break;
		}
	}

	public void setState(int state) {
		this.mState = state;
	}

	public abstract void onStartTurn();

	/**
	 * Gets the player to make a turn. @return true if they have completed their
	 * turn and placed a card
	 */
	public abstract boolean onDoTurn();

}
