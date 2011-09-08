package com.skripiio.destinytriad.battle;

import java.util.ArrayList;
import java.util.Random;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.StrokeFont;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;

import com.skripiio.destinytriad.battle.engine.Battle;
import com.skripiio.destinytriad.battle.rules.RuleSelectionScene;
import com.skripiio.destinytriad.battle.rules.RuleSelectionScene.RulesSelectedListener;
import com.skripiio.destinytriad.battle.rules.RuleSet;
import com.skripiio.destinytriad.card.Card;
import com.skripiio.destinytriad.card.CardFactory;

public class BattleActivity extends BaseGameActivity implements IOnSceneTouchListener {

	public static final int CAMERA_WIDTH = 720;
	public static final int CAMERA_HEIGHT = 480;

	private Camera mCamera;

	// ===============================================
	// Card Textures
	// ===============================================
	private BitmapTextureAtlas mCardTextureAtlas;
	public TextureRegion mCardBorderTextureRegion;
	public TextureRegion mCardBackTextureRegion;

	// ===============================================
	// Battle Textures
	// ===============================================
	private BitmapTextureAtlas mBattleTextureAtlas;
	private TextureRegion mArrowTextureAtlas;

	// ===============================================
	// Font Textures
	// ===============================================
	private BitmapTextureAtlas mCardGreenFontTexture;
	public StrokeFont mCardGreenFont;

	private BitmapTextureAtlas mCardRedFontTexture;
	public StrokeFont mCardRedFont;

	private BitmapTextureAtlas mCardFontTexture;
	public StrokeFont mCardFont;

	private BitmapTextureAtlas mRuleFontTexture;
	private StrokeFont mRuleFont;

	private BitmapTextureAtlas mHeadingFontTexture;
	private StrokeFont mHeadingFont;

	public TurnIndicator mTurnIndicator;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mCamera.setZClippingPlanes(-100, 100);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new FillResolutionPolicy(), this.mCamera));

	}

	@Override
	public void onLoadResources() {
		this.mCardTextureAtlas = new BitmapTextureAtlas(256, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		// ==========================
		// Card Textures
		// ==========================
		mCardBorderTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mCardTextureAtlas, this, "gfx/cardborderx.png", 0, 0);
		mCardBackTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mCardTextureAtlas, this, "gfx/cardback.jpg", 0,
				mCardBorderTextureRegion.getHeight());

		mBattleTextureAtlas = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mArrowTextureAtlas = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mBattleTextureAtlas, this, "gfx/arrow.png", 0, 0);

		this.mEngine.getTextureManager().loadTextures(this.mCardTextureAtlas,
				mBattleTextureAtlas);

		mTurnIndicator = new TurnIndicator(0, 0, 100, 100, mArrowTextureAtlas);

		this.mCardFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mRuleFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mCardGreenFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mCardRedFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		// ========================================================
		// Fonts
		// ========================================================
		this.mCardFont = new StrokeFont(this.mCardFontTexture, Typeface.create(
				Typeface.DEFAULT, Typeface.BOLD), 20, true, Color.WHITE, 1, Color.BLACK);
		this.mRuleFont = new StrokeFont(this.mRuleFontTexture, Typeface.create(
				Typeface.DEFAULT, Typeface.NORMAL), 28, true, Color.WHITE, 1, Color.GREEN);
		this.mCardGreenFont = new StrokeFont(this.mCardGreenFontTexture, Typeface.create(
				Typeface.DEFAULT, Typeface.BOLD), 20, true, Color.GREEN, 1, Color.BLACK);
		this.mCardRedFont = new StrokeFont(this.mCardRedFontTexture, Typeface.create(
				Typeface.DEFAULT, Typeface.BOLD), 20, true, Color.RED, 1, Color.BLACK);
		this.mEngine.getTextureManager().loadTextures(this.mRuleFontTexture,
				this.mCardFontTexture, mCardRedFontTexture, mCardGreenFontTexture);
		this.mEngine.getFontManager().loadFonts(this.mRuleFont, this.mCardFont,
				mCardGreenFont, mCardRedFont);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final ArrayList<Card> mPlayerCards = new ArrayList<Card>();
		final ArrayList<Card> mOpponentCards = new ArrayList<Card>();

		Random r = new Random();
		r.nextInt(19);

		for (int i = 0; i < 5; i++) {
			mPlayerCards.add(CardFactory.createCard(BattleActivity.this.getEngine(), 105, 140,
					r.nextInt(19), 0, mCardFont, mCardGreenFont, mCardRedFont,
					mCardBorderTextureRegion.clone(), mCardBackTextureRegion,
					mCardBorderTextureRegion));

		}

		for (int i = 0; i < 5; i++) {
			mOpponentCards.add(CardFactory.createCard(BattleActivity.this.getEngine(), 105, 140,
					r.nextInt(19), 1, mCardFont, mCardGreenFont, mCardRedFont,
					mCardBorderTextureRegion.clone(), mCardBackTextureRegion,
					mCardBorderTextureRegion));
		}

		RuleSelectionScene rss = new RuleSelectionScene(mRuleFont, mCardFont, this,
				new RulesSelectedListener() {

					@Override
					public void onRulesSelected(RuleSet rs) {
						final BattleScene bs = new BattleScene(mPlayerCards, mOpponentCards,
								BattleActivity.this, rs, mArrowTextureAtlas);
						BattleActivity.this.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								BattleActivity.this.getEngine().setScene(bs);

							}
						});
					}
				});

		/*
		 * CardSelectorScene css = new CardSelectorScene(new
		 * CardSelectedListener() {
		 * @Override public void onCardsSelected(Card[] pPlayerCards) { //
		 * nothinggg } }, mPlayerCards);
		 */

		BattleActivity gbs = new BattleActivity(this, new RuleSet());

		return gbs;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

	/** Cannot get out of battles lolz */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return false;
	}

}
