package com.developersboard.backend.service.storage.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.constant.StorageConstants;
import com.developersboard.exception.InvalidFileFormatException;
import com.developersboard.shared.util.core.FileUtils;
import com.developersboard.shared.util.core.ValidationUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class provides working/production ready operations available to Amazon S3.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Profile({ProfileTypeConstants.PROD, ProfileTypeConstants.TEST})
public class AmazonS3ServiceImpl extends AbstractAmazonS3Service {

  /**
   * Amazon S3 provides storage for the Internet, and is designed to make web-scale computing easier
   * for developers.
   */
  private final AmazonS3 s3Client;

  private final AwsProperties properties;

  /**
   * It stores the given file name in S3 and returns the key under which the file has been stored.
   *
   * @param file The multipart file uploaded by the user
   * @param path The folder within which this file will be placed
   * @param fileName The file name eg. fileName.png
   * @return The profile image key
   * @throws IOException If something goes wrong with file handling
   * @throws InterruptedException if there is any interruptions
   */
  @Override
  public String storeFile(MultipartFile file, String path, String fileName)
      throws IOException, InterruptedException {

    ValidationUtils.validateInputs(file, path, fileName);

    if (file.isEmpty()) {
      LOG.debug(StorageConstants.MULTI_PART_FILE_IS_EMPTY);
      throw new InvalidFileFormatException(StorageConstants.MULTI_PART_FILE_IS_EMPTY);
    }
    File image = multipartToFile(file);
    // check if the file to upload is an image then we will resize accordingly.
    if (Objects.nonNull(ImageIO.read(image))) {
      LOG.debug("MultipartFile is an image and a resize will be done accordingly.");
      FileUtils.resize600(image);
    }
    var imageUrl = storeFileToS3(image, path, fileName, s3Client, properties);
    if (imageUrl != null && image.exists() && Files.deleteIfExists(image.toPath())) {
      LOG.debug("Image successfully deleted!");
    }
    return imageUrl;
  }

  /**
   * Return all files under the path given.
   *
   * @param path the path
   * @return list of files
   * @throws AmazonS3Exception if the path is not found
   */
  @Override
  public List<String> getFiles(String path) {
    Objects.requireNonNull(path, StorageConstants.PATH_CANNOT_BE_NULL);

    List<String> files = new ArrayList<>();
    var objectsV2Result = s3Client.listObjectsV2(properties.getS3BucketName(), path);
    for (S3ObjectSummary objectSummary : objectsV2Result.getObjectSummaries()) {
      files.add(objectSummary.getKey());
    }
    return files;
  }

  /**
   * Return a file for the path given.
   *
   * @param path the path
   * @return the file
   * @throws AmazonS3Exception if the path is not found
   */
  @Override
  public InputStream getFile(String path) throws IOException {
    Objects.requireNonNull(path, StorageConstants.PATH_CANNOT_BE_NULL);

    try (S3Object s3ClientObject = s3Client.getObject(properties.getS3BucketName(), path)) {
      try (S3ObjectInputStream content = s3ClientObject.getObjectContent()) {
        return content;
      }
    }
  }

  /**
   * Pre-signed URLs allows formation of a signed URL for an Amazon S3 resource.
   *
   * @param key the key of the user as path to image file
   * @return the pre-signed url
   */
  @Override
  public String generatePreSignedUrl(String key) {
    var expiration = DateUtils.addDays(new Date(), StorageConstants.PRE_SIGNED_URL_DAYS_TO_EXPIRE);
    return s3Client.generatePresignedUrl(properties.getS3BucketName(), key, expiration).toString();
  }

  /**
   * Rename the currentKey with the new key.
   *
   * @param currentKey the current key
   * @param newKey the new key
   * @return the updated key
   */
  @Override
  public String renameFile(String currentKey, String newKey) {
    ValidationUtils.validateInputs(currentKey, newKey);

    var updatedNewKeyPath = newKey.replace("\\", "/");
    // create the request to make a copy of the object with the new name
    var bucketName = properties.getS3BucketName();
    var copyObjRequest =
        new CopyObjectRequest(bucketName, currentKey, bucketName, updatedNewKeyPath);

    // submit the request to make a copy of the object
    s3Client.copyObject(copyObjRequest);

    // delete the previous copy as it is not needed anymore.
    s3Client.deleteObject(new DeleteObjectRequest(bucketName, currentKey));

    return updatedNewKeyPath;
  }

  /**
   * Deletes the folder hosted on amazon s3 assigned to user.
   *
   * @param key the key of the user as path to image file
   */
  @Override
  public void delete(String key) {
    ValidationUtils.validateInputs(key);

    var bucketName = properties.getS3BucketName();

    s3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
    LOG.debug("Object successfully deleted from bucket {} and key {}", bucketName, key);
  }
}
