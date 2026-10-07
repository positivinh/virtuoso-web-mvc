# virtuoso-web-mvc

Software factory components for Spring MVC applications.

## Error responses

`web-mvc-starter` answers exceptions with RFC 7807 `ProblemDetail`s:

- `ProblemDetailFactory` builds the response. It copies only allow-listed context entries (`EXCEPTION_MESSAGE` plus
  `virtuoso.web.mvc.problem-details.exposed-context-keys`), lists validation errors as field, code and message without
  the rejected values, and logs client errors at `WARN` without a stack trace.
- A fallback `@RestControllerAdvice`, ordered last, maps any `ApplicationException` to `400`.

A service maps its own exceptions in an advice ordered first:

```kotlin
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class AppControllerAdvice(private val problemDetailFactory: ProblemDetailFactory) {

  @ExceptionHandler(UserNotFoundException::class)
  fun handleUserNotFoundException(exception: UserNotFoundException): ProblemDetail {

    return problemDetailFactory.create(exception, HttpStatus.NOT_FOUND)
  }
}
```

```yaml
virtuoso:
  web:
    mvc:
      problem-details:
        exposed-context-keys:
          - USERNAME
```

## Commands

### Install

```shell
mvn clean install
```

### Run sonarqube analysis

Set environment variable `SONAR_TOKEN`

```shell
export SONAR_TOKEN=[...]
```

Run sonarqube analysis

```shell
mvn clean verify sonar:sonar -P sonarqube
```
