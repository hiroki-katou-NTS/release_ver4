package nts.uk.ctx.at.record.dom.daily.holidayworktime;

import lombok.Getter;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.OverTimeFrameTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;

/**
 * 休出枠時間
 * @author keisuke_hoshina
 *
 */
@Getter
public class HolidayWorkFrameTime {
	private HolidayWorkFrameNo holidayFrameNo;
	private Finally<TimeWithCalculation> holidayWorkTime;
	private Finally<TimeWithCalculation> transferTime;
	private Finally<AttendanceTime> beforeApplicationTime;
	
	/**
	 * Constructor 
	 */
	public HolidayWorkFrameTime(HolidayWorkFrameNo holidayFrameNo, Finally<TimeWithCalculation> holidayWorkTime,
			Finally<TimeWithCalculation> transferTime, Finally<AttendanceTime> beforeApplicationTime) {
		super();
		this.holidayFrameNo = holidayFrameNo;
		this.holidayWorkTime = holidayWorkTime;
		this.transferTime = transferTime;
		this.beforeApplicationTime = beforeApplicationTime;
	}
	
	public void addHolidayTime(AttendanceTime holidayWorkTime) {
		this.holidayWorkTime = Finally.of(this.holidayWorkTime.get().addMinutes(holidayWorkTime, holidayWorkTime));
	}
	
	//休出枠Noのみ指定した休出枠Noに更新する
	public HolidayWorkFrameTime updateHolidayFrameNo(HolidayWorkFrameNo holidayFrameNo) {
		
		HolidayWorkFrameTime holidayWorkFrameTime = new HolidayWorkFrameTime(
				holidayFrameNo,
				this.holidayWorkTime,
				this.transferTime,
				this.beforeApplicationTime);
		return holidayWorkFrameTime;
	}


	/**
	 * 残業時間を入れ替えて作り直す
	 * @return
	 */
	public HolidayWorkFrameTime changeOverTime(TimeWithCalculation holidayWorkTime) {
		return new HolidayWorkFrameTime(this.holidayFrameNo,
				 						Finally.of(holidayWorkTime),
				 						this.transferTime,
				 						this.getBeforeApplicationTime());
	}

	/**
	 * 振替時間を入れ替えて作り直す
	 * @return
	 */
	public HolidayWorkFrameTime changeTransTime(TimeWithCalculation transTime) {
		return new HolidayWorkFrameTime(this.holidayFrameNo,
									 this.holidayWorkTime,
									 Finally.of(transTime),
									 this.getBeforeApplicationTime());
	}
	
}
