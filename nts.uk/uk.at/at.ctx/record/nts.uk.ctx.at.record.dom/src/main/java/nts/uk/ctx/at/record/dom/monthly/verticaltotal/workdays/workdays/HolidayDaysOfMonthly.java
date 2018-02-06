package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.WorkTypeDaysCountTable;

/**
 * 月別実績の休日日数
 * @author shuichu_ishida
 */
@Getter
public class HolidayDaysOfMonthly {

	/** 日数 */
	private AttendanceDaysMonth days;
	
	/**
	 * コンストラクタ
	 */
	public HolidayDaysOfMonthly(){
		
		this.days = new AttendanceDaysMonth(0.0);
	}
	
	/**
	 * ファクトリー
	 * @param days 日数
	 * @return 月別実績の休日日数
	 */
	public static HolidayDaysOfMonthly of(AttendanceDaysMonth days){
		
		val domain = new HolidayDaysOfMonthly();
		domain.days = days;
		return domain;
	}
	
	/**
	 * 集計
	 * @param workTypeDaysCountTable 勤務種類の日数カウント表
	 */
	public void aggregate(WorkTypeDaysCountTable workTypeDaysCountTable){

		if (workTypeDaysCountTable == null) return;
		
		// 休日日数に加算する
		this.days = this.days.addDays(workTypeDaysCountTable.getHolidayDays().v());
	}
}
