package cmm044;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case5 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm044/Scenario1Case5";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("006310", "Jinjikoi5");
        WaitPageLoad();

        //update colsure
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        setValueInputClosure("inpMonth", "2019/11");

        //KMW003A 月別実績の修正
        driver.get(domain + "nts.uk.at.web/view/kmw/003/a/index.xhtml?initmode=2");
        WaitPageLoad();


        driver.findElement(By.id("btn-unSignAll")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitElementLoad(By.xpath("//button[@class ='large']"));
        driver.findElement(By.xpath("//button[@class ='large']")).click();
        WaitPageLoad();

        WebElement el = driver.findElement(By.xpath("//td[contains(.,'013232')]"));
        el.findElements(By.xpath("following::td")).get(2).click();
        WaitPageLoad();

        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();

        screenShot();
        this.uploadTestLink(433, 98);

    }

    public void setValueInputClosure(String id, String value) {
        driver.findElement(By.id(id)).click();
        driver.findElement(By.id(id)).clear();
        WaitElementLoad(By.id(id));
        driver.findElement(By.id(id)).sendKeys(value);
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();
    }

    @AfterEach
    public void tearDown() throws Exception {
        //driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}