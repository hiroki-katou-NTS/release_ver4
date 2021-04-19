package nts.uk.ctx.at.function.dom.alarm.alarmlist.persistenceextractresult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionCode;
import nts.uk.ctx.at.shared.dom.alarmList.AlarmCategory;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckType;

/**
 * アラーム抽出条件
 */
@Getter
@AllArgsConstructor
public class AlarmExtractionCondition {

    /** アラームチェック条件NO */
    private String alarmCheckConditionNo;

    /**  アラームチェック条件コード */
    private AlarmCheckConditionCode alarmCheckConditionCode;

    /** アラームリストのカテゴリ */
    private AlarmCategory alarmCategory;

    /** チェック種類 */
    private AlarmListCheckType alarmListCheckType;

}
