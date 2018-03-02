# passwordvalidation

### This project was

- written in IntelliJ
- based on the Spring framework
- unit tested with the Spock framework
- and built with Gradle

### Build

run 

```
./gradlew clean build
```

### Usage

In your controller, inject the password validation service by:

```
@Autowired // or @Inject if you are not using Spring
private PasswordValidationService passwordValidationService;
```

Then in the controller method where you'd like to validate a password, call:

```
List<String> result = passwordValidationService.validatePassword("the pwd to validate");
```

When the result is empty, it means the password is valid. 
Otherwise, the result contains a list of messages explaining which rules the password broke.

### Configuration and Customization

To set the rules for validating your password, please reference the config file at (https://github.com/lihaoyang1975/passwordvalidation/blob/master/src/test/groovy/com/hermanlee/passwordvalidation/service/PasswordValidationServiceSpec.groovy).

The rules being currently applied are the ones specified in the original project requirement.

To specify or add additional rules, please refer to the builder class at (https://github.com/lihaoyang1975/passwordvalidation/blob/master/src/main/java/com/hermanlee/passwordvalidation/utility/ValidationRuleBuilder.java).


