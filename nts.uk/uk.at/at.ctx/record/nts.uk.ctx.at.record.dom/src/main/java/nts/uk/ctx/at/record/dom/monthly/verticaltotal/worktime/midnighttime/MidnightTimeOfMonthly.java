package nts.uk.ctx.at.record.dom.monthly.verticaltotal.worktime.midnighttime;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.daily.midnight.WithinStatutoryMidNightTime;
import nts.uk.ctx.at.record.dom.daily.withinworktime.WithinStatutoryTimeOfDaily;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

/**
 * 月別実績の深夜時間
 * @author shuichu_ishida
 */
@Getter
public class MidnightTimeOfMonthly {

	/** 残業深夜時間 */
	private TimeMonthWithCalculation overWorkMidnightTime;
	/** 法定内深夜時間 */
	private TimeMonthWithCalculation legalMidnightTime;
	/** 法定外深夜時間 */
	private IllegalMidnightTime illegalMidnightTime;
	/** 法定内休出深夜時間 */
	private TimeMonthWithCalculation legalHolidayWorkMidnightTime;
	/** 法定外休出深夜時間 */
	private TimeMonthWithCalculation illegalHolidayWorkMidnightTime;
	/** 祝日休出深夜時間 */
	private TimeMonthWithCalculation specialHolidayWorkMidnightTime;
	
	/**
	 * コンストラクタ
	 */
	public MidnightTimeOfMonthly(){
		
		this.overWorkMidnightTime = TimeMonthWithCalculation.ofSameTime(0);
		this.legalMidnightTime = TimeMonthWithCalculation.ofSameTime(0);
		this.illegalMidnightTime = new IllegalMidnightTime();
		this.legalHolidayWorkMidnightTime = TimeMonthWithCalculation.ofSameTime(0);
		this.illegalHolidayWorkMidnightTime = TimeMonthWithCalculation.ofSameTime(0);
		this.specialHolidayWorkMidnightTime = TimeMonthWithCalculation.ofSameTime(0);
	}
	
	/**
	 * ファクトリー
	 * @param overWorkMidnightTime 残業深夜時間
	 * @param legalMidnightTime 法定内深夜時間
	 * @param illegalMidnightTime 法定外深夜時間
	 * @param legalHolidayWorkMidnightTime 法定内休出深夜時間
	 * @param illegalHolidayWorkMidnightTime 法定外休出深夜時間
	 * @param specialHolidayWorkMidnightTime 祝日休出深夜時間
	 * @return 月別実績の深夜時間
	 */
	public static MidnightTimeOfMonthly of(
			TimeMonthWithCalculation overWorkMidnightTime,
			TimeMonthWithCalculation legalMidnightTime,
			IllegalMidnightTime illegalMidnightTime,
			TimeMonthWithCalculation legalHolidayWorkMidnightTime,
			TimeMonthWithCalculation illegalHolidayWorkMidnightTime,
			TimeMonthWithCalculation specialHolidayWorkMidnightTime){
		
		val domain = new MidnightTimeOfMonthly();
		domain.overWorkMidnightTime = overWorkMidnightTime;
		domain.legalMidnightTime = legalMidnightTime;
		domain.illegalMidnightTime = illegalMidnightTime;
		domain.legalHolidayWorkMidnightTime = legalHolidayWorkMidnightTime;
		domain.illegalHolidayWorkMidnightTime = illegalHolidayWorkMidnightTime;
		domain.specialHolidayWorkMidnightTime = specialHolidayWorkMidnightTime;
		return domain;
	}
	
	/**
	 * 集計
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 */
	public void aggregate(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily){

		if (attendanceTimeOfDaily == null) return;
		
		val totalWorkingTime = attendanceTimeOfDaily.getActualWorkingTimeOfDaily().getTotalWorkingTime();
		WithinStatutoryTimeOfDaily legalTime = totalWorkingTime.getWithinStatutoryTimeOfDaily();
		val illegalTime = totalWorkingTime.getExcessOfStatutoryTimeOfDaily();
		if (legalTime == null){
			legalTime = WithinStatutoryTimeOfDaily.createWithinStatutoryTimeOfDaily(
					new AttendanceTime(0),
					new AttendanceTime(0),
					new AttendanceTime(0),
					new WithinStatutoryMidNightTime(TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0))),
					new AttendanceTime(0));
		}
		
		// 所定内深夜時間を累積
		val legalMidnightTime = legalTime.getWithinStatutoryMidNightTime().getTime();
		this.legalMidnightTime = this.legalMidnightTime.addMinutes(
				legalMidnightTime.getTime().v(), legalMidnightTime.getCalcTime().v());
		
		// 所定外深夜時間を累積
		val illegalMidnightTime = illegalTime.getExcessOfStatutoryMidNightTime();
		this.illegalMidnightTime.addMinutesToTime(
				illegalMidnightTime.getTime().getTime().v(), illegalMidnightTime.getTime().getCalcTime().v());
		this.illegalMidnightTime.addMinutesToBeforeTime(
				illegalMidnightTime.getBeforeApplicationTime().v());
		
		// 残業深夜時間を累積
		if (illegalTime.getOverTimeWork().isPresent()){
			val overTime = illegalTime.getOverTimeWork().get();
			val overWorkMidnightTime = overTime.getExcessOverTimeWorkMidNightTime().get().getTime();
			this.overWorkMidnightTime = this.overWorkMidnightTime.addMinutes(
					overWorkMidnightTime.getTime().v(), overWorkMidnightTime.getCalcTime().v());
		}
		
		// 休出深夜時間を累積
		if (illegalTime.getWorkHolidayTime().isPresent()){
			val holidayWorkTime = illegalTime.getWorkHolidayTime().get();
			val hdwkMidnightTimeList = holidayWorkTime.getHolidayMidNightWork().get().getHolidayWorkMidNightTime();
			for (val hdwkMidnightTime : hdwkMidnightTimeList){
				val targetTime = hdwkMidnightTime.getTime();
				switch (hdwkMidnightTime.getStatutoryAtr()){
				case WithinPrescribedHolidayWork:
					this.legalHolidayWorkMidnightTime = this.legalHolidayWorkMidnightTime.addMinutes(
							targetTime.getTime().v(), targetTime.getCalcTime().v());
					break;
				case ExcessOfStatutoryHolidayWork:
					this.illegalHolidayWorkMidnightTime = this.illegalHolidayWorkMidnightTime.addMinutes(
							targetTime.getTime().v(), targetTime.getCalcTime().v());
					break;
				case PublicHolidayWork:
					this.specialHolidayWorkMidnightTime = this.specialHolidayWorkMidnightTime.addMinutes(
							targetTime.getTime().v(), targetTime.getCalcTime().v());
					break;
				}
			}
		}
	}
}
