package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Robot;

import br.eti.kinoshita.testlinkjavaapi.constants.*;
import br.eti.kinoshita.testlinkjavaapi.model.*;
import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;

public class TestRoot {
    public static WebDriver driver;
    public static WebDriverWait _wait;
    public static String screenshotPath;
    public static File folder;
    public static JavascriptExecutor js;
    public static SimpleDateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat df2 = new SimpleDateFormat("MM/dd");
    public static SimpleDateFormat df3 = new SimpleDateFormat("yyyy/MM");
    public static String domain = "";

    public static String DEVKEY;
    public static String url;
    public static String chromedriverPath;
    public StringBuffer verificationErrors = new StringBuffer();
    public int imageIndex = 1;
    public String dbServer;
    public String databaseName;
    public String user;
    public String password;

    public void init() throws Exception {
        File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        try (InputStream input = new FileInputStream(jarFile.getParent() + File.separator + "config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            DEVKEY = prop.getProperty("DEVKEY");
            url = prop.getProperty("url");
            domain = prop.getProperty("domain");
            dbServer = prop.getProperty("dbServer");
            databaseName = prop.getProperty("databaseName");
            user = prop.getProperty("user");
            password = prop.getProperty("password");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        folder = new File(screenshotPath);
        if(folder.exists()) FileUtils.cleanDirectory(folder);
        System.setProperty("webdriver.chrome.driver", jarFile.getParent() + File.separator + "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        js = (JavascriptExecutor) driver;
        _wait = new WebDriverWait(driver, 180);
    }

    public void WaitPageLoad() {
        try {
            Thread.sleep(1000);
            _wait.until(d -> {
                try {
                    d.findElement(By.xpath("//div[contains(@class,'blockOverlay')]"));
                } catch (NoSuchElementException e) {
                    return true;
                }
                return false;
            });
        } catch (Exception e) {
        }
    }

    public void WaitElementLoad(By locator) {
        try {
            Thread.sleep(1000);
            _wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            _wait.until(d -> ExpectedConditions.elementToBeClickable(d.findElement(locator)));
        } catch (Exception e) {
        }
    }

    public void login(String userId, String password) {
        driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("company-code-select")).click();
        WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        driver.findElement(By.id("password-input")).clear();
        driver.findElement(By.id("password-input")).sendKeys(password);
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys(userId);
        this.screenShot();
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
    }

    public void screenShot() {
        try {
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/image" + String.valueOf(imageIndex)  + ".png"));
            imageIndex++;
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    public void screenShotFull() {
        try {
            Robot r = new Robot();
            Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage Image = r.createScreenCapture(capture);
            ImageIO.write(Image, "png", new File(screenshotPath + "/image" + String.valueOf(imageIndex)  + ".png"));
            imageIndex++;
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    //select item kdw003 個人別
    public WebElement selectItemKdw003_1(String itemName, String date) {
        int xPosition = driver.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getX() + 1;
        int yPosition = driver.findElement(By.xpath("//td[.='" + date + "']")).getRect().getY() + 1;
        return (WebElement)js.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition, yPosition);
    }
    //select item kdw003 個人別
    public WebElement selectItemKdw003_2(String itemName, String date) {
        int xPosition = driver.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getX() +  driver.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getWidth()/2 + 5;
        int yPosition = driver.findElement(By.xpath("//td[.='" + date + "']")).getRect().getY() + 20;
        return (WebElement)js.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition, yPosition);
    }
    //select item kdw003 日付別
    public WebElement selectItemKdw003_3(String itemName, String empId) {
        int xPosition = driver.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getX() + 1;
        int yPosition = driver.findElement(By.xpath("(//div[@class='mgrid-fixed']//td[.='" + empId + "'])[1]")).getRect().getY() + 1;
        return (WebElement)js.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition, yPosition);
    }
    //select item kdw003 日付別
    public WebElement selectItemKdw003_4(String itemName, String empId) {
        int xPosition = driver.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getX() + driver.findElement(By.xpath("//td[.='" + itemName + "']")).getRect().getWidth()/2 + 5;
        int yPosition = driver.findElement(By.xpath("(//div[@class='mgrid-fixed']//td[.='" + empId + "'])[1]")).getRect().getY() + 20;
        return (WebElement)js.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition, yPosition);
    }
    //select item kdw004
    public WebElement selectItemKdw004(String date, String empId) {
        int xPosition = driver.findElement(By.xpath("//span[.='" + date + "']")).getRect().getX() + 1;
        int yPosition = driver.findElement(By.xpath("(//div[@id='approvalSttGrid_scroll']//td[.='" + empId + "'])[1]")).getRect().getY() + 1;
        return (WebElement)js.executeScript("return document.elementFromPoint(arguments[0], arguments[1])", xPosition, yPosition);
    }

    public void registerDB(List<String> sqls) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            //TODO: handle exception
        }
        String dbURL = "jdbc:sqlserver://" + dbServer;
        Properties properties = new Properties();
        properties.put("DatabaseName", databaseName);
        properties.put("user", user);
        properties.put("password", password);
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = DriverManager.getConnection(dbURL, properties);
            for (String sql:sqls) {
                statement = conn.prepareStatement(sql);
                // statement.setString(1, "bill");
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            //TODO: handle exception
        } finally {
            try {
                conn.close();
                statement.close();
            } catch (SQLException e) {
                //TODO: handle exception
            }
        }
    }


    public void uploadTestLink(int testCaseId, int testCaseExternalId) {
        try {
            final URL testlinkURL = new URL(url);
            final TestLinkAPI api = new TestLinkAPI(testlinkURL, DEVKEY);

            //final TestCaseStepResult tcstepres1 = new TestCaseStepResult(1, ExecutionStatus.PASSED, "", true, ExecutionType.AUTOMATED);
            //final TestCaseStepResult tcstepres2 = new TestCaseStepResult(2, ExecutionStatus.PASSED, "", true, ExecutionType.AUTOMATED);
            List < TestCaseStepResult > steps = new ArrayList < TestCaseStepResult > ();
            //steps.add(tcstepres1);
            //steps.add(tcstepres2);

            final Integer testPlanId = 15;
            final ExecutionStatus status = ExecutionStatus.PASSED;
            final Integer buildId = 2;
            final String buildName = "Build2";
            String notes = "";
            final Integer executionDuration = 1;
            final Boolean guess = false;
            final String bugId = "";
            final Integer platformId = 0;
            final String platformName = "Chrome";
            final Map < String, String > customFields = new HashMap < > ();
            final Boolean overwrite = false;
            final String user = "user";

            final ReportTCResultResponse report_response = api.reportTCResult(testCaseId, testCaseExternalId, testPlanId, status,
                steps, buildId, buildName, notes, executionDuration, guess, bugId, platformId, platformName, customFields, overwrite, user, "");
            final Integer executionId = report_response.getExecutionId();

            String title = "";
            final String description = "";
            String fileName = "";
            final String fileType = "image/png";
            File attachmentFile = null;
            byte[] byteArray = null;
            String fileContent = null;

            int size = folder.list().length;
            for (int i = size; i > 0 ; i--) {
                fileName = "image" + i + ".png";
                title = "結果" + i;
                attachmentFile = new File(screenshotPath + "/" + fileName);
                byteArray = FileUtils.readFileToByteArray(attachmentFile);
                fileContent = Base64.getEncoder().encodeToString(byteArray);
                api.uploadExecutionAttachment(executionId, title, description, fileName, fileType, fileContent);
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }


}