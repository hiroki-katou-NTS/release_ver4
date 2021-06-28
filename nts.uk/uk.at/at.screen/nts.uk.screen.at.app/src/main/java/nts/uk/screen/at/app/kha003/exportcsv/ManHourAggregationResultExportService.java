package nts.uk.screen.at.app.kha003.exportcsv;

import lombok.val;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.workrecord.workmanagement.manhoursummarytable.*;
import nts.uk.screen.at.app.kdl053.RegistrationErrorListDto;
import nts.uk.screen.at.app.kha003.b.ManHourPeriod;
import nts.uk.screen.at.app.kha003.d.ManHourAggregationResultDto;
import nts.uk.screen.at.app.kha003.d.AggregationResultQuery;
import nts.uk.screen.at.app.kha003.d.CreateAggregationManHourResult;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.infra.file.csv.CSVFileData;
import nts.uk.shr.infra.file.csv.CSVReportGenerator;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
//public class ManHourAggregationResultExportService extends ExportService<AggregationResultQuery> {
public class ManHourAggregationResultExportService extends ExportService<List<RegistrationErrorListDto>> {
    @Inject
    private CSVReportGenerator generator;

    @Inject
    private CreateAggregationManHourResult createAggregationManHourResult;

    private static final String CODE_HEADER = "KHA003_103";

    private static final String TOTAL_HEADER = "KHA003_98";

    private static final String PGID = "KHA003";

    private static final String FILE_EXTENSION = ".csv";

    private static final String DATE_FORMAT = "yyyy/MM/dd";

    @Override
//    protected void handle(ExportServiceContext<AggregationResultQuery> exportServiceContext) {
    protected void handle(ExportServiceContext<List<RegistrationErrorListDto>> exportServiceContext) {
//        val query = exportServiceContext.getQuery();
//        if (query == null) return;
        AggregationResultQuery query = new AggregationResultQuery(
                "01", null, Collections.emptyList(),
                new ManHourPeriod(
                        0,
                        new DatePeriod(GeneralDate.fromString("2021/06/01", "yyyy/MM/dd"), GeneralDate.fromString("2021/06/03", "yyyy/MM/dd")),
                        new YearMonthPeriod(YearMonth.of(2021, 6), YearMonth.of(2021, 6))
                )
        );

        String executionTime = GeneralDateTime.now().toString().replaceAll("[/:\\s]", "");

        // Get data result
//        ManHourAggregationResultDto data = this.createAggregationManHourResult.get(query.getCode(), query.getMasterNameInfo(), query.getWorkDetailList(),
//                query.getDateList(), query.getYearMonthList());
        ManHourAggregationResultDto data = new ManHourAggregationResultDto(
                Dummy.SummaryTableFormat.create(),
                Dummy.SummaryTableOutputContent.create());
        val detailFormatSetting = data.getSummaryTableFormat().getDetailFormatSetting();
        val displayFormat = detailFormatSetting.getDisplayFormat();
        val outputContent = data.getOutputContent();
        val totalUnit = detailFormatSetting.getTotalUnit();
        int maxRangeDate = totalUnit == TotalUnit.DATE ? query.getPeriod().getDateList().size() : query.getPeriod().getYearMonthList().size();

        // Flag display total
        val isDisplayTotal = detailFormatSetting.getDisplayVerticalHorizontalTotal().value == 1;

        // create Header list
        List<String> headerList = this.createTextHeader(query, detailFormatSetting, isDisplayTotal);

        // Add data source
        List<Map<String, Object>> dataSource = new ArrayList<>();
        // Handle data on needed data to export
        this.dataOutputProcessing(outputContent, isDisplayTotal, totalUnit, dataSource, headerList, maxRangeDate, displayFormat);

        // Execute export
        CSVFileData fileData = new CSVFileData(
                PGID + "_" + executionTime + "_" + AppContexts.user().companyCode() + FILE_EXTENSION, headerList, dataSource);
        generator.generate(exportServiceContext.getGeneratorContext(), fileData);
    }

