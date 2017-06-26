package nts.uk.pr.file.infra.residentialtax;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ejb.Stateless;

import com.aspose.cells.BackgroundType;
import com.aspose.cells.BorderType;
import com.aspose.cells.CellArea;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Color;
import com.aspose.cells.FormatCondition;
import com.aspose.cells.FormatConditionCollection;
import com.aspose.cells.FormatConditionType;
import com.aspose.cells.PageSetup;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Worksheet;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.residentialtax.InhabitantTaxChecklistCGenerator;
import nts.uk.file.pr.app.export.residentialtax.data.InhabitantTaxChecklistCReport;
import nts.uk.file.pr.app.export.residentialtax.data.InhabitantTaxChecklistCRpData;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

@Stateless
public class InhabitantTaxChecklistReportCGenerator extends AsposeCellsReportGenerator
		implements InhabitantTaxChecklistCGenerator {

	/** The Constant TEMPLATE_FILE. */
	private static final String TEMPLATE_FILE = "report/qpp011c.xlsx";
	/** The Constant REPORT_FILE_NAME. */
	protected static final String REPORT_FILE_NAME = "テストQPP011_{0}.pdf";

	@Override
	public void generate(FileGeneratorContext fileContext, InhabitantTaxChecklistCReport dataExport) {
		try {
			AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE);


			// set datasource
			reportContext.setDataSource("header", dataExport.getHeader());
			reportContext.setDataSource("list", dataExport.getData());

			PageSetup pageSetup = reportContext.getWorkbook().getWorksheets().get(0).getPageSetup();
			pageSetup.setHeader(0, dataExport.getHeader().getCompanyName());
			pageSetup.setHeader(2, "&D &T");

			// process data binginds in template
			reportContext.getWorkbook().calculateFormula(true);
			reportContext.getDesigner().process(false);

			Worksheet worksheet = reportContext.getWorkbook().getWorksheets().get(0);

			int startRowIdx = 6;
			int rowColorIdex = 0;
			for (InhabitantTaxChecklistCRpData item : dataExport.getData()) {
				// Add FormatConditions to the instance of Worksheet
				int index = worksheet.getConditionalFormattings().add();

				// Access the newly added FormatConditions via its index
				FormatConditionCollection conditionCollection = worksheet.getConditionalFormattings().get(index);

				// Define a CellsArea on which conditional formatting will be
				// applicable
				CellArea area = CellArea.createCellArea("A" + startRowIdx, "F" + startRowIdx);

				// Add area to the instance of FormatConditions
				conditionCollection.addArea(area);

				// Add a condition to the instance of FormatConditions. For this
				// case, the condition type is expression, which is based on
				// some formula
				index = conditionCollection.addCondition(FormatConditionType.EXPRESSION);

				// Access the newly added FormatCondition via its index
				FormatCondition formatCondirion = conditionCollection.get(index);

				// Set the formula for the FormatCondition. Formula uses the
				// Excel's built-in functions as discussed earlier in this
				// article
				formatCondirion.setFormula1("=MOD(ROW(),1)=0");

				// Set the background color and patter for the FormatCondition's
				// Style
				formatCondirion.getStyle().setPattern(BackgroundType.SOLID);
				if (!item.getCheckSum()) {
					if (rowColorIdex % 2 == 0) {
						formatCondirion.getStyle().setBackgroundColor(Color.getWhite());
					} else {
						Color color = Color.fromArgb(204, 244, 145);
						formatCondirion.getStyle().setBackgroundArgbColor(color.toArgb());
					}
					rowColorIdex++;
				}

				if (item.getCheckSum()) {
					rowColorIdex = 0;
					Color color = Color.fromArgb(197, 241, 247);
					formatCondirion.getStyle().setBackgroundArgbColor(color.toArgb());
					formatCondirion.getStyle().setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
					formatCondirion.getStyle().setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN,
							Color.getBlack());
				}

				startRowIdx++;
			}

			// save as PDF file
			PdfSaveOptions option = new PdfSaveOptions(SaveFormat.PDF);
			option.setAllColumnsInOnePagePerSheet(true);
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddmmss");
			String message = MessageFormat.format(REPORT_FILE_NAME, format1.format(cal.getTime()));

			reportContext.getWorkbook().save(this.createNewFile(fileContext, message), option);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
