package nts.uk.ctx.at.record.dom.workrecord.erroralarm.alarmlistworkplace.daily;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.alarmList.primitivevalue.DisplayMessage;

/**
 * AggregateRoot: アラームリスト（職場）日別の固定抽出条件
 *
 * @author Thanh.LNP
 */
@Getter
@AllArgsConstructor
public class FixedExtractionDayCon extends AggregateRoot {
    /**
     * 職場のエラーアラームチェックID
     */
    private String errorAlarmWorkplaceId;

    /**
     * No
     */
    private FixedCheckDayItems fixedCheckDayItems;

    /**
     * 表示するメッセージ
     */
    private DisplayMessage messageDisp;

    /**使用区分*/
    private boolean useAtr;

    /**
     * 作成する
     *
     * @param errorAlarmWorkplaceId 職場のエラーアラームチェックID
     * @param fixedCheckDayItems    No
     * @param messageDisp           表示するメッセージ
     */
    public static FixedExtractionDayCon create(String errorAlarmWorkplaceId,
                                               int fixedCheckDayItems,
                                               String messageDisp,Boolean useAtr) {

        return new FixedExtractionDayCon(errorAlarmWorkplaceId,
                EnumAdaptor.valueOf(fixedCheckDayItems, FixedCheckDayItems.class),
                new DisplayMessage(messageDisp),useAtr);
    }
}
