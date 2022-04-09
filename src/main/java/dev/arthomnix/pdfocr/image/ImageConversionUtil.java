package dev.arthomnix.pdfocr.image;

import org.bytedeco.leptonica.PIX;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.bytedeco.leptonica.global.lept.*;

/**
 * Methods for converting images.
 */
public class ImageConversionUtil {
    /**
     * Convert a {@link BufferedImage} to a {@link PIX} (required by Tesseract).
     * @param image The {@link BufferedImage} to convert.
     * @return A {@link PIX} representing the same image.
     * @throws IOException if an error occurs when writing the image to a ByteArrayOutputStream.
     */
    public static PIX bufferedImageToPix(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        byte[] data = out.toByteArray();
        return pixReadMemPng(data, data.length);
    }
}
