package kmw005;

import java.io.File;

import org.junit.jupiter.api.*;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.*;

import common.TestRoot;
import java.awt.Toolkit;
import java.awt.Rectangle; 
import java.awt.Robot; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO; 


public class Scenario2Case1 extends TestRoot {
    public int randomInt;
    String company_code = "company-code-select";//xpath
    String company_code_value = "//li[@data-value='0001']";//xpath
    String employee_code = "employee-code-inp";//id
    String password = "password-input" ;//id
    String login_btn = "login-btn";//id
    String inpMonth ="inpMonth";//id
    String btnsave = "btn_save";//id
    String locklist ="//*[@id='actualLock-list']/tbody/tr[1]";
    String lock1 ="//*[@id='monthlyActualLock']/button[1]";
    String unlock1 ="//*[@id='monthlyActualLock']/button[2]";
    String closeMsg15="/html/body/div[5]/div[3]/div/button";
   


    @BeforeEach
    public void setUp() throws Exception {
        screenshotPath = "images/kmw005/Scenario2Case1_2";
        this.init();
    }

    @Test
    public void test() throws Exception {
        
        //login 018234/Jinjikoi5
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        login("018234", "Jinjikoi5", "image1", screenshotFile);
        
        //go kmw005
        driver.get(domain+ "nts.uk.at.web/view/kmw/005/a/index.xhtml");
        WaitPageLoad();
        //lock 1
        driver.findElement(By.xpath(locklist)).click();
        driver.findElement(By.xpath(lock1)).click();
        driver.findElement(By.id(btnsave)).click();
        WaitElementLoad(By.xpath(closeMsg15));
        screenShot_Full("/image2.png");
        driver.findElement(By.xpath(closeMsg15)).click();
        WaitPageLoad();

        // go kdw003
        driver.get(domain+"nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        // tháng 11 change time value
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDatePrevButton")).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        Random rd = new Random();
        randomInt = rd.nextInt((1959 - 1900) + 1) + 1900;
        String value = String.valueOf(randomInt);
        setValueGrid(6,8,value);
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id='function-content']/button[1]")).click();
        WaitPageLoad();
        Thread.sleep(2000);
        screenShot_Full("/image3.png");
        // done case2-1

         //unlock1
        driver.get(domain+ "nts.uk.at.web/view/kmw/005/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.xpath(locklist)).click();
        driver.findElement(By.xpath(unlock1)).click();
        driver.findElement(By.id(btnsave)).click();
        driver.findElement(By.xpath(closeMsg15)).click();
        WaitPageLoad();

        //tháng 11 return value 17:30
        driver.get(domain+ "nts.uk.at.web/view/kdw/003/a/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsDatePrevButton")).click();
        driver.findElement(By.id("btnExtraction")).click();
        WaitPageLoad();
        setValueGrid(6,8,"17:30");
        driver.findElement(By.xpath("//body")).click();
        WaitPageLoad();
        driver.findElement(By.xpath("//*[@id='function-content']/button[1]")).click();

     // this.uploadTestLink(76, 13);
    }

        @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
    public void screenShot(String image_name) throws Exception {
         File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
       FileUtils.copyFile(screenshotFile, new File(screenshotPath + image_name));
    }

    public void screenShot_Full(String image_name) throws Exception {
        Robot r = new Robot(); 
        Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage Image = r.createScreenCapture(capture); 
        ImageIO.write(Image, "png", new File(screenshotPath + image_name));
   }

    public void login(String id,String pass,String nameImage,File screenshotFile) throws Exception{
        driver.get(domain+"nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id(company_code)).click();
        WaitElementLoad(By.xpath(company_code_value));
        driver.findElement(By.xpath(company_code_value)).click();
        driver.findElement(By.id(employee_code)).clear();
        driver.findElement(By.id(employee_code)).sendKeys(id);
        driver.findElement(By.id(password)).clear();
        driver.findElement(By.id(password)).sendKeys(pass);
        screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File(screenshotPath + "/"+nameImage+".png"));
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
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

}