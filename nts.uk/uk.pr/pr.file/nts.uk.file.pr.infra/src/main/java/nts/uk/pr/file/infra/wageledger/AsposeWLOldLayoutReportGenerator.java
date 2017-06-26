/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.wageledger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.aspose.cells.StyleFlag;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Worksheet;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.wageledger.WLOldLayoutReportGenerator;
import nts.uk.file.pr.app.export.wageledger.WageLedgerReportQuery;
import nts.uk.file.pr.app.export.wageledger.data.WLOldLayoutReportData;
import nts.uk.file.pr.app.export.wageledger.data.oldlayout.DeductionData;
import nts.uk.file.pr.app.export.wageledger.data.oldlayout.PaymentData;
import nts.uk.file.pr.app.export.wageledger.data.share.HeaderReportData;
import nts.uk.file.pr.app.export.wageledger.data.share.MonthlyData;
import nts.uk.file.pr.app.export.wageledger.data.share.ReportItemDto;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;

/**
 * The Class AsposeWLOldLayoutReportGenerator.
 */
@Stateless
public class AsposeWLOldLayoutReportGenerator extends WageLedgerBaseGenerator implements WLOldLayoutReportGenerator {
	
	/** The Constant TEMPLATE_FILE. */
	private static final String TEMPLATE_FILE = "report/QET001_1.xlsx";
	
	/** The Constant ROW_START_REPORT. */
	private static final int ROW_START_REPORT = 5;
	
	/** The Constant COLUMN_START_REPORT. */
	private static final int COLUMN_START_REPORT = 0;
	
	/** The Constant MAX_ROW_ON_ONE_PAGE. */
	private static final int MAX_ROW_ON_ONE_PAGE = 47;
	
	/** The Constant MAX_RECORD_ON_ONE_PAGE. */
	private static final int MAX_RECORD_ON_ONE_PAGE = 35;

