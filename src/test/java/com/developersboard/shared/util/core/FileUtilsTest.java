package com.developersboard.shared.util.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.core.io.ClassPathResource;

class FileUtilsTest {

  private transient File file;

  @BeforeEach
  void setUp() throws IOException {
    var resource = new ClassPathResource("/profileImage.jpeg");
    file = resource.getFile();
  }

  @Test
  void callingConstructorShouldThrowException() {
    Assertions.assertThrows(
        AssertionError.class, () -> ReflectionUtils.newInstance(FileUtils.class));
  }

  @Test
  void resizeImage600x600() throws IOException {
    File resize600 = FileUtils.resize600(file);
    BufferedImage image = ImageIO.read(resize600);

    Assertions.assertEquals(600, image.getWidth());
    Assertions.assertEquals(600, image.getHeight());
  }

  @Test
  void resizeImage200x200() throws IOException {
    int width = 200;
    int height = 150;
    File resize600 = FileUtils.resize(file, width, height);
    BufferedImage image = ImageIO.read(resize600);

    Assertions.assertEquals(width, image.getWidth());
    Assertions.assertEquals(height, image.getHeight());
  }
}
