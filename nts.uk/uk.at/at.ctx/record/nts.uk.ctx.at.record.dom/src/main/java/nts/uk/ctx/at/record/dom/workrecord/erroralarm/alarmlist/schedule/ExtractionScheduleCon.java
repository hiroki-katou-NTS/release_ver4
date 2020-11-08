package nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlist.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.CompareRange;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.CompareSingleValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.monthlycheckcondition.NameAlarmExtractionCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.DisplayMessage;

/**
 * AggregateRoot: アラームリスト（職場別）スケジュール／日次の抽出条件
 *
 * @author Thanh.LNP
 */
@Getter
@AllArgsConstructor
public class ExtractionScheduleCon extends AggregateRoot {
    /**
     * 職場のエラーアラームチェックID
     */
    private String errorAlarmWorkplaceId;

    /**
     * No
     */
    private int orderNumber;

    /**
     * チェック項目
     */
    private CheckDayItemsType checkDayItemsType;

    /**
     * 使用区分
     */
    private boolean useAtr;

    /**
     * 対比チェック項目
     */
    private ComparisonCheckItems comparisonCheckItems;

    /**
     * 日次抽出条件名称
     */
    private NameAlarmExtractionCondition daiExtracConName;

    /**
     * 表示するメッセージ
     */
    private DisplayMessage messageDisp;

    /**
     * 範囲との比較
     */
    private CompareRange compareRange;

    /**
     * 単一値との比較
     */
    private CompareSingleValue compareSingleValue;

    /**
     * 作成する
     *
     * @param errorAlarmWorkplaceId 職場のエラーアラームチェックID
     * @param orderNumber           No
     * @param checkDayItemsType     チェック項目
     * @param useAtr                使用区分
     * @param comparisonCheckItems  対比チェック項目
     * @param daiExtracConName      日次抽出条件名称
     * @param messageDisp           表示するメッセージ
     * @param compareRange          範囲との比較
     * @param compareSingleValue    単一値との比較
     */
    public static ExtractionScheduleCon create(String errorAlarmWorkplaceId,
                                               int orderNumber,
                                               CheckDayItemsType checkDayItemsType,
                                               boolean useAtr,
                                               ComparisonCheckItems comparisonCheckItems,
                                               NameAlarmExtractionCondition daiExtracConName,
                                               DisplayMessage messageDisp,
                                               CompareRange compareRange,
                                               CompareSingleValue compareSingleValue) {

        return new ExtractionScheduleCon(errorAlarmWorkplaceId, orderNumber,
                checkDayItemsType, useAtr,
                comparisonCheckItems, daiExtracConName,
                messageDisp, compareRange,
                compareSingleValue);
    }
}
