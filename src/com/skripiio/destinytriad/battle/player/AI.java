package com.skripiio.destinytriad.battle.player;

import com.skripiio.destinytriad.battle.engine.IBattlePlayer;
import com.skripiio.destinytriad.card.IBattleCard;

public abstract class AI {
	
	private IBattlePlayer mBattlePlayer;
	
	public AI() {
	}
	
	public void setBattlePlayer(IBattlePlayer pBattlePlayer) {
		mBattlePlayer = pBattlePlayer;
	}
	
	public IBattlePlayer getBattlePlayer() {
		return mBattlePlayer;
	}
	
	public abstract void onDoTurn();
	public abstract void analyzeCard(IBattleCard pCard);
	public abstract void onAnalysisComplete();
	
}
