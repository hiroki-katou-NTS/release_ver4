package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;

import org.openqa.selenium.*;

import common.TestRoot;

public class Scenario2Case4 extends TestRoot {

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario2Case4";
        this.init();
    }

    @Test
    public void test() throws Exception {
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.MONTH, 0);
        
        //ログイン（上司）
        login("010578", "Jinjikoi5");
//        //処理年月の変更
//        new Kdw003Common().setProcessYearMonth(1, "2020/04");
        
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
        
        //社員範囲選択（部下を検索）
        driver.findElement(By.id("ccg001-btn-search-drawer")).click();
        WaitElementLoad(By.xpath("//a[contains(.,'入力検索')]"));
        driver.findElement(By.xpath("//a[contains(.,'入力検索')]")).click();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).clear();
        WaitElementLoad(By.id("ccg001-input-code"));
        driver.findElement(By.id("ccg001-input-code")).sendKeys("014666");
        WaitElementLoad(By.xpath("//button[contains(@class,'proceed caret-bottom pull-right')]"));
        driver.findElements(By.xpath("//button[contains(@class,'proceed caret-bottom pull-right')]")).get(0).click();
        WaitElementLoad(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[3]/div/div[2]/div[1]/div/div"));
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[3]/div/div[2]/div[1]/div/div")).click();

        
        WaitPageLoad();
        
        screenShot();

        
        //ログイン（部下）
        login("014666", "Jinjikoi5");
        
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
        
        //本人確認
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[2]/td[5]/label/span")).click();
        
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;

        WaitPageLoad();
        
        screenShot();


        //ログイン（上司）
        login("010578", "Jinjikoi5");
        
        //KDW004A 日別実績の確認
        driver.get(domain+"nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        
        //翌月表示を当月に戻す
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[3]/div/div[1]")).click();
        WaitElementLoad(By.id("extractBtn"));
        driver.findElement(By.id("extractBtn")).click();
        WaitPageLoad();

        
        //部下を選択
        driver.findElements(By.xpath("//a[contains(.,'014666')]")).get(0).click();
        WaitPageLoad();
        
        //承認
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[2]/td[6]/label/span")).click();
        
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();;
        
        WaitPageLoad();
      
        screenShot();

        this.uploadTestLink(725, 161);
    }

    public boolean checkedBox(int rowNumber, int columnNumber) {
        String element = driver
                .findElements(By.xpath("//table[@class ='mgrid-fixed-table']/tbody/tr[" + rowNumber
                        + "]/.//label[@class = 'ntsCheckBox']"))
                .get(columnNumber).findElement(By.tagName("input")).getAttribute("checked");
        if (element != null) {
            return true;
        }
        return false;
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