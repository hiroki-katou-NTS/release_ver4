package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.WorkTypeDaysCountTable;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;

/**
 * 月別実績の勤務日数　（詳細）
 * @author shuichu_ishida
 */
@Getter
public class WorkDaysDetailOfMonthly {

	/** 日数 */
	private AttendanceDaysMonth days;
	
	/**
	 * コンストラクタ
	 */
	public WorkDaysDetailOfMonthly(){
		
		this.days = new AttendanceDaysMonth(0.0);
	}
	
	/**
	 * ファクトリー
	 * @param days 日数
	 * @return 月別実績の勤務日数
	 */
	public static WorkDaysDetailOfMonthly of(AttendanceDaysMonth days){
		
		val domain = new WorkDaysDetailOfMonthly();
		domain.days = days;
		return domain;
	}
	
	/**
	 * 集計
	 * @param workingSystem 労働制
	 * @param workTypeDaysCountTable 勤務種類の日数カウント表
	 * @param isAttendanceDay 出勤しているかどうか
	 */
	public void aggregate(
			WorkingSystem workingSystem,
			WorkTypeDaysCountTable workTypeDaysCountTable,
			boolean isAttendanceDay){
		
		if (workTypeDaysCountTable == null) return;
			
		// 労働制を取得
		if (workingSystem == WorkingSystem.EXCLUDED_WORKING_CALCULATE){

			// 計算対象外の時、無条件で、勤務日数に加算する
			this.days = this.days.addDays(workTypeDaysCountTable.getWorkDays().v());
		}
		else {
			
			// その他労働制の時、出勤している日なら、勤務日数に加算する
			if (isAttendanceDay){
				this.days = this.days.addDays(workTypeDaysCountTable.getWorkDays().v());
			}
		}
	}
}
