package kdw003;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Scenario11Case11 extends Kdw003Common {
    private Integer i = 1;

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario11Case11";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者

        login("013232", "Jinjikoi5");

        //kmk012 change closure 1
        setProcessYearMonth(1, "2020/05");

        showScreen003();

        for (int i = 9; i < 12; i++) {
            if (checkedBox(i, 0)) {
                clickCheckBox(i, 0);
            }
        }
        try {
            driver.findElement(By.xpath("//div[@id='function-content']//button[1]")).click();
            WaitPageLoad();
            driver.findElement(By.xpath("//div[@class='ui-dialog-buttonset']//button[@class='large']")).click();
            dialog("window_" + i);

        } catch (Exception e) {

        }

        js.executeScript("$('.mgrid-free').scrollLeft(2000)");
        WaitPageLoad();

        driver.get(domain + "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//table[@id='tbl']//label[@class='ntsCheckBox-label']//span[@class='box']"));
        driver.findElement(By.xpath("//table[@id='tbl']//label[@class='ntsCheckBox-label']//span[@class='box']"))
                .click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[contains(@class,'ntsStartDate')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[contains(@class,'ntsStartDate')]//input[1]")).sendKeys("2020/05/08");

        driver.findElement(By.xpath("//div[contains(@class,'ntsEndDate')]//input[1]")).clear();
        driver.findElement(By.xpath("//div[contains(@class,'ntsEndDate')]//input[1]")).sendKeys("2020/05/11");

        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//div[@id ='hdType']/button"));
        driver.findElement(By.xpath("//div[@id ='hdType']/button")).click();

        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id='functions-area']/button[1]")).click();
        WaitPageLoad();

        showScreen003();
        WaitPageLoad();
        js.executeScript("$('.mgrid-free').scrollLeft(2000)");
        js.executeScript("$('.mgrid-free').scrollTop(0)");

        for (int i = 9; i < 12; i++) {
            clickCheckBox(i, 0);
        }
        driver.findElement(By.xpath("//div[@id='function-content']//button[1]")).click();
        dialog("window_" + i);
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(1182, 287);
    }

    public void showScreen003() {
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        dialog("window_" + i);

        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .sendKeys("2020/05/01");

        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .sendKeys("2020/05/31");
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