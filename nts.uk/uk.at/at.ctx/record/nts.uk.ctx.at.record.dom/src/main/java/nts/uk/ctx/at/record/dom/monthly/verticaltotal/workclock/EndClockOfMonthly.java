package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimesMonthDom;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;

/**
 * 月別実績の終業時刻
 * @author shuichu_ishida
 */
@Getter
public class EndClockOfMonthly {

	/** 回数 */
	private AttendanceTimesMonthDom times;
	/** 合計時刻 */
	private AttendanceTimeMonth totalClock;
	/** 平均時刻 */
	private AttendanceTimeMonth averageClock;
	
	/**
	 * コンストラクタ
	 */
	public EndClockOfMonthly(){
		
		this.times = new AttendanceTimesMonthDom(0);
		this.totalClock = new AttendanceTimeMonth(0);
		this.averageClock = new AttendanceTimeMonth(0);
	}
	
	/**
	 * ファクトリー
	 * @param times 回数
	 * @param totalClock 合計時刻
	 * @param averageClock 平均時刻
	 * @return 月別実績の終業時刻
	 */
	public static EndClockOfMonthly of(
			AttendanceTimesMonthDom times,
			AttendanceTimeMonth totalClock,
			AttendanceTimeMonth averageClock){
		
		EndClockOfMonthly domain = new EndClockOfMonthly();
		domain.times = times;
		domain.totalClock = totalClock;
		domain.averageClock = averageClock;
		return domain;
	}
	
	/**
	 * 集計
	 * @param timeLeavingOfDaily 日別実績の出退勤
	 * @param predTimeSetForCalc 計算用所定時間設定
	 */
	public void aggregate(
			TimeLeavingOfDailyPerformance timeLeavingOfDaily,
			PredetermineTimeSetForCalc predTimeSetForCalc){
		
		if (timeLeavingOfDaily == null) return;
		if (predTimeSetForCalc == null) return;
		
		// 所定を超えて退勤しているかどうか判断
		for (val timeLeavingWork : timeLeavingOfDaily.getTimeLeavingWorks()){
			
			// 退勤　確認
			if (!timeLeavingWork.getLeaveStamp().isPresent()) continue;
			val leaveStamp = timeLeavingWork.getLeaveStamp().get();
			if (!leaveStamp.getStamp().isPresent()) continue;
			val stamp = leaveStamp.getStamp().get();
			if (stamp.getTimeWithDay() == null) continue;
			
			// 時間帯　確認
			val workNo = timeLeavingWork.getWorkNo();
			val timezoneUseOpt = predTimeSetForCalc.getTimeSheets(AttendanceHolidayAttr.FULL_TIME, workNo.v());
			if (!timezoneUseOpt.isPresent()) continue;
			val timezoneUse = timezoneUseOpt.get();
			if (timezoneUse.getUseAtr() == UseSetting.NOT_USE) continue;
			
			// 判断
			if (stamp.getTimeWithDay().v() <= timezoneUse.getEnd().v()) continue;
			
			// 退勤時刻を合計
			this.totalClock = this.totalClock.addMinutes(stamp.getTimeWithDay().v());
			
			// 回数を＋１
			this.times = this.times.addTimes(1);
		}
		
		// 平均時刻を計算
		this.averageClock = new AttendanceTimeMonth(0);
		if (this.times.v() != 0){
			this.averageClock = new AttendanceTimeMonth(this.totalClock.v() / this.times.v());
		}
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(EndClockOfMonthly target){
		
		this.times = this.times.addTimes(target.times.v());
		this.totalClock = this.totalClock.addMinutes(target.totalClock.v());
		
		this.averageClock = new AttendanceTimeMonth(0);
		if (this.times.v() != 0){
			this.averageClock = new AttendanceTimeMonth(this.totalClock.v() / this.times.v());
		}
	}
}
