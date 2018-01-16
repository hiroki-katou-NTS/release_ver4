package nts.uk.ctx.at.record.dom.monthly.calc.totalworkingtime.hdwkandcompleave;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.holidayworktime.HolidayWorkFrameTime;
import nts.uk.ctx.at.record.dom.monthly.TimeMonthWithCalculation;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.timeseries.HolidayWorkTimeOfTimeSeries;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;

/**
 * 集計休出時間
 * @author shuichi_ishida
 */
@Getter
public class AggregateHolidayWorkTime {

	/** 休出枠NO */
	private final HolidayWorkFrameNo holidayWorkFrameNo;
	
	/** 休出時間 */
	private TimeMonthWithCalculation holidayWorkTime;
	/** 事前休出時間 */
	private AttendanceTimeMonth beforeHolidayWorkTime;
	/** 振替時間 */
	private TimeMonthWithCalculation transferTime;
	/** 法定内休出時間 */
	private AttendanceTimeMonth legalHolidayWorkTime;
	/** 法定内振替休出時間 */
	private AttendanceTimeMonth legalTransferHolidayWorkTime;
	
	/** 時系列ワーク */
	private Map<GeneralDate, HolidayWorkTimeOfTimeSeries> timeSeriesWorks;
	
	/**
	 * コンストラクタ
	 * @param holidayWorkFrameNo 休出枠NO
	 */
	public AggregateHolidayWorkTime(HolidayWorkFrameNo holidayWorkFrameNo){
		
		this.holidayWorkFrameNo = holidayWorkFrameNo;

		this.holidayWorkTime = TimeMonthWithCalculation.ofSameTime(0);
		this.beforeHolidayWorkTime = new AttendanceTimeMonth(0);
		this.transferTime = TimeMonthWithCalculation.ofSameTime(0);
		this.legalHolidayWorkTime = new AttendanceTimeMonth(0);
		this.legalTransferHolidayWorkTime = new AttendanceTimeMonth(0);
		
		this.timeSeriesWorks = new HashMap<>();
	}
	
	/**
	 * ファクトリー
	 * @param holidayWorkFrameNo 休出枠NO
	 * @param holidayWorkTime 休出時間
	 * @param beforeHolidayWorkTime 事前休出時間
	 * @param transferTime 振替時間
	 * @param legalHolidayWorkTime 法定内休出時間
	 * @param legalTransferHolidayWorkTime 法定内振替休出時間
	 * @return 集計休出時間
	 */
	public static AggregateHolidayWorkTime of(
			HolidayWorkFrameNo holidayWorkFrameNo,
			TimeMonthWithCalculation holidayWorkTime,
			AttendanceTimeMonth beforeHolidayWorkTime,
			TimeMonthWithCalculation transferTime,
			AttendanceTimeMonth legalHolidayWorkTime,
			AttendanceTimeMonth legalTransferHolidayWorkTime){
		
		val domain = new AggregateHolidayWorkTime(holidayWorkFrameNo);
		domain.holidayWorkTime = holidayWorkTime;
		domain.beforeHolidayWorkTime = beforeHolidayWorkTime;
		domain.transferTime = transferTime;
		domain.legalHolidayWorkTime = legalHolidayWorkTime;
		domain.legalTransferHolidayWorkTime = legalTransferHolidayWorkTime;
		return domain;
	}
	
	/**
	 * 時系列ワークを取得する
	 * @param ymd 年月日
	 * @return 時系列ワーク
	 */
	public HolidayWorkTimeOfTimeSeries getTimeSeriesWork(GeneralDate ymd){
		
		if (!this.timeSeriesWorks.containsKey(ymd)){
			this.timeSeriesWorks.put(ymd, new HolidayWorkTimeOfTimeSeries(ymd, this.holidayWorkFrameNo));
		}
		return this.timeSeriesWorks.get(ymd);
	}
	
	/**
	 * 休出時間（時系列ワーク）に加算する
	 * @param ymd 年月日
	 * @param holidayWorkTime 休出時間　（加算元）
	 */
	public void addHolidayWorkTimeInTimeSeriesWork(GeneralDate ymd, HolidayWorkFrameTime holidayWorkTime){
		
		if (!this.timeSeriesWorks.containsKey(ymd)){
			this.timeSeriesWorks.put(ymd, new HolidayWorkTimeOfTimeSeries(ymd, holidayWorkTime.getHolidayFrameNo()));
		}
		val targetTimeSeriesWork = this.timeSeriesWorks.get(ymd);
		
		targetTimeSeriesWork.addHolidayWorkTime(holidayWorkTime);
	}
	
	/**
	 * 法定内休出時間（時系列ワーク）に加算する
	 * @param ymd 年月日
	 * @param legalHolidayWorkTime 法定内休出時間　（加算元）
	 */
	public void addLegalHolidayWorkTimeInTimeSeriesWork(GeneralDate ymd, HolidayWorkFrameTime legalHolidayWorkTime){
		
		if (!this.timeSeriesWorks.containsKey(ymd)){
			this.timeSeriesWorks.put(ymd, new HolidayWorkTimeOfTimeSeries(ymd, legalHolidayWorkTime.getHolidayFrameNo()));
		}
		val targetTimeSeriesWork = this.timeSeriesWorks.get(ymd);
		
		targetTimeSeriesWork.addLegalHolidayWorkTime(legalHolidayWorkTime);
	}
	
	/**
	 * 集計する
	 */
	public void aggregate(){

		this.holidayWorkTime = TimeMonthWithCalculation.ofSameTime(0);
		this.beforeHolidayWorkTime = new AttendanceTimeMonth(0);
		this.transferTime = TimeMonthWithCalculation.ofSameTime(0);
		this.legalHolidayWorkTime = new AttendanceTimeMonth(0);
		this.legalTransferHolidayWorkTime = new AttendanceTimeMonth(0);
		
		for (val timeSeriesWork : this.timeSeriesWorks.values()){
			this.holidayWorkTime = this.holidayWorkTime.addMinutes(
					timeSeriesWork.getHolidayWorkTime().getHolidayWorkTime().get().getTime().v(),
					timeSeriesWork.getHolidayWorkTime().getHolidayWorkTime().get().getCalcTime().v());
			this.beforeHolidayWorkTime = this.beforeHolidayWorkTime.addMinutes(
					timeSeriesWork.getHolidayWorkTime().getBeforeApplicationTime().get().v());
			this.transferTime = this.transferTime.addMinutes(
					timeSeriesWork.getHolidayWorkTime().getTransferTime().get().getTime().v(),
					timeSeriesWork.getHolidayWorkTime().getTransferTime().get().getCalcTime().v());
			this.legalHolidayWorkTime = this.legalHolidayWorkTime.addMinutes(
					timeSeriesWork.getLegalHolidayWorkTime().getHolidayWorkTime().get().getTime().v());
			this.legalTransferHolidayWorkTime = this.legalTransferHolidayWorkTime.addMinutes(
					timeSeriesWork.getLegalHolidayWorkTime().getTransferTime().get().getTime().v());
		}
	}
}
