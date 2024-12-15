package com.developersboard.backend.service.storage.impl;

import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.EnvConstants;
import com.developersboard.constant.StorageConstants;
import com.developersboard.exception.InvalidFileFormatException;
import com.developersboard.shared.util.core.FileUtils;
import com.developersboard.shared.util.core.ValidationUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

/**
 * This class provides working/production-ready operations available to Amazon S3.
 *
 * @author Eric Opoku
 * @version 2.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Profile({EnvConstants.PRODUCTION, EnvConstants.INTEGRATION_TEST_CI, EnvConstants.INTEGRATION_TEST})
public class AmazonS3ServiceImpl extends AbstractAmazonS3Service {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final AwsProperties properties;

  @Override
  public String storeFile(MultipartFile file, String path, String fileName)
      throws IOException, InterruptedException {
    ValidationUtils.validateInputs(file, path, fileName);

    if (file.isEmpty()) {
      LOG.debug(StorageConstants.MULTI_PART_FILE_IS_EMPTY);
      throw new InvalidFileFormatException(StorageConstants.MULTI_PART_FILE_IS_EMPTY);
    }
    File image = multipartToFile(file);
    if (Objects.nonNull(ImageIO.read(image))) {
      LOG.debug("MultipartFile is an image and a resize will be done accordingly.");
      FileUtils.resize600(image);
    }
    String imageUrl = storeFileToS3(image, path, fileName, s3Client, properties);
    if (imageUrl != null && image.exists() && Files.deleteIfExists(image.toPath())) {
      LOG.debug("Image successfully deleted!");
    }
    return imageUrl;
  }

  @Override
  public List<String> getFiles(String path) {
    Objects.requireNonNull(path, StorageConstants.PATH_CANNOT_BE_NULL);

    List<String> files = new ArrayList<>();
    ListObjectsV2Request request =
        ListObjectsV2Request.builder().bucket(properties.getS3BucketName()).prefix(path).build();

    ListObjectsV2Response response = s3Client.listObjectsV2(request);
    response.contents().forEach(object -> files.add(object.key()));

    return files;
  }

  @Override
  public InputStream getFile(String path) throws IOException {
    Objects.requireNonNull(path, StorageConstants.PATH_CANNOT_BE_NULL);

    GetObjectRequest request =
        GetObjectRequest.builder().bucket(properties.getS3BucketName()).key(path).build();

    return s3Client.getObject(request);
  }

  @Override
  public String generatePreSignedUrl(String key) {
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(properties.getS3BucketName()).key(key).build();

    GetObjectPresignRequest presignRequest =
        GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofDays(StorageConstants.PRE_SIGNED_URL_DAYS_TO_EXPIRE))
            .getObjectRequest(getObjectRequest)
            .build();

    return s3Presigner.presignGetObject(presignRequest).url().toString();
  }

  @Override
  public String renameFile(String currentKey, String newKey) {
    ValidationUtils.validateInputs(currentKey, newKey);

    String updatedNewKeyPath = newKey.replace("\\", "/");
    String bucketName = properties.getS3BucketName();

    CopyObjectRequest copyRequest =
        CopyObjectRequest.builder()
            .sourceBucket(bucketName)
            .sourceKey(currentKey)
            .destinationBucket(bucketName)
            .destinationKey(updatedNewKeyPath)
            .build();

    s3Client.copyObject(copyRequest);
    s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(currentKey).build());

    return updatedNewKeyPath;
  }

  @Override
  public void delete(String key) {
    ValidationUtils.validateInputs(key);

    String bucketName = properties.getS3BucketName();

    s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());

    LOG.debug("Object successfully deleted from bucket {} and key {}", bucketName, key);
  }
}
