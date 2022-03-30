package com.developersboard.shared.util.core;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;

class ValidationUtilsTest {

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
    Object[] objects = {null, testInfo.getDisplayName()};
    var message = "An empty array should not be accepted as a valid input";

    var ex = NullPointerException.class;
    Assertions.assertThrows(ex, () -> ValidationUtils.validateInputWithMessage(message, objects));
  }

  @Test
  void emptyArrayInputThrowsException() {
    Object[] objects = {};

    var expectedType = IllegalArgumentException.class;
    Assertions.assertThrows(expectedType, () -> ValidationUtils.validateInputs(objects));
  }

  @Test
  void validArrayInputDoesNotThrowException(TestInfo testInfo) {
    Object[] objects = {testInfo.getDisplayName()};

    Assertions.assertDoesNotThrow(() -> ValidationUtils.validateInputs(objects));
  }

  @Test
  void validArrayWithEmptyArrayInputThrowsException() {
    Object[] objects = {new String[] {}};

    var expectedType = IllegalArgumentException.class;
    Assertions.assertThrows(expectedType, () -> ValidationUtils.validateInputs(objects));
  }

  @Test
  void validArrayWithArrayInputsShouldBeValid(TestInfo testInfo) {
    Object[] objects = {testInfo.getDisplayName(), new String[] {testInfo.getDisplayName()}};

    Assertions.assertDoesNotThrow(() -> ValidationUtils.validateInputs(objects));
  }

  @Test
  void validArrayWithPrimitiveArrayInputsShouldBeValid(TestInfo testInfo) {
    Object[] objects = {testInfo.getDisplayName(), new int[] {1}};

    Assertions.assertDoesNotThrow(() -> ValidationUtils.validateInputs(objects));
  }

  private static Stream<String> blankStrings() {
    return Stream.of("", "   ");
  }
}
