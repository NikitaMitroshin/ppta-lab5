package util;


import model.Rule;

import java.util.ArrayList;
import java.util.List;

public class RuleParser {

    private static final String DELIMITER = "->";
    private static final String OR_SYMBOL = "\\|";

    public static List<Rule> parseAll(List<String> rulesStrings) {
        List<Rule> rules = new ArrayList<>();
        for (String ruleString : rulesStrings) {
            List<Rule> rule = parse(ruleString);
            rules.addAll(rule);
        }
        return rules;
    }

    private static List<Rule> parse(String ruleString) {
        List<Rule> rules = new ArrayList<>();
        final String[] ruleParts = ruleString.split(DELIMITER);
        if (ruleParts.length > 2) {
            throw new IllegalArgumentException("Invalid grammar");
        }
        final String[] rightParts = ruleParts[1].split(OR_SYMBOL);
        for (String rightPart : rightParts) {
            rules.add(new Rule(ruleParts[0], rightPart));
        }

        return rules;
    }
}
