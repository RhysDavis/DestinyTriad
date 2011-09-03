package com.skripiio.destinytriad.battle.rules;

import org.anddev.andengine.entity.primitive.Rectangle;

public class RuleButton extends Rectangle {

	private boolean state;

	public RuleButton(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight);
		state = false;
		colorToState();
	}

	public RuleButton(float pX, float pY, float pWidth, float pHeight, boolean state) {
		super(pX, pY, pWidth, pHeight);
		this.state = state;
		colorToState();
	}

	private void colorToState() {
		if (!state) {
			this.setColor(1, 0, 0);
		} else {
			this.setColor(0, 1, 0);
		}
	}

	public void swapState() {
		if (!state) {
			state = true;
		} else {
			state = false;
		}
		colorToState();
	}

	public boolean getState() {
		return state;
	}

}
