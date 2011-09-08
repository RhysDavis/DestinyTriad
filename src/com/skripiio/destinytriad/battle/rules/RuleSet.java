package com.skripiio.destinytriad.battle.rules;

import java.util.ArrayList;

/** RuleSEt */
public class RuleSet {

	/**
	 * Add the attacking side with the adjacent defending side. If 2 or more of
	 * these numbers add up to the same, flip the cards. This is a combo rule
	 */
	private boolean mPlusRule;

	/**
	 * Flips cards with the same number side as yours if they connect & there are
	 * 2 or more. Flipped cards will flip adjacent cards if they're sides are
	 * greater than those next to it. This is a combo rule
	 */
	private boolean mSameRule;

	/**
	 * Some squares are elemental. If the element of your card matches the
	 * element of the square, the cards sides get +1, else your cards get -1
	 */
	private boolean mElementalRule;

	/** A and the wall is counted as same */
	private boolean mSameWallRule;

	/**
	 * If the game ends in a draw, players get the cards they ended the game with
	 * and play again until a player wins
	 */
	private boolean mSuddenDeathRule;

	/** If used: both players can see each others hand */
	private boolean mOpenRule;

	/** Your hand is random! */
	private boolean mRandomRule;

	/** Get whatever card you turn over, lose whatever card gets flipped */
	private boolean mDirectRule;

	/**
	 * You can take extra cards based on how much you win by. For example, if you
	 * win 6->4 you get to take 2 cards, 7->3 you take 4 cards etc
	 */
	private boolean mDiffRule;

	/** Winner takes all the cards */
	private boolean mAllRule;

	/** Winner chooses a single card of the opponents to keep */
	private boolean mOneRule;

	public RuleSet() {
		initialize();
	}

	/** Auto sets all rules off */
	private void initialize() {
		this.mPlusRule = false;
		this.mSameRule = false;
		this.mElementalRule = false;
		this.mSameWallRule = false;
		this.mSuddenDeathRule = false;
		this.mOpenRule = false;
		this.mRandomRule = false;
		this.mDirectRule = false;
		this.mDiffRule = false;
		this.mAllRule = false;
		this.mOneRule = false;
	}

	public void setAllRule() {
		this.mAllRule = true;
	}

	public void setOneRule() {
		this.mOneRule = true;
	}

	public void setRandomRule() {
		this.mRandomRule = true;
	}

	public void setDirectRule() {
		this.mDirectRule = true;
	}

	public void setDiffRule() {
		this.mDiffRule = true;
	}

	public void setPlusRule() {
		this.mPlusRule = true;
	}

	public void setSameRule() {
		this.mSameRule = true;
	}

	public void setElementalRule() {
		this.mElementalRule = true;
	}

	public void setSameWallRule() {
		this.mSameWallRule = true;
	}

	public void setSuddenDeathRule() {
		this.mSuddenDeathRule = true;
	}

	public void setOpenRule() {
		this.mOpenRule = true;
	}

	public boolean isPlusRule() {
		return mPlusRule;
	}

	public boolean isSameRule() {
		return mSameRule;
	}

	public boolean isOpenRule() {
		return mOpenRule;
	}

	public boolean isSuddenDeathRule() {
		return mSuddenDeathRule;
	}

	public boolean isSameWallRule() {
		return mSameWallRule;
	}

	public boolean isElementalRule() {
		return mElementalRule;
	}

	public boolean isRandomRule() {
		return this.mRandomRule;
	}

	public boolean isDirectRule() {
		return mDirectRule;
	}

	public boolean isDiffRule() {
		return mDiffRule;
	}

	public boolean isAllRule() {
		return mAllRule;
	}

	public boolean isOneRule() {
		return mOneRule;
	}

	public Rule[] getRules() {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		if (isPlusRule()) {
			rules.add(Rule.Plus);
		}
		if (isSameRule()) {
			rules.add(Rule.Same);
		}
		if (isSameWallRule()) {
			rules.add(Rule.Same_Wall);
		}
		if (isElementalRule()) {
			rules.add(Rule.Elemental);
		}
		if (isOpenRule()) {
			rules.add(Rule.Open);
		}
		if (isSuddenDeathRule()) {
			rules.add(Rule.Sudden_Death);
		}
		if (isDiffRule()) {
			
		}
		if (isDirectRule()) {
		}
		if (isOneRule()) {
		}
		if (isAllRule()) {
		}
		Rule[] ruleList = new Rule[rules.size()];
		return ruleList;
	}
	
	public static RuleSet fromString(String ruleSetString) {
		return null;
	}

	public String toString() {
		String s = new String();
		if (isPlusRule()) {
			s.concat("'plus'");
		}
		if (isSameRule()) {
			s.concat("'same'");
		}
		if (isSameWallRule()) {
			s.concat("'samewall'");
		}
		if (isElementalRule()) {
			s.concat("'elemental'");
		}
		if (isOpenRule()) {
			s.concat("'open'");
		}
		if (isSuddenDeathRule()) {
			s.concat("'suddendeath'");
		}
		if (isDiffRule()) {
			s.concat("'diff'");
		}
		if (isDirectRule()) {
			s.concat("'direct'");
		}
		if (isOneRule()) {
			s.concat("'one'");
		}
		if (isAllRule()) {
			s.concat("'all'");
		}
		return s;
	}

}
