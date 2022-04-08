package dev.arthomnix.pdfocr.pdf;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PDFImageReader {
    private final PDDocument document;

    public PDFImageReader(PDDocument document) {
        this.document = document;
    }

    public List<BufferedImage> readImages() {
        List<BufferedImage> images = new ArrayList<>();

        PDPageTree pages = document.getPages();
        pages.forEach(page -> {
            PDResources resources = page.getResources();
            Iterable<COSName> objectNames = resources.getXObjectNames();
            AtomicReference<PDImageXObject> image = new AtomicReference<>();
            objectNames.forEach(name -> {
                if (image.get() != null) {
                    throw new IllegalArgumentException("PDF must contain exactly one image per page!");
                } else {
                    try {
                        if (resources.isImageXObject(name)) {
                            image.set((PDImageXObject) resources.getXObject(name));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (image.get() == null) {
                throw new IllegalArgumentException("PDF must contain exactly one image per page!");
            } else {
                try {
                    images.add(image.get().getImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return images;
    }
}
