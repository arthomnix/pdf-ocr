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

public class ImageTextReader {
    private final TessBaseAPI tess;

    public ImageTextReader(TessBaseAPI tess) {
        this.tess = tess;
    }

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
            strings.add(new BoundedString(Normalizer.normalize(text.getString(), Normalizer.Form.NFKC), left.get(), top.get(), right.get(), bottom.get()));
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
