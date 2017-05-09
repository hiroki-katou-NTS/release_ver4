package nts.uk.pr.file.infra.residentialtax;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.aspose.cells.BackgroundType;
import com.aspose.cells.CellArea;
import com.aspose.cells.Color;
import com.aspose.cells.FormatCondition;
import com.aspose.cells.FormatConditionCollection;
import com.aspose.cells.FormatConditionType;
import com.aspose.cells.PageSetup;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Row;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Worksheet;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.residentialtax.InhabitantTaxChecklistBGenerator;
import nts.uk.file.pr.app.export.residentialtax.data.InhabitantTaxChecklistBReport;
import nts.uk.file.pr.app.export.residentialtax.data.InhabitantTaxChecklistBRpData;
import nts.uk.file.pr.app.export.residentialtax.data.InhabitantTaxChecklistBRpHeader;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

@Stateless
public class InhabitantTaxChecklistReportBGenerator extends AsposeCellsReportGenerator
		implements InhabitantTaxChecklistBGenerator {
	
	/** The Constant TEMPLATE_FILE. */
	private static final String TEMPLATE_FILE = "report/qpp011b.xlsx";
	/** The Constant REPORT_FILE_NAME. */
	protected static final String REPORT_FILE_NAME = "テストQPP011.pdf";
	
	@Override
	public void generate(FileGeneratorContext fileContext, InhabitantTaxChecklistBReport dataExport) {
		try {
			AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE);
			
			List<InhabitantTaxChecklistBRpData> reportData = null;
			reportData = new ArrayList<>();
			for(int i = 0; i<10; i++) {
				InhabitantTaxChecklistBRpData r = new InhabitantTaxChecklistBRpData();
				r.setNumberPeople("100" + i);
				r.setResidenceTaxCode("a" + i);
				r.setResiTaxAutonomy("b" + i);
				r.setValue(Double.valueOf("100" + i));
				if (i == 2 || i == 9) {
					r.setTotal(true);
				}
				reportData.add(r);
			}
			
			

			// set datasource
			InhabitantTaxChecklistBRpHeader header = new InhabitantTaxChecklistBRpHeader();
			header.setCompanyName("NITTSU SYSTEM VN");
			header.setDate("2017/05/08");
			header.setLateResiTaxAutonomy("lateResiTaxAutonomy");
			header.setStartResiTaxAutonomy("startResiTaxAutonomy");
			 
			reportContext.setDataSource("header", header);
			reportContext.setDataSource("list", reportData);
			
			PageSetup pageSetup = reportContext.getWorkbook().getWorksheets().get(0).getPageSetup();
			pageSetup.setHeader(0, header.getCompanyName());
			pageSetup.setHeader(2, "&D &T");
						
			// process data binginds in template
			reportContext.getWorkbook().calculateFormula(true);
			reportContext.getDesigner().process(false);
			
			Worksheet worksheet = reportContext.getWorkbook().getWorksheets().get(0);
			
			int startRowIdx = 8;
			int rowColorIdex = 0;
			for (InhabitantTaxChecklistBRpData item : reportData) {
				// Add FormatConditions to the instance of Worksheet
				int index = worksheet.getConditionalFormattings().add();
				
				// Access the newly added FormatConditions via its index
				FormatConditionCollection conditionCollection = worksheet.getConditionalFormattings().get(index);
				
				// Define a CellsArea on which conditional formatting will be applicable
				CellArea area = CellArea.createCellArea("A" +  startRowIdx, "F" + startRowIdx);
	
				// Add area to the instance of FormatConditions
				conditionCollection.addArea(area);
	
				// Add a condition to the instance of FormatConditions. For this case, the condition type is expression, which is based on
				// some formula
				index = conditionCollection.addCondition(FormatConditionType.EXPRESSION);
	
				// Access the newly added FormatCondition via its index
				FormatCondition formatCondirion = conditionCollection.get(index);
	
				// Set the formula for the FormatCondition. Formula uses the Excel's built-in functions as discussed earlier in this
				// article
				formatCondirion.setFormula1("=MOD(ROW(),1)=0");
				
				// Set the background color and patter for the FormatCondition's Style
				formatCondirion.getStyle().setPattern(BackgroundType.SOLID);
				if (!item.isTotal()) {
					if (rowColorIdex%2==0) {
						formatCondirion.getStyle().setBackgroundColor(Color.getWhite());
					} else {
						formatCondirion.getStyle().setBackgroundColor(Color.getBlue());
					}
					rowColorIdex ++;
				}
				
				if (item.isTotal()) {
					rowColorIdex = 0;
					formatCondirion.getStyle().setBackgroundColor(Color.getRed());
				}  
					
				startRowIdx++;
			}
			
			// save as PDF file
			PdfSaveOptions option = new PdfSaveOptions(SaveFormat.PDF);
			option.setAllColumnsInOnePagePerSheet(true);

			reportContext.getWorkbook().save(this.createNewFile(fileContext, REPORT_FILE_NAME), option);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
