package com.developersboard.backend.service.storage.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.developersboard.backend.service.storage.AmazonS3Service;
import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.StorageConstants;
import com.developersboard.exception.InvalidFileFormatException;
import com.developersboard.shared.util.core.ValidationUtils;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * This abstract class provides some operations available to Amazon S3 already implemented.
 *
 * @author Eric Opoku
 * @version 1.0
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
   * @throws IOException if any error comes up dealing with i/o operations
   * @throws InterruptedException if there is any interruptions
   */
  @Override
  public String storeProfileImage(final MultipartFile uploadedFile, final String username)
      throws IOException, InterruptedException {

    var path = String.join(StorageConstants.SEPARATOR, StorageConstants.PROFILE_PATH, username);
    return storeFile(uploadedFile, path, StorageConstants.PROFILE_PICTURE_FILE_NAME);
  }

  /**
   * Retrieve file content from multipart file.
   *
   * @param multipart the multipart file
   * @return the file
   * @throws IllegalStateException if something goes wrong with the internal conversion
   * @throws IOException if there is an error with inputs/outputs
   */
  protected File multipartToFile(final MultipartFile multipart) throws IOException {
    ValidationUtils.validateInputsWithMessage(StorageConstants.MULTIPART_FILE_IS_NULL, multipart);

    var path = multipart.getOriginalFilename();
    Objects.requireNonNull(path, StorageConstants.A_NULL_WITHIN_METHOD);

    var file = new File(path);
    var newFile = file.createNewFile();
    if (newFile) {
      LOG.debug(StorageConstants.FILE_CREATED_SUCCESSFULLY);
    }

    try (OutputStream fos = Files.newOutputStream(file.toPath())) {
      fos.write(multipart.getBytes());
      if (!file.exists()) {
        LOG.debug(StorageConstants.FILE_DOES_NOT_EXIST);
        throw new InvalidFileFormatException(StorageConstants.FILE_DOES_NOT_EXIST);
      }
    }
    return file;
  }

  /**
   * Asynchronously upload object to s3.
   *
   * @param resource the resource to upload
   * @param key the key
   * @param s3Client the amazonS3 object
   * @param properties the AWS properties
   * @throws InterruptedException if there is any interruption during upload
   */
  protected void processObjectTransfer(
      final File resource,
      final String key,
      final AmazonS3 s3Client,
      final AwsProperties properties)
      throws InterruptedException {
    try {
      var tm = TransferManagerBuilder.standard().withS3Client(s3Client).build();
      // TransferManager processes all transfers asynchronously,
      // so this call returns immediately.
      Upload upload = tm.upload(properties.getS3BucketName(), key, resource);
      LOG.debug("Object upload started asynchronously...");

      // Optionally, wait for the upload to finish before continuing.
      upload.waitForCompletion();
      LOG.debug("Object upload completed successfully");

    } catch (AmazonClientException ace) {
      LOG.error(
          "A client exception occurred while trying to store the "
              + "profile image {} on S3. The profile image won't be stored",
          resource.getAbsolutePath(),
          ace);
    }
  }

  /**
   * Returns the root URL where the bucket name is located.
   *
   * <p>Please note that the URL does not contain the bucket name
   *
   * @param bucketName The bucket name
   * @return the root URL where the bucket name is located.
   * @throws AmazonS3Exception If something goes wrong.
   */
  private String ensureBucketExists(final String bucketName, final AmazonS3 s3Client) {
    try {
      if (!s3Client.doesBucketExistV2(bucketName)) {
        LOG.debug("Bucket {} doesn't exists... Creating one", bucketName);
        s3Client.createBucket(bucketName);
        LOG.debug("Created bucket: {}", bucketName);
      }
      return s3Client.getBucketLocation(bucketName) + bucketName;
    } catch (AmazonClientException ace) {
      LOG.error(
          "An error occurred while connecting to S3. Will not execute " + "action for bucket: {}",
          bucketName,
          ace);
    }
    return StringUtils.EMPTY;
  }

  /**
   * It stores the given file name in S3 and returns the key under which the file has been stored.
   *
   * @param resource The file resource to upload to S3
   * @param path The folder within which this file will be placed
   * @param fileName The file name e.g. fileName.png
   * @param s3Client the amazonS3 object
   * @param properties the AWS properties
   * @return The URL of the uploaded resource or null if a problem occurred
   * @throws AmazonS3Exception If something goes wrong.
   */
  protected String storeFileToS3(
      final File resource,
      final String path,
      final String fileName,
      final AmazonS3 s3Client,
      final AwsProperties properties)
      throws InterruptedException {
    if (!resource.exists()) {
      LOG.error("The file {} does not exist. Throwing an exception", resource.getAbsolutePath());
      throw new AmazonS3Exception("The file " + resource.getAbsolutePath() + " " + "doesn't exist");
    }
    String rootBucketUrl = ensureBucketExists(properties.getS3BucketName(), s3Client);
    if (StringUtils.isEmpty(rootBucketUrl)) {
      LOG.error(
          "The bucket {} does not exist and the application was not able "
              + "to create it. Image won't be stored with the profile",
          properties.getS3BucketName());
    } else {
      String key = path + "/" + fileName + "." + FilenameUtils.getExtension(resource.getName());
      processObjectTransfer(resource, key, s3Client, properties);
      return key;
    }
    return null;
  }
}
