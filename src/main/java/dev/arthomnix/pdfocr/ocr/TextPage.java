package dev.arthomnix.pdfocr.ocr;

import java.util.List;

public record TextPage(int imageWidth, int imageHeight, List<BoundedString> text) {
}
