import OperationsWithPDF.OperationsWithPDF;
import Tools.FileDownloadFromWeb;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ex.ElementNotFound;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public String downloadFolder = "build";
    String findingText = "Кодекс корпоративной этики Группы Сбербанк одобрен Правлением Банка\n" +
            "30 сентября 2015 года (постановление Правления No534§6а), утвержден решением Наблюдательного совета Банка 29 октября 2015 года (протокол No52).";


    FileDownloadFromWeb fileDownloadFromWeb = new FileDownloadFromWeb();
    OperationsWithPDF operationsWithPDF = new OperationsWithPDF();

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

}
