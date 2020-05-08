package cps001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Scenario2Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cps001/Scenario2Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        // File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //login申請者
        // login("020905", "Jinjikoi5", "image1", screenshotFile);
        login("020905", "Jinjikoi5");

        //old data
        driver.get(domain+"nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-6"));
        driver.findElement(By.id("ui-id-6")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030675");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-3"));
        driver.findElement(By.id("ui-id-3")).click();
        WaitPageLoad();
        //雇用
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00014']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00014']")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        screenShot();
        //勤務種別 
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        screenShot();
        //職位 
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00016']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00016']")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        screenShot();
         //労働条件 
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00020']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00020']")).click();
        WaitPageLoad();
        js.executeScript("$('#cps007_srt_control').scrollTop(580)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image5.png"));
        screenShot();
        //ksu001
        driver.get(domain+"nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-5"));
        driver.findElement(By.id("ui-id-5")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030675");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image6.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(700)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image7.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(0)");
        driver.findElements(By.xpath("//button[contains(.,'時刻')]")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image8.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(700)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image9.png"));
        screenShot();


        //change new data
        driver.get(domain+"nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-6"));
        driver.findElement(By.id("ui-id-6")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030675");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-3"));
        driver.findElement(By.id("ui-id-3")).click();
        WaitPageLoad();
        //雇用
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00014']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00014']")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'複製')]")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00014IS00066']/.//input")).clear();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00014IS00066']/.//input")).sendKeys("2019/11/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00014IS00068']")).click();
        driver.findElement(By.xpath("//li[@data-value='02']")).click();
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(.,'登録')]")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        Thread.sleep(1000);
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image10.png"));
        screenShot();
        //勤務種別 
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'複製')]")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00021IS00255']/.//input")).clear();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00021IS00255']/.//input")).sendKeys("2019/11/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00021IS00257']")).click();
        driver.findElement(By.xpath("//li[@data-value='0000000110']")).click();
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(.,'登録')]")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        Thread.sleep(1000);
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image11.png"));
        screenShot();
        //職位 
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00016']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00016']")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'複製')]")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00016IS00077']/.//input")).clear();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00016IS00077']/.//input")).sendKeys("2019/11/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00016IS00079']")).click();
        driver.findElement(By.xpath("//li[@data-value='f2e5a397-9bbe-4aea-a6c6-e788fdde3c7d']")).click();//00110　Iﾜｰｸ(900-1730) 
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(.,'登録')]")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        Thread.sleep(1000);
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image12.png"));
        screenShot();
         //労働条件 
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00020']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00020']")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'複製')]")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00020IS00119']/.//input")).clear();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00020IS00119']/.//input")).sendKeys("2019/11/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00020IS00252']")).click();
        driver.findElement(By.xpath("//li[@data-value='0']")).click();//通常勤務 
        
        WaitElementLoad(By.xpath("//div[@id='COM1000000000000000CS00020IS00127']"));
        driver.findElement(By.xpath("//div[@id='COM1000000000000000CS00020IS00127']")).click();
        driver.findElement(By.xpath("//li[@data-value='002']")).click(); //002　夏固定：試験有(技術) 

        WaitElementLoad(By.xpath("//button[@id ='COM1000000000000000CS00020IS00131']"));
        driver.findElement(By.xpath("//button[@id ='COM1000000000000000CS00020IS00131']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        js.executeScript("$('#day-list-tbl_displayContainer').scrollTop($('#day-list-tbl')[0].scrollHeight/2)");
        driver.findElements(By.xpath("//*[contains(@data-id,'100')]")).get(1).click();
        driver.findElements(By.xpath("//button[contains(.,'決定')]")).get(0).click();

        driver.findElement(By.xpath("//button[@id ='COM1000000000000000CS00020IS00140']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_2");
        js.executeScript("$('#day-list-tbl_displayContainer').scrollTop($('#day-list-tbl')[0].scrollHeight/2)");
        driver.findElements(By.xpath("//*[contains(@data-id,'100')]")).get(0).click();
        driver.findElements(By.xpath("//button[contains(.,'決定')]")).get(0).click();
        
        js.executeScript("$('#cps007_srt_control').scrollTop(580)");
        driver.findElements(By.xpath("//button[contains(.,'登録')]")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        Thread.sleep(1000);
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image13.png"));
        screenShot();



        //create schedule KSC001
        driver.get(domain+"nts.uk.at.web/view/ksc/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-4"));
        driver.findElement(By.id("ui-id-4")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030675");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[@tabindex='6']"));
        driver.findElement(By.xpath("//button[@tabindex='6']")).click();
        WaitElementLoad(By.xpath("//button[contains(.,'再作成')]"));
        driver.findElements(By.xpath("//button[contains(.,'再作成')]")).get(0).click();
        WaitElementLoad(By.xpath("//button[@tabindex='17']"));
        driver.findElement(By.xpath("//button[@tabindex='17']")).click();
        WaitElementLoad(By.xpath("//button[@tabindex='5']"));
        driver.findElement(By.xpath("//button[@tabindex='5']")).click();
        WaitElementLoad(By.id("buttonFinishPageE"));
        driver.findElement(By.id("buttonFinishPageE")).click();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image14.png"));
        screenShot();
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(@class,'danger')]")).get(0).click();
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(@class,'danger')]")).get(0).click();
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(@class,'danger')]")).get(0).click();
        Thread.sleep(1000);
        WebDriverWait _wait300 = new WebDriverWait(driver, 300);
        driver.switchTo().frame("window_1");
        _wait300.until(d -> "完了"
                .equals(d.findElements(By.xpath("//td[@class='valueScheduleSetting']/span")).get(0).getText()));
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image15.png"));
        screenShot();
        WaitPageLoad();


        //screen after create schedule
        driver.get(domain+"nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-5"));
        driver.findElement(By.id("ui-id-5")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030675");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image16.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(700)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image17.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(0)");
        driver.findElements(By.xpath("//button[contains(.,'時刻')]")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image18.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(700)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image19.png"));
        screenShot();


        //data before create data
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-4"));
        driver.findElement(By.id("ui-id-4")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030675");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).get(1).click();
        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image20.png"));
        screenShot();
        
        //create data KDW001
        driver.get(domain+"nts.uk.at.web/view/kdw/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-4"));
        driver.findElement(By.id("ui-id-4")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030675");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.id("button22")).click();
        WaitElementLoad(By.xpath("//button[contains(.,'再作成')]"));
        driver.findElements(By.xpath("//button[contains(.,'再作成')]")).get(0).click();
        WaitElementLoad(By.xpath("//span[contains(.,'もう一度作り直す')]"));
        driver.findElements(By.xpath("//span[contains(.,'もう一度作り直す')]")).get(0).click();
        WaitElementLoad(By.id("button6"));
        driver.findElement(By.id("button6")).click();
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(@class,'danger')]")).get(0).click();
        WaitElementLoad(By.id("button113"));
        driver.findElement(By.id("button113")).click();
        Thread.sleep(1000);
        driver.switchTo().frame("window_1");
        _wait300 = new WebDriverWait(driver, 300);
        _wait300.until(d -> (false == d.findElement(By.className("danger")).isEnabled()));
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image21.png"));
        screenShot();


        //data after create data
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-4"));
        driver.findElement(By.id("ui-id-4")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030675");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).get(1).click();
        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image22.png"));
        screenShot();
        this.uploadTestLink(304, 63);
     
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