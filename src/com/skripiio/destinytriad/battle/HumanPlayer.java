//package com.skripiio.destinytriad.battle;
//
//import java.util.ArrayList;
//
//import org.anddev.andengine.entity.primitive.Rectangle;
//import org.anddev.andengine.entity.scene.Scene;
//import org.anddev.andengine.input.touch.TouchEvent;
//
//import android.util.Log;
//
//import com.skripiio.destinytriad.card.Card;
//
//public class HumanPlayer extends BattlePlayer {
//
//	private Card mSelectedCard;
//	private int mSquareSelected;
//	private boolean selectedSquare = false;
//
//	public HumanPlayer(ArrayList<Card> playerCards, int playerNumber) {
//		super(playerCards, playerNumber);
//	}
//
//	@Override
//	public boolean onDoTurn() {
//		if (selectedSquare) {
//			getEngine().placeCard(mSelectedCard, mSquareSelected, r);
//			mSelectedCard = null;
//			selectedSquare = false;
//			return true;
//		}
//		return false;
//	}
//
//	public void selectCard(Card c) {
//		mSelectedCard = c;
//	}
//
//	Rectangle r;
//
//	public void selectLocation(int square, Rectangle rect) {
//		mSquareSelected = square;
//		r = rect;
//		selectedSquare = true;
//
//	}
//
//	@Override
//	public boolean isHuman() {
//		return true;
//	}
//
//	@Override
//	public void onStartTurn() {
//		setState(STATE_DO);
//
//	}
//}
