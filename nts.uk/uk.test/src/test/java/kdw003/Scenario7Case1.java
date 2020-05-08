package kdw003;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

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
        
        this.init();
    }

    @Test
    public void test() throws Exception  {

        options = new ChromeOptions();
        options.addArguments("--incognito");

        driver2 = new ChromeDriver(options);
        driver2.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver2.manage().window().maximize();
        js2 = (JavascriptExecutor) driver2;
        _wait = new WebDriverWait(driver2, 30);

         //login 016209/Jinjikoi5
        login("016209", "Jinjikoi5");
        login2("003944", "Jinjikoi5");

        // change closure 1
        driver.get(domain+ "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(inpMonth)).click();
        driver.findElement(By.id(inpMonth)).clear();
        driver.findElement(By.id(inpMonth)).sendKeys("2019/7");
        driver.findElement(By.xpath("//body")).click();
        driver.findElement(By.id(btnsave)).click();

       
        // click checkbox KDW006C
        driver.get(domain + "nts.uk.at.web/view/kdw/006/c/index.xhtml");
        WaitPageLoad();
        WebElement a = driver.findElement(By.xpath("//*[@id='checkBox161']"));
        WaitElementLoad(By.xpath("//*[@id='checkBox161']/label/span[1]"));
        if (a.getAttribute("class").indexOf("checked") == -1) {
            driver.findElement(By.id("register-button")).click();
        } else {
            driver.findElement(By.xpath("//*[@id='checkBox161']/label/span[1]")).click();
            driver.findElement(By.id("register-button")).click();
        }
        driver.findElement(By.xpath("//body")).click();

       // go kdw003/ tháng 8 uncheck
        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        selectItemKdw003_2("本人", "08/01(木)").click();

        driver2.get(domain+"nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        WaitElementLoad(By.xpath("//a[contains(.,'016209')]"));
        driver2.findElement(By.xpath("//a[contains(.,'016209')]")).click();
        WaitPageLoad();
        // selectItemKdw003_2_2("承認","08/01(木)").click();// không click được 
        WaitElementLoad(By.xpath("//*[@id='dpGrid']/div[3]/table/tbody/tr[2]/td[6]/label"));
        driver2.findElement(By.xpath("//*[@id='dpGrid']/div[3]/table/tbody/tr[2]/td[6]/label")).click();

        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        WaitPageLoad();

        WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
        driver2.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
        WaitPageLoad();
        screenShotFull();
    //   // go kdw003/ tháng 8 check
    //   driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
    //   WaitPageLoad();
    //   selectItemKdw003_2("本人", "08/01(木)").click();
    //   WaitElementLoad(By.xpath("//button[contains(.,'確定')]"));
    //   driver.findElements(By.xpath("//button[contains(.,'確定')]")).get(0).click();
    //   WaitPageLoad();
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