package com.skripiio.destinytriad.battle;

import java.util.Random;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.IModifier.IModifierListener;

import android.util.Log;


public class TurnIndicator extends Sprite {

	
	public TurnIndicator(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);

	}

	public void onChangeTurn(
			IOnActionFinishedListener pOnAnimationFinishedListener) {
		Log.e("BattleEngine", "Turn Indicator - Doing turn change");
		pOnAnimationFinishedListener.OnAnimationFinished();
	}

	private int firstPlayer;

	public void onFirstTurn(
			final IOnActionFinishedListener pOnAnimationFinishedListener) {
		Random r = new Random();
		firstPlayer = r.nextInt(2);
		Log.e("BattleEngine", "Turn Indicator - Doing first turn");
		pOnAnimationFinishedListener.OnAnimationFinished();
	}

	public int getPlayerTurn() {
		return firstPlayer;
	}

}
