package kdw003;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

public class Scenario11Case3 extends Kdw003Common {
    private Integer i = 1;

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario11Case3";
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

        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

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
        js.executeScript("$('.mgrid-free').scrollLeft(2000)");
        WaitPageLoad();
        screenShot();

        if (!checkedBox(2, 0)) {
            clickCheckBox(2, 0);
            driver.findElement(By.xpath("//div[@id='function-content']//button[1]")).click();
            WaitPageLoad();
            screenShot();
            driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']//button[@class='large']")).click();
            dialog("window_" + i);
        }
        this.uploadTestLink(1156, 279);
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