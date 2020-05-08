package kif001;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import common.TestRoot;

public class Scenario1Case4 extends TestRoot {

    public Robot robot;

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kif001/Scenario1Case4";
        robot = new Robot();
        this.init();   
    }

    @Test
    public void test() throws Exception {
        //login
        login("001369", "Jinjikoi5");

        //KDW003A
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).sendKeys("2019/11/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).sendKeys("2019/11/30");
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        js.executeScript("$($('.mgrid-free')[1]).scrollLeft($(\"td:contains('退門時刻1')\").offset().left - $($('.mgrid-free')[1]).offset().left - $($('.mgrid-free')[1]).width() + 200)");
        screenShot();
        
        driver.quit();
        Thread.sleep(1000);
        String command = "D:/KinErp/kinjirou/bin/KMTX03.exe"; 
        Runtime run = Runtime.getRuntime(); 
        run.exec(command);
        Thread.sleep(10000);
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.mouseMove(1135, 520);
        leftClick();
        robot.mouseMove(1135, 630);
        leftClick();
        leftClick();
        leftClick();
        leftClick();
        leftClick();
        robot.mouseMove(1000, 630);
        leftClick();
        robot.mouseMove(780, 450);
        leftClick();
        Thread.sleep(1000);
        robot.mouseMove(967, 570);
        leftClick();
        Thread.sleep(25000);
        screenShot();
        robot.mouseMove(1010, 570);
        leftClick();
        robot.mouseMove(870, 450);
        leftClick();

        //driver recreate
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;
        _wait = new WebDriverWait(driver, 30);

        //login
        login("001369", "Jinjikoi5");

        //KBT002F
        driver.get(domain + "nts.uk.at.web/view/kbt/002/f/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@data-value=14 and contains(.,'即時実行')]")).click();
        WaitPageLoad();
        Thread.sleep(120000);
        screenShot();
        driver.findElement(By.xpath("//button[@data-value=14 and contains(.,'終了')]")).click();
        WaitPageLoad();

        //KDW003A
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(3).sendKeys("2019/11/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(3).sendKeys("2019/11/30");
        driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        WaitPageLoad();
        js.executeScript("$($('.mgrid-free')[1]).scrollLeft($(\"td:contains('退門時刻1')\").offset().left - $($('.mgrid-free')[1]).offset().left - $($('.mgrid-free')[1]).width() + 200)");
        screenShot();

        //KBT002F
        driver.get(domain + "nts.uk.at.web/view/kbt/002/f/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//button[@data-value=14 and contains(.,'詳細')]")).click();
        WaitPageLoad();
        screenShot();
        driver.switchTo().frame("window_1");
        WebElement el = driver.findElements(By.xpath("//button[contains(.,'業務エラー内容')]")).stream()
            .filter(el1 -> el1.isEnabled()).findFirst().orElse(null);
        if (null != el) {
            el.click();
        }
        WaitPageLoad();
        screenShot();

        this.uploadTestLink(784, 176);
        
    }

    public void leftClick() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(200);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(200);
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