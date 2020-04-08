package cmm053;

import static org.junit.jupiter.api.Assertions.fail;
import java.util.Calendar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import common.TestRoot;


public class Scenario1Case1 extends TestRoot {
    String closeMsg15="/html/body/div[5]/div[3]/div/button";
    


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm053/Scenario1Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {

        //login 025445/Jinjikoi5
        login("025445", "Jinjikoi5");
        driver.get(domain+ "nts.uk.com.web/view/cmm/053/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("A1_2")).click();
        WaitPageLoad();
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.DATE, 1);
        driver.findElement(By.id("A2_3")).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("A2_7")).sendKeys("018937");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("A1_3")).click();
        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.xpath(closeMsg15)).click();
        logout();

        //login 018937/Jinjikoi5
        login("018937", "Jinjikoi5");
        driver.get(domain+ "nts.uk.com.web/view/cmm/044/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@class,'ntsStartDatePicker')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@class,'ntsStartDatePicker')]")).get(3).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("BTN_A4_003")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.name("chk"));
        driver.findElement(By.name("chk")).click();
        driver.findElement(By.xpath("//*[@id='status-control']/table/tbody/tr[5]/td/button")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//td[contains(.,'016976')]")).get(2).click();//2 or3
        driver.findElement(By.xpath("//*[@id='functions-area-bottom']/button[1]")).click();
        driver.findElement(By.xpath("//*[@id='functions-area']/button[2]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath(closeMsg15)).click();
        screenShotFull();
        logout();

        //login 010392/Jinjikoi5
        login("010392", "Jinjikoi5");
        driver.get(domain+ "nts.uk.com.web/view/cmm/044/a/index.xhtml");
        WaitPageLoad();
        Calendar inputdate2 = Calendar.getInstance();
        driver.findElements(By.xpath("//input[contains(@class,'ntsStartDatePicker')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@class,'ntsStartDatePicker')]")).get(3).sendKeys(df1.format(inputdate2.getTime()));
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id("BTN_A4_003")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.name("chk"));
        driver.findElement(By.name("chk")).click();
        driver.findElement(By.xpath("//*[@id='status-control']/table/tbody/tr[5]/td/button")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//td[contains(.,'022497')]")).get(2).click();//2 or3
        driver.findElement(By.xpath("//*[@id='functions-area-bottom']/button[1]")).click();
        driver.findElement(By.xpath("//*[@id='functions-area']/button[2]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath(closeMsg15)).click();
        screenShotFull();
        logout();

        //TC1-1
        //go cmm053
        login("025445", "Jinjikoi5");
        driver.get(domain+ "nts.uk.com.web/view/cmm/053/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("A1_5")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        driver.findElement(By.xpath("//*[@id='B1_1_grid']/tbody/tr[2]/td")).click();
        driver.findElement(By.xpath("//*[@id='B1_1_grid']/tbody/tr[2]/td")).click();
        screenShotFull();

        //go kaf010
        driver.get(domain+ "nts.uk.at.web/view/kaf/010/a/index.xhtml");
        WaitPageLoad();
        //Calendar inputdate2 = Calendar.getInstance();
        driver.findElement(By.id("inputdate")).clear();
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(inputdate2.getTime()));
        // JavascriptExecutor jse = (JavascriptExecutor)driver;
        js.executeScript("$('#contents-area').scrollTop(1008)");
        WaitPageLoad();
        screenShotFull();

        //TC1-2
         //go cmm053
         
         driver.get(domain+ "nts.uk.com.web/view/cmm/053/a/index.xhtml");
         WaitPageLoad();
         driver.findElement(By.id("A1_5")).click();
         WaitPageLoad();
         //driver.switchTo().frame("window_1");
         //driver.findElement(By.xpath("//*[@id='B1_1_grid']/tbody/tr[2]/td")).click();
         screenShotFull();//done

         //Delete cmm053
         driver.switchTo().frame("window_1");
         driver.findElement(By.id("B2_1")).click();
         WaitElementLoad(By.id("A1_4"));
         driver.findElement(By.id("A1_4")).click();
         WaitElementLoad(By.xpath("/html/body/div[7]/div[3]/div/button[1]"));
         driver.findElement(By.xpath("/html/body/div[7]/div[3]/div/button[1]")).click();
         driver.findElement(By.xpath(closeMsg15)).click();

         this.uploadTestLink(733, 164);
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