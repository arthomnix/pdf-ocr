package dev.arthomnix.pdfocr.ocr;

import dev.arthomnix.pdfocr.image.ImageConversionUtil;
import dev.arthomnix.pdfocr.pdf.PDFImageReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.tesseract.global.tesseract.*;

public class PDFOCRReader implements AutoCloseable {
    private final PDFImageReader reader;
    private final TessBaseAPI tess;

    public PDFOCRReader(PDDocument document, String trainingDataPath, String language) throws IOException {
        reader = new PDFImageReader(document);
        tess = new TessBaseAPI();
        if (tess.Init(trainingDataPath, language) != 0) {
            throw new RuntimeException("Failed to initialise Tesseract!");
        }
    }

    public List<TextPage> read() {
        List<TextPage> pages = new ArrayList<>();
        List<BufferedImage> images = reader.readImages();
        ImageTextReader imageReader = new ImageTextReader(tess);
        images.forEach(image -> {
            try {
                PIX pix = ImageConversionUtil.bufferedImageToPix(image);
                pages.add(new TextPage(image.getWidth(), image.getHeight(), imageReader.readImage(pix, RIL_WORD)));
                pixDestroy(pix);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return pages;
    }

    @Override
    public void close() {
        tess.End();
    }
}
