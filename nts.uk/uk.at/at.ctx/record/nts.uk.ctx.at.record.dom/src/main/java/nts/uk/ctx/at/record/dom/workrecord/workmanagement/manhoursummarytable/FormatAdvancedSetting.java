package nts.uk.ctx.at.record.dom.workrecord.workmanagement.manhoursummarytable;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import org.eclipse.persistence.internal.xr.ValueObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 	フォーマット詳細設定
 */
@Getter
public class FormatAdvancedSetting extends ValueObject {
    /** 表示形式 */
    private DisplayFormat displayFormat;
    /** 合計単位 */
    private TotalUnit totalUnit;
    /** 縦計・横計を表示する */
    private NotUseAtr displayVerticalHorizontalTotal;
    /** 集計項目一覧 */
    private List<SummaryItem> summaryItemList;

    /**
     * [C-1] フォーマット詳細設定
     * @return 工数集計表出力内容
     */
    public FormatAdvancedSetting(DisplayFormat displayFormat, TotalUnit totalUnit, NotUseAtr displayVertHoriTotal,
                                 List<SummaryItem> summaryItemList) {
        this.displayFormat = displayFormat;
        this.totalUnit = totalUnit;
        this.displayVerticalHorizontalTotal = displayVertHoriTotal;
        summaryItemList.sort(Comparator.comparing(SummaryItem::getHierarchicalOrder));
        this.summaryItemList = summaryItemList;
    }

    /**
     * [1] 工数集計表出力内容を作成する
     * @param dateList List<年月日>
     * @param yearMonthList List<年月>
     * @param workDetailList List<作業詳細データ>
     * @param masterNameInfo マスタ名称情報
     * @return 工数集計表出力内容
     */
    public ManHourSummaryTableOutputContent createManHourSummaryTableOutputContent(List<GeneralDate> dateList, List<YearMonth> yearMonthList,
                                                                                   List<WorkDetailData> workDetailList,
                                                                                   MasterNameInformation masterNameInfo) {
        val firstItem = summaryItemList.stream().findFirst().orElse(null);
        val itemDetail = createSummaryItemDetail(dateList, yearMonthList, firstItem, workDetailList, masterNameInfo);
        val outputContent = new ManHourSummaryTableOutputContent(Collections.singletonList(itemDetail));  //TODO: param is: List or Object ???

        if (displayVerticalHorizontalTotal == NotUseAtr.USE)
            outputContent.calculateTotal(totalUnit, dateList, yearMonthList);

        return outputContent;
    }

    /**
     * [prv-1] 集計項目詳細を作成する
     * @param dateList List<年月日>
     * @param yearMonthList List<年月>
     * @param summaryItem 集計項目
     * @param workDetailList List<作業詳細データ>
     * @param masterNameInfo マスタ名称情報
     * @return 集計項目詳細
     */
    private SummaryItemDetail createSummaryItemDetail(List<GeneralDate> dateList, List<YearMonth> yearMonthList,
                                                      SummaryItem summaryItem, List<WorkDetailData> workDetailList,
                                                      MasterNameInformation masterNameInfo) {
        int hierarchyNo = summaryItemList.indexOf(summaryItem);
        boolean lastFlag = hierarchyNo >= summaryItemList.size() - 1;
        //$作業データグループ = 作業詳細リスト：map groupingBy $.集計項目をマッピングする(対象項目)
        Map<String, List<WorkDetailData>> workDataGroup = workDetailList.stream().collect(Collectors.groupingBy(x -> x.mapSummaryItem(summaryItem.getSummaryItemType())));
        String key = workDataGroup.entrySet().stream().map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        SummaryItemDetail summaryItemDetail = null;
        if (lastFlag)
            summaryItemDetail = createItemDetailForBottomLayer(dateList, yearMonthList, key, summaryItem.getSummaryItemType(), workDetailList, masterNameInfo);
        else {
            SummaryItem nextTargetItem = summaryItemList.stream().filter(x -> x.getHierarchicalOrder() == summaryItem.getHierarchicalOrder() + 1).findFirst().orElse(null);
            if (nextTargetItem != null)
                summaryItemDetail = createItemDetailOtherThanBottomLayer(dateList, yearMonthList, key, nextTargetItem, workDetailList, masterNameInfo);
        }

        return summaryItemDetail;
    }

