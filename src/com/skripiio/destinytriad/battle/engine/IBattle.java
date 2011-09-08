package com.skripiio.destinytriad.battle.engine;

import com.skripiio.destinytriad.battle.rules.Rule;

public interface IBattle {

	// model & view <-- the controller calls 'do stuff' on this
	
	
	// get players
	public BattlePlayer[] getPlayers();
	
	// get game board state
	public Rule[] getRules();
	
	public IBoardSquare[] getBoardSquares();
	
	// controller needs to be able to attach listeners since this shit is also
	// the view

}
