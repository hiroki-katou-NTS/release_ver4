package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

public class Scenario11Case19 extends Kdw003Common {
    private Integer i = 1;

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario11Case19";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者

        login("016209", "Jinjikoi5");

         //kmk012 change closure 1
         driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
         WaitPageLoad();
         driver.findElement(By.id("inpMonth")).click();
         driver.findElement(By.id("inpMonth")).clear();
         WaitElementLoad(By.id("inpMonth"));
         driver.findElement(By.id("inpMonth")).sendKeys("2019/11");
         driver.findElement(By.xpath("//body")).click();
         WaitElementLoad(By.id("btn_save"));
         driver.findElement(By.id("btn_save")).click();

        showScreen003();
        WaitPageLoad();
        js.executeScript("$('.mgrid-free').scrollLeft(2000)");
        js.executeScript("$('.mgrid-free').scrollTop(0)");
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(1207, 295);
    }

    public void showScreen003() {
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        dialog("window_" + i);

        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .sendKeys("2019/11/01");

        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .sendKeys("2019/11/30");
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@id = 'btnExtraction']")).click();

        dialog("window_" + i);
    }

    public void dialog(String wd) {
        try {
            WebElement dialogError = driver.findElement(By.xpath("//iframe[contains(@name,'" + wd + "')]"));
            WaitPageLoad();
            if (dialogError.isDisplayed()) {
                driver.switchTo().frame(dialogError);
                WaitPageLoad();
                driver.findElement(By.id("dialogClose")).click();
                i++;
            }
        } catch (Exception ex) {

        }
        WaitPageLoad();
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}