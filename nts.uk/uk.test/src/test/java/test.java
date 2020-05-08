import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.interactions.Actions;

import common.TestRoot;

public class test extends TestRoot {

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
        // login("004515", "Jinjikoi5");

        //KDW003A
        // driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        // WaitPageLoad();
        // driver.findElement(By.xpath("//div[@id='cbDisplayFormat']")).click();
        // WaitElementLoad(By.xpath("//li[@data-value='1']"));
        // driver.findElement(By.xpath("//li[@data-value='1']")).click();
        // Thread.sleep(1000);
        // WaitPageLoad();
        // driver.findElement(By.xpath("//button[@id='btnExtraction']")).click();
        // WaitPageLoad();
        // selectItemKdw004("5", "006310").click();

        driver.quit();
        
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