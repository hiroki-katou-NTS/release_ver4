package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.monthly.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.WorkTypeDaysCountTable;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 月別実績の欠勤日数
 * @author shuichu_ishida
 */
@Getter
public class AbsenceDaysOfMonthly {

	/** 欠勤合計日数 */
	private AttendanceDaysMonth totalAbsenceDays;
	/** 欠勤日数 */
	private Map<Integer, AggregateAbsenceDays> absenceDaysList;
	
	/**
	 * コンストラクタ
	 */
	public AbsenceDaysOfMonthly(){
		
		this.totalAbsenceDays = new AttendanceDaysMonth(0.0);
		this.absenceDaysList = new HashMap<>();
	}
	
	/**
	 * ファクトリー
	 * @param totalAbsenceDays 欠勤合計日数
	 * @param absenceDaysList 欠勤日数
	 * @return 月別実績の欠勤日数
	 */
	public static AbsenceDaysOfMonthly of(
			AttendanceDaysMonth totalAbsenceDays,
			List<AggregateAbsenceDays> absenceDaysList){
		
		val domain = new AbsenceDaysOfMonthly();
		domain.totalAbsenceDays = totalAbsenceDays;
		for (val absenceDays : absenceDaysList){
			val absenceFrameNo = Integer.valueOf(absenceDays.getAbsenceFrameNo());
			domain.absenceDaysList.putIfAbsent(absenceFrameNo, AggregateAbsenceDays.of(
					absenceFrameNo, new AttendanceDaysMonth(absenceDays.getDays().v())));
		}
		return domain;
	}
	
	/**
	 * 集計
	 * @param workingSystem 労働制
	 * @param workType 勤務種類
	 * @param workTypeDaysCountTable 勤務種類の日数カウント表
	 * @param isAttendanceDay 出勤しているかどうか
	 */
	public void aggregate(WorkingSystem workingSystem, WorkType workType,
			WorkTypeDaysCountTable workTypeDaysCountTable, boolean isAttendanceDay){

		if (workType == null) return;
		if (workTypeDaysCountTable == null) return;
		
		for (val aggrAbsenceDays : workTypeDaysCountTable.getAbsenceDaysMap().values()){
			
			// 欠勤枠日数の集計
			if (aggrAbsenceDays.getDays().greaterThan(0.0)){
				boolean isAddAbsenceDays = false;

				if (workingSystem == WorkingSystem.EXCLUDED_WORKING_CALCULATE) {

					// 計算対象外の時、無条件で加算
					isAddAbsenceDays = true;
				}
				else {
					
					// その他の時、半日出勤系の勤務があれば、出勤状態を確認して加算　（なければ、無条件加算）
					// ※　勤務種類設定（workTypeSet）が取得出来る　＝　半日出勤系勤務がある
					val workTypeSet = workType.getWorkTypeSet();
					if (workTypeSet != null){
						if (isAttendanceDay) isAddAbsenceDays = true;
					}
					else {
						isAddAbsenceDays = true;
					}
				}
				
				if (isAddAbsenceDays){
					
					// 欠勤日数に加算
					val absenceFrameNo = Integer.valueOf(aggrAbsenceDays.getAbsenceFrameNo());
					this.absenceDaysList.putIfAbsent(absenceFrameNo, new AggregateAbsenceDays(absenceFrameNo));
					val targetAbsenceDays = this.absenceDaysList.get(absenceFrameNo);
					targetAbsenceDays.addDays(aggrAbsenceDays.getDays().v());
				}
			}
			
			// 欠勤合計日数の集計
			this.totalAbsenceDays = this.totalAbsenceDays.addDays(aggrAbsenceDays.getDays().v());
		}
	}
}
