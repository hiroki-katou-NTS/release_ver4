package kdw007;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import common.TestRoot;

public class kdw007Common extends TestRoot{
    public void closeDialog()throws Exception{
        try {
            WebElement dialog = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
            WaitElementLoad(By.xpath("//iframe[contains(@name,'window')]"));
            if(dialog.isDisplayed()){
                driver.switchTo().frame(driver.findElement(By.xpath("//iframe[contains(@name,'window')]")));
                driver.findElement(By.id("dialogClose")).click();
                driver.findElement(By.xpath("//button[@id='btnExtraction']"));
            }       
        } catch (Exception e) {
            
        }
    }
    public void switchFrame()throws Exception{
        WebElement dialog = driver.findElement(By.xpath("//iframe[contains(@name,'window')]"));
        driver.switchTo().frame(dialog);
    }
    public void scrollToPos(String errorCode, String dateTime)throws Exception{
        Object index = js.executeScript("return _.findIndex(nts.uk.ui._viewModel.content.lstError(), o => o.errorCode == '" + errorCode + "'&& o.dateDetail.format('YYYY/MM/DD')=='"+dateTime+"')");
        js.executeScript("$(`#grid_scroll`).scrollTop(" + index + "*40)");
        driver.findElement(By.xpath("//td[contains(.,'"+errorCode+"')]")).click();
    }
    public void closeMsg()throws Exception{
        try {
            WebElement msg = driver.findElement(By.xpath("//button[@class='large']"));
            if(msg.isDisplayed()){
                msg.click();
            }
        } catch (Exception e) {
            
        }
    }
    public void inputDate(String startDate, String endDate)throws Exception{
    WebElement startTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsStartDatePicker"));
    startTime.clear();
    startTime.sendKeys(startDate);
    WebElement endTime = driver.findElement(By.id("daterangepicker")).findElement(By.className("ntsEndDatePicker"));
    endTime.clear();
    endTime.sendKeys(endDate);
    }
}