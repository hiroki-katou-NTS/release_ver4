package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;

/**
 * 時系列の残業時間
 * @author shuichi_ishida
 */
@Getter
public class OverTimeOfTimeSeries {

	/** 年月日 */
	private final GeneralDate ymd;
	
	/** 残業時間 */
	@Setter
	private OverTimeFrameTime overTime;
	/** 法定内残業時間 */
	@Setter
	private OverTimeFrameTime legalOverTime;
	
	/**
	 * コンストラクタ
	 * @param ymd 年月日
	 */
	public OverTimeOfTimeSeries(GeneralDate ymd, OverTimeFrameNo overTimeFrameNo){
		
		this.ymd = ymd;
		this.overTime = new OverTimeFrameTime(
				new OverTimeFrameNo(overTimeFrameNo.v()),
				TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				new AttendanceTime(0),
				new AttendanceTime(0));
		this.legalOverTime = new OverTimeFrameTime(
				new OverTimeFrameNo(overTimeFrameNo.v()),
				TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0)),
				new AttendanceTime(0),
				new AttendanceTime(0));
	}
	
	/**
	 * 残業時間に残業枠時間を加算する
	 * @param addTime 加算時間　（残業枠時間）
	 */
	public void addOverTime(OverTimeFrameTime addTime){
		this.overTime = this.addOverTimeFrameTime(this.overTime, addTime);
	}
	
	/**
	 * 法定内残業時間に残業枠時間を加算する
	 * @param addTime 加算時間　（残業枠時間）
	 */
	public void addLegalOverTime(OverTimeFrameTime addTime){
		this.legalOverTime = this.addOverTimeFrameTime(this.legalOverTime, addTime);
	}
	
	/**
	 * 法定内残業時間に残業枠時間の残業時間・振替時間を加算する
	 * @param addTime 加算時間　（残業枠時間）
	 */
	public void addLegalOverAndTrans(OverTimeFrameTime addTime){
		this.legalOverTime = this.addOverAndTransTime(this.legalOverTime, addTime);
	}
	
	/**
	 * 残業時間：残業時間を加算する
	 * @param overTime 残業時間　（計算付き時間）
	 */
	public void addOverTimeInOverTime(TimeDivergenceWithCalculation overTime){
		this.overTime = this.addOverTimeOnly(this.overTime, overTime);
	}
	
	/**
	 * 法定内残業時間：残業時間を加算する
	 * @param overTime 残業時間　（計算付き時間）
	 */
	public void addOverTimeInLegalOverTime(TimeDivergenceWithCalculation overTime){
		this.legalOverTime = this.addOverTimeOnly(this.legalOverTime, overTime);
	}
	
	/**
	 * 残業時間：振替時間を加算する
	 * @param transferTime 振替時間　（計算付き時間）
	 */
	public void addTransferTimeInOverTime(TimeDivergenceWithCalculation transferTime){
		this.overTime = this.addTransferTimeOnly(this.overTime, transferTime);
	}
	
	/**
	 * 法定内残業時間：振替時間を加算する
	 * @param transferTime 振替時間　（計算付き時間）
	 */
	public void addTransferTimeInLegalOverTime(TimeDivergenceWithCalculation transferTime){
		this.legalOverTime = this.addTransferTimeOnly(this.legalOverTime, transferTime);
	}
	
	/**
	 * 残業時間：事前申請時間を加算する
	 * @param beforeAppTime 事前申請時間　（計算付き時間）
	 */
	public void addBeforeAppTimeInOverTime(AttendanceTime beforeAppTime){
		this.overTime = this.addBeforeAppTimeOnly(this.overTime, beforeAppTime);
	}
	
	/**
	 * 残業枠時間に加算する
	 * @param target 残業枠時間　（加算先）
	 * @param addTime 加算する残業枠時間
	 * @return 残業枠時間　（加算後）
	 */
	private OverTimeFrameTime addOverTimeFrameTime(OverTimeFrameTime target, OverTimeFrameTime addTime){
		
		return new OverTimeFrameTime(
				target.getOverWorkFrameNo(),
				target.getOverTimeWork().addMinutes(
						addTime.getOverTimeWork().getTime(),
						addTime.getOverTimeWork().getCalcTime()),
				target.getTransferTime().addMinutes(
						addTime.getTransferTime().getTime(),
						addTime.getTransferTime().getCalcTime()),
				target.getBeforeApplicationTime().addMinutes(addTime.getBeforeApplicationTime().v()),
				target.getOrderTime().addMinutes(addTime.getOrderTime().v())
			);
	}
	
	/**
	 * 残業枠時間に残業時間・振替時間を加算する
	 * @param target 残業枠時間　（加算先）
	 * @param addTime 加算する残業枠時間
	 * @return 残業枠時間　（加算後）
	 */
	private OverTimeFrameTime addOverAndTransTime(OverTimeFrameTime target, OverTimeFrameTime addTime){
		
		return new OverTimeFrameTime(
				target.getOverWorkFrameNo(),
				target.getOverTimeWork().addMinutes(
						addTime.getOverTimeWork().getTime(),
						addTime.getOverTimeWork().getCalcTime()),
				target.getTransferTime().addMinutes(
						addTime.getTransferTime().getTime(),
						addTime.getTransferTime().getCalcTime()),
				target.getBeforeApplicationTime(),
				target.getOrderTime());
	}
	
	/**
	 * 残業枠時間の残業時間のみ加算する
	 * @param target 残業枠時間　（加算先）
	 * @param overTime 加算する時間　（計算付き時間）
	 * @return 残業枠時間　（加算後）
	 */
	private OverTimeFrameTime addOverTimeOnly(OverTimeFrameTime target, TimeDivergenceWithCalculation overTime){
		
		return new OverTimeFrameTime(
				target.getOverWorkFrameNo(),
				target.getOverTimeWork().addMinutes(
						overTime.getTime(),
						overTime.getCalcTime()),
				target.getTransferTime(),
				target.getBeforeApplicationTime(),
				target.getOrderTime());
	}
	
	/**
	 * 残業枠時間の振替時間のみ加算する
	 * @param target 残業枠時間　（加算先）
	 * @param transferTime 加算する時間　（計算付き時間）
	 * @return 残業枠時間　（加算後）
	 */
	private OverTimeFrameTime addTransferTimeOnly(OverTimeFrameTime target, TimeDivergenceWithCalculation transferTime){
		
		return new OverTimeFrameTime(
				target.getOverWorkFrameNo(),
				target.getOverTimeWork(),
				target.getTransferTime().addMinutes(
						transferTime.getTime(),
						transferTime.getCalcTime()),
				target.getBeforeApplicationTime(),
				target.getOrderTime());
	}
	
	/**
	 * 残業枠時間の事前申請時間のみ加算する
	 * @param target 残業枠時間　（加算先）
	 * @param beforeAppTime 加算する時間
	 * @return 残業枠時間　（加算後）
	 */
	private OverTimeFrameTime addBeforeAppTimeOnly(OverTimeFrameTime target, AttendanceTime beforeAppTime){
		
		if (beforeAppTime == null) return target;
		return new OverTimeFrameTime(
				target.getOverWorkFrameNo(),
				target.getOverTimeWork(),
				target.getTransferTime(),
				target.getBeforeApplicationTime().addMinutes(beforeAppTime.v()),
				target.getOrderTime());
	}

	/**
	 * 法定内残業の計算残業を残業時間の計算残業へ移送
	 */
	public void addCalcLegalOverTimeToCalcOverTime(){
		int calcLegalOverMinutes = this.legalOverTime.getOverTimeWork().getCalcTime().v();
		int calcLegalTransferMinutes = this.legalOverTime.getTransferTime().getCalcTime().v();
		this.legalOverTime = new OverTimeFrameTime(
				this.legalOverTime.getOverWorkFrameNo(),
				TimeDivergenceWithCalculation.createTimeWithCalculation(
						this.legalOverTime.getOverTimeWork().getTime(),
						new AttendanceTime(0)),
				TimeDivergenceWithCalculation.createTimeWithCalculation(
						this.legalOverTime.getTransferTime().getTime(),
						new AttendanceTime(0)),
				this.legalOverTime.getBeforeApplicationTime(),
				this.legalOverTime.getOrderTime()
			);
		this.overTime = new OverTimeFrameTime(
				this.overTime.getOverWorkFrameNo(),
				this.overTime.getOverTimeWork().addMinutes(
						new AttendanceTime(0),
						new AttendanceTime(calcLegalOverMinutes)),
				this.overTime.getTransferTime().addMinutes(
						new AttendanceTime(0),
						new AttendanceTime(calcLegalTransferMinutes)),
				this.overTime.getBeforeApplicationTime(),
				this.overTime.getOrderTime()
			);
	}
}
