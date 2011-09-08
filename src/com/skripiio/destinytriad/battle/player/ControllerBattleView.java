package com.skripiio.destinytriad.battle.player;

import java.util.ArrayList;

import com.skripiio.destinytriad.battle.engine.Battle;
import com.skripiio.destinytriad.battle.engine.IBattle;
import com.skripiio.destinytriad.battle.engine.IBattlePlayer;
import com.skripiio.destinytriad.battle.engine.IBoardSquare;
import com.skripiio.destinytriad.battle.rules.Rule;

public class ControllerBattleView {

	private IBattle mGameBattleScene;

	private int mPlayerID;

	public ControllerBattleView(IBattle pGameBattleScene, int pPlayerID) {
		mPlayerID = pPlayerID;
	}

	public IBattlePlayer getSelf() {
		return mGameBattleScene.getPlayers()[mPlayerID];
	}

	/**
	 * @return your opponent if the open rule is in play. Else it throws an
	 *         exception
	 */
	public IBattlePlayer getOpponent() {
		if (!getGameRules().contains(Rule.Open)) {
			throw new IllegalAccessError("The Open rule is not in use");
		}

		return mGameBattleScene.getPlayers()[(mPlayerID + 1) % 2];
	}

	public ArrayList<Rule> getGameRules() {
		return null;
	}

	public IBoardSquare[] getBoardSquares() {
		return null;
	}

}
