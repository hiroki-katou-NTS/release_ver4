package kdw003;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Scenario3Case7 extends Kdw003Common {
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw/003/Scenario3/Case7";
        this.initDefault();
    }

    @Test
    public void test() throws Exception {
        // login
        employeeCode = "012467";
        login(employeeCode, password);
        WaitPageLoad();
        driver.get(kdw004);
        WaitPageLoad();
        
        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2019, 10, 1);
        WebElement wEleStart = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsStartDatePicker')]"));
        wEleStart.clear();
        wEleStart.sendKeys(df1.format(inputStartDate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2019, 10, 30);
        WebElement wEleEnd = driver.findElement(By.xpath("//div[@id ='daterangepicker']/.//input[contains(@class,'ntsEndDatePicker')]"));
        wEleEnd.clear();
        wEleEnd.sendKeys(df1.format(inputEndDate.getTime()));
        driver.findElement(By.xpath("//body")).click();

        driver.findElement(By.id("extractBtn")).click();
        WaitPageLoad();

        driver.findElements(By.xpath("//table[@id ='approvalSttGrid']"));
        WaitElementLoad(By.linkText("013235"));
        driver.findElement(By.linkText("013235")).click();
        WaitPageLoad();

        int checkBoxSize = driver.findElements(By.xpath("//table[@class ='mgrid-fixed-table']/tbody/tr")).size();

        for (int i = 2; i <= checkBoxSize - 2; i++) {
            if (i == 15) {
                scrollToRowColumn(30, 2);
            }
            if (checkedBox(i, 1)) {
                clickCheckBox(i, 1);
            }
        }
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();

        driver.get(kmw003Approval);
        WaitPageLoad();

        screenShot();
        this.uploadTestLink(917, 226);
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