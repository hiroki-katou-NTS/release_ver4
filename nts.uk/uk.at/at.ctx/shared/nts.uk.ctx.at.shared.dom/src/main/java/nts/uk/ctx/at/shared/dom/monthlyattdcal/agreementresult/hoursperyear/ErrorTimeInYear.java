package nts.uk.ctx.at.shared.dom.monthlyattdcal.agreementresult.hoursperyear;

import lombok.Getter;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.uk.ctx.at.shared.dom.monthlyattdcal.agreementresult.AgreementOneYearTime;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.persistence.internal.xr.ValueObject;

/**
 * @author quang.nh1
 * 1年間のエラーアラーム時間
 */
@Getter
public class ErrorTimeInYear extends ValueObject {

    /**
     * エラー時間
     */
    private AgreementOneYearTime errorTime;

    /**
     * アラーム時間
     */
    private AgreementOneYearTime alarmTime;

    /**
     * [C-1] 1ヶ月のエラーアラーム時間
     */
    public ErrorTimeInYear(AgreementOneYearTime errorTime, AgreementOneYearTime alarmTime) {
        if (errorTime.v() <= alarmTime.v()) {
            throw new BusinessException("Msg_59", "KMK008_67", "KMK008_66");
        }
        this.errorTime = errorTime;
        this.alarmTime = alarmTime;
    }

    /**
     * [1] エラー時間を超えているか
     */
    public Pair<Boolean, AgreementOneYearTime> checkErrorTimeExceeded(AgreementOneYearTime applicationTime) {
        Pair<Boolean, AgreementOneYearTime> reusult = new ImmutablePair<>(errorTime.v() < applicationTime.v(), errorTime);
        return reusult;
    }

    /**
     * [2] アラーム時間を計算する
     */
    public AgreementOneYearTime calculateAlarmTime(AgreementOneYearTime applicationTime) {
        val calculateAlarmTime =  applicationTime.v() - (errorTime.v() - alarmTime.v());

        return calculateAlarmTime > 0 ? new AgreementOneYearTime(calculateAlarmTime)  : new AgreementOneYearTime(0);
    }
}
