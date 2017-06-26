/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.denominationtable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.ejb.Stateless;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableInt;

import com.aspose.cells.BackgroundType;
import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.Range;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.time.GeneralDate;
import nts.uk.file.pr.app.export.denominationtable.DenoTableReportGenerator;
import nts.uk.file.pr.app.export.denominationtable.data.Denomination;
import nts.uk.file.pr.app.export.denominationtable.data.DenominationTableData;
import nts.uk.file.pr.app.export.denominationtable.data.DepartmentData;
import nts.uk.file.pr.app.export.denominationtable.data.EmployeeData;
import nts.uk.file.pr.app.export.denominationtable.query.DenoTableReportQuery;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * The Class AsposeDenoTblReportGenerator.
 */
@Stateless
public class AsposeDenoTblReportGenerator extends AsposeCellsReportGenerator implements DenoTableReportGenerator {

	/** The Constant REPORT_FILE_NAME. */
	private static final String REPORT_FILE_NAME = "SalaryTableReport.pdf";

	/** The Constant TEMPLATE_FILE. */
	private static final String TEMPLATE_FILE = "report/QPP009.xlsx";

	/** The Constant FIRST_COLUMN_INDEX. */
	private static final int FIRST_COLUMN_INDEX = 0;

	/** The Constant LAST_COLUMN_INDEX. */
	private static final int LAST_COLUMN_INDEX = 12;

	/** The Constant COLUMN_INDEX. */
	private static final int[] COLUMN_INDEX = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	
	/** The Constant LAST_IN_COLUMN_INDEX. */
	private static final int LAST_IN_COLUMN_INDEX = 11;

	/** The Constant FIRST_ROW_INDEX. */
	private static final int FIRST_ROW_INDEX = 9;
	
	/** The Constant BLANK_ROWS. */
	private static final int BLANK_ROWS = 2;
	
	/** The Constant ROWS_PER_PAGE. */
	private static final int ROWS_PER_PAGE = 37;
	
	/** The Constant HIERARCHY_DEP_TEXT. */
	private static final String HIERARCHY_DEP_TEXT = "部門階層素計";

	/** The Constant SPACES. */
	private static final String SPACES = "    ";

	/** The Constant SPACES1. */
	private static final String SPACES1 = "                        ";
	
	/** The Constant LIGHT_BLUE_COLOR. */
	private static final Color LIGHT_BLUE_COLOR = Color.fromArgb(197, 241, 247);
	
	/** The Constant LIGHT_GREEN_COLOR. */
	private static final Color LIGHT_GREEN_COLOR = Color.fromArgb(199, 243, 145);
	
	/** The Constant NONE_BREAK_CODE. */
	private static final int NONE_BREAK_CODE = 1;
	
	/** The Constant EMPLOYEE_BREAK_CODE. */
	private static final int EMPLOYEE_BREAK_CODE = 2;
	
	/** The Constant DEPARTMENT_BREAK_CODE. */
	private static final int DEPARTMENT_BREAK_CODE = 3;
	
