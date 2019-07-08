package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon;

import java.util.Optional;

import lombok.Getter;
import lombok.val;
import nts.gul.util.value.MutableValue;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * 集計PCログオン時刻
 * @author shuichu_ishida
 */
@Getter
public class AggrPCLogonClock {

	/** 105：プレミアムデー */
	public static final WorkTypeCode PREMIUM_DAY = new WorkTypeCode("105");
	/** 002：半出　+　プレミアム */
	public static final WorkTypeCode HALF_WORK_PREMIUM = new WorkTypeCode("002");
	
	/** 合計日数 */
	private AttendanceDaysMonth totalDays;
	/** 合計時刻 */
	private AttendanceTimeMonth totalClock;
	/** 平均時刻 */
	private AttendanceTimeMonth averageClock;
	
	/**
	 * コンストラクタ
	 */
	public AggrPCLogonClock(){
		
		this.totalDays = new AttendanceDaysMonth(0.0);
		this.totalClock = new AttendanceTimeMonth(0);
		this.averageClock = new AttendanceTimeMonth(0);
	}

	/**
	 * ファクトリー
	 * @param totalDays 合計日数
	 * @param totalClock 合計時刻
	 * @param averageClock 平均時刻
	 * @return 集計PCログオン時刻
	 */
	public static AggrPCLogonClock of(
			AttendanceDaysMonth totalDays,
			AttendanceTimeMonth totalClock,
			AttendanceTimeMonth averageClock){
		
		AggrPCLogonClock domain = new AggrPCLogonClock();
		domain.totalDays = totalDays;
		domain.totalClock = totalClock;
		domain.averageClock = averageClock;
		return domain;
	}
	
	/**
	 * 集計
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 */
	public void aggregateLogOn(Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt){
		
		if (!pcLogonInfoOpt.isPresent()) return;
		val pcLogonInfo = pcLogonInfoOpt.get();

		boolean isExistLogon = false;
		
		// ログオン時刻を合計
		for (val logonInfo : pcLogonInfo.getLogOnInfo()){
			if (!logonInfo.getLogOn().isPresent()) continue;
			this.totalClock = this.totalClock.addMinutes(logonInfo.getLogOn().get().v());
			isExistLogon = true;
		}
		
		// 合計日数を加算
		if (isExistLogon) this.totalDays = this.totalDays.addDays(1.0);
		
		calcAverageClock();
	}
	
	/**
	 * 集計
	 * @param pcLogonInfoOpt 日別実績のPCログオン情報 
	 */
	public void aggregateLogOff(Optional<PCLogOnInfoOfDaily> pcLogonInfoOpt, TimeLeavingOfDailyPerformance timeLeavingOfDaily,
			WorkType workType, PredetermineTimeSetForCalc predTimeSetForCalc) {
		
		if (!pcLogonInfoOpt.isPresent()) return;
		Integer timeLeave = isLeaved(timeLeavingOfDaily);
		if(timeLeave != null) return;
		
		if(!workType.getWorkTypeCode().equals(HALF_WORK_PREMIUM) 
				&& !workType.getWorkTypeCode().equals(PREMIUM_DAY)) {
			val pcLogonInfo = pcLogonInfoOpt.get();

			boolean isExistLogoff = false;
			
			// ログオフ時刻を合計
			for (val logonInfo : pcLogonInfo.getLogOnInfo()){
				if (!logonInfo.getLogOff().isPresent()) continue;
				
				this.totalClock = this.totalClock.addMinutes(getLogOffClock(logonInfo.getLogOff().get().valueAsMinutes(), 
																			timeLeave, predTimeSetForCalc));
				isExistLogoff = true;
			}
			
			// 合計日数を加算
			if (isExistLogoff) 
				this.totalDays = this.totalDays.addDays(1.0);
		}
		
		calcAverageClock();
	}

	private void calcAverageClock() {
		// 平均時刻を計算
		this.averageClock = new AttendanceTimeMonth(0);
		if (this.totalDays.v() != 0.0){
			this.averageClock = new AttendanceTimeMonth(this.totalClock.v() / this.totalDays.v().intValue());
		}
	}
	
	private int getLogOffClock(int logOff, Integer timeLeave, PredetermineTimeSetForCalc predTimeSetForCalc) {
		
		boolean shouldUseLogOff = predTimeSetForCalc.getTimeSheets().stream().anyMatch(ts -> ts.getUseAtr() == UseSetting.USE
				&& ts.getStart().valueAsMinutes() <= logOff 
				&& ts.getEnd().valueAsMinutes() >= logOff);
		
		if(shouldUseLogOff){
			return logOff;
		}
		
		if(logOff > timeLeave){
			return logOff;
		}
		
		return timeLeave;
	}

	private Integer isLeaved(TimeLeavingOfDailyPerformance timeLeavingOfDaily) {
		MutableValue<Integer> isLeaved = new MutableValue<>(null);
		
		timeLeavingOfDaily.getAttendanceLeavingWork(1).ifPresent(tl -> {
			tl.getLeaveStamp().ifPresent(leave -> {
				leave.getStamp().ifPresent(ls -> {
					if (ls.getTimeWithDay() != null) isLeaved.set(ls.getTimeWithDay().valueAsMinutes());
				});
			});
		});
		
		return isLeaved.get();
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AggrPCLogonClock target){
		
		this.totalDays = this.totalDays.addDays(target.totalDays.v());
		this.totalClock = this.totalClock.addMinutes(target.totalClock.v());
		
		calcAverageClock();
	}
}
