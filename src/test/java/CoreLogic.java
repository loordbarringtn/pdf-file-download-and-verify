import OperationsWithPDF.OperationsWithPDF;
import Tools.FileDownloadFromWeb;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ex.ElementNotFound;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

public class CoreLogic {
    Logger log = LoggerFactory.getLogger(CoreLogic.class.getName());
    public String url = "https://www.sberbank.ru/ru/person";
    public String escho = "//div[contains(@class,'segments__link segments__dropdown_button')]";
    public String sharesAndInvtorsDrpDwn = "//a[@class='segments__dropdown_link '][contains(text(),'Акционерам и инвесторам')]";
    public String sharesAndInvtors = "//span[contains(@class, 'lg-menu__text') and contains(text(),'Акционерам и инвесторам')]";
    public String disclosure = "//li[@class='lg-menu__item lg-menu__item_hovered']//a[contains(.,'Регуляторное раскрытие информации')]";
    public String corpCharAndOthDoc = "//img[@alt='Устав и иные документы Банка']";
    public String codeOfCorpEthics = "//a[@href='/common/img/uploaded/files/pdf/normative_docs/Sberbank_Code_of_corporate_ethics.pdf']";
    public static String downloadFolder = "build";
    public static String findingText = "Кодекс корпоративной этики Группы Сбербанк одобрен Правлением Банка\n" +
            "30 сентября 2015 года (постановление Правления No534§6а), утвержден решением Наблюдательного совета Банка 29 октября 2015 года (протокол No52).";
    public static String pdfFileToOpen;
    public static String pdfFileToOpenFileName;

    public void clickOnElement (String elementXpath){
        try {
            String elementText = $(By.xpath(elementXpath)).waitUntil(Condition.visible,20000).getText();
            $(By.xpath(elementXpath)).waitUntil(Condition.visible,20000).click();
            log.info("Клик по элементу: \"{}\"",elementText);
        } catch (ElementNotFound elementNotFound) {
            log.error("Элемент: \"{}\"", elementXpath);
            throw new NoSuchElementException(elementNotFound.getMessage());
        }
    }

    public static void downloadPDFFile (String urlWithPdfFile){
        FileDownloadFromWeb fileDownloadFromWeb = new FileDownloadFromWeb();
        Logger log = LoggerFactory.getLogger(CoreLogic.class.getName());

        try {
            fileDownloadFromWeb.ifDirectoryExists(downloadFolder, "downloads");
        } catch (IOException error) {
            error.printStackTrace();
        }

        File fileDownloadPath = new File (fileDownloadFromWeb.getFileAbsolutePath(downloadFolder) +
                File.separator + "downloads" + File.separator + fileDownloadFromWeb.getCurrentDateAndTime() + ".pdf");
        String getDownloadedFileName = fileDownloadPath.getName();

        Thread thread = new Thread(new FileDownloadFromWeb(urlWithPdfFile, fileDownloadPath));
        log.info("Имя для скаченного файла: \"{}\"", getDownloadedFileName);
        thread.start();

        while (thread.isAlive()){}

        pdfFileToOpen = fileDownloadFromWeb.getFileAbsolutePath(downloadFolder) +
                File.separator + "downloads" + File.separator + getDownloadedFileName;
        pdfFileToOpenFileName= pdfFileToOpen.substring(pdfFileToOpen.length() - 21);
        log.info("Приступаем к открытию файла \"{}\"", pdfFileToOpen);
        fileDownloadFromWeb.ifFileExists(pdfFileToOpen);
    }

    public static void pdfVerify () throws IOException {
        OperationsWithPDF operationsWithPDF = new OperationsWithPDF();
        boolean verificationResult = operationsWithPDF.verifyPDFContent(findingText, pdfFileToOpen);
        Logger log = LoggerFactory.getLogger(CoreLogic.class.getName());

        log.info("Результат сравнения исходного текста с текстом в PDF: \"{}\"",verificationResult);
        if (verificationResult){
            log.info("Результат верификации файла \"{}\" положительный!", pdfFileToOpenFileName);
        } else {
            log.info("Результат верификации файла \"{}\" отрицательный!", pdfFileToOpenFileName);
        }
    }

}
