package nts.uk.file.at.infra.manhoursummarytable;

import com.aspose.cells.*;
import lombok.val;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.workrecord.workmanagement.manhoursummarytable.*;
import nts.uk.file.at.app.export.manhoursummarytable.ManHourSummaryExportData;
import nts.uk.file.at.app.export.manhoursummarytable.ManHourSummaryTableGenerator;
import nts.uk.screen.at.app.kha003.b.ManHourPeriod;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AsposeManHourSummaryTableGenerator extends AsposeCellsReportGenerator implements ManHourSummaryTableGenerator {
    private static final String FILE_TITLE = "職場別作業集計表";
    private static final String TEMPLATE_FILE = "report/KHA003.xlsx";
    private static final String EXCEL_EXTENSION = ".xlsx";
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String VERTICAL_TOTAL = "KHA003_99";
    private static final String HORIZONTAL_TOTAL = "KHA003_98";
    private static final String TOTAL = "KHA003_100";
    private static final int MAX_COLUMN_TEMPLATE = 36;

    @Override
    public void generate(FileGeneratorContext generatorContext, ManHourSummaryExportData dataSource) {
        try {
            AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE);
            Workbook workbook = reportContext.getWorkbook();
            WorksheetCollection worksheets = workbook.getWorksheets();
            String title = FILE_TITLE;

            val isDisplayTotal = dataSource.getSummaryTableFormat().getDetailFormatSetting().getDisplayVerticalHorizontalTotal().value == 1;
            Worksheet worksheetTemplate = isDisplayTotal ? worksheets.get(0) : worksheets.get(1);
            Worksheet worksheet = worksheets.get(2);
            worksheet.setName(title);

            pageSetting(worksheet, title);
            printContents(worksheetTemplate, worksheet, dataSource, title);
            worksheets.removeAt(1);
            worksheets.removeAt(0);
            worksheets.setActiveSheetIndex(0);
            reportContext.processDesigner();
            val fileName = title + "_" + GeneralDateTime.now().toString("yyyyMMddHHmmss");
            reportContext.saveAsExcel(this.createNewFile(generatorContext, fileName + EXCEL_EXTENSION));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void pageSetting(Worksheet worksheet, String title) {
        String companyName = "";
        PageSetup pageSetup = worksheet.getPageSetup();
        pageSetup.setPaperSize(PaperSizeType.PAPER_A_4);
        pageSetup.setOrientation(PageOrientationType.LANDSCAPE);

        pageSetup.setHeader(0, "&9&\"ＭＳ フォントサイズ\"" + companyName);
        pageSetup.setHeader(1, "&16&\"ＭＳ フォントサイズ,Bold\"" + title);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern("yyyy/MM/dd  H:mm", Locale.JAPAN);
        pageSetup.setHeader(2,
                "&9&\"MS フォントサイズ\"" + LocalDateTime.now().format(dateTimeFormatter) + "\n" +
                        TextResource.localize("page") + " &P");
        pageSetup.setFitToPagesTall(0);
        pageSetup.setFitToPagesWide(0);
        pageSetup.setCenterHorizontally(true);
        pageSetup.setBottomMarginInch(1.5);
        pageSetup.setTopMarginInch(1.5);
        pageSetup.setLeftMarginInch(1.0);
        pageSetup.setRightMarginInch(1.0);
        pageSetup.setHeaderMarginInch(0.8);
        pageSetup.setZoom(100);
    }

    private void printContents(Worksheet worksheetTemplate, Worksheet worksheet, ManHourSummaryExportData data, String title) throws Exception {
        val detailFormatSetting = data.getSummaryTableFormat().getDetailFormatSetting();
        val dispFormat = detailFormatSetting.getDisplayFormat();
        val totalUnit = detailFormatSetting.getTotalUnit();
        val isDisplayTotal = detailFormatSetting.getDisplayVerticalHorizontalTotal().value == 1;
        val outputContent = data.getOutputContent();

        String dateRange = totalUnit == TotalUnit.DATE
                ? data.getPeriod().getDatePeriod().start().toString(DATE_FORMAT) + "　～　" + data.getPeriod().getDatePeriod().end().toString(DATE_FORMAT)
                : data.getPeriod().getYearMonthPeriod().start().toString() + "　～　" + data.getPeriod().getYearMonthPeriod().end().toString();
        int maxDateRange = totalUnit == TotalUnit.DATE ? data.getPeriod().getDateList().size() : data.getPeriod().getYearMonthList().size();
        int totalLevel = data.getTotalLevel();
        if (totalLevel == 0) return;
        val headerList = getHeaderList(data.getPeriod(), detailFormatSetting, isDisplayTotal);
        int countColumn = headerList.size();

        Cells cellsTemplate = worksheetTemplate.getCells();
        Cells cells = worksheet.getCells();
        cells.copyRows(cellsTemplate, 0, 0, 3);  // Copy 3 row

        // Delete column name thừa
        if (totalLevel == 1) {
            cells.deleteColumns(1, 3, true);
        }
        if (totalLevel == 2) {
            cells.deleteColumns(2, 2, true);
        }
        if (totalLevel == 3) {
            cells.deleteColumns(3, 1, true);
        }

        // Check total column
        int maxColumnTemplate = isDisplayTotal ? MAX_COLUMN_TEMPLATE : (MAX_COLUMN_TEMPLATE - 1);
        int columnHandle = checkTotalColumn(maxColumnTemplate, countColumn);
        if (columnHandle < 0) {
            for (int i = 1; i <= Math.abs(columnHandle); i++) {
                cells.copyColumns(cellsTemplate, 5, headerList.size(), i);
            }
        }

//        double columnWith = 0.41;
//        for (int i = 0; i <= 3 ; i++) {
//            cells.setColumnWidthInch(i,columnWith);
//        }
//        for (int i = 0; i <= headerList.size() - 4 ; i++) {
//            cells.setColumnWidthInch(i,columnWith);
//        }
        cells.clearContents(0, 0, cells.getMaxRow(), cells.getMaxColumn());
        //A1_1
        cells.get(0, 0).setValue(title);
        //A1_2
        cells.get(1, 0).setValue(dateRange);
        //D2_1 -> D5_1 , D6_1... D7_1 : Print Header
        for (int i = 0; i <= headerList.size() - 1; i++) {
            String item = headerList.get(i);
            cells.get(2, i).setValue(item);
        }

        // Print data
        switch (totalLevel) {
            case 1:
                printData1Level(cellsTemplate, cells, outputContent, isDisplayTotal, maxDateRange, headerList, dispFormat, totalUnit);
                break;
            case 2:
                printData2Level(cellsTemplate, cells, outputContent, isDisplayTotal, maxDateRange, headerList, dispFormat, totalUnit);
                break;
            case 3:
                printData3Level(cellsTemplate, cells, outputContent, isDisplayTotal, maxDateRange, headerList, dispFormat, totalUnit);
                break;
            case 4:
                printData4Level(cellsTemplate, cells, outputContent, isDisplayTotal, maxDateRange, headerList, dispFormat, totalUnit);
                break;
        }

        // Xoa column date thừa
        if (columnHandle > 0) {
//            if (totalLevel == 1) {
//                cells.deleteColumns(headerList.size(), columnHandle - 3, true);
//            }
//            if (totalLevel == 2) {
//                cells.deleteColumns(headerList.size(), columnHandle - 2, true);
//            }
//            if (totalLevel == 3) {
//                cells.deleteColumns(headerList.size(), columnHandle - 1, true);
//            }
//            if (totalLevel == 4) {
                cells.deleteColumns(headerList.size(), columnHandle, true);
//            }
        }
    }

    private void printData1Level(Cells cellsTemplate, Cells cells, ManHourSummaryTableOutputContent outputContent, boolean isDispTotal, int maxDateRange, List<String> headerList, DisplayFormat dispFormat, TotalUnit unit) throws Exception {
        List<SummaryItemDetail> itemDetails = outputContent.getItemDetails();
        int countRow = 3;
        for (int i = 1; i <= itemDetails.size(); i++) {
            SummaryItemDetail level1 = itemDetails.get(i - 1);
            cells.copyRows(cellsTemplate, 4, countRow, 1);
            cells.get(countRow, 0).setValue(level1.getDisplayInfo().getName());
            cells.get(countRow, 1).setValue("");
            cells.get(countRow, 2).setValue("");
            cells.get(countRow, 3).setValue("");
            val workingTimeMap1 = this.getWorkingTimeByDate(unit, level1.getVerticalTotalList());
            for (int c = 1; c < maxDateRange + 1; c++) {
                cells.get(countRow, c).setValue(formatValue(Double.valueOf(workingTimeMap1.getOrDefault(headerList.get(c), 0)), dispFormat));
                setHorizontalAlignment(cells.get(countRow, c));
            }
            if (isDispTotal) {  // Tong chieu ngang level
                cells.get(countRow, headerList.size() - 1).setValue(formatValue(level1.getTotalPeriod().isPresent() ? Double.valueOf(level1.getTotalPeriod().get()) : 0, dispFormat));
                setHorizontalAlignment(cells.get(countRow, headerList.size() - 1));
            }
            countRow++;
        }
        if (isDispTotal) { // Tong chieu doc cua level 1
            cells.copyRows(cellsTemplate, 37, countRow, 1);
            printAllTotalByVertical(cells, outputContent, maxDateRange, headerList, dispFormat, unit, countRow, 0);
        }
    }

    private void printData2Level(Cells cellsTemplate, Cells cells, ManHourSummaryTableOutputContent outputContent, boolean isDispTotal, int maxDateRange, List<String> headerList, DisplayFormat dispFormat, TotalUnit unit) throws Exception {
        List<SummaryItemDetail> itemDetails = outputContent.getItemDetails();
        int countRow = 3;
        for (SummaryItemDetail level1 : itemDetails) {
            boolean isPrintNameLv1 = false;
            int mergeIndexLv1 = countRow;
            List<SummaryItemDetail> childHierarchyList = level1.getChildHierarchyList();
            for (int i = 1; i <= childHierarchyList.size(); i++) {
                SummaryItemDetail level2 = childHierarchyList.get(i - 1);
                cells.copyRows(cellsTemplate, 4, countRow, 1);
                cells.get(countRow, 0).setValue(!isPrintNameLv1 ? level1.getDisplayInfo().getName() : "");
                isPrintNameLv1 = true;
                cells.get(countRow, 1).setValue(level2.getDisplayInfo().getName());
                cells.get(countRow, 2).setValue("");
                cells.get(countRow, 3).setValue("");
                val workingTimeMap2 = this.getWorkingTimeByDate(unit, level2.getVerticalTotalList());
                for (int c = 2; c < maxDateRange + 2; c++) {
                    cells.get(countRow, c).setValue(formatValue(Double.valueOf(workingTimeMap2.getOrDefault(headerList.get(c), 0)), dispFormat));
                    setHorizontalAlignment(cells.get(countRow, c));
                }
                if (isDispTotal) {  // Tong chieu ngang level 3
                    cells.get(countRow, headerList.size() - 1).setValue(formatValue(level2.getTotalPeriod().isPresent() ? Double.valueOf(level2.getTotalPeriod().get()) : 0, dispFormat));
                    setHorizontalAlignment(cells.get(countRow, headerList.size() - 1));
                }
                countRow++;
            }
            if (isDispTotal) { // Tong chieu doc level 2
                cells.copyRows(cellsTemplate, 11, countRow, 1);
                printTotalByVerticalOfEachLevel(cells, level1, maxDateRange, headerList, dispFormat, unit, countRow, 1, 0);
                countRow++;
            }
            cells.merge(mergeIndexLv1, 0, countRow - mergeIndexLv1 - 1, 1, true, true);
            setVerticalAlignment(cells.get(mergeIndexLv1, 1));
        }
        if (isDispTotal) { // Tong chieu doc cua level 1
            cells.copyRows(cellsTemplate, 37, countRow, 1);
            printAllTotalByVertical(cells, outputContent, maxDateRange, headerList, dispFormat, unit, countRow, 1);
        }
    }

    private void printData3Level(Cells cellsTemplate, Cells cells, ManHourSummaryTableOutputContent outputContent, boolean isDispTotal, int maxDateRange, List<String> headerList, DisplayFormat dispFormat, TotalUnit unit) throws Exception {
        List<SummaryItemDetail> itemDetails = outputContent.getItemDetails();
        int countRow = 3;
        for (SummaryItemDetail level1 : itemDetails) {
            boolean isPrintNameLv1 = false;
            int mergeIndexLv1 = countRow;
            for (SummaryItemDetail level2 : level1.getChildHierarchyList()) {
                boolean isPrintNameLv2 = false;
                int mergeIndexLv2 = countRow;
                List<SummaryItemDetail> childHierarchyList = level2.getChildHierarchyList();
                for (int i = 1; i <= childHierarchyList.size(); i++) {
                    SummaryItemDetail level3 = childHierarchyList.get(i - 1);
                    cells.copyRows(cellsTemplate, 4, countRow, 1);
                    cells.get(countRow, 0).setValue(!isPrintNameLv1 ? level1.getDisplayInfo().getName() : "");
                    isPrintNameLv1 = true;
                    cells.get(countRow, 1).setValue(!isPrintNameLv2 ? level2.getDisplayInfo().getName() : "");
                    isPrintNameLv2 = true;
                    cells.get(countRow, 2).setValue(level3.getDisplayInfo().getName());
                    cells.get(countRow, 3).setValue("");
                    val workingTimeMap3 = this.getWorkingTimeByDate(unit, level3.getVerticalTotalList());
                    for (int c = 3; c < maxDateRange + 3; c++) {
                        cells.get(countRow, c).setValue(formatValue(Double.valueOf(workingTimeMap3.getOrDefault(headerList.get(c), 0)), dispFormat));
                        setHorizontalAlignment(cells.get(countRow, c));
                    }
                    if (isDispTotal) {  // Tong chieu ngang level 3
                        cells.get(countRow, headerList.size() - 1).setValue(formatValue(level3.getTotalPeriod().isPresent() ? Double.valueOf(level3.getTotalPeriod().get()) : 0, dispFormat));
                        setHorizontalAlignment(cells.get(countRow, headerList.size() - 1));
                    }
                    countRow++;
                }
                if (isDispTotal) { // Tong chieu doc level 3
                    cells.copyRows(cellsTemplate, 12, countRow, 1);
                    printTotalByVerticalOfEachLevel(cells, level2, maxDateRange, headerList, dispFormat, unit, countRow, 2, 1);
                    countRow++;
                }
                cells.merge(mergeIndexLv2, 1, countRow - mergeIndexLv2 - 1, 1, true, true);
                setVerticalAlignment(cells.get(mergeIndexLv2, 2));
            }
            if (isDispTotal) { // Tong chieu doc level 2
                cells.copyRows(cellsTemplate, 11, countRow, 1);
                printTotalByVerticalOfEachLevel(cells, level1, maxDateRange, headerList, dispFormat, unit, countRow, 2, 0);
                countRow++;
            }
            cells.merge(mergeIndexLv1, 0, countRow - mergeIndexLv1 - 1, 1, true, true);
            setVerticalAlignment(cells.get(mergeIndexLv1, 1));
        }
        if (isDispTotal) { // Tong chieu doc cua level 1
            cells.copyRows(cellsTemplate, 37, countRow, 1);
            printAllTotalByVertical(cells, outputContent, maxDateRange, headerList, dispFormat, unit, countRow, 2);
        }
    }

    private void printData4Level(Cells cellsTemplate, Cells cells, ManHourSummaryTableOutputContent outputContent, boolean isDispTotal, int maxDateRange, List<String> headerList, DisplayFormat dispFormat, TotalUnit unit) throws Exception {
        List<SummaryItemDetail> itemDetails = outputContent.getItemDetails();
        int countRow = 3;
        cells.get(0, 5).getStyle().setBackgroundColor(Color.getRed());
        for (SummaryItemDetail level1 : itemDetails) {
            boolean isPrintNameLv1 = false;
            int mergeIndexLv1 = countRow;
            for (SummaryItemDetail level2 : level1.getChildHierarchyList()) {
                boolean isPrintNameLv2 = false;
                int mergeIndexLv2 = countRow;
                for (SummaryItemDetail level3 : level2.getChildHierarchyList()) {
                    boolean isPrintNameLv3 = false;
                    int mergeIndexLv3 = countRow;
                    List<SummaryItemDetail> childHierarchyList = level3.getChildHierarchyList();
                    for (int i = 1; i <= childHierarchyList.size(); i++) {
                        if (i == childHierarchyList.size() && !isDispTotal) {
                            cells.copyRows(cellsTemplate, 14, countRow, 1);
                        } else {
                            cells.copyRows(cellsTemplate, 4, countRow, 1);
                        }

                        SummaryItemDetail level4 = childHierarchyList.get(i - 1);
                        cells.get(countRow, 0).setValue(!isPrintNameLv1 ? level1.getDisplayInfo().getName() : "");
                        isPrintNameLv1 = true;
                        cells.get(countRow, 1).setValue(!isPrintNameLv2 ? level2.getDisplayInfo().getName() : "");
                        isPrintNameLv2 = true;
                        cells.get(countRow, 2).setValue(!isPrintNameLv3 ? level3.getDisplayInfo().getName() : "");
                        isPrintNameLv3 = true;
                        cells.get(countRow, 3).setValue(level4.getDisplayInfo().getName());
                        val workingTimeMap4 = this.getWorkingTimeByDate(unit, level4.getVerticalTotalList());
                        for (int c = 4; c < maxDateRange + 4; c++) {
                            cells.get(countRow, c).setValue(formatValue(Double.valueOf(workingTimeMap4.getOrDefault(headerList.get(c), 0)), dispFormat));
                            setHorizontalAlignment(cells.get(countRow, c));
                        }
                        if (isDispTotal) {  // Tong chieu ngang level 4
                            cells.get(countRow, headerList.size() - 1).setValue(formatValue(level4.getTotalPeriod().isPresent() ? Double.valueOf(level4.getTotalPeriod().get()) : 0, dispFormat));
                            setHorizontalAlignment(cells.get(countRow, headerList.size() - 1));
                        }
                        countRow++;
                    }
                    if (isDispTotal) { // Tong chieu doc level 4
                        cells.copyRows(cellsTemplate, 6, countRow, 1);
                        printTotalByVerticalOfEachLevel(cells, level3, maxDateRange, headerList, dispFormat, unit, countRow, 3, 2);
                        countRow++;
                    }
                    cells.merge(mergeIndexLv3, 2, isDispTotal ? countRow - mergeIndexLv3 - 1 : countRow - mergeIndexLv3, 1, true, true);
                    setVerticalAlignment(cells.get(mergeIndexLv3, 2));
                }
                if (isDispTotal) { // Tong chieu doc level 3
                    cells.copyRows(cellsTemplate, 11, countRow, 1);
                    printTotalByVerticalOfEachLevel(cells, level2, maxDateRange, headerList, dispFormat, unit, countRow, 3, 1);
                    countRow++;
                }
                cells.merge(mergeIndexLv2, 1, isDispTotal ? countRow - mergeIndexLv2 - 1 : countRow - mergeIndexLv2, 1, true, true);
                setVerticalAlignment(cells.get(mergeIndexLv2, 1));
            }
            if (isDispTotal) { // Tong chieu doc level 2
                cells.copyRows(cellsTemplate, 21, countRow, 1);
                printTotalByVerticalOfEachLevel(cells, level1, maxDateRange, headerList, dispFormat, unit, countRow, 3, 0);
                countRow++;
            }
            cells.merge(mergeIndexLv1, 0, isDispTotal ? countRow - mergeIndexLv1 - 1 : countRow - mergeIndexLv1, 1, true, true);
            setVerticalAlignment(cells.get(mergeIndexLv1, 0));
        }
        if (isDispTotal) { // Tong chieu doc cua level 1
            cells.copyRows(cellsTemplate, 37, countRow, 1);
            printAllTotalByVertical(cells, outputContent, maxDateRange, headerList, dispFormat, unit, countRow, 3);
        }
    }

    // Total of each column of each level by vertical
    private void printTotalByVerticalOfEachLevel(Cells cells, SummaryItemDetail summaryItemDetail, int maxDateRange, List<String> headerList, DisplayFormat dispFormat, TotalUnit unit,
                                                 int row, int index, int columnNameIndex) {
        cells.get(row, columnNameIndex).setValue(summaryItemDetail.getDisplayInfo().getName() + TextResource.localize(TOTAL));
        for (int t = 1; t <= maxDateRange; t++) {
            val mapTotal = this.getWorkingTimeByDate(unit, summaryItemDetail.getVerticalTotalList());
            setHorizontalAlignment(cells.get(row, t + index));
            cells.get(row, t + index).setValue(formatValue(Double.valueOf(mapTotal.getOrDefault(headerList.get(t + index), 0)), dispFormat));
        }
        cells.get(row, headerList.size() - 1).setValue(formatValue(summaryItemDetail.getTotalPeriod().isPresent() ? Double.valueOf(summaryItemDetail.getTotalPeriod().get()) : 0, dispFormat));
    }

    // All total by vertical
    private void printAllTotalByVertical(Cells cells, ManHourSummaryTableOutputContent outputContent, int maxDateRange, List<String> headerList, DisplayFormat dispFormat,
                                         TotalUnit unit, int row, int index) {
        cells.get(row, 0).setValue(TextResource.localize(VERTICAL_TOTAL));
        for (int t = 1; t <= maxDateRange; t++) {
            val mapTotal = this.getWorkingTimeByDate(unit, outputContent.getVerticalTotalValues());
            setHorizontalAlignment(cells.get(row, t + index));
            cells.get(row, t + index).setValue(formatValue(Double.valueOf(mapTotal.getOrDefault(headerList.get(t + index), 0)), dispFormat));
        }
        cells.get(row, headerList.size() - 1).setValue(formatValue(outputContent.getTotalPeriod().isPresent() ? Double.valueOf(outputContent.getTotalPeriod().get()) : 0, dispFormat));
    }

    private int checkTotalColumn(int maxColumnTemplate, int countColumn) {
        int countColumnNeedHandle;
        if (countColumn < maxColumnTemplate) { // Thừa: > 0
            countColumnNeedHandle = maxColumnTemplate - countColumn;
        } else if (countColumn > maxColumnTemplate) { // Thiếu: < 0
            countColumnNeedHandle = maxColumnTemplate - countColumn;
        } else {
            countColumnNeedHandle = 0;
        }

        return countColumnNeedHandle;
    }

    private Map<String, Integer> getWorkingTimeByDate(TotalUnit unit, List<VerticalValueDaily> lstValueDaily) {
        Map<String, Integer> map = new HashMap<>();
        if (unit == TotalUnit.DATE)
            lstValueDaily.forEach(d -> map.put(d.getDate().toString(), d.getWorkingHours()));
        else
            lstValueDaily.forEach(d -> map.put(d.getDate().toString(), d.getWorkingHours()));

        return map;
    }

    private List<String> getHeaderList(ManHourPeriod period, DetailFormatSetting detailSetting, boolean isDispTotal) {
        List<String> lstHeader = new ArrayList<>();
        // Sort before adding
        val sortedList = detailSetting.getSummaryItemList().stream().sorted(Comparator.comparing(SummaryItem::getHierarchicalOrder)).collect(Collectors.toList());
        // Add code & name to header
        for (SummaryItem item : sortedList) {
            lstHeader.add(item.getSummaryItemType().nameId);
        }

        // Add date/yearMonth list to header
        if (detailSetting.getTotalUnit() == TotalUnit.DATE)
            period.getDateList().forEach(date -> lstHeader.add(date.toString()));
        else
            period.getYearMonthList().forEach(ym -> lstHeader.add(toYearMonthString(ym)));

        // Add horizontal total to header
        if (isDispTotal) lstHeader.add(TextResource.localize(HORIZONTAL_TOTAL));

        return lstHeader;
    }

    /**
     * Format value by display format
     *
     * @param value
     * @param displayFormat
     * @return String
     */
    private Double formatValue(Double value, DisplayFormat displayFormat) {
        Double targetValue = null;
        switch (displayFormat) {
            case DECIMAL:
                BigDecimal decimaValue = new BigDecimal(value);
                decimaValue = decimaValue.divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
                targetValue = decimaValue.doubleValue();
                break;
            case HEXA_DECIMAL:
                BigDecimal decimalValue = new BigDecimal(value);
                BigDecimal intValue = decimalValue.divideToIntegralValue(BigDecimal.valueOf(60));
                BigDecimal remainValue = decimalValue.subtract(intValue.multiply(BigDecimal.valueOf(60)));
                decimalValue = intValue.add(remainValue.divide(BigDecimal.valueOf(100), 3, RoundingMode.UNNECESSARY));
                targetValue = decimalValue.doubleValue();
                break;
            case MINUTE:
                NumberFormat df = new DecimalFormat("#0.0");
                targetValue = new Double(df.format(value));
                break;
        }

        return targetValue;
    }

    private void setVerticalAlignment(Cell cell) {
        Style style = cell.getStyle();
        style.setVerticalAlignment(TextAlignmentType.TOP);
        cell.setStyle(style);
    }

//    private void setBorderStyle(Cell cell) {
//        Style style = cell.getStyle();
//        style.setBorder(BorderType.TOP_BORDER, CellBorderType.THIN, Color.getBlack());
//        style.setBorder(BorderType.BOTTOM_BORDER, CellBorderType.THIN, Color.getBlack());
//        style.setBorder(BorderType.LEFT_BORDER, CellBorderType.THIN, Color.getBlack());
//        style.setBorder(BorderType.RIGHT_BORDER, CellBorderType.THIN, Color.getBlack());
//        cell.setStyle(style);
//    }

    private void setHorizontalAlignment(Cell cell) {
        Style style = cell.getStyle();
        style.setHorizontalAlignment(TextAlignmentType.RIGHT);
        cell.setStyle(style);
    }

    // Convert YearMonth to String with format: yyyy/MM
    private String toYearMonthString(YearMonth yearMonth) {
        return String.format("%04d/%02d", yearMonth.year(), yearMonth.month());
    }
}
