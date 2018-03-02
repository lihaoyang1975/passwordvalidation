package com.hermanlee.passwordvalidation.utility;

import com.hermanlee.passwordvalidation.domain.CharClass;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is where you can add additional rules for your own application.
 *
 * All the common ones are already included.
 *
 * This builder in intended to help people who are not familiar with regex to define their password validation rules
 * using a DSL (Domain Specific Language) like interface.
 */
public class ValidationRuleBuilder {

    private List<Pair<Predicate<String>, String>> rules = new ArrayList<>();

    public ValidationRuleBuilder() {
    }

    public ValidationRuleBuilder consistsOfOnly(final CharClass charClass) {
        rules.add(Pair.of(Pattern.compile("^" + charClass.toRegex() + "+$").asPredicate(), "Password must consists of only " + charClass.getName()));
        return this;
    }

    public ValidationRuleBuilder startsWith(final CharClass charClass) {
        rules.add(Pair.of(Pattern.compile("^" + charClass.toRegex()).asPredicate(), "Password must start with " + charClass.getName()));
        return this;
    }

    public ValidationRuleBuilder endsWith(final CharClass charClass) {
        rules.add(Pair.of(Pattern.compile(charClass.toRegex() + "$").asPredicate(), "Password must end with " + charClass.getName()));
        return this;
    }

    public ValidationRuleBuilder withMinLength(final Integer minLength) {
        rules.add(Pair.of(s -> s.length() >= minLength, "Password must not be shorter than " + minLength + " characters"));
        return this;
    }

    public ValidationRuleBuilder withMaxLength(final Integer maxLength) {
        rules.add(Pair.of(s -> s.length() <= maxLength, "Password must not be longer than " + maxLength + " characters"));
        return this;
    }

    public ValidationRuleBuilder containsAtLeast(final Integer occurrences, final CharClass charClass) {
        rules.add(Pair.of(
                    s -> {
                        Integer foundOccurrences = 0;
                        Matcher matcher = Pattern.compile(charClass.toRegex()).matcher(s);
                        while (matcher.find() && foundOccurrences < occurrences) {
                            foundOccurrences++;
                        }
                        return foundOccurrences >= occurrences; },
                    String.format("Password must contain at least %d %s", occurrences, charClass.getName())
        ));
        return this;
    }

    public ValidationRuleBuilder containsAtMost(final Integer occurrences, final CharClass charClass) {
        rules.add(Pair.of(
                    s -> {
                        Integer toBeFoundOccurrences = occurrences;
                        Matcher matcher = Pattern.compile(charClass.toRegex()).matcher(s);
                        while (matcher.find() && toBeFoundOccurrences >= 0) {
                            toBeFoundOccurrences--;
                        }
                        return toBeFoundOccurrences >= 0; },
                    String.format("Password must contain at most %d %s", occurrences, charClass.getName())
        ));
        return this;
    }

    public ValidationRuleBuilder withoutRepeatingSequences(final Boolean consecutive) {
        rules.add(Pair.of(
                    Pattern.compile(consecutive ? "(.{2,})\\1" : "(.{2,}).+\\1").asPredicate().negate(),
                    String.format("Password must not contain any character sequence" + (consecutive ? " immediately" : "") + " followed by the same sequence")
        ));
        return this;
    }

    public List<Pair<Predicate<String>, String>> build() {
        return rules;
    }
}
