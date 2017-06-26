package nts.uk.shr.sample.report;

import java.io.InputStream;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class AsposeLicenseLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsposeLicenseLoader.class);
	
	@PostConstruct
	public void initialize() {
		loadLicense("Aspose.Cells.lic", is -> new com.aspose.cells.License().setLicense(is));
//		loadLicense("Aspose.Pdf.lic", is -> {
//			try {
//				new com.aspose.pdf.License().setLicense(is);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		});
	}
	
	private static void loadLicense(String licenseFileName, Consumer<InputStream> loadLicense) {
		LOGGER.info("Started loading license: " + licenseFileName);
		InputStream is = AsposeLicenseLoader.class.getResourceAsStream("/aspose/" + licenseFileName);
		try {
			loadLicense.accept(is);
		} catch (Throwable ex) {
			LOGGER.info("Failed loading license: " + licenseFileName);
			LOGGER.info(ex.toString());
			throw new RuntimeException(ex);
		}
		LOGGER.info("Completed loading license: " + licenseFileName);
	}
	
}

