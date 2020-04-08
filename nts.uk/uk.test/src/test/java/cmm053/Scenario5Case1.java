package cmm053;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import common.TestRoot;


public class Scenario5Case1 extends TestRoot {
    String closeMsg15="/html/body/div[5]/div[3]/div/button";

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm053/Scenario4Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {

       
        //login 010392/Jinjikoi5 - TC5-1
        login("010392", "Jinjikoi5");
        driver.get(domain+ "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        screenShotFull();
        logout();

        //login 022497/Jinjikoi5 - TC5-2
        login("022497", "Jinjikoi5");
        driver.get(domain+ "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id='grid1_check']/span/div/label")).click();
        screenShotFull();
        WaitElementLoad(By.xpath("//*[@id='functions-area']/div/button"));
        driver.findElement(By.xpath("//*[@id='functions-area']/div/button")).click();
        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.xpath("/html/body/div[6]/div[3]/div/button")).click();
        logout();

        //login 018937/Jinjikoi5 - TC5-3
        login("018937", "Jinjikoi5");
        driver.get(domain+ "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        screenShotFull();
        logout();

        //login 016976/Jinjikoi5 - TC5-4
        login("016976", "Jinjikoi5");
        driver.get(domain+ "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id='grid1_check']/span/div/label")).click();
        screenShotFull();
        WaitElementLoad(By.xpath("//*[@id='functions-area']/div/button"));
        driver.findElement(By.xpath("//*[@id='functions-area']/div/button")).click();
        WaitPageLoad();
        screenShotFull();
        driver.findElement(By.xpath("/html/body/div[6]/div[3]/div/button")).click();
        logout();
     
        //login 018937/Jinjikoi5 - TC5-3
        login("018937", "Jinjikoi5");
        driver.get(domain+ "nts.uk.at.web/view/cmm/045/a/index.xhtml?a=1");
        WaitPageLoad();
        driver.findElements(By.xpath("//label[contains(@class,'ntsCheckBox')]")).get(1).click();
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.DATE, 1);
        driver.findElement(By.xpath("//input[contains(@class,'ntsStartDatePicker')]")).clear();
        driver.findElement(By.xpath("//input[contains(@class,'ntsStartDatePicker')]")).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//input[contains(@class,'ntsEndDatePicker')]")).clear();
        driver.findElement(By.xpath("//input[contains(@class,'ntsEndDatePicker')]")).sendKeys("2020/03/31");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//*[@id='content-area']/div[2]/div[2]/div/table/tbody/tr[2]/td[2]/button"));
        driver.findElement(By.xpath("//*[@id='content-area']/div[2]/div[2]/div/table/tbody/tr[2]/td[2]/button")).click();
        WaitPageLoad();
        screenShotFull();
       // logout();


    this.uploadTestLink(746, 168);
    }

        @AfterEach
    public void tearDown() throws Exception {
        //driver.quit();
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