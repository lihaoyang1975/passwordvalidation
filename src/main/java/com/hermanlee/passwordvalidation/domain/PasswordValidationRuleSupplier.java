package com.hermanlee.passwordvalidation.domain;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Defines the Supplier interface for providing password validation rules.
 */
public interface PasswordValidationRuleSupplier extends Supplier<List<Pair<Predicate<String>, String>>> {
    @Override
    List<Pair<Predicate<String>, String>> get();
}
