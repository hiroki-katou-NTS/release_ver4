package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario2Case2 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario2Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.MONTH, 0);
        //ログイン（フレックス）
        login("015243", "Jinjikoi5");

        //処理年月の変更
        new Kdw003Common().setProcessYearMonth(1, "2020/04");
        
        //KDW003A 勤務報告書
        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        
        //エラー参照ダイアログが起動していれば閉じる
        if (driver.findElements(By.xpath("//iframe[@name='window_1']")).size() !=0) {
            driver.switchTo().frame("window_1");
            
            WaitElementLoad(By.id("dialogClose"));
            driver.findElement(By.id("dialogClose")).click();
        }
        
        //翌月表示を当月に戻す
        driver.findElements(By.xpath("//button[contains(@class,'ntsDatePrevButton')]")).get(1).click();
        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        
        setValueGrid(19,8,"1500");
        WaitPageLoad();
        setValueGrid(19,13,"1500");
        WaitPageLoad();
        setValueGrid(19,14,"1530");
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
        setValueGrid(19,8,"1530");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        WaitPageLoad();
        screenShot();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        Thread.sleep(1000);
        setValueGrid(19,8,"1500");
        WaitPageLoad();
        driver.findElements(By.xpath("//span[contains(@class,'box')]")).get(36).click();
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;
        WaitPageLoad();
        screenShot();
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        Thread.sleep(1000); 
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        Thread.sleep(1000); 
        driver.findElements(By.xpath("//li[contains(.,'乖離シート')]")).get(0).click();
        WaitElementLoad(By.xpath("//span[contains(@class,'box')]"));
        driver.findElements(By.xpath("//span[contains(@class,'box')]")).get(36).click();
        Thread.sleep(1000);
        selectItemKdw003_2("PCログオフ乖離時間理由（選択）", "12/18(水)").click();
        // WaitElementLoad(By.xpath("//a[contains(@class,'mlink-button')]"));
        // driver.findElements(By.xpath("//a[contains(@class,'mlink-button')]")).get(69).click();
        WaitPageLoad();
        driver.switchTo().frame("window_2");
        driver.findElements(By.xpath("//td[contains(.,'自己研鑽・自己学習')]")).get(1).click();
        driver.findElement(By.id("save")).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(721, 159);
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