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

/**
 * A class to read text from a PDF document containing only images. The document must have exactly one image per page.
 * @author arthomnix
 */
public class PDFOCRReader implements AutoCloseable {
    private final PDFImageReader reader;
    private final TessBaseAPI tess;

    /**
     * Constructor accepting a {@link PDDocument}, the path to the Tesseract training data and the language for
     * Tesseract to use.
     * @param document The PDF document to recognise text in.
     * @param trainingDataPath The filesystem path to Tesseract training data.
     *                         This can be obtained from <a href="https://github.com/tesseract-ocr/tessdata">
     *                         Tesseract's GitHub.</a>
     * @param language The language to use for OCR.
     */
    public PDFOCRReader(PDDocument document, String trainingDataPath, String language) {
        reader = new PDFImageReader(document);
        tess = new TessBaseAPI();
        if (tess.Init(trainingDataPath, language) != 0) {
            throw new RuntimeException("Failed to initialise Tesseract!");
        }
    }

    /**
     * Read text from the PDF document.
     * @return A list of {@link TextPage}s.<br>
     * This list is in page order with a 1:1 relationship between pages in the PDF and items in the list.
     */
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

    /**
     * End the TessBaseAPI used by this class.<br>
     * As Tesseract is a C++ library, this must be called to prevent memory leaks.<br>
     * As this class implements {@link AutoCloseable}, this method will be called automatically if a try-with-resources
     * block is used.
     */
    @Override
    public void close() {
        tess.End();
    }
}
