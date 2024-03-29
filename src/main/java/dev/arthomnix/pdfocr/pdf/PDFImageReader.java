package dev.arthomnix.pdfocr.pdf;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


/**
 * A class to read BufferedImages from a PDF document.
 * The document must contain exactly one image per page.
 * @author arthomnix
 */
public class PDFImageReader {
    private final PDDocument document;

    /**
     * Constructor that accepts a PDF document as a {@link PDDocument} instance.
     * @param document A PDF document, must contain exactly one image per page.
     */
    public PDFImageReader(PDDocument document) {
        this.document = document;
    }

    /**
     * Reads images from the PDF document.
     * @return A list of {@link BufferedImage} in page order. There is a 1:1 relation between list items and pages.
     * @throws IllegalArgumentException if the document contains a page with more or less than one image.
     */
    public List<BufferedImage> readImages() {
        List<BufferedImage> images = new ArrayList<>();

        PDPageTree pages = document.getPages();
        final int[] pageCounter = {0}; // we need to do this because of lambda weirdness

        pages.forEach(page -> {
            System.out.println("Getting image from page %d/%d".formatted(pageCounter[0] + 1, pages.getCount()));
            pageCounter[0]++;

            PDResources resources = page.getResources();
            Iterable<COSName> objectNames = resources.getXObjectNames();
            AtomicReference<PDImageXObject> image = new AtomicReference<>();
            objectNames.forEach(name -> {
                if (image.get() != null) {
                    throw new IllegalArgumentException("PDF must contain exactly one image per page!");
                } else {
                    try {
                        if (resources.isImageXObject(name)) {
                            image.set((PDImageXObject) resources.getXObject(name));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (image.get() == null) {
                throw new IllegalArgumentException("PDF must contain exactly one image per page!");
            } else {
                try {
                    images.add(image.get().getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return images;
    }
}
