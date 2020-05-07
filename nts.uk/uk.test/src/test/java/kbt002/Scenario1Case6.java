package kbt002;

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

public class Scenario1Case6 extends TestRoot {

    public Robot robot;

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kbt002/Scenario1Case6";
        robot = new Robot();
        this.init();   
    }

    @Test
    public void test() throws Exception {

        //login
        login("019335", "Jinjikoi5");

        //CPS001A
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'カテゴリ')]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@title='ドロップダウンの表示']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00014']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00014']")).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@title='ドロップダウンの表示']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']")).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@title='ドロップダウンの表示']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00020']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00020']")).click();
        WaitPageLoad();
        screenShot();
        
        driver.quit();
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
        robot.mouseMove(1000, 555);
        leftClick();
        robot.mouseMove(780, 450);
        leftClick();
        Thread.sleep(1000);
        robot.mouseMove(967, 570);
        leftClick();
        Thread.sleep(60000);
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
        login("019335", "Jinjikoi5");

        //CPS001A
        driver.get(domain + "nts.uk.com.web/view/cps/001/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath("//a[contains(.,'カテゴリ')]")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//div[@title='ドロップダウンの表示']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00014']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00014']")).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@title='ドロップダウンの表示']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00021']")).click();
        WaitPageLoad();
        screenShot();
        driver.findElement(By.xpath("//div[@title='ドロップダウンの表示']")).click();
        WaitElementLoad(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00020']"));
        driver.findElement(By.xpath("//li[@data-value='COM1_00000000000000000000000_CS00020']")).click();
        WaitPageLoad();
        screenShot();

        //KDW003
        driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(4).clear();
        driver.findElements(By.xpath("//input[contains(@id,'startInput')]")).get(4).sendKeys("2019/12/01");
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(4).clear();
        driver.findElements(By.xpath("//input[contains(@id,'endInput')]")).get(4).sendKeys("2019/12/31");
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        selectItemKdw003_1("出勤時刻1", "12/02(月)").click();
        Thread.sleep(1000);
        selectItemKdw003_1("出勤時刻1", "12/02(月)").click();
        selectItemKdw003_1("出勤時刻1", "12/02(月)").findElement(By.xpath("./div/input")).clear();
        selectItemKdw003_1("出勤時刻1", "12/02(月)").findElement(By.xpath("./div/input")).sendKeys("10:00");
        driver.findElement(By.xpath("//button[.='確定']")).click();
        WaitPageLoad();
        screenShot();
        this.uploadTestLink(1269, 308);
        
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