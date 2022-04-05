package com.developersboard.constant;

/**
 * This class holds all constants used by the Storage Services.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public final class StorageConstants {

  public static final String S3_BASE_URL = "https://spring-boot-starter.s3.amazonaws.com";
  public static final String SEPARATOR = "/";
  public static final int PRE_SIGNED_URL_DAYS_TO_EXPIRE = 7;
  private static final String S3_EMAIL_IMAGE_BASE_URL = S3_BASE_URL + "/email-template-images";
  public static final String S3_EMAIL_IMG_BG_URL = S3_EMAIL_IMAGE_BASE_URL + "/email-bg.jpg";
  public static final String S3_EMAIL_IMG_UPDATE_URL = S3_EMAIL_IMAGE_BASE_URL + "/update.jpg";

  public static final String PROFILE_PATH = "profileImages";
  public static final String PROFILE_PICTURE_FILE_NAME = "profileImage";
  public static final String FILE_DOES_NOT_EXIST = "File does not exist!";
  public static final String FILE_CREATED_SUCCESSFULLY = "File created successfully!";
  public static final String A_NULL_WITHIN_METHOD =
      "fileName returned a null within multipartToFile method!";
  public static final String MULTI_PART_FILE_IS_EMPTY = "Multi part file is empty";

  public static final String ERROR_CREATING_STORAGE = "Error creating storage directory";
  public static final String MULTIPART_FILE_IS_NULL = "Multipart file is null";
  public static final String INCORRECT_FILE_FORMAT = "File format not correct or not allowed";
  public static final String PATH_CANNOT_BE_NULL = "Path cannot be null";

  private StorageConstants() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }
}
