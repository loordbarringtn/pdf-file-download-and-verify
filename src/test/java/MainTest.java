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
        downloadPDFFile(urlWithPdfFile);
        pdfVerify();
    }
}
