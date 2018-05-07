package nts.uk.ctx.at.function.infra.repository.holidaysremaining.report;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import com.aspose.cells.BackgroundType;
import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.Shape;
import com.aspose.cells.Style;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import lombok.val;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.ctx.at.function.dom.holidaysremaining.report.HolidayRemainingDataSource;
import nts.uk.ctx.at.function.dom.holidaysremaining.report.HolidaysRemainingEmployee;
import nts.uk.ctx.at.function.dom.holidaysremaining.report.HolidaysRemainingReportGenerator;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

@Stateless
public class HolidaysRemainingReportGeneratorImp extends AsposeCellsReportGenerator
		implements HolidaysRemainingReportGenerator {

	private static final String TEMPLATE_FILE = "report/休暇残数管理票_テンプレート.xlsx";
	private static final String REPORT_FILE_NAME = "休暇残数管理票.xlsx";
	private final int numberRowOfPage = 37;
	// private final int minRowDetails = 4;
	private HolidayRemainingDataSource dataSource;

	@Override
	public void generate(FileGeneratorContext generatorContext, HolidayRemainingDataSource dataSource) {
		try (val reportContext = this.createContext(TEMPLATE_FILE)) {
			this.dataSource = dataSource;
			val designer = this.createContext(TEMPLATE_FILE);
			Workbook workbook = designer.getWorkbook();
			WorksheetCollection worksheets = workbook.getWorksheets();
			Worksheet worksheet = worksheets.get(0);

			printTemplate(worksheet);

			if (dataSource.getPageBreak() == 0) {
				printNoneBreakPage(worksheet);
			} else if (dataSource.getPageBreak() == 1) {
				printWorkplaceBreakPage(worksheet);
			} else if (dataSource.getPageBreak() == 2) {
				printPersonBreakPage(worksheet);
			}

			removeTemplate(worksheet);

			designer.getDesigner().setWorkbook(workbook);
			designer.processDesigner();

			designer.saveAsExcel(this.createNewFile(generatorContext, this.getReportName(REPORT_FILE_NAME)));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void printTemplate(Worksheet worksheet) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
		YearMonth start = YearMonth.parse(dataSource.getStartMonth(), formatter);
		YearMonth end = YearMonth.parse(dataSource.getEndMonth(), formatter);
		YearMonth currentMonth = YearMonth.now();

		Cells cells = worksheet.getCells();

		// B1_1, B1_2
		cells.get(1, 0)
				.setValue(TextResource.localize("KDR001_2") + start.format(formatter) + "　～　" + end.format(formatter));
		// B1_3
		cells.get(2, 0).setValue(TextResource.localize("KDR001_3"));
		// C1_1
		cells.get(3, 2).setValue(TextResource.localize("KDR001_4"));
		// C1_2
		cells.get(3, 4).setValue(TextResource.localize("KDR001_5"));
		// C1_3
		cells.get(3, 9).setValue(TextResource.localize("KDR001_6"));
		// C1_4
		cells.get(4, 4).setValue(TextResource.localize("KDR001_7"));
		// C1_5
		cells.get(4, 5).setValue(TextResource.localize("KDR001_8"));
		// C1_6
		cells.get(4, 6).setValue(TextResource.localize("KDR001_9"));
		// C1_7
		cells.get(4, 7).setValue(TextResource.localize("KDR001_10"));
		// C1_8
		cells.get(4, 8).setValue(TextResource.localize("KDR001_11"));
		if (end.compareTo(currentMonth) >= 0 && currentMonth.compareTo(start) >= 0) {
			Shape shape = worksheet.getShapes().get(0);
			shape.setLowerRightColumn(shape.getLowerRightColumn() + totalMonths(start, currentMonth));
		} else {
			worksheet.getShapes().removeAt(0);
		}
		// C1_9
		for (int i = 0; i <= totalMonths(start, end); i++) {
			cells.get(4, 10 + i).setValue(String.valueOf(start.plusMonths(i).getMonthValue()) + "月");
		}

	}

	private void printNoneBreakPage(Worksheet worksheet) throws Exception {
		int firstRow = numberRowOfPage;

		Cells cells = worksheet.getCells();

		// print Header
		cells.copyRows(cells, 0, firstRow, 6);
		firstRow += 5;

		// Order by Employee Code
		List<HolidaysRemainingEmployee> employees = dataSource.getListEmployee().stream()
				.sorted(Comparator.comparing(HolidaysRemainingEmployee::getEmployeeCode)).collect(Collectors.toList());
		for (HolidaysRemainingEmployee employee : employees) {
			int rowCount = countRowEachPerson() + 1;
			if (firstRow % numberRowOfPage != 5
					&& firstRow / numberRowOfPage != (firstRow + rowCount) / numberRowOfPage) {
				firstRow = (firstRow/numberRowOfPage + 1)* numberRowOfPage;
				// print Header
				cells.copyRows(cells, 0, firstRow, 6);
				firstRow += 5;
			}
			// D1_1, D1_2
			cells.get(firstRow, 0).setValue(TextResource.localize("KDR001_12") + ": " + employee.getWorkplaceCode()
					+ "　" + employee.getWorkplaceName());
			firstRow += 1;
			firstRow = this.printHolidayRemainingEachPerson(worksheet, firstRow, employee);
		}
	}

	private void printWorkplaceBreakPage(Worksheet worksheet) throws Exception {
		int firstRow = numberRowOfPage;

		Map<String, List<HolidaysRemainingEmployee>> map = dataSource.getListEmployee().stream()
				.collect(Collectors.groupingBy(HolidaysRemainingEmployee::getWorkplaceId));

		for (List<HolidaysRemainingEmployee> listEmployee : map.values()) {
			List<HolidaysRemainingEmployee> employees = listEmployee.stream()
					.sorted(Comparator.comparing(HolidaysRemainingEmployee::getEmployeeCode))
					.collect(Collectors.toList());
			firstRow = printEachWorkplace(worksheet, firstRow, employees);
		}
	}

	private int printEachWorkplace(Worksheet worksheet, int firstRow, List<HolidaysRemainingEmployee> employees)
			throws Exception {
		if (employees.size() == 0) {
			return firstRow;
		}
		Cells cells = worksheet.getCells();

		// print Header
		cells.copyRows(cells, 0, firstRow, 6);
		// D1_1, D1_2
		cells.get(firstRow + 5, 0).setValue(TextResource.localize("KDR001_12") + ": "
				+ employees.get(0).getWorkplaceCode() + "　" + employees.get(0).getWorkplaceName());
		firstRow += 6;

		for (HolidaysRemainingEmployee employee : employees) {
			int rowCount = countRowEachPerson();
			if (firstRow % numberRowOfPage != 6
					&& firstRow / numberRowOfPage != (firstRow + rowCount) / numberRowOfPage) {
				firstRow = (firstRow/numberRowOfPage + 1)* numberRowOfPage;
				// print Header
				cells.copyRows(cells, 0, firstRow, 6);
				// D1_1, D1_2
				cells.get(firstRow + 5, 0).setValue(TextResource.localize("KDR001_12") + ": "
						+ employees.get(0).getWorkplaceCode() + "　" + employees.get(0).getWorkplaceName());
				firstRow += 6;
			}
			firstRow = printHolidayRemainingEachPerson(worksheet, firstRow, employee);
		}
		if (firstRow % numberRowOfPage != 0) {
			firstRow += (numberRowOfPage - firstRow % numberRowOfPage);
		}

		return firstRow;
	}

	private void printPersonBreakPage(Worksheet worksheet) throws Exception {
		int firstRow = numberRowOfPage;

		// Order by Employee Code
		List<HolidaysRemainingEmployee> employees = dataSource.getListEmployee().stream()
				.sorted(Comparator.comparing(HolidaysRemainingEmployee::getEmployeeCode)).collect(Collectors.toList());
		for (HolidaysRemainingEmployee employee : employees) {
			firstRow = this.printEachPerson(worksheet, firstRow, employee);
		}
	}

	private int printEachPerson(Worksheet worksheet, int firstRow, HolidaysRemainingEmployee employee)
			throws Exception {
		Cells cells = worksheet.getCells();
		// D index
		// print Header
		cells.copyRows(cells, 0, firstRow, 6);
		// D1_1, D1_2
		cells.get(firstRow + 5, 0).setValue(TextResource.localize("KDR001_12") + ": " + employee.getWorkplaceCode()
				+ "　" + employee.getWorkplaceName());
		firstRow += 6;

		firstRow = printHolidayRemainingEachPerson(worksheet, firstRow, employee);

		if (firstRow % numberRowOfPage != 0) {
			firstRow += (numberRowOfPage - firstRow % numberRowOfPage);
		}

		return firstRow;
	}

	private int printHolidayRemainingEachPerson(Worksheet worksheet, int firstRow, HolidaysRemainingEmployee employee)
			throws Exception {
		int rowIndexD = firstRow;
		Cells cells = worksheet.getCells();
		int totalRowDetails = 0;
		// 年休
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getAnnualHoliday().isYearlyHoliday()) {
			cells.copyRows(cells, 6, firstRow, 2);
			// E1_1
			cells.get(firstRow, 2).setValue(TextResource.localize("Com_PaidHoliday"));
			// E2_1
			cells.get(firstRow, 9).setValue(TextResource.localize("KDR001_14"));
			// E3_1
			cells.get(firstRow + 1, 9).setValue(TextResource.localize("KDR001_15"));
			firstRow += 2;
			totalRowDetails += 2;
		}
		// 積立年休
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getYearlyReserved().isYearlyReserved()) {
			cells.copyRows(cells, 8, firstRow, 2);
			// H1_1
			cells.get(firstRow, 2).setValue(TextResource.localize("Com_FundedPaidHoliday"));
			// H2_1
			cells.get(firstRow, 9).setValue(TextResource.localize("KDR001_14"));
			// H2_2
			cells.get(firstRow + 1, 9).setValue(TextResource.localize("KDR001_15"));
			firstRow += 2;
			totalRowDetails += 2;
		}
		// 代休
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getSubstituteHoliday()
				.isOutputItemSubstitute()) {
			cells.copyRows(cells, 10, firstRow, 4);
			// I1_1
			cells.get(firstRow, 2).setValue(TextResource.localize("Com_CompensationHoliday"));
			// I2_1
			cells.get(firstRow, 9).setValue(TextResource.localize("KDR001_16"));
			// I3_1
			cells.get(firstRow + 1, 9).setValue(TextResource.localize("KDR001_17"));
			// I4_1
			cells.get(firstRow + 2, 9).setValue(TextResource.localize("KDR001_17"));
			// I5_1
			cells.get(firstRow + 3, 9).setValue(TextResource.localize("KDR001_18"));
			firstRow += 4;
			totalRowDetails += 4;
		}
		// 振休
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getPause().isPauseItem()) {
			cells.copyRows(cells, 14, firstRow, 4);
			// J1_1
			cells.get(firstRow, 2).setValue(TextResource.localize("Com_SubstituteHoliday"));
			// J2_1
			cells.get(firstRow, 9).setValue(TextResource.localize("KDR001_16"));
			// J2_2
			cells.get(firstRow + 1, 9).setValue(TextResource.localize("KDR001_17"));
			// J2_3
			cells.get(firstRow + 2, 9).setValue(TextResource.localize("KDR001_11"));
			// J2_4
			cells.get(firstRow + 3, 9).setValue(TextResource.localize("KDR001_18"));
			firstRow += 4;
		}
		// 特別休暇
		List<Integer> specialHoliday = dataSource.getHolidaysRemainingManagement().getListItemsOutput()
				.getSpecialHoliday();
		for (int i = 0; i < specialHoliday.size(); i++) {
			cells.copyRows(cells, 18, firstRow, 2);
			// M2_1
			cells.get(firstRow, 9).setValue(TextResource.localize("KDR001_17"));
			// M2_3
			cells.get(firstRow + 1, 9).setValue(TextResource.localize("KDR001_18"));
			firstRow += 2;
		}
		// 子の看護休暇
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getChildNursingVacation()
				.isChildNursingLeave()) {
			cells.copyRows(cells, 20, firstRow, 2);
			// N1_1
			cells.get(firstRow, 2).setValue(TextResource.localize("KDR001_23"));
			// N2_1
			cells.get(firstRow, 9).setValue(TextResource.localize("KDR001_9"));
			// N2_2
			cells.get(firstRow + 1, 9).setValue(TextResource.localize("KDR001_18"));
			firstRow += 2;
		}
		// 介護休暇
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getNursingcareLeave().isNursingLeave()) {
			cells.copyRows(cells, 22, firstRow, 2);
			// O1_1
			cells.get(firstRow, 2).setValue(TextResource.localize("KDR001_24"));
			// O2_1
			cells.get(firstRow, 9).setValue(TextResource.localize("KDR001_9"));
			// O2_2
			cells.get(firstRow + 1, 9).setValue(TextResource.localize("KDR001_18"));
			firstRow += 2;
		}

		// if (totalRowDetails < minRowDetails) {
		// cells.copyRows(cells, 25, firstRow, minRowDetails - totalRowDetails);
		// firstRow += (minRowDetails - totalRowDetails);
		// }

		// D2_1
		cells.get(rowIndexD, 0).setValue(employee.getEmployeeCode());
		// D2_2
		cells.get(rowIndexD, 1).setValue(employee.getEmployeeName());
		// D2_3 Todo rql No.369
		
		// Set Style
		setTopBorderStyle(cells.get(rowIndexD, 0));
		setTopBorderStyle(cells.get(rowIndexD, 1));
		setBottomBorderStyle(cells.get(firstRow - 1, 0));
		setBottomBorderStyle(cells.get(firstRow - 1, 1));
		
		return firstRow;
	}

	private int countRowEachPerson() {
		int totalRowDetails = 0;
		// 年休
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getAnnualHoliday().isYearlyHoliday()) {
			totalRowDetails += 2;
		}
		// 積立年休
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getYearlyReserved().isYearlyReserved()) {
			totalRowDetails += 2;
		}
		// 代休
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getSubstituteHoliday()
				.isOutputItemSubstitute()) {
			totalRowDetails += 4;
		}
		// 振休
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getPause().isPauseItem()) {
			totalRowDetails += 4;
		}
		// 特別休暇
		List<Integer> specialHoliday = dataSource.getHolidaysRemainingManagement().getListItemsOutput()
				.getSpecialHoliday();
		for (int i = 0; i < specialHoliday.size(); i++) {
			totalRowDetails += 2;
		}
		// 子の看護休暇
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getChildNursingVacation()
				.isChildNursingLeave()) {
			totalRowDetails += 2;
		}
		// 介護休暇
		if (dataSource.getHolidaysRemainingManagement().getListItemsOutput().getNursingcareLeave().isNursingLeave()) {
			totalRowDetails += 2;
		}

		return totalRowDetails;
	}

	private void removeTemplate(Worksheet worksheet) {
		if (worksheet.getShapes().getCount() > 0)
			worksheet.getShapes().removeAt(0);
		Cells cells = worksheet.getCells();
		cells.deleteRows(0, numberRowOfPage);
	}

	private void setTopBorderStyle(Cell cell) {
		Style style = cell.getStyle();
		style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
		cell.setStyle(style);
	}
	
	private void setBottomBorderStyle(Cell cell) {
		Style style = cell.getStyle();
		style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
		cell.setStyle(style);
	}
	
	private int totalMonths(YearMonth start, YearMonth end) {
		return (end.getYear() - start.getYear()) * 12 + (end.getMonthValue() - start.getMonthValue());
	}
}
