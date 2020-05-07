package kdw003;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

public class Scenario3Case1 extends Kdw003Common {
    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw/003/Scenario3/Case1";
        this.initDefault();
    }

    @Test
    public void test() throws Exception {
        // login
        login(employeeCode, password);
        WaitPageLoad();

        driver.get(kdw003);
        WaitPageLoad();

        clickCheckBox(6, 0);
        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(907, 221);
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