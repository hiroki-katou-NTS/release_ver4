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

public class Scenario4Case1 extends TestRoot {
    public static JavascriptExecutor js2;
    public static ChromeOptions options;
    public static WebDriver driver2;

    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kdw004/Scenario4Case1";
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

        // login申請者
        driver2.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver2.findElement(By.id("company-code-select")).click();
        WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        driver2.findElement(By.xpath("//li[@data-value='0001']")).click();
        driver2.findElement(By.id("password-input")).clear();
        driver2.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        driver2.findElement(By.id("employee-code-inp")).clear();
        driver2.findElement(By.id("employee-code-inp")).sendKeys("007102");
        driver2.findElement(By.id("login-btn")).click();
        WaitPageLoad();

        // Setting screen kmk012
        driver2.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();

        // Clear Input Month
        WaitElementLoad(By.id("inpMonth"));
        driver2.findElement(By.id("inpMonth")).clear();

        // Input into Month
        WaitElementLoad(By.id("inpMonth"));
        driver2.findElement(By.id("inpMonth")).sendKeys("2019/10");
        driver2.findElement(By.id("contents-right")).click();

        // Click button Save
        WaitElementLoad(By.id("btn_save"));
        driver2.findElement(By.id("btn_save")).click();
        WaitPageLoad();

        // Go to screen Kdw003a
        driver2.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        WaitElementLoad((By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")));
        driver2.findElement(By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")).click();

        driver2.findElement(By.xpath("//li[contains(.,'個人別')]")).click();

        WaitElementLoad(By.id("btnExtraction"));
        driver2.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        if (!selectItemKdw003_11("本人", "11/11(月)").findElement(By.xpath("./label/input")).isSelected()
                && selectItemKdw003_11("本人", "11/11(月)").findElement(By.xpath("./label/input"))
                        .isEnabled()) {
            selectItemKdw003_11("本人", "11/11(月)").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver2.findElement(By.className("proceed")).click();
        WaitPageLoad();

         // Go to screen Kdw003a
         driver2.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
         WaitPageLoad();

        // login申請者
        driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("company-code-select")).click();
        WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        WaitElementLoad(By.id("password-input"));
        driver.findElement(By.id("password-input")).clear();
        driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys("004515");
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image1.png"));
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();

        // Setting screen kmk012
        driver.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();

        // Clear Input Month
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).clear();

        // Input into Month
        WaitElementLoad(By.id("inpMonth"));
        driver.findElement(By.id("inpMonth")).sendKeys("2019/10");
        driver.findElement(By.id("contents-right")).click();

        // Click button Save
        WaitElementLoad(By.id("btn_save"));
        driver.findElement(By.id("btn_save")).click();
        WaitPageLoad();

        // Go to screen Kdw003a
        driver.get(domain + "nts.uk.at.web/view/kdw/004/a/index.xhtml");
        WaitPageLoad();

        selectItemKdw004("社員名", "007102").click();

        WaitElementLoad((By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")));
        driver.findElement(By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")).click();

        WaitElementLoad(By.xpath("//li[contains(.,'個人別')]"));
        driver.findElement(By.xpath("//li[contains(.,'個人別')]")).click();

        WaitElementLoad(By.id("btnExtraction"));
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        if (selectItemKdw003_1("承認", "11/11(月)").findElement(By.xpath("./label/input")).isSelected()
                && selectItemKdw003_1("承認", "11/11(月)").findElement(By.xpath("./label/input"))
                        .isEnabled()) {
            selectItemKdw003_1("承認", "11/11(月)").click();
        }

        // tacke a photo
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image2.png"));

        // Setting screen kmk012
        driver2.get(domain + "nts.uk.at.web/view/kmk/012/a/index.xhtml");
        WaitPageLoad();

        // Clear Input Month
        WaitElementLoad(By.id("inpMonth"));
        driver2.findElement(By.id("inpMonth")).clear();

        // Input into Month
        WaitElementLoad(By.id("inpMonth"));
        driver2.findElement(By.id("inpMonth")).sendKeys("2019/10");
        driver2.findElement(By.id("contents-right")).click();

        // Click button Save
        WaitElementLoad(By.id("btn_save"));
        driver2.findElement(By.id("btn_save")).click();
        WaitPageLoad();

        // Go to screen Kdw003a
        driver2.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();

        WaitElementLoad((By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")));
        driver2.findElement(By.xpath("//*[@id='cbDisplayFormat']/div/div[2]")).click();

        driver2.findElement(By.xpath("//li[contains(.,'個人別')]")).click();

        WaitElementLoad(By.id("btnExtraction"));
        driver2.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();

        if (selectItemKdw003_11("本人", "11/11(月)").findElement(By.xpath("./label/input")).isSelected()
                && selectItemKdw003_11("本人", "11/11(月)").findElement(By.xpath("./label/input"))
                        .isEnabled()) {
            selectItemKdw003_11("本人", "11/11(月)").click();
        }

        WaitElementLoad(By.className("proceed"));
        driver2.findElement(By.className("proceed")).click();
        WaitPageLoad();

        // tacke a photo
        screenshotFile = ((TakesScreenshot) driver2).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image3.png"));
        WaitPageLoad();

        // // login申請者
        // driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        // WaitPageLoad();
        // driver.findElement(By.id("company-code-select")).click();
        // WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        // driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        // driver.findElement(By.id("password-input")).clear();
        // driver.findElement(By.id("password-input")).sendKeys("Jinjikoi5");
        // driver.findElement(By.id("employee-code-inp")).clear();
        // driver.findElement(By.id("employee-code-inp")).sendKeys("004515");
        // screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image5.png"));
        // driver.findElement(By.id("login-btn")).click();
        // WaitPageLoad();

        // // Go to screen Kdw003a
        // driver.get(domain + "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        // WaitPageLoad();

        selectItemKdw003_1("承認", "11/11(月)").click();
        Thread.sleep(2000);

        WaitElementLoad(By.className("proceed"));
        driver.findElement(By.className("proceed")).click();
        Thread.sleep(2000);

        
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image4.png"));
        WaitPageLoad();
        
        this.uploadTestLink(870, 211);
    }

    public WebElement selectItemKdw003_11(String itemName, String date) {
        int xPosition = driver2.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getX() + 1;
        int yPosition = driver2.findElement(By.xpath("//td[.='" + date + "']")).getRect().getY() + 1;
        return (WebElement)js2.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition, yPosition);
    }

     public WebElement selectItemKdw003_31(String itemName, String empId) {
        int xPosition = driver2.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getX() + 1;
        int yPosition = driver2.findElement(By.xpath("(//div[@class='mgrid-fixed']//td[.='" + empId + "'])[1]")).getRect().getY() + 1;
        return (WebElement)js2.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition, yPosition);
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