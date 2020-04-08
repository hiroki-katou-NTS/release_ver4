package kdw003;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Scenario3Case5 extends Kdw003Common {
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw/003/Scenario3/Case5";
        this.initDefault();
    }

    @Test
    public void test() throws Exception {
        // login
        login(employeeCode, password);
        WaitPageLoad();

        driver.get(kdw003);
        WaitPageLoad();

        Calendar inputStartDate = Calendar.getInstance();
        inputStartDate.set(2019, 10, 1);
        Calendar inputEndDate = Calendar.getInstance();
        inputEndDate.set(2019, 10, 30);

        extractData(inputStartDate, inputEndDate);
        WebElement elementMonth = driver.findElement(By.xpath("//button[.= '本人締め解除']"));
        if(elementMonth.isDisplayed() && elementMonth.isEnabled()){
            elementMonth.click();
            WaitPageLoad();
            WaitElementLoad(By.xpath("//button[@class = 'large']"));
            driver.findElement(By.xpath("//button[@class = 'large']")).click();
        }
        if (checkedBox(10, 0)) {
            clickCheckBox(10, 0);
        }

        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[@class = 'large']"));
        driver.findElement(By.xpath("//button[@class = 'large']")).click();

        screenShot();
        this.uploadTestLink(913, 224);
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