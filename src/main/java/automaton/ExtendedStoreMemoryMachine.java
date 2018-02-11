package automaton;

import model.Grammar;
import model.GrammarType;
import model.Rule;
import util.GrammarUtil;

public class ExtendedStoreMemoryMachine {

	private static final char UNIQUE_SYMBOL = '#';

	private final Grammar grammar;

	public ExtendedStoreMemoryMachine(Grammar grammar) {
		this.grammar = grammar;
		if (GrammarUtil.getType(grammar).ordinal() < GrammarType.TYPE_2.ordinal()) {
			throw new IllegalArgumentException("Invalid grammar type");
		}
	}

	public void startRecognition(String stringToRecognition) {
		System.out.println("Start recognition " + stringToRecognition);
		if (!recognitionStep(stringToRecognition, String.valueOf(UNIQUE_SYMBOL))) {
			System.out.println("Can't recognize " + stringToRecognition);
		}
	}

	private boolean recognitionStep(String stringToRecognition, String memory) {
		if (stringToRecognition.isEmpty() && memory.isEmpty()) {
			printStep(stringToRecognition, memory);
			return true;
		} else if (!stringToRecognition.isEmpty() && memory.isEmpty()) {
			return false;
		} else if (stringToRecognition.isEmpty() && memory.equals(String.valueOf(UNIQUE_SYMBOL) + grammar.getS())) {
			if (recognitionStep(stringToRecognition, "")) {
				printStep(stringToRecognition, memory);
				return true;
			} else return false;
		} else {
			for (Rule rule : grammar.getRules()) {
				final String ruleRight = rule.getRight();
				if (memory.contains(ruleRight) && memory.lastIndexOf(ruleRight) == memory.length() - ruleRight.length()) {
					try {
						if (recognitionStep(stringToRecognition, memory.substring(0, memory.length() - ruleRight.length()) + rule.getLeft())) {
							printStep(stringToRecognition, memory);
							return true;
						}
					} catch (StackOverflowError ignore) {
					}
				}
			}
			if (stringToRecognition.isEmpty()) {
				return false;
			}
			if (recognitionStep(stringToRecognition.substring(1), memory + stringToRecognition.substring(0, 1))) {
				printStep(stringToRecognition, memory);
				return true;
			} else return false;
		}
	}

	private void printStep(String stringToRecognition, String memory) {
		System.out.println("( " + stringToRecognition + " )" + " : " + "( " + memory + " )");
	}
}
