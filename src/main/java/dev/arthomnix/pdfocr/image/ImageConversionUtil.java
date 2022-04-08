package dev.arthomnix.pdfocr.image;

import org.bytedeco.leptonica.PIX;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.bytedeco.leptonica.global.lept.*;

public class ImageConversionUtil {
    public static PIX bufferedImageToPix(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        byte[] data = out.toByteArray();
        return pixReadMemPng(data, data.length);
    }
}
