package kdw003;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

public class Scenario3Case4 extends Kdw003Common {
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw/003/Scenario3/Case4";
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
        int checkBoxSize = driver.findElements(By.xpath("//table[@class ='mgrid-fixed-table']/tbody/tr")).size();

        for (int i = 2; i <= checkBoxSize -2; i++) {
            if(i == 15){
                scrollToRowColumn(30, 2);
            }
            if(!checkedBox(i, 0)){
                clickCheckBox(i, 0);
            }
        }

        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[@class = 'large']"));
        driver.findElement(By.xpath("//button[@class = 'large']")).click();

        WaitElementLoad(By.xpath("//div[@id ='function-content']/button[6]"));
        driver.findElement(By.xpath("//div[@id ='function-content']/button[6]")).click();
        WaitPageLoad();

        screenShot();
        this.uploadTestLink(911, 223);
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