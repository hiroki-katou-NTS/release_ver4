package kdw004;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import common.TestRoot;
import kdw003.Kdw003Common;

public class Scenario4Case4 extends Kdw003Common {
    public static JavascriptExecutor js2;
    public static ChromeOptions options;
    public static WebDriver driver2;

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario4Case4";
        this.init();
    }

    @Test
    public void test() throws Exception {

        options = new ChromeOptions();
        options.addArguments("--incognito");

        driver2 = new ChromeDriver(options);
        driver2.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver2.manage().window().maximize();
        js2 = (JavascriptExecutor) driver2;
        _wait = new WebDriverWait(driver2, 30);

        // login承認者
        login("004515", "Jinjikoi5");

        setProcessYearMonth(1, "2020/05");

        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();
        selectItemKdw004("11", "007102").click();
        WaitPageLoad();

        // setting 004515
        WaitElementLoad((By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")));
        driver.findElement(By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")).click();

        driver.findElement(By.xpath("//li[contains(.,'日付別')]")).click();

        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        if (selectItemKdw003_3("承認", "007102").findElement(By.xpath("./label/input")).isSelected()) {
            selectItemKdw003_3("承認", "007102").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();
        if (driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).size() > 1) {
            driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        }

        // login申請者
        driver2.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver2.findElement(By.id("company-code-select")).click();
        driver2.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        driver2.findElement(By.id("employee-code-inp")).clear();
        driver2.findElement(By.id("employee-code-inp")).sendKeys("007102");
        File screenshotFile = ((TakesScreenshot) driver2).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image1.png"));
        driver2.findElement(By.id("login-btn")).click();
        WaitPageLoad();

//        // Setting screen kmk012
//        driver2.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
//        WaitPageLoad();
//
//        // Clear Input Month
//        driver2.findElement(By.id("inpMonth")).clear();
//
//        // Input into Month
//        driver2.findElement(By.id("inpMonth")).sendKeys("2020/05");
//        driver2.findElement(By.id("contents-right")).click();
//
//        // Click button Save
//        driver2.findElement(By.id("btn_save")).click();
//        WaitPageLoad();

        // Go to screen Kdw003a
        driver2.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

//        // login承認者
//        login("004515", "Jinjikoi5");
//
//        // Go to screen Kdw003a
//        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
//        WaitPageLoad();

//        selectItemKdw004("11", "007102").click();
//        WaitPageLoad();
//
//        // setting 004515
//        WaitElementLoad((By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")));
//        driver.findElement(By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")).click();
//
//        driver.findElement(By.xpath("//li[contains(.,'日付別')]")).click();
//
//        WaitElementLoad(By.id("btnExtraction"));
//        driver.findElement(By.id("btnExtraction")).click();
//        WaitPageLoad();
//
//        if (selectItemKdw003_3("承認", "007102").findElement(By.xpath("./label/input")).isSelected()) {
//            selectItemKdw003_3("承認", "007102").click();
//        }
//
//        WaitElementLoad(By.className("proceed"));
//        driver.findElement(By.className("proceed")).click();
//        WaitPageLoad();
//
//        if (driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).size() > 1) {
//            driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
//        }

        // setting 007102
        WaitElementLoad((By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")));
        driver2.findElement(By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")).click();

        driver2.findElement(By.xpath("//li[contains(.,'日付別')]")).click();

        WaitElementLoad(By.id("btnExtraction"));
        driver2.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        driver2.findElement(By.id("choosenDate")).clear();
        driver2.findElement(By.id("choosenDate")).sendKeys("2020/05/11");

        WaitElementLoad(By.id("btnExtraction"));
        driver2.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        if (!selectItemKdw003_31("本人", "007102").findElement(By.xpath("./label/input")).isSelected()) {
            selectItemKdw003_31("本人", "007102").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver2.findElement(By.className("proceed")).click();
        WaitPageLoad();

        screenshotFile = ((TakesScreenshot) driver2).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));

        if (driver2.findElements(By.xpath("//button[contains(.,'閉じる')]")).size() > 1) {
            driver2.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        }

        // setting 45
        if (!selectItemKdw003_3("承認", "007102").findElement(By.xpath("./label/input")).isSelected()) {
            selectItemKdw003_3("承認", "007102").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        if (driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).size() > 1) {
            driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        }

        // 7102
        WaitElementLoad(By.id("btnExtraction"));
        driver2.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        // setting 45
        if (selectItemKdw003_3("承認", "007102").findElement(By.xpath("./label/input")).isSelected()) {
            selectItemKdw003_3("承認", "007102").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        if (driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).size() > 1) {
            driver.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        }

        // 7102
        WaitElementLoad(By.id("btnExtraction"));
        driver2.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        // setting 45
        if (!selectItemKdw003_3("承認", "007102").findElement(By.xpath("./label/input")).isSelected()) {
            selectItemKdw003_3("承認", "007102").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        WaitPageLoad();

        screenshotFile = ((TakesScreenshot) driver2).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));


        // 7102
        if (driver2.findElements(By.xpath("//button[contains(.,'閉じる')]")).size() > 1) {
            driver2.findElements(By.xpath("//button[contains(.,'閉じる')]")).get(1).click();
        }

        if (selectItemKdw003_31("本人", "007102").findElement(By.xpath("./label/input")).isSelected()) {
            selectItemKdw003_31("本人", "007102").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver2.findElement(By.className("proceed")).click();
        WaitPageLoad();

        screenshotFile = ((TakesScreenshot) driver2).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));

        this.uploadTestLink(876, 214);
    }

    public WebElement selectItemKdw003_11(String itemName, String date) {
        int xPosition = driver2.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getX() + 1;
        int yPosition = driver2.findElement(By.xpath("//td[.='" + date + "']")).getRect().getY() + 1;
        return (WebElement) js2.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition,
                yPosition);
    }

    public WebElement selectItemKdw003_31(String itemName, String empId) {
        int xPosition = driver2.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getX() + 1;
        int yPosition = driver2.findElement(By.xpath("(//div[@class='mgrid-fixed']//td[.='" + empId + "'])[1]"))
                .getRect().getY() + 1;
        return (WebElement) js2.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition,
                yPosition);
    }

    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        final String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    @AfterEach
    public void tearDown2() throws Exception {
        driver2.quit();
        final String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}