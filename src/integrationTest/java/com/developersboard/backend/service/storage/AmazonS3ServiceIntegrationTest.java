package com.developersboard.backend.service.storage;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.developersboard.IntegrationTestUtils;
import com.developersboard.constant.StorageConstants;
import com.developersboard.exception.InvalidFileFormatException;
import com.developersboard.shared.util.core.FileUtils;
import io.findify.s3mock.S3Mock;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;

class AmazonS3ServiceIntegrationTest extends IntegrationTestUtils {

  /*
  S3Mock.create(8001, "/tmp/s3");
  */
  private transient S3Mock api;

  @BeforeEach
  void setUp(TestInfo testInfo) {
    multipartFile = getMultipartFile(testInfo.getDisplayName(), false);

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
    var expectedUrl =
        String.format(
            "%s/%s/profileImages/%s/profileImage.png",
            awsProperties.getServiceEndpoint(),
            awsProperties.getS3BucketName(),
            URLEncoder.encode(testInfo.getDisplayName(), StandardCharsets.UTF_8));

    Assertions.assertEquals(expectedUrl, preSignedUrl);
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
