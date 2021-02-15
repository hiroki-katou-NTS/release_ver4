package nts.uk.file.at.infra.workledgeroutputitem;

import com.aspose.cells.*;
import lombok.val;
import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.CommonAttributesOfForms;
import nts.uk.ctx.at.function.dom.workledgeroutputitem.WorkLedgerDisplayContent;
import nts.uk.ctx.at.function.dom.workledgeroutputitem.WorkLedgerExportDataSource;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.enums.PrimitiveValueOfAttendanceItem;
import nts.uk.file.at.app.export.workledgeroutputitem.WorkLedgerOutputItemGenerator;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportGenerator;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AposeWorkLedgerOutputGenerator extends AsposeCellsReportGenerator implements WorkLedgerOutputItemGenerator {
    private static final String TEMPLATE_FILE_ADD = "report/KWR005.xlsx";
    private static final String EXCEL_EXT = ".xlsx";
    private static final String PRINT_AREA = "A1:O";
    private static final int NUMBER_ROW_OF_PAGE = 50;

    @Override
    public void generate(FileGeneratorContext generatorContext, WorkLedgerExportDataSource dataSource) {
        try {
            AsposeCellsReportContext reportContext = this.createContext(TEMPLATE_FILE_ADD);
            Workbook workbook = reportContext.getWorkbook();
            WorksheetCollection worksheets = workbook.getWorksheets();
            Worksheet worksheet = worksheets.get(0);
            worksheet.setName(dataSource.getTitle());
            if (!dataSource.getListContent().isEmpty()) {
                settingPage(worksheet, dataSource);
                printContents(worksheet, dataSource);
            }
            worksheets.setActiveSheetIndex(0);
            reportContext.processDesigner();
            String fileName = dataSource.getTitle() + "_" + GeneralDateTime.now().toString("yyyyMMddHHmmss");
            reportContext.saveAsExcel(this.createNewFile(generatorContext, fileName + EXCEL_EXT));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void settingPage(Worksheet worksheet, WorkLedgerExportDataSource dataSource) {
        PageSetup pageSetup = worksheet.getPageSetup();
        pageSetup.setPaperSize(PaperSizeType.PAPER_A_4);
        pageSetup.setOrientation(PageOrientationType.LANDSCAPE);
        String companyName = dataSource.getCompanyName();
        pageSetup.setHeader(0, "&7&\"ＭＳ フォントサイズ\"" + companyName);
        pageSetup.setHeader(1, "&9&\"ＭＳ フォントサイズ\""
                + dataSource.getTitle());

        DateTimeFormatter fullDateTimeFormatter = DateTimeFormatter
                .ofPattern("yyyy/MM/dd  H:mm", Locale.JAPAN);
        pageSetup.setHeader(2,
                "&9&\"MS フォントサイズ\"" + LocalDateTime.now().format(fullDateTimeFormatter) + "\n" +
                        TextResource.localize("page") + " &P");
        pageSetup.setZoom(100);
    }

    private void printContents(Worksheet worksheet, WorkLedgerExportDataSource dataSource) throws Exception {
        HorizontalPageBreakCollection pageBreaks = worksheet.getHorizontalPageBreaks();
        List<WorkLedgerDisplayContent> listContent = dataSource.getListContent();
        int count = 0;
        int itemOnePage = 0;
        Cells cells = worksheet.getCells();
        for (int i = 0; i < listContent.size(); i++) {
            val yearMonths = dataSource.getYearMonthPeriod().yearMonthsBetween();
            val content = listContent.get(i);
            if (i >= 1) {
                itemOnePage = 0;
                pageBreaks.add(count);
                cells.copyRow(cells, 0, count);
                cells.copyRow(cells, 1, count + 1);
                cells.copyRow(cells, 2, count + 2);
                cells.clearContents(count, 0, cells.getMaxRow(), 15);
            }
            cells.get(count, 0).setValue(TextResource.localize("KWR005_301") + "　" + content.getWorkplaceCode() + "　" + content.getWorkplaceName());
            cells.get(count, 7).setValue(TextResource.localize(TextResource.localize("KWR005_303")) +
                    this.toYearMonthString(dataSource.getYearMonthPeriod().start()) + TextResource.localize("KWR005_305") +
                    this.toYearMonthString(dataSource.getYearMonthPeriod().end()));
            cells.get(count + 1, 0).setValue(TextResource.localize("KWR005_302") + "　" + content.getEmployeeCode() + "　" + content.getEmployeeName());
            // print date
            printDate(worksheet, count + 2, yearMonths);
            count += 3;
            val data = content.getMonthlyDataList();
            for (int j = 0; j < data.size(); j++) {
                val oneLine = data.get(j);
                if (itemOnePage >= NUMBER_ROW_OF_PAGE) {
                    itemOnePage = 0;
                    pageBreaks.add(count);
                    cells.copyRow(cells, 0, count);
                    cells.copyRow(cells, 1, count + 1);
                    cells.copyRow(cells, 2, count + 2);
                    cells.clearContents(count, 0, cells.getMaxRow(), 15);
                    cells.get(count, 6).setValue(TextResource.localize("KWR005_303") +
                            this.toYearMonthString(dataSource.getYearMonthPeriod().start()) + TextResource.localize("KWR005_305") +
                            this.toYearMonthString(dataSource.getYearMonthPeriod().end()));

                    cells.get(count, 0).setValue("");
                    cells.get(count + 1, 0).setValue("");

                    printDate(worksheet, count + 2, yearMonths);
                    count += 3;

                }
                if (j % 2 == 0) {
                    cells.copyRow(cells, 3, count);
                } else {
                    cells.copyRow(cells, 4, count);
                }
                cells.clearContents(count, 0, cells.getMaxRow(), 15);
                cells.merge(count, 0, 1, 2, true, true);

                Cell cell = cells.get(count, 0);
                Style style = cell.getStyle();
                style.setVerticalAlignment(TextAlignmentType.LEFT);

                if(dataSource.isCode() && checkCode(oneLine.getPrimitiveValue())){
                    cells.get(count, 0).setValue(oneLine.getCode());
                    cell.setStyle(style);
                    cells.setStyle(style);

                }else {
                    cells.get(count, 0).setValue(oneLine.getAttendanceItemName());
                    cell.setStyle(style);
                    cells.setStyle(style);
                }
                cells.get(count, 14).getStyle()
                        .setVerticalAlignment(TextAlignmentType.RIGHT);
                cells.get(count, 14).setValue(oneLine.getTotal());
                cells.get(count, 14).setValue(formatValue(oneLine.getTotal(), null,
                        oneLine.getAttribute(), dataSource.isZeroDisplay()));
                cells.setColumnWidth(0, 5);
                cells.setColumnWidth(1, 5);
                for (int k = 0; k < oneLine.getValueList().size(); k++) {
                    val item = oneLine.getValueList().get(k);
                    val column = yearMonths.indexOf(item.getDate()) + 2;
                        cells.get(count, column).setValue(formatValue(item.getActualValue(), item.getCharacterValue(),
                                oneLine.getAttribute(), dataSource.isZeroDisplay()));
                }
                itemOnePage++;
                count++;
            }
        }
        PageSetup pageSetup = worksheet.getPageSetup();
        pageSetup.setPrintArea(PRINT_AREA + count);

    }

    private void printDate(Worksheet worksheet, int rowCount, List<YearMonth> yearMonths) {
        Cells cells = worksheet.getCells();
        for (int mi = 0; mi < yearMonths.size(); mi++) {
            cells.setColumnWidth(mi + 2, 7);
            val yearMonth = yearMonths.get(mi);
            String yearMonthString = ( mi >0 && yearMonth.month()==1) ? (String.valueOf(yearMonth.year())
            +TextResource.localize("KWR005_307")+
            String.valueOf(yearMonth.month()) + TextResource.localize("KWR005_306"))
                    :(String.valueOf(yearMonth.month()) + TextResource.localize("KWR005_306"));
            cells.merge(rowCount, 0, 1, 2, true, true);
            cells.get(rowCount, 2 + mi).setValue(yearMonthString);
        }
        cells.get(rowCount, 14).setValue(TextResource.localize("KWR005_304"));
    }

    /**
     * Display year month yyyy/MM
     */
    private String toYearMonthString(YearMonth yearMonth) {
        return String.format("%04d/%02d", yearMonth.year(), yearMonth.month());
    }

    /**
     * Format value
     */
    private String formatValue(Double valueDouble, String valueString, CommonAttributesOfForms attributes, Boolean isZeroDisplay) {
        String rs = "";
        switch (attributes) {
            case WORK_TYPE:
                rs = valueString;
                break;
            case WORKING_HOURS:
                rs = valueString;
                break;
            case OTHER_CHARACTER_NUMBER:
                rs = valueString;
                break;
            case OTHER_CHARACTERS:
                rs = valueString;
                break;
            case OTHER_NUMERICAL_VALUE:
                rs = valueString;
                break;

            case TIME_OF_DAY:
                if(valueDouble!=null){
                    rs = convertToTime((int) valueDouble.intValue());
                }

                break;
            case DAYS:
                if(valueDouble!=null){
                    if (valueDouble != 0 || isZeroDisplay) {
                        DecimalFormat formatter2 = new DecimalFormat("#.#");
                        rs = formatter2.format(valueDouble) + TextResource.localize("KWR_1");
                    }
                }
                break;
            case TIME:
                if(valueDouble!=null){
                val minute = (int) valueDouble.intValue();
                if (minute != 0 || isZeroDisplay) {
                    rs = convertToTime(minute);
                }}
                break;
            case AMOUNT_OF_MONEY:
                if(valueDouble!=null){
                if (valueDouble != 0 || isZeroDisplay) {
                    DecimalFormat formatter3 = new DecimalFormat("#,###");
                    rs = formatter3.format((int) valueDouble.intValue()) + TextResource.localize("KWR_3");
                }}
                break;
            case NUMBER_OF_TIMES:
                if(valueDouble!=null){
                    if (valueDouble != 0 || isZeroDisplay) {
                        DecimalFormat formatter1 = new DecimalFormat("#.#");
                        rs = formatter1.format(valueDouble) + TextResource.localize("KWR_2");
                    }
                }

                break;
        }
        return rs;
    }

    /**
     * Convert minute to HH:mm
     */
    private String convertToTime(int minute) {
        val minuteAbs = Math.abs(minute);
        int hours = minuteAbs / 60;
        int minutes = minuteAbs % 60;
        return (minute < 0 ? "-" : "") + String.format("%d:%02d", hours, minutes);
    }

    private boolean checkCode( Integer primitive) {
        val listAtt = Arrays.asList(
                PrimitiveValueOfAttendanceItem.WORK_HOURS_CD,
                PrimitiveValueOfAttendanceItem.POSITION_CD,
                PrimitiveValueOfAttendanceItem.EMP_CTG_CD,
                PrimitiveValueOfAttendanceItem.CLASSIFICATION_CD);
        return primitive != null && listAtt.stream().anyMatch(x -> x.value == primitive);
    }
}
