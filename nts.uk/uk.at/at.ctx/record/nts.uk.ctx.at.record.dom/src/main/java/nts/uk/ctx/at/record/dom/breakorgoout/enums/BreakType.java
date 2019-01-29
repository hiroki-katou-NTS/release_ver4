package nts.uk.ctx.at.record.dom.breakorgoout.enums;

import lombok.AllArgsConstructor;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.TimeSheetAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.FixedRestCalculateMethod;
/**
 * 
 * @author nampt
 * 休憩種類
 *
 */
@AllArgsConstructor
public enum BreakType {
	
	/* 就業時間帯から参照 */
	REFER_WORK_TIME(0),
	/* スケジュールから参照 */
	REFER_SCHEDULE(1);
	
	public final int value;
	
	/**
	 * 就業時間帯から参照であるか判定する
	 * @return　就業時間帯から参照である
	 */
	public boolean isReferWorkTime() {
		return REFER_WORK_TIME.equals(this);
	}
	
	/**
	 * スケジュールから参照であるか判定する
	 * @return スケジュールから参照である
	 */
	public boolean isReferSchedule() {
		return REFER_SCHEDULE.equals(this);
	}
	
	public boolean isUse(TimeSheetAtr tAtr) {
		switch(tAtr) {
		case RECORD:
			return this.isReferWorkTime();
		case SCHEDULE:
			return this.isReferSchedule();
			default:
				throw new RuntimeException("unknown TimeSheet Atr");
		}
	}
	
	public static BreakType convertFromFixedRestCalculateMethod(FixedRestCalculateMethod calcMethod) {
		if(calcMethod.isReferToMaster()) {
			return REFER_WORK_TIME;
		}
		else {
			return REFER_SCHEDULE;
		}
	}
}
