package com.skripiio.destinytriad.battle.rules;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.skripiio.destinytriad.DestinyTriad;

public class RuleSelectionScene extends Scene implements IOnSceneTouchListener {

	public interface RulesSelectedListener {
		public void onRulesSelected(RuleSet rs);
	}

	public static final int CAMERA_WIDTH = 720;
	public static final int CAMERA_HEIGHT = 480;

	public static final int PADDING_LEFT = 15;
	public static final int BUTTON = 40;

	// =========================================
	// Rule Constants
	// =========================================
	private static final String mOpenRule = "All cards are face up during the game";
	private static final String mPlusRule = "If the side of the placed card + the adjacent side of the \n"
			+ "defensive card are the same as more than 1 side, both \n"
			+ "cards are flipped. This is a combo rule";
	private static final String mSameRule = "If 2 or more sides of the attacking card are the same \nas the "
			+ "adjacent defending card, both cards are flipped. \nThis is a combo rule";
	private static final String mSameWallRule = "Walls count as 'A' when calculating the same rule";
	private static final String mElementalRule = "Some squares are elemental. If a card with the same \n"
			+ "element is placed on that square, it gets +1 to all it's \n"
			+ "sides. All other cards placed there get -1 to all its sides";
	private static final String mSuddenDeathRule = "In the event of a draw, the game is played again but \n"
			+ "with each card they took during the previous game";
	private static final String mRandomRule = "Both players play with random cards";

	// =========================================
	// Rules
	// =========================================
	private Font mFont;
	// ===== Game Rules =====
	private RuleText mOpenText;
	private RuleButton mOpenButton;

	private RuleText mPlusText;
	private RuleButton mPlusButton;

	private RuleText mSameText;
	private RuleButton mSameButton;

	private RuleText mSameWallText;
	private RuleButton mSameWallButton;

	private RuleText mElementalText;
	private RuleButton mElementalButton;

	private RuleText mSuddenDeathText;
	private RuleButton mSuddenDeathButton;

	private RuleText mRandomText;
	private RuleButton mRandomButton;
	// ===== Take Rules =====
	private RuleText mDifferenceText;
	private RuleButton mDifferenceButton;

	private RuleText mOneText;
	private RuleButton mOneButton;

	private RuleText mAllText;
	private RuleButton mAllButton;

	private RuleText mDirectText;
	private RuleButton mDirectButton;

	private Rectangle mHelpSectionBar;
	private Rectangle mPlayButton;
	private Text mHelpText;

	private Font mHelpFont;

	private RulesSelectedListener mListener;

	private BaseGameActivity mEngine;

	private RuleButton[] mRuleButtons;

	private RuleText[] mRuleTexts;

	public RuleSelectionScene(Font font, Font helpFont, BaseGameActivity engine,
			RulesSelectedListener rsl) {
		this.mFont = font;
		mHelpFont = helpFont;
		mEngine = engine;
		this.mListener = rsl;
		initialize();
	}

