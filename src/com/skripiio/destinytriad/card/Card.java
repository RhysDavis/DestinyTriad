package com.skripiio.destinytriad.card;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.ParallelEntityModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.modifier.SequenceEntityModifier;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.IModifier.IModifierListener;

import android.util.Log;

import com.skripiio.destinytriad.battle.engine.Battle;
import com.skripiio.destinytriad.battle.engine.BoardSquare;
import com.skripiio.destinytriad.battle.engine.IOnActionFinishedListener;

public class Card extends Sprite implements IBattleCard, IOnAreaTouchListener {

	public static final int CARD_WIDTH = 105;
	public static final int CARD_HEIGHT = 140;

	public static final int ELEMENT_NONE = -1;
	public static final int ELEMENT_FIRE = 0;
	public static final int ELEMENT_WATER = 1;
	public static final int ELEMENT_EARTH = 2;
	public static final int ELEMENT_THUNDER = 3;
	public static final int ELEMENT_WIND = 4;
	public static final int ELEMENT_ICE = 5;
	public static final int ELEMENT_GHOST = 6;

	private static final float MOVE_TO_BOARD_TIME = 0.0006f;
	private static final float MOVE_TO_TOP_TIME = 0.15f;
	// Selection Animation Time
	private static final float SELECT_DURATION = 0.05f;

	private int east;

	private boolean flipCard = false;

	private float flipCounter = 0;
	boolean flipToBack = false;
	boolean flipToFront = false;
	private boolean isFlipping = false;

	/** @true if the card has been placed in battle */
	private boolean isPlaced;
	boolean isSelected = false;

	private Sprite mBorderImage;

	private IOnActionFinishedListener mCardFlipListener;

	/** The Sprite that is the front of the image */
	private Sprite mCardImage;

	private Text mFaceTextEast;

	private Text mFaceTextNorth;

	private Text mFaceTextSouth;

	private Text mFaceTextWest;

	private boolean mFlipListenerActive = false;

	private float mFlipTime = 0.8f;

	SequenceEntityModifier mFlipToBackEntityModifier;
	SequenceEntityModifier mFlipToFrontEntityModifier;
	private Font mFont;
	private Font mGreenFont;
	private Font mRedFont;

	SequenceEntityModifier mFullCardFlipEntityModifier;

	private Rectangle mPlayerColorRectangle;

	private Rectangle mElementRectangle;

	private int mPlayerID;
	// Select Modifier
	MoveModifier mSelectLeftModifier;

	MoveModifier mSelectRightModifier;
	private int north;
	private int south;

	private int west;
	private int mElement;

	private Engine mEngine;

	/**
	 * Creates a new card
	 * 
	 * @param pX
	 *           the x co-ordinate in the scene
	 * @param pY
	 *           the y co-ordinate in the scene
	 * @param pWidth
	 *           the width of the card
	 * @param pHeight
	 *           the height of the card
	 * @param pMonsterTextureRegion
	 *           the texture of the monster that this card represents
	 * @param pBackTextureRegion
	 *           the texture of the back of the card
	 * @param pBorderTextureRegion
	 *           the border texture of the card
	 * @param playerID
	 *           the ID of the player who owns this card. Either PLAYER_ONE or
	 *           PLAYER_TWO
	 * @param mFont
	 *           the font to draw the side values with
	 * @param faces
	 *           the values of the sides of the card as [north, south, east,
	 *           west]
	 */
	public Card(float pX, float pY, float pWidth, float pHeight,
			TextureRegion pMonsterTextureRegion, TextureRegion pBackTextureRegion,
			TextureRegion pBorderTextureRegion, int playerID, Font mFont, int[] faces,
			int element, Engine e, Font pRedValueFont, Font greenFont) {
		super(pX, pY, pWidth, pHeight, pBackTextureRegion);
		this.mFont = mFont;
		this.mPlayerID = playerID;
		this.mRedFont = pRedValueFont;
		this.mGreenFont = greenFont;
		mEngine = e;
		// set player id color
		this.mPlayerColorRectangle = new Rectangle(0, 0, pWidth, pHeight);
		if (playerID == Battle.PLAYER_USER) {
			this.mPlayerColorRectangle.setColor(0, 0, 1);
		} else {
			this.mPlayerColorRectangle.setColor(1, 0, 0);
		}
		this.attachChild(this.mPlayerColorRectangle);
		// set monster
		this.mCardImage = new Sprite(0, 0, pMonsterTextureRegion);
		this.mCardImage.setSize(this.getWidth(), this.getHeight());
		this.attachChild(this.mCardImage);
		// set border
		this.mBorderImage = new Sprite(0, 0, pBorderTextureRegion);
		this.mBorderImage.setSize(this.getWidth(), this.getHeight());
		this.attachChild(this.mBorderImage);
		// set sides
		setFaces(faces[0], faces[1], faces[2], faces[3]);

		// set element if element exists
		setElement(element);

		initialize();
	}

