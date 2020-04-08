package kdw003;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

public class Scenario3Case2 extends Kdw003Common {
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw/003/Scenario3/Case2";
        this.initDefault();
    }

    @Test
    public void test() throws Exception {
        // login
        login(employeeCode, password);
        WaitPageLoad();

        driver.get(kdw003);
        WaitPageLoad();
        
        if(!checkedBox(3, 0)){
            clickCheckBox(3, 0);
        }
        
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();

        employeeCode = "012467";
        login(employeeCode, password);
        WaitPageLoad();

        driver.get(kdw004);
        WaitPageLoad();

        driver.findElements(By.xpath("//table[@id ='approvalSttGrid']"));
        WaitElementLoad(By.linkText("013235"));
        driver.findElement(By.linkText("013235")).click();
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(909, 222);
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