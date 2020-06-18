package kmw003;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;



public class Scenario1Case1 extends Kmw003Common {
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw003/Scenario1Case1";
        this.init();
        employeeCode = "020905";
        password = "Jinjikoi5";
        kmk012 = linkFullScreen("nts.uk.at.web/view/kmk/012/a/index.xhtml");
        kmw003 = linkFullScreen("nts.uk.at.web/view/kmw/003/a/index.xhtml");

    }

    @Test
    public void test() throws Exception {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        int i = 1;
        //login
        login(screenshotFile, employeeCode, password);
        WaitPageLoad();

        //set kmk012
        driver.get(kmk012);
        WaitPageLoad();
        setValueInput("inpMonth", "2019/11");
        driver.findElement(By.id("btn_save")).click();
        WaitPageLoad();
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));

        //set kmw/003 month
        driver.get(kmw003);
        WaitPageLoad();
        setValueInput("yearMonthPicker", "2019/11");
        WaitPageLoad();

        //unlock
        driver.findElement(By.cssSelector("[data-bind = '{click:unlockProcess}']")).click();
        WaitElementLoad(By.xpath("//button[@class ='yes large danger']"));
        driver.findElement(By.xpath("//button[@class ='yes large danger']")).click();
        //WaitPageLoad();
        WaitElementLoad(By.xpath("//div[@class='mgrid-fixed']"));
        i++;
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));

        //change sheet
        // WaitElementLoad(By.xpath("//li[@class ='mgrid-sheet-button ui-state-default']"));
        // driver.findElements(By.xpath("//li[@class ='mgrid-sheet-button ui-state-default']")).get(0).click();
        WaitPageLoad();
        setValueGrid(2, 1, "1:00");
        setValueGrid(3, 2, "2:00");
        setValueGrid(4, 3, "3:00");

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[@class = 'large']"));
        driver.findElement(By.xpath("//button[@class = 'large']")).click();
        // WaitElementLoad(By.xpath("//li[@class ='mgrid-sheet-button ui-state-default']"));
        // driver.findElements(By.xpath("//li[@class ='mgrid-sheet-button ui-state-default']")).get(0).click();

        i++;
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image"+ i+ ".png"));
        this.uploadTestLink(317, 65);
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