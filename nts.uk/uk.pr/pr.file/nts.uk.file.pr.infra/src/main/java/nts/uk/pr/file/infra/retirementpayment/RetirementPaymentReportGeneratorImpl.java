/**
 * 
 */
package nts.uk.pr.file.infra.retirementpayment;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.cells.WorkbookDesigner;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.retirementpayment.RetirementPaymentReportGenerator;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirePayItemDto;
import nts.uk.file.pr.app.export.retirementpayment.data.RetirementPaymentReportData;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * @author hungnm
 *
 */
@Stateless
public class RetirementPaymentReportGeneratorImpl extends AsposeCellsReportGenerator
		implements RetirementPaymentReportGenerator {
	/** The Constant TEMPLATE_FILE. */
	private static final String TEMPLATE_FILE = "report/qrm009.xlsx";
	/** The Constant REPORT_FILE_NAME. */
	protected static final String REPORT_FILE_NAME = "退職金明細書.pdf";

	@Override
	public void generate(FileGeneratorContext generatorContext, List<RetirementPaymentReportData> dataSource,
			List<RetirePayItemDto> lstRetirePayItemDto) {
		try {
			// get main template
			AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE);
			Worksheet mainWorkSheet = reportContext.getWorkbook().getWorksheets().get(0);
			// create workbook to export
			Workbook workbook = new Workbook();
			workbook.getWorksheets().removeAt(0);
			// create designer
			WorkbookDesigner designer = new WorkbookDesigner();
			designer.setWorkbook(workbook);
			// get current date
			Date date = new Date();
			// filter pay item name
			String printNameT1001 = lstRetirePayItemDto.stream().filter(item -> item.getItemCode().equals("T001"))
					.findFirst().orElse(new RetirePayItemDto("T001", "退職金支給額", "")).getItemName();
			String printNameT2001 = lstRetirePayItemDto.stream().filter(item -> item.getItemCode().equals("T101"))
					.findFirst().orElse(new RetirePayItemDto("T101", "所得税", "")).getItemName();
			String printNameT2002 = lstRetirePayItemDto.stream().filter(item -> item.getItemCode().equals("T102"))
					.findFirst().orElse(new RetirePayItemDto("T102", "市区町村民税", "")).getItemName();
			String printNameT2003 = lstRetirePayItemDto.stream().filter(item -> item.getItemCode().equals("T103"))
					.findFirst().orElse(new RetirePayItemDto("T103", "都道府県民税", "")).getItemName();
			String printNameT2004 = lstRetirePayItemDto.stream().filter(item -> item.getItemCode().equals("T104"))
					.findFirst().orElse(new RetirePayItemDto("T104", "その他控除1", "")).getItemName();
			String printNameT2005 = lstRetirePayItemDto.stream().filter(item -> item.getItemCode().equals("T105"))
					.findFirst().orElse(new RetirePayItemDto("T105", "その他控除2", "")).getItemName();
			String printNameT2006 = lstRetirePayItemDto.stream().filter(item -> item.getItemCode().equals("T106"))
					.findFirst().orElse(new RetirePayItemDto("T106", "その他控除3", "")).getItemName();
			String printNameT2007 = lstRetirePayItemDto.stream().filter(item -> item.getItemCode().equals("T107"))
					.findFirst().orElse(new RetirePayItemDto("T107", "控除額合計", "")).getItemName();
			String printNameT3001 = lstRetirePayItemDto.stream().filter(item -> item.getItemCode().equals("T301"))
					.findFirst().orElse(new RetirePayItemDto("T301", "差引支給額", "")).getItemName();
			int sheetNumber = 0;
			for (RetirementPaymentReportData retirementPaymentReportData : dataSource) {
				// create new page follow main template
				workbook = addWorksheet(workbook, mainWorkSheet);
				// bind data to this sheet
				Worksheet worksheet = workbook.getWorksheets().get(sheetNumber);
				worksheet.replace("data", String.valueOf("data" + sheetNumber));
				designer.setDataSource("data" + sheetNumber, Arrays.asList(retirementPaymentReportData));
				designer.setDataSource("currentDate", date);
				designer.setDataSource("CTR_002", "12年7ヶ 月");
				designer.setDataSource("retirePayItemT1001", printNameT1001);
				designer.setDataSource("retirePayItemT2001", printNameT2001);
				designer.setDataSource("retirePayItemT2002", printNameT2002);
				designer.setDataSource("retirePayItemT2003", printNameT2003);
				designer.setDataSource("retirePayItemT2004", printNameT2004);
				designer.setDataSource("retirePayItemT2005", printNameT2005);
				designer.setDataSource("retirePayItemT2006", printNameT2006);
				designer.setDataSource("retirePayItemT2007", printNameT2007);
				designer.setDataSource("retirePayItemT3001", printNameT3001);
				designer.setDataSource("companyAddress", retirementPaymentReportData.getCompanyMasterDto().getAddress1()
						+ "  " + retirementPaymentReportData.getCompanyMasterDto().getAddress2());
				sheetNumber++;
			}
			// apply data's binding in template
			designer.getWorkbook().calculateFormula(true);
			designer.process();
			// save as PDF file
			PdfSaveOptions option = new PdfSaveOptions(SaveFormat.PDF);
			option.setAllColumnsInOnePagePerSheet(true);
			OutputStream outputFile = this.createNewFile(generatorContext, this.getReportName(REPORT_FILE_NAME));
			designer.getWorkbook().save(outputFile, option);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Workbook addWorksheet(Workbook workbook, Worksheet worksheet) {
		// Workbookに宛名のsheetとテンプレートのsheetを追加
		try {
			WorksheetCollection worksheets = workbook.getWorksheets();

			int mainSheet = worksheets.add();
			worksheets.get(mainSheet).copy(worksheet);

		} catch (Exception e) {
			throw new RuntimeException(e);
			// LOGGER.error("Failed create workbook. " + e.getMessage());
		}

		return workbook;
	}

}
