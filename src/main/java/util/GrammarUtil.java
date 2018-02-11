package util;


import model.Grammar;
import model.GrammarType;
import model.Rule;

import java.util.List;
import java.util.Set;

public class GrammarUtil {

	public static Grammar fromFile(String filename) {
		try {
			final List<String> rulesStrings = FileUtil.readFile(filename);
			final List<Rule> rules = RuleParser.parseAll(rulesStrings);
			final Set<Character> terminals = RuleAnalyzer.getTerminals(rules);
			final Set<Character> nonTerminals = RuleAnalyzer.getNonterminals(rules);
			return new Grammar(rules, terminals, nonTerminals);
		} catch (java.io.IOException e) {
			throw new IllegalArgumentException("Wrong file name");
		}
	}

	public static GrammarType getType(Grammar grammar) {
		if (!isType1(grammar)) return GrammarType.TYPE_0;
		if (!isType2(grammar)) return GrammarType.TYPE_1;
		if (!isType3(grammar)) return GrammarType.TYPE_2;
		return GrammarType.TYPE_3;
	}

	private static boolean isType1(Grammar grammar) {
		for (Rule rule : grammar.getRules()) {
			if (rule.getLeft()
					.length() > rule.getRight()
					.length()) return false;
		}

		return true;
	}

	private static boolean isType2(Grammar grammar) {
		for (Rule rule : grammar.getRules()) {
			List<Character> leftChars = StringUtil.splitToChars(rule.getLeft());
			if (leftChars.size() > 1) return false;
			if (grammar.getT()
					.contains(leftChars.get(0))) {
				return false;
			}
		}

		return true;
	}


	public static void printGrammar(final Grammar grammar) {
		for (Rule rule : grammar.getRules()) {
			System.out.println(rule);
		}
	}

	// for state machines
	private static boolean isType3(Grammar grammar) {
		for (Rule rule : grammar.getRules()) {
			List<Character> rightChars = StringUtil.splitToChars(rule.getRight());
			if (rightChars.size() > 2) return false;
			int nonterminalsCount = 0;
			for (Character rightChar : rightChars) {
				if (grammar.getN()
						.contains(rightChar)) nonterminalsCount++;
			}

			if (nonterminalsCount != 0) {
				if (rightChars.size() == 1) return false;
				if (nonterminalsCount > 1) return false;
			}
		}

		return true;
	}
}