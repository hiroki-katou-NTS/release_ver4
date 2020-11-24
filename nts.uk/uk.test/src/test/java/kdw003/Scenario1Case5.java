package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import common.TestRoot;

/**
 * @author ThuHT
 */

public class Scenario1Case5 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario1Case5";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login
        login("000001", "0");
        
        //処理年月の変更
        new Kdw003Common().setProcessYearMonth(1, "2019/09");

        //CPS001A 個人情報の登録
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        
        WaitElementLoad(By.xpath("//a[contains(.,'入力検索')]"));
        driver.findElement(By.xpath("//a[contains(.,'入力検索')]")).click();
        
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).sendKeys("018234");
        
        WaitElementLoad(By.xpath("//button[contains(@class,'proceed caret-bottom pull-right')]"));
        driver.findElements(By.xpath("//button[contains(@class,'proceed caret-bottom pull-right')]")).get(0).click();

        WaitElementLoad(By.xpath("//*[@id='ccg001-tab-content-3']/div[2]/div[1]/div/div/i"));
        driver.findElement(By.xpath("//*[@id='ccg001-tab-content-3']/div[2]/div[1]/div/div/i")).click();
        
        WaitPageLoad();
        driver.findElement(By.id("ui-id-3")).click();
        
        WaitElementLoad(By.xpath("//div[@tabindex='9']"));
        driver.findElement(By.xpath("//div[@tabindex='9']")).click();
        
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00003']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00003']")).click();
        
        WaitElementLoad(By.id("COM1000000000000000CS00003IS00020"));
        driver.findElement(By.id("COM1000000000000000CS00003IS00020")).click();
        
        WaitElementLoad(By.xpath("//input[@tabindex ='0']"));
        driver.findElement(By.xpath("//input[@tabindex ='0']")).clear();
        
        WaitElementLoad(By.xpath("//input[@tabindex ='0']")); 
        driver.findElement(By.xpath("//input[@tabindex ='0']")).sendKeys("2019/10/16");
        
        driver.findElement(By.xpath("//body")).click();

        screenShot();

        WaitElementLoad(By.xpath("//button[@class='proceed']"));
        driver.findElement(By.xpath("//button[@class='proceed']")).click();
        
        WaitElementLoad(By.xpath("//button[@class ='large']"));
        driver.findElement(By.xpath("//button[@class ='large']")).click();
        
        WaitPageLoad();

         //KDW003A 勤務報告書
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        
        WaitPageLoad();
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        
        WaitElementLoad(By.xpath("//a[contains(.,'入力検索')]"));
        driver.findElement(By.xpath("//a[contains(.,'入力検索')]")).click();
        
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).sendKeys("018234");
        
        WaitElementLoad(By.xpath("//button[contains(@class,'proceed caret-bottom pull-right')]"));
        driver.findElements(By.xpath("//button[contains(@class,'proceed caret-bottom pull-right')]")).get(0).click();
        
        WaitElementLoad(By.xpath("//*[@id='ccg001-tab-content-3']/div[2]/div[1]/div/div/i"));
        driver.findElement(By.xpath("//*[@id='ccg001-tab-content-3']/div[2]/div[1]/div/div/i")).click();
        
        WaitPageLoad();

        //chon ngay
        WebElement startDate = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
        startDate.clear();
        startDate.sendKeys("2019/10/01");
        driver.findElement(By.xpath("//body")).click();
        Thread.sleep(1000);
        WebElement endDate = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
        endDate.clear();
        endDate.sendKeys("2019/10/31");
        driver.findElement(By.xpath("//body")).click();
        
        WaitElementLoad(By.xpath("//button[@id='btnExtraction']"));
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        
        WaitPageLoad();
        
        screenShot();

        this.uploadTestLink(592, 146);
     
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