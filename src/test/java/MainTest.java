import Tools.FileDownloadFromWeb;
import com.codeborne.selenide.Configuration;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static com.codeborne.selenide.Selenide.*;

public class MainTest extends CoreLogic {

    @BeforeEach
    public  void setUp() {
        Configuration.browser = "chrome";
        System.setProperty("selenide.browser", "chrome");
        Configuration.browserSize = "1920x1080";
        Configuration.startMaximized = true;
    }

    @Test
    public void downloadAndVerifyPDFTest () throws IOException {
        open(url);
        clickOnElement(escho);
        clickOnElement(sharesAndInvtorsDrpDwn);
        clickOnElement(sharesAndInvtors);
        clickOnElement(disclosure);
        clickOnElement(corpCharAndOthDoc);
        String pageToClose = title();
        log.info("Title страницы к закрытию: \"{}\"", pageToClose);
        clickOnElement(codeOfCorpEthics);
        switchTo().window(pageToClose).close();
        log.info("Закрыли ненужную страницу: \"{}\"", pageToClose);
        String urlWithPdfFile = switchTo().window(0).getCurrentUrl();
        log.info("URL страницы с PDF файлом: \"{}\"", urlWithPdfFile);
        Assert.assertTrue(urlWithPdfFile.contains(".pdf"));
        log.info("Проверили наличие в URL расширения \"pdf\"");

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

        String pdfFileToOpen = fileDownloadFromWeb.getFileAbsolutePath(downloadFolder) +
                File.separator + "downloads" + File.separator + getDownloadedFileName;
        final String pdfFileToOpenFileName= pdfFileToOpen.substring(pdfFileToOpen.length() - 21);
        log.info("Приступаем к открытию файла \"{}\"", pdfFileToOpen);
        fileDownloadFromWeb.ifFileExists(pdfFileToOpen);

        boolean verificationResult = operationsWithPDF.verifyPDFContent(findingText, pdfFileToOpen);

        log.info("Результат сравнения исходного текста с текстом в PDF: \"{}\"",verificationResult);
        if (verificationResult){
            log.info("Результат верификации файла \"{}\" положительный!", pdfFileToOpenFileName);
        } else {
            log.info("Результат верификации файла \"{}\" отрицательный!", pdfFileToOpenFileName);
        }
    }
}