    private void dataOutputProcessing(ManHourSummaryTableOutputContent outputContent, boolean isDispTotal, TotalUnit unit, List<Map<String, Object>> dataSource,
                                      List<String> headerList, int maxRangeDate, DisplayFormat dispFormat) {
        for (SummaryItemDetail level1 : outputContent.getItemDetails()) {
            if (level1.getChildHierarchyList().isEmpty()) {
                Map<String, Object> row1 = new HashMap<>();
                row1.put(headerList.get(0), level1.getDisplayInfo().getCode());
                row1.put(headerList.get(1), level1.getDisplayInfo().getName());

                val workingTimeMap1 = this.getWorkingTimeByDate(unit, level1.getVerticalTotalList());
                for (int i = 2; i < maxRangeDate + 2; i++) {
                    row1.put(headerList.get(i), workingTimeMap1.get(headerList.get(i)));
                }
                // Tong chieu ngang
                if (isDispTotal) {
                    row1.put(headerList.get(headerList.size() - 1), level1.getTotalPeriod().get());
                }
                dataSource.add(row1);
            } else {
                for (SummaryItemDetail level2 : level1.getChildHierarchyList()) {
                    if (level2.getChildHierarchyList().isEmpty()) {
                        Map<String, Object> row2 = new HashMap<>();
                        row2.put(headerList.get(0), level1.getDisplayInfo().getCode());
                        row2.put(headerList.get(1), level1.getDisplayInfo().getName());
                        row2.put(headerList.get(2), level2.getDisplayInfo().getCode());
                        row2.put(headerList.get(3), level2.getDisplayInfo().getName());

                        for (int i = 4; i < maxRangeDate + 4; i++) {
                            val workingTimeMap2 = this.getWorkingTimeByDate(unit, level2.getVerticalTotalList());
                            row2.put(headerList.get(i), workingTimeMap2.get(headerList.get(i)));
                        }
                        // Tong chieu ngang
                        if (isDispTotal) {
                            row2.put(headerList.get(headerList.size() - 1), level2.getTotalPeriod().get());
                        }
                        dataSource.add(row2);
                    } else {
                        for (SummaryItemDetail level3 : level2.getChildHierarchyList()) {
                            if (level3.getChildHierarchyList().isEmpty()) {
                                Map<String, Object> row3 = new HashMap<>();
                                row3.put(headerList.get(0), level1.getDisplayInfo().getCode());
                                row3.put(headerList.get(1), level1.getDisplayInfo().getName());
                                row3.put(headerList.get(2), level2.getDisplayInfo().getCode());
                                row3.put(headerList.get(3), level2.getDisplayInfo().getName());
                                row3.put(headerList.get(4), level3.getDisplayInfo().getCode());
                                row3.put(headerList.get(5), level3.getDisplayInfo().getName());

                                for (int i = 6; i < maxRangeDate + 6; i++) {
                                    val workingTimeMap3 = this.getWorkingTimeByDate(unit, level3.getVerticalTotalList());
                                    row3.put(headerList.get(i), workingTimeMap3.get(headerList.get(i)));
                                }
                                // Tong chieu ngang
                                if (isDispTotal) {
                                    row3.put(headerList.get(headerList.size() - 1), level3.getTotalPeriod().get());
                                }
                                dataSource.add(row3);
                            } else {
                                for (SummaryItemDetail level4 : level3.getChildHierarchyList()) {
                                    Map<String, Object> row4 = new HashMap<>();
                                    row4.put(headerList.get(0), level1.getDisplayInfo().getCode());
                                    row4.put(headerList.get(1), level1.getDisplayInfo().getName());
                                    row4.put(headerList.get(2), level2.getDisplayInfo().getCode());
                                    row4.put(headerList.get(3), level2.getDisplayInfo().getName());
                                    row4.put(headerList.get(4), level3.getDisplayInfo().getCode());
                                    row4.put(headerList.get(5), level3.getDisplayInfo().getName());
                                    row4.put(headerList.get(6), level4.getDisplayInfo().getCode());
                                    row4.put(headerList.get(7), level4.getDisplayInfo().getName());

                                    for (int i = 8; i < maxRangeDate + 8; i++) {
                                        val workingTimeMap4 = this.getWorkingTimeByDate(unit, level4.getVerticalTotalList());
//                                        row4.put(headerList.get(i), workingTimeMap4.get(headerList.get(i))); // chưa test
                                        row4.put(headerList.get(i), formatValue(Double.valueOf(workingTimeMap4.get(headerList.get(i))), dispFormat));
                                    }
                                    // Tong chieu ngang
                                    if (isDispTotal) {
                                        row4.put(headerList.get(headerList.size() - 1), level4.getTotalPeriod().get());
                                    }
                                    dataSource.add(row4);
                                }
                                if (isDispTotal) {
                                    // Tong level 4 theo chieu doc
                                    Map<String, Object> rowTotalLv4 = new HashMap<>();
                                    rowTotalLv4.put(headerList.get(5), headerList.get(5) + TextResource.localize("KHA003_100"));
                                    for (int i = 8; i < headerList.size(); i++) {
                                        val mapTotal4 = this.getWorkingTimeByDate(unit, level3.getVerticalTotalList());
                                        rowTotalLv4.put(headerList.get(i), mapTotal4.get(headerList.get(i)));
                                    }
                                    rowTotalLv4.put(headerList.get(headerList.size() - 1), level3.getTotalPeriod().get());
                                    dataSource.add(rowTotalLv4);
                                }
                            }
                        }
                        if (isDispTotal) {
                            // Tong level 3 theo chieu doc
                            Map<String, Object> rowTotalLv3 = new HashMap<>();
                            rowTotalLv3.put(headerList.get(3), headerList.get(3) + TextResource.localize("KHA003_100"));
                            for (int i = 6; i < headerList.size(); i++) {
                                val mapTotal2 = this.getWorkingTimeByDate(unit, level2.getVerticalTotalList());
                                rowTotalLv3.put(headerList.get(i), mapTotal2.get(headerList.get(i)));
                            }
                            rowTotalLv3.put(headerList.get(headerList.size() - 1), level2.getTotalPeriod().get());
                            dataSource.add(rowTotalLv3);
                        }
                    }
                }
                if (isDispTotal) {
                    // Tong level 2 theo chieu doc
                    Map<String, Object> rowTotalLv2 = new HashMap<>();
                    rowTotalLv2.put(headerList.get(1), headerList.get(1) + TextResource.localize("KHA003_100"));
                    for (int i = 4; i < headerList.size(); i++) {
                        val mapTotal2 = this.getWorkingTimeByDate(unit, level1.getVerticalTotalList());
                        rowTotalLv2.put(headerList.get(i), mapTotal2.get(headerList.get(i)));
                    }
                    rowTotalLv2.put(headerList.get(headerList.size() - 1), level1.getTotalPeriod().get());
                    dataSource.add(rowTotalLv2);
                }
            }
        }
        if (isDispTotal) {
            // Tong tat ca cac level theo chieu doc
            Map<String, Object> rowTotal = new HashMap<>();
            rowTotal.put(headerList.get(1), "総合計");
            for (int i = 2; i < headerList.size(); i++) {
                val mapTotal = this.getWorkingTimeByDate(unit, outputContent.getVerticalTotalValues());
                rowTotal.put(headerList.get(i), mapTotal.get(headerList.get(i)));
            }
            rowTotal.put(headerList.get(headerList.size() - 1), outputContent.getTotalPeriod().get());
            dataSource.add(rowTotal);
        }
    }

