# pdf-ocr
A command line Java application for adding text to image-only PDFs.

Very WIP. Currently renders each word individually at a fixed text size.

## CLI usage
Options:

`-i`, `--inputFile` - the input PDF document

`-o`, `--outputFile` - the file to output the new PDF document

`-t`, `--trainingData` - the path to the Tesseract training data directory 

`-l`, `--language` - the language to use for OCR (optional, defaults to 'eng')

`--visibleText` - make the text visible (by default it is selectable but invisible)

### Thanks to the following libraries:
* [Tesseract OCR](https://tesseract-ocr.github.io) - optical character recognition library
* [Bytedeco javacpp-presets](https://github.com/bytedeco/javacpp-presets) - Java bindings for Tesseract
* [Apache PDFBox](https://pdfbox.apache.org) - PDF content extraction and editing
* [jopt-simple](https://jopt-simple.github.io/jopt-simple) - CLI argument parsing