    /**
     * [prv-2] 最下層以外の項目詳細を作成する
     * @param dateList List<年月日>
     * @param yearMonthList List<年月>
     * @param code コード
     * @param summaryItem 集計項目
     * @param workDetailList List<作業詳細データ>
     * @param masterNameInfo マスタ名称情報
     * @return 集計項目詳細
     */
    private SummaryItemDetail createItemDetailOtherThanBottomLayer(List<GeneralDate> dateList, List<YearMonth> yearMonthList,
                                                                   String code, SummaryItem summaryItem, List<WorkDetailData> workDetailList,
                                                                   MasterNameInformation masterNameInfo) {
        val displayInfo = masterNameInfo.getDisplayInfo(code, summaryItem.getSummaryItemType());
        val nextLevelList = createSummaryItemDetail(dateList, yearMonthList, summaryItem, workDetailList, masterNameInfo);
        val summaryItemDetail = SummaryItemDetail.createNew(code, displayInfo, Collections.singletonList(nextLevelList));  //TODO: param is: List or Object ???

        if (displayVerticalHorizontalTotal == NotUseAtr.USE)
            summaryItemDetail.calculateTotal(totalUnit, dateList, yearMonthList);

        return summaryItemDetail;
    }

    /**
     * [prv-3] 最下層の集計項目詳細を作成する
     * @param dateList List<年月日>
     * @param yearMonthList List<年月>
     * @param code コード
     * @param summaryItemType 集計項目種類
     * @param workDetailList List<作業詳細データ>
     * @param masterNameInfo マスタ名称情報
     * @return 集計項目詳細
     */
    private SummaryItemDetail createItemDetailForBottomLayer(List<GeneralDate> dateList, List<YearMonth> yearMonthList,
                                                                   String code, SummaryItemType summaryItemType, List<WorkDetailData> workDetailList,
                                                                   MasterNameInformation masterNameInfo) {
        val displayInfo = masterNameInfo.getDisplayInfo(code, summaryItemType);
        val summaryItemDetail = SummaryItemDetail.createNewBottomLayer(code, displayInfo, totalUnit ,dateList, yearMonthList, workDetailList);

        if (displayVerticalHorizontalTotal == NotUseAtr.USE)
            summaryItemDetail.calculateHorizontalTotal();

        return summaryItemDetail;
    }

    /**
     * [prv-4] 年月日別に作業時間明細を作成する
     * @param dateList List<年月日>
     * @param workDetailList List<作業詳細データ>
     * @return List<日々縦計値>

     */
    public List<VerticalValueDaily> createWorkTimeDetailByDate(List<GeneralDate> dateList, List<WorkDetailData> workDetailList) {
        List<VerticalValueDaily> verticalTotalList = new ArrayList<>();
        for (val date : dateList) {
            val workingTime = workDetailList.stream().filter(x -> x.getDate().equals(date)).mapToInt(WorkDetailData::getTotalWorkingHours).sum();
            verticalTotalList.add(new VerticalValueDaily(workingTime, null, date)); //TODO
        }
        return verticalTotalList;
    }

    /**
     * [prv-5] 年月別に作業時間明細を作成する
     * @param yearMonthList List<年月>
     * @param workDetailList List<作業詳細データ>
     * @return List<日々縦計値>
     */
    public List<VerticalValueDaily> createWorkTimeDetailByYearMonth(List<YearMonth> yearMonthList, List<WorkDetailData> workDetailList) {
        List<VerticalValueDaily> verticalValueList = new ArrayList<>();
        for (val ym : yearMonthList) {
            val workingTime = workDetailList.stream().filter(x -> x.getDate().yearMonth() == ym).mapToInt(WorkDetailData::getTotalWorkingHours).sum();
            verticalValueList.add(new VerticalValueDaily(workingTime, ym, null)); //TODO
        }
        return verticalValueList;
    }
}