    private Map<String, Integer> getWorkingTimeByDate(TotalUnit unit, List<VerticalValueDaily> lstValueDaily) {
        Map<String, Integer> map = new HashMap<>();
        if (unit == TotalUnit.DATE) {
            lstValueDaily.sort(Comparator.comparing(VerticalValueDaily::getDate));
            lstValueDaily.forEach(d -> map.put(d.getDate().toString(), d.getWorkingHours()));
        } else {
            lstValueDaily.sort(Comparator.comparing(VerticalValueDaily::getYearMonth));
            lstValueDaily.forEach(d -> map.put(d.getDate().toString(), d.getWorkingHours()));
        }
        return map;
    }

    private void getHierarchy(ManHourSummaryTableOutputContent outputContent, List<SummaryItemDetail> target, int levelTotal) {
//        List<SummaryItemDetail> lstParent
//        if (outputContent == null || CollectionUtil.isEmpty(outputContent.getItemDetails())) return ;
//
//        lstResult.addAll(itemDetails);
//        List<SummaryItemDetail> childHierarchy = itemDetails.stream().flatMap(x -> x.getChildHierarchyList().stream()).collect(Collectors.toList());
//        // Recursive
//        convertTreeToFlatList(childHierarchy, lstResult);
    }

    /**
     * create text Header
     *
     * @param query
     * @param detailSetting
     * @param isDispTotal
     * @return
     */
    private List<String> createTextHeader(AggregationResultQuery query, DetailFormatSetting detailSetting, boolean isDispTotal) {
        List<String> lstHeader = new ArrayList<>();
        // Sort before adding
        val sortedList = detailSetting.getSummaryItemList().stream().sorted(Comparator.comparing(SummaryItem::getHierarchicalOrder)).collect(Collectors.toList());
        // Add code & name to header
        for (int i = 0; i < sortedList.size(); i++) {
            SummaryItem item = sortedList.get(i);
            lstHeader.add(TextResource.localize(CODE_HEADER) + i);
            lstHeader.add(item.getSummaryItemType().nameId);
        }

        // Add date/yearMonth list to header
        if (detailSetting.getTotalUnit() == TotalUnit.DATE) {
            query.getPeriod().getDateList().forEach(date -> lstHeader.add(date.toString()));
        } else {
            query.getPeriod().getYearMonthList().forEach(ym -> lstHeader.add(yearMonthToString(ym)));
        }

        // Add horizontal total to header
        if (isDispTotal) {
            lstHeader.add(TextResource.localize(TOTAL_HEADER));
        }
        return lstHeader;
    }

