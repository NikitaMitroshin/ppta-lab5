package automaton.eamm;

import model.Grammar;
import model.GrammarType;
import model.StackParams;
import util.GrammarUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EAMMControlUnit {

	private final List<EMemoryFunction> functions;

	public EAMMControlUnit(Grammar grammar) {
		final GrammarType type = GrammarUtil.getType(grammar);
		if (type != GrammarType.TYPE_2 && type != GrammarType.TYPE_3) {
			throw new IllegalArgumentException();
		}

		final List<EMemoryFunction> memoryFunctions = Stream.concat(grammar.getRules()
				.stream()
				.map(rule -> new EMemoryFunction(null, rule.getRight(), rule.getLeft()
						.charAt(0))), grammar.getT()
				.stream()
				.map(t -> new EMemoryFunction(t, "", t)))
				.collect(Collectors.toList());
		memoryFunctions.add(new EMemoryFunction(null, "#E", null));
		functions = memoryFunctions;
	}

	//	public boolean recognize(final String string, final String memory, final StackParams params) {
	//		if (params.getIndex() > params.getLimit()) {
	//			return false;
	//		}
	//		if (memory.isEmpty() && string.isEmpty()) {
	//			System.out.println("String: ' ' Memory: ' '");
	//			return true;
	//		}
	//		return getFunctionsByInput(string.isEmpty() ? null : string.charAt(0), memory).stream()
	//				.anyMatch(function -> {
	//					try {
	//						final String memoryPostfix = function.getMemoryIn();
	//						final String newMemory = memory.endsWith(memoryPostfix) ? memory.substring(0, memory.length() - memoryPostfix.length()) +
	//								function.getMemoryOut() : memory;
	//						if (recognize(function.getStringIn() == null ? string : string.substring(1), newMemory,
	//								new StackParams(params.getLimit(), params.getIndex() + 1))) {
	//							System.out.println("String: '" + string + "' Memory: '" + memory + "'");
	//							return true;
	//						}
	//					} catch (StackOverflowError ignored) {
	//					} return false;
	//				});
	//	}


	public boolean recognize(String string, String memory, final StackParams params) {
		if (params.getIndex() > params.getLimit()) {
			return false;
		}
		if (memory.isEmpty() && string.isEmpty()) {
			System.out.println("String: '" + string + "' Memory: '" + memory + "'");
			return true;
		}
		final List<EMemoryFunction> availableFunctions = getFunctionsByInput(string.isEmpty() ? null : string.charAt(0), memory);
		for (EMemoryFunction function : availableFunctions) {
			try {
				String memoryPostfix = function.getMemoryIn();
				if (memory.endsWith(memoryPostfix)) memory = memory.substring(0, memory.length() - memoryPostfix.length()) + function.getMemoryOut();
				if (recognize(function.getStringIn() == null ? string : string.substring(1), memory,
						new StackParams(params.getLimit(), params.getIndex() + 1))) {
					System.out.println("String: '" + string + "' Memory: '" + memory + "'");
					return true;
				}
			} catch (StackOverflowError ignored) {
			}
		}
		return false;
	}

	private List<EMemoryFunction> getFunctionsByInput(final Character stringIn, final String memoryIn) {
		return functions.stream()
				.filter(function -> (function.getStringIn() == null && memoryIn.endsWith(function.getMemoryIn())) || (Objects.equals(function.getMemoryIn(),
						"") && function.getStringIn() == stringIn))
				.collect(Collectors.toList());
	}

	public List<EMemoryFunction> getFunctions() {
		return functions;
	}

}
