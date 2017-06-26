package nts.uk.pr.file.infra.banktransfer;

import java.util.List;

import javax.ejb.Stateless;

import com.aspose.cells.PageSetup;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.SaveFormat;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.banktransfer.BankTransferRpAGenerator;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferAReport;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferARpData;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

@Stateless
public class BankTransferReportAGenerator extends AsposeCellsReportGenerator implements BankTransferRpAGenerator {

	/** The Constant TEMPLATE_FILE. */
	private static final String TEMPLATE_FILE = "report/qpp014a.xlsx";
	/** The Constant REPORT_FILE_NAME. */
	protected static final String REPORT_FILE_NAME = "テストQPP014.pdf";

	@Override
	public void generator(FileGeneratorContext fileGeneratorContext, BankTransferAReport report) {
		try {
			AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE);
			List<BankTransferARpData> rpData = report.getData();

			// set datasource
			reportContext.setDataSource("header", report.getHeader());
			reportContext.setDataSource("list", rpData);

			PageSetup pageSetup = reportContext.getWorkbook().getWorksheets().get(0).getPageSetup();
			pageSetup.setHeader(0, report.getHeader().getCompanyName());
			pageSetup.setHeader(2, "&D &T");

			// process data binginds in template
			reportContext.getWorkbook().calculateFormula(true);
			reportContext.getDesigner().process(false);

			// save as PDF file
			PdfSaveOptions option = new PdfSaveOptions(SaveFormat.PDF);
			option.setAllColumnsInOnePagePerSheet(true);

			reportContext.getWorkbook().save(this.createNewFile(fileGeneratorContext, REPORT_FILE_NAME), option);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
