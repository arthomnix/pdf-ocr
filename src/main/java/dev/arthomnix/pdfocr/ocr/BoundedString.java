package dev.arthomnix.pdfocr.ocr;

public record BoundedString(String s, int left, int top, int right, int bottom) {
}
