package nts.uk.pr.file.infra.residentialtax;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;

import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import com.aspose.cells.WorkbookDesigner;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.residentialtax.ResidentialTaxGenerator;
import nts.uk.file.pr.app.export.residentialtax.data.ResidentTaxReportData;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

@Stateless
public class ResidentialTaxReportGenerator extends AsposeCellsReportGenerator implements ResidentialTaxGenerator {
	/** The Constant TEMPLATE_FILE. */
	private static final String TEMPLATE_FILE = "report/qpp011a.xlsx";
	/** The Constant REPORT_FILE_NAME. */
	protected static final String REPORT_FILE_NAME = "テストQPP011_{0}.pdf";

	@Override
	public void generate(FileGeneratorContext fileContext, List<ResidentTaxReportData> reportDataList) {
		try {
			AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE);
			Worksheet mainWorkSheet = reportContext.getWorkbook().getWorksheets().get(0);

			Workbook workbook = new Workbook();
			workbook.getWorksheets().removeAt(0);
			WorkbookDesigner designer = new WorkbookDesigner();
			designer.setWorkbook(workbook);

			int sheetNumber = 0;

			for (ResidentTaxReportData reportData : reportDataList) {
				workbook = addWorksheet(workbook, mainWorkSheet);

				Worksheet worksheet = workbook.getWorksheets().get(sheetNumber);
				worksheet.replace("DBD_", String.valueOf("DBD" + sheetNumber + "_"));
				worksheet.replace("CTR_", String.valueOf("CTR" + sheetNumber + "_"));
				//DBD_001  residenceTaxCode
				designer.setDataSource("DBD" + sheetNumber + "_001", reportData.getResidenceTaxCode());
				//DBD_002  resiTaxAutonomy
				designer.setDataSource("DBD" + sheetNumber + "_002", reportData.getResiTaxAutonomy());
				//DBD_003  companyAccountNo
				designer.setDataSource("DBD" + sheetNumber + "_003", reportData.getCompanyAccountNo());
				//DBD_004  registeredName
				designer.setDataSource("DBD" + sheetNumber + "_004", reportData.getRegisteredName());
				//DBD_005  companySpecifiedNo
				designer.setDataSource("DBD" + sheetNumber + "_005", reportData.getCompanySpecifiedNo());
				//DBD_006  salaryPaymentAmount
				designer.setDataSource("DBD" + sheetNumber + "_006", format(reportData.getSalaryPaymentAmount()));
				//DBD_007  deliveryAmountRetirement
				designer.setDataSource("DBD" + sheetNumber + "_007", format(reportData.getDeliveryAmountRetirement()));
				//DBD_008  postal
				designer.setDataSource("DBD" + sheetNumber + "_008", reportData.getPostal());
				//DBD_009  address1
				designer.setDataSource("DBD" + sheetNumber + "_009", reportData.getAddress1());
				//DBD_010  address2
				designer.setDataSource("DBD" + sheetNumber + "_010", reportData.getAddress2());
				//DBD_011  companyName
				designer.setDataSource("DBD" + sheetNumber + "_011", reportData.getCompanyName());
				//DBD_012  cordinatePostOffice
				designer.setDataSource("DBD" + sheetNumber + "_012", reportData.getCordinatePostOffice());
				//DBD_013  cordinatePostalCode
				designer.setDataSource("DBD" + sheetNumber + "_013", reportData.getCordinatePostalCode());
				//DBD_014  deliveryNumber
				designer.setDataSource("DBD" + sheetNumber + "_014", reportData.getDeliveryNumber());
				//DBD_015  actualRecieveMny
				designer.setDataSource("DBD" + sheetNumber + "_015", format(reportData.getActualRecieveMny()));
				//DBD_016  cityTaxMny
				designer.setDataSource("DBD" + sheetNumber + "_016", format(reportData.getCityTaxMny()));
				//DBD_017  prefectureTaxMny
				designer.setDataSource("DBD" + sheetNumber + "_017", format(reportData.getPrefectureTaxMny()));
				//DBD_018  taxOverdueMny
				designer.setDataSource("DBD" + sheetNumber + "_018", format(reportData.getTaxOverdueMny()));
				//DBD_019  taxDemandChargeMny
				designer.setDataSource("DBD" + sheetNumber + "_019", format(reportData.getTaxDemandChargeMny()));
				//DBD_020  filingDate
				designer.setDataSource("DBD" + sheetNumber + "_020", reportData.getFilingDate());
				//CTR_001  designatedYM
				designer.setDataSource("CTR" + sheetNumber + "_001", reportData.getDesignatedYM());
				//CTR_002  totalAmountTobePaid
				designer.setDataSource("CTR" + sheetNumber + "_002", format(reportData.getTotalAmountTobePaid()));
				//CTR_003  dueDate
				designer.setDataSource("CTR" + sheetNumber + "_003", reportData.getDueDate());
				
				worksheet.replace("&=$DBD"+ sheetNumber +"_002", reportData.getAddress1());
				worksheet.replace("&=$DBD"+ sheetNumber +"_008", reportData.getPostal());
				worksheet.replace("&=$DBD"+ sheetNumber +"_009", reportData.getAddress1());
				worksheet.replace("&=$DBD"+ sheetNumber +"_010", reportData.getAddress2());
				worksheet.replace("&=$DBD"+ sheetNumber +"_011", reportData.getCompanyName());
				worksheet.replace("&=$DBD"+ sheetNumber +"_012", reportData.getCordinatePostOffice());
				worksheet.replace("&=$DBD"+ sheetNumber +"_013", reportData.getCordinatePostalCode());
				worksheet.replace("&=$DBD"+ sheetNumber +"_014", reportData.getDeliveryNumber());
				worksheet.replace("&=$DBD"+ sheetNumber +"_020", reportData.getFilingDate());
				
				sheetNumber++;
			}

			// process data binginds in template
			designer.getWorkbook().calculateFormula(true);
			designer.process(false);

			// save as PDF file
			PdfSaveOptions option = new PdfSaveOptions(SaveFormat.PDF);
			option.setAllColumnsInOnePagePerSheet(true);
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddmmss");
			String message = MessageFormat.format(REPORT_FILE_NAME, format1.format(cal.getTime()));
			
			designer.getWorkbook().save(this.createNewFile(fileContext, message), option);

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
	
	private String format(Double input) {
		if (input == null) {
			input = 0.0;
		}
		DecimalFormat formatter = new DecimalFormat();
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(',');
		formatter.setDecimalFormatSymbols(symbols);
		
		return formatter.format(input);
	}

}
