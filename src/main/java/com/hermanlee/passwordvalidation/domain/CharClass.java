package com.hermanlee.passwordvalidation.domain;

import java.util.Objects;

/**
 * This class contains pre-defined character classes that are common used in regex.
 *
 * It also allows you to define your own character classes, e.g.
 *
 * CharClass specialChars = new CharClass("special characters", "!@#$%^&"
 *
 * You can also use the or(CharClass) method to combine this CharClass with another,
 * and the negate() method to attain a new CharClass that is the opposite of this CharClass.
 *
 */
public class CharClass {

    public static CharClass ALPHABETIC = new CharClass("alphabetic characters", "a-zA-Z");
    public static CharClass LOWERCASE = new CharClass("lowercase letters", "a-z");
    public static CharClass UPPERCASE = new CharClass("uppercase letters", "A-Z");
    public static CharClass NUMERIC = new CharClass("numeric digits", "\\d");
    public static CharClass ALPHANUMERIC = new CharClass("alphanumeric characters", "a-zA-Z\\d");
    public static CharClass WHITESPACE = new CharClass("whitespace characters", "\\s");

    private final String name;
    private final String chars;

    public CharClass(final String name, final String chars) {
        this.name = name;
        this.chars = chars;
    }

    public String getName() {
        return name;
    }

    public String getChars() {
        return chars;
    }

    public CharClass or(CharClass alt) {
        Objects.requireNonNull(alt);
        return new CharClass(name + " or " + alt.name, chars + alt.chars);
    }

    public CharClass negate() {
        return new CharClass("non-" + name, "^" + chars);
    }

    public String toRegex() {
        return "[" + chars + "]";
    }

}
