/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.accumulatedpayment;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import com.aspose.cells.BackgroundType;
import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.PageSetup;
import com.aspose.cells.Range;
import com.aspose.cells.Style;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import lombok.val;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.time.GeneralDate;
import nts.uk.file.pr.app.export.accumulatedpayment.AccPaymentReportGenerator;
import nts.uk.file.pr.app.export.accumulatedpayment.data.AccPaymentDataSource;
import nts.uk.file.pr.app.export.accumulatedpayment.data.AccPaymentItemData;
import nts.uk.file.pr.app.export.accumulatedpayment.query.AccPaymentReportQuery;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * The Class AsposeAccPaymentReportGenerator.
 */
@Stateless
public class AsposeAccPaymentReportGenerator extends AsposeCellsReportGenerator implements AccPaymentReportGenerator {

	/** The Constant REPORT_FILE_NAME. */
	private static final String REPORT_FILE_NAME = "QET002.xlsx";

	/** The Constant TEMPLATE_FILE. */
	private static final String TEMPLATE_FILE = "report/QET002.xlsx";

	/** The Constant FIRST_ROW_INDEX. */
	private static final int FIRST_ROW_INDEX = 9;

	/** The Constant FIRST_COLUMN. */
	private static final int FIRST_COLUMN = 0;
	
	/** The Constant AMOUNT_COLUMN. */
	private static final int AMOUNT_COLUMN = 7;

	/** The Constant AMOUNT_PER_PAGE. */
	private static final int AMOUNT_PER_PAGE = 46;
	
	/** The Constant AMOUNT_ROWS_IN_PAGE. */
	private static final int AMOUNT_ROWS_IN_PAGE = 60;
	
	/** The Constant SPACES. */
	private static final String SPACES = "             ";
	
	/** The Constant COLUMN_INDEX. */
	private static final int[] COLUMN_INDEX = {0, 1, 2, 3, 4, 5, 6};
	
