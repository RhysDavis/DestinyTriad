package com.skripiio.destinytriad.battle.engine;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.skripiio.destinytriad.card.IBattleCard;

public abstract class BoardSquare extends Sprite implements IBoardSquare {

	private int mElement;

	private IBattleCard mCard;

	/** Creates a normal Board Square */
	public BoardSquare(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		mElement = -1;
	}

	/** Creates a board square with an element */
	public BoardSquare(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion, int pElement) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		this.mElement = pElement;
	}

	@Override
	public boolean isElement() {
		return (mElement != -1);
	}

	@Override
	public int getElement() {
		return mElement;
	}

	@Override
	public boolean isTaken() {
		return (mCard != null);
	}

	public IBattleCard getCard() {
		return mCard;
	};

	@Override
	public void setCard(IBattleCard pCard) {
		mCard = pCard;
	}
}
