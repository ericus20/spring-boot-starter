package com.developersboard.shared.util.core;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;

class ValidationUtilsTest {

  private static Stream<String> blankStrings() {
    return Stream.of("", "   ");
  }

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(ValidationUtils.class));
  }

  /** Runs a parametrized test with the values returned by the method source as inputs */
  @ParameterizedTest
  @MethodSource({"blankStrings"})
  void invalidInputThrowsException(String input) {
    Assertions.assertThrows(
        IllegalArgumentException.class, () -> ValidationUtils.validateInputs(input));
  }

  @Test
  void validInputsDoesNotThrowException(TestInfo testInfo) {
    Assertions.assertDoesNotThrow(() -> ValidationUtils.validateInputs(testInfo));
  }

  @Test
  void nullAndValidArrayInputThrowsException(TestInfo testInfo) {

    Assertions.assertThrows(
        NullPointerException.class,
        () ->
            ValidationUtils.validateInputsWithMessage(
                "An empty array should not be accepted as a valid input",
                null,
                testInfo.getDisplayName()));
  }

  @Test
  void emptyArrayInputThrowsException() {
    Assertions.assertThrows(IllegalArgumentException.class, ValidationUtils::validateInputs);
  }

  @Test
  void validArrayInputDoesNotThrowException(TestInfo testInfo) {
    Assertions.assertDoesNotThrow(() -> ValidationUtils.validateInputs(testInfo.getDisplayName()));
  }

  @Test
  void validArrayWithEmptyArrayInputThrowsException() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> ValidationUtils.validateInputs((Object) new String[] {}));
  }

  @Test
  void validArrayWithArrayInputsShouldBeValid(TestInfo testInfo) {

    Assertions.assertDoesNotThrow(
        () ->
            ValidationUtils.validateInputs(
                testInfo.getDisplayName(), new String[] {testInfo.getDisplayName()}));
  }

  @Test
  void validArrayWithPrimitiveArrayInputsShouldBeValid(TestInfo testInfo) {

    Assertions.assertDoesNotThrow(
        () -> ValidationUtils.validateInputs(testInfo.getDisplayName(), new int[] {1}));
  }
}
