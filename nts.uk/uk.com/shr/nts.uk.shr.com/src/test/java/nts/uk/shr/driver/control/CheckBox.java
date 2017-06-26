package nts.uk.shr.driver.control;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import nts.uk.shr.driver.DriverInitializer;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CheckBox {
	
	private static final String CHECKBOX_ID = "check-box";
	private static final String RESET_ID = "reset";
	
	private static WebDriver driver;
	
	private static WebElement enableCheckBox;
	private static WebElement checkBoxGroup;
	private static WebElement resetBtn;
	private static List<WebElement> checkBoxes;
	
	@BeforeClass
	public static void setup() {
		driver = DriverInitializer.get();
		driver.get("http://localhost:8080/nts.uk.com.web/view/sample/multicheckbox/multi-checkbox.xhtml");
		
		enableCheckBox = driver.findElement(By.className("ntsCheckBox"));
		checkBoxGroup = driver.findElement(By.id(CHECKBOX_ID));
		resetBtn = driver.findElement(By.id(RESET_ID));
	}
	
	@Test
	public void s1_enable() {
		enableCheckBox.click();
		List<Boolean> expected = Arrays.asList(false, false, true, false, false, true, false, false, true);
		checkBoxes = checkBoxGroup.findElements(By.tagName("label"));
		List<Boolean> values = checkBoxes.stream().map(e -> {
			return e.findElement(By.tagName("input")).isEnabled();
		}).collect(Collectors.toList());
		Assert.assertEquals(expected, values);
	}
	
	@Test
	public void s2_reset() {
		checkBoxes.get(5).click();
		resetBtn.click();
		checkBoxes.get(8).click();
		enableCheckBox.click();
		List<Boolean> expected = Arrays.asList(true, true, false, false, false, true, false, false, false);
		List<Boolean> selected = checkBoxes.stream().map(e -> e.findElement(By.tagName("input")).isSelected()).collect(Collectors.toList());
		Assert.assertEquals(expected, selected);
	}
	
	@AfterClass
	public static void tearDown() {
		driver.quit();
	}
	
}
