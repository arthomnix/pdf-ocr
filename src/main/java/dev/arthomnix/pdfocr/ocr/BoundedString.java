package dev.arthomnix.pdfocr.ocr;

/**
 * A string with the coordinates of the string's bounding box on a page of text.
 * @param s The string
 * @param left The x-coordinate of the left of the string's bounding box
 * @param top The y-coordinate of the top of the string's bounding box
 * @param right The x-coordinate of the right of the string's bounding box
 * @param bottom The y-coordinate of the bottom of the string's bounding box
 */
public record BoundedString(String s, int left, int top, int right, int bottom) {
}
