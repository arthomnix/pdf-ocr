package dev.arthomnix.pdfocr.pdf;

import dev.arthomnix.pdfocr.ocr.TextPage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;
import java.util.List;

/**
 * A class to add text to PDFs.
 * @author arthomnix
 */
public class PDFTextAnnotator {
    private final PDDocument document;
    private final boolean visible;

    /**
     * Constructor that takes in a {@link PDDocument} and whether to make the text visible or not.
     * @param document The document to add text to.
     * @param visible Whether the text should be visible. If this is {@code false}, the text will be invisible but
     *                still selectable and searchable.
     */
    public PDFTextAnnotator(PDDocument document, boolean visible) {
        this.document = document;
        this.visible = visible;
    }

    /**
     * Add text to a PDF document.
     * @param texts The text to add, as a list of {@link TextPage}s.<br>
     *              The list should be in page order and have a 1:1 mapping between items in the list and pages in the
     *              document.
     */
    public void addText(List<TextPage> texts) {
        texts.forEach(textPage -> {
            int pageNumber = texts.indexOf(textPage);
            try {
                PDPage page = document.getPage(pageNumber);
                PDPageContentStream stream = new PDPageContentStream(document, document.getPage(pageNumber), PDPageContentStream.AppendMode.APPEND, true);
                textPage.text().forEach(boundedString -> {
                    try {
                        stream.beginText();
                        stream.setTextMatrix(
                                Matrix.getTranslateInstance(
                                        boundedString.left() / (textPage.imageWidth() / page.getCropBox().getWidth()),
                                        page.getCropBox().getHeight() - boundedString.bottom() / (textPage.imageHeight() / page.getCropBox().getHeight())
                                )
                        );
                        stream.setFont(PDType1Font.HELVETICA, 10);
                        if (!visible) {
                            stream.setRenderingMode(RenderingMode.NEITHER);
                        }
                        stream.showText(boundedString.s().replaceAll("[^\\x00-\\x7F]", ""));
                        stream.endText();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
