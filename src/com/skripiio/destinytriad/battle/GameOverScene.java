//package com.skripiio.destinytriad.battle;
//
//import java.util.ArrayList;
//
//import org.anddev.andengine.entity.scene.Scene;
//import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
//import org.anddev.andengine.entity.scene.background.ColorBackground;
//import org.anddev.andengine.entity.text.Text;
//import org.anddev.andengine.input.touch.TouchEvent;
//import org.anddev.andengine.opengl.font.Font;
//import org.anddev.andengine.util.HorizontalAlign;
//
//import com.skripiio.destinytriad.card.Card;
//
//public class GameOverScene extends Scene implements IOnSceneTouchListener {
//
//	Text mGameStatusText;
//	Text mExpText;
//	Text mExpNumbers;
//
//	/**
//	 * A scene showing 2 rows of cards. One row for each player. The winner gets
//	 * to pick cards based on rules of the game to take from the opponent, or to
//	 * display none at all if no take rules are being played.
//	 */
//	public GameOverScene(int winnerID, ArrayList<Card> playerCards,
//			ArrayList<Card> opponentCards, Font font) {
//		// set white background
//		this.setBackground(new ColorBackground(1, 1, 1));
//
//		if (winnerID == BattleScene.PLAYER_USER) {
//			mGameStatusText = new Text(0, 0, font, "VICTORY", HorizontalAlign.CENTER);
//		} else {
//			mGameStatusText = new Text(0, 0, font, "DEFEAT", HorizontalAlign.CENTER);			
//		}
//		
//		mExpText = new Text(0, 30, font, "EXP");
//		mExpNumbers = new Text(50, 30, font, "145/200");
//		
//		
//	}
//
//	/** Handles touching cards */
//	@Override
//	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
//
//		return false;
//	}
//
//}