	public void setElement(int element) {
		this.mElement = element;
		if (mElement != ELEMENT_NONE) {
			mElementRectangle = new Rectangle((CARD_WIDTH / 4) - 10 + (CARD_WIDTH / 2),
					(CARD_HEIGHT / 4) - 10, 20, 20);
		}
		switch (mElement) {
			case (ELEMENT_FIRE):
				mElementRectangle.setColor(204f / 255f, 0, 0);
				attachChild(mElementRectangle);
				break;
			case (ELEMENT_WATER):
				mElementRectangle.setColor(0, 0, 187f / 255f);
				attachChild(mElementRectangle);
				break;
			case (ELEMENT_WIND):
				mElementRectangle.setColor(136f / 255f, 136f / 255f, 136f / 255f);
				attachChild(mElementRectangle);
				break;
			case (ELEMENT_EARTH):
				mElementRectangle.setColor(102f / 255f, 153f / 255f, 85f / 255f);
				attachChild(mElementRectangle);
				break;
			case (ELEMENT_THUNDER):
				mElementRectangle.setColor(1, 187f / 255f, 0);
				attachChild(mElementRectangle);
				break;
			case (ELEMENT_ICE):
				mElementRectangle.setColor(102f / 255f, 221f / 255f, 1);
				attachChild(mElementRectangle);
				break;
			case (ELEMENT_GHOST):
				mElementRectangle.setColor(153f / 255f, 0, 187f / 255f);
				attachChild(mElementRectangle);
				break;

		}
	}

	@Override
	protected void applyRotation(final GL10 pGL) {
		/* Disable culling so we can see the backside of this sprite. */
		GLHelper.disableCulling(pGL);

		final float rotation = this.mRotation;

		if (rotation != 0) {
			final float rotationCenterX = this.mRotationCenterX;
			final float rotationCenterY = this.mRotationCenterY;

			pGL.glTranslatef(rotationCenterX, rotationCenterY, 0);
			/*
			 * Note we are applying rotation around the y-axis and not the z-axis
			 * anymore!
			 */
			pGL.glRotatef(rotation, 0, 1, 0);
			pGL.glTranslatef(-rotationCenterX, -rotationCenterY, 0);
		}
	}

	/** Animates the card to be deselected */
	public void deSelectCard() {
		if (this.isSelected) {
			if (this.getPlayerID() == Battle.PLAYER_USER) {
				this.mSelectRightModifier = new MoveModifier(SELECT_DURATION, this.getX(),
						this.getX() - (this.getWidth() / 2f), this.getY(), this.getY());
				this.registerEntityModifier(this.mSelectRightModifier);
			} else {
				this.mSelectLeftModifier = new MoveModifier(SELECT_DURATION, this.getX(),
						this.getX() + (this.getWidth() / 2f), this.getY(), this.getY());
				this.registerEntityModifier(this.mSelectLeftModifier);

			}
			this.isSelected = false;
		}
	}

	@Override
	protected void drawVertices(final GL10 pGL, final Camera pCamera) {
		super.drawVertices(pGL, pCamera);
		/* Enable culling as 'normal' entities profit from culling. */
		GLHelper.enableCulling(pGL);
	}

	/** Flips the card to the other players ownership */
	public void flipCard(IOnActionFinishedListener cfl) {
		if (this.mPlayerID == Battle.PLAYER_USER) {
			this.mPlayerID = Battle.PLAYER_OPP;
		} else if (this.mPlayerID == Battle.PLAYER_OPP) {
			this.mPlayerID = Battle.PLAYER_USER;
		}
		// bring this card to the front
		int counter = 0;
		for (int i = 0; i < getParent().getChildCount(); i++) {
			getParent().getChild(i).setZIndex(counter);
			counter++;
		}
		this.setZIndex(getParent().getChildCount());
		this.getParent().sortChildren();
		this.mCardFlipListener = cfl;
		this.mFlipListenerActive = true;
		this.flipCard = true;
	}

