package com.developersboard.backend.service.storage;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.developersboard.IntegrationTestUtils;
import com.developersboard.config.properties.AwsProperties;
import com.developersboard.constant.StorageConstants;
import io.findify.s3mock.S3Mock;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

class AmazonS3ServiceIntegrationTest extends IntegrationTestUtils {

  @Mock private transient MockMultipartFile multipartFile;

  @Autowired private transient AwsProperties awsProperties;

  @Autowired private transient AmazonS3Service amazonS3Service;

  /*
  S3Mock.create(8001, "/tmp/s3");
  */
  private transient S3Mock api;
  private transient String expectedUrl;

  @BeforeEach
  void setUp(TestInfo testInfo) {
    expectedUrl =
        String.format(
            "%s/%s/profileImage.png", StorageConstants.PROFILE_PATH, testInfo.getDisplayName());

    multipartFile =
        new MockMultipartFile("test", "test.png", "image", "bar".getBytes(StandardCharsets.UTF_8));

    api =
        new S3Mock.Builder()
            .withPort(Integer.parseInt(awsProperties.getServicePort()))
            .withInMemoryBackend()
            .build();
    api.start();
  }

  @AfterEach
  void tearDown() {
    api.shutdown(); // kills the underlying actor system. Use api.stop() to just unbind the port.
  }

  @Test
  void storeProfileImage(TestInfo testInfo) throws Exception {
    var imageUrl = amazonS3Service.storeProfileImage(multipartFile, testInfo.getDisplayName());

    Assertions.assertEquals(expectedUrl, imageUrl);
  }

  @Test
  void deleteProfileImage(TestInfo testInfo) throws IOException, InterruptedException {
    var imageUrl = amazonS3Service.storeProfileImage(multipartFile, testInfo.getDisplayName());
    try (InputStream storedImageUrl = amazonS3Service.getFile(imageUrl)) {
      Assertions.assertNotNull(storedImageUrl);
    }

    // Delete image from s3.
    amazonS3Service.delete(imageUrl);

    // We will get a AmazonS3Exception 404 error if the key doesn't exist.
    Assertions.assertThrows(AmazonS3Exception.class, () -> amazonS3Service.getFile(imageUrl));
  }

  @Test
  void renameFile(TestInfo testInfo) throws IOException, InterruptedException {
    var imageUrl = amazonS3Service.storeProfileImage(multipartFile, testInfo.getDisplayName());
    Assertions.assertEquals(expectedUrl, imageUrl);

    String newKey = testInfo.getDisplayName() + ".png";

    String renameFile = amazonS3Service.renameFile(imageUrl, newKey);
    Assertions.assertEquals(newKey, renameFile);

    // We will get a AmazonS3Exception 404 error if the key doesn't exist.
    Assertions.assertThrows(AmazonS3Exception.class, () -> amazonS3Service.getFile(imageUrl));

    try (InputStream storedImageUrl = amazonS3Service.getFile(newKey)) {
      Assertions.assertNotNull(storedImageUrl);
    }
  }
}
