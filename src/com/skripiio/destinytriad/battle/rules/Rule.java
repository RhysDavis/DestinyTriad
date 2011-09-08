package com.skripiio.destinytriad.battle.rules;

public enum Rule {

	Open(""),
	Same(""),
	Plus(""),
	Elemental(""),
	Sudden_Death(""),
	Same_Wall(""),
	Random(""),
	Direct(""),
	Diff(""),
	One(""),
	All("");
	
	private String ruleText;

	Rule(String text) {
		ruleText = text;
	}
	
	public String getRuleText() {
		return ruleText;
	}
}
