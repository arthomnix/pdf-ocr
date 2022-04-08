package dev.arthomnix.pdfocr.main;

import dev.arthomnix.pdfocr.ocr.PDFOCRReader;
import dev.arthomnix.pdfocr.pdf.PDFTextAnnotator;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (PDDocument document = PDDocument.load(new File(args[0]))) {
            try (PDFOCRReader reader = new PDFOCRReader(document, "/home/arthomnix/Downloads/traineddata", "eng")) {
                PDFTextAnnotator annotator = new PDFTextAnnotator(document);
                annotator.addText(reader.read());
                document.save(args[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
