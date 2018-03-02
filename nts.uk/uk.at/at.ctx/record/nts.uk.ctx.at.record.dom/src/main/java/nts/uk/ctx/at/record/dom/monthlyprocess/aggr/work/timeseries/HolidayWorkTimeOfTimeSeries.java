package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.gul.util.value.Finally;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;

/**
 * 時系列の休出時間
 * @author shuichi_ishida
 */
@Getter
public class HolidayWorkTimeOfTimeSeries {

	/** 年月日 */
	private final GeneralDate ymd;
	
	/** 休出時間 */
	@Setter
	private HolidayWorkFrameTime holidayWorkTime;
	/** 法定内休出時間 */
	@Setter
	private HolidayWorkFrameTime legalHolidayWorkTime;
	
	/**
	 * コンストラクタ
	 * @param ymd 年月日
	 * @param holidayWorkFrameNo 休出枠NO
	 */
	public HolidayWorkTimeOfTimeSeries(GeneralDate ymd, HolidayWorkFrameNo holidayWorkFrameNo){
		
		this.ymd = ymd;
		this.holidayWorkTime = new HolidayWorkFrameTime(
				holidayWorkFrameNo,
				Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),
				Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),
				Finally.of(new AttendanceTime(0)));
		this.legalHolidayWorkTime = new HolidayWorkFrameTime(
				holidayWorkFrameNo,
				Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),
				Finally.of(TimeWithCalculation.sameTime(new AttendanceTime(0))),
				Finally.of(new AttendanceTime(0)));
	}
	
	/**
	 * 休出時間に休出枠時間を加算する
	 * @param addTime 加算時間　（休出枠時間）
	 */
	public void addHolidayWorkTime(HolidayWorkFrameTime addTime){
		this.holidayWorkTime = this.addHolidayWorkFrameTime(this.holidayWorkTime, addTime);
	}
	
	/**
	 * 法定内休出時間に休出枠時間を加算する
	 * @param addTime 加算時間　（休出枠時間）
	 */
	public void addLegalHolidayWorkTime(HolidayWorkFrameTime addTime){
		this.legalHolidayWorkTime = this.addHolidayWorkFrameTime(this.legalHolidayWorkTime, addTime);
	}
	
	/**
	 * 休出時間：休出時間を加算する
	 * @param holidayWorkTime 休出時間　（計算付き時間）
	 */
	public void addHolidayWorkTimeInHolidayWorkTime(TimeWithCalculation holidayWorkTime){
		this.holidayWorkTime = this.addHolidayWorkTimeOnly(this.holidayWorkTime, holidayWorkTime);
	}
	
	/**
	 * 法定内休出時間：休出時間を加算する
	 * @param holidayWorkTime 休出時間　（計算付き時間）
	 */
	public void addHolidayWorkTimeInLegalHolidayWorkTime(TimeWithCalculation holidayWorkTime){
		this.legalHolidayWorkTime = this.addHolidayWorkTimeOnly(this.legalHolidayWorkTime, holidayWorkTime);
	}
	
	/**
	 * 休出時間：振替時間を加算する
	 * @param transferTime 休出時間　（計算付き時間）
	 */
	public void addTransferTimeInHolidayWorkTime(TimeWithCalculation transferTime){
		this.holidayWorkTime = this.addTransferTimeOnly(this.holidayWorkTime, transferTime);
	}
	
	/**
	 * 法定内休出時間：振替時間を加算する
	 * @param transferTime 休出時間　（計算付き時間）
	 */
	public void addTransferTimeInLegalHolidayWorkTime(TimeWithCalculation transferTime){
		this.legalHolidayWorkTime = this.addTransferTimeOnly(this.legalHolidayWorkTime, transferTime);
	}
	
	/**
	 * 休出枠時間に加算する
	 * @param target 休出枠時間　（加算先）
	 * @param addTime 加算する休出枠時間
	 * @return 休出枠時間　（加算後）
	 */
	private HolidayWorkFrameTime addHolidayWorkFrameTime(HolidayWorkFrameTime target, HolidayWorkFrameTime addTime){
		return new HolidayWorkFrameTime(
				target.getHolidayFrameNo(),
				Finally.of(target.getHolidayWorkTime().get().addMinutes(
						addTime.getHolidayWorkTime().get().getTime(),
						addTime.getHolidayWorkTime().get().getCalcTime())),
				Finally.of(target.getTransferTime().get().addMinutes(
						addTime.getTransferTime().get().getTime(),
						addTime.getTransferTime().get().getCalcTime())),
				Finally.of(target.getBeforeApplicationTime().get().addMinutes(
						addTime.getBeforeApplicationTime().get().v()))
			);
	}
	
	/**
	 * 休出枠時間の休出時間のみ加算する
	 * @param target 休出枠時間　（加算先）
	 * @param holidayWorkTime 加算する時間　（計算付き時間）
	 * @return 休出枠時間　（加算後）
	 */
	private HolidayWorkFrameTime addHolidayWorkTimeOnly(HolidayWorkFrameTime target, TimeWithCalculation holidayWorkTime){
		return new HolidayWorkFrameTime(
				target.getHolidayFrameNo(),
				Finally.of(target.getHolidayWorkTime().get().addMinutes(
						holidayWorkTime.getTime(),
						holidayWorkTime.getCalcTime())),
				Finally.of(target.getTransferTime().get()),
				Finally.of(target.getBeforeApplicationTime().get())
			);
	}
	
	/**
	 * 休出枠時間の振替時間のみ加算する
	 * @param target 休出枠時間　（加算先）
	 * @param transferTime 加算する時間　（計算付き時間）
	 * @return 休出枠時間　（加算後）
	 */
	private HolidayWorkFrameTime addTransferTimeOnly(HolidayWorkFrameTime target, TimeWithCalculation transferTime){
		return new HolidayWorkFrameTime(
				target.getHolidayFrameNo(),
				Finally.of(target.getHolidayWorkTime().get()),
				Finally.of(target.getTransferTime().get().addMinutes(
						transferTime.getTime(),
						transferTime.getCalcTime())),
				Finally.of(target.getBeforeApplicationTime().get())
			);
	}
}
