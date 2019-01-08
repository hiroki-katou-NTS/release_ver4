package nts.uk.ctx.at.function.infra.generator.annualworkschedule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.OutputAgreementTime;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.PageBreakIndicator;
import nts.uk.ctx.at.function.dom.annualworkschedule.enums.ValueOuputFormat;
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

	private static final String REPORT_FILE_NAME = "年間勤務表.xlsx";

	private static final int MAX_EXPORT_ITEM = 10;
	private static final int ROW_PER_PAGE = 26;
	private static final int ROW_PER_PAGE_7_GROUP_MONTHS = 28;
	/** C2_3 or C2_5 */
	private static final int MAX_GROUP_MONTHS = 7;

	@Override
	public void generate(FileGeneratorContext fileContext, ExportData dataSource) {
		try (AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE)) {
			Workbook wb = reportContext.getWorkbook();
			WorksheetCollection wsc = wb.getWorksheets();
			Worksheet ws = wsc.get(0);
			boolean is7Group = true;
			int pageScale = 95;
			// set A_11
			reportContext.setHeader(0, "&8&\"MS ゴシック\""+dataSource.getHeader().getTitle());
			// set A1_2
			reportContext.setHeader(1, "&16&\"MS ゴシック\"" /* font size */
					+ dataSource.getHeader().getReportName());
			
			// Set header date
			DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d  H:mm", Locale.JAPAN);
			reportContext.setHeader(2, "&8&\"MS ゴシック\"" + LocalDateTime.now().format(fullDateTimeFormatter) + "\npage &P ");
			
			ws.setName(dataSource.getHeader().getReportName());

			// delete superfluous column
			int rowsPerPage = ROW_PER_PAGE_7_GROUP_MONTHS;
			if (dataSource.getHeader().getMonthPeriodLabels() == null
					|| dataSource.getHeader().getMonthPeriodLabels().size() < MAX_GROUP_MONTHS) {
				ws.getCells().deleteColumn(wsc.getRangeByName("monthPeriodLabel7").getFirstColumn());
				is7Group = false;
				pageScale += 3;
				rowsPerPage = ROW_PER_PAGE;
				wsc.getRangeByName("monthPeriod6Range").setOutlineBorder(BorderType.RIGHT_BORDER, CellBorderType.DOTTED,
						Color.getBlack());
			}
			if (!dataSource.isOutNumExceedTime36Agr()) {
				ws.getCells().deleteColumn(wsc.getRangeByName("numExceedTime").getFirstColumn());
				ws.getCells().deleteColumn(wsc.getRangeByName("numRemainingTime").getFirstColumn());
				pageScale += 6;
			}
			PageSetup pageSetup = ws.getPageSetup();
			pageSetup.setZoom(pageScale);

			// delete superfluous rows
			Range empRange = wsc.getRangeByName("employeeRange");
			int rowPerEmp = dataSource.getExportItems().size();
			ws.getCells().deleteRows(empRange.getFirstRow() + empRange.getRowCount() // last
																						// row
					- (MAX_EXPORT_ITEM - rowPerEmp), MAX_EXPORT_ITEM - rowPerEmp);
			// refresh range after delete superfluous rows
			empRange = wsc.getRangeByName("employeeRange");
			Range workplaceRange = wsc.getRangeByName("workplaceRange");
			// set border bottom after delete superfluous rows
			empRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.MEDIUM, Color.getBlack());
			// print header
			this.printHeader(wsc, dataSource.getHeader());

			HorizontalPageBreakCollection pageBreaks = ws.getHorizontalPageBreaks();
			List<String> empIds = dataSource.getEmployeeIds();
			List<ExportItem> itemBooks = dataSource.getExportItems();
			// set first employee
			EmployeeData firstEmp = dataSource.getEmployees().get(empIds.remove(0));

			String workplaceCd = firstEmp.getEmployeeInfo().getWorkplaceCode();
			RangeCustom newRange = new RangeCustom(empRange, 0);
			int offset = 0, sumRowCount = workplaceRange.getRowCount();
			boolean nextWorkplace;
			for (String empId : empIds) {
				EmployeeData emp = dataSource.getEmployees().get(empId);
				nextWorkplace = !workplaceCd.equals(emp.getEmployeeInfo().getWorkplaceCode());

				// check next work place or new page
				if (nextWorkplace || (sumRowCount + empRange.getRowCount() > rowsPerPage)) {
					workplaceCd = emp.getEmployeeInfo().getWorkplaceCode();
					newRange = copyRangeDown(workplaceRange, offset);
				} else {
					newRange = copyRangeDown(empRange, offset);
				}
				sumRowCount += newRange.range.getRowCount();
				// break page and print
				boolean isNewPage = sumRowCount > rowsPerPage
						|| (nextWorkplace && PageBreakIndicator.WORK_PLACE.equals(dataSource.getPageBreak()));
				print(wsc, newRange, emp, isNewPage || nextWorkplace, is7Group, itemBooks,
						dataSource.isOutNumExceedTime36Agr(), dataSource.getHeader());
				if (isNewPage) {
					pageBreaks.add(newRange.range.getFirstRow());
					sumRowCount = newRange.range.getRowCount(); // reset sum row
																// count
				}
				offset = newRange.offset;
			}

			print(wsc, new RangeCustom(empRange, 0), firstEmp, true, is7Group, itemBooks,
					dataSource.isOutNumExceedTime36Agr(), dataSource.getHeader());

			reportContext.processDesigner();
			reportContext.saveAsExcel(this.createNewFile(fileContext, this.getReportName(REPORT_FILE_NAME)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

		if (!PrintFormat.AGREEMENT_36.equals(headerData.getPrintFormat())) {
			return;
		}
		if (OutputAgreementTime.TWO_MONTH.equals(headerData.getOutputAgreementTime())) {
			wsc.getRangeByName("outputAgreementTime").setValue(TextResource.localize("KWR008_48"));
		} else if (OutputAgreementTime.THREE_MONTH.equals(headerData.getOutputAgreementTime())) {
			wsc.getRangeByName("outputAgreementTime").setValue(TextResource.localize("KWR008_49"));
		}

		List<String> monthPeriodLabels = headerData.getMonthPeriodLabels();
		if (monthPeriodLabels != null) {
			for (int i = 0; i < monthPeriodLabels.size(); i++) {
				wsc.getRangeByName("monthPeriodLabel" + (i + 1)).setValue(monthPeriodLabels.get(i));
			}
		}
	}

	/**
	 * @param range
	 * @param emp
	 * @param isNewPage
	 */
	private void print(WorksheetCollection wsc, RangeCustom range, EmployeeData emp, boolean isPrintWorkplace,
			boolean is7Group, List<ExportItem> itemBooks, boolean isOutNumExceedTime36Agr, HeaderData headerData) {
		if (isPrintWorkplace) {
			String workplace = emp.getEmployeeInfo().getWorkplaceName();
			range.cell("workplace").putValue(workplace);
		}
		// print employee info
		if (itemBooks.size() >= 1) {
			range.cell("empName")
					.setValue(emp.getEmployeeInfo().getEmployeeCode() + " " + emp.getEmployeeInfo().getEmployeeName());
		}
		if (itemBooks.size() >= 2) {
			range.cell("employmentName").setValue(emp.getEmployeeInfo().getEmploymentName());
		}
		if (itemBooks.size() >= 3) {
			range.cell("jobTitle").setValue(emp.getEmployeeInfo().getJobTitle());
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
		
			processingPeriod(range, data, rowOffset, headerData);
			if (is7Group) {
				range.cell("period7th", rowOffset, 0).putValue(data.formatMonthPeriod7th());
				this.setCellStyle(range.cell("period7th", rowOffset, 0), data.getColorPeriodMonth7th());
			}
			rowOffset++;
		}
	}


	private RangeCustom copyRangeDown(Range range, int extra) throws Exception {
		Range newRange = range.getWorksheet().getCells().createRange(range.getFirstRow() + range.getRowCount() + extra,
				range.getFirstColumn(), range.getRowCount(), range.getColumnCount());
		newRange.copyStyle(range);
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
	/**
	 * processingPeriod
	 * @param range
	 * @param data
	 * @param rowOffset
	 * @param headerData
	 */
	private void processingPeriod(RangeCustom range, AnnualWorkScheduleData data, int rowOffset,
			HeaderData headerData) {
		range.cell("period1st", rowOffset, 0)
				.putValue(OutputAgreementTime.TWO_MONTH.equals(headerData.getOutputAgreementTime())
						? this.fromMinutesToHHmm(
								data.getMonth1st() != null ? data.getMonth1st().getValue() : BigDecimal.ZERO,
								data.getMonth2nd() != null ? data.getMonth2nd().getValue() : null)
						: this.fromMinutesToHHmm(data.getMonth1st() != null ? data.getMonth1st().getValue() : null,
								data.getMonth2nd() != null ? data.getMonth2nd().getValue() : null,
								data.getMonth3rd() != null ? data.getMonth3rd().getValue() : null));
		this.setCellStyle(range.cell("period1st", rowOffset, 0), data.getColorPeriodMonth1st());
		range.cell("period2nd", rowOffset, 0)
				.putValue(OutputAgreementTime.TWO_MONTH.equals(headerData.getOutputAgreementTime())
						? this.fromMinutesToHHmm(data.getMonth3rd() != null ? data.getMonth3rd().getValue() : null,
								data.getMonth4th() != null ? data.getMonth4th().getValue() : null)
						: this.fromMinutesToHHmm(data.getMonth4th() != null ? data.getMonth4th().getValue() : null,
								data.getMonth5th() != null ? data.getMonth5th().getValue() : null,
								data.getMonth6th() != null ? data.getMonth6th().getValue() : null));
		
		this.setCellStyle(range.cell("period2nd", rowOffset, 0), data.getColorPeriodMonth2nd());
		range.cell("period3rd", rowOffset, 0)
				.putValue(OutputAgreementTime.TWO_MONTH.equals(headerData.getOutputAgreementTime())
						? this.fromMinutesToHHmm(data.getMonth5th() != null ? data.getMonth5th().getValue() : null,
								data.getMonth6th() != null ? data.getMonth6th().getValue() : null)
						: this.fromMinutesToHHmm(data.getMonth7th() != null ? data.getMonth7th().getValue() : null,
								data.getMonth8th() != null ? data.getMonth8th().getValue() : null,
								data.getMonth9th() != null ? data.getMonth9th().getValue() : null));
		this.setCellStyle(range.cell("period3rd", rowOffset, 0), data.getColorPeriodMonth3rd());
		range.cell("period4th", rowOffset, 0)
				.putValue(OutputAgreementTime.TWO_MONTH.equals(headerData.getOutputAgreementTime())
						? this.fromMinutesToHHmm(data.getMonth7th() != null ? data.getMonth7th().getValue() : null,
								data.getMonth8th() != null ? data.getMonth8th().getValue() : null)
						: this.fromMinutesToHHmm(data.getMonth10th() != null ? data.getMonth10th().getValue() : null,
								data.getMonth11th() != null ? data.getMonth11th().getValue() : null,
								data.getMonth12th() != null ? data.getMonth12th().getValue() : null));
		this.setCellStyle(range.cell("period4th", rowOffset, 0), data.getColorPeriodMonth4th());
		range.cell("period5th", rowOffset, 0)
				.putValue(OutputAgreementTime.TWO_MONTH.equals(headerData.getOutputAgreementTime())
						? this.fromMinutesToHHmm(data.getMonth9th() != null ? data.getMonth9th().getValue() : null,
								data.getMonth10th() != null ? data.getMonth10th().getValue() : null)
						: null);
		this.setCellStyle(range.cell("period5th", rowOffset, 0), data.getColorPeriodMonth5th());
		range.cell("period6th", rowOffset, 0)
				.putValue(OutputAgreementTime.TWO_MONTH.equals(headerData.getOutputAgreementTime())
						? this.fromMinutesToHHmm(data.getMonth11th() != null ? data.getMonth11th().getValue() : null,
								data.getMonth12th() != null ? data.getMonth12th().getValue() : null)
						: null);
		this.setCellStyle(range.cell("period6th", rowOffset, 0), data.getColorPeriodMonth6th());
	}

	public String fromMinutesToHHmm(BigDecimal... a) {
		BigDecimal total = BigDecimal.ZERO;
		for (BigDecimal d : a) {
			if (d != null) {
				total = total.add(d);
			}
		}

		if (total.compareTo(BigDecimal.ZERO) > 0) {
			
			int hours = (int) TimeUnit.MINUTES.toHours(total.longValue());
			int remainMinutes = total.intValue() - (int) TimeUnit.HOURS.toMinutes(hours);
			return String.format("%02d:%02d", hours, remainMinutes);

		}
		return "";
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
}