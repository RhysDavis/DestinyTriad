package com.skripiio.destinytriad.battle;

import java.util.ArrayList;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;

import com.skripiio.destinytriad.battle.rules.RuleSet;
import com.skripiio.destinytriad.card.Card;

public class BattleResultsScene extends Scene {

	private Rectangle[] mPlayer1CardRow;
	private Rectangle[] mPlayer2CardRow;

	private Font mFont;
	
	private Text mWinnerText;
	private Text mExperienceText;
	private Text mExpTotal;
	
	public BattleResultsScene(RuleSet rs, int winnerID, ArrayList<Card> player1Cards,
			ArrayList<Card> player2Cards) {
		
	}
	
	
	
}
