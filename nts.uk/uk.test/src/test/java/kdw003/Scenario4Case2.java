package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import org.openqa.selenium.*;
import common.TestRoot;

public class Scenario4Case2 extends TestRoot {

    String inpMonth = "inpMonth";//id
    String btnsave = "btn_save";//id

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario4Case2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.MONTH, 0);
        
        //ログイン（正社員）
        login("001509", "Jinjikoi5");

        //処理年月の変更
        new Kdw003Common().setProcessYearMonth(1, "2020/04");
        
        //KDW006C 勤怠項目前準備 - 機能制限
        driver.get(domain + "nts.uk.at.web/view/kdw/006/c/index.xhtml");
        WaitPageLoad();
        
        //月の本人確認を利用するになっているか確認
        WebElement a = driver.findElement(By.xpath("//*[@id='checkBox121']"));
        WaitElementLoad(By.xpath("//*[@id='checkBox121']/label/span[1]"));
        if (a.getAttribute("class").indexOf("checked") == -1) {
            driver.findElement(By.xpath("//*[@id='checkBox121']/label/span[1]")).click();
            driver.findElement(By.id("register-button")).click();
        } else {
            driver.findElement(By.id("register-button")).click();
        }
        driver.findElement(By.xpath("//body")).click();

        screenShot();

        //KDW006D 勤怠項目前準備 - 権限別機能制限
        driver.get(domain + "nts.uk.at.web/view/kdw/006/d/index.xhtml");
        WaitPageLoad();
        
        //営業
        WaitElementLoad(By.xpath("//*[@id='single-list']/tbody/tr[4]"));
        driver.findElement(By.xpath("//*[@id='single-list']/tbody/tr[4]")).click();
        
        WaitElementLoad(By.xpath("//*[@id='grid2']/tbody/tr[8]/td[3]/div/div/label"));
        WebElement b = driver.findElement(By.xpath("//*[@id='grid2']/tbody/tr[8]/td[3]/div/div/label"));
        //本人締め処理の使用可否
        if (!b.findElement(By.xpath("./input")).isSelected()) {
            b.click();
            driver.findElement(By.id("register-button")).click();
        } else {
            driver.findElement(By.id("register-button")).click();
        }
        driver.findElement(By.xpath("//body")).click();

        screenShot();

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
        
        //本人確認
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[2]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[3]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[4]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[5]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[6]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[7]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[8]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[9]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[10]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[11]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[12]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[13]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[14]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[15]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[16]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[17]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[18]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[19]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[20]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[21]/td[5]/label/span")).click();
        
        //グリッドをスクロール
        js.executeScript("$('.mgrid-free').scrollTop(1000)");
        
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[22]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[23]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[24]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[25]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[26]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[27]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[28]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[29]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[30]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[31]/td[5]/label/span")).click();
        
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        WaitPageLoad();
        
        WaitElementLoad(By.xpath("//button[contains(.,'閉じる')]"));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitPageLoad();
        
        screenShot();

        this.uploadTestLink(827, 192);
        
        //本人確認を戻す
        //グリッドをスクロール
        js.executeScript("$('.mgrid-free').scrollTop(-1000)");
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[2]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[3]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[4]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[5]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[6]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[7]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[8]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[9]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[10]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[11]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[12]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[13]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[14]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[15]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[16]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[17]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[18]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[19]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[20]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[21]/td[5]/label/span")).click();
        
        //グリッドをスクロール
        js.executeScript("$('.mgrid-free').scrollTop(1000)");
        
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[22]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[23]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[24]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[25]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[26]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[27]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[28]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[29]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[30]/td[5]/label/span")).click();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[31]/td[5]/label/span")).click();
        
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        WaitPageLoad();
        
        WaitElementLoad(By.xpath("//button[contains(.,'閉じる')]"));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitPageLoad();
        
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