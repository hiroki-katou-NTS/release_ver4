package nts.uk.shr.sample.report.infra.generator.sample;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;
import nts.uk.shr.sample.report.app.export.sample.SampleReportDataSource;
import nts.uk.shr.sample.report.app.export.sample.SampleReportGenerator;

@Stateless
public class AsposeSampleReportGenerator extends AsposeCellsReportGenerator implements SampleReportGenerator {
	
	private static final String TEMPLATE_FILE = "report/SampleReport.xlsx";
	
	private static final String REPORT_FILE_NAME = "サンプル帳票.pdf";
	
	private static final String REPORT_ID = "ReportSample";

	@Override
	public void generate(FileGeneratorContext generatorContext, SampleReportDataSource dataSource) {
		
		try (val reportContext = this.createContext(TEMPLATE_FILE, REPORT_ID)) {
			
			// set data source named "item"
			reportContext.setDataSource("item", dataSource.getItems());
			
			// process data binginds in template
			reportContext.processDesigner();
			
			// save as PDF file
			reportContext.saveAsPdf(this.createNewFile(generatorContext, REPORT_FILE_NAME));
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
