package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.onemonth;

import org.apache.commons.lang3.tuple.Pair;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.ExcessState;

/** １ヶ月時間 */
@Getter
public class OneMonthTime {
	
	/** エラーアラーム時間 */
	@Setter
	private OneMonthErrorAlarmTime erAlTime;
	/** 上限時間 */
	private AgreementOneMonthTime upperLimit;

	public OneMonthTime() {
		this.erAlTime = new OneMonthErrorAlarmTime();
		this.upperLimit = new AgreementOneMonthTime(0);
	}
	
	public OneMonthTime(OneMonthErrorAlarmTime erAlTime, AgreementOneMonthTime upperLimit) {
		super();
		/** 不変条件: @上限時間 ≧ @エラーアラーム時間.エラー時間 */
		if (upperLimit.lessThan(erAlTime.getError())) {
			throw new BusinessException("Msg_59", "KMK008_66", "KMK008_129");
		}
		this.erAlTime = erAlTime;
		this.upperLimit = upperLimit;
	}
	
	/** エラーチェック */
	public ExcessState check(AttendanceTimeMonth target) {
		
		if (target.v() > upperLimit.valueAsMinutes()) {
			return ExcessState.UPPER_LIMIT_OVER;
		}
		
		if (target.v() > erAlTime.getError().valueAsMinutes()) {
			return ExcessState.ERROR_OVER;
		}
		
		if (target.v() > erAlTime.getAlarm().valueAsMinutes()) {
			return ExcessState.ALARM_OVER;
		}
		
		return ExcessState.NORMAL;
	}
	
	/** エラー時間を超えているか */
	public Pair<Boolean, AgreementOneMonthTime> isErrorTimeOver(AgreementOneMonthTime applyTime) {
		
		/** ＠エラーアラーム時間.エラー時間を超えているか(申請時間) */
		return erAlTime.isErrorTimeOver(applyTime);
	}
	
	/** アラーム時間を計算する */
	public AgreementOneMonthTime calcAlarmTime(AgreementOneMonthTime applyTime) {
		
		/** ＠エラーアラーム時間.アラーム時間を計算する(申請時間) */
		return erAlTime.calcAlarmTime(applyTime);
	}

}
