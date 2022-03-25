package com.developersboard;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

/**
 * This class holds common test functionalities to be used with other Test.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public class TestUtils {

  private static final String[] IGNORED_FIELDS = {
    "id", "createdAt", "createdBy", "updatedAt", "updatedBy"
  };
  private static final String[] BASE_EQUALS_AND_HASH_CODE_FIELDS = {"version", "publicId"};
  private static final String[] USER_EQUALS_FIELDS = {"publicId", "username", "email"};

  public static Collection<String> getBaseEqualsAndHashCodeFields() {
    return Collections.unmodifiableCollection(List.of(BASE_EQUALS_AND_HASH_CODE_FIELDS));
  }

  public static Collection<String> getIgnoredFields() {
    return Collections.unmodifiableCollection(Arrays.asList(IGNORED_FIELDS));
  }

  public static String[] getEntityEqualsFields(String... fields) {
    return ArrayUtils.addAll(getBaseEqualsAndHashCodeFields().toArray(new String[0]), fields);
  }

  public static Collection<String> getUserEqualsFields() {
    var userEquals = ArrayUtils.addAll(BASE_EQUALS_AND_HASH_CODE_FIELDS, USER_EQUALS_FIELDS);
    return List.of(userEquals);
  }

  /**
   * Converts an object to JSON string.
   *
   * @param object the object
   * @param <T> the type of object passed
   * @return the json string
   */
  public static <T> String toJson(T object) {
    return new Gson().toJson(object);
  }

  /**
   * Parse a JSON string to an object.
   *
   * @param content the content to be parsed
   * @param classType the class to be returned
   * @param <T> the class type
   * @return the parsed object
   */
  public static <T> T parse(String content, Class<T> classType) {
    return new Gson().fromJson(content, classType);
  }
}
