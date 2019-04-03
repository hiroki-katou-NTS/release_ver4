package nts.uk.ctx.at.record.dom.daily.holidayworktime;

import lombok.Getter;
import lombok.Setter;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.TimeDivergenceWithCalculation;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;

/**
 * 休出枠時間
 * @author keisuke_hoshina
 *
 */
@Getter
public class HolidayWorkFrameTime {
	//休出枠時間No
	private HolidayWorkFrameNo holidayFrameNo;
	//休出時間
	@Setter
	private Finally<TimeDivergenceWithCalculation> holidayWorkTime;
	//振替時間
	private Finally<TimeDivergenceWithCalculation> transferTime;
	//事前申請時間
	@Setter	
	private Finally<AttendanceTime> beforeApplicationTime;
	
	/**
	 * Constructor 
	 */
	public HolidayWorkFrameTime(HolidayWorkFrameNo holidayFrameNo, Finally<TimeDivergenceWithCalculation> holidayWorkTime,
			Finally<TimeDivergenceWithCalculation> transferTime, Finally<AttendanceTime> beforeApplicationTime) {
		super();
		this.holidayFrameNo = holidayFrameNo;
		this.holidayWorkTime = holidayWorkTime;
		this.transferTime = transferTime;
		this.beforeApplicationTime = beforeApplicationTime;
	}
	
	public void addHolidayTime(AttendanceTime time,AttendanceTime calcTime) {
		this.holidayWorkTime = Finally.of(this.holidayWorkTime.get().addMinutes(time, calcTime));
	}
	
	public HolidayWorkFrameTime addHolidayTimeExistReturn(AttendanceTime time,AttendanceTime calcTime) {
		return new HolidayWorkFrameTime(this.holidayFrameNo, 
										Finally.of(this.holidayWorkTime.get().addMinutes(time, calcTime)), 
										this.transferTime, 
										this.beforeApplicationTime);
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
	public HolidayWorkFrameTime changeOverTime(TimeDivergenceWithCalculation holidayWorkTime) {
		return new HolidayWorkFrameTime(this.holidayFrameNo,
				 						Finally.of(holidayWorkTime),
				 						this.transferTime,
				 						this.getBeforeApplicationTime());
	}

	/**
	 * 振替時間を入れ替えて作り直す
	 * @return
	 */
	public HolidayWorkFrameTime changeTransTime(TimeDivergenceWithCalculation transTime) {
		return new HolidayWorkFrameTime(this.holidayFrameNo,
									 this.holidayWorkTime,
									 Finally.of(transTime),
									 this.getBeforeApplicationTime());
	}
	
	/**
	 * 事前申請を足す(4末納品きんきゅうたいおうby 保科)
	 * @param addTime
	 */
	public void addBeforeTime(AttendanceTime addTime) {
		if(this.getBeforeApplicationTime().isPresent())
			this.beforeApplicationTime = Finally.of(this.getBeforeApplicationTime().get().addMinutes(addTime.valueAsMinutes()));
	}
	
	/**
	 * 実績超過乖離時間の計算
	 * @return
	 */
	public int calcOverLimitDivergenceTime() {
		AttendanceTime holTime = new AttendanceTime(0);
		if(this.getHolidayWorkTime() != null
			&& this.getHolidayWorkTime().isPresent()
			&& this.getHolidayWorkTime().get().getDivergenceTime() != null)
			holTime = this.getHolidayWorkTime().get().getDivergenceTime();
		
		AttendanceTime transTime = new AttendanceTime(0);
		if(this.getTransferTime() != null
		   && this.getTransferTime().isPresent())
			transTime = this.getTransferTime().get().getDivergenceTime();
		return holTime.addMinutes(transTime.valueAsMinutes()).valueAsMinutes();  
	}

	/**
	 * 実績超過乖離時間が発生しているか判定する
	 * @return 乖離時間が発生している
	 */
	public boolean isOverLimitDivergenceTime() {
		return this.calcOverLimitDivergenceTime() > 0 ? true:false;
	}
	
	/**
	 * 実績時間の計算(申請超過用)
	 * @return
	 */
	public int calcOverLimitTime() {
		AttendanceTime holTime = new AttendanceTime(0);
		if(this.getHolidayWorkTime() != null
			&& this.getHolidayWorkTime().isPresent()
			&& this.getHolidayWorkTime().get().getTime()!= null)
			holTime = this.getHolidayWorkTime().get().getTime();
		
		AttendanceTime transTime = new AttendanceTime(0);
		if(this.getTransferTime() != null
		   && this.getTransferTime().isPresent())
			transTime = this.getTransferTime().get().getTime();
		return holTime.addMinutes(transTime.valueAsMinutes()).valueAsMinutes();  
	}
	
	/**
	 * 事前申請超過時間の計算
	 * @return
	 */
	public int calcPreOverLimitDivergenceTime() {
		if(this.getBeforeApplicationTime().isPresent()) {
			return calcOverLimitTime() - this.getBeforeApplicationTime().get().valueAsMinutes();
		}
		else {
			return calcOverLimitTime();
		}
	}

	/**
	 * 事前申請超過時間が発生しているか判定する
	 * @return 乖離時間が発生している
	 */
	public boolean isPreOverLimitDivergenceTime() {
		return this.calcPreOverLimitDivergenceTime() > 0 ? true:false;
	}
	
	
	public HolidayWorkFrameTime calcDiverGenceTime() {
		TimeDivergenceWithCalculation holidayWorkTime = this.holidayWorkTime.isPresent()?this.holidayWorkTime.get().calcDiverGenceTime():TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0));
		TimeDivergenceWithCalculation transferTime = this.transferTime.isPresent()?this.transferTime.get().calcDiverGenceTime():TimeDivergenceWithCalculation.sameTime(new AttendanceTime(0));
		
		return new HolidayWorkFrameTime(this.holidayFrameNo,Finally.of(holidayWorkTime),Finally.of(transferTime),this.beforeApplicationTime);
	}
	
}
