package nts.uk.ctx.at.record.dom.dailyprocess.calc;
/**
 * 残業枠時間
 * @author keisuke_hoshina
 *
 */

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;

@Getter
public class OverTimeFrameTime {
	/** 残業枠NO: 残業枠NO */
	private OverTimeFrameNo OverWorkFrameNo;
	/** 残業時間: 計算付き時間 */
	private TimeWithCalculation OverTimeWork;
	/** 振替時間: 計算付き時間 */
	private TimeWithCalculation TransferTime;
	/** 事前申請時間: 勤怠時間 */
	@Setter
	private AttendanceTime BeforeApplicationTime;
	/** 指示時間: 勤怠時間 */
	private AttendanceTime orderTime;
	
	/**
	 * Constructor 
	 */
	public OverTimeFrameTime(OverTimeFrameNo overWorkFrameNo, TimeWithCalculation overTimeWork,
			TimeWithCalculation transferTime, AttendanceTime beforeApplicationTime, AttendanceTime orderTime) {
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
	public OverTimeFrameTime changeOverTime(TimeWithCalculation overTime) {
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
	public OverTimeFrameTime changeTransTime(TimeWithCalculation transTime) {
		return new OverTimeFrameTime(this.OverWorkFrameNo,
									 this.OverTimeWork,
									 transTime,
									 this.BeforeApplicationTime,
									 this.orderTime);
	}
	
	/**
	 * 上限の時間から残業時間の差分をとる
	 * @param limitTime　上限の時間
	 * @return
	 */
//	public int calcLimit(int limitTime) {
//		return limitTime - overWorkFrae;
//	}
	
//	public boolean limitgreaterthanOverWorkTime(int limitTime) {|
//		if(limit>=OverWorkTime) {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}

}
