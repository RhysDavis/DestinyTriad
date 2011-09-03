package com.skripiio.destinytriad;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;

import com.skripiio.destinytriad.battle.Battle;
import com.skripiio.destinytriad.battle.rules.RuleSelectionActivity;
import com.skripiio.destinytriad.battle.rules.RuleSelectionScene;

public class DestinyTriad extends BaseGameActivity {

	public static final int CAMERA_WIDTH = 720;
	public static final int CAMERA_HEIGHT = 480;

	private Camera mCamera;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mNewGameTextureRegion;
	private TextureRegion mContinueTextureRegion;
	private TextureRegion mOptionsTextureRegion;
	private TextureRegion mQuitGameTextureRegion;

	private BitmapTextureAtlas mTitleTextureAtlas;
	private TextureRegion mTitleTextureRegion;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new FillResolutionPolicy(), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(512, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTitleTextureAtlas = new BitmapTextureAtlas(512, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mNewGameTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "gfx/newgamebutton.png", 0,
						0);
		this.mContinueTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "gfx/continuebutton.png", 0,
						mNewGameTextureRegion.getHeight());
		this.mOptionsTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "gfx/optionsbutton.png", 0,
						mNewGameTextureRegion.getHeight() + mContinueTextureRegion.getHeight());
		this.mQuitGameTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "gfx/quitbutton.png", 0,
						mNewGameTextureRegion.getHeight() + mContinueTextureRegion.getHeight()
								+ mOptionsTextureRegion.getHeight());

		this.mTitleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				this.mTitleTextureAtlas, this, "gfx/title.png", 0, 0);

		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
		this.mEngine.getTextureManager().loadTexture(this.mTitleTextureAtlas);
	}

	private Scene mScene;

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		mScene = new Scene();
		mScene.setBackground(new ColorBackground(1, 1, 1));

		Sprite title = new Sprite(CAMERA_WIDTH / 2 - mTitleTextureRegion.getWidth() / 2, 0,
				mTitleTextureRegion);
		mScene.attachChild(title);

		Sprite newGame = new Sprite(CAMERA_WIDTH / 2 - mNewGameTextureRegion.getWidth() / 2
				+ 20, mTitleTextureRegion.getHeight() + 10,
				mNewGameTextureRegion.getWidth() - 40, mNewGameTextureRegion.getHeight(),
				mNewGameTextureRegion) {

			public boolean onAreaTouched(
					org.anddev.andengine.input.touch.TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					Intent i = new Intent(DestinyTriad.this, Battle.class);
					DestinyTriad.this.startActivity(i);
					//overridePendingTransition(R.anim.fade, R.anim.hold);
					
				}
				
				return true;
			};
		};

		mScene.registerTouchArea(newGame);
		mScene.attachChild(newGame);

		Sprite continueGame = new Sprite(CAMERA_WIDTH / 2
				- mContinueTextureRegion.getWidth() / 2 + 20, mTitleTextureRegion.getHeight()
				+ 10 + newGame.getHeight() + 10, mContinueTextureRegion.getWidth() - 40,
				mContinueTextureRegion.getHeight(), mContinueTextureRegion);
		mScene.attachChild(continueGame);

		Sprite options = new Sprite(CAMERA_WIDTH / 2 - mOptionsTextureRegion.getWidth() / 2
				+ 20, mTitleTextureRegion.getHeight() + 10 + newGame.getHeight() + 10
				+ continueGame.getHeight() + 10, mOptionsTextureRegion.getWidth() - 40,
				mOptionsTextureRegion.getHeight(), mOptionsTextureRegion);
		mScene.attachChild(options);

		Sprite quitGame = new Sprite(CAMERA_WIDTH / 2 - mQuitGameTextureRegion.getWidth()
				/ 2 + 20, mTitleTextureRegion.getHeight() + 10 + newGame.getHeight() + 10
				+ continueGame.getHeight() + 10 + options.getHeight() + 10,
				mQuitGameTextureRegion.getWidth() - 40, mQuitGameTextureRegion.getHeight(),
				mQuitGameTextureRegion);

		float left = CAMERA_HEIGHT - (quitGame.getX() + quitGame.getHeight());
		float division = left / 4;

		title.setPosition(title.getX(), title.getY() + division);
		newGame.setPosition(newGame.getX(), newGame.getY() + division * 2);
		continueGame.setPosition(continueGame.getX(), continueGame.getY() + division * 2);
		options.setPosition(options.getX(), options.getY() + division * 2);
		quitGame.setPosition(quitGame.getX(), quitGame.getY() + division * 2);

		mScene.attachChild(quitGame);
		
		return mScene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

}