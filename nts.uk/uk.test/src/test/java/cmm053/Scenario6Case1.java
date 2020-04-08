package cmm053;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import common.TestRoot;


public class Scenario6Case1 extends TestRoot {


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm053/Scenario6Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {

       
        //login 010392/Jinjikoi5 - TC6-1
        login("010392", "Jinjikoi5");
        screenShotFull();
        logout();

        //login 022497/Jinjikoi5 - TC6-2
        login("022497", "Jinjikoi5");
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("$('.placement-wraper').scrollTop(1008)");
        WaitPageLoad();
        screenShotFull();
        logout();

        //login 018937/Jinjikoi5 - TC6-3
        login("018937", "Jinjikoi5");
        screenShotFull();
        logout();

        //login 016976/Jinjikoi5 - TC6-4
        login("016976", "Jinjikoi5");
        jse.executeScript("$('.placement-wraper').scrollTop(1008)");
        WaitPageLoad();
        screenShotFull();
        logout();//done
     


    this.uploadTestLink(748, 169);
    }

        @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    public void logout() {
        driver.findElements(By.xpath("//div[contains(@class,'ui-icon-caret-1-s')]")).get(1).click();
        driver.findElement(By.xpath("//li[text()='ログアウト']")).click();
    }
}