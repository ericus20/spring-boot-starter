package com.developersboard.shared.util.core;

import com.developersboard.constant.ErrorConstants;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

/**
 * This utility class holds all operations on I/O operations used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public final class FileUtils {
  private static final int IMG_WIDTH = 600;
  private static final int IMG_HEIGHT = 600;

  private FileUtils() {
    throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
  }

  /**
   * Resize the file provided to the default width and height.
   *
   * @param file the file
   * @return the resized file
   * @throws IOException if there is an issue with resizing
   */
  public static File resize600(final File file) throws IOException {
    return resize(file, IMG_WIDTH, IMG_HEIGHT);
  }

  /**
   * Resize the file provided to the given width and height.
   *
   * @param file the file
   * @param width the width
   * @param height the height
   * @return the resized file
   * @throws IOException if there is an issue with resizing
   */
  public static File resize(final File file, int width, int height) throws IOException {
    var image = ImageIO.read(file);

    var resizedBufferedImage = resize(image, width, height, image.getType());

    ImageIO.write(resizedBufferedImage, FilenameUtils.getExtension(file.getName()), file);
    return file;
  }

  private static BufferedImage resize(
      final BufferedImage originalImage, int width, int height, int type) {
    BufferedImage resizedImage = new BufferedImage(width, height, type);
    Graphics2D graphics2D = resizedImage.createGraphics();
    graphics2D.drawImage(originalImage, 0, 0, width, height, null);
    graphics2D.dispose();
    graphics2D.setComposite(AlphaComposite.Src);

    graphics2D.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    return resizedImage;
  }
}
