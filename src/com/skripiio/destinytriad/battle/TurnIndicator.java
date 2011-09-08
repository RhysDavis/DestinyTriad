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
	}

	private int firstPlayer;

	public void onFirstTurn(
			final IOnActionFinishedListener pOnAnimationFinishedListener) {
		Random r = new Random();
		firstPlayer = r.nextInt(2);
		Log.e("BattleEngine", "Turn Indicator - Doing first turn");

		MoveModifier rm = new MoveModifier(10f, 0f, 0f, 200f, 200f);

		rm.addModifierListener(new IModifierListener<IEntity>() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				pOnAnimationFinishedListener.OnAnimationFinished();

			}
		});
		registerEntityModifier(rm);
	}

	public int getPlayerTurn() {
		return firstPlayer;
	}

}
