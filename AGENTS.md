# Repository Guidelines

## Project Structure & Module Organization

This is a Gradle-based Spring Boot application named `verification-service`. Production code lives under
`src/main/java/pl/kmalski/verification`, with the application entry point in `VerificationApplication.java`.
Configuration files belong in `src/main/resources`; the current application name is configured in `application.yaml`.
Tests mirror the main package structure under `src/test/java/pl/kmalski/verification`. Generated build output is written
to `build/` and should not be edited or committed.

## Build, Test, and Development Commands

Use the Gradle wrapper so builds use the repository-pinned Gradle version:

- `./gradlew build` or `gradlew.bat build`: compiles, runs tests, and assembles the application.
- `./gradlew test` or `gradlew.bat test`: runs the JUnit Platform test suite.
- `./gradlew bootRun` or `gradlew.bat bootRun`: starts the Spring Boot application locally.
- `./gradlew clean` or `gradlew.bat clean`: removes generated build artifacts.

The project uses Java 25 via the Gradle toolchain. Ensure that a compatible JDK is available before running builds.

## Coding Style & Naming Conventions

Use Java package names under `pl.kmalski.verification`. Keep classes focused on one responsibility and name Spring
components by role, for example `VerificationController`, `VerificationService`, or `ProviderClient`. Use 4-space
indentation in Java files. Prefer constructor injection for Spring dependencies. Keep configuration in YAML under
`src/main/resources` and use kebab-case for property names. Lombok is available as a compile-only dependency; use it
sparingly and only where it removes routine boilerplate without hiding important behavior.

## Testing Guidelines

Tests use JUnit 5 through Spring Boot's test starter. Name test classes after the unit or slice under test with a
`Tests` suffix, such as `VerificationServiceTests`. Use `@SpringBootTest` for full application context checks and
narrower tests for isolated service logic when possible. Run `./gradlew test` before opening a pull request. Add tests
for new orchestration rules, provider aggregation behavior, and error-handling paths.

## Security & Configuration Tips

Do not commit secrets, provider credentials, or machine-specific IDE settings. Keep environment-specific values outside
`application.yaml` unless they are safe defaults. Prefer Spring profiles or environment variables for local overrides.
