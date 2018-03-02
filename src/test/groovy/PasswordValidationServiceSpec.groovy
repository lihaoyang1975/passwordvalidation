import com.hermanlee.passwordvalidation.domain.CharClass
import com.hermanlee.passwordvalidation.service.PasswordValidationService
import com.hermanlee.passwordvalidation.utility.ValidationRuleBuilder
import spock.lang.Shared
import spock.lang.Specification

class PasswordValidationServiceSpec extends Specification {

    @Shared def service = new PasswordValidationService()

    def "valid password gets accepted and invalid one gets rejected"() {
        setup:
        service.ruleSupplier = { ->
            new ValidationRuleBuilder()
                    .consistsOfOnly(CharClass.LOWERCASE.or(CharClass.NUMERIC))
                    .containsAtLeast(1, CharClass.LOWERCASE)
                    .containsAtLeast(1, CharClass.NUMERIC)
                    .withMinLength(5)
                    .withMaxLength(12)
                    .withoutRepeatingSequences(true)
                    .build()
        }
        when: "password matches all rules"
        def pwd = "a1bbqsauce"
        def result = service.validatePassword(pwd)

        then: "no rejection"
        result.isEmpty()

        when: "password fails to contain at least one digit"
        pwd = "abbqsauce"
        result = service.validatePassword(pwd)

        then: "one rejection message returned"
        result.size() == 1
        result.contains("Password must contain at least 1 numeric digits")

        when: "password contains an uppercase letter"
        pwd = "A1bbqsauce"
        result = service.validatePassword(pwd)

        then: "one rejection message returned"
        result.size() == 1
        result.contains("Password must consists of only lowercase letters or numeric digits")

        when: "password contains more than 12 chars"
        pwd = "a1bbqsauceisgreat"
        result = service.validatePassword(pwd)

        then: "one rejection messages returned"
        result.size() == 1
        result.contains("Password must not be longer than 12 characters")

        when: "password contains consecutive repeating sequences"
        pwd = "a1bbqbbq"
        result = service.validatePassword(pwd)

        then: "one rejection messages returned"
        result.size() == 1
        result.contains("Password must not contain any character sequence immediately followed by the same sequence")
    }

    def "more test by applying other rules"() {
        setup:
        service.ruleSupplier = { ->
            new ValidationRuleBuilder()
                .startsWith(CharClass.UPPERCASE)
                .endsWith(CharClass.ALPHANUMERIC.negate())
                .containsAtMost(2, CharClass.ALPHANUMERIC.negate())
                .containsAtMost(2, CharClass.UPPERCASE)
                .withoutRepeatingSequences(false)
                .build()
        }

        when: "password matches all rules"
        def pwd = "A1bbqsauce!"
        def result = service.validatePassword(pwd)

        then: "no rejection"
        result.isEmpty()

        when: "password does not start with an uppercase letter"
        pwd = "a1bbqsauce!"
        result = service.validatePassword(pwd)

        then: "one rejection message returned"
        result.size() == 1
        result.contains("Password must start with uppercase letters")

        when: "password does not end with a non-alphanumeric char"
        pwd = "A1bbqsauce"
        result = service.validatePassword(pwd)

        then: "one rejection message returned"
        result.size() == 1
        result.contains("Password must end with non-alphanumeric characters")

        when: "password contains more than 2 non-alphanumeric chars"
        pwd = "A1#bbq?sauce!"
        result = service.validatePassword(pwd)

        then: "one rejection messages returned"
        result.size() == 1
        result.contains("Password must contain at most 2 non-alphanumeric characters")

        when: "password contains more than 2 uppercase letters"
        pwd = "A1BbqSauce!"
        result = service.validatePassword(pwd)

        then: "one rejection messages returned"
        result.size() == 1
        result.contains("Password must contain at most 2 uppercase letters")

        when: "password contains non-consecutive repeating sequences"
        pwd = "A1bbqsaucebbq!"
        result = service.validatePassword(pwd)

        then: "one rejection messages returned"
        result.size() == 1
        result.contains("Password must not contain any character sequence followed by the same sequence")
    }
}
