package com.skripiio.destinytriad.battle.engine;

import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class GameBoardSquare extends BoardSquare {

	public GameBoardSquare(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
	}

	public GameBoardSquare(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pTextureRegion, int pElement) {
		super(pX, pY, pWidth, pHeight, pTextureRegion, pElement);
	
	}

}
