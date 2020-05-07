package kaf006;

import java.io.File;

import org.openqa.selenium.By;

import common.TestRoot;

public class Kaf006Common extends TestRoot {
    String employeeCode = "";
    String password = "";
    String kaf006 = "";
    String cmm045 ="";
    String kdw003 = "";
    String kmk015 = "";

    public void login(File screenshotFile, String empCode, String passWord) throws Exception {
        driver.get(domain + "nts.uk.com.web/view/ccg/007/d/index.xhtml");
        WaitPageLoad();
        driver.findElement(By.id("company-code-select")).click();
        WaitElementLoad(By.xpath("//li[@data-value='0001']"));
        driver.findElement(By.xpath("//li[@data-value='0001']")).click();
        driver.findElement(By.id("employee-code-inp")).click();
        driver.findElement(By.id("employee-code-inp")).clear();
        driver.findElement(By.id("employee-code-inp")).sendKeys(empCode);

        driver.findElement(By.id("password-input")).click();
        driver.findElement(By.id("password-input")).clear();
        driver.findElement(By.id("password-input")).sendKeys(passWord);
        driver.findElement(By.id("login-btn")).click();
        WaitPageLoad();
    }

    public static String linkFullScreen(String link){
        return domain + link;
    }
}
