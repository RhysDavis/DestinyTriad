package com.skripiio.destinytriad.battle.rules;

import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;

public class RuleText extends Text {

	public String mRuleDescription;

	public RuleText(float pX, float pY, Font pFont, String pRuleName,
			String pRuleDescription) {
		super(pX, pY, pFont, pRuleName);
		mRuleDescription = pRuleDescription;
		
	}

	public String getRuleDescription() {
		return mRuleDescription;
	}
}
