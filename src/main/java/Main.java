import automaton.ExtendedStoreMemoryMachine;
import automaton.StoreMemoryMachine;
import lombok.SneakyThrows;
import model.Grammar;
import util.GrammarUtil;

public class Main {

	public static void main(String[] args) {
		final String rulesFileName = "rules.txt";
		final String validString = "nnn*nn+n/";
		final String invalidString = "m+n+h";

		System.out.println("------------------------------");
		System.out.println("Recognition of valid string by simple push down automaton: " + validString);
		System.out.println("------------------------------");
		ammTest(validString, rulesFileName);
		System.out.println("------------------------------");
		System.out.println("Recognition of invalid string by simple push down automaton: " + invalidString);
		System.out.println("------------------------------");
		ammTest(invalidString, rulesFileName);
		System.out.println("------------------------------");
		System.out.println("Recognition of valid string by extended push down automaton: " + validString);
		System.out.println("------------------------------");
		eammTest(validString, rulesFileName);
		System.out.println("------------------------------");
		System.out.println("Recognition of invalid string by extended push down automaton: " + invalidString);
		System.out.println("------------------------------");
		eammTest(invalidString, rulesFileName);
		System.out.println("------------------------------");
	}

	@SneakyThrows
	private static void ammTest(final String string, final String rulesFileName) {
		final Grammar grammar = GrammarUtil.fromFile(rulesFileName);
		final StoreMemoryMachine unit = new StoreMemoryMachine(grammar);
		unit.startRecognition(string);
	}

	@SneakyThrows
	private static void eammTest(final String string, final String rulesFileName) {
		final Grammar grammar = GrammarUtil.fromFile(rulesFileName);
		final ExtendedStoreMemoryMachine extendedStoreMemoryMachine = new ExtendedStoreMemoryMachine(grammar);
		extendedStoreMemoryMachine.startRecognition(string);
	}
}
