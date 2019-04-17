package nts.uk.ctx.at.record.dom.monthly.verticaltotal.workclock.pclogon;

import lombok.Getter;
import lombok.val;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.common.days.AttendanceDaysMonth;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;

/**
 * 集計PCログオン乖離
 * @author shuichi_ishida
 */
@Getter
public class AggrPCLogonDivergence {

	/** 日数 */
	private AttendanceDaysMonth days;
	/** 合計時間 */
	private AttendanceTimeMonth totalTime;
	/** 平均時間 */
	private AttendanceTimeMonth averageTime;
	
	/**
	 * コンストラクタ
	 */
	public AggrPCLogonDivergence(){
		
		this.days = new AttendanceDaysMonth(0.0);
		this.totalTime = new AttendanceTimeMonth(0);
		this.averageTime = new AttendanceTimeMonth(0);
	}

	/**
	 * ファクトリー
	 * @param days 日数
	 * @param totalTime 合計時間
	 * @param averageTime 平均時間
	 * @return 集計PCログオン乖離
	 */
	public static AggrPCLogonDivergence of(
			AttendanceDaysMonth days,
			AttendanceTimeMonth totalTime,
			AttendanceTimeMonth averageTime){
		
		AggrPCLogonDivergence domain = new AggrPCLogonDivergence();
		domain.days = days;
		domain.totalTime = totalTime;
		domain.averageTime = averageTime;
		return domain;
	}
	
	/**
	 * 集計
	 * @param attendanceTimeOfDaily 日別実績の勤怠時間
	 * @param isLogon ログオン集計するか
	 */
	public void aggregate(AttendanceTimeOfDailyPerformance attendanceTimeOfDaily, boolean isLogon){

		if (attendanceTimeOfDaily == null) return;
		val stayingTime =  attendanceTimeOfDaily.getStayingTime();

		if (isLogon){
			
			// 合計時間を集計
			int logonMinutes = stayingTime.getBeforePCLogOnTime().v();
			if (logonMinutes > 0) this.totalTime = this.totalTime.addMinutes(logonMinutes);
			
			// 日数を集計する
			if (stayingTime.getBeforePCLogOnTime().v() > 0) this.days = this.days.addDays(1.0);
		}
		else {
			
			// 合計時間を集計
			int logoffMinutes = stayingTime.getAfterPCLogOffTime().v();
			if (logoffMinutes > 0) this.totalTime = this.totalTime.addMinutes(logoffMinutes);
			
			// 日数を集計する
			if (stayingTime.getAfterPCLogOffTime().v() > 0) this.days = this.days.addDays(1.0);
		}
		
		// 平均時間を計算する
		this.averageTime = new AttendanceTimeMonth(0);
		if (this.days.v() != 0.0){
			this.averageTime = new AttendanceTimeMonth(this.totalTime.v() / this.days.v().intValue());
		}
	}
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AggrPCLogonDivergence target){
		
		this.days = this.days.addDays(target.days.v());
		this.totalTime = this.totalTime.addMinutes(target.totalTime.v());
		
		this.averageTime = new AttendanceTimeMonth(0);
		if (this.days.v() != 0.0){
			this.averageTime = new AttendanceTimeMonth(this.totalTime.v() / this.days.v().intValue());
		}
	}
}
