package OperationsWithPDF;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;

public class OperationsWithPDF {
    private static String dash = "----------------------------------------------------------------------------------------------";
    static Logger log = LoggerFactory.getLogger(OperationsWithPDF.class.getName());

    public String openPdfFile (String path) throws IOException {
        PDDocument document = PDDocument.load(new File(path));
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        log.info(dash);
        log.info("Открываем PDF файл: \"{}\"", path.substring(path.length() - 21));
        log.info(dash);
        document.close();
        return text;
    }

    public String pdfFileCleaning (String path) throws IOException {
        String originalTextFromPdf = openPdfFile(path);
        String lowcaseText = originalTextFromPdf.toLowerCase();
        String noSpaces = lowcaseText.replaceAll("\\s+", "");
        String noDash = noSpaces.replaceAll("-","");
        String noRlLfSymbols = noDash.replaceAll("\\r\\n ","");
        String numberConversion = noRlLfSymbols.replaceAll("№","no");
        String noMarks =numberConversion.replaceAll("•","");
        return noMarks;
    }

    public String textToFindInPdf (String checkingText){
        String lowcaseText = checkingText.toLowerCase();
        String noSpaces = lowcaseText.replaceAll("\\s+", "");
        String noDash = noSpaces.replaceAll("-","");
        String noRlLfSymbols = noDash.replaceAll("\\r\\n ","");
        String numberConversion = noRlLfSymbols.replaceAll("№","no");
        String noMarks =numberConversion.replaceAll("•","");
        return noMarks;
    }

    public boolean verifyPDFContent (String checkingText, String path) throws IOException {
        String textFromPDF = pdfFileCleaning(path);
        String textThatWeLookingFor = textToFindInPdf(checkingText);
        return textFromPDF.contains(textThatWeLookingFor);
    }
}