	/** The Constant ROW_HEIGHT. */
	private static final int ROW_HEIGHT = 28;

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.ctx.pr.screen.app.report.qet002.AccPaymentReportGenerator#generate
	 * (nts.arc.layer.infra.file.export.FileGeneratorContext,
	 * nts.uk.ctx.pr.screen.app.report.qet002.data.AccPaymentDataSource)
	 */
	@Override
	public void generate(FileGeneratorContext generatorContext, 
			AccPaymentDataSource dataSource, AccPaymentReportQuery query) {
		List<AccPaymentItemData> accumulatedPaymentList = dataSource.getAccPaymentItemData();
		try {
			val designer = this.createContext(TEMPLATE_FILE);
			Workbook workbook = designer.getWorkbook();
			WorksheetCollection worksheets = workbook.getWorksheets();
			Worksheet worksheet = worksheets.get(0);
			Cells cells = worksheet.getCells();
			
			// Set header.
			worksheet.getPageSetup().setHeader(2,
					"&\"IPAPGothic\"&13 " + GeneralDate.today().toString() + "\r\n&P ページ");
			designer.getDesigner().setDataSource("Header", dataSource.getHeaderData());
			
			// Fill data
			// Item Data List
			int amountEmployee = accumulatedPaymentList.size();
			int startIndex = 0;
			int firstRowIndex = FIRST_ROW_INDEX;
			int numberOfPage = 0;
			int rangeRows = AMOUNT_PER_PAGE;
			while(amountEmployee > 0){
				int endIndex = startIndex + AMOUNT_PER_PAGE;
				List<AccPaymentItemData> subList = 
						subAccList(accumulatedPaymentList, startIndex, endIndex);				
				
				// Create ranges 
				if(amountEmployee < AMOUNT_PER_PAGE){
					rangeRows= Math.min(amountEmployee, AMOUNT_PER_PAGE);
				}
				createRange(cells, firstRowIndex, rangeRows);
				
				// Print Title 
				printTitleRow(worksheets, firstRowIndex-1);	
				
				// Print content
				createContent(cells, firstRowIndex, subList);
				
				// Reset variables for new page
				amountEmployee -= AMOUNT_PER_PAGE;
				startIndex += AMOUNT_PER_PAGE;
				numberOfPage++;
				firstRowIndex += AMOUNT_ROWS_IN_PAGE;
				
				// Set Print Area				
				PageSetup pageSetup = worksheet.getPageSetup();
				int totalRow = numberOfPage * AMOUNT_ROWS_IN_PAGE;
				pageSetup.setPrintArea("A1:G" + totalRow);				
			}			
			designer.getDesigner().setWorkbook(workbook);
			designer.processDesigner();
			designer.saveAsExcel(this.createNewFile(generatorContext,
					this.getReportName(REPORT_FILE_NAME)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Prints the title row.
	 *
	 * @param worksheets the worksheets
	 * @param rowIndex the row index
	 */
	private void printTitleRow(WorksheetCollection worksheets, int rowIndex){
		Worksheet worksheet = worksheets.get(0);
		Cells cells = worksheet.getCells();
		// Set Row height
		cells.setRowHeightPixel(rowIndex, ROW_HEIGHT);
		
		// Print Employee Header
		Cell empCell = cells.get(rowIndex, COLUMN_INDEX[0]);		
		empCell.setValue("社員");
		
		// Print Tax Amount Header
		Cell taxCell = cells.get(rowIndex, COLUMN_INDEX[1]);		
		taxCell.setValue("課税対象支給額");
		
		// Print Social Insurance Header
		Cell socialInsCell = cells.get(rowIndex, COLUMN_INDEX[2]);		
		socialInsCell.setValue("社会保険合計額");
		
		// Print Amount after tax deduction Header
		Cell afterTaxDeductionCell = cells.get(rowIndex, COLUMN_INDEX[3]);		
		afterTaxDeductionCell.setValue("社保控除後の額");
		
		// Print Witholding Tax Amount Header
		Cell widthHoldingTaxAmCell = cells.get(rowIndex, COLUMN_INDEX[4]);
		widthHoldingTaxAmCell.setValue("源泉徴収税額");
		
		// Print Enrolment Status Header
		Cell enrolmentCell = cells.get(rowIndex, COLUMN_INDEX[5]);
		enrolmentCell.setValue("在籍状態");
		
		// Print Direction Status Header
		Cell directionCell = cells.get(rowIndex, COLUMN_INDEX[6]);
		directionCell.setValue("出向状態");
		
		for (int c: COLUMN_INDEX) {
			Cell cell = cells.get(rowIndex, COLUMN_INDEX[c]);			
			setTitleStyle(cell);
		}
	}
	
	/**
	 * Creates the content.
	 *
	 * @param cells the cells
	 * @param firstRowIndex the first row index
	 * @param accumulatedPaymentList the accumulated payment list
	 */
	private void createContent(Cells cells, int firstRowIndex,
			List<AccPaymentItemData> accumulatedPaymentList){
		for(int i = 0; i < accumulatedPaymentList.size(); i++){
			// Set Row height
			cells.setRowHeightPixel(firstRowIndex, ROW_HEIGHT);
			
			AccPaymentItemData accPayment = accumulatedPaymentList.get(i);			
			// Print Employee Code and Name
			Cell empCell = cells.get(firstRowIndex, COLUMN_INDEX[0]);		
			empCell.setValue(accPayment.getEmpCode() + SPACES + accPayment.getEmpName());
			
			// Print Tax Amount
			Cell taxAmountCell = cells.get(firstRowIndex, COLUMN_INDEX[1]);
			taxAmountCell.setValue(accPayment.getTaxAmount());
			
			// Print Social Insurance 
			Cell socialInsCell = cells.get(firstRowIndex, COLUMN_INDEX[2]);
			socialInsCell.setValue(accPayment.getSocialInsuranceAmount());	
			
			// Print Amount after tax deduction
			double deductedTaxValue = taxAmountCell.getDoubleValue() - socialInsCell.getDoubleValue();
			
			Cell afterTaxDeductionCell = cells.get(firstRowIndex, COLUMN_INDEX[3]);
			afterTaxDeductionCell.setValue(deductedTaxValue);
			
			// Print Witholding Tax Amount
			Cell widthHoldingTaxAmCell = cells.get(firstRowIndex, COLUMN_INDEX[4]);
			widthHoldingTaxAmCell.setValue(accPayment.getWidthHoldingTaxAmount());
			
			// Print Enrolment Status
			Cell enrolmentCell = cells.get(firstRowIndex, COLUMN_INDEX[5]);
			enrolmentCell.setValue(accPayment.getEnrollmentStatus());
			
			// Print Direction Status
			Cell directionCell = cells.get(firstRowIndex, COLUMN_INDEX[6]);
			directionCell.setValue(accPayment.getDirectionalStatus());
			
			// Set Background Color for odd rows
			if ((i % 2) == 1) {
				for(int c: COLUMN_INDEX){
					Cell oddCell = cells.get(firstRowIndex, COLUMN_INDEX[c]);
					setBackgroundcolor(oddCell);
				}
			}
			firstRowIndex ++;
		}
	}

	
	/**
	 * Creates the range.
	 *
	 * @param cells the cells
	 * @param firstRow the first row
	 * @param totalRow the total row
	 */
	private void createRange(Cells cells, int firstRow, int totalRow){
		for(int i = FIRST_COLUMN; i<AMOUNT_COLUMN; i++){
			Range range = cells.createRange(firstRow, i, totalRow, 1);
			range.setOutlineBorders(CellBorderType.THIN, Color.getGray());
		}
	}
	
	/**
	 * Sub acc list.
	 *
	 * @param <T> the generic type
	 * @param list the list
	 * @param fromIndex the from index
	 * @param toIndex the to index
	 * @return the list
	 */
	 protected <T> List<T> subAccList(List<T> list, int fromIndex, int toIndex) {
	  int size = list.size();
	  if (fromIndex >= size || toIndex <= 0 || fromIndex >= toIndex) {
	   return new ArrayList<>();
	  }

	  fromIndex = Math.max(0, fromIndex);
	  toIndex = Math.min(size, toIndex);
	  return list.subList(fromIndex, toIndex);
	 }
	
	/**
	 * Sets the title style.
	 *
	 * @param cell the new title style
	 */
	private void setTitleStyle(Cell cell){
		Style style = cell.getStyle();
		style.setForegroundColor(Color.fromArgb(197, 241, 247));
		style.setPattern(BackgroundType.SOLID);
		style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getGray());
		style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getGray());
		style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getGray());
		style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getGray());
		cell.setStyle(style);
	}
	/**
	 * Sets the backgroundcolor.
	 *
	 * @param cell the new backgroundcolor
	 */
	private void setBackgroundcolor(Cell cell){
		Style style = cell.getStyle();
		style.setForegroundColor(Color.fromArgb(199, 243, 145));
		style.setPattern(BackgroundType.SOLID);
		cell.setStyle(style);
	}
	
}
