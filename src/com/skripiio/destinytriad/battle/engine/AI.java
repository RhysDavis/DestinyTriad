package com.skripiio.destinytriad.battle.engine;

import com.skripiio.destinytriad.battle.player.ControllerBattleView;

public abstract class AI {

	private ControllerBattleView mLink;
	
	public AI() {
		
	}
	
	public ControllerBattleView getLink() {
		return mLink;
	}
	
	public abstract void onStartTurn();
}
