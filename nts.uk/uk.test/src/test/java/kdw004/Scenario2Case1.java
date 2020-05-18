package kdw004;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import common.TestRoot;
import kdw003.Kdw003Common;


public class Scenario2Case1 extends Kdw003Common {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario2Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // login申請者
    	login("007102", "Jinjikoi5");

        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2020, 4, 1);
        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2020, 4, 31);

        // Setting screen cmm053
        driver.get(domain + "nts.uk.com.web/view/cmm/053/a/index.xhtml");
        WaitPageLoad();

        WaitElementLoad(By.id("A2_7"));
        driver.findElement(By.id("A2_7")).clear();
        driver.findElement(By.id("A2_7")).click();
        driver.findElement(By.id("A2_7")).sendKeys("017170");

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("document.activeElement.blur();");    //leave focus

        WaitElementLoad(By.id("A1_3"));
        driver.findElement(By.id("A1_3")).click();

        driver.get(domain + "nts.uk.com.web/view/cmm/053/a/index.xhtml");
        WaitPageLoad();
        screenShot();

        // login承認者
    	login("004515", "Jinjikoi5");

        // Go to screen Kdw004a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        setKdw004Period(inputStartDate, inputEndDate);

        WaitPageLoad();
        screenShot();
        WaitPageLoad();
        this.uploadTestLink(856, 204);
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