	/** @return the east value for this card */
	public int getEast() {
		return this.east + bonus;
	}

	/** @return the north value for this card */
	public int getNorth() {
		return this.north + bonus;
	}

	public int getPlayerID() {
		return this.mPlayerID;
	}

	/** @return the south value for this card */
	public int getSouth() {
		return this.south + bonus;
	}

	/** @return the west value for this card */
	public int getWest() {
		return this.west + bonus;
	}

	private int bonus = 0;

	/** Sets the elemental bonus on this card */
	public void setElementalBonus(final boolean sameElement) {
		mEngine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				detachChild(Card.this.mFaceTextNorth);
				detachChild(Card.this.mFaceTextSouth);
				detachChild(Card.this.mFaceTextWest);
				detachChild(Card.this.mFaceTextEast);
				if (sameElement) {
					bonus = 1;
					Card.this.mFaceTextNorth = new Text(Card.this.mGreenFont
							.getStringWidth("A") + CARD_TEXT_PADDING + 2, CARD_TEXT_PADDING,
							Card.this.mGreenFont, "" + Card.this.getNorth());
					Card.this.mFaceTextSouth = new Text(Card.this.mGreenFont
							.getStringWidth("A") + 2 + CARD_TEXT_PADDING, Card.this.mGreenFont
							.getLineHeight()
							- 5
							+ Card.this.mGreenFont.getLineHeight()
							- 5
							+ CARD_TEXT_PADDING, Card.this.mGreenFont, "" + Card.this.getSouth());
					Card.this.mFaceTextWest = new Text(CARD_TEXT_PADDING + 2,
							Card.this.mGreenFont.getLineHeight() - 5 + CARD_TEXT_PADDING,
							Card.this.mGreenFont, "" + Card.this.getWest());
					Card.this.mFaceTextEast = new Text(Card.this.mGreenFont
							.getStringWidth("A")
							+ Card.this.mGreenFont.getStringWidth("A")
							+ CARD_TEXT_PADDING + 2, Card.this.mGreenFont.getLineHeight() - 5
							+ CARD_TEXT_PADDING, Card.this.mGreenFont, "" + Card.this.getEast());
					Card.this.attachChild(Card.this.mFaceTextNorth);
					Card.this.attachChild(Card.this.mFaceTextSouth);
					Card.this.attachChild(Card.this.mFaceTextWest);
					Card.this.attachChild(Card.this.mFaceTextEast);
				} else {
					bonus = -1;
					Card.this.mFaceTextNorth = new Text(Card.this.mRedFont.getStringWidth("A")
							+ CARD_TEXT_PADDING + 2, CARD_TEXT_PADDING, Card.this.mRedFont, ""
							+ Card.this.getNorth());
					Card.this.mFaceTextSouth = new Text(Card.this.mRedFont.getStringWidth("A")
							+ 2 + CARD_TEXT_PADDING, Card.this.mRedFont.getLineHeight() - 5
							+ Card.this.mRedFont.getLineHeight() - 5 + CARD_TEXT_PADDING,
							Card.this.mRedFont, "" + Card.this.getSouth());
					Card.this.mFaceTextWest = new Text(CARD_TEXT_PADDING + 2,
							Card.this.mRedFont.getLineHeight() - 5 + CARD_TEXT_PADDING,
							Card.this.mRedFont, "" + Card.this.getWest());
					Card.this.mFaceTextEast = new Text(Card.this.mRedFont.getStringWidth("A")
							+ Card.this.mRedFont.getStringWidth("A") + CARD_TEXT_PADDING + 2,
							Card.this.mRedFont.getLineHeight() - 5 + CARD_TEXT_PADDING,
							Card.this.mRedFont, "" + Card.this.getEast());
					Card.this.attachChild(Card.this.mFaceTextNorth);
					Card.this.attachChild(Card.this.mFaceTextSouth);
					Card.this.attachChild(Card.this.mFaceTextWest);
					Card.this.attachChild(Card.this.mFaceTextEast);
				}

			}
		});

	}

	public void setNumberText(Font pFont) {
		mFaceTextNorth = new Text(pFont.getStringWidth("A") + CARD_TEXT_PADDING + 2,
				CARD_TEXT_PADDING, pFont, "" + getNorth());
		mFaceTextSouth = new Text(pFont.getStringWidth("A") + 2 + CARD_TEXT_PADDING,
				pFont.getLineHeight() - 5 + pFont.getLineHeight() - 5 + CARD_TEXT_PADDING,
				pFont, "" + getSouth());
		mFaceTextWest = new Text(CARD_TEXT_PADDING + 2, pFont.getLineHeight() - 5
				+ CARD_TEXT_PADDING, pFont, "" + getWest());
		mFaceTextEast = new Text(pFont.getStringWidth("A") + pFont.getStringWidth("A")
				+ CARD_TEXT_PADDING + 2, pFont.getLineHeight() - 5 + CARD_TEXT_PADDING,
				pFont, "" + getEast());
		attachChild(mFaceTextNorth);
		attachChild(mFaceTextSouth);
		attachChild(mFaceTextWest);
		attachChild(mFaceTextEast);
	}

	public static final int CARD_TEXT_PADDING = 4;

	/** Initializes the Card */
	private void initialize() {
		// Set Texts
		this.mFaceTextNorth = new Text(this.mFont.getStringWidth("A") + CARD_TEXT_PADDING
				+ 2, CARD_TEXT_PADDING, this.mFont, "" + this.north);
		this.mFaceTextSouth = new Text(this.mFont.getStringWidth("A") + 2
				+ CARD_TEXT_PADDING, this.mFont.getLineHeight() - 5
				+ this.mFont.getLineHeight() - 5 + CARD_TEXT_PADDING, this.mFont, ""
				+ this.south);
		this.mFaceTextWest = new Text(CARD_TEXT_PADDING + 2, this.mFont.getLineHeight() - 5
				+ CARD_TEXT_PADDING, this.mFont, "" + this.west);
		this.mFaceTextEast = new Text(this.mFont.getStringWidth("A")
				+ this.mFont.getStringWidth("A") + CARD_TEXT_PADDING + 2,
				this.mFont.getLineHeight() - 5 + CARD_TEXT_PADDING, this.mFont, ""
						+ this.east);
		attachChild(this.mFaceTextNorth);
		attachChild(this.mFaceTextSouth);
		attachChild(this.mFaceTextWest);
		attachChild(this.mFaceTextEast);

		// Full Flip
		ScaleModifier scaleUp = new ScaleModifier((this.mFlipTime / 2f), 1, 1.2f);
		ScaleModifier scaleDown = new ScaleModifier((this.mFlipTime / 2f), 1.2f, 1);
		RotationModifier rotateToBack = new RotationModifier(this.mFlipTime / 2f, 0, 180);
		RotationModifier rotateToFront = new RotationModifier(this.mFlipTime / 2f, 180, 360);
		ParallelEntityModifier startFlipModifier = new ParallelEntityModifier(scaleUp,
				rotateToBack);
		startFlipModifier.addModifierListener(new IModifierListener<IEntity>() {

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				if (Card.this.mPlayerID == Battle.PLAYER_USER) {
					Card.this.mPlayerColorRectangle.setColor(0, 0, 1);

				} else if (Card.this.mPlayerID == Battle.PLAYER_OPP) {
					Card.this.mPlayerColorRectangle.setColor(1, 0, 0);

				}

			}

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub

			}
		});
		ParallelEntityModifier endFlipModifier = new ParallelEntityModifier(scaleDown,
				rotateToFront);

		this.mFullCardFlipEntityModifier = new SequenceEntityModifier(startFlipModifier,
				endFlipModifier);

		ScaleModifier scaleUpHalf = new ScaleModifier((this.mFlipTime / 4f), 1, 1.2f);
		ScaleModifier scaleDownHalf = new ScaleModifier((this.mFlipTime / 4f), 1.2f, 1f);
		RotationModifier rotateToBackHalf = new RotationModifier(this.mFlipTime / 4f, 0, 90);
		RotationModifier rotateToBackHalf2 = new RotationModifier(this.mFlipTime / 4f, 90,
				180);
		RotationModifier rotateToFrontHalf = new RotationModifier(this.mFlipTime / 4f, 180,
				270);
		RotationModifier rotateToFrontHalf2 = new RotationModifier(this.mFlipTime / 4f,
				270, 360);

		ParallelEntityModifier flipToBack = new ParallelEntityModifier(scaleUpHalf,
				rotateToBackHalf);
		ParallelEntityModifier flipToBack2 = new ParallelEntityModifier(scaleDownHalf,
				rotateToBackHalf2);

		ParallelEntityModifier flipToFront = new ParallelEntityModifier(scaleUpHalf,
				rotateToFrontHalf);
		ParallelEntityModifier flipToFront2 = new ParallelEntityModifier(scaleDownHalf,
				rotateToFrontHalf2);

		this.mFlipToBackEntityModifier = new SequenceEntityModifier(flipToBack, flipToBack2);
		this.mFlipToFrontEntityModifier = new SequenceEntityModifier(flipToFront,
				flipToFront2);

		// selection modifiers
		this.mSelectLeftModifier = new MoveModifier(SELECT_DURATION, this.getX(),
				this.getX() + (this.getWidth() / 2f), this.getY(), this.getY());
		this.mSelectRightModifier = new MoveModifier(SELECT_DURATION, this.getX(),
				this.getX() - (this.getWidth() / 2f), this.getY(), this.getY());
		this.mSelectLeftModifier.setRemoveWhenFinished(true);
		this.mSelectRightModifier.setRemoveWhenFinished(true);

		this.isPlaced = false;
	}

	public void instantFlipToBack(boolean faceDown) {
		if (faceDown) {
			isFaceDown = true;
			this.setRotation(180);
		} else {
			isFaceDown = false;
			this.setRotation(0);
		}
	}

	public boolean isPlaced() {
		return this.isPlaced;
	}

	public void moveCardToLocation(float placeX, float placeY,
			final IOnActionFinishedListener al) {
		Log.v("Game", "Moving Card To Location");
		// move the card to the top of the screen
		MoveModifier moveToTop = new MoveModifier(MOVE_TO_TOP_TIME, this.getX(),
				this.getX(), this.getY(), this.getHeight() * -1f);

		// slam the card down on the square
		MoveModifier moveToSquare = new MoveModifier((placeY - (this.getHeight() * -1f))
				* MOVE_TO_BOARD_TIME, placeX, placeX, this.getHeight() * -1f, placeY);

		moveToTop.addModifierListener(new IModifierListener<IEntity>() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				instantFlipToBack(false);

			}
		});

		// add sequence modifier
		SequenceEntityModifier cardMover = new SequenceEntityModifier(moveToTop,
				moveToSquare);
		cardMover.setRemoveWhenFinished(true);
		cardMover.addModifierListener(new IModifierListener<IEntity>() {

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				al.OnAnimationFinished();
			}

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
		});
		registerEntityModifier(cardMover);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		// Handles flipping the card
		if (this.flipCard) {
			this.isFlipping = true;
			this.flipCard = false;
			registerEntityModifier(this.mFullCardFlipEntityModifier);
		}

		// Handles finishing flipping the card
		if (this.isFlipping) {
			this.flipCounter += pSecondsElapsed;
			if (this.flipCounter >= 1) {
				this.isFlipping = false;
				this.flipCounter = 0;
				unregisterEntityModifier(this.mFullCardFlipEntityModifier);
				this.mFullCardFlipEntityModifier.reset();
				if (this.mFlipListenerActive) {
					this.mCardFlipListener.OnAnimationFinished();
					this.mFlipListenerActive = false;
				}
			}
		}

		if (this.flipToBack) {
			if (this.mFlipToFrontEntityModifier.isFinished()) {
				this.mFlipToFrontEntityModifier.reset();
				this.flipToBack = false;
			}
		} else if (this.flipToFront) {
			if (this.mFlipToBackEntityModifier.isFinished()) {
				this.mFlipToBackEntityModifier.reset();
				this.flipToFront = false;
			}
		}

	}

	public void placeCardDown(final float placeX, final float placeY,
			final IOnActionFinishedListener al) {
		// ensure the card is selected
		// bring this card to the front
		int counter = 0;
		for (int i = 0; i < getParent().getChildCount(); i++) {
			getParent().getChild(i).setZIndex(counter);
			counter++;
		}
		this.setZIndex(getParent().getChildCount());
		this.getParent().sortChildren();
		Log.v("Game", "Placing Card Down");
		select(new IOnActionFinishedListener() {

			@Override
			public void OnAnimationFinished() {
				Log.v("Game", "Card is selected");
				Card.this.isSelected = false;
				moveCardToLocation(placeX, placeY, al);

			}
		});
	}

	@Override
	public void reset() {
		this.isPlaced = false;
		this.isSelected = false;
	}

	/** Selects a card with an animation listener */
	public void select(final IOnActionFinishedListener al) {
		if (!this.isSelected) {
			if (this.getPlayerID() == Battle.PLAYER_USER) {
				this.mSelectLeftModifier = new MoveModifier(SELECT_DURATION, this.getX(),
						this.getX() + (this.getWidth() / 2f), this.getY(), this.getY());
				this.mSelectLeftModifier
						.addModifierListener(new IModifierListener<IEntity>() {

							@Override
							public void onModifierFinished(IModifier<IEntity> pModifier,
									IEntity pItem) {
								al.OnAnimationFinished();

							}

							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier,
									IEntity pItem) {

							}
						});
				this.registerEntityModifier(this.mSelectLeftModifier);

			} else {
				this.mSelectRightModifier = new MoveModifier(SELECT_DURATION, this.getX(),
						this.getX() - (this.getWidth() / 2f), this.getY(), this.getY());
				this.mSelectRightModifier
						.addModifierListener(new IModifierListener<IEntity>() {

							@Override
							public void onModifierFinished(IModifier<IEntity> pModifier,
									IEntity pItem) {
								al.OnAnimationFinished();

							}

							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier,
									IEntity pItem) {

							}
						});
				this.registerEntityModifier(this.mSelectRightModifier);
			}

			this.isSelected = true;
		} else {
			// card is already selected, don't need to animate
			al.OnAnimationFinished();
		}
	}

	/** Animates the card to be selected */
	public void selectCard() {
		if (!this.isSelected) {
			if (this.getPlayerID() == Battle.PLAYER_USER) {
				this.mSelectLeftModifier = new MoveModifier(SELECT_DURATION, this.getX(),
						this.getX() + (this.getWidth() / 2f), this.getY(), this.getY());
				this.registerEntityModifier(this.mSelectLeftModifier);
			} else {
				this.mSelectRightModifier = new MoveModifier(SELECT_DURATION, this.getX(),
						this.getX() - (this.getWidth() / 2f), this.getY(), this.getY());
				this.registerEntityModifier(this.mSelectRightModifier);
			}

			this.isSelected = true;
		}
	}

	/** Sets the card face down */
	public void setFaceDown(boolean faceDown) {
		if (faceDown) {
			this.flipToBack = true;
			this.mFlipToBackEntityModifier.setRemoveWhenFinished(true);
			this.registerEntityModifier(this.mFlipToBackEntityModifier);
		} else {
			this.flipToFront = true;
			this.mFlipToFrontEntityModifier.setRemoveWhenFinished(true);
			this.registerEntityModifier(this.mFlipToFrontEntityModifier);
		}
	}

	/**
	 * Sets the values for each face of this card
	 * 
	 * @param north
	 *           the north value to set
	 * @param south
	 *           the south value to set
	 * @param east
	 *           the east value to set
	 * @param west
	 *           the west value to set
	 */
	private void setFaces(int north, int south, int east, int west) {
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
	}

	boolean isFaceDown = false;

	public boolean isFaceDown() {
		return isFaceDown;
	}

	/** Sets this card as placed */
	public void setPlaced() {
		this.isPlaced = true;
	}

	public void flip(IOnActionFinishedListener onAnimationFinishedListener) {
		flipCard(onAnimationFinishedListener);
	}

	@Override
	public int getElement() {
		return mElement;
	}

	public void placeCard(BoardSquare pBoardSquare,
			IOnActionFinishedListener onAnimationFinishedListener) {
		moveCardToLocation(pBoardSquare.getX(), pBoardSquare.getY(),
				onAnimationFinishedListener);
	}

	public void setFaceDown(boolean faceDown, IOnActionFinishedListener listener) {
		setFaceDown(true);
		listener.OnAnimationFinished();
	}

	public interface IOnCardSelectedListener {
		public void onCardSelect(Card pCard);
	}

	private IOnCardSelectedListener mSelectedListener;

	public void setOnSelectListener(IOnCardSelectedListener pIOnCardSelectedListener) {
		mSelectedListener = pIOnCardSelectedListener;
	}

	/** Selects this card */
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		mSelectedListener.onCardSelect(this);
		return true;
	}

}
