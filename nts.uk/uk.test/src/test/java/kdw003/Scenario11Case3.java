package kdw003;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

        login("091636", "Jinjikoi5");

        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        Calendar startdate = Calendar.getInstance();
        startdate.set(yesterday.get(Calendar.YEAR), yesterday.get(Calendar.MONTH), 1);

        Calendar enddate = Calendar.getInstance();
        enddate.set(yesterday.get(Calendar.YEAR), yesterday.get(Calendar.MONTH) + 1, 1);
        enddate.add(Calendar.DATE, -1);

        //kmk012 change closure 1
        setProcessYearMonth(1, df3.format(yesterday.getTime()));

        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsStartDate')]//input[1]"))
        		.sendKeys(df1.format(startdate.getTime()));

        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
                .clear();
        driver.findElement(By.xpath("//div[@id = 'daterangepicker']//div[contains(@class,'ntsEndDate')]//input[1]"))
        		.sendKeys(df1.format(enddate.getTime()));
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@id = 'btnExtraction']")).click();
        dialog("window_" + i);

        if((yesterday.get(Calendar.DAY_OF_MONTH)  + 1) >= 15) {
        	js.executeScript("$('.mgrid-free').scrollTop(400)");
        }
        js.executeScript("$('.mgrid-free').scrollLeft(2000)");
        WaitPageLoad();
        screenShot();

        if (!checkedBox(yesterday.get(Calendar.DAY_OF_MONTH)  + 1, 0)) {
            clickCheckBox(yesterday.get(Calendar.DAY_OF_MONTH)  + 1, 0);
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