	/** The Constant HIERARCHY_BREAK_CODE. */
	private static final int HIERARCHY_BREAK_CODE = 4;
	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.ctx.pr.screen.app.report.salarychart.SalaryChartReportGenerator#
	 * generate(nts.arc.layer.infra.file.export.FileGeneratorContext,
	 * nts.uk.ctx.pr.screen.app.report.salarychart.data.SalaryChartDataSource)
	 */
	@Override
	public void generate(FileGeneratorContext generatorContext, DenominationTableData reportData,
			DenoTableReportQuery query) {
		List<EmployeeData> empList = reportData.getEmployeeList();
		try {
			 AsposeCellsReportContext reportContext =
			 this.createContext(TEMPLATE_FILE);
			 Workbook workbook = reportContext.getWorkbook();

			// CREATE WORKSHEET AND FORMARTTING
			WorksheetCollection worksheets = workbook.getWorksheets();
			Worksheet worksheet = worksheets.get(0);
			Cells cells = worksheet.getCells();
			
			// SET HEADER
			worksheet.getPageSetup().setHeader(2,
					"&\"IPAPGothic\"&13 " + GeneralDate.today().toString() + "\r\n&P ページ");
			reportContext.getDesigner().setDataSource("Header", reportData.getSalaryChartHeader());
			
			// INITIAL VARIABLES
			PrintProcess printProcess = new PrintProcess();
			printProcess.cells = cells;
			printProcess.query = query;
			printProcess.rowIndex = FIRST_ROW_INDEX;
			printProcess.depStack = new Stack<>();
			printProcess.tempDenomination = new HashMap<>();
			printProcess.tempAccumulate = 0;
			printProcess.members = 0;
			
			// FILL DATA
			this.printData(printProcess, empList);
			
			reportContext.getDesigner().setWorkbook(workbook);
			reportContext.processDesigner();
			reportContext.saveAsPdf(this.createNewFile(generatorContext,
					this.getReportName(REPORT_FILE_NAME)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Prints the data.
	 *
	 * @param printProcess the print process
	 * @param empList the emp list
	 */
	private void printData(PrintProcess printProcess, List<EmployeeData> empList) {
		// INITIAL TEMPLE GROSS VARIABLES
		Map<Denomination, Long> grossTotalDenomination = new HashMap<>();
		MutableDouble grossTotalAccumulate = new MutableDouble(0);
		MutableInt grossTotalEmp = new MutableInt(0);
		
		// Background Color of Row is White (or Green)
		MutableBoolean isGreenRow = new MutableBoolean(false);
		Iterator<EmployeeData> itr = empList.iterator();
		while (itr.hasNext()) {
			EmployeeData currentEmp = itr.next();
			printProcess.currentEmp = currentEmp;
			
			// Cumulate to Gross Total vars
			this.cumulateToAmount(grossTotalDenomination, grossTotalAccumulate, grossTotalEmp, currentEmp);
			
			if (printProcess.prevEmp == null) {
				// Print First Employee
				this.printFirstEmp(printProcess, isGreenRow);
				printProcess.prevEmp = currentEmp;
				continue;
			}
			String prevDepCode = printProcess.prevEmp.getDepartmentData().getDepCode();
			String currentDepCode = printProcess.currentEmp.getDepartmentData().getDepCode();
			
			if (prevDepCode.equals(currentDepCode)) {
				// Cumulate To Temp Variables and Print Employee
				this.printEmpSameDep(printProcess, isGreenRow);
				printProcess.prevEmp = currentEmp;
				continue;
			}
			else {
				// Push Temp DepartmentData To Stack and Reset Temp vars
				this.pushToStackAndReset(printProcess);
				// Cumulate to Temp vars
				this.cumulateToTemp(printProcess);

				// Print Sub Accumulated payment of Sub Department
				this.fillAccSubDep(printProcess);

				// Print Employee
				this.printEmpDifferentDep(printProcess, isGreenRow);
				printProcess.prevEmp = currentEmp;
			}
		}		
		// END BLOCK OF REPORT
		// Fill Total Of Department
		this.printTotalOfDep(printProcess);

		// Fill Accumulated Payment
		this.printAccAtTheEnd(printProcess);
		
		// Fill Gross Total
		this.printGrossTotal(printProcess, grossTotalEmp.getValue(), 
				grossTotalDenomination, grossTotalAccumulate.getValue());
	}
	
	
	/**
	 * Last cumulated deps.
	 *
	 * @param printProcess the print process
	 */
	private void lastCumulatedDeps(PrintProcess printProcess){
		while (!printProcess.depStack.isEmpty()) {
			DepartmentData empDep = printProcess.prevEmp.getDepartmentData();
			String empDepPath = empDep.getDepPath();
			DepartmentData topInStack = printProcess.depStack.pop();
			// Cumulate printed Denomination into Stack
			
			if (empDepPath.startsWith(topInStack.getDepPath())) {
				// Calculate Accumulation
				this.calculateDenomination(empDep, topInStack);
				this.printAccByHierarchy(printProcess, topInStack);
			}
			else {
				// print Last Accumulated DepartmentData
				this.printAccByHierarchy(printProcess, topInStack);
			}
			printProcess.prevEmp.setDepartmentData(topInStack);
		}
	}
	
	/**
	 * Prints the emp same dep.
	 *
	 * @param printProcess the print process
	 * @param isGreen the is green
	 */
	private void printEmpSameDep(PrintProcess printProcess, MutableBoolean isGreen) {
		DenoTableReportQuery query = printProcess.query;
		// Breaking Page Code
		int breakCode = query.getSelectedBreakPageCode();

		if (breakCode == EMPLOYEE_BREAK_CODE && query.getIsPrintDetailItem()) {
			this.breakPageByOption(printProcess);
			this.printDepInfo(printProcess);
		}
		// Cumulate To Temp Variables
		this.cumulateToTemp(printProcess);

		// print currentEmp
		this.printEmployee(printProcess, isGreen);
	}
	
	/**
	 * Push to stack and reset.
	 *
	 * @param printProcess the print process
	 */
	private void pushToStackAndReset(PrintProcess printProcess){
		
		// Create border line for last employee in a department
		this.createEndRowRange(printProcess);
		
		// Print Total In Department if choosen
		this.printTotalOfDep(printProcess);
		
		// Push DepartmentData to Stack
		this.pushDepData(printProcess);
		
		// Reset Temp Variable and continue Cumulate
		printProcess.tempAccumulate = 0;
		printProcess.members = 0;
		printProcess.tempDenomination = new HashMap<>();
	}
	
	/**
	 * Prints the emp different dep.
	 *
	 * @param printProcess the print process
	 * @param isGreen the is green
	 */
	private void printEmpDifferentDep(PrintProcess printProcess, MutableBoolean isGreen){
		DenoTableReportQuery query = printProcess.query;
		// Breaking Page Code
		int breakCode = query.getSelectedBreakPageCode();
		
		// Check for Breaking page By Employee
		if(query.getIsPrintDetailItem() && 
				((breakCode == EMPLOYEE_BREAK_CODE) || (breakCode == DEPARTMENT_BREAK_CODE))){
			this.breakPageByOption(printProcess);
		}
		// Print current Department Info
		this.printDepInfo(printProcess);
		
		// Print current Employee
		isGreen.setValue(false);
		this.printEmployee(printProcess, isGreen);
	}
	
	/**
	 * Fill acc sub dep.
	 *
	 * @param printProcess the print process
	 */
	private void fillAccSubDep(PrintProcess printProcess){
		
		// Department Path of Current Employee and Previous employee
		String currentDepPath = printProcess.currentEmp.getDepartmentData().getDepPath();
		String prevDepPath = printProcess.prevEmp.getDepartmentData().getDepPath();
		
		// Check if Current Department is a sub Department of Previous Department
		boolean isSubDep = currentDepPath.startsWith(prevDepPath);
		if (!isSubDep) {
			// Print Sub Accumulated...
			this.printSubAccumulated(printProcess);
		}
	}

	/**
	 * Prints the acc at the end.
	 *
	 * @param printProcess the print process
	 */
	private void printAccAtTheEnd(PrintProcess printProcess) {
		// Print Accumulated Hierarchy of Department
		if (printProcess.query.getIsPrintDepHierarchy()) {
			DepartmentData empDep = printProcess.prevEmp.getDepartmentData();
			empDep.setDenomination(printProcess.tempDenomination);
			empDep.setAccumulatedPayment(printProcess.tempAccumulate);
			this.printAccByHierarchy(printProcess, empDep);
			this.lastCumulatedDeps(printProcess);
		}
	}
	
	/**
	 * Cumulate to temp.
	 *
	 * @param printProcess the print process
	 */
	private void cumulateToTemp(PrintProcess printProcess){
		EmployeeData currentEmp = printProcess.currentEmp;
		
		// Cumulate to temple variables
		this.cumulateDenomination(printProcess);
		printProcess.tempAccumulate += currentEmp.getPaymentAmount();
		printProcess.members ++;
	}
	
	/**
	 * Cumulate to amount.
	 *
	 * @param tempDenomination the temp denomination
	 * @param tempAccumulate the temp accumulate
	 * @param members the members
	 * @param currentEmp the current emp
	 */
	private void cumulateToAmount(Map<Denomination, Long> tempDenomination, 
			MutableDouble tempAccumulate, MutableInt members, EmployeeData currentEmp){
		
		// Cumulate to temple variables
		this.cumulateGrossDenomination(tempDenomination, currentEmp.getDenomination());
		tempAccumulate.add(currentEmp.getPaymentAmount());
		members.increment();
	}
	
	/**
	 * Prints the first emp.
	 *
	 * @param printProcess the print process
	 * @param isGreen the is green
	 */
	private void printFirstEmp(PrintProcess printProcess, MutableBoolean isGreen){
		// Print Title Row and Department Info
		this.createTitle(printProcess);
		this.printDepInfo(printProcess);
		
		 // Cumulate To Temp Variables
		this.cumulateToTemp(printProcess);
		this.printEmployee(printProcess, isGreen);
	}
	
	/**
	 * None break page.
	 *
	 * @param printProcess the print process
	 */
	private void noneBreakPage(PrintProcess printProcess) {
		int currentRow = printProcess.rowIndex;
		
		if (currentRow % ROWS_PER_PAGE == 0) {
			this.createEndRowRange(printProcess);
			printProcess.rowIndex += BLANK_ROWS;
			this.createTitle(printProcess);
		}
	}
	
	/**
	 * Break page border line.
	 *
	 * @param printProcess the print process
	 */
	private void breakPageBorderLine(PrintProcess printProcess) {
		int currentRow = printProcess.rowIndex;
		
		// Create Double border for last row 
		if (currentRow % ROWS_PER_PAGE == 0) {
			this.createEndRowRange(printProcess);
		}
		// Create Double border for first row in next page
		if(currentRow % ROWS_PER_PAGE == 1){
			this.createStartRowRange(printProcess);
		}
	}
	
	/**
	 * Break page by option.
	 *
	 * @param printProcess the print process
	 */
	private void breakPageByOption(PrintProcess printProcess){
		int currentRow = printProcess.rowIndex;
		if(currentRow % ROWS_PER_PAGE == 0){
			this.createTitle(printProcess);
		}
		else {
			// Create End Row Range
			this.createEndRowRange(printProcess);		
			int nextPage = currentRow / ROWS_PER_PAGE +1;
			int blankRowsCurrentPage = nextPage * ROWS_PER_PAGE +1 - currentRow;
			printProcess.rowIndex += (blankRowsCurrentPage + BLANK_ROWS);
			// Create Title for Next Page
			this.createTitle(printProcess);
		}
	}

	/**
	 * Creates the end row range.
	 *
	 * @param printProcess the print process
	 */
	private void createEndRowRange(PrintProcess printProcess){
		Cells cells = printProcess.cells;
		int currentRow = printProcess.rowIndex;
		Range endRange = cells.createRange(currentRow - 1, FIRST_COLUMN_INDEX, 1, LAST_COLUMN_INDEX);
		endRange.setOutlineBorder(BorderType.BOTTOM_BORDER, CellBorderType.DOUBLE, Color.getBlack());
	}
	
	/**
	 * Creates the start row range.
	 *
	 * @param printProcess the print process
	 */
	private void createStartRowRange(PrintProcess printProcess){
		Cells cells = printProcess.cells;
		int currentRow = printProcess.rowIndex;
		Range startRange = cells.createRange(currentRow - 1, FIRST_COLUMN_INDEX, 1, LAST_COLUMN_INDEX);
		startRange.setOutlineBorder(BorderType.TOP_BORDER, CellBorderType.DOUBLE, Color.getBlack());
	}
	
	/**
	 * Creates the left right border.
	 *
	 * @param printProcess the print process
	 */
	private void createLeftRightBorder(PrintProcess printProcess) {
		Cells cells = printProcess.cells;
		int currentRow = printProcess.rowIndex;
		
		// Create Left and Right Border to Cell
		Range range = cells.createRange(currentRow, FIRST_COLUMN_INDEX, 1, LAST_COLUMN_INDEX);
		range.setOutlineBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
		range.setOutlineBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
	}
	
	/**
	 * Prints the sub accumulated.
	 *
	 * @param printProcess the print process
	 */
	private void printSubAccumulated(PrintProcess printProcess){
		// Peek Department Data from Stack
		DepartmentData depToPrint = printProcess.depStack.peek();
		String depToPrintPath = depToPrint.getDepPath();
		int depToPrintLevel = depToPrint.getDepLevel();
		int currentDepLevel = printProcess.currentEmp.getDepartmentData().getDepLevel();
		
		do { // Print Accumulated payment Of Department
			depToPrint = printProcess.depStack.pop();
			this.printAccByHierarchy(printProcess, depToPrint);
			
			if (!printProcess.depStack.isEmpty()) {
				DepartmentData topInStack = printProcess.depStack.pop();
				if (depToPrintPath.startsWith(topInStack.getDepPath())) {
					this.calculateDenomination(depToPrint, topInStack);
					printProcess.depStack.push(topInStack);
				}
				// Assign 
				depToPrintPath = topInStack.getDepPath();
				depToPrintLevel = topInStack.getDepLevel();
			}
		} while (!printProcess.depStack.isEmpty() && !(currentDepLevel > depToPrintLevel));
	}

	/**
	 * Push dep data.
	 *
	 * @param printProcess the print process
	 */
	private void pushDepData(PrintProcess printProcess){
		// Set Denomination for Temp DepartmentData
		DepartmentData prevDep = printProcess.prevEmp.getDepartmentData();
		DepartmentData tempDepData = DepartmentData.builder().build();
		tempDepData.setDenomination(printProcess.tempDenomination);
		tempDepData.setAccumulatedPayment(printProcess.tempAccumulate);
		tempDepData.setDepLevel(prevDep.getDepLevel());
		tempDepData.setDepCode(prevDep.getDepCode());
		tempDepData.setDepName(prevDep.getDepName());
		tempDepData.setDepPath(prevDep.getDepPath());
		
		// Push DepartmentData to Stack
		printProcess.depStack.push(tempDepData);
	}
	
	/**
	 * Prints the dep info.
	 *
	 * @param printProcess the print process
	 */
	private void printDepInfo(PrintProcess printProcess) {
		EmployeeData currentEmp = printProcess.currentEmp;
		Cells cells = printProcess.cells;
		int rowIndex = printProcess.rowIndex;
		DepartmentData depData = currentEmp.getDepartmentData();
		Cell cell = cells.get(rowIndex, 0);
		cell.setValue("部門  : " + SPACES + depData.getDepCode() + SPACES + depData.getDepName());

		// Set Style for cells
		for (int i = FIRST_COLUMN_INDEX; i < LAST_COLUMN_INDEX; i++) {
			Cell cell1 = cells.get(rowIndex, i);
			StyleModel stylemodel = new StyleModel();
			stylemodel.backgroundColor = LIGHT_BLUE_COLOR;
			stylemodel.borderType = CellsBorderType.TopBottomOnly;
			this.setCellStyle1(cell1, stylemodel);
			createLeftRightBorder(printProcess);	
		}
		printProcess.rowIndex ++;
		// Break Page
		this.breakPage(printProcess);
	}
	
	/**
	 * Sets the cum cell style.
	 *
	 * @param printProcess the new cum cell style
	 */
	private void setCumCellStyle(PrintProcess printProcess) {
		Cells cells = printProcess.cells;
		int rowIndex = printProcess.rowIndex;
		
		// Style for cells
		for (int i = FIRST_COLUMN_INDEX; i < LAST_COLUMN_INDEX; i++) {
			Cell cell = cells.get(rowIndex, i);
			StyleModel stylemodel = new StyleModel();
			stylemodel.backgroundColor = LIGHT_BLUE_COLOR;
			stylemodel.borderType = CellsBorderType.DoubleTopBorder;
			this.setCellStyle1(cell, stylemodel);
			createLeftRightBorder(printProcess);
		}
	}
	
	/**
	 * Break page.
	 *
	 * @param printProcess the print process
	 */
	private void breakPage(PrintProcess printProcess){
		int breakCode = printProcess.query.getSelectedBreakPageCode();
		if (breakCode == NONE_BREAK_CODE) {
			this.noneBreakPage(printProcess);
		}
		if ((breakCode == HIERARCHY_BREAK_CODE) || (breakCode == DEPARTMENT_BREAK_CODE)) {
			this.breakPageBorderLine(printProcess);
		}
	}
	
	/**
	 * Prints the total of dep.
	 *
	 * @param printProcess the print process
	 */
	private void printTotalOfDep(PrintProcess printProcess) {
		DenoTableReportQuery query = printProcess.query;
		Cells cells = printProcess.cells;
		int rowIndex = printProcess.rowIndex;
		int members = printProcess.prevEmp.getDepartmentData().getNumberOfEmp();
		if (query.getIsPrintTotalOfDepartment()) {
			
			// Fill Data to cells
			cells.get(rowIndex, COLUMN_INDEX[FIRST_COLUMN_INDEX]).setValue("部門計" + SPACES1 + members + "人");
			for (Denomination d : Denomination.values()) {
				int columnIndex = d.value + 1;
				cells.get(rowIndex, columnIndex).setValue(printProcess.tempDenomination.get(d) + "枚");
			}
			cells.get(rowIndex, COLUMN_INDEX[LAST_IN_COLUMN_INDEX]).setValue(printProcess.tempAccumulate);
			
			// Style for cells
			this.setCumCellStyle(printProcess);
			printProcess.rowIndex ++;
			
			// Break Page
			this.breakPage(printProcess);
		}

	}
	
	/**
	 * Prints the acc by hierarchy.
	 *
	 * @param printProcess the print process
	 * @param depToPrint the dep to print
	 */
	private void printAccByHierarchy(PrintProcess printProcess, DepartmentData depToPrint) {
		DenoTableReportQuery query = printProcess.query;
		
		// Breaking Page Code
		int breakCode = query.getSelectedBreakPageCode();
		
		// Hierarchy Breaking Page code
		int hierarchyBreakCode = query.getSelectedBreakPageHierarchyCode();
		List<Integer> selectedLevels = query.getSelectedLevels();
		int depToPrintLevel = depToPrint.getDepLevel();
		for (int i : selectedLevels) {
			if (depToPrintLevel == i) {
				this.printAccumulated(printProcess, depToPrint);
				
				// Check for Breaking page by Hierarchy
				if ((breakCode == HIERARCHY_BREAK_CODE) && (hierarchyBreakCode == depToPrintLevel)) {
					this.breakPageByOption(printProcess);
				}
				break;
			}
		}
	}

	/**
	 * Prints the gross total.
	 *
	 * @param printProcess the print process
	 * @param totalmembers the totalmembers
	 * @param denomination the denomination
	 * @param amount the amount
	 */
	private void printGrossTotal(PrintProcess printProcess, int totalmembers, 
			Map<Denomination, Long> denomination, double amount) {
		Cells cells = printProcess.cells;
		int rowIndex = printProcess.rowIndex;
		if (printProcess.query.getIsCalculateTotal()) {
			// Fill Data to cells
			cells.get(rowIndex, COLUMN_INDEX[0]).setValue("総合計" + SPACES1 + totalmembers + "人");
			for (Denomination d : Denomination.values()) {
				int columnIndex = d.value + 1;
				cells.get(rowIndex, columnIndex).setValue(denomination.get(d) + "枚");
			}
			cells.get(rowIndex, COLUMN_INDEX[LAST_IN_COLUMN_INDEX]).setValue(amount);

			// Style for cells
			for (int i = FIRST_COLUMN_INDEX; i < LAST_COLUMN_INDEX; i++) {
				Cell cell = cells.get(rowIndex, i);
				StyleModel stylemodel = new StyleModel();
				stylemodel.backgroundColor = LIGHT_BLUE_COLOR;
				stylemodel.borderType = CellsBorderType.DoubleTopBorder;
				this.setCellStyle1(cell, stylemodel);
				createLeftRightBorder(printProcess);
			}
		}
	}

	/**
	 * Prints the accumulated.
	 *
	 * @param printProcess the print process
	 * @param depToPrint the dep to print
	 */
	private void printAccumulated(PrintProcess printProcess, DepartmentData depToPrint) {
		Cells cells = printProcess.cells;
		int rowIndex = printProcess.rowIndex;
		cells.get(rowIndex, COLUMN_INDEX[0]).setValue(depToPrint.getDepName() + SPACES + HIERARCHY_DEP_TEXT);
		for (Denomination d : Denomination.values()) {
			int columnIndex = d.value + 1;
			cells.get(rowIndex, columnIndex).setValue(depToPrint.getDenomination().get(d) + "枚");
		}
		cells.get(rowIndex, COLUMN_INDEX[LAST_IN_COLUMN_INDEX])
		.setValue(depToPrint.getAccumulatedPayment());
		
		// Style for cells
		this.setCumCellStyle(printProcess);
		printProcess.rowIndex ++;
		
		// Break Page
		this.breakPage(printProcess);
	}

	/**
	 * Cumulate gross denomination.
	 *
	 * @param denomination the denomination
	 * @param currentEmpDeno the current emp deno
	 */
	private void cumulateGrossDenomination(Map<Denomination, Long> denomination, 
			Map<Denomination, Long> currentEmpDeno) {
		for (Denomination d : Denomination.values()) {
			if(denomination.get(d) == null){
				denomination.put(d, 0l);
			}
			long quantity = denomination.get(d) + currentEmpDeno.get(d);
			denomination.replace(d, quantity);
		}		
	}
	
	/**
	 * Cumulate denomination.
	 *
	 * @param printProcess the print process
	 */
	private void cumulateDenomination(PrintProcess printProcess) {
		Map<Denomination, Long> denomination = printProcess.tempDenomination;
		for (Denomination d : Denomination.values()) {
			if(denomination.get(d) == null){
				printProcess.tempDenomination.put(d, 0L);
			}
			Map<Denomination, Long> currentEmpDeno = printProcess.currentEmp.getDenomination();
			long quantity = denomination.get(d) + currentEmpDeno.get(d);
			printProcess.tempDenomination.replace(d, quantity);
		}		
	}

	/**
	 * Calculate denomination.
	 *
	 * @param fromDep the from dep
	 * @param toDep the to dep
	 * @return the department data
	 */
	private DepartmentData calculateDenomination(DepartmentData fromDep, DepartmentData toDep) {
		for (Denomination d : Denomination.values()) {
			long quantity = fromDep.getDenomination().get(d) + toDep.getDenomination().get(d);
			toDep.getDenomination().replace(d, quantity);
		}
		if(fromDep.getAccumulatedPayment() == null){
			fromDep.setAccumulatedPayment(0.0);
		}
		double accumulateAmount = fromDep.getAccumulatedPayment() + toDep.getAccumulatedPayment();
		toDep.setAccumulatedPayment(accumulateAmount);
		return toDep;
	}

	/**
	 * Prints the employee.
	 *
	 * @param printProcess the print process
	 * @param isGreen the is green
	 */
	private void printEmployee(PrintProcess printProcess, 
			MutableBoolean isGreen) {
		Cells cells = printProcess.cells;
		int rowIndex = printProcess.rowIndex;
		EmployeeData currentEmp = printProcess.currentEmp;
		// Print Employee 
		boolean isPrintEmp = printProcess.query.getIsPrintDetailItem();
		if (isPrintEmp) {
			cells.get(rowIndex, COLUMN_INDEX[0])
					.setValue(currentEmp.getEmpCode() + SPACES + currentEmp.getEmpName());
			Map<Denomination, Long> deno = currentEmp.getDenomination();
			for (Denomination d : Denomination.values()) {
				int columnIndex = d.value + 1;
				cells.get(rowIndex, columnIndex).setValue(deno.get(d) + "枚");
			}
			cells.get(rowIndex, COLUMN_INDEX[LAST_IN_COLUMN_INDEX]).setValue(currentEmp.getPaymentAmount());

			// Style for cells
			for (int i = FIRST_COLUMN_INDEX; i < LAST_COLUMN_INDEX; i++) {
				Cell cell = cells.get(rowIndex, i);
				StyleModel stylemodel = new StyleModel();
				stylemodel.borderType = CellsBorderType.CommonBorder;
				stylemodel.backgroundColor = isGreen.getValue() ? LIGHT_GREEN_COLOR : null;
				this.setCellStyle1(cell, stylemodel);
				createLeftRightBorder(printProcess);
			}
			//isGreen.setValue(this.switchColor(isGreen.getValue()));
			if (isGreen.booleanValue()) {
				isGreen.setValue(false);
			} else {
				isGreen.setValue(true);
			}
			printProcess.rowIndex ++;
		}
		// Break Page
		this.breakPage(printProcess);
	}

	/**
	 * Creates the title.
	 *
	 * @param printProcess the print process
	 */
	private void createTitle(PrintProcess printProcess) {
		Cells cells = printProcess.cells;
		int rowIndex = printProcess.rowIndex;
		// Set Header for Employee column
		Cell empCell = cells.get(rowIndex, COLUMN_INDEX[0]);
		empCell.setValue("社員");
		Style style = empCell.getStyle();
		style.setHorizontalAlignment(TextAlignmentType.LEFT);
		empCell.setStyle(style);

		for (Denomination deno : Denomination.values()) {
			int columnIndex = deno.value + 1;
			Cell denoCell = cells.get(rowIndex, columnIndex);
			denoCell.setValue(deno.description);
		}
		// Set Header for Amount Column
		Cell amountCell = cells.get(rowIndex, COLUMN_INDEX[LAST_IN_COLUMN_INDEX]);
		amountCell.setValue("金額");

		// Set Style for Header Row
		for (int i = FIRST_COLUMN_INDEX; i < LAST_COLUMN_INDEX; i++) {
			Cell cell = cells.get(rowIndex, i);
			this.setCellStyle1(cell, StyleModel.titleStyle());
		}
		printProcess.rowIndex ++;
		
	}

	
	/**
	 * The Enum CellsBorderType.
	 */
	private enum CellsBorderType{
		
		/** The Top bottom only. */
		TopBottomOnly,
		
		/** The Double top border. */
		DoubleTopBorder,
		
		/** The Medium border. */
		MediumBorder,
		
		/** The Common border. */
		CommonBorder,
	}
	
	/**
	 * The Class StyleModel.
	 */
	public static class StyleModel{
		
		/** The background color. */
		public Color backgroundColor;
		
		/** The border type. */
		public CellsBorderType borderType;
		
		/** The text alignment. */
		public int textAlignment;
		
		/**
		 * Title style.
		 *
		 * @return the style model
		 */
		public static StyleModel titleStyle() {
			StyleModel style = new StyleModel();
			style.backgroundColor = LIGHT_BLUE_COLOR;
			style.borderType = CellsBorderType.MediumBorder;
			style.textAlignment = TextAlignmentType.CENTER;
			return style;
		}
	}
	
	/**
	 * Sets the cell style 1.
	 *
	 * @param cell the cell
	 * @param styleModel the style model
	 */
	private void setCellStyle1(Cell cell, StyleModel styleModel){
		Style style = cell.getStyle();
		if (styleModel.backgroundColor != null) {
			style.setForegroundColor(styleModel.backgroundColor);
			style.setPattern(BackgroundType.SOLID);
		} 
		if (styleModel.textAlignment != 0) {
			style.setHorizontalAlignment(styleModel.textAlignment);
		}
		switch (styleModel.borderType) {
			case MediumBorder:
				style.setBorder(BorderType.TOP_BORDER, CellBorderType.MEDIUM, Color.getBlack());
				style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.MEDIUM, Color.getBlack());
				style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
				style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
				break;
			case TopBottomOnly:
				style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
				style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
				break;
			case DoubleTopBorder:
				style.setBorder(BorderType.TOP_BORDER, CellBorderType.DOUBLE, Color.getBlack());
				style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.DOUBLE, Color.getBlack());
				style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.DOTTED, Color.getBlack());
				break;
			case CommonBorder:
				style.setBorder(BorderType.TOP_BORDER, CellBorderType.NONE, Color.getBlack());
				style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.NONE, Color.getBlack());
				style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.DOTTED, Color.getBlack());
				break;
			default:
				break;
		}
		cell.setStyle(style);
	}
	

	/**
	 * The Class PrintProcess.
	 */
	class PrintProcess{
		
		/** The row index. */
		public int rowIndex;
		
		/** The cells. */
		public Cells cells;
		
		/** The query. */
		public DenoTableReportQuery query;
		
		/** The dep stack. */
		public Stack<DepartmentData> depStack;
		
		/** The current emp. */
		public EmployeeData currentEmp;
		
		/** The prev emp. */
		public EmployeeData prevEmp;
		
		/** The temp denomination. */
		public Map<Denomination, Long> tempDenomination;
		
		/** The temp accumulate. */
		public double tempAccumulate; 
		
		/** The members. */
		public int members;
	}
	
}