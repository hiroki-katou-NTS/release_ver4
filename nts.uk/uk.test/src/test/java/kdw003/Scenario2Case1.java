package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario2Case1 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario2Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        //login申請者
        login("010927", "Jinjikoi5");

        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).get(1).click();
        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        setValueGrid(15,8,"1900");
        WaitPageLoad();
        setValueGrid(15,13,"1835");
        WaitPageLoad();
        screenShot();
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;
        WaitPageLoad();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();;
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(.,'エラー参照')]")).get(0).click();
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        screenShot();
        driver.findElement(By.id("dialogClose")).click();
        WaitPageLoad();
        driver.findElements(By.xpath("//li[contains(.,'乖離シート')]")).get(0).click();
        Thread.sleep(1000);
        WaitElementLoad(By.xpath("//span[contains(@class,'box')]"));
        driver.findElements(By.xpath("//span[contains(@class,'box')]")).get(28).click();
        Thread.sleep(1000);
        selectItemKdw003_2("休憩超過理由（選択）", "11/14(木)").click();
        // WaitElementLoad(By.xpath("//a[contains(@class,'mlink-button')]"));
        // driver.findElements(By.xpath("//a[contains(@class,'mlink-button')]")).get(54).click();
        WaitPageLoad();
        driver.switchTo().frame("window_2");
        driver.findElements(By.xpath("//td[contains(.,'自己研鑽･自己学習')]")).get(1).click();
        driver.findElement(By.id("save")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;
        WaitPageLoad();
        screenShot();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();;
        Thread.sleep(1000); 
        setValueGrid(16,2,"1900");
        WaitPageLoad();
        driver.findElements(By.xpath("//span[contains(@class,'box')]")).get(30).click();
        Thread.sleep(1000);
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(719, 158);
     
    }

    public void setValueGrid(int rowNumber, int columnNumber, String value){
        if(value.isEmpty()){
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]"+"/.//input")).get(0).clear();
        }else{
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).click();
            driver.findElements(By.xpath(".//*[@class=\"mgrid-free\"]/table/tbody/tr[" + rowNumber+ "]/td[" +columnNumber + "]")).get(0).sendKeys(value);
        }
        driver.findElement(By.xpath("//body")).click();
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