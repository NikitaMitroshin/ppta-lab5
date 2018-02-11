package automaton;

import javafx.util.Pair;
import model.Grammar;
import model.GrammarType;
import model.Rule;
import util.GrammarUtil;
import util.RandomCharacterUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoreMemoryMachine {

	private final Grammar grammar;

	public StoreMemoryMachine(Grammar grammar) {
		this.grammar = grammar;
		if (GrammarUtil.getType(grammar).ordinal() < GrammarType.TYPE_2.ordinal()) {
			throw new IllegalArgumentException("Invalid grammar type");
		}
		System.out.println("Original grammar");
		System.out.println("------------------------------");
		for (Rule rule : grammar.getRules()) {
			System.out.println(rule);
		}
		System.out.println("------------------------------");
		System.out.println("Removing left lrec");
		removeLeftRecursive(grammar);
		System.out.println("------------------------------");
		System.out.println("Grammar without lrec");
		System.out.println("------------------------------");
		for (Rule rule : grammar.getRules()) {
			System.out.println(rule);
		}
	}

	public void startRecognition(String stringToRecognition) {
		System.out.println("--------------------------------------------");
		System.out.println("Start recognition " + stringToRecognition);
		if (!recognitionStep(stringToRecognition, grammar.getS())) {
			System.out.println("Can't recognize " + stringToRecognition);
		}
	}

	private boolean recognitionStep(String stringToRecognition, String memory) {
		if (stringToRecognition.isEmpty() && memory.isEmpty()) {
			printStep(stringToRecognition, memory);
			return true;
		} else if (stringToRecognition.isEmpty() != memory.isEmpty()) {
			return false;
		} else if (stringToRecognition.charAt(0) == memory.charAt(0)) {
			if (recognitionStep(stringToRecognition.substring(1), memory.substring(1))) {
				printStep(stringToRecognition, memory);
				return true;
			} else {
				return false;
			}
		} else {
			if (grammar.getN()
					.contains(memory.charAt(0))) {
				for (Rule rule : grammar.getRules()) {
					if (rule.getLeft()
							.charAt(0) == memory.charAt(0)) {
						final String newMemoryState = rule.getRight() + memory.substring(1);
						if (recognitionStep(stringToRecognition, newMemoryState)) {
							printStep(stringToRecognition, memory);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private void printStep(String stringToRecognition, String memory) {
		System.out.println(stringToRecognition + ":" + memory);
	}

	private void removeLeftRecursive(Grammar grammar) {
		final List<Rule> rules = grammar.getRules();
		while (true) {
			final Character recursiveNonterminal = findRecursiveNonterminal(grammar);
			if (recursiveNonterminal != null) {
				final Pair<Set<Rule>, Set<Rule>> r_norRules = findRecursiveNorecursiveRules(grammar.getRules(), recursiveNonterminal);
				final Character newNonterminal = RandomCharacterUtil.getRandom(grammar.getN());
				grammar.getN()
						.add(newNonterminal);
				System.out.println("Adding new nonterminal: " + newNonterminal);
				for (Rule rule : r_norRules.getKey()) {
					rules.add(new Rule(String.valueOf(newNonterminal), rule.getRight()
							.substring(1) + String.valueOf(newNonterminal)));
					rules.add(new Rule(String.valueOf(newNonterminal), rule.getRight()
							.substring(1)));
					rules.remove(rule);
				}
				for (Rule rule : r_norRules.getValue()) {
					rules.add(new Rule(rule.getLeft(), rule.getRight() + String.valueOf(newNonterminal)));
				}
			} else {
				return;
			}
		}
	}

	private Character findRecursiveNonterminal(Grammar grammar) {
		for (Character nonterminal : grammar.getN()) {
			for (Rule rule : grammar.getRules()) {
				if (rule.getRight()
						.indexOf(nonterminal) == 0 && rule.getLeft()
						.indexOf(nonterminal) == 0) return nonterminal;
			}
		}
		return null;
	}

	private Pair<Set<Rule>, Set<Rule>> findRecursiveNorecursiveRules(List<Rule> rules, Character nonterminal) {
		final Pair<Set<Rule>, Set<Rule>> result = new Pair<>(new HashSet<>(), new HashSet<>());
		for (Rule rule : grammar.getRules()) {
			if (rule.getLeft()
					.indexOf(nonterminal) == 0) {
				if (rule.getRight()
						.indexOf(nonterminal) == 0) {
					result.getKey()
							.add(rule);
				} else {
					result.getValue()
							.add(rule);
				}
			}
		}
		return result;
	}
}