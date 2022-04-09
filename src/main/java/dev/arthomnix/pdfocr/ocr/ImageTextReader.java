package dev.arthomnix.pdfocr.ocr;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.ResultIterator;
import org.bytedeco.tesseract.TessBaseAPI;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.tesseract.global.tesseract.TessMonitorCreate;

/**
 * A class to read text from an image.
 * @author arthomnix
 */
public class ImageTextReader {
    private final TessBaseAPI tess;

    /**
     * Constructor that accepts an instance of TessBaseAPI for text recognition.
     *
     * @param tess An instance of TessBaseAPI (must be initialised).
     */
    public ImageTextReader(TessBaseAPI tess) {
        this.tess = tess;
    }

    /**
     * Read text from an image.
     * @param image The image to read, as an instance of {@link PIX}.
     * @param level The level of text to read (characters, words, lines etc.)
     *              (See RIL_WORD, RIL_TEXTLINE etc in {@link org.bytedeco.tesseract.global.tesseract})
     * @return A list of {@link BoundedString}s representing each piece of text.
     *         The size of each piece of text depends on the {@code level} parameter.
     */
    public List<BoundedString> readImage(PIX image, int level) {
        List<BoundedString> strings = new ArrayList<>();

        tess.SetImage(image);
        tess.Recognize(TessMonitorCreate());

        ResultIterator ri = tess.GetIterator();
        ri.Begin();
        while (ri.Next(level)) {
            BytePointer text = ri.GetUTF8Text(level);
            IntPointer left = new IntPointer(1);
            IntPointer top = new IntPointer(1);
            IntPointer right = new IntPointer(1);
            IntPointer bottom = new IntPointer(1);
            ri.BoundingBox(level, left, top, right, bottom);
            strings.add(
                    new BoundedString(
                            Normalizer.normalize(text.getString(), Normalizer.Form.NFKC),
                            left.get(), top.get(), right.get(), bottom.get()
                    )
            );
            text.deallocate();
            left.deallocate();
            top.deallocate();
            right.deallocate();
            bottom.deallocate();
        }
        ri.deallocate();
        return strings;
    }

}
