import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

public class YandexMarketTest {

    private WebDriver driver;
    private final String URL = "https://yandex.ru";
    @BeforeSuite
    public void setUpWebDriver() {
        System.setProperty(
                "webdriver.chrome.driver",
                "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        System.out.println("Настойка закончена.");
    }

    @BeforeTest
    public void chromeSetup() {
        driver.manage().window().maximize();
        System.out.println("Настройка браузера закончена.");
    }

    @BeforeClass
    public void appSetup() {
        driver.get(URL);
        System.out.println("Настройка приложения закончена.");
    }

    @Test
    public void testLogo() throws InterruptedException {
        WebElement yandexImg = driver.findElement(By.xpath("//div[@class = 'home-logo__default']"));
        //Thread.sleep(6000);
        assertTrue(yandexImg.isEnabled(), "Логотип не отображается.");
        System.out.println("Проверка логотипа завершена.");

        WebElement marketSection = driver.findElement(By.xpath("//a[@data-id = 'market']"));
        //Thread.sleep(6000);
        assertTrue(marketSection.isEnabled(), "Раздел Маркет не найден.");
        System.out.println("Проверка раздела \"Маркет\" завершена.");

        WebElement marketTab = driver.findElement(By.xpath("//a[@data-id = 'market']"));
        marketTab.click();
        System.out.println("Переход в раздел Маркет.");

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        assertTrue(driver.getTitle().contains("Яндекс.Маркет — покупки с быстрой доставкой"),
                "Не верный заголовок");

        WebElement searchString = driver.findElement(By.id("header-search"));
        searchString.sendKeys("ноутбук xiaomi redmibook");
        WebElement searchButton =
                driver.findElement(By.xpath("//button[@data-r = 'search-button']"));
        searchButton.click();

        WebElement checkBox =
                driver.findElement(By.xpath("//div[@class = '_1exhF _8oEFs']"));
        checkBox.click();
        Thread.sleep(6000);

        driver.navigate().refresh();

        Thread.sleep(6000);

        WebElement isChecked =
                driver.findElement(By.xpath("//input[@name= 'local-offers-first']"));

        assertTrue(isChecked.isSelected(), "Не нажат");

    }

    @AfterMethod
    public void screenShot() throws IOException {
        TakesScreenshot scr = ((TakesScreenshot) driver);
        File file = scr.getScreenshotAs(OutputType.FILE);

        FileUtils.copyFile(file, new File("screenShot.png"));
        System.out.println("Скриншот теста сделан.");
    }

    @AfterClass
    public void closeUp() {
        driver.quit();
        System.out.println("Закрытие теста");
    }

    @AfterTest
    public void reportReady() {
        System.out.println("Отчет теста сформирован.");
    }

}
