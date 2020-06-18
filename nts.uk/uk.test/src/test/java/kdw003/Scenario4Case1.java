package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario4Case1 extends TestRoot {

    String inpMonth = "inpMonth";//id
    String btnsave = "btn_save";//id

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario4Case1";
        this.init();
    }

    @Test
    public void test() throws Exception {
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.MONTH, 0);
        
        //ログイン
    	login("000001", "0");

        //処理年月の変更
        new Kdw003Common().setProcessYearMonth(1, "2020/04");
        
//        //KDW006C 勤怠項目前準備 - 機能制限
//        driver.get(domain + "nts.uk.at.web/view/kdw/006/c/index.xhtml");
//        WaitPageLoad();
//        
//        //月の本人確認を利用するになっているか確認
//        WebElement a = driver.findElement(By.xpath("//*[@id='checkBox121']"));
//        WaitElementLoad(By.xpath("//*[@id='checkBox121']/label/span[1]"));
//        if (a.getAttribute("class").indexOf("checked") == -1) {
//            driver.findElement(By.xpath("//*[@id='checkBox121']/label/span[1]")).click();
//            driver.findElement(By.id("register-button")).click();
//        } else {
//            driver.findElement(By.id("register-button")).click();
//        }
//        driver.findElement(By.xpath("//body")).click();
//
//        screenShot();
//
//        //KDW006D 勤怠項目前準備 - 権限別機能制限
//        driver.get(domain + "nts.uk.at.web/view/kdw/006/d/index.xhtml");
//        WaitPageLoad();
//        
//        //営業
//        WaitElementLoad(By.xpath("//*[@id='single-list']/tbody/tr[4]"));
//        driver.findElement(By.xpath("//*[@id='single-list']/tbody/tr[4]")).click();
//        
//        WaitElementLoad(By.xpath("//*[@id='grid2']/tbody/tr[8]/td[3]/div/div/label"));
//        WebElement b = driver.findElement(By.xpath("//*[@id='grid2']/tbody/tr[8]/td[3]/div/div/label"));
//        //本人締め処理の使用可否
//        if (!b.findElement(By.xpath("./input")).isSelected()) {
//            b.click();
//            driver.findElement(By.id("register-button")).click();
//        } else {
//            driver.findElement(By.id("register-button")).click();
//        }
//        driver.findElement(By.xpath("//body")).click();
//
//        screenShot();
        
        //KDW003A 勤務報告書
        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        
        //エラー参照ダイアログが起動しているか
        if (driver.findElements(By.xpath("//iframe[@name='window_1']")).size() !=0) {
            driver.switchTo().frame("window_1");
            WaitPageLoad();
            
            WaitElementLoad(By.id("dialogClose"));
            driver.findElement(By.id("dialogClose")).click();
        }
        
        //翌月表示を当月に戻す
        WaitElementLoad(By.xpath("/html/body/div[1]/div[2]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div/div[1]/button"));
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[2]/div[1]/div[1]/div[1]/div[1]/div/div[1]/button")).click();
        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        
        WaitElementLoad(By.id("ccg001-btn-search-drawer"));
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        
        //入力検索
        WaitElementLoad(By.xpath("//*[@id='tab-panel']/ul/li[3]"));
        driver.findElement(By.xpath("//*[@id='tab-panel']/ul/li[3]")).click();
        
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).sendKeys("001509");
        
        WaitElementLoad(By.xpath("//*[@id='ccg001-part-g']/div[1]/button"));
        driver.findElement(By.xpath("//*[@id='ccg001-part-g']/div[1]/button")).click();
        
        WaitPageLoad();
        WaitElementLoad(By.id("ccg001-btn-KCP005-apply"));
        driver.findElement(By.id("ccg001-btn-KCP005-apply")).click();
        WaitPageLoad();
        
        screenShot();

        this.uploadTestLink(825, 191);
        
        //処理年月の変更
        new Kdw003Common().setProcessYearMonth(1, df3.format(inputdate.getTime()));
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