package com.hermanlee.passwordvalidation.config;

import com.hermanlee.passwordvalidation.domain.CharClass;
import com.hermanlee.passwordvalidation.domain.PasswordValidationRuleSupplier;
import com.hermanlee.passwordvalidation.utility.ValidationRuleBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Predicate;

/**
 * Provides the bean factory method for creating a custom password validation rule supplier.
 *
 * This is where you would define your own set of rules for your application.
 */

@Configuration
public class PasswordValidationConfig {

    @Bean
    public PasswordValidationRuleSupplier passwordValidationRuleSupplier() {
        List<Pair<Predicate<String>, String>> rules = new ValidationRuleBuilder()
                .consistsOfOnly(CharClass.LOWERCASE.or(CharClass.NUMERIC))
                .containsAtLeast(1, CharClass.LOWERCASE)
                .containsAtLeast(1, CharClass.NUMERIC)
                .withMinLength(5)
                .withMaxLength(12)
                .withoutRepeatingSequences(true)
                .build();
        return () -> rules;
    }

}
