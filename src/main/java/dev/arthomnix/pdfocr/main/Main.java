package dev.arthomnix.pdfocr.main;

import dev.arthomnix.pdfocr.ocr.PDFOCRReader;
import dev.arthomnix.pdfocr.pdf.PDFTextAnnotator;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        ArgumentAcceptingOptionSpec<String> inputOption = parser.acceptsAll(List.of("i", "inputFile"), "The file to run OCR on").withRequiredArg().required();
        ArgumentAcceptingOptionSpec<String> outputOption = parser.acceptsAll(List.of("o", "outputFile"), "The file to output, with selectable text added").withRequiredArg().required();
        ArgumentAcceptingOptionSpec<String> trainingOption = parser.acceptsAll(List.of("t", "trainingData"), "Training data for Tesseract OCR (you can download this from https://github.com/tesseract-ocr/tessdata").withRequiredArg().required();
        ArgumentAcceptingOptionSpec<String> langOption = parser.acceptsAll(List.of("l", "language"), "The language to use for OCR").withRequiredArg().defaultsTo("eng");
        parser.acceptsAll(List.of("h", "help"), "Display usage information").forHelp();

        OptionSet opts = parser.parse(args);

        if (opts.has("help")) {
            try {
                parser.printHelpOn(System.out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

        try (PDDocument document = PDDocument.load(new File(opts.valueOf(inputOption)));
             PDFOCRReader reader = new PDFOCRReader(document, opts.valueOf(trainingOption), opts.valueOf(langOption))
        ) {
            new PDFTextAnnotator(document).addText(reader.read());
            document.save(opts.valueOf(outputOption));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
