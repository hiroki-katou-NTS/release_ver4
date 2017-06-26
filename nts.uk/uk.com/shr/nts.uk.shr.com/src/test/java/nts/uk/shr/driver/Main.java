package nts.uk.shr.driver;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import nts.uk.shr.driver.control.CheckBox;

public class Main {
	
	private static final String CLASS_EXT = ".class";
	private static final JUnitCore junit = new JUnitCore(); 
	
	public static void main(String args[]) {
		DriverInitializer.setBrowser(Browser.IE);
		junit.addListener(new TextListener(System.out));
		run();
	}
	
	private static void run(Class<?>... clazz) {
		if (clazz.length != 0) { 
			junit.run(clazz);
			return;
		}
		
		String pkg = CheckBox.class.getPackage().getName();
		String path = pkg.replaceAll("\\.", "/");
		URL url = Thread.currentThread().getContextClassLoader().getResource(path);
		File scannedDir = new File(url.getFile());
		List<Class<?>> classes = Arrays.asList(scannedDir.listFiles()).stream().filter(f -> f.getName().contains(CLASS_EXT))
									.map(f -> {
											int end = f.getName().length() - CLASS_EXT.length();
											String className = pkg + "." + f.getName().substring(0, end);
											try {
												return Class.forName(className);
											} catch (ClassNotFoundException e) {
												return null;
											}
										}).collect(Collectors.toList());
		
		junit.run(classes.toArray(new Class[classes.size()]));
	}
}
