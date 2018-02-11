package automaton.amm;


import javaslang.Tuple;
import model.Grammar;
import model.GrammarType;
import model.StackParams;
import util.GrammarUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AMMControlUnit {

	private final List<MemoryFunction> functions;

	public AMMControlUnit(Grammar grammar) {
		final GrammarType type = GrammarUtil.getType(grammar);
		if (type != GrammarType.TYPE_2 && type != GrammarType.TYPE_3) {
			throw new IllegalArgumentException();
		}
		functions = Stream.concat(grammar.getT()
				.stream()
				.map(t -> new MemoryFunction(t, t, "")), grammar.getRules()
				.stream()
				.map(rule -> new MemoryFunction(null, rule.getLeft()
						.charAt(0), rule.getRight())))
				.collect(Collectors.toList());
	}

	public boolean recognize(final String string, final String memory, final StackParams params) {
		if (params.getIndex() > params.getLimit()) {
			return false;
		}
		if (string.isEmpty()) {
			System.out.println("String: ' ' Memory: '" + memory + "'");
			return true;
		}
		if (memory.isEmpty()) {
			return false;
		}
		final List<MemoryFunction> availableFunctions = getFunctionsByInput(string.charAt(0), memory.charAt(0));
		availableFunctions.sort(Comparator.comparingInt(o -> o.getMemoryOut()
				.length()));

		return availableFunctions.stream()
				.map(function -> Tuple.of(function, string, memory))
				.filter(tuple -> recognize(tuple._1.getStringIn() == null ? tuple._2: tuple._2.substring(1), tuple._1.getMemoryOut() + tuple._3.substring(1),
						new StackParams(params.getLimit(), params.getIndex() + 1)))
				.findFirst()
				.map(tuple -> {
					System.out.println("String: '" + tuple._2 + "' Memory: '" + tuple._3 + "'");
					return true;
				}).orElse(false);
	}

	public List<MemoryFunction> getFunctions() {
		return functions;
	}

	private List<MemoryFunction> getFunctionsByInput(final Character stringIn, final char memoryIn) {
		return functions.stream()
				.filter(function -> (function.getStringIn() == null || stringIn == function.getStringIn()) && memoryIn == function.getMemoryIn())
				.collect(Collectors.toList());
	}

}
