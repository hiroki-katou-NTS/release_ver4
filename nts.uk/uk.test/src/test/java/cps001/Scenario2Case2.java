package cps001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Scenario2Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/cps001/Scenario2Case2";
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
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030676");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-3"));
        driver.findElement(By.id("ui-id-3")).click();
        WaitPageLoad();
        //職場 
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00017']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00017']")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));
        screenShot();
        //分類 
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00004']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00004']")).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        screenShot();
        //ksu001
        driver.get(domain+"nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-5"));
        driver.findElement(By.id("ui-id-5")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030676");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(700)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image5.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(0)");
        driver.findElements(By.xpath("//button[contains(.,'時刻')]")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image6.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(700)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image7.png"));
        screenShot();



        // change new data
        driver.get(domain+"nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-6"));
        driver.findElement(By.id("ui-id-6")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030676");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(1).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-3"));
        driver.findElement(By.id("ui-id-3")).click();
        WaitPageLoad();
        //職場 
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00017']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00017']")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'複製')]")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00017IS00082']/.//input")).clear();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00017IS00082']/.//input")).sendKeys("2019/11/11");
        driver.findElement(By.xpath("//body")).click();
        WaitElementLoad(By.xpath("//button[@id ='COM1000000000000000CS00017IS00084']"));
        driver.findElement(By.xpath("//button[@id ='COM1000000000000000CS00017IS00084']")).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        WaitElementLoad(By.xpath("//input[contains(@class,'ntsSearchBox_Component')]"));
        driver.findElements(By.xpath("//input[contains(@class,'ntsSearchBox_Component')]")).get(0).clear();
        driver.findElements(By.xpath("//input[contains(@class,'ntsSearchBox_Component')]")).get(0).sendKeys("001075");

        WaitElementLoad(By.xpath("//button[contains(@class,'search-btn')]"));
        driver.findElements(By.xpath("//button[contains(@class,'search-btn')]")).get(0).click();
        driver.findElements(By.xpath("//button[contains(.,'決定')]")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'登録')]")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image8.png"));
        screenShot();
        //分類 
        WaitPageLoad();
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00004']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00004']")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'複製')]")).get(0).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00004IS00026']/.//input")).clear();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00004IS00026']/.//input")).sendKeys("2019/11/11");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.xpath("//div[@id ='COM1000000000000000CS00004IS00028']")).click();
        driver.findElement(By.xpath("//li[@data-value='0000001316']")).click();
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(.,'登録')]")).get(0).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image9.png"));
        screenShot();


        //create schedule KSC001
        driver.get(domain+"nts.uk.at.web/view/ksc/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-4"));
        driver.findElement(By.id("ui-id-4")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030676");
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
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image10.png"));
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
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image11.png"));
        screenShot();
        WaitPageLoad();


        //screen after create schedule
        driver.get(domain+"nts.uk.at.web/view/ksu/001/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.id("ui-id-5"));
        driver.findElement(By.id("ui-id-5")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030676");
        driver.findElements(By.xpath("//button[contains(@class,'pull-right')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElements(By.id("ccg001-btn-KCP005-apply")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image12.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(700)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image13.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(0)");
        driver.findElements(By.xpath("//button[contains(.,'時刻')]")).get(0).click();
        WaitPageLoad();
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image14.png"));
        screenShot();
        js.executeScript("$('.ex-body-detail').scrollLeft(700)");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image15.png"));
        screenShot();


        //data before create data
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-4"));
        driver.findElement(By.id("ui-id-4")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030676");
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
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image16.png"));
        screenShot();
        

        //create data KDW001
        driver.get(domain+"nts.uk.at.web/view/kdw/001/b/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-4"));
        driver.findElement(By.id("ui-id-4")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030676");
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
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image17.png"));
        screenShot();


        //data after create data
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.id("ui-id-4"));
        driver.findElement(By.id("ui-id-4")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        driver.findElement(By.id("ccg001-input-code")).sendKeys("030676");
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
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image18.png"));
        screenShot();
        this.uploadTestLink(306, 64);
     
    }

    // public void login(String id, String pass, String nameImage, File screenshotFile) throws Exception {
    //     driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
    //     WaitPageLoad();
    //     driver.findElement(By.id("company-code-select")).click();
    //     WaitElementLoad(By.xpath("//li[@data-value='0001']"));
    //     driver.findElement(By.xpath("//li[@data-value='0001']")).click();
    //     driver.findElement(By.id("employee-code-inp")).clear();
    //     driver.findElement(By.id("employee-code-inp")).sendKeys(id);
    //     driver.findElement(By.id("password-input")).clear();
    //     driver.findElement(By.id("password-input")).sendKeys(pass);
    //     screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    //     FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/" + nameImage + ".png"));
    //     driver.findElement(By.id("login-btn")).click();
    //     WaitPageLoad();
    // }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}