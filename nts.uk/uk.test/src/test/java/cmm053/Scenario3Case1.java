package cmm053;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import common.TestRoot;


public class Scenario3Case1 extends TestRoot {
    
    String inpMonth ="inpMonth";//id
    String btnsave = "btn_save";//id
    String closeMsg15="/html/body/div[5]/div[3]/div/button";

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm053/Scenario3Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
// phải setting trước cmm018 mới chạy được
       
        // //cmm018
        // login("000001", "0");
        // driver.get(domain+ "nts.uk.com.web/view/cmm/018/a/index.xhtml");
        // WaitPageLoad();
        // driver.findElement(By.xpath("//*[@id='sidebar-area']/div/ul/li[3]/a")).click();
        // WaitPageLoad();
        // //search
        // driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        // WaitElementLoad(By.xpath("//*[@id='tab-panel']/ul/li[3]"));
        // driver.findElement(By.xpath("//*[@id='tab-panel']/ul/li[3]")).click();
        // WaitElementLoad(By.id("ccg001-input-code"));
        // driver.findElement(By.id("ccg001-input-code")).sendKeys("025445");
        // WaitElementLoad(By.xpath("//*[@id='ccg001-part-g']/div[1]/button"));
        // driver.findElement(By.xpath("//*[@id='ccg001-part-g']/div[1]/button")).click();
        // WaitPageLoad();
        // WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        // driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();

        // WaitElementLoad(By.xpath("//button[contains(@class,'dateControlBtn')]"));
        // driver.findElements(By.xpath("//button[contains(@class,'dateControlBtn')]")).get(8).click();
        // driver.switchTo().frame("window_1");
        // WaitElementLoad(By.id("startDateInput"));
        // driver.findElement(By.id("startDateInput")).click();
        // driver.findElement(By.id("startDateInput")).sendKeys("2020/01/19");
        // driver.findElement(By.xpath("//body")).click();
        // WaitElementLoad(By.xpath("//*[@id='functions-area-bottom']/button[1]"));
        // driver.findElement(By.xpath("//*[@id='functions-area-bottom']/button[1]")).click();
        // WaitElementLoad(By.xpath("//*[@id='fixed-tablePs']/tbody/tr[1]/td[2]/button"));
        // driver.findElement(By.xpath("//*[@id='fixed-tablePs']/tbody/tr[1]/td[2]/button")).click();
        // WaitPageLoad();

        // driver.switchTo().frame("window_2");
        // WaitElementLoad(By.xpath("//input[contains(@class,'ntsSearchBox')]"));
        // driver.findElements(By.xpath("//input[contains(@class,'ntsSearchBox')]")).get(0).click();
        // driver.findElements(By.xpath("//input[contains(@class,'ntsSearchBox')]")).get(0).clear();
        // driver.findElements(By.xpath("//input[contains(@class,'ntsSearchBox')]")).get(0).sendKeys("003130");
        // driver.findElement(By.xpath("//body")).click();

        // WaitElementLoad(By.xpath("//button[contains(@class,'search-btn')]"));
        // driver.findElements(By.xpath("//button[contains(@class,'search-btn')]")).get(0).click();
        // WaitPageLoad();
        // JavascriptExecutor jse = (JavascriptExecutor)driver;
        // jse.executeScript("$('#contents-area').scrollTop(937)");
        // WaitPageLoad();
        // WaitElementLoad(By.xpath("//td[contains(.,'018937')]"));
        // driver.findElements(By.xpath("//td[contains(.,'018937')]")).get(2).click();
        // WaitElementLoad(By.xpath("//*[@id='swap-list-move-data']/button[1]"));
        // driver.findElement(By.xpath("//*[@id='swap-list-move-data']/button[1]")).click();
        // WaitElementLoad(By.xpath("//td[contains(.,'016976')]"));
        // driver.findElements(By.xpath("//td[contains(.,'016976')]")).get(2).click();
        // WaitElementLoad(By.xpath("//*[@id='swap-list-move-data']/button[1]"));
        // driver.findElement(By.xpath("//*[@id='swap-list-move-data']/button[1]")).click();
        // WaitElementLoad(By.xpath("//td[contains(.,'010392')]"));
        // driver.findElements(By.xpath("//td[contains(.,'010392')]")).get(2).click();
        // WaitElementLoad(By.xpath("//*[@id='swap-list-move-data']/button[2]"));
        // driver.findElement(By.xpath("//*[@id='swap-list-move-data']/button[2]")).click();
        // WaitElementLoad(By.id("execution"));
        // driver.findElement(By.id("execution")).click();
        // WaitElementLoad(By.xpath("//*[@id='tabpanel-3']/div[1]/button[1]"));
        // driver.findElement(By.xpath("//*[@id='tabpanel-3']/div[1]/button[1]")).click();
        // driver.findElement(By.xpath(closeMsg15)).click();
        // logout();

