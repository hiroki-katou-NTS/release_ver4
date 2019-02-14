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
 * 月別実績の特別休暇日数
 * @author shuichi_ishida
 */
@Getter
public class SpcVacationDaysOfMonthly {

	/** 特別休暇合計日数 */
	private AttendanceDaysMonth totalSpcVacationDays;
	/** 特別休暇合計時間 */
	private AttendanceTimeMonth totalSpcVacationTime;
	/** 特別休暇日数 */
	private Map<Integer, AggregateSpcVacationDays> spcVacationDaysList;
	
	/**
	 * コンストラクタ
	 */
	public SpcVacationDaysOfMonthly(){
		
		this.totalSpcVacationDays = new AttendanceDaysMonth(0.0);
		this.totalSpcVacationTime = new AttendanceTimeMonth(0);
		this.spcVacationDaysList = new HashMap<>();
	}
	
	/**
	 * ファクトリー
	 * @param totalSpcVacationDays 特別休暇合計日数
	 * @param totalSpcVacationTime 特別休暇合計時間
	 * @param spcVacationDaysList 特別休暇日数
	 * @return 月別実績の特別休暇日数
	 */
	public static SpcVacationDaysOfMonthly of(
			AttendanceDaysMonth totalSpcVacationDays,
			AttendanceTimeMonth totalSpcVacationTime,
			List<AggregateSpcVacationDays> spcVacationDaysList){
		
		SpcVacationDaysOfMonthly domain = new SpcVacationDaysOfMonthly();
		domain.totalSpcVacationDays = totalSpcVacationDays;
		domain.totalSpcVacationTime = totalSpcVacationTime;
		for (val spcVacationDays : spcVacationDaysList){
			val spcVacationFrameNo = Integer.valueOf(spcVacationDays.getSpcVacationFrameNo());
			domain.spcVacationDaysList.putIfAbsent(spcVacationFrameNo, spcVacationDays);
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
		
		for (val aggrSpcVacationDays : workTypeDaysCountTable.getSpcVacationDaysMap().values()){
			
			// 特別休暇枠日数の集計
			if (aggrSpcVacationDays.getDays().greaterThan(0.0)){
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
					
					// 特別休暇日数に加算
					val spcVacationFrameNo = Integer.valueOf(aggrSpcVacationDays.getSpcVacationFrameNo());
					this.spcVacationDaysList.putIfAbsent(spcVacationFrameNo, new AggregateSpcVacationDays(spcVacationFrameNo));
					val targetSpcVacationDays = this.spcVacationDaysList.get(spcVacationFrameNo);
					targetSpcVacationDays.addDays(aggrSpcVacationDays.getDays().v());
					
					// 枠時間の集計
					int addMinutes = 0;
					if (aggrSpcVacationDays.getDays().v() > 0.0) {
						
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
							if (workTypeDaysCountTable.getSpcVacationWorkAtrMap().containsKey(spcVacationFrameNo)) {
								val workAtr = workTypeDaysCountTable.getSpcVacationWorkAtrMap().get(spcVacationFrameNo);
								
								// 時間をセット
								if (workAtr == WorkAtr.OneDay && checkBreakDownTime != null) {
									if (checkBreakDownTime.getOneDay() != null) {
										addMinutes = checkBreakDownTime.getOneDay().v();
										targetSpcVacationDays.addTime(addMinutes);
									}
								}
								if (workAtr == WorkAtr.Monring && checkBreakDownTime != null) {
									if (checkBreakDownTime.getMorning() != null) {
										addMinutes = checkBreakDownTime.getMorning().v();
										targetSpcVacationDays.addTime(addMinutes);
									}
								}
								if (workAtr == WorkAtr.Afternoon && checkBreakDownTime != null) {
									if (checkBreakDownTime.getAfternoon() != null) {
										addMinutes = checkBreakDownTime.getAfternoon().v();
										targetSpcVacationDays.addTime(addMinutes);
									}
								}
							}
						}
					}
					
					// 特別休暇合計日数の集計
					this.totalSpcVacationDays = this.totalSpcVacationDays.addDays(aggrSpcVacationDays.getDays().v());
					this.totalSpcVacationTime = this.totalSpcVacationTime.addMinutes(addMinutes);
				}
			}
		}
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(SpcVacationDaysOfMonthly target){
		
		this.totalSpcVacationDays = this.totalSpcVacationDays.addDays(target.totalSpcVacationDays.v());
		this.totalSpcVacationTime = this.totalSpcVacationTime.addMinutes(target.totalSpcVacationTime.v());
		for (val spcVacationDays : this.spcVacationDaysList.values()){
			val frameNo = spcVacationDays.getSpcVacationFrameNo();
			if (target.spcVacationDaysList.containsKey(frameNo)){
				spcVacationDays.addDays(target.spcVacationDaysList.get(frameNo).getDays().v());
				spcVacationDays.addTime(target.spcVacationDaysList.get(frameNo).getTime().v());
			}
		}
		for (val targetSpcVacationDays : target.spcVacationDaysList.values()){
			val frameNo = targetSpcVacationDays.getSpcVacationFrameNo();
			this.spcVacationDaysList.putIfAbsent(frameNo, targetSpcVacationDays);
		}
	}
}
