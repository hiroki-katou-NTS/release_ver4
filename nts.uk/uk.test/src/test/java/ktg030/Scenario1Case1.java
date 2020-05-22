package ktg030;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import kdw003.Kdw003Common;


public class Scenario1Case1 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/ktg030/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
        login("004515", "Jinjikoi5");

        // Setting screen kmk012
        setProcessYearMonth(1, "2020/05");

        // Setting screen kmw003
        driver.get(domain + "nts.uk.at.web/view/kmw/003/a/index.xhtml?initmode=2");
        WaitPageLoad();
        // Check checkbox of data 2019/11
        if (driver.findElements(By.xpath("//input[(@checked='checked')]")).size() <= 1) {
            driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox')]")).get(11).click();
            Thread.sleep(100);
            driver.findElements(By.xpath("//button")).get(0).click();
            WaitPageLoad();
            driver.findElements(By.xpath("//button[contains(@class,'large')]")).get(1).click();
        }

        // Go to 2019/12
        driver.findElement(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).click();
        WaitPageLoad();

        // Check checkbox of data 2019/12
        if (driver.findElements(By.xpath("//input[(@checked='checked')]")).size() <= 1) {
            driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox')]")).get(3).click();
            Thread.sleep(100);
            driver.findElements(By.xpath("//button")).get(0).click();
            WaitPageLoad();
            driver.findElements(By.xpath("//button[contains(@class,'large')]")).get(1).click();
        }

        // Go to screen cgg008
        driver.get(domain + "nts.uk.com.web/view/ccg/008/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//li[@aria-controls='tab-1']"));
        driver.findElement(By.xpath("//li[@aria-controls='tab-1']")).click();

        // Take a photo
        screenShot();
        Thread.sleep(500);

        // Go to screen kmw003
        driver.get(domain + "nts.uk.at.web/view/kmw/003/a/index.xhtml?initmode=2");
        WaitPageLoad();

        // Take a photo
        screenShot();

        WaitPageLoad();
        this.uploadTestLink(127, 28);
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