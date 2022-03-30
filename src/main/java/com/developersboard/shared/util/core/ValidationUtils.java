package com.developersboard.shared.util.core;

import com.developersboard.constant.ErrorConstants;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

/**
 * Validation utility holds methods used across application for validation.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public final class ValidationUtils {

  private ValidationUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * A helper method which takes in multiple arguments and validate each instance not being null.
   *
   * @param inputs instances to be validated
   * @throws IllegalArgumentException if any of the inputs is {@literal null}.
   */
  public static void validateInputs(final Object... inputs) {
    validateInputWithMessage(ErrorConstants.NULL_ELEMENTS_NOT_ALLOWED, inputs);
  }

  /**
   * A helper method which takes in multiple arguments with a message and validate each instance.
   *
   * @param message custom message to be displayed
   * @param inputs instances to be validated
   * @throws IllegalArgumentException if any of the inputs is {@literal null}.
   */
  public static void validateInputWithMessage(String message, final Object... inputs) {
    // validate that the entire array is not empty (if length is 0)
    Validate.notEmpty(inputs, message);

    for (Object input : inputs) {
      // Ensure the element is not null
      Validate.notNull(input, message);

      if (isObjectAString(input)) {
        // Ensure the element is not blank if it's a string
        Validate.notBlank(String.valueOf(input), message);
      } else if (isObjectAnArray(input)) {
        // Ensure the element is not blank if it's an array
        Validate.notEmpty(convertToObjectArray(input), message);
      }
    }
  }

  /**
   * Checks if the object is a string then validates that it is not blank.
   *
   * @param object the object to validate
   * @return if object is valid
   */
  private static boolean isObjectAString(Object object) {
    return object instanceof String;
  }

  /**
   * Checks if the object is an array.
   *
   * @param object the object to validate
   * @return if object is an array
   */
  private static boolean isObjectAnArray(Object object) {
    return object.getClass().isArray();
  }

  /**
   * converts an object to an array if the object is an array.
   *
   * @param array the object
   * @return the array
   */
  private static Object[] convertToObjectArray(Object array) {
    Class<?> ofArray = array.getClass().getComponentType();
    if (ofArray.isPrimitive()) {
      List<Object> ar = new ArrayList<>();
      int length = Array.getLength(array);
      for (int i = 0; i < length; i++) {
        ar.add(Array.get(array, i));
      }
      return ar.toArray();
    } else {
      return (Object[]) array;
    }
  }
}