        //login 018937/Jinjikoi5
        login("018937", "Jinjikoi5");
        driver.get(domain+ "nts.uk.com.web/view/cmm/044/a/index.xhtml");
        WaitPageLoad();
        Calendar inputdate = Calendar.getInstance();
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
        logout();

        //  login 025445/Jinjikoi5
        login("025445", "Jinjikoi5");
        //change closure 1
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(inpMonth)).click();
        driver.findElement(By.id(inpMonth)).clear();
        driver.findElement(By.id(inpMonth)).sendKeys("2019/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id(btnsave)).click();
       
        
        //case 3-1
        driver.get(domain+ "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.className("ntsCheckBox-label")).click();
        inputdate.add(Calendar.DATE, 1);
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[contains(@class,'nts-switch-button')]"));
        driver.findElements(By.xpath("//button[contains(@class,'nts-switch-button')]")).get(2).click();
        //WaitElementLoad(By.xpath("//button[contains(@class,'nts-switch-button')]"));
        WaitElementLoad(By.xpath("//*[@id='functions-area']/button[1]"));
        driver.findElement(By.xpath("//*[@id='functions-area']/button[1]")).click();
        WaitPageLoad();
        screenShotFull();

        //case3-2
        driver.get(domain+ "nts.uk.at.web/view/kaf/006/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.className("ntsCheckBox-label")).click();
        inputdate.add(Calendar.DATE, 8);
        driver.findElement(By.id("inputdate")).sendKeys(df1.format(inputdate.getTime()));
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[contains(@class,'nts-switch-button')]"));
        driver.findElements(By.xpath("//button[contains(@class,'nts-switch-button')]")).get(2).click();
        WaitElementLoad(By.xpath("//*[@id='functions-area']/button[1]"));
        driver.findElement(By.xpath("//*[@id='functions-area']/button[1]")).click();
        WaitPageLoad();
        screenShotFull();

       // comeback closure 
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(inpMonth)).click();
        driver.findElement(By.id(inpMonth)).clear();
        driver.findElement(By.id(inpMonth)).sendKeys("2021/12");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id(btnsave)).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("/html/body/div[7]/div[3]/div/button"));
        driver.findElement(By.xpath("/html/body/div[7]/div[3]/div/button")).click();
        logout();
     
        //login 018937/Jinjikoi5
        login("018937", "Jinjikoi5");
        driver.get(domain+ "nts.uk.com.web/view/cmm/044/a/index.xhtml");
        WaitPageLoad();
        Calendar inputdate2 = Calendar.getInstance();
        inputdate2.add(Calendar.DATE, 1);
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
        driver.findElements(By.xpath("//td[contains(.,'016976')]")).get(2).click();//2 or3
        driver.findElement(By.xpath("//*[@id='functions-area-bottom']/button[1]")).click();
        driver.findElement(By.xpath("//*[@id='functions-area']/button[2]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath(closeMsg15)).click();

    this.uploadTestLink(742, 166);
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