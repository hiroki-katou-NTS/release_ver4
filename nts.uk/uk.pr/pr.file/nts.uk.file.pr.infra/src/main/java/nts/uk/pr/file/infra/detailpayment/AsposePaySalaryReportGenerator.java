/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.detailpayment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.aspose.cells.BackgroundType;
import com.aspose.cells.BorderType;
import com.aspose.cells.Cell;
import com.aspose.cells.CellBorderType;
import com.aspose.cells.Cells;
import com.aspose.cells.Color;
import com.aspose.cells.PageOrientationType;
import com.aspose.cells.PageSetup;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.report.dom.salarydetail.SalaryCategory;
import nts.uk.file.pr.app.export.detailpayment.PaySalaryInsuGenerator;
import nts.uk.file.pr.app.export.detailpayment.data.DepartmentDto;
import nts.uk.file.pr.app.export.detailpayment.data.EmployeeDto;
import nts.uk.file.pr.app.export.detailpayment.data.EmployeeKey;
import nts.uk.file.pr.app.export.detailpayment.data.HeaderReportData;
import nts.uk.file.pr.app.export.detailpayment.data.PaymentConstant;
import nts.uk.file.pr.app.export.detailpayment.data.PaymentSalaryReportData;
import nts.uk.file.pr.app.export.detailpayment.data.SalaryPrintSettingDto;
import nts.uk.shr.com.time.japanese.JapaneseDate;
import nts.uk.shr.com.time.japanese.JapaneseErasProvider;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * The Class AsposePaySalaryReportGenerator.
 */
