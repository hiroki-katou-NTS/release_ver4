package cmm044;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case3 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cmm044/Scenario1Case3";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //代行者の登録
        login("006310", "Jinjikoi5");
        WaitPageLoad();

        //KDW004A 日別実績の確認
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        //承認対象者が存在しません。
        // if("Msg_916".equals(driver.findElement(By.xpath("//div[@class ='control pre']")).getText())){
        //     driver.findElement(By.xpath("//button[@class='large']")).click();
        //     WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        //     startTime.clear();
        //     startTime.sendKeys("2019/12/01");  
        //     driver.findElement(By.xpath("//body")).click();
        //     //startTime.wait(700);
           
        //     WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        //     endTime.clear();
        //     endTime.sendKeys("2019/12/31"); 
        //     driver.findElement(By.xpath("//body")).click();
        //     //endTime.wait(700);
        //     driver.findElement(By.xpath("//button[@id='extractBtn']")).click();
        //     WaitPageLoad();
        // } else{
        //     screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //     FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        //     driver.findElement(By.xpath("//tr[@data-id='017267']/td[4]/a")).click();
        //     WaitPageLoad(); 
        // }
        screenShot();
        driver.findElement(By.xpath("//tr[@data-id='017267']/td[4]/a")).click();
        WaitPageLoad();   
        //KDW003A 勤務報告書
        
        WebElement el2 = driver.findElement(By.xpath("//td[contains(.,'12/02')]"));
        if ("true".equals(el2.findElements(By.xpath("following::td")).get(1).findElement(By.xpath("label/input")).getAttribute("checked")) ) {
            el2.findElements(By.xpath("following::td")).get(1).click();
        } 
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        screenShot();          
        //this.uploadTestLink(427, 96);
     
    }

    @AfterEach
    public void tearDown() throws Exception {
        //driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}