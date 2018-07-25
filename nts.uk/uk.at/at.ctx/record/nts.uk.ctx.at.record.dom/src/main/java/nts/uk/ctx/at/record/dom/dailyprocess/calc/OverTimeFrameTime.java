package nts.uk.ctx.at.record.dom.dailyprocess.calc;
/**
 * 残業枠時間
 * @author keisuke_hoshina
 *
 */

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;

@Getter
public class OverTimeFrameTime {
	/** 残業枠NO: 残業枠NO */
	private OverTimeFrameNo OverWorkFrameNo;
	/** 残業時間: 計算付き時間 */
	@Setter
	private TimeDivergenceWithCalculation OverTimeWork;
	/** 振替時間: 計算付き時間 */
	private TimeDivergenceWithCalculation TransferTime;
	/** 事前申請時間: 勤怠時間 */
	@Setter
	private AttendanceTime BeforeApplicationTime;
	/** 指示時間: 勤怠時間 */
	private AttendanceTime orderTime;
	
	/**
	 * Constructor 
	 */
	public OverTimeFrameTime(OverTimeFrameNo overWorkFrameNo, TimeDivergenceWithCalculation overTimeWork,
			TimeDivergenceWithCalculation transferTime, AttendanceTime beforeApplicationTime, AttendanceTime orderTime) {
		super();
		OverWorkFrameNo = overWorkFrameNo;
		OverTimeWork = overTimeWork;
		TransferTime = transferTime;
		BeforeApplicationTime = beforeApplicationTime;
		this.orderTime = orderTime;
	}
	
	
	public OverTimeFrameTime addOverTime(AttendanceTime time,AttendanceTime calcTime) {
		return new OverTimeFrameTime(this.OverWorkFrameNo,
									 this.OverTimeWork.addMinutes(time, calcTime),
									 this.TransferTime,
									 this.BeforeApplicationTime,
									 this.orderTime);
	}

	/**
	 * 残業枠Noを入れ替えて作り直す
	 * @return
	 */
	public OverTimeFrameTime changeFrameNo(Integer overTimeFrameNo) {
		return new OverTimeFrameTime(new OverTimeFrameNo(overTimeFrameNo),
									 this.OverTimeWork,
									 this.TransferTime,
									 this.BeforeApplicationTime,
									 this.orderTime);
	}
	
	/**
	 * 残業時間を入れ替えて作り直す
	 * @return
	 */
	public OverTimeFrameTime changeOverTime(TimeDivergenceWithCalculation overTime) {
		return new OverTimeFrameTime(this.OverWorkFrameNo,
									 overTime,
									 this.TransferTime,
									 this.BeforeApplicationTime,
									 this.orderTime);
	}

	/**
	 * 振替時間を入れ替えて作り直す
	 * @return
	 */
	public OverTimeFrameTime changeTransTime(TimeDivergenceWithCalculation transTime) {
		return new OverTimeFrameTime(this.OverWorkFrameNo,
									 this.OverTimeWork,
									 transTime,
									 this.BeforeApplicationTime,
									 this.orderTime);
	}
	
	/**
	 * 事前申請を足す(4末納品きんきゅうたいおうby 保科)
	 * @param addTime
	 */
	public void addBeforeTime(AttendanceTime addTime) {
		this.BeforeApplicationTime = this.getBeforeApplicationTime().addMinutes(addTime.valueAsMinutes());
	}
	
	/**
	 * 実績超過乖離時間の計算
	 * @return
	 */
	public int calcOverLimitDivergenceTime() {
		AttendanceTime overTime = new AttendanceTime(0);
		if(this.getOverTimeWork() != null
			&& this.getOverTimeWork().getDivergenceTime() != null)
			overTime = this.getOverTimeWork().getDivergenceTime();
		
		AttendanceTime transTime = new AttendanceTime(0);
		if(this.getTransferTime() != null
		   && this.getTransferTime().getDivergenceTime() != null)
			transTime = this.getTransferTime().getDivergenceTime();
		return overTime.addMinutes(transTime.valueAsMinutes()).valueAsMinutes();  
				 
	}

	/**
	 * 実績超過乖離時間が発生しているか判定する
	 * @return 乖離時間が発生している
	 */
	public boolean isOverLimitDivergenceTime() {
		return this.calcOverLimitDivergenceTime() > 0 ? true:false;
	}
	
	/**
	 * 申請超過時間の計算
	 * @return
	 */
	public int calcOverLimitTime() {
		AttendanceTime overTime = new AttendanceTime(0);
		if(this.getOverTimeWork() != null
			&& this.getOverTimeWork().getTime() != null)
			overTime = this.getOverTimeWork().getTime();
		
		AttendanceTime transTime = new AttendanceTime(0);
		if(this.getTransferTime() != null
		   && this.getTransferTime().getTime() != null)
			transTime = this.getTransferTime().getTime();
		return overTime.addMinutes(transTime.valueAsMinutes()).valueAsMinutes();  
				 
	}
	
	/**
	 * 事前申請超過時間の計算
	 * @return
	 */
	public int calcPreOverLimitDivergenceTime() {
		if(this.BeforeApplicationTime != null) {
			return calcOverLimitTime() - this.BeforeApplicationTime.valueAsMinutes();
		}
		return calcOverLimitTime();
	}

	/**
	 * 事前申請超過時間が発生しているか判定する
	 * @return 乖離時間が発生している
	 */
	public boolean isPreOverLimitDivergenceTime() {
		return this.calcPreOverLimitDivergenceTime() > 0 ? true:false;
	}
	
	/**
	 * 乖離時間のみ再計算
	 * @return
	 */
	public OverTimeFrameTime calcDiverGenceTime() {
		
		TimeDivergenceWithCalculation overTimeWork = this.OverTimeWork==null?TimeDivergenceWithCalculation.emptyTime():this.OverTimeWork.calcDiverGenceTime();
		TimeDivergenceWithCalculation transferTime = this.TransferTime==null?TimeDivergenceWithCalculation.emptyTime():this.TransferTime.calcDiverGenceTime();
		
		return new OverTimeFrameTime(this.getOverWorkFrameNo(),overTimeWork,transferTime,this.BeforeApplicationTime,this.orderTime);
	}
	
}
