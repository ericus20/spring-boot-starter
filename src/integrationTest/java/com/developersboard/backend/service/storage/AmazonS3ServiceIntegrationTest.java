package com.developersboard.backend.service.storage;

import com.developersboard.IntegrationTestUtils;
import com.developersboard.constant.StorageConstants;
import com.developersboard.exception.InvalidFileFormatException;
import com.developersboard.shared.util.core.FileUtils;
import com.adobe.testing.s3mock.S3MockApplication;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

/**
 * This annotation tells JUnit to reuse the same test instance for the entire test class.
 * Then @BeforeAll does NOT need to be static anymore.
 * <p>
 * To use awsProperties inside @BeforeAll,
 * you need to avoid the static requirement that JUnit puts on @BeforeAll
 * with @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AmazonS3ServiceIntegrationTest extends IntegrationTestUtils {

  private S3MockApplication api;

  @BeforeAll
  void beforeAll() {
    var mockConfig = "jar:" + S3MockApplication.class.getProtectionDomain()
        .getCodeSource().getLocation() + "!/application.properties";
    api = S3MockApplication.start(
        "--spring.config.location=" + mockConfig,
        "--spring.profiles.active=s3mock",
        "--http.port=" + awsProperties.getServicePort(),
        "--server.port=0",
        "--springdoc.api-docs.enabled=false",
        "--springdoc.swagger-ui.enabled=false",
        "--spring.autoconfigure.exclude=" + String.join(",",
            "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration",
            "org.springframework.boot.liquibase.autoconfigure.LiquibaseAutoConfiguration",
            "org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration",
            "org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration",
            "org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration",
            "org.springframework.boot.security.autoconfigure.actuate.web.servlet.ManagementWebSecurityAutoConfiguration"));
  }

  @AfterAll
  void afterAll() {
    if (api != null) {
      api.stop();
    }
  }

  @BeforeEach
  void setUp(TestInfo testInfo) {
    multipartFile = getMultipartFile(testInfo.getDisplayName(), false);
  }

  @Test
  void storeProfileImage(TestInfo testInfo) throws Exception {
    var imageUrl = amazonS3Service.storeProfileImage(multipartFile, testInfo.getDisplayName());

    var expectedUrl =
        String.format(
            "%s/%s/profileImage.png", StorageConstants.PROFILE_PATH, testInfo.getDisplayName());

    Assertions.assertEquals(expectedUrl, imageUrl);
  }

  @Test
  void storeFileWithInvalidFileThrowsException(TestInfo testInfo) {
    multipartFile = getMultipartFile(testInfo.getDisplayName(), true);

    Assertions.assertThrows(
        InvalidFileFormatException.class,
        () ->
            amazonS3Service.storeFile(
                multipartFile, testInfo.getDisplayName(), testInfo.getDisplayName()));
  }

  @Test
  void storeFileWithImageShouldGetResized(TestInfo testInfo)
      throws IOException, InterruptedException {

    try (var mockedImageIo = Mockito.mockStatic(ImageIO.class)) {

      mockedImageIo
          .when(() -> ImageIO.read(Mockito.any(File.class)))
          .thenReturn(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));

      try (var mockedFileUtils = Mockito.mockStatic(FileUtils.class)) {
        mockedFileUtils.when(() -> FileUtils.resize600(Mockito.any(File.class))).thenReturn(null);

        var filename = testInfo.getDisplayName();
        var expectedUrl = String.format("%s/%s.png", filename, filename);
        var imageUrl = amazonS3Service.storeFile(multipartFile, filename, filename);

        Assertions.assertEquals(expectedUrl, imageUrl);
        mockedFileUtils.verify(() -> FileUtils.resize600(Mockito.any(File.class)));
      }
      mockedImageIo.verify(() -> ImageIO.read(Mockito.any(File.class)));
    }
  }

  @Test
  void storeFilesThenGetFiles(TestInfo testInfo) throws Exception {
    var file1 = getMultipartFile(testInfo.getDisplayName());
    String secondFileName = "file2";
    var file2 = getMultipartFile(secondFileName);

    String path = "personal-files";
    var file1Url = amazonS3Service.storeFile(file1, path, testInfo.getDisplayName());
    var file2Url = amazonS3Service.storeFile(file2, path, secondFileName);

    var files = amazonS3Service.getFiles(path);

    Assertions.assertEquals(2, files.size());
    Assertions.assertTrue(files.contains(file1Url));
    Assertions.assertTrue(files.contains(file2Url));
  }

  @Test
  void generatedPreSignedUrl(TestInfo testInfo) throws Exception {
    var imageUrl = amazonS3Service.storeProfileImage(multipartFile, testInfo.getDisplayName());

    var preSignedUrl = amazonS3Service.generatePreSignedUrl(imageUrl);
    URI uri = URI.create(preSignedUrl);

    var expectedPath = String.format(
        "/profileImages/%s/profileImage.png",
        testInfo.getDisplayName()
    );

    Assertions.assertEquals(expectedPath, uri.getPath());
    Assertions.assertTrue(preSignedUrl.contains("X-Amz-Expires"));
    Assertions.assertTrue(preSignedUrl.contains("X-Amz-Signature"));
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
    Assertions.assertThrows(NoSuchKeyException.class, () -> amazonS3Service.getFile(imageUrl));
  }

  @Test
  void renameFile(TestInfo testInfo) throws IOException, InterruptedException {
    var imageUrl = amazonS3Service.storeProfileImage(multipartFile, testInfo.getDisplayName());

    String newKey = testInfo.getDisplayName() + ".png";

    String renameFile = amazonS3Service.renameFile(imageUrl, newKey);
    Assertions.assertEquals(newKey, renameFile);

    // We will get a AmazonS3Exception 404 error if the key doesn't exist.
    Assertions.assertThrows(NoSuchKeyException.class, () -> amazonS3Service.getFile(imageUrl));

    try (InputStream storedImageUrl = amazonS3Service.getFile(newKey)) {
      Assertions.assertNotNull(storedImageUrl);
    }
  }
}