	/* (non-Javadoc)
	 * @see nts.uk.file.pr.app.export.wageledger.WageLedgerReportGenerator
	 * #generate(nts.arc.layer.infra.file.export.FileGeneratorContext,
	 *  nts.uk.file.pr.app.export.wageledger.data.WageLedgerReportData)
	 */
	@Override
	public void generate(FileGeneratorContext fileContext, List<WLOldLayoutReportData> reportDatas,
			WageLedgerReportQuery query) {
		// Generate report.
		final List<AsposeCellsReportContext> reportFiles = reportDatas.stream()
				.map(data -> this.generateReport(data))
				.collect(Collectors.toList());
		
		// Save report to file PDF.
		try {
			AsposeCellsReportContext reportContext = reportFiles.get(0);

			// If have 2 or more report -> combine to 1 workbook.
			if (reportFiles.size() > 1) {
				for (int i = 1; i < reportFiles.size(); i++) {
					reportContext.getWorkbook().combine(reportFiles.get(i).getWorkbook());
				}
			}

			// Save to PDF.
			reportContext.saveAsPdf(this.createNewFile(fileContext, this.getReportName(REPORT_FILE_NAME)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Generate report.
	 *
	 * @param fileContext the file context
	 * @param reportData the report data
	 * @param query the query
	 * @return the aspose cells report context
	 */
	private AsposeCellsReportContext generateReport(WLOldLayoutReportData reportData) {
		try (AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE)) {
			
			Worksheet ws = reportContext.getDesigner().getWorkbook().getWorksheets().get(0);
			HeaderReportData headerData = reportData.headerData;
			
			// Create Print Data.
			PrintData printData = new PrintData();
			printData.amountItemLeftOnCurrentPage = MAX_RECORD_ON_ONE_PAGE;
			printData.currentColumn = COLUMN_START_REPORT;
			printData.currentRow = ROW_START_REPORT;
			printData.reportData = reportData;
			printData.reportContext = reportContext;
			printData.isSalaryPath = true;

			// ======================== Fill header data.========================
			this.fillHeaderData(reportContext, headerData);
			
			// ======================== Fill Salary payment content.========================
			printData.headerLabel = "【給与支給】";
			this.fillSalaryOrBonusHeaderTable(printData);
			this.fillPaymentDataContentOldLayout(reportData.salaryPaymentData, printData);
			this.breakPage(printData);
			
			// ======================== Fill Salary deduction, ========================
			// ======================== salary attendance content and net salary.========================
			this.fillDeductionAndAttendanceDataContentOldLayout(printData);
			// Create end line of salary part.
			Range salaryEndRowRange = ws.getCells().createRange(printData.currentRow - 1, COLUMN_START_REPORT + 2, 1,
					reportData.salaryMonthList.size() + 1);
			salaryEndRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
			this.breakPage(printData);
			
			// ======================== Fill Bonus payment content.========================
			printData.isSalaryPath = false;
			printData.headerLabel = "【賞与支給】";
			printData.currentColumn = COLUMN_START_REPORT;
			this.fillSalaryOrBonusHeaderTable(printData);
			this.fillPaymentDataContentOldLayout(reportData.bonusPaymentData, printData);
			this.breakPage(printData);
			
			// ======================== Fill Bonus deduction, bonus attendance content and total bonus. ===============
			this.fillDeductionAndAttendanceDataContentOldLayout(printData);
			// Create end line of bonus part.
			Range bonusEndRowRange = ws.getCells().createRange(printData.currentRow - 1, COLUMN_START_REPORT + 2, 1,
					reportData.bonusMonthList.size() + 1);
			bonusEndRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
			
			// Set print area.
			PageSetup pageSetup = ws.getPageSetup();
			Cell endCell = ws.getCells().get(printData.currentRow - 1, 14);
			pageSetup.setPrintArea("A1:" + endCell.getName());
			
			// process data binginds in template.
			reportContext.getDesigner().getWorkbook().calculateFormula(true);
			reportContext.getDesigner().process(false);

			// save as PDF file
			return reportContext;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Fill payment data content old layout.
	 *
	 * @param reportContext the report context
	 * @param paymentData the payment data
	 * @param startRow the start row
	 * @param monthList the month list
	 */
	private void fillPaymentDataContentOldLayout(PaymentData paymentData, PrintData printData) {
		List<ReportItemDto> reportItems = paymentData.aggregateItemList;
		reportItems.add(paymentData.totalTax);
		reportItems.add(paymentData.totalTaxExemption);
		reportItems.add(paymentData.totalSalaryPayment);
		
		// Fill item list.
		printData.headerLabel = "支給";
		this.fillReportItemsData(reportItems, printData);
	}
	
	/**
	 * Fill deduction and attendance data content old layout.
	 *
	 * @param reportContext the report context
	 * @param deductionData the deduction data
	 * @param totalData the total data
	 * @param attendanceData the attendance data
	 * @param startRow the start row
	 * @param monthList the month list
	 */
	private void fillDeductionAndAttendanceDataContentOldLayout(PrintData printData) {
		WLOldLayoutReportData reportData = printData.reportData;
		// Fill Deduction data.
		DeductionData deductionData = printData.isSalaryPath ? reportData.salaryDeductionData
				: reportData.bonusDeductionData;
		List<ReportItemDto> reportItems = deductionData.aggregateItemList;
		reportItems.add(deductionData.totalDeduction);
		printData.headerLabel = "控除";
		this.fillReportItemsData(reportItems, printData);
		
		// Fill total item.
		ReportItemDto totalItem = printData.isSalaryPath ? reportData.netSalaryData
				: reportData.totalBonusData;
		printData.currentColumn = COLUMN_START_REPORT;
		printData.isPrintTotalItem = true;
		this.fillItemData(totalItem, printData);
		printData.isPrintTotalItem = false;
		
		// Fill Attendance Data.
		List<ReportItemDto> attendanceItems = printData.isSalaryPath ? reportData.salaryAttendanceDatas
				: reportData.bonusAttendanceDatas;
		printData.headerLabel = "勤怠";
		this.fillReportItemsData(attendanceItems, printData);
	}
	
	/**
	 * Fill report items data.
	 *
	 * @param reportContext the report context
	 * @param reportItems the report items
	 * @param startRow the start row
	 * @param contentName the content name
	 * @param monthList the month list
	 */
	private void fillReportItemsData(List<ReportItemDto> reportItems, PrintData printData) {
		reportItems = reportItems.stream().filter(item -> item.isShow()).collect(Collectors.toList());
		if (reportItems.size() == 0) {
			return;
		}
		Worksheet ws = printData.reportContext.getDesigner().getWorkbook().getWorksheets().get(0);
		Cells cells = ws.getCells();
		int totalItemData = reportItems.size();
		int fromIndex = 0;
		
		while (totalItemData > 0) {
			int amountItemOnPage = Math.min(printData.amountItemLeftOnCurrentPage, MAX_RECORD_ON_ONE_PAGE);
			int toIndex = fromIndex + amountItemOnPage;
			List<ReportItemDto> items = this.safeSubList(reportItems, fromIndex, toIndex);
			int currentColumn = COLUMN_START_REPORT;
			
			// Create line start page.
			int amountMonth = printData.isSalaryPath ? printData.reportData.salaryMonthList.size()
					: printData.reportData.bonusMonthList.size();
			Range startRowRange = cells.createRange(printData.currentRow, COLUMN_START_REPORT + 2, 1, amountMonth + 1);
			startRowRange.setOutlineBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
			
			// Fill Payment label cell.
			if (printData.headerLabel != null) {
				int totalRow = items.size();
				Range paymentLabelRange = cells.createRange(printData.currentRow, 0, totalRow, 1);
				paymentLabelRange.get(0, 0).setValue(printData.headerLabel);
				paymentLabelRange.merge();
				this.setStyleRange(paymentLabelRange, null);
				currentColumn++;
			}
			
			// Fill report item list.
			for (int i = 0; i < items.size(); i++) {
				ReportItemDto item = items.get(i);
				
				// Fill items
				printData.isPrintTotalItem = false;
				printData.currentColumn = currentColumn;
				this.fillItemData(item, printData);
			}
			
			// Calculate to next page.
			totalItemData -= MAX_RECORD_ON_ONE_PAGE;
			fromIndex += MAX_RECORD_ON_ONE_PAGE;
		}
		
	}
	
	/**
	 * Fill item data.
	 *
	 * @param reportContext the report context
	 * @param item the item
	 * @param startRow the start row
	 * @param startColumn the start column
	 * @param isTotalData the is total data
	 * @param monthList the month list
	 */
	private void fillItemData(ReportItemDto item, PrintData printData) {
		Worksheet ws = printData.reportContext.getDesigner().getWorkbook().getWorksheets().get(0);
		Cells cells = ws.getCells();
		Color backgroundColor = printData.isGreenRow ? GREEN_COLOR : null;
		printData.isGreenRow = !printData.isGreenRow;
		
		// Fill item name cell.
		int amountColumn = printData.isPrintTotalItem ? 2 : 1;
		Color nameColor = printData.isPrintTotalItem ? GREEN_COLOR : backgroundColor;
		Range nameCell = cells.createRange(printData.currentRow, printData.currentColumn, 1, amountColumn);
		nameCell.get(0, 0).setValue(!item.isShowName && item.isZeroValue() ? "" : item.name);
		nameCell.merge();
		this.setStyleRange(nameCell, nameColor);
		printData.currentColumn += amountColumn;
		
		// Fill item data cells.
		Map<Integer, MonthlyData> dataMap = item != null ? item.monthlyDatas.stream()
				.collect(Collectors.toMap(d -> d.month, Function.identity())) : new HashMap<>();
		List<Integer> monthList = printData.isSalaryPath ? printData.reportData.salaryMonthList
						: printData.reportData.bonusMonthList;
		boolean isShowValue = !item.isZeroValue() || item.isShowValue;
		for (int j = 0; j < monthList.size(); j++) {
			MonthlyData data = dataMap.get(monthList.get(j));
			Cell monthCell = cells.get(printData.currentRow, printData.currentColumn);
			// Check show value.
			if (isShowValue) {
				monthCell.setValue(data != null ? data.amount : 0);
			}

			// Set style for cell.
			StyleModel dataCellStyle = StyleModel
					.createItemCellStyle(backgroundColor, j != item.monthlyDatas.size() - 1);
			this.setStyleCell(monthCell, dataCellStyle);

			// Next column.
			printData.currentColumn++;
		}

		// Fill Total Cell.
		Cell totalCell = cells.get(printData.currentRow, printData.currentColumn);
		if (isShowValue) {
			item.calculateTotal();
			totalCell.setValue(item.getTotal());
		}
		StyleModel totalCellStyle = StyleModel.createTotalCellStyle(backgroundColor);
		this.setStyleCell(totalCell, totalCellStyle);
		
		// Next row.
		printData.currentRow++;
		// Calculate amount item left on page.
		printData.amountItemLeftOnCurrentPage--;
		
		// Check row is last of page.
		if (printData.amountItemLeftOnCurrentPage == 0) {
			this.breakPage(printData);
		}
	}
	
	/**
	 * Break page.
	 *
	 * @param reportContext the report context
	 * @param currentRow the current row
	 * @param amountMonth the amount month
	 */
	private void breakPage(PrintData printData) {
		Worksheet ws = printData.reportContext.getDesigner().getWorkbook().getWorksheets().get(0);
		Cells cells = ws.getCells();
		int amountMonth = printData.isSalaryPath ? printData.reportData.salaryMonthList.size()
				: printData.reportData.bonusMonthList.size();
		// Create line end page.
		Range endRowRange = cells.createRange(printData.currentRow - 1, COLUMN_START_REPORT + 2, 1, amountMonth + 1);
		endRowRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());

		int currentPage = printData.currentRow / MAX_ROW_ON_ONE_PAGE + 1;
		printData.currentRow = currentPage * MAX_ROW_ON_ONE_PAGE + ROW_START_REPORT;
		printData.amountItemLeftOnCurrentPage = MAX_RECORD_ON_ONE_PAGE;
	}
	
	
	/**
	 * Fill salary or bonus header table.
	 *
	 * @param reportContext the report context
	 * @param startRow the start row
	 * @param monthList the month list
	 * @param contentLabel the content label
	 */
	private void fillSalaryOrBonusHeaderTable(PrintData printData) {
		Worksheet ws = printData.reportContext.getDesigner().getWorkbook().getWorksheets().get(0);
		Cells cells = ws.getCells();
		int startColumn = printData.currentColumn;
		
		// Create Salary label cell.
		Range salaryLabelRange = cells.createRange(--printData.currentRow, startColumn, 1, 2);
		salaryLabelRange.get(0, 0).setValue(printData.headerLabel);
		salaryLabelRange.merge();
		this.setStyleRange(salaryLabelRange, BLUE_COLOR);
		startColumn += 2;
		
		// Create 12 month cells.
		List<Integer> monthList = printData.isSalaryPath ? printData.reportData.salaryMonthList
				: printData.reportData.bonusMonthList;
		for (int i = 0; i < monthList.size(); i++) {
			Cell monthCell = cells.get(printData.currentRow, startColumn);
			monthCell.setValue(monthList.get(i) + " 月");
			this.setStyleCell(monthCell, StyleModel.createHeaderCellStyle(false));
			startColumn++;
		}
		
		// Create Total cell.
		Cell totalCell = cells.get(printData.currentRow, startColumn);
		totalCell.setValue("合計");
		this.setStyleCell(totalCell, StyleModel.createHeaderCellStyle(true));
		
		// Next row.
		printData.currentRow++;
	}
	
	/**
	 * Sets the style range.
	 *
	 * @param range the range
	 * @param backgroundColor the background color
	 */
	private void setStyleRange(Range range, Color backgroundColor) {
		Style style = new Style();
		style.setTextWrapped(true);
		
		// Setting the line style of the top border
		style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
		// Setting the line style of the bottom border
		style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
		// Setting the line style of the left border
		style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
		// Setting the line style of the right border
		style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
		
		// Set background Color.
		if (backgroundColor != null) {
			style.setForegroundColor(backgroundColor);
			style.setPattern(BackgroundType.SOLID);
		}
		
		// Set Alignment.
		style.setVerticalAlignment(TextAlignmentType.CENTER);
		style.setHorizontalAlignment(TextAlignmentType.CENTER);
		
		// Style flag.
		StyleFlag styleFlag = new StyleFlag();
		styleFlag.setCellShading(true);
		styleFlag.setBorders(true);
		styleFlag.setHorizontalAlignment(true);
		styleFlag.setVerticalAlignment(true);
		styleFlag.setWrapText(true);
		range.applyStyle(style, styleFlag);
	}
	
	/**
	 * The Class PrintData.
	 */
	private class PrintData {
		
		/** The report context. */
		AsposeCellsReportContext reportContext;
		
		/** The report data. */
		WLOldLayoutReportData reportData;
		
		/** The current row. */
		int currentRow;
		
		/** The current column. */
		int currentColumn;
		
		/** The amount item left on current page. */
		int amountItemLeftOnCurrentPage;
		
		/** The is green row. */
		boolean isGreenRow;
		
		/** The is salary path. */
		boolean isSalaryPath;
		
		/** The is total item. */
		boolean isPrintTotalItem;
		
		/** The header label. */
		String headerLabel;
	}
}
