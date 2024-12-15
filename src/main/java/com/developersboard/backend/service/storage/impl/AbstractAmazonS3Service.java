package com.developersboard.backend.service.storage.impl;

import com.developersboard.backend.service.storage.AmazonS3Service;
import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.StorageConstants;
import com.developersboard.shared.util.core.ValidationUtils;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

/**
 * This abstract class provides some operations available to Amazon S3 already implemented.
 *
 * @author Eric Opoku
 * @version 2.0
 * @since 1.0
 */
@Slf4j
public abstract class AbstractAmazonS3Service implements AmazonS3Service {

  /**
   * It stores the given file name in S3 and returns the key under which the file has been stored.
   *
   * @param uploadedFile The multipart file uploaded by the user
   * @param username The username for which to upload this file
   * @return The URL of the uploaded image
   * @throws IOException if any error comes up dealing with I/O operations
   * @throws InterruptedException if there are any interruptions
   */
  @Override
  public String storeProfileImage(final MultipartFile uploadedFile, final String username)
      throws IOException, InterruptedException {

    var path = String.join(StorageConstants.SEPARATOR, StorageConstants.PROFILE_PATH, username);
    return storeFile(uploadedFile, path, StorageConstants.PROFILE_PICTURE_FILE_NAME);
  }

  /**
   * Converts a multipart file to a regular file.
   *
   * @param multipart the multipart file
   * @return the converted file
   * @throws IOException if there is an error with inputs/outputs
   */
  protected File multipartToFile(final MultipartFile multipart) throws IOException {
    ValidationUtils.validateInputsWithMessage(StorageConstants.MULTIPART_FILE_IS_NULL, multipart);

    var path = multipart.getOriginalFilename();
    Objects.requireNonNull(path, StorageConstants.A_NULL_WITHIN_METHOD);

    var file = new File(path);
    if (file.createNewFile()) {
      LOG.debug(StorageConstants.FILE_CREATED_SUCCESSFULLY);
    }

    try (OutputStream fos = Files.newOutputStream(file.toPath())) {
      fos.write(multipart.getBytes());
    }

    return file;
  }

  /**
   * Ensures the bucket exists, creating it if necessary.
   *
   * @param bucketName The bucket name
   * @param s3Client The S3 client
   * @return The bucket's region
   */
  private String ensureBucketExists(final String bucketName, final S3Client s3Client) {
    try {
      s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
      LOG.debug("Bucket {} exists", bucketName);
    } catch (NoSuchBucketException e) {
      LOG.debug("Bucket {} doesn't exist. Creating one...", bucketName);
      s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
      LOG.debug("Created bucket: {}", bucketName);
    }

    return s3Client
        .getBucketLocation(GetBucketLocationRequest.builder().bucket(bucketName).build())
        .locationConstraintAsString();
  }

  /**
   * Stores a file in S3 and returns the key under which the file has been stored.
   *
   * @param resource The file resource to upload to S3
   * @param path The folder within which this file will be placed
   * @param fileName The file name (e.g., fileName.png)
   * @param s3Client The S3 client
   * @param properties The AWS properties
   * @return The S3 object key
   */
  protected String storeFileToS3(
      final File resource,
      final String path,
      final String fileName,
      final S3Client s3Client,
      final AwsProperties properties) {

    String bucketLocation = ensureBucketExists(properties.getS3BucketName(), s3Client);
    LOG.info("Bucket location: {}", bucketLocation);

    String key = path + "/" + fileName + "." + FilenameUtils.getExtension(resource.getName());
    uploadFileToS3(resource, key, s3Client, properties);

    return key;
  }

  /**
   * Uploads a file to S3.
   *
   * @param resource The file resource to upload
   * @param key The key for the file
   * @param s3Client The S3 client
   * @param properties The AWS properties
   */
  private void uploadFileToS3(
      File resource, String key, S3Client s3Client, AwsProperties properties) {
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder().bucket(properties.getS3BucketName()).key(key).build();

    LOG.debug("Starting file upload...");
    s3Client.putObject(putObjectRequest, RequestBody.fromFile(resource));
    LOG.debug("File uploaded successfully: {}", key);
  }
}
