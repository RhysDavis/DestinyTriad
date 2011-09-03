package com.skripiio.destinytriad.battle.engine;

import java.util.ArrayList;

import org.anddev.andengine.entity.Entity;

import android.util.Log;

import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.IBattleCard;

public abstract class BattlePlayer extends Entity implements IBattlePlayer {

	private IBattleEngine mBattleEngine;

	private ArrayList<Card> mPlayerCards;

	private int mPlayerNumber;

	public BattlePlayer(int pPlayerNumber) {
		mPlayerNumber = pPlayerNumber;
	}

	@Override
	public void setPlayerCards(ArrayList<Card> pPlayerCards) {
		Log.v("BattleEngine", "Player - Player's Cards Set");
		mPlayerCards = pPlayerCards;
	}

	@Override
	public ArrayList<Card> getCards() {
		return mPlayerCards;
	}

	public IBattleEngine getBattleEngine() {
		return mBattleEngine;
	}

	public void setBattleEngine(IBattleEngine pBattleEngine) {
		mBattleEngine = pBattleEngine;
	}

	@Override
	public int getPlayerNumber() {
		return mPlayerNumber;
	}

	@Override
	public void playCard(IBattleCard pCard, int pSquareNumber) {
		mBattleEngine.placeCard(pCard, pSquareNumber);
	}

}
