package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workdays.workdays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.record.dom.monthly.WorkTypeDaysCountTable;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingSystem;
import nts.uk.ctx.at.shared.dom.worktime.predset.BreakDownTimeDay;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;

/**
 * 月別実績の欠勤日数
 * @author shuichi_ishida
 */
@Getter
public class AbsenceDaysOfMonthly {

	/** 欠勤合計日数 */
	private AttendanceDaysMonth totalAbsenceDays;
	/** 欠勤合計時間 */
	private AttendanceTimeMonth totalAbsenceTime;
	/** 欠勤日数 */
	private Map<Integer, AggregateAbsenceDays> absenceDaysList;
	
	/**
	 * コンストラクタ
	 */
	public AbsenceDaysOfMonthly(){
		
		this.totalAbsenceDays = new AttendanceDaysMonth(0.0);
		this.totalAbsenceTime = new AttendanceTimeMonth(0);
		this.absenceDaysList = new HashMap<>();
	}
	
	/**
	 * ファクトリー
	 * @param totalAbsenceDays 欠勤合計日数
	 * @param totalAbsenceTime 欠勤合計時間
	 * @param absenceDaysList 欠勤日数
	 * @return 月別実績の欠勤日数
	 */
	public static AbsenceDaysOfMonthly of(
			AttendanceDaysMonth totalAbsenceDays,
			AttendanceTimeMonth totalAbsenceTime,
			List<AggregateAbsenceDays> absenceDaysList){
		
		val domain = new AbsenceDaysOfMonthly();
		domain.totalAbsenceDays = totalAbsenceDays;
		domain.totalAbsenceTime = totalAbsenceTime;
		for (val absenceDays : absenceDaysList){
			val absenceFrameNo = Integer.valueOf(absenceDays.getAbsenceFrameNo());
			domain.absenceDaysList.putIfAbsent(absenceFrameNo, absenceDays);
		}
		return domain;
	}
	
	/**
	 * 集計
	 * @param workingSystem 労働制
	 * @param workType 勤務種類
	 * @param workTypeDaysCountTable 勤務種類の日数カウント表
	 * @param isAttendanceDay 出勤しているかどうか
	 * @param predetermineTimeSet 所定時間設定
	 * @param predTimeSetOnWeekday 所定時間設定（平日時）
	 */
	public void aggregate(
			WorkingSystem workingSystem,
			WorkType workType,
			WorkTypeDaysCountTable workTypeDaysCountTable,
			boolean isAttendanceDay,
			PredetemineTimeSetting predetermineTimeSet,
			PredetemineTimeSetting predTimeSetOnWeekday){

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
					
					// 枠時間の集計
					int addMinutes = 0;
					if (aggrAbsenceDays.getDays().v() > 0.0) {
						
						// 所定時間設定を取得
						PredetemineTimeSetting checkPredTimeSet = predetermineTimeSet;
						if (predetermineTimeSet == null) {
							checkPredTimeSet = predTimeSetOnWeekday;
						}
						if (checkPredTimeSet != null) {
							
							BreakDownTimeDay checkBreakDownTime = null;
							if (checkPredTimeSet.getPredTime() != null) {
								if (checkPredTimeSet.getPredTime().getPredTime() != null) {
									checkBreakDownTime = checkPredTimeSet.getPredTime().getPredTime();
								}
							}
							
							// 1日・午前・午後の判定
							if (workTypeDaysCountTable.getAbsenceWorkAtrMap().containsKey(absenceFrameNo)) {
								val workAtr = workTypeDaysCountTable.getAbsenceWorkAtrMap().get(absenceFrameNo);
								
								// 時間をセット
								if (workAtr == WorkAtr.OneDay && checkBreakDownTime != null) {
									if (checkBreakDownTime.getOneDay() != null) {
										addMinutes = checkBreakDownTime.getOneDay().v();
										targetAbsenceDays.addTime(addMinutes);
									}
								}
								if (workAtr == WorkAtr.Monring && checkBreakDownTime != null) {
									if (checkBreakDownTime.getMorning() != null) {
										addMinutes = checkBreakDownTime.getMorning().v();
										targetAbsenceDays.addTime(addMinutes);
									}
								}
								if (workAtr == WorkAtr.Afternoon && checkBreakDownTime != null) {
									if (checkBreakDownTime.getAfternoon() != null) {
										addMinutes = checkBreakDownTime.getAfternoon().v();
										targetAbsenceDays.addTime(addMinutes);
									}
								}
							}
						}
					}
					
					// 欠勤合計日数の集計
					this.totalAbsenceDays = this.totalAbsenceDays.addDays(aggrAbsenceDays.getDays().v());
					this.totalAbsenceTime = this.totalAbsenceTime.addMinutes(addMinutes);
				}
			}
		}
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AbsenceDaysOfMonthly target){
		
		this.totalAbsenceDays = this.totalAbsenceDays.addDays(target.totalAbsenceDays.v());
		this.totalAbsenceTime = this.totalAbsenceTime.addMinutes(target.totalAbsenceTime.v());
		for (val absenceDays : this.absenceDaysList.values()){
			val frameNo = absenceDays.getAbsenceFrameNo();
			if (target.absenceDaysList.containsKey(frameNo)){
				absenceDays.addDays(target.absenceDaysList.get(frameNo).getDays().v());
				absenceDays.addTime(target.absenceDaysList.get(frameNo).getTime().v());
			}
		}
		for (val targetAbsenceDays : target.absenceDaysList.values()){
			val frameNo = targetAbsenceDays.getAbsenceFrameNo();
			this.absenceDaysList.putIfAbsent(frameNo, targetAbsenceDays);
		}
	}
}
