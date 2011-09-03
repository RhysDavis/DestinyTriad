package com.skripiio.destinytriad.card;

import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.StrokeFont;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class CardFactory {

	/** Creates a card based on the ID */
	public static Card createCard(Engine pEngine, int pWidth, int pHeight, int pCardId,
			int pPlayerNum, Font pValueFont, Font pGreenFont,
			Font pRedValueFont, TextureRegion pMonsterTexture, TextureRegion pBackTextureRegion,
			TextureRegion pBorderTextureRegion) {

		Random r = new Random();
		// cards have a 1/3 chance to be elemental
		int chance = r.nextInt(3);
		int element = -1;
		if (chance == 0) {
			element = r.nextInt(7);
		}

		Card c = new Card(0, 0, pWidth, pHeight, pMonsterTexture, pBackTextureRegion,
				pBorderTextureRegion, pPlayerNum, pValueFont, mCardSideValues[pCardId], element, pEngine,
				pRedValueFont, pGreenFont);

		return c;
	}

	/** N, S, E, W */

	public static int[] mCard01 = { 2, 1, 2, 3 };
	public static int[] mCard02 = { 1, 2, 3, 1 };
	public static int[] mCard03 = { 2, 3, 1, 1 };
	public static int[] mCard04 = { 2, 1, 3, 2 };
	public static int[] mCard05 = { 1, 3, 2, 1 };
	public static int[] mCard06 = { 3, 2, 1, 1 };
	public static int[] mCard07 = { 2, 2, 1, 3 };
	public static int[] mCard08 = { 2, 1, 3, 1 };
	public static int[] mCard09 = { 3, 1, 2, 1 };
	public static int[] mCard10 = { 3, 1, 1, 2 };
	public static int[] mCard11 = { 2, 1, 2, 3 };
	public static int[] mCard12 = { 2, 2, 3, 1 };
	public static int[] mCard13 = { 2, 3, 2, 1 };
	public static int[] mCard14 = { 2, 1, 3, 2 };
	public static int[] mCard15 = { 2, 3, 2, 1 };
	public static int[] mCard16 = { 3, 2, 1, 2 };
	public static int[] mCard17 = { 2, 2, 1, 3 };
	public static int[] mCard18 = { 2, 1, 3, 1 };
	public static int[] mCard19 = { 3, 2, 2, 1 };
	public static int[] mCard20 = { 2, 2, 2, 2 };

	public static int[][] mCardSideValues = { mCard01, mCard02, mCard03, mCard04, mCard05,
			mCard06, mCard07, mCard08, mCard09, mCard10, mCard11, mCard12, mCard13, mCard14,
			mCard15, mCard16, mCard17, mCard18, mCard19, mCard20 };

}
