package nts.uk.shr.driver.control;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import nts.uk.shr.driver.DriverInitializer;

import static org.hamcrest.CoreMatchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TextEditor {
	
	private static final String RESET_BTN_ID = "reset";
	private static final String ERR_NOTIFIER_ID = "func-notifier-errors";
	private static final String TEXT1_ID = "text-1";
	private static final String KANA_ID = "kana-txt";
	private static final String ALPHANUM_ID = "an-txt";
	private static final String HALF_INT_ID = "half-int";
	private static final String TEXT_SIMPLE_ID = "text-simple";
	
	private static WebDriver driver;
	
	private static WebElement text1;
	private static WebElement errorNotifier;
	private static WebElement resetBtn;
	private static WebElement enableCheckBox;
	private static WebElement kanaTxt;
	private static WebElement alphanumericTxt;
	private static WebElement halfIntTxt;
	private static WebElement textSimple;
	
	@BeforeClass
	public static void setup() {
		driver = DriverInitializer.get();
		driver.get("http://localhost:8080/nts.uk.com.web/view/sample/editor/text-editor.xhtml");
		
		text1 = driver.findElement(By.id(TEXT1_ID));
		errorNotifier = driver.findElement(By.id(ERR_NOTIFIER_ID));
		resetBtn = driver.findElement(By.id(RESET_BTN_ID));
		enableCheckBox = driver.findElement(By.className("ntsCheckBox"));
		kanaTxt = driver.findElement(By.id(KANA_ID));
		alphanumericTxt = driver.findElement(By.id(ALPHANUM_ID));
		halfIntTxt = driver.findElement(By.id(HALF_INT_ID));
		textSimple = driver.findElement(By.id(TEXT_SIMPLE_ID));
	}
	
	@Test
	public void textEditor1_notValid() {
		text1.sendKeys("abc");
		assertErrDisplay(true);
	}

	@Test
	public void textEditor2_valid() {
		text1.clear();
		text1.sendKeys("1234");
		assertErrDisplay(false);
	}
	
	@Test
	public void textEditor3_reset() {
		resetBtn.click();
		text1.sendKeys("567");
		// Disable
		enableCheckBox.click();
		String val = getText(id(TEXT1_ID));
		Assert.assertEquals(val, "1234");
	}
	
	@Test
	public void textEditor4_kanaInvalid() {
		kanaTxt.sendKeys("000");
		assertErrDisplay(true);
	}
	
	@Test
	public void textEditor5_kanaValid() {
		kanaTxt.clear();
		kanaTxt.sendKeys("ああ");
		alphanumericTxt.click();
		String val = getText(id(KANA_ID));
		Assert.assertEquals("アア", val);
	}
	
	@Test
	public void textEditor6_alphanumericInvalid() {
		alphanumericTxt.sendKeys("~");
		assertErrDisplay(true);
	}
	
	@Test
	public void textEditor7_alphanumericValid() {
		alphanumericTxt.clear();
		alphanumericTxt.sendKeys("a1");
		halfIntTxt.click();
		assertErrDisplay(false);
		Assert.assertEquals("A1", getText(id(ALPHANUM_ID)));
	}
	
	@Test
	public void textEditor8_halfIntInvalid() {
		halfIntTxt.sendKeys("1.6");
		assertErrDisplay(true);
	}
	
	@Test
	public void textEditor9_halfIntValid() {
		halfIntTxt.clear();
		halfIntTxt.sendKeys("1.5");
		textSimple.click();
		assertErrDisplay(false);
	}
	
	private String getText(String selector) {
		return (String) ((JavascriptExecutor) driver).executeScript("return $('" + selector + "').val();");
	}
	
	private String id(String name) {
		return "#" + name;
	}
	
	private void assertErrDisplay(boolean val) {
		Assert.assertThat(errorNotifier.isDisplayed(), is(val));
	}
	
	@AfterClass
	public static void tearDown() {
		driver.quit();
	}
}
