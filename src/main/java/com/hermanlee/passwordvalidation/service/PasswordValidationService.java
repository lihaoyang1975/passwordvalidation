package com.hermanlee.passwordvalidation.service;

import com.hermanlee.passwordvalidation.domain.PasswordValidationRuleSupplier;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * This is the password validation service that you should inject into your login controller
 */

@Service
@Lazy
public class PasswordValidationService {

    @Autowired
    private PasswordValidationRuleSupplier ruleSupplier;

    // Run the rule set against the password and find all the rules that were failed and return their respective error messages.
    // Each rule is a Pair<Predicate, String>, the left being the test that returns true or false, and the right being the error msg.
    public List<String> validatePassword(final String password) {
        return ruleSupplier.get().stream()
                                 .filter(rule -> !(rule.getLeft().test(password)))
                                 .map(Pair::getRight)
                                 .collect(toList());
    }

    // for injecting ruleSupplier during testing
    public void setRuleSupplier(final PasswordValidationRuleSupplier ruleSupplier) {
        this.ruleSupplier = ruleSupplier;
    }
}
