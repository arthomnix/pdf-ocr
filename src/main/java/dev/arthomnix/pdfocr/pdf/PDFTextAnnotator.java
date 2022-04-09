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

public class PDFTextAnnotator {
    private final PDDocument document;
    private final boolean visible;

    public PDFTextAnnotator(PDDocument document, boolean visible) {
        this.document = document;
        this.visible = visible;
    }

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
                        stream.showText(boundedString.s());
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
