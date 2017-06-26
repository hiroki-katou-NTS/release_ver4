/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.insurance.salary;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import nts.gul.reflection.ReflectionUtil;
import nts.uk.file.pr.app.export.insurance.data.ChecklistPrintSettingDto;
import nts.uk.file.pr.app.export.insurance.data.DataRowItem;
import nts.uk.file.pr.app.export.insurance.data.HeaderReportData;
import nts.uk.file.pr.app.export.insurance.data.InsuranceOfficeDto;
import nts.uk.file.pr.app.export.insurance.data.SocialInsuReportData;
import nts.uk.file.pr.app.export.insurance.salary.SocialInsuGenerator;
import nts.uk.pr.file.infra.insurance.ReportConstant;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

/**
 * The Class AsposeSalarySocialInsuranceReportGenerator.
 */
@Stateless
public class AsposeSocialInsuReportGenerator extends AsposeCellsReportGenerator
        implements SocialInsuGenerator {

    /** The Constant TEMPLATE_FILE. */
    private static final String TEMPLATE_FILE = "report/QPP018_1.xlsx";

    /** The Constant PRINT_AREA. */
    private static final String PRINT_AREA = "A1:Q";

    /** The Constant NUMBER_COLUMN. */
    private static final int NUMBER_COLUMN = 17;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * nts.uk.file.pr.app.export.insurance.salary.SalarySocialInsuranceGenerator
     * #generate(nts.arc.layer.infra.file.export.FileGeneratorContext,
     * nts.uk.file.pr.app.export.insurance.data.SalarySocialInsuranceReportData)
     */
    @Override
    public void generate(FileGeneratorContext fileContext, List<SocialInsuReportData> listReport) {
        try (val reportContext = this.createContext(TEMPLATE_FILE)) {
            // set workbook for personal
            Workbook workbookPersonal = reportContext.getWorkbook();
            workbookPersonal.calculateFormula(true);
            WorksheetCollection worksheets = workbookPersonal.getWorksheets();
            SocialInsuReportData reportPersonal = listReport.get(ReportConstant.NUMBER_ZERO);
            createNewSheet(worksheets, ReportConstant.SHEET_NAME, reportPersonal);
            reportContext.getDesigner().setWorkbook(workbookPersonal);
            reportContext.getDesigner().setDataSource(ReportConstant.HEADER, Arrays.asList(reportPersonal
                    .getHeaderData()));
            reportContext.processDesigner();
            
            // set workbook for company
            SocialInsuReportData reportCompany = listReport.get(ReportConstant.NUMBER_ONE);
            Workbook workbookCompany = createWorkbook(reportCompany);
            
            // combine personal and company workbook 
            workbookPersonal.combine(workbookCompany);
            
            reportContext.saveAsPdf(this.createNewFile(fileContext,
                    this.getReportName(ReportConstant.REPORT_FILE_NAME)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Creates the workbook.
     *
     * @param reportData the report data
     * @return the workbook
     */
    private Workbook createWorkbook(SocialInsuReportData reportData) {
        Workbook workbook = null;
        try (val reportContext = this.createContext(TEMPLATE_FILE)) {
            workbook = reportContext.getWorkbook();
            WorksheetCollection worksheets = workbook.getWorksheets();
            createNewSheet(worksheets, ReportConstant.SHEET_NAME, reportData);
            reportContext.getDesigner().setWorkbook(workbook);
            workbook.calculateFormula(true);
            reportContext.getDesigner().setDataSource(ReportConstant.HEADER, Arrays.asList(reportData.getHeaderData()));
            reportContext.processDesigner();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return workbook;
    }
    
    /**
     * Creates the new sheet.
     *
     * @param worksheets the worksheets
     * @param sheetName the sheet name
     * @param reportData the report data
     * @throws Exception the exception
     */
    private void createNewSheet(WorksheetCollection worksheets, String sheetName,
            SocialInsuReportData reportData) throws Exception {
        Worksheet worksheet = worksheets.get(ReportConstant.NUMBER_ZERO);
        worksheet.setName(sheetName);
        worksheet.autoFitColumns();
        int numberOfColumn = NUMBER_COLUMN;
        
        PrintProcess printProcess = new PrintProcess();
        printProcess.worksheet = worksheet;

        HashMap<String, Range> mapRange = new HashMap<String, Range>();
        // get range from template then remove it.
        Range rangeOffice = worksheets.getRangeByName(ReportConstant.RANGE_OFFICE);
        mapRange.put(ReportConstant.RANGE_OFFICE, rangeOffice);
        Range rangeEmployee = worksheets.getRangeByName(ReportConstant.RANGE_EMPLOYEE);
        mapRange.put(ReportConstant.RANGE_EMPLOYEE, rangeEmployee);
        Range rangeFooterEachOffice = worksheets.getRangeByName(ReportConstant.RANGE_FOOTER_EACH_OFFICE);
        mapRange.put(ReportConstant.RANGE_FOOTER_EACH_OFFICE, rangeFooterEachOffice);
        Range rangeDeliveryNoticeAmount = worksheets.getRangeByName(ReportConstant.RANGE_DELIVERY_NOTICE_AMOUNT);
        Range rangeChildRaising = worksheets.getRangeByName(ReportConstant.RANGE_CHILD_RAISING);

        ChecklistPrintSettingDto configOutput = reportData.getConfigureOutput();
        printProcess.configOutput = configOutput;
        
        setColumnWidth(worksheet, numberOfColumn, ReportConstant.COLUMN_WIDTH);
        
        // Begin write report
        writeContentArea(printProcess, reportData, mapRange);
        
        if (configOutput.getShowDeliveryNoticeAmount()) {
            Range rangeDelivery = rangeDeliveryNoticeAmount;
            Range rangeChild = rangeChildRaising;
            // it is printing for company(not personal).
            if (!reportData.getIsCompany()) {
                writeFooterPage(printProcess, reportData, rangeDelivery, rangeChild);
            }
        }
        removeRowTemplate(worksheets.getNamedRanges().length, worksheet, mapRange);
        int totalRow = printProcess.indexRow;
        String printArea = PRINT_AREA.concat(String.valueOf(totalRow));
        settingPage(worksheet, printArea, reportData.getHeaderData());
        drawBorderLinePageBreak(worksheet, totalRow, numberOfColumn);
        
    }

    /**
     * Sets the column width.
     *
     * @param worksheet the worksheet
     * @param numberColumn the number column
     * @param columnWidth the column width
     */
    private void setColumnWidth(Worksheet worksheet, int numberColumn, double columnWidth) {
        worksheet.getCells().setColumnWidth(ReportConstant.NUMBER_ZERO, ReportConstant.COLUMN_WIDTH_OFFICE_CODE);
        for (int i = ReportConstant.NUMBER_ONE; i < numberColumn; i++) {
            worksheet.getCells().setColumnWidth(i, columnWidth);
        }
    }

    /**
     * Draw border line page break.
     *
     * @param worksheet the worksheet
     * @param totalRow the total row
     * @param numberOfColumn the number of column
     */
    private void drawBorderLinePageBreak(Worksheet worksheet, int totalRow, int numberOfColumn) {
        for (int i = ReportConstant.NUMBER_SECOND; i < totalRow; i++) {
            int indexOfArray = i - ReportConstant.NUMBER_ONE;
            if (i % ReportConstant.NUMBER_LINE_OF_PAGE == ReportConstant.NUMBER_ZERO) {
                drawBorderLine(worksheet, indexOfArray, numberOfColumn, BorderType.BOTTOM_BORDER);
            } else if (i % ReportConstant.NUMBER_LINE_OF_PAGE == ReportConstant.NUMBER_ONE) {
                drawBorderLine(worksheet, indexOfArray, numberOfColumn, BorderType.TOP_BORDER);
            }
        }
    }

    /**
     * Setting page.
     *
     * @param worksheet the worksheet
     * @param printArea the print area
     * @param header the header
     */
    private void settingPage(Worksheet worksheet, String printArea, HeaderReportData header) {
        PageSetup pageSetup = worksheet.getPageSetup();
        pageSetup.setPrintArea(printArea);
        pageSetup.setCenterHorizontally(true);
        pageSetup.setCenterVertically(false);
        
        // set header
        pageSetup.setHeader(0,"&\"IPAPGothic\"&11 " + header.getNameCompany());
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        pageSetup.setHeader(2,"&\"IPAPGothic\"&11 " + dateFormat.format(new Date()) + "\n&P ページ");
    }

    /**
     * Write content area.
     *
     * @param printProcess the print process
     * @param reportData the report data
     * @param mapRange the map range
     * @throws Exception the exception
     */
    private void writeContentArea(PrintProcess printProcess, SocialInsuReportData reportData,
            HashMap<String, Range> mapRange) throws Exception {
        List<InsuranceOfficeDto> offices = reportData.getOfficeItems();
        printProcess.indexRow = ReportConstant.INDEX_ROW_CONTENT_AREA;
        boolean isCompany = reportData.getIsCompany();
        for (InsuranceOfficeDto office : offices) {
            writeHeaderOffice(printProcess, office, mapRange.get(ReportConstant.RANGE_OFFICE), isCompany);
            int indexRowContentOffice = printProcess.indexRow;
            if (printProcess.configOutput.getShowDetail()) {
                writeContentOffice(printProcess, office, mapRange.get(ReportConstant.RANGE_EMPLOYEE));
            }
            // setting show total each of office.
            if (printProcess.configOutput.getShowOffice()) {
                writeFooterEachOffice(printProcess, office, indexRowContentOffice,
                        mapRange.get(ReportConstant.RANGE_FOOTER_EACH_OFFICE));
            }
        }
        // setting show total all office.
        if (printProcess.configOutput.getShowTotal()) {
            writeSummaryOffice(printProcess, reportData.getTotalAllOffice(),
                    mapRange.get(ReportConstant.RANGE_FOOTER_EACH_OFFICE));
        }
    }

    /**
     * Write header office.
     *
     * @param printProcess the print process
     * @param office the office
     * @param rangeOffice the range office
     * @param isCompany the is print company
     * @throws Exception the exception
     */
    private void writeHeaderOffice(PrintProcess printProcess, InsuranceOfficeDto office, Range rangeOffice, 
            boolean isCompany) throws Exception {
        Range newRange = createRangeFromOtherRange(printProcess, rangeOffice);
        String officeCode = ReportConstant.OFFICE_JP.concat(office.getCode());
        setDataRangeFirstRow(printProcess.worksheet, newRange, ReportConstant.NUMBER_ZERO, officeCode,
                office.getName());
        
        if (!isCompany) {
            int indexColumnLast = NUMBER_COLUMN - ReportConstant.NUMBER_ONE;
            printProcess.worksheet.getCells().hideColumn(indexColumnLast);
            Style style = newRange.get(ReportConstant.NUMBER_ZERO, indexColumnLast - ReportConstant.NUMBER_ONE)
                    .getStyle();
            style.setPattern(BackgroundType.SOLID);
            style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
            newRange.get(ReportConstant.NUMBER_ZERO, indexColumnLast - ReportConstant.NUMBER_ONE).setStyle(style);
        }
        printProcess.indexRow++;
    }

    /**
     * Write content office.
     *
     * @param printProcess the print process
     * @param office the office
     * @param rangeEmployee the range employee
     * @throws Exception the exception
     */
    private void writeContentOffice(PrintProcess printProcess, InsuranceOfficeDto office, Range rangeEmployee)
            throws Exception {
        for (int i = 0; i < office.getEmployeeDtos().size(); i++) {
            boolean isForegroundColor = false;
            if (i % ReportConstant.NUMBER_SECOND != 0) {
                isForegroundColor = true;
            }
            DataRowItem employee = office.getEmployeeDtos().get(i);
            Range newRange = createRangeFromOtherRange(printProcess, rangeEmployee);
            writeDataEmployee(printProcess, employee, newRange, isForegroundColor);
            printProcess.indexRow++;
        }
    }

    /**
     * Write footer each office.
     *
     * @param printProcess the print process
     * @param office the office
     * @param indexRowBeginEmployee the index row begin employee
     * @param rangeFooterEachOffice the range footer each office
     * @throws Exception the exception
     */
    private void writeFooterEachOffice(PrintProcess printProcess, InsuranceOfficeDto office, int indexRowBeginEmployee,
            Range rangeFooterEachOffice) throws Exception {
        int numberEmployeeOffice = office.getEmployeeDtos().size();
        String totalEmployeeOffice = String.valueOf(numberEmployeeOffice).concat(" 人");
        Range newRange = createRangeFromOtherRange(printProcess, rangeFooterEachOffice);
        setDataRangeFirstRow(printProcess.worksheet, newRange, ReportConstant.NUMBER_ZERO, "事業所　計",
                totalEmployeeOffice);
        setDataTotal(printProcess.worksheet, newRange, office.getTotalEachOffice());
        printProcess.indexRow++;
    }

    /**
     * Write summary office.
     *
     * @param printProcess the print process
     * @param totalAllOffice the total all office
     * @param rangeFooterOffice the range footer office
     * @throws Exception the exception
     */
    private void writeSummaryOffice(PrintProcess printProcess, DataRowItem totalAllOffice,
            Range rangeFooterOffice) throws Exception {
        Range newRange = createRangeFromOtherRange(printProcess, rangeFooterOffice);
        setDataRangeFirstRow(printProcess.worksheet, newRange, ReportConstant.NUMBER_ZERO, "総合計");
        setDataTotal(printProcess.worksheet, newRange, totalAllOffice);
        printProcess.indexRow++;
    }

    /**
     * Write footer page.
     *
     * @param printProcess the print process
     * @param reportData the report data
     * @param rangeDeliveryNoticeAmount the range delivery notice amount
     * @param rangeChildRaising the range child raising
     * @throws Exception the exception
     */
    private void writeFooterPage(PrintProcess printProcess, SocialInsuReportData reportData,
            Range rangeDeliveryNoticeAmount, Range rangeChildRaising)
                   throws Exception {
        printProcess.indexRow++;
        Range newRangeDeliveryNoticeAmount = createRangeFromOtherRange(printProcess, rangeDeliveryNoticeAmount);
        printProcess.indexRow++;
        Range newRangeChildRaising = createRangeFromOtherRange(printProcess, rangeChildRaising);
        setDataRangeFirstRow(printProcess.worksheet, newRangeDeliveryNoticeAmount, ReportConstant.INDEX_COLUMN_DELIVERY, 
                reportData.getDeliveryNoticeAmount());
        setDataRangeFirstRow(printProcess.worksheet, newRangeDeliveryNoticeAmount, ReportConstant.INDEX_COLUMN_INSURED, 
                reportData.getInsuredCollectAmount());
        setDataRangeRowCalculateSub(printProcess.worksheet, newRangeDeliveryNoticeAmount, newRangeChildRaising, 
                reportData.getChildRaisingTotalCompany());
    }

    /**
     * Write data employee.
     *
     * @param printProcess the print process
     * @param rowData the row data
     * @param range the range
     * @param isForegroundColor the is foreground color
     */
    @SuppressWarnings("rawtypes")
    private void writeDataEmployee(PrintProcess printProcess, DataRowItem rowData, Range range,
            boolean isForegroundColor) {
        Cells cells = printProcess.worksheet.getCells();
        int indexRowCurrent = range.getFirstRow();
        List valueOfAttributes = convertObjectToList(rowData);
        int numberColumn = valueOfAttributes.size();
        for (int i = 0; i < numberColumn; i++) {
            Object item = valueOfAttributes.get(i);
            cells.get(indexRowCurrent, i).setValue(item);
            if (isForegroundColor) {
                setForegroundColorByRow(printProcess.worksheet, indexRowCurrent, i);
            }
        }
    }

    /**
     * Sets the data range first row.
     *
     * @param worksheet the worksheet
     * @param range the range
     * @param indexColumnBegin the index column begin
     * @param data the data
     */
    private void setDataRangeFirstRow(Worksheet worksheet, Range range, int indexColumnBegin, Object... data) {
        Cells cells = worksheet.getCells();
        int indexRowCurrent = range.getFirstRow();
        int index = ReportConstant.NUMBER_ZERO;
        while (index < data.length) {
            int indexColumn = indexColumnBegin + index;
            cells.get(indexRowCurrent, indexColumn).setValue(data[index]);
            index++;
        }
    }

    /**
     * Sets the data total.
     *
     * @param worksheet the worksheet
     * @param range the range
     * @param total the total
     */
    @SuppressWarnings("rawtypes")
    private void setDataTotal(Worksheet worksheet, Range range, DataRowItem total) {
        Cells cells = worksheet.getCells();
        int indexRowCurrent = range.getFirstRow();
        List valueOfAttributes = convertObjectToList(total);
        int numberColumnNeedFill = valueOfAttributes.size();
        for (int i = ReportConstant.NUMBER_ZERO; i < numberColumnNeedFill; i++) {
            int indexColumn = i + ReportConstant.NUMBER_SECOND;
            cells.get(indexRowCurrent, indexColumn).setValue(valueOfAttributes.get(i));
        }
    }

    /**
     * Sets the data range row calculate sub.
     *
     * @param worksheet the worksheet
     * @param newRangeDeliveryNoticeAmount the new range delivery notice amount
     * @param newRangeChildRaising the new range child raising
     * @param totalChildRaisingBusiness the total child raising business
     */
    private void setDataRangeRowCalculateSub(Worksheet worksheet, Range newRangeDeliveryNoticeAmount,
            Range newRangeChildRaising, double totalChildRaisingBusiness) {
        Cells cells = worksheet.getCells();
        int indexRowCurrent = newRangeDeliveryNoticeAmount.getFirstRow();
        double cellStartValue = (double) cells.get(indexRowCurrent, ReportConstant.INDEX_COLUMN_DELIVERY).getValue();
        double cellEndValue = (double) cells.get(indexRowCurrent, ReportConstant.INDEX_COLUMN_INSURED).getValue();
        double valueBurden = cellStartValue - cellEndValue;
        Cell cellBurden = cells.get(indexRowCurrent, ReportConstant.INDEX_COLUMN_CHILD_RAISING);
        cellBurden.setValue(valueBurden);
        double childRaising = valueBurden - totalChildRaisingBusiness;
        cells.get(newRangeChildRaising.getFirstRow(), ReportConstant.INDEX_COLUMN_CHILD_RAISING).setValue(childRaising);
    }

    /**
     * Creates the range from other range.
     *
     * @param printProcess the print process
     * @param range the range
     * @return the range
     * @throws Exception the exception
     */
    private Range createRangeFromOtherRange(PrintProcess printProcess, Range range) throws Exception {
        Cells cells = printProcess.worksheet.getCells();
        String cellStart = ReportConstant.ALPHABET_A.concat(String.valueOf(printProcess.indexRow));
        String cellEnd = ReportConstant.ALPHABET_Q.concat(String.valueOf(printProcess.indexRow));
        Range newRange = cells.createRange(cellStart, cellEnd);
        newRange.copy(range);
        return newRange;
    }

    /**
     * Removes the row template.
     *
     * @param totalRange the total range
     * @param worksheet the worksheet
     * @param mapRange the map range
     */
    private void removeRowTemplate(int totalRange, Worksheet worksheet, HashMap<String, Range> mapRange) {
        int count = ReportConstant.NUMBER_ZERO;
        while (count <= totalRange) {
            worksheet.getCells().deleteRow(mapRange.get(ReportConstant.RANGE_OFFICE).getFirstRow());
            count++;
        }
    }

    /**
     * Draw border line.
     *
     * @param worksheet the worksheet
     * @param indexRow the index row
     * @param numberColumn the number column
     * @param offset the offset
     */
    private void drawBorderLine(Worksheet worksheet, int indexRow, int numberColumn, int offset) {
        Cells cells = worksheet.getCells();
        for (int i = 0; i < numberColumn; i++) {
            Style style = cells.get(indexRow, i).getStyle();
            style.setPattern(BackgroundType.SOLID);
            style.setBorder(offset, CellBorderType.THIN, Color.getBlack());
            cells.get(indexRow, i).setStyle(style);
        }
    }

    /**
     * Sets the foreground color by row.
     *
     * @param worksheet the worksheet
     * @param indexRow the index row
     * @param indexColumn the index column
     */
    private void setForegroundColorByRow(Worksheet worksheet, int indexRow, int indexColumn) {
        Cells cells = worksheet.getCells();
        Style style = cells.get(indexRow, indexColumn).getStyle();
        style.setForegroundArgbColor(Integer.parseInt(ReportConstant.COLOR_EMPLOYEE_HEX, ReportConstant.RADIX));
        cells.get(indexRow, indexColumn).setStyle(style);
    }
    
    /**
     * Convert object to list.
     *
     * @param obj the obj
     * @return the list
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private List convertObjectToList(Object obj) {
        List valueOfAttributes = new ArrayList<>();
        if (obj == null) {
            return valueOfAttributes;
        }
        if (obj instanceof DataRowItem) {
            obj = (DataRowItem) obj;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object val = ReflectionUtil.getFieldValue(field, obj);
            Object item = null;
            if (val == null) {
                // item = "";
                continue;
            }
            if (val instanceof String) {
                item = (String) val;
            } else if (val instanceof Double) {
                item = (Double) val;
            }
            valueOfAttributes.add(item);
        }
        return valueOfAttributes;
    }
    
    /**
     * The Class PrintProcess.
     */
    class PrintProcess {
        
        /** The worksheet. */
        Worksheet worksheet;
        
        /** The index row. */
        int indexRow;
        
        /** The configure output. */
        ChecklistPrintSettingDto configOutput;
    }
}
