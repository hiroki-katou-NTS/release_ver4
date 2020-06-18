package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import common.TestRoot;

public class Scenario7Case1 extends TestRoot {

    public static JavascriptExecutor js2;
    public static ChromeOptions options;
    public static WebDriver driver2;

    String inpMonth = "inpMonth";// id
    String btnsave = "btn_save";// id


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw003/Scenario7Case1";
        options = new ChromeOptions();
        options.addArguments("--incognito");

        driver2 = new ChromeDriver(options);
        driver2.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver2.manage().window().maximize();
        js2 = (JavascriptExecutor) driver2;
        _wait = new WebDriverWait(driver2, 30);
        
        this.init();
    }

    @Test
    public void test() throws Exception  {
        Calendar inputdate = Calendar.getInstance();
        inputdate.add(Calendar.MONTH, 0);
        
        //ログイン1（部下）
        login("004098", "Jinjikoi5");        
        //ログイン1（上司）
        login2("004122", "Jinjikoi5");

        //処理年月の変更
        new Kdw003Common().setProcessYearMonth(1, "2020/04");

       
        //KDW006C 勤怠項目前準備 - 機能制限
        driver.get(domain + "nts.uk.at.web/view/kdw/006/c/index.xhtml");
        WaitPageLoad();
        
        //本人確認、上司承認を利用するになっているか確認
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
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(.,'閉じる')]"));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitPageLoad();
        
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
        
        //KDW004A 日別実績の確認
        driver2.get(domain+"nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        
        //翌月表示を当月に戻す
        driver2.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div[3]/div/div[1]")).click();
        WaitElementLoad(By.id("extractBtn"));
        driver2.findElement(By.id("extractBtn")).click();
        WaitPageLoad();

        
        //部下を選択
        driver2.findElements(By.xpath("//a[contains(.,'004098')]")).get(0).click();
        WaitPageLoad();
        
        //エラー参照ダイアログが起動しているか
        if (driver2.findElements(By.xpath("//iframe[@name='window_1']")).size() !=0) {
            driver2.switchTo().frame("window_1");
            WaitPageLoad();
            
            WaitElementLoad(By.id("dialogClose"));
            driver2.findElement(By.id("dialogClose")).click();
        }
        WaitPageLoad();
        
        //①本人確認を外して登録
        driver.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[2]/td[5]/label/span")).click();
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(.,'閉じる')]"));
        driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitPageLoad();
        
        //②上司承認をつけて登録
        driver2.findElement(By.xpath("//*[@id=\"dpGrid\"]/div[3]/table/tbody/tr[2]/td[6]/label/span")).click();
        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver2.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        WaitPageLoad();
        WaitElementLoad(By.xpath("//button[contains(.,'閉じる')]"));
        driver2.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        WaitPageLoad();
        
        screenShot();
        
        this.uploadTestLink(1226, 301);
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        driver2.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
    public void login2(String userId2, String password2) {
        driver2.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver2.findElement(By.id("company-code-select")).click();
        WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        driver2.findElement(By.xpath("//li[@data-value='0001']")).click();
        driver2.findElement(By.id("password-input")).clear();
        driver2.findElement(By.id("password-input")).sendKeys(password2);
        driver2.findElement(By.id("employee-code-inp")).clear();
        driver2.findElement(By.id("employee-code-inp")).sendKeys(userId2);
        this.screenShot();
        driver2.findElement(By.id("login-btn")).click();
        WaitPageLoad();
    }
    public WebElement selectItemKdw003_2_2(String itemName, String date) {
        int xPosition = driver2.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getX() +  driver2.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getWidth()/2 + 5;
        int yPosition = driver2.findElement(By.xpath("//td[.='" + date + "']")).getRect().getY() + 20;
        return (WebElement)js.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition, yPosition);
    }
}