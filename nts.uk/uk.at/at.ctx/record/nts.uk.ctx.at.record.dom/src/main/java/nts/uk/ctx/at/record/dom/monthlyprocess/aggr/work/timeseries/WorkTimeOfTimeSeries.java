package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.premiumtarget.getvacationaddtime.AddSet;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;

/**
 * 時系列の就業時間
 * @author shuichi_ishida
 */
@Getter
public class WorkTimeOfTimeSeries {

	/** 年月日 */
	private GeneralDate ymd;
	
	/** 法定内時間 */
	private WithinStatutoryTimeOfDaily legalTime;
	/** 休暇加算時間 */
	private AttendanceTime vacationAddTime;
	/** 勤務種類 */
	private WorkType workType;
	
	/**
	 * コンストラクタ
	 * @param ymd 年月日
	 */
	public WorkTimeOfTimeSeries(GeneralDate ymd){
		
		this.ymd = ymd;
		this.legalTime = WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
				new AttendanceTime(0),
				new AttendanceTime(0),
				new AttendanceTime(0),
				new WithinStatutoryMidNightTime(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0))),
				new AttendanceTime(0));
		this.vacationAddTime = new AttendanceTime(0);
		this.workType = null;
	}
	
	/**
	 * ファクトリー
	 * @param ymd 年月日
	 * @param legalTime 法定内時間
	 * @param vacationAddTime 休暇加算時間
	 * @param workType 勤務種類
	 * @return 時系列の就業時間
	 */
	public static WorkTimeOfTimeSeries of(
			GeneralDate ymd,
			WithinStatutoryTimeOfDaily legalTime,
			AttendanceTime vacationAddTime,
			WorkType workType){
		
		val domain = new WorkTimeOfTimeSeries(ymd);
		domain.legalTime = legalTime;
		domain.vacationAddTime = vacationAddTime;
		domain.workType = workType;
		return domain;
	}
	
	/**
	 * 法定内時間に加算する
	 * @param addTime 加算する法定内時間
	 */
	public void addLegalTime(WithinStatutoryTimeOfDaily addTime){
		
		this.legalTime = WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
				this.legalTime.getWorkTime().addMinutes(addTime.getWorkTime().v()),
				this.legalTime.getActualWorkTime().addMinutes(addTime.getActualWorkTime().v()),
				this.legalTime.getWithinPrescribedPremiumTime().addMinutes(addTime.getWithinPrescribedPremiumTime().v()),
				new WithinStatutoryMidNightTime(
						this.legalTime.getWithinStatutoryMidNightTime().getTime().addMinutes(
								addTime.getWithinStatutoryMidNightTime().getTime().getTime(),
								addTime.getWithinStatutoryMidNightTime().getTime().getCalcTime())),
				this.legalTime.getVacationAddTime().addMinutes(addTime.getVacationAddTime().v()));
	}
	
	/**
	 * 集計対象時間を取得
	 * @param addSet 加算設定
	 * @return 集計対象時間
	 */
	public AttendanceTime getAggregateTargetTime(AddSet addSet){
		
		boolean isReturnWorkTime = false;		// 就業時間を返すかどうか
		
		// 大塚モードの確認
		if (true) {
			
			// 勤務種類が1日特別休暇かどうか判断
			if (this.workType != null) {
				if (this.workType.isOneDay()) {
					val workTypeClass = this.workType.getDailyWork().getOneDay();
					if (workTypeClass == WorkTypeClassification.SpecialHoliday) {
						
						// 特別休暇を加算する時、就業時間を返す
						if (addSet.isSpecialHoliday() == true) {
							isReturnWorkTime = true;
						}
					}
				}
			}
		}
		
		if (isReturnWorkTime) {
			
			// 就業時間を返す
			if (this.legalTime.getWorkTime() == null) return new AttendanceTime(0);
			return this.legalTime.getWorkTime();
		}
		
		// 実働就業時間を返す
		if (this.legalTime.getActualWorkTime() == null) return new AttendanceTime(0);
		return this.legalTime.getActualWorkTime();
	}
}
