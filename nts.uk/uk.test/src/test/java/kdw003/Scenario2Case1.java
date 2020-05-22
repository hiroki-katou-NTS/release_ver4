package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
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
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.MONTH, 0);
        
        //ログイン（フレックス）
        login("004444", "Jinjikoi5");
        
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
        
        //退勤時刻の変更
        setValueGrid(2,8,"1900");
        //休憩終了時刻の変更
        setValueGrid(2,16,"1835");
        
        screenShot();
        
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        
        Thread.sleep(10000);

        WaitElementLoad(By.xpath("//button[@class ='large']"));
        driver.findElement(By.xpath("//button[@class ='large']")).click();
        
        //エラー参照ダイアログが起動する
        WaitPageLoad();
        driver.switchTo().frame("window_1");
        
        screenShot();
        
        WaitElementLoad(By.id("dialogClose"));
        driver.findElement(By.id("dialogClose")).click();
        WaitPageLoad();
        
        //シート切り替え
        driver.findElements(By.xpath("//li[contains(.,'乖離シート')]")).get(0).click();
        
        
        //乖離理由
        Thread.sleep(1000);
        WaitElementLoad(By.xpath("//*[@id=\"dpGrid\"]/div[4]/table/tbody/tr[2]/td[20]/a"));
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[4]/table/tbody/tr[2]/td[20]/a")).click();
        
        WaitPageLoad();
        driver.switchTo().frame("window_2");
        driver.findElements(By.xpath("//td[contains(.,'自己研鑽･自己学習')]")).get(1).click();
        driver.findElement(By.id("save")).click();
        
        //本人確認
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[2]/td[5]/label/span")).click();
        
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;

        WaitPageLoad();
        
        screenShot();
        
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

        //退勤時刻の変更
        setValueGrid(3,8,"1900");
        
        //本人確認
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[3]/td[5]/label/span")).click();
        
        screenShot();
        
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        
        WaitPageLoad();
        
        WaitElementLoad(By.xpath("//button[@class ='large']"));
        driver.findElement(By.xpath("//button[@class ='large']")).click();
        
        WaitPageLoad();
        
        screenShot();
        
        this.uploadTestLink(719, 158);
        
        //処理年月の変更
        new Kdw003Common().setProcessYearMonth(1, df3.format(inputdate.getTime()));
     
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