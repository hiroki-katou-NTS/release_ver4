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
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 集計休出時間
 * @author shuichi_ishida
 */
@Getter
public class AggregateHolidayWorkTime implements Cloneable {

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
	
	@Override
	public AggregateHolidayWorkTime clone() {
		AggregateHolidayWorkTime cloned = new AggregateHolidayWorkTime(this.holidayWorkFrameNo);
		try {
			cloned.holidayWorkTime = new TimeMonthWithCalculation(
					new AttendanceTimeMonth(this.holidayWorkTime.getTime().v()),
					new AttendanceTimeMonth(this.holidayWorkTime.getCalcTime().v()));
			cloned.beforeHolidayWorkTime = new AttendanceTimeMonth(this.beforeHolidayWorkTime.v());
			cloned.transferTime = new TimeMonthWithCalculation(
					new AttendanceTimeMonth(this.transferTime.getTime().v()),
					new AttendanceTimeMonth(this.transferTime.getCalcTime().v()));
			cloned.legalHolidayWorkTime = new AttendanceTimeMonth(this.legalHolidayWorkTime.v());
			cloned.legalTransferHolidayWorkTime = new AttendanceTimeMonth(this.legalTransferHolidayWorkTime.v());
		}
		catch (Exception e){
			throw new RuntimeException("AggregateHolidayWorkTime clone error.");
		}
		return cloned;
	}
	
	/**
	 * 時系列ワークを取得する
	 * @param ymd 年月日
	 * @return 時系列ワーク
	 */
	public HolidayWorkTimeOfTimeSeries getAndPutTimeSeriesWork(GeneralDate ymd){
		
		this.timeSeriesWorks.putIfAbsent(ymd, new HolidayWorkTimeOfTimeSeries(ymd, this.holidayWorkFrameNo));
		return this.timeSeriesWorks.get(ymd);
	}
	
	/**
	 * 休出時間（時系列ワーク）に加算する
	 * @param ymd 年月日
	 * @param holidayWorkTime 休出時間　（加算元）
	 */
	public void addHolidayWorkTimeInTimeSeriesWork(GeneralDate ymd, HolidayWorkFrameTime holidayWorkTime){
		
		this.timeSeriesWorks.putIfAbsent(ymd, new HolidayWorkTimeOfTimeSeries(ymd, holidayWorkTime.getHolidayFrameNo()));
		val targetTimeSeriesWork = this.timeSeriesWorks.get(ymd);
		
		targetTimeSeriesWork.addHolidayWorkTime(holidayWorkTime);
	}
	
	/**
	 * 法定内休出時間（時系列ワーク）に加算する
	 * @param ymd 年月日
	 * @param legalHolidayWorkTime 法定内休出時間　（加算元）
	 */
	public void addLegalHolidayWorkTimeInTimeSeriesWork(GeneralDate ymd, HolidayWorkFrameTime legalHolidayWorkTime){
		
		this.timeSeriesWorks.putIfAbsent(ymd, new HolidayWorkTimeOfTimeSeries(ymd, legalHolidayWorkTime.getHolidayFrameNo()));
		val targetTimeSeriesWork = this.timeSeriesWorks.get(ymd);
		
		targetTimeSeriesWork.addLegalHolidayWorkTime(legalHolidayWorkTime);
	}
	
	/**
	 * 法定内休出時間（時系列ワーク）に休出時間・振替時間を加算する
	 * @param ymd 年月日
	 * @param legalHolidayWorkTime 法定内休出時間　（加算元）
	 */
	public void addLegalHdwkAndTransInTimeSeriesWork(GeneralDate ymd, HolidayWorkFrameTime legalHolidayWorkTime){
		
		this.timeSeriesWorks.putIfAbsent(ymd, new HolidayWorkTimeOfTimeSeries(ymd, legalHolidayWorkTime.getHolidayFrameNo()));
		val targetTimeSeriesWork = this.timeSeriesWorks.get(ymd);
		
		targetTimeSeriesWork.addLegalHdwkAndTrans(legalHolidayWorkTime);
	}
	
	/**
	 * 集計する
	 * @param datePeriod 期間
	 */
	public void aggregate(DatePeriod datePeriod){

		this.holidayWorkTime = TimeMonthWithCalculation.ofSameTime(0);
		this.beforeHolidayWorkTime = new AttendanceTimeMonth(0);
		this.transferTime = TimeMonthWithCalculation.ofSameTime(0);
		this.legalHolidayWorkTime = new AttendanceTimeMonth(0);
		this.legalTransferHolidayWorkTime = new AttendanceTimeMonth(0);
		
		for (val timeSeriesWork : this.timeSeriesWorks.values()){
			if (!datePeriod.contains(timeSeriesWork.getYmd())) continue;
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
	
	/**
	 * 合算する
	 * @param target 加算対象
	 */
	public void sum(AggregateHolidayWorkTime target){
		
		this.holidayWorkTime = this.holidayWorkTime.addMinutes(
				target.holidayWorkTime.getTime().v(), target.holidayWorkTime.getCalcTime().v());
		this.beforeHolidayWorkTime = this.beforeHolidayWorkTime.addMinutes(target.beforeHolidayWorkTime.v());
		this.transferTime = this.transferTime.addMinutes(
				target.transferTime.getTime().v(), target.transferTime.getCalcTime().v());
		this.legalHolidayWorkTime = this.legalHolidayWorkTime.addMinutes(target.legalHolidayWorkTime.v());
		this.legalTransferHolidayWorkTime = this.legalTransferHolidayWorkTime.addMinutes(target.legalTransferHolidayWorkTime.v());
	}
}
