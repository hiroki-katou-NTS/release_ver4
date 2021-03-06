package nts.uk.ctx.at.function.infra.generator.annualworkschedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;

import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Color;
import com.aspose.cells.HorizontalPageBreakCollection;
import com.aspose.cells.PageSetup;
import com.aspose.cells.Range;
import com.aspose.cells.Style;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.MonthsInTotalDisplay;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.PageBreakIndicator;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.AnnualWorkScheduleData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.AnnualWorkScheduleGenerator;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.EmployeeData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExportData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.ExportItem;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.HeaderData;
import nts.uk.ctx.at.function.dom.annualworkschedule.export.PrintFormat;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

@Stateless
public class AnnualWorkScheduleExportGenerator extends AsposeCellsReportGenerator
		implements AnnualWorkScheduleGenerator {

	private static final String TEMPLATE_FILE = "report/年間勤務表.xlsx";

	private static final int MAX_EXPORT_ITEM = 11;
	private static final int ROW_PER_PAGE = 28;	// Except the first 3 rows
	private static final int MAX_PAGE_PER_SHEET = 1000;

	@Override
	public void generate(FileGeneratorContext fileContext, ExportData dataSource) {
		try (AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE)) {
			Workbook wb = reportContext.getWorkbook();
			WorksheetCollection wsc = wb.getWorksheets();
			int sheetIndex = 1;
			this.setupSheet(wsc, dataSource);
			Worksheet ws = this.copySheet(wsc, sheetIndex, dataSource.getHeader().getReportName());

			Range empRange = wsc.getRangeByName("employeeRange");
			Range workplaceRange = wsc.getRangeByName("workplaceRange");

			HorizontalPageBreakCollection pageBreaks = ws.getHorizontalPageBreaks();
			List<String> empIds = dataSource.getEmployeeIds();
			List<ExportItem> itemBooks = dataSource.getExportItems();
			// set first employee
			EmployeeData firstEmp = dataSource.getEmployees().get(empIds.get(0));

			String workplaceCd = firstEmp.getEmployeeInfo().getWorkplaceCode();
			RangeCustom newRange;
			int offset = 0, sumRowCount = 0;
			boolean nextWorkplace;
			boolean isFirstEmp = true;
			boolean isLastEmpOfWorkplace = false;
			for (int i = 0; i < empIds.size(); i++) {
				EmployeeData emp = dataSource.getEmployees().get(empIds.get(i));
				EmployeeData empNext = i < (empIds.size() - 1) ? dataSource.getEmployees().get(empIds.get(i + 1)) : null;
				nextWorkplace = !workplaceCd.equals(emp.getEmployeeInfo().getWorkplaceCode());
				isLastEmpOfWorkplace = empNext != null
									? !empNext.getEmployeeInfo().getWorkplaceCode().equals(emp.getEmployeeInfo().getWorkplaceCode())
									: true;

				if (isFirstEmp) {
					newRange = new RangeCustom(wsc.getRangeByName(ws.getName() + "!workplaceRange"), 0);
				} else if (nextWorkplace || (sumRowCount + empRange.getRowCount() > ROW_PER_PAGE)) {
					// check next work place or new page
					workplaceCd = emp.getEmployeeInfo().getWorkplaceCode();
					newRange = copyRangeDown(ws, workplaceRange, offset, isLastEmpOfWorkplace, true);
				} else {
					newRange = copyRangeDown(ws, empRange, offset, isLastEmpOfWorkplace, false);
				}

				sumRowCount += newRange.range.getRowCount();
				// break page and print
				boolean isNewPage = sumRowCount > ROW_PER_PAGE
						|| (nextWorkplace && PageBreakIndicator.WORK_PLACE.equals(dataSource.getPageBreak()));
				if (isNewPage) {
					// check to next sheet
					pageBreaks.add(newRange.range.getFirstRow());
					if (pageBreaks.getCount() == MAX_PAGE_PER_SHEET) {
						// delete range of last page
						newRange.delete();
						// add sheet
						sheetIndex++;
						ws = this.copySheet(wsc, sheetIndex, dataSource.getHeader().getReportName());
						// first range of sheet
						newRange = new RangeCustom(wsc.getRangeByName(ws.getName() + "!workplaceRange"), 0);
						pageBreaks = ws.getHorizontalPageBreaks();
					}
					// reset sum row count
					sumRowCount = newRange.range.getRowCount();
				}
				print(newRange, emp, isFirstEmp || isNewPage || nextWorkplace, itemBooks,
						dataSource.isOutNumExceedTime36Agr());
				offset = newRange.offset;
				isFirstEmp = false;
			}

			// delete temp sheet
			wsc.removeAt(0);

			// focus first sheet
			wsc.setActiveSheetIndex(0);
			
			// Set scaling
			int sheetCount = wsc.getCount();
			for (int i = 0; i < sheetCount; i++) {
				Worksheet sheet = wsc.get(i);
				sheet.getPageSetup().setPercentScale(true);
				sheet.getPageSetup().setZoom(100);
			}
			
			// Get current date and format it
			DateTimeFormatter jpFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.JAPAN);
			String currentFormattedDate = LocalDateTime.now().format(jpFormatter);

			reportContext.processDesigner();
			// Update #119036
			reportContext.getWorkbook().getBuiltInDocumentProperties().setAuthor("Kinjirou");
			reportContext.getWorkbook().getBuiltInDocumentProperties().setLastSavedBy("");
			reportContext.saveAsExcel(this.createNewFile(fileContext, dataSource.getReportName() + "_" + currentFormattedDate + ".xlsx"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void setupSheet(WorksheetCollection wsc, ExportData dataSource) {
		Worksheet ws = wsc.get(0);
		PageSetup pageSetup = ws.getPageSetup();
		int pageScale = 98;
		// set A_11
		pageSetup.setHeader(0, "&9&\"MS ゴシック\"" + dataSource.getHeader().getTitle());
		// set A1_2
		pageSetup.setHeader(1, "&16&\"MS ゴシック,Bold\"" /* font size */
				+ dataSource.getHeader().getReportName());
		// Set header date
		DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d  H:mm", Locale.JAPAN);
		pageSetup.setHeader(2, "&9&\"MS ゴシック\"" + LocalDateTime.now().format(fullDateTimeFormatter) + "\npage &P ");

		if (!dataSource.isOutNumExceedTime36Agr()) {
			ws.getCells().deleteColumn(wsc.getRangeByName("numExceedTime").getFirstColumn());
			ws.getCells().deleteColumn(wsc.getRangeByName("numRemainingTime").getFirstColumn());
			pageScale += 6;
		}
		pageSetup.setZoom(pageScale);
		pageSetup.setPrintArea("A1:W");

		Range empRange = wsc.getRangeByName("employeeRange");
		int rowPerEmp = dataSource.getExportItems().size();
		ws.getCells().deleteRows(empRange.getFirstRow() + empRange.getRowCount() // last
				// row
				- (MAX_EXPORT_ITEM - rowPerEmp), MAX_EXPORT_ITEM - rowPerEmp);
		// refresh range after delete superfluous rows
		empRange = wsc.getRangeByName("employeeRange");
		// set border bottom after delete superfluous rows
		empRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
		// print header
		this.printHeader(wsc, dataSource.getHeader());

		// select range
		Range headerRange = wsc.getRangeByName("headerRange");
		ws.selectRange(headerRange.getFirstRow(), headerRange.getFirstColumn(), headerRange.getRowCount(), headerRange.getColumnCount(), true);
	}

	private Worksheet copySheet(WorksheetCollection wsc, int sheetIndex, String reportName) throws Exception {
		wsc.addCopy(0);
		Worksheet ws = wsc.get(sheetIndex);
		// sheet name
		String sheetName = sheetIndex == 1 ? reportName : reportName + sheetIndex;
		ws.setName(sheetName);
		return ws;
	}

	/**
	 * @param wsc
	 *            Work sheet collection
	 */
	private void printHeader(WorksheetCollection wsc, HeaderData headerData) {
		// print C1_2
		List<String> months = headerData.getMonths();
		wsc.getRangeByName("month1stLabel").setValue(months.get(0));
		wsc.getRangeByName("month2ndLabel").setValue(months.get(1));
		wsc.getRangeByName("month3rdLabel").setValue(months.get(2));
		wsc.getRangeByName("month4thLabel").setValue(months.get(3));
		wsc.getRangeByName("month5thLabel").setValue(months.get(4));
		wsc.getRangeByName("month6thLabel").setValue(months.get(5));
		wsc.getRangeByName("month7thLabel").setValue(months.get(6));
		wsc.getRangeByName("month8thLabel").setValue(months.get(7));
		wsc.getRangeByName("month9thLabel").setValue(months.get(8));
		wsc.getRangeByName("month10thLabel").setValue(months.get(9));
		wsc.getRangeByName("month11thLabel").setValue(months.get(10));
		wsc.getRangeByName("month12thLabel").setValue(months.get(11));
		// print B1_1 + B1_2
		wsc.getRangeByName("period").setValue(headerData.getPeriod());
		// print C1_1
		wsc.getRangeByName("empInfoLabel").setValue(headerData.getEmpInfoLabel());
		wsc.getRangeByName("itemName").setValue(TextResource.localize("KWR008_106"));

		if (!PrintFormat.AGREEMENT_36.equals(headerData.getPrintFormat())) {
			return;
		}
		if (headerData.getMonthsInTotalDisplay().isPresent()) {
			if (MonthsInTotalDisplay.TWO_MONTH.equals(headerData.getMonthsInTotalDisplay().get())) {
				wsc.getRangeByName("outputAgreementTime").setValue(TextResource.localize("KWR008_48"));
			} else if (MonthsInTotalDisplay.THREE_MONTH.equals(headerData.getMonthsInTotalDisplay().get())) {
				wsc.getRangeByName("outputAgreementTime").setValue(TextResource.localize("KWR008_49"));
			}
		}
		if (headerData.isMaximumAgreementTime()) {
			wsc.getRangeByName("outputAgreementTime").setValue(TextResource.localize("KWR008_66"));
		}

		List<String> monthPeriodLabels = headerData.getMonthPeriodLabels();
		if (monthPeriodLabels != null) {
			for (int i = 0; i < monthPeriodLabels.size(); i++) {
				wsc.getRangeByName("monthPeriodLabel" + (i + 1)).setValue(monthPeriodLabels.get(i));
			}
		}
	}

	private void print(RangeCustom range, EmployeeData emp, boolean isPrintWorkplace,
			List<ExportItem> itemBooks, boolean isOutNumExceedTime36Agr) {
		if (isPrintWorkplace) {
			String workplace = TextResource.localize("KWR008_50")		// D1_1
							 + emp.getEmployeeInfo().getWorkplaceCode() // D1_3
						   	 + "　"
						   	 + emp.getEmployeeInfo().getWorkplaceName(); // D1_2
			range.cell("workplace").putValue(workplace);
		}
		// print employee info
		if (itemBooks.size() >= 1) {
			range.cell("empName").setValue(
					emp.getEmployeeInfo().getEmployeeCode()		// D2_1
					+ " "
					+ emp.getEmployeeInfo().getEmployeeName()); // D2_2
		}
		if (itemBooks.size() >= 2) {
			range.cell("employmentName").setValue(
					emp.getEmployeeInfo().getEmploymentCode() 	// D2_6
					+ " "
					+ emp.getEmployeeInfo().getEmploymentName() // D2_3
			);
		}
		if (itemBooks.size() >= 3) {
			range.cell("jobTitle").setValue(
					emp.getEmployeeInfo().getJobTitleCode()		// D2_7
					+ " "
					+ emp.getEmployeeInfo().getJobTitle()		// D2_4
			);
		}

		int rowOffset = 0;
		for (ExportItem itemBook : itemBooks) {
			range.cell("item", rowOffset, 0).putValue(itemBook.getHeadingName());
			AnnualWorkScheduleData data = emp.getAnnualWorkSchedule() == null ? null
					: emp.getAnnualWorkSchedule().get(itemBook.getCd());
			if (data == null) {
				rowOffset++;
				continue;
			}

			range.cell("month1st", rowOffset, 0).putValue(data.formatMonth1st());
			this.setCellStyle(range.cell("month1st", rowOffset, 0), data.getColorMonth1st());
			range.cell("month2nd", rowOffset, 0).putValue(data.formatMonth2nd());
			this.setCellStyle(range.cell("month2nd", rowOffset, 0), data.getColorMonth2nd());
			range.cell("month3rd", rowOffset, 0).putValue(data.formatMonth3rd());
			this.setCellStyle(range.cell("month3rd", rowOffset, 0), data.getColorMonth3rd());
			range.cell("month4th", rowOffset, 0).putValue(data.formatMonth4th());
			this.setCellStyle(range.cell("month4th", rowOffset, 0), data.getColorMonth4th());
			range.cell("month5th", rowOffset, 0).putValue(data.formatMonth5th());
			this.setCellStyle(range.cell("month5th", rowOffset, 0), data.getColorMonth5th());
			range.cell("month6th", rowOffset, 0).putValue(data.formatMonth6th());
			this.setCellStyle(range.cell("month6th", rowOffset, 0), data.getColorMonth6th());
			range.cell("month7th", rowOffset, 0).putValue(data.formatMonth7th());
			this.setCellStyle(range.cell("month7th", rowOffset, 0), data.getColorMonth7th());
			range.cell("month8th", rowOffset, 0).putValue(data.formatMonth8th());
			this.setCellStyle(range.cell("month8th", rowOffset, 0), data.getColorMonth8th());
			range.cell("month9th", rowOffset, 0).putValue(data.formatMonth9th());
			this.setCellStyle(range.cell("month9th", rowOffset, 0), data.getColorMonth9th());
			range.cell("month10th", rowOffset, 0).putValue(data.formatMonth10th());
			this.setCellStyle(range.cell("month10th", rowOffset, 0), data.getColorMonth10th());
			range.cell("month11th", rowOffset, 0).putValue(data.formatMonth11th());
			this.setCellStyle(range.cell("month11th", rowOffset, 0), data.getColorMonth11th());
			range.cell("month12th", rowOffset, 0).putValue(data.formatMonth12th());
			this.setCellStyle(range.cell("month12th", rowOffset, 0), data.getColorMonth12th());
			range.cell("average", rowOffset, 0).putValue(data.formatAverage());
			range.cell("sum", rowOffset, 0).putValue(data.formatSum());
			this.setCellStyle(range.cell("sum", rowOffset, 0), data.getColorSum());
			if (isOutNumExceedTime36Agr && data.isAgreementTime()) {
				range.cell("numExceedTime", rowOffset, 0).putValue(data.formatMonthsExceeded());
				range.cell("numRemainingTime", rowOffset, 0).putValue(data.formatMonthsRemaining());
			}
		
			range.cell("period1st", rowOffset, 0).putValue(data.formatMonthPeriod1st());
			this.setCellStyle(range.cell("period1st", rowOffset, 0), data.getColorPeriodMonth1st());
			range.cell("period2nd", rowOffset, 0).putValue(data.formatMonthPeriod2nd());
			this.setCellStyle(range.cell("period2nd", rowOffset, 0), data.getColorPeriodMonth2nd());
			range.cell("period3rd", rowOffset, 0).putValue(data.formatMonthPeriod3rd());
			this.setCellStyle(range.cell("period3rd", rowOffset, 0), data.getColorPeriodMonth3rd());
			range.cell("period4th", rowOffset, 0).putValue(data.formatMonthPeriod4th());
			this.setCellStyle(range.cell("period4th", rowOffset, 0), data.getColorPeriodMonth4th());
			range.cell("period5th", rowOffset, 0).putValue(data.formatMonthPeriod5th());
			this.setCellStyle(range.cell("period5th", rowOffset, 0), data.getColorPeriodMonth5th());
			range.cell("period6th", rowOffset, 0).putValue(data.formatMonthPeriod6th());
			this.setCellStyle(range.cell("period6th", rowOffset, 0), data.getColorPeriodMonth6th());
			rowOffset++;
		}
	}


	private RangeCustom copyRangeDown(Worksheet ws, Range range, int extra, boolean isLastEmp, boolean isWorkplace) throws Exception {
		Range newRange = ws.getCells().createRange(range.getFirstRow() + range.getRowCount() + extra,
				range.getFirstColumn(), range.getRowCount(), range.getColumnCount());
		newRange.copyStyle(range);
		newRange.setOutlineBorder(BorderType.BOTTOM_BORDER, isLastEmp || isWorkplace ? CellBorderType.MEDIUM : CellBorderType.THIN,
				Color.getBlack());
		newRange.setOutlineBorder(BorderType.TOP_BORDER, isWorkplace ? CellBorderType.MEDIUM : CellBorderType.THIN, Color.getBlack());
		int offset = newRange.getFirstRow() - range.getFirstRow();
		return new RangeCustom(newRange, offset);
	}

	private void setCellStyle(Cell cell, Integer color) {
		if (color == null)
			return;
		Style style = cell.getStyle();
		style.setForegroundArgbColor(color);
		cell.setStyle(style);
	}
}

class RangeCustom {
	int index;
	final int offset;
	Range range;
	Worksheet worksheet;
	WorksheetCollection worksheets;

	public RangeCustom(Range range, int offset) {
		this.offset = offset;
		this.range = range;
		this.worksheet = range.getWorksheet();
		this.worksheets = this.worksheet.getWorkbook().getWorksheets();
	}

	public RangeCustom(Range range, int offset, int index) {
		this.index = index;
		this.offset = offset;
		this.range = range;
		this.worksheet = range.getWorksheet();
		this.worksheets = this.worksheet.getWorkbook().getWorksheets();
	}

	public Cell cell(String name) {
		Cell orginCell = worksheets.getRangeByName(name).getCellOrNull(0, 0);
		return worksheet.getCells().get(orginCell.getRow() + offset, orginCell.getColumn());
	}

	public Cell cell(String name, int rowOffset, int columnOffset) {
		Cell orginCell = worksheets.getRangeByName(name).getCellOrNull(0, 0);
		return worksheet.getCells().get(orginCell.getRow() + offset + rowOffset, orginCell.getColumn() + columnOffset);
	}

	public Cell firstCell() {
		return range.getCellOrNull(0, 0);
	}

	public void delete(){
		worksheet.getCells().deleteRows(this.range.getFirstRow(), this.range.getRowCount());
	}
}