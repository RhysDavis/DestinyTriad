package com.skripiio.destinytriad.battle;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.StrokeFont;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;
import android.graphics.Typeface;

import com.skripiio.destinytriad.battle.rules.RuleSelectionScene;
import com.skripiio.destinytriad.battle.rules.RuleSelectionScene.RulesSelectedListener;
import com.skripiio.destinytriad.battle.rules.RuleSet;

public class BattleActivity extends BaseGameActivity {

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

		RuleSelectionScene rss = new RuleSelectionScene(mRuleFont, mCardFont, this,
				new RulesSelectedListener() {

					@Override
					public void onRulesSelected(RuleSet rs) {
						final Battle bs = new Battle(BattleActivity.this, rs);
						BattleActivity.this.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								BattleActivity.this.getEngine().setScene(bs);

							}
						});
					}
				});

		return rss;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

}
