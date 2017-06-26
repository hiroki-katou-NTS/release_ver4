package nts.uk.shr.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;

public class DriverInitializer {
	
	private static Browser browser = Browser.IE;
	
	public static void setBrowser(Browser b) {
		browser = b;
	}
	
	public static WebDriver get() {
		switch(browser) {
			case CHROME: 
				String chromeDriverPath = "C:\\Works\\Installer\\Selenium\\chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", chromeDriverPath);
				return new ChromeDriver();
			case IE:
				String ieDriverPath = "C:\\Works\\Installer\\Selenium\\IEDriverServer.exe";
				System.setProperty("webdriver.ie.driver", ieDriverPath);
				return new InternetExplorerDriver();
			case SAFARI:
				// Install safari extension.
				return new SafariDriver();
		}
		return null;
	}
}
