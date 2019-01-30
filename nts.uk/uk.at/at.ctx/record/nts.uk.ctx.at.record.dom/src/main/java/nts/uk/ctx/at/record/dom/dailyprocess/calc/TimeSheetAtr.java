package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;

/**
 * 実働or予定時間帯作成から呼び出されたか
 * @author keisuke_hoshina
 *
 */
public enum TimeSheetAtr {
	RECORD,  //実績
	SCHEDULE;//スケジュール
	
	/**
	 * 予定時間帯から呼び出されたか判定する
	 * @return　予定時間帯である
	 */
	public boolean isSchedule() {
		return SCHEDULE.equals(this);
	}
	
	/**
	 * 実績時間帯から呼び出されたか判定する
	 * @return　実績時間帯である
	 */
	public boolean isRecord() {
		return RECORD.equals(this);
	}
	
	public BreakType decisionBreakTime() {
		switch(this) {
			case RECORD:
				return BreakType.REFER_WORK_TIME;
			case SCHEDULE:
				return BreakType.REFER_SCHEDULE;
			default:
				throw new RuntimeException("unknown BreakType");
		}
	}
}