    /**
     * Format value by display format
     *
     * @param value
     * @param displayFormat
     * @return String
     */
    private String formatValue(Double value, DisplayFormat displayFormat) {
        String targetValue = null;
        switch (displayFormat) {
            case DECIMAL:
                if (value != null && value != 0) {
                    DecimalFormat formatter = new DecimalFormat("#.#");
                    targetValue = formatter.format(value);
                }
                break;
            case HEXA_DECIMAL:
                BigDecimal decimalValue = new BigDecimal(value);
                BigDecimal intValue = decimalValue.divideToIntegralValue(BigDecimal.valueOf(60.00));
                BigDecimal remainValue = decimalValue.subtract(intValue.multiply(BigDecimal.valueOf(60.00)));
                decimalValue = intValue.add(remainValue.divide(BigDecimal.valueOf(100.00), 2, RoundingMode.HALF_UP));
                targetValue = decimalValue.toString();
                break;
            case MINUTE:
                if (value != null) {
                    val intItemValue = value.intValue();
                    if (intItemValue != 0) {
                        targetValue = toMinute(intItemValue);
                    }
                }
                break;
        }

        return targetValue;
    }

    /**
     * convert YearMonth to String with format: yyyy/MM
     *
     * @param yearMonth
     * @return
     */
    private String yearMonthToString(YearMonth yearMonth) {
        return String.format("%04d/%02d", yearMonth.year(), yearMonth.month());
    }

    /**
     * Convert to minute (HH:mm)
     *
     * @param value
     * @return
     */
    private String toMinute(int value) {
        val minuteAbs = Math.abs(value);
        int hours = minuteAbs / 60;
        int minutes = minuteAbs % 60;
        return (value < 0 ? "-" : "") + String.format("%d:%02d", hours, minutes);
    }

}
