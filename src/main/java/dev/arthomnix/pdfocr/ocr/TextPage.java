package dev.arthomnix.pdfocr.ocr;

import java.util.List;

/**
 * Represents a page of text, retrieved from a PDF by OCR.
 * @param imageWidth The width of the image embedded within the PDF.
 *                   This is important because pixels in the image don't line up with the PDF coordinate system, so we
 *                   need to correct for this.
 * @param imageHeight The height of the image embedded within the PDF.
 * @param text A list of {@link BoundedString}s representing all the text in the page.
 */
public record TextPage(int imageWidth, int imageHeight, List<BoundedString> text) {
}