@Stateless
public class AsposePaySalaryReportGenerator extends AsposeCellsReportGenerator
        implements PaySalaryInsuGenerator {

    /** The japanese provider. */
    @Inject
    private JapaneseErasProvider japaneseProvider;

    /** The Constant TEMPLATE_FILE. */
    private static final String TEMPLATE_FILE = "report/QPP007.xlsx";
    
    /** The Constant REPORT_FILE_NAME. */
    private static final String REPORT_FILE_NAME = "明細一覧表.pdf";
    
    /** The Constant SHEET_NAME. */
    private static final String SHEET_NAME = "Sheet 1";
    
    /*
     * (non-Javadoc)
     * 
     * @see nts.uk.file.pr.app.export.detailpaymentsalary.
     * PaymentSalaryInsuranceGenerator#generate(nts.arc.layer.infra.file.export.
     * FileGeneratorContext, nts.uk.file.pr.app.export.detailpaymentsalary.data.
     * PaymentSalaryReportData)
     */
    @Override
    public void generate(FileGeneratorContext fileContext, PaymentSalaryReportData reportData) {
        try (val reportContext = this.createContext(TEMPLATE_FILE)) {
            Workbook workbook = reportContext.getWorkbook();
            WorksheetCollection worksheets = workbook.getWorksheets();
            workbook.calculateFormula(true);
            
            // =============== CREATE NEW SHEET ===============
            createNewSheet(worksheets, reportData);
            reportContext.getDesigner().setWorkbook(workbook);
            
            // =============== SET DATASOURCE HEADER ===============
            reportContext.getDesigner().setDataSource(PaymentConstant.HEADER,
                    Arrays.asList(reportData.getHeaderData()));
            
            reportContext.processDesigner();
            
            // =============== SET AUTO FIT ROW HEIGHT OF TITLE ROW ===============
            worksheets.get(PaymentConstant.ZERO).autoFitRow(PaymentConstant.INDEX_ROW_TITLE);
            
            // =============== SAVE AS PDF ===============
            reportContext.saveAsPdf(this.createNewFile(fileContext, this.getReportName(REPORT_FILE_NAME)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the new sheet.
     *
     * @param worksheets the worksheets
     * @param reportData the report data
     */
    private void createNewSheet(WorksheetCollection worksheets, PaymentSalaryReportData reportData) {
        Worksheet worksheet = worksheets.get(PaymentConstant.ZERO);
        worksheet.setName(SHEET_NAME);

        PrintProcess printProcess = new PrintProcess();
        printProcess.worksheet = worksheet;
        printProcess.configure = reportData.getConfigure();

        writeContent(printProcess, reportData);

        // ====== SET PRINT PAGE ======
        setupPage(printProcess, reportData.getHeaderData());
    }

    /**
     * Setup page.
     *
     * @param printProcess the print process
     * @param header the header
     */
    private void setupPage(PrintProcess printProcess, HeaderReportData header) {
        // ======== PAGE SETUP =========
        PageSetup pageSetup = printProcess.worksheet.getPageSetup();
        pageSetup.setOrientation(PageOrientationType.LANDSCAPE);
        pageSetup.setCenterVertically(false);
        pageSetup.setCenterHorizontally(false);
        
        // ===== SET HEADER =======
        int offsetLeft = 0;
        int offsetRight = 2;
        pageSetup.setHeader(offsetLeft, "&\"IPAPGothic\"&11 " + header.getNameCompany());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        pageSetup.setHeader(offsetRight, "&\"IPAPGothic\"&11 " + dateFormat.format(new Date()) + "\n&P ページ");

        // merge row title report
        printProcess.worksheet.getCells().merge(PaymentConstant.ZERO, PaymentConstant.ZERO,
                PaymentConstant.ONE, PaymentConstant.NUMBER_COLUMN_PAGE);

        // ======== SET PRINT AREA ========
        int lastColumn;
        // ========= NO HAVE DATE REPORT, PRINT AREA DEFAULT A PAGE;
        if (printProcess.totalColumn == 0) {
            lastColumn = PaymentConstant.NUMBER_COLUMN_PAGE;
        } else {
            lastColumn = printProcess.totalColumn - PaymentConstant.ONE;
        }
        Cell cellEnd = printProcess.worksheet.getCells().get(printProcess.indexRow, lastColumn);
        String endArea = cellEnd.getName();
        String printArea = PaymentConstant.START_AREA + endArea;
        pageSetup.setPrintArea(printArea);
    }

    /**
     * Write content.
     *
     * @param printProcess the print process
     * @param reportData the report data
     */
    private void writeContent(PrintProcess printProcess, PaymentSalaryReportData reportData) {
        printProcess.indexRow = PaymentConstant.INDEX_ROW_CONTENT;
        printProcess.selectedLevels = reportData.getConfigure().getSelectedLevels();
        printProcess.employees = reportData.getEmployees().stream()
                .filter(distinctByKey(p -> p.getCode()))
                .collect(Collectors.toList());

        Map<EmployeeKey, Double> mapAmount = reportData.getMapEmployeeAmount();

        List<Integer> yearMonths = mapAmount.entrySet().stream()
                .sorted((p1, p2) -> p1.getKey().getYearMonth().compareTo(p2.getKey().getYearMonth()))
                .filter(distinctByKey(p -> p.getKey().getYearMonth())).map(p -> p.getKey().getYearMonth())
                .collect(Collectors.toList());
        printProcess.yearMonths = yearMonths;

        // ========== CONTENT REPORT ===========
        // payment
        Map<EmployeeKey, Double> mapPayment = findMapCategory(mapAmount, SalaryCategory.Payment);
        if (!mapPayment.isEmpty()) {
            printProcess.mapAmount = mapPayment;
            printProcess.category = SalaryCategory.Payment;
            writeCategoryItem(printProcess);
        }
        // deduction
        Map<EmployeeKey, Double> mapDeduction = findMapCategory(mapAmount, SalaryCategory.Deduction);
        if (!mapDeduction.isEmpty()) {
            printProcess.mapAmount = mapDeduction;
            printProcess.category = SalaryCategory.Deduction;
            writeCategoryItem(printProcess);
        }
        // attendance
        Map<EmployeeKey, Double> mapAttendance = findMapCategory(mapAmount, SalaryCategory.Attendance);
        if (!mapAttendance.isEmpty()) {
            printProcess.mapAmount = mapAttendance;
            printProcess.category = SalaryCategory.Attendance;
            writeCategoryItem(printProcess);
        }
        // article others
        Map<EmployeeKey, Double> mapArticleOthers = findMapCategory(mapAmount, SalaryCategory.ArticleOthers);
        if (!mapArticleOthers.isEmpty()) {
            printProcess.mapAmount = mapArticleOthers;
            printProcess.category = SalaryCategory.ArticleOthers;
            writeCategoryItem(printProcess);
        }

        // ========== TITLE REPORT ===========
        writeTitleRow(printProcess);

        // ========== WRITE NUMBER EMPLOYEE FOOTER REPORT ===========
        writeNumberEmployee(printProcess);
    }

    /**
     * Write title row.
     *
     * @param printProcess the print process
     */
    private void writeTitleRow(PrintProcess printProcess) {
        if (printProcess.mapTitle == null) {
            return;
        }
        Cells cells = printProcess.worksheet.getCells();
        int indexRow = PaymentConstant.INDEX_ROW_TITLE;
//        cells.setRowHeight(indexRow, PaymentConstant.ROW_HEIGHT_TITLE);
        StyleModel styleModel = new StyleModel(PaymentConstant.LIGHT_BLUE_COLOR);
        
        for (Map.Entry<Integer, String> entry : printProcess.mapTitle.entrySet()) {
            int indexColumn = entry.getKey();
            String item = entry.getValue();
            Cell cell = cells.get(indexRow, indexColumn);
            cell.setValue(item);
            styleModel.drawBorderCell(cell);
        }
    }

    /**
     * Find map category.
     *
     * @param mapAmount the map amount
     * @param category the category
     * @return the map
     */
    private Map<EmployeeKey, Double> findMapCategory(Map<EmployeeKey, Double> mapAmount, SalaryCategory category) {
        return mapAmount.entrySet().stream()
                .sorted((p1, p2) -> p1.getKey().getYearMonth().compareTo(p2.getKey().getYearMonth()))
                .filter(item -> item.getKey().getSalaryCategory() == category)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    /**
     * Write category item.
     *
     * @param printProcess the print process
     */
    private void writeCategoryItem(PrintProcess printProcess) {
        Map<EmployeeKey, Double> mapAmount = printProcess.mapAmount;
        // ========= FIND LIST ITEM NAME =========
        List<String> itemNames = mapAmount.entrySet().stream()
                .sorted((p1, p2) -> p1.getKey().getOrderItemName().compareTo(p2.getKey().getOrderItemName()))
                .map(p -> p.getKey().getItemName())
                .distinct()
                .collect(Collectors.toList());
        printProcess.indexRowHeaderCategory = printProcess.indexRow;
        printProcess.indexRow++;
        int indexArr = 0;
        
        // ========= PRINT ITEM OF A CATEGORY =========
        for (String itemName : itemNames) {
            printProcess.isForebackground = false;
            if (indexArr % PaymentConstant.SECOND != PaymentConstant.ZERO) {
                printProcess.isForebackground = true;
            }
            calAmountByItemName(printProcess, itemName);

            // ===== UPDATE VARIABLE ====
            printProcess.totalColumn = printProcess.indexColumn;
            printProcess.indexColumn = PaymentConstant.ZERO;
            printProcess.indexRow++;
            printProcess.isHasTitleRow = true;
            printProcess.mapAmountDepMonths.clear();
            indexArr++;
        }
        // ========= WRITE HEADER CATEGORY =========
        writeHeaderCategory(printProcess);
    }

    /**
     * Write header category.
     *
     * @param printProcess the print process
     */
    private void writeHeaderCategory(PrintProcess printProcess) {
        String header;
        // ====== FIND HEADER BY CATEGORY ======
        switch (printProcess.category) {
            case Payment :
                header = "【支給項目】";
                break;
            case Deduction :
                header = "【控除項目】";
                break;
            case Attendance :
                header = "【勤怠項目】";
                break;
            case ArticleOthers :
                header = "【記事項目】";
                break;
            default :
                header = "";
        }
        Cells cells = printProcess.worksheet.getCells();
        Cell cell = cells.get(printProcess.indexRowHeaderCategory, PaymentConstant.ZERO);
        cell.setValue(header);

        int numberColumn = printProcess.totalColumn;
        StyleModel styleModel = new StyleModel();
        styleModel.setHorizontalBorder(printProcess.configure.getIsHorizontalLine());
        styleModel.setVerticalBorder(printProcess.configure.getIsVerticalLine());
        styleModel.drawHorizontalEdgePage(cells, numberColumn, printProcess.indexRowHeaderCategory);
        styleModel.drawBorderLineRow(cells, numberColumn, printProcess.indexRowHeaderCategory);
    }

    /**
     * Cal amount by item name.
     *
     * @param printProcess the print process
     * @param itemName the item name
     */
    private void calAmountByItemName(PrintProcess printProcess, String itemName) {
        // ========== WRITE ITEM NAME ==========
        writeItemName(printProcess, itemName);

        // ========== DECLARE VARIABLE =========
        Map<EmployeeKey, Double> mapCategory = printProcess.mapAmount;
        Map<EmployeeKey, Double> mapAmountItemName = subMapAmount(mapCategory, itemName);
        Stack<DepartmentDto> stackDep = new Stack<>();
        List<EmployeeDto> employees = printProcess.employees;
        Iterator<EmployeeDto> iteratorEmp = employees.iterator();
        EmployeeDto prevEmp = null;

        // list code employee in department.
        List<String> codeEmpDeps = new ArrayList<>();
        DepartmentDto lastDep = null;

        // ========== FIND AND PRINT EMPLOYEES ===========
        while (iteratorEmp.hasNext()) {
            EmployeeDto currEmp = iteratorEmp.next();
            if (prevEmp == null) {
                prevEmp = currEmp;
                codeEmpDeps.add(currEmp.getCode());
                // print amount of employee monthly and total all month.
                writeEmployee(printProcess, mapAmountItemName, currEmp);
                continue;
            }
            DepartmentDto prevDep = prevEmp.getDepartment();
            DepartmentDto currDep = currEmp.getDepartment();
            lastDep = currDep;
            // check employees in department
            if (currDep.getCode().equals(prevDep.getCode())) {
                // print employee same a department
                writeEmployee(printProcess, mapAmountItemName, currEmp);
                codeEmpDeps.add(currEmp.getCode());
                continue;
            }
            
            // ==================================== JUMP NEW DEPARTMENT  ====================================
            stackDep.push(prevDep);
            // write department monthly and total department all month.
            writeTotalDep(printProcess, mapAmountItemName, codeEmpDeps, prevDep);
            
            // ========= SET EMPLOYEE CODE OF NEW DEPARMENT =========
            codeEmpDeps.clear();
            codeEmpDeps.add(currEmp.getCode());
            
            // ========= CHECK CURRENT DEPARTMENT =========
            if (!currDep.getDepPath().startsWith(prevDep.getDepPath())) {
                // print calculate cumulative of previous department
                stackDep = findSameLevelDep(printProcess, stackDep, currDep);
            }
            
            // ========= PRIN EMPLOYEE OF NEW DEPARTMENT =========
            writeEmployee(printProcess, mapAmountItemName, currEmp);
            prevEmp = currEmp;
        }
        // write total monthly and total all of last department.
        if (lastDep != null) {
            writeTotalDep(printProcess, mapAmountItemName, codeEmpDeps, lastDep);
            // write cumulative monthly and total of department.
            writeCumulateDep(printProcess, lastDep, new ArrayList<>());
        }

        // write total departments monthly and all department
        writeTotalAllDep(printProcess);
    }

    /**
     * Write item name.
     *
     * @param printProcess the print process
     * @param itemName the item name
     */
    private void writeItemName(PrintProcess printProcess, String itemName) {
        Cell cell = printProcess.worksheet.getCells().get(printProcess.indexRow, printProcess.indexColumn);
        cell.setValue(itemName);

        StyleModel styleModel = new StyleModel();
        styleModel.setHorizontalBorder(printProcess.configure.getIsHorizontalLine());
        styleModel.setVerticalBorder(printProcess.configure.getIsVerticalLine());
        styleModel.setBorderType(CellsBorderType.VerticalBorder);

        if (printProcess.isForebackground) {
            styleModel.setForegroundColor(PaymentConstant.LIGHT_GREEN_COLOR);
        }
        styleModel.drawBorderCell(cell);
        
        if (printProcess.mapTitle == null) {
            printProcess.mapTitle = new HashMap<>();
        }
        if (PaymentConstant.OUTPUT_FORMAT_TYPE_CUMULATIVE.equals(printProcess.configure.getOutputFormatType())) {
            printProcess.mapTitle.put(printProcess.indexColumn, "");
        } else {
            printProcess.mapTitle.put(printProcess.indexColumn, "社員");
        }
        printProcess.indexColumn++;
    }

    /**
     * Write employee.
     *
     * @param printProcess the print process
     * @param mapAmountItemName the map amount item name
     * @param emp the emp
     */
    private void writeEmployee(PrintProcess printProcess, Map<EmployeeKey, Double> mapAmountItemName, EmployeeDto emp) {
        // map amount of a employee. It can a month or multiple month.
        Map<EmployeeKey, Double> amountEmp = mapAmountItemName.entrySet().stream()
                .filter(p -> p.getKey().getEmployeeCode().equals(emp.getCode()))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        // ========= NOT PRINT EMPLOYEE WHO HAS AMOUNT =========
        if (amountEmp.isEmpty()) {
            return;
        }
        int indexRow = printProcess.indexRow;
        int indexColumn = printProcess.indexColumn;
        SalaryPrintSettingDto configure = printProcess.configure;
        
        // ======= SET STYLE ======
        StyleModel styleModel = new StyleModel();
        styleModel.setHorizontalBorder(printProcess.configure.getIsHorizontalLine());
        styleModel.setVerticalBorder(printProcess.configure.getIsVerticalLine());
        styleModel.setBorderType(CellsBorderType.VerticalBorder);
        styleModel.setFormatNumber(PaymentConstant.FORMAT_NUMBER);

        if (printProcess.isForebackground) {
            styleModel.setForegroundColor(PaymentConstant.LIGHT_GREEN_COLOR);
        }
        Cells cells = printProcess.worksheet.getCells();
        // write amount of a employee.
        double totalEmployee = PaymentConstant.ZERO;
        Map<Integer, String> mapTitle = printProcess.mapTitle;
        
        // ====== WRITE EMPLOYEE MONTHLY ======
        for (Integer yearMonth : printProcess.yearMonths) {
            List<Double> amountMonth = amountEmp.entrySet().stream()
                    .filter(p -> p.getKey().getYearMonth().equals(yearMonth)).map(p -> p.getValue())
                    .collect(Collectors.toList());
            for (Double amount : amountMonth) {
                if (configure.getOutputFormatType().equals(PaymentConstant.OUTPUT_FORMAT_TYPE_DETAIL)
                        && configure.getShowPayment()) {
                    Cell cell = cells.get(indexRow, indexColumn);
                    cell.setValue(amount);
                    styleModel.drawBorderCell(cell);
                    styleModel.drawBorderLastColPageIfNeed(cell);
                    // write title row
                    if (!printProcess.isHasTitleRow) {
                        String titleRow = convertYearMonthJP(yearMonth) + "\n" + emp.getCode() + "\n" + emp.getName();
                        mapTitle.put(indexColumn, titleRow);
                    }
                    indexColumn++;
                }
            }
            // if multiple month, will write amount total of employee monthly.
            double totalAmountMonthly = amountMonth.stream()
                    .mapToDouble(p -> p.doubleValue())
                    .sum();
            totalEmployee += totalAmountMonthly;
            if (configure.getOutputFormatType().equals(PaymentConstant.OUTPUT_FORMAT_TYPE_DETAIL)
                    && configure.getSumMonthPersonSet()) {
                Cell cellTotalMonthly = cells.get(indexRow, indexColumn);
                cellTotalMonthly.setValue(totalAmountMonthly);
                styleModel.drawBorderCell(cellTotalMonthly);
                styleModel.drawBorderLastColPageIfNeed(cellTotalMonthly);
                // write title row
                if (!printProcess.isHasTitleRow) {
                    String titleRow = convertYearMonthJP(yearMonth) + "\n社員月計\n" + emp.getCode() + "\n"
                            + emp.getName();
                    mapTitle.put(indexColumn, titleRow);
                }
                indexColumn++;
            }
        }
        
        // ====== WRITE EMPLOYEE TOTAL ======
        if (configure.getOutputFormatType().equals(PaymentConstant.OUTPUT_FORMAT_TYPE_DETAIL)
                && configure.getSumPersonSet()) {
            Cell cellTotal = cells.get(indexRow, indexColumn);
            cellTotal.setValue(totalEmployee);
            styleModel.drawBorderCell(cellTotal);
            styleModel.drawBorderLastColPageIfNeed(cellTotal);
            // write title row
            if (!printProcess.isHasTitleRow) {
                String titleRow = "個人計\n" + emp.getCode() + "\n" + emp.getName();
                mapTitle.put(indexColumn, titleRow);
            }
            if (PaymentConstant.PAGE_BREAK_EMPLOYEE.equals(printProcess.configure.getPageBreakSetting())) {
                styleModel.drawBorderRight(cellTotal);
                indexColumn = breakPage(printProcess, indexColumn);
            } else {
                indexColumn++;
            }
        }
        printProcess.mapTitle = mapTitle;
        printProcess.indexColumn = indexColumn;
    }

    /**
     * Write total dep.
     *
     * @param printProcess the print process
     * @param mapAmountItemName the map amount item name
     * @param codeEmps the code emps
     * @param prevDep the prev dep
     */
    private void writeTotalDep(PrintProcess printProcess, Map<EmployeeKey, Double> mapAmountItemName,
            List<String> codeEmps, DepartmentDto prevDep) {
        int indexRow = printProcess.indexRow;
        int indexColumn = printProcess.indexColumn;
        
        // ====== SET STYLE ======
        StyleModel styleModel = new StyleModel();
        styleModel.setHorizontalBorder(printProcess.configure.getIsHorizontalLine());
        styleModel.setVerticalBorder(printProcess.configure.getIsVerticalLine());
        styleModel.setBorderType(CellsBorderType.DoubleVerticalBorder);
        styleModel.setFormatNumber(PaymentConstant.FORMAT_NUMBER);
        // =======================
        
        if (printProcess.isForebackground) {
            styleModel.setForegroundColor(PaymentConstant.LIGHT_GREEN_COLOR);
        }
        double totalDep = PaymentConstant.ZERO;
        Cells cells = printProcess.worksheet.getCells();
        Map<Integer, String> mapTitle = printProcess.mapTitle;
        if (printProcess.mapFooter == null) {
            printProcess.mapFooter = new HashMap<>();
        }
        Map<DepartmentDto, Double> mapAmountDepMonths = printProcess.mapAmountDepMonths;
        if (mapAmountDepMonths == null) {
            mapAmountDepMonths = new HashMap<>();
        }
        SalaryPrintSettingDto configure = printProcess.configure;
        int totalEmp = 0;
        
        // ====== WRITE AMOUNT A DEPARTMENT MONTHLY ======
        for (Integer yearMonth : printProcess.yearMonths) {
            double amountMonth = mapAmountItemName.entrySet().stream()
                    .filter(p -> p.getKey().getYearMonth().equals(yearMonth) 
                            && codeEmps.contains(p.getKey().getEmployeeCode()))
                    .mapToDouble(p -> p.getValue())
                    .sum();
            totalDep += amountMonth;
            DepartmentDto dep = DepartmentDto.newDepartment(prevDep, yearMonth);
            mapAmountDepMonths.put(dep, amountMonth);
            if (configure.getSumMonthDeprtSet()) {
                Cell cell = cells.get(indexRow, indexColumn);
                cell.setValue(amountMonth);
                styleModel.drawBorderCell(cell);

                // write title row
                if (!printProcess.isHasTitleRow) {
                    String titleRow = convertYearMonthJP(yearMonth) + "\n部門月計\n" + prevDep.getCode()
                        + prevDep.getName();
                    mapTitle.put(indexColumn, titleRow);
                }
                totalEmp += codeEmps.size();
                // count number employee
                String numberEmp = codeEmps.size() + "人";
                printProcess.mapFooter.put(indexColumn, numberEmp);

                indexColumn++;
            }
        }
        
        // ====== WRITE AMOUNT A DEPARTMENT TOTAL =======
        if (configure.getSumEachDeprtSet()) {
            // write total amount a department in many months.
            Cell cellTotal = cells.get(indexRow, indexColumn);
            cellTotal.setValue(totalDep);
            styleModel.drawBorderCell(cellTotal);

            // write title row
            if (!printProcess.isHasTitleRow) {
                String titleRow = "\n部門計\n" + prevDep.getCode() + prevDep.getName();
                mapTitle.put(indexColumn, titleRow);
            }
            String numberEmp = totalEmp + "人";
            printProcess.mapFooter.put(indexColumn, numberEmp);

            if (PaymentConstant.PAGE_BREAK_DEPARTMENT.equals(printProcess.configure.getPageBreakSetting())) {
                styleModel.drawBorderRight(cellTotal);
                indexColumn = breakPage(printProcess, indexColumn);
            } else {
                indexColumn++;
            }
        }
        printProcess.indexColumn = indexColumn;
        printProcess.mapTitle = mapTitle;
        printProcess.mapAmountDepMonths = mapAmountDepMonths;
    }

    /**
     * Find same level dep.
     *
     * @param printProcess the print process
     * @param stackDep the stack dep
     * @param currentDep the current dep
     * @return the stack
     */
    private Stack<DepartmentDto> findSameLevelDep(PrintProcess printProcess, Stack<DepartmentDto> stackDep,
            DepartmentDto currentDep) {
        Stack<DepartmentDto> newStack = new Stack<>();
        newStack.addAll(stackDep);
        
        List<DepartmentDto> tmpDeps = new ArrayList<>();
        boolean isEqualDepLevel = false;
        while (!newStack.isEmpty()) {
            DepartmentDto prevDep = newStack.pop();
            tmpDeps.add(prevDep);
            if (currentDep.getDepLevel() == prevDep.getDepLevel()) {
                calCumulateDepMonthly(printProcess, tmpDeps, prevDep);
                // remove department code: C, D, E --> remove E (D contains E)
                removeTempDep(tmpDeps, prevDep);
                isEqualDepLevel = true;
            }
            // write cumulative department.
            writeCumulateDep(printProcess, prevDep, tmpDeps);
            if (isEqualDepLevel) {
                break;
            }
        }
        return newStack;
    }
    
    /**
     * Removes the temp dep.
     *
     * @param tmpDeps the tmp deps
     * @param prevDep the prev dep
     */
    private void removeTempDep(List<DepartmentDto> tmpDeps, DepartmentDto prevDep) {
        for (int i = 0; i < tmpDeps.size(); i++) {
            if (tmpDeps.get(i) == prevDep) {
                break;
            } else {
                tmpDeps.remove(i);
            }
        }
    }

    /**
     * Write cumulate dep.
     *
     * @param printProcess the print process
     * @param prevDep the prev dep
     * @param tmpDeps the tmp deps
     */
    private void writeCumulateDep(PrintProcess printProcess, DepartmentDto prevDep, List<DepartmentDto> tmpDeps) {
        if (!printProcess.selectedLevels.contains(prevDep.getDepLevel())) {
            return;
        }
        Cells cells = printProcess.worksheet.getCells();
        int indexRow = printProcess.indexRow;
        int indexColumn = printProcess.indexColumn;
        Map<Integer, String> mapTitle = printProcess.mapTitle;
        calCumulateDepMonthly(printProcess, tmpDeps, prevDep);
        Map<DepartmentDto, Double> mapAmountDepMonths = printProcess.mapAmountDepMonths;
        double totalCumulate = 0;
        
        // ====== SET STYLE ======
        StyleModel styleModel = new StyleModel();
        styleModel.setHorizontalBorder(printProcess.configure.getIsHorizontalLine());
        styleModel.setVerticalBorder(printProcess.configure.getIsVerticalLine());
        styleModel.setBorderType(CellsBorderType.DoubleVerticalBorder);
        styleModel.setFormatNumber(PaymentConstant.FORMAT_NUMBER);

        if (printProcess.isForebackground) {
            styleModel.setForegroundColor(PaymentConstant.LIGHT_GREEN_COLOR);
        }

        // ====== WRITE CUMULATIVE MONTH DEPARTMENT ======
        for (Integer yearMonth : printProcess.yearMonths) {
            prevDep.setYearMonth(yearMonth);
            double amount = mapAmountDepMonths.get(prevDep);
            totalCumulate += amount;
            if (printProcess.configure.getSumMonthDepHrchySet()) {
                Cell cell = cells.get(indexRow, indexColumn);
                cell.setValue(amount);
                styleModel.drawBorderCell(cell);

                if (!printProcess.isHasTitleRow) {
                    String titleRow = convertYearMonthJP(yearMonth) + "\n部門階層月計\n" + prevDep.getCode()
                    + prevDep.getName();
                    mapTitle.put(indexColumn, titleRow);
                }
                indexColumn++;
            }
        }
        
        // ====== WRITE CUMULATIVE DEPARTMENT TOTAL ======
        if (printProcess.configure.getSumDepHrchyIndexSet()) {
            Cell cellTotal = cells.get(indexRow, indexColumn);
            cellTotal.setValue(totalCumulate);
            styleModel.drawBorderCell(cellTotal);

            if (!printProcess.isHasTitleRow) {
                String titleRow = "\n部門階層累計\n" + prevDep.getCode() + prevDep.getName();
                mapTitle.put(indexColumn, titleRow);
            }

            if (PaymentConstant.PAGE_BREAK_HIERARCHY_DEPARTMENT.equals(printProcess.configure.getPageBreakSetting())
                    && printProcess.configure.getHierarchy().equals(prevDep.getDepLevel().toString())) {
                styleModel.drawBorderRight(cellTotal);
                indexColumn = breakPage(printProcess, indexColumn);
            } else {
                indexColumn++;
            }
        }

        printProcess.indexColumn = indexColumn;
        printProcess.mapTitle = mapTitle;
    }

    /**
     * Write total all dep.
     *
     * @param printProcess the print process
     */
    private void writeTotalAllDep(PrintProcess printProcess) {
        Cells cells = printProcess.worksheet.getCells();
        int indexRow = printProcess.indexRow;
        int indexColumn = printProcess.indexColumn;
        Map<Integer, String> mapTitle = printProcess.mapTitle;
        Map<DepartmentDto, Double> mapAmountDepMonths = printProcess.mapAmountDepMonths;
        double totalCumulate = 0;
        
        // ====== SET STYLE =======
        StyleModel styleModel = new StyleModel();
        styleModel.setHorizontalBorder(printProcess.configure.getIsHorizontalLine());
        styleModel.setVerticalBorder(printProcess.configure.getIsVerticalLine());
        styleModel.setBorderType(CellsBorderType.DoubleVerticalBorder);
        styleModel.setFormatNumber(PaymentConstant.FORMAT_NUMBER);

        if (printProcess.isForebackground) {
            styleModel.setForegroundColor(PaymentConstant.LIGHT_GREEN_COLOR);
        }

        // ====== WIRITE AMOUNT MONTH TOTAL ======
        for (Integer yearMonth : printProcess.yearMonths) {
            double amount = mapAmountDepMonths.entrySet().stream()
                    .filter(p -> p.getKey().getYearMonth().equals(yearMonth))
                    .mapToDouble(p -> p.getValue())
                    .sum();
            totalCumulate += amount;
            if (printProcess.configure.getMonthTotalSet()) {
                Cell cell = cells.get(indexRow, indexColumn);
                cell.setValue(amount);
                styleModel.drawBorderCell(cell);

                if (!printProcess.isHasTitleRow) {
                    String titleRow = convertYearMonthJP(yearMonth) + "\n総合 月計\n";
                    mapTitle.put(indexColumn, titleRow);
                }
                indexColumn++;
            }
        }
        // ====== WRITE AMOUNT TOTAL ======
        if (printProcess.configure.getTotalSet()) {
            Cell cellTotal = cells.get(indexRow, indexColumn);
            cellTotal.setValue(totalCumulate);
            styleModel.drawBorderCell(cellTotal);

            if (!printProcess.isHasTitleRow) {
                String titleRow = "総合計\n";
                mapTitle.put(indexColumn, titleRow);
            }
            indexColumn++;
        }

        printProcess.indexColumn = indexColumn;
        printProcess.mapTitle = mapTitle;
    }

    /**
     * Write number employee.
     *
     * @param printProcess the print process
     */
    private void writeNumberEmployee(PrintProcess printProcess) {
        if (printProcess.mapFooter == null) {
            return;
        }
        Cells cells = printProcess.worksheet.getCells();

        Cell cellName = cells.get(printProcess.indexRow, PaymentConstant.ZERO);
        cellName.setValue("件数");

        for (int indexColum : printProcess.mapFooter.keySet()) {
            String numberEmp = printProcess.mapFooter.get(indexColum);
            Cell cell = cells.get(printProcess.indexRow, indexColum);
            cell.setValue(numberEmp);
        }
        int numberColumn = printProcess.totalColumn;
        
        // ====== SET STYLE FOR ROW NUMBER EMPLOYEE ======
        StyleModel styleModel = new StyleModel(PaymentConstant.LIGHT_BLUE_COLOR);
        styleModel.setHorizontalBorder(printProcess.configure.getIsHorizontalLine());
        styleModel.setVerticalBorder(printProcess.configure.getIsVerticalLine());
        styleModel.drawHorizontalEdgePage(cells, numberColumn, printProcess.indexRow);
        styleModel.drawBorderLineRow(cells, numberColumn, printProcess.indexRow);

        printProcess.indexRow++;
    }

    /**
     * Cal cumulate dep monthly.
     *
     * @param printProcess the print process
     * @param tmpDeps the tmp deps
     * @param dep the dep
     */
    private void calCumulateDepMonthly(PrintProcess printProcess, List<DepartmentDto> tmpDeps, DepartmentDto dep) {
        Map<DepartmentDto, Double> mapAmountDepMonths = printProcess.mapAmountDepMonths;
        List<String> depCodes = tmpDeps.stream()
                .map(p -> p.getCode())
                .collect(Collectors.toList());
        
        // ====== UPDATE AMOUNT MONTHLY OF DEPARTMENT ======
        for (Integer yearMonth : printProcess.yearMonths) {
            DepartmentDto newDep = DepartmentDto.newDepartment(dep, yearMonth);
            double amount = mapAmountDepMonths.entrySet().stream()
                    .filter(p -> p.getKey().getYearMonth().equals(yearMonth)
                            && depCodes.contains(p.getKey().getCode()))
                    .mapToDouble(p -> p.getValue())
                    .sum();
            if (amount > PaymentConstant.ZERO) {
                mapAmountDepMonths.remove(newDep);
                mapAmountDepMonths.put(newDep, amount);
            }
        }
        printProcess.mapAmountDepMonths = mapAmountDepMonths;
    }

    /**
     * Break page.
     *
     * @param printProcess the print process
     * @param indexColumn the index column
     * @return the int
     */
    private int breakPage(PrintProcess printProcess, int indexColumn) {
        int numColumnExist = indexColumn % PaymentConstant.NUMBER_COLUMN_PAGE;
        int numColumnBlank = PaymentConstant.NUMBER_COLUMN_PAGE - numColumnExist;

        StyleModel styleModel = new StyleModel();
        styleModel.setHorizontalBorder(printProcess.configure.getIsHorizontalLine());
        styleModel.setVerticalBorder(printProcess.configure.getIsVerticalLine());
        Cells cells = printProcess.worksheet.getCells();
        
        // ====== DRAW BORDER FIRST AND LAST COLUMN OF ROW HEADER CATEGORY ======
        Cell cellStartPage = cells.get(printProcess.indexRowHeaderCategory, indexColumn - numColumnExist);
        styleModel.drawBorderLeft(cellStartPage);

        Cell cellEndPage = cells.get(printProcess.indexRowHeaderCategory, indexColumn);
        styleModel.drawBorderRight(cellEndPage);

        return indexColumn + numColumnBlank;
    }

    /**
     * Sub map amount.
     *
     * @param mapAmount the map amount
     * @param itemName the item name
     * @return the map
     */
    private Map<EmployeeKey, Double> subMapAmount(Map<EmployeeKey, Double> mapAmount, String itemName) {
        return mapAmount.entrySet().stream()
                .filter(p -> p.getKey().getItemName().equals(itemName))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    /**
     * Distinct by key.
     *
     * @param <T> the generic type
     * @param keyExtractor the key extractor
     * @return the predicate
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * Convert year month JP.
     *
     * @param yearMonth the year month
     * @return the string
     */
    private String convertYearMonthJP(Integer yearMonth) {
        String firstDay = "01";
        String tmpDate = yearMonth.toString().concat(firstDay);
        String dateFormat = "yyyyMMdd";
        GeneralDate generalDate = GeneralDate.fromString(tmpDate, dateFormat);
        JapaneseDate japaneseDate = this.japaneseProvider.toJapaneseDate(generalDate);
        return japaneseDate.era() + japaneseDate.year() + "年" + japaneseDate.month() + "月度"; 
    }

    @Setter
    @Getter
    class StyleModel {

        /** The foreground color. */
        private Color foregroundColor;
        
        /** The format number. */
        private String formatNumber;
        
        /** The border type. */
        private CellsBorderType borderType;
        
        /** The is vertical border. */
        private boolean isVerticalBorder;
        
        /** The is horizontal border. */
        private boolean isHorizontalBorder;

        /**
         * Instantiates a new style model.
         */
        public StyleModel() {
            this.foregroundColor = Color.getWhite();
            this.borderType = CellsBorderType.CommonBorder;
            this.isVerticalBorder = true;
            this.isHorizontalBorder = true;
        }

        /**
         * Instantiates a new style model.
         *
         * @param background the background
         */
        public StyleModel(Color background) {
            this.foregroundColor = background;
            this.borderType = CellsBorderType.CommonBorder;
            this.isVerticalBorder = true;
            this.isHorizontalBorder = true;
        }

        /**
         * Draw title report.
         *
         * @param cell the cell
         */
        public void drawTitleReport(Cell cell) {
            Style style = this.findStyleCell(cell, this.borderType);
            style.setHorizontalAlignment(TextAlignmentType.CENTER);
            cell.setStyle(style);
        }

        /**
         * Draw horizontal edge page.
         *
         * @param cells the cells
         * @param numberColumn the number column
         * @param indexRow the index row
         */
        public void drawHorizontalEdgePage(Cells cells, int numberColumn, int indexRow) {
            Cell firstCellRow = cells.get(indexRow, 0);
            this.drawBorderLeft(firstCellRow);

            int indexLastColumn = numberColumn - PaymentConstant.ONE;
            Cell lastCellRow = cells.get(indexRow, indexLastColumn);
            this.drawBorderRight(lastCellRow);
        }

        /**
         * Draw border cell.
         *
         * @param cell the cell
         */
        public void drawBorderCell(Cell cell) {
            Style style = this.findStyleCell(cell, this.borderType);
            if (this.formatNumber != null) {
                style.setCustom(this.formatNumber);
                style.setHorizontalAlignment(TextAlignmentType.RIGHT);
            }
            cell.setStyle(style);
        }

        /**
         * Draw double border.
         *
         * @param cell the cell
         * @param borderType the border type
         */
        public void drawDoubleBorder(Cell cell, CellsBorderType borderType) {
            Style style = this.findStyleCell(cell, borderType);
            cell.setStyle(style);
        }

        /**
         * Draw border line row.
         *
         * @param cells the cells
         * @param numberColumn the number column
         * @param indexRow the index row
         */
        public void drawBorderLineRow(Cells cells, int numberColumn, int indexRow) {
            for (int i = 0; i < numberColumn; i++) {
                Cell currentCell = cells.get(indexRow, i);
                Style style = this.findStyleCell(currentCell, CellsBorderType.HorizontalBorder);
                currentCell.setStyle(style);

                // ====== DRAW BORDER LAST AND FIRST COLUMN IN A PAGE ======
                if (i > PaymentConstant.ONE) {
                    this.drawBorderLastColPageIfNeed(currentCell);
                }
                
                Cell cellAbove = cells.get(indexRow - 1, i);
                Cell cellBelow = cells.get(indexRow + 1, i);
                // ====== CLEAR BORDER COLUMN HAS VALUE EMPTY ======
                if (cellAbove.getValue() == null && currentCell.getValue() == null && cellBelow.getValue() == null) {
                    this.clearBorderColumnEmpty(cells, indexRow, i);
                }

            }
        }

        /**
         * Draw border last column page if need.
         *
         * @param cell the cell
         */
        public void drawBorderLastColPageIfNeed(Cell cell) {
            int indexColumn = cell.getColumn();
            Style newStyle = null;
            int indexReal = indexColumn + PaymentConstant.ONE;
            if (indexReal % PaymentConstant.NUMBER_COLUMN_PAGE == PaymentConstant.ZERO) {
                newStyle = this.findStyleCell(cell, CellsBorderType.RightBorder);
            } else if (indexReal % PaymentConstant.NUMBER_COLUMN_PAGE == PaymentConstant.ONE) {
                newStyle = this.findStyleCell(cell, CellsBorderType.LeftBorder);
            }
            if (newStyle != null) {
                cell.setStyle(newStyle);
            }
        }

        /**
         * Clear border column empty.
         *
         * @param cells the cells
         * @param indexRow the index row
         * @param indexColumn the index column
         */
        public void clearBorderColumnEmpty(Cells cells, int indexRow, int indexColumn) {
            // ====== FIND CELL AROUND ======
            Cell currentCell = cells.get(indexRow, indexColumn);
            Style style = this.findStyleCell(currentCell, CellsBorderType.NoBorder);
            style.setForegroundColor(Color.getWhite());
            currentCell.setStyle(style);
            
            Cell prevCellAbove = cells.get(indexRow - 1, (indexColumn - 1) >= 0 ? (indexColumn - 1) : 0);
            Cell nextCellAbove = cells.get(indexRow - 1, indexColumn + 1);
            Cell prevCellBelow = cells.get(indexRow + 1, (indexColumn - 1) >= 0 ? (indexColumn - 1) : 0);
            Cell nextCellBelow = cells.get(indexRow + 1, indexColumn + 1);
            
            // ====== SET RIGHT BORDER - LAST COLUMN OF A PAGE ======
            if (prevCellAbove.getValue() != null || prevCellBelow.getValue() != null) {
                Cell prevCell = cells.get(indexRow, (indexColumn - 1) >= 0 ? (indexColumn - 1) : 0);
                Style styleLeft = this.findStyleCell(prevCell, CellsBorderType.RightBorder);
                prevCell.setStyle(styleLeft);
            }
            
            // ====== SET LEFT BORDER - FIRST COLUMN OF A PAGE
            if (nextCellAbove.getValue() != null || nextCellBelow.getValue() != null) {
                Cell nextCell = cells.get(indexRow, indexColumn + 1);
                Style styleLeft = this.findStyleCell(nextCell, CellsBorderType.LeftBorder);
                nextCell.setStyle(styleLeft);
            }
        }

        /**
         * Draw border left.
         *
         * @param cell the cell
         */
        public void drawBorderLeft(Cell cell) {
            Style style = this.findStyleCell(cell, CellsBorderType.LeftBorder);
            cell.setStyle(style);
        }

        /**
         * Draw border right.
         *
         * @param cell the cell
         */
        public void drawBorderRight(Cell cell) {
            Style style = this.findStyleCell(cell, CellsBorderType.RightBorder);
            cell.setStyle(style);
        }

        /**
         * Find style cell.
         *
         * @param cell the cell
         * @param borderType the border type
         * @return the style
         */
        private Style findStyleCell(Cell cell, CellsBorderType borderType) {
            Style style = cell.getStyle();
            style.setForegroundColor(this.foregroundColor);
            style.setPattern(BackgroundType.SOLID);
            style.setTextWrapped(true);

            switch (borderType) {
                case NoBorder :
                    style.setBorder(BorderType.LEFT_BORDER, CellBorderType.NONE, Color.getWhite());
                    style.setBorder(BorderType.TOP_BORDER, CellBorderType.NONE, Color.getWhite());
                    style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.NONE, Color.getWhite());
                    style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.NONE, Color.getWhite());
                    break;
                case CommonBorder :
                    if (this.isVerticalBorder) {
                        style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
                        style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
                    }
                    if (this.isHorizontalBorder) {
                        style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
                        style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
                    }
                    break;
                case VerticalBorder :
                    if (this.isVerticalBorder) {
                        style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
                        style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
                    }
                    break;
                case HorizontalBorder :
                    if (this.isHorizontalBorder) {
                        style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
                        style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
                    }
                    break;
                case LeftBorder :
                    if (this.isVerticalBorder) {
                        style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
                    }
                    break;
                case RightBorder :
                    if (this.isVerticalBorder) {
                        style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
                    }
                    break;
                case DoubleVerticalBorder :
                    if (this.isVerticalBorder) {
                        style.setBorder(BorderType.LEFT_BORDER, CellBorderType.DOUBLE, Color.getBlack());
                        style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.DOUBLE, Color.getBlack());
                    }
                    break;
                default :
                    break;
            }
            return style;
        }
    }

    /**
     * The Enum CellsBorderType.
     */
    enum CellsBorderType {
        
        /** The No border. */
        NoBorder,
        
        /** The Common border. */
        CommonBorder,
        
        /** The Vertical border. */
        VerticalBorder,
        
        /** The Horizontal border. */
        HorizontalBorder,
        
        /** The Left border. */
        LeftBorder,
        
        /** The Right border. */
        RightBorder,
        
        /** The Double vertical border. */
        DoubleVerticalBorder
    }

    /**
     * The Class PrintProcess.
     */
    class PrintProcess {
        
        /** The worksheet. */
        Worksheet worksheet;
        
        /** The index row. */
        int indexRow;
        
        /** The index column. */
        int indexColumn;
        
        /** The index row header category. */
        int indexRowHeaderCategory;
        
        /** The total column. */
        int totalColumn;
        
        /** The is fore background. */
        boolean isForebackground;
        
        /** The is has title row. */
        boolean isHasTitleRow;
        
        /** The map title. */
        Map<Integer, String> mapTitle;
        
        /** The map footer. */
        Map<Integer, String> mapFooter;
        
        /** The category. */
        SalaryCategory category;
        
        /** The employees. */
        List<EmployeeDto> employees;
        
        /** The departments. */
        List<DepartmentDto> departments;
        
        /** The selected levels. */
        List<Integer> selectedLevels;
        
        /** The map amount. */
        Map<EmployeeKey, Double> mapAmount;
        
        /** The year months. */
        List<Integer> yearMonths;
        
        /** The map amount dep months. */
        Map<DepartmentDto, Double> mapAmountDepMonths;
        
        /** The configure. */
        SalaryPrintSettingDto configure;
    }
}
