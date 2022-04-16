package com.developersboard.backend.service.storage.impl;

import com.developersboard.constant.ProfileTypeConstants;
import com.developersboard.shared.util.core.ValidationUtils;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class provides mock operations available to Amazon S3 Service.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@Profile({ProfileTypeConstants.DEV})
public class MockAmazonS3ServiceImpl extends AbstractAmazonS3Service {
  /**
   * It stores the given file name in S3 and returns the key under which the file has been stored.
   *
   * @param multipartFile The multipart file uploaded by the user
   * @param path The folder within which this file will be placed
   * @param fileName The file name e.g. fileName.png
   * @return The profile image key
   */
  @Override
  public String storeFile(
      final MultipartFile multipartFile, final String path, final String fileName) {
    LOG.info("Simulating image submission to Amazon S3...");
    LOG.info("Submission successful...");
    return path + "/" + fileName + ".png";
  }

  /**
   * Return all files under the path given.
   *
   * @param path the path
   * @return list of files
   */
  @Override
  public List<String> getFiles(String path) {
    return Collections.singletonList(path);
  }

  /**
   * Return a file for the path given.
   *
   * @param path the path
   * @return the file
   */
  @Override
  public InputStream getFile(String path) {
    return InputStream.nullInputStream();
  }

  /**
   * Pre-signed URLs allows formation of a signed URL for an Amazon S3 resource.
   *
   * @param key the key of the user as path to image file
   * @return the pre-signed url
   */
  @Override
  public String generatePreSignedUrl(String key) {
    return key;
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
    LOG.debug("Renaming current file from {} to {}", currentKey, newKey);
    return newKey;
  }

  /**
   * Deletes the folder hosted on amazon s3 assigned to user.
   *
   * @param key the key of the user as path to image file
   */
  @Override
  public void delete(String key) {
    LOG.info("Requesting to delete object with key {} from Amazon S3", key);
    LOG.info("Successfully deleted object");
  }
}