	/** Setup all game screen stuff */
	private void initialize() {
		float gapDistance = ((CAMERA_HEIGHT - 100) - (mFont.getLineHeight() * 4)) / 5f;

		mHelpSectionBar = new Rectangle(0, DestinyTriad.CAMERA_HEIGHT - 100,
				DestinyTriad.CAMERA_WIDTH - 180, 100);
		mHelpSectionBar.setColor(1, 1, 1);
		mHelpText = new Text(mHelpSectionBar.getX() + 10, mHelpSectionBar.getY() + 10,
				mHelpFont, "Tap a rule to view it's description");
		attachChild(mHelpSectionBar);
		attachChild(mHelpText);

		mPlayButton = new Rectangle(mHelpSectionBar.getWidth(), mHelpSectionBar.getY(),
				180, 100);
		mPlayButton.setColor(0, 0, 1);
		attachChild(mPlayButton);

		mOpenButton = new RuleButton(PADDING_LEFT, gapDistance, BUTTON, BUTTON);
		attachChild(mOpenButton);

		float buttonHalf = (50 - mFont.getLineHeight()) / 2f;
		mOpenText = new RuleText(mOpenButton.getWidth() + PADDING_LEFT * 2, gapDistance
				+ buttonHalf, mFont, "Open", mOpenRule);
		attachChild(mOpenText);

		mPlusButton = new RuleButton(PADDING_LEFT, gapDistance * 2 + mFont.getLineHeight(),
				BUTTON, BUTTON);
		attachChild(mPlusButton);

		mPlusText = new RuleText(mPlusButton.getWidth() + PADDING_LEFT * 2, gapDistance * 2
				+ mFont.getLineHeight() + buttonHalf, mFont, "Plus", mPlusRule);

		attachChild(mPlusText);

		mSameButton = new RuleButton(PADDING_LEFT, gapDistance * 3 + mFont.getLineHeight()
				* 2, BUTTON, BUTTON);
		attachChild(mSameButton);

		mSameText = new RuleText(mSameButton.getWidth() + PADDING_LEFT * 2, gapDistance * 3
				+ mFont.getLineHeight() * 2 + buttonHalf, mFont, "Same", mSameRule);
		attachChild(mSameText);

		mSameWallButton = new RuleButton(PADDING_LEFT, gapDistance * 4
				+ mFont.getLineHeight() * 3, BUTTON, BUTTON);
		attachChild(mSameWallButton);
		mSameWallText = new RuleText(PADDING_LEFT * 2 + mSameWallButton.getWidth(),
				gapDistance * 4 + mFont.getLineHeight() * 3 + buttonHalf, mFont, "Same Wall",
				mSameWallRule);
		attachChild(mSameWallText);

		// second row of rules
		float marginLeft = 215;
		mElementalButton = new RuleButton(mOpenButton.getX() + marginLeft,
				mOpenButton.getY(), BUTTON, BUTTON);
		attachChild(mElementalButton);

		mElementalText = new RuleText(mElementalButton.getX() + BUTTON + PADDING_LEFT,
				mOpenText.getY(), mFont, "Elemental", mElementalRule);
		attachChild(mElementalText);

		mSuddenDeathButton = new RuleButton(mPlusButton.getX() + marginLeft,
				mPlusButton.getY(), BUTTON, BUTTON);
		attachChild(mSuddenDeathButton);

		mSuddenDeathText = new RuleText(mSuddenDeathButton.getX() + BUTTON + PADDING_LEFT,
				mPlusText.getY(), mFont, "Sudden Death", mSuddenDeathRule);
		attachChild(mSuddenDeathText);

		mRandomButton = new RuleButton(mSameButton.getX() + marginLeft, mSameButton.getY(),
				BUTTON, BUTTON);
		attachChild(mRandomButton);
		mRandomText = new RuleText(mRandomButton.getX() + BUTTON + PADDING_LEFT,
				mSameText.getY(), mFont, "Random", mRandomRule);
		attachChild(mRandomText);
		mRuleTexts = new RuleText[7];

		mRuleTexts[0] = mOpenText;
		mRuleTexts[1] = mPlusText;
		mRuleTexts[2] = mSameText;
		mRuleTexts[3] = mSameWallText;
		mRuleTexts[4] = mElementalText;
		mRuleTexts[5] = mSuddenDeathText;
		mRuleTexts[6] = mRandomText;

		mRuleButtons = new RuleButton[7];
		mRuleButtons[0] = mOpenButton;
		mRuleButtons[1] = mPlusButton;
		mRuleButtons[2] = mSameButton;
		mRuleButtons[3] = mSameWallButton;
		mRuleButtons[4] = mElementalButton;
		mRuleButtons[5] = mSuddenDeathButton;
		mRuleButtons[6] = mRandomButton;

		setOnSceneTouchListener(this);
		setOnSceneTouchListenerBindingEnabled(true);
	}

	private boolean helpTextAttached = true;
	private String mHelpString;

	/** Touch Scene */
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			for (int i = 0; i < mRuleTexts.length; i++) {
				if (mRuleTexts[i].contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
					if (helpTextAttached) {
						mHelpString = mRuleTexts[i].getRuleDescription();
						helpTextAttached = false;
						mEngine.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								RuleSelectionScene.this.detachChild(mHelpText);
								mHelpText = new Text(mHelpText.getX(), mHelpText.getY(),
										mHelpFont, mHelpString);
								RuleSelectionScene.this.attachChild(mHelpText);
								helpTextAttached = true;
							}

						});
					}
				}
			}

			for (int i = 0; i < mRuleButtons.length; i++) {
				if (mRuleButtons[i]
						.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
					mRuleButtons[i].swapState();
				}
			}

			if (mPlayButton.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
				RuleSet rs = new RuleSet();
				if (mOpenButton.getState()) {
					rs.setOpenRule();
				}
				if (mPlusButton.getState()) {
					rs.setPlusRule();
				}
				if (mSameButton.getState()) {
					rs.setSameRule();
				}
				if (mSameWallButton.getState()) {
					rs.setSameWallRule();
				}
				if (mElementalButton.getState()) {
					rs.setElementalRule();
				}
				if (mSuddenDeathButton.getState()) {
					rs.setSuddenDeathRule();
				}
				if (mRandomButton.getState()) {
					rs.setRandomRule();
				}

				mListener.onRulesSelected(rs);
			}
		}

		return true;
	}
}
