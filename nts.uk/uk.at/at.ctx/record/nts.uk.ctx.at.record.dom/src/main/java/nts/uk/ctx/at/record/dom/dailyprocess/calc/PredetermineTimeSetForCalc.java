package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.val;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.HasTimeSpanList;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.worktime.CommonSetting.PredetermineTime;
import nts.uk.ctx.at.shared.dom.worktimeset.Timezone;
import nts.uk.ctx.at.shared.dom.worktimeset.WorkTimeSet;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 所定時間設定(計算用クラス)
 * @author ken_takasu
 *
 */
@Getter
public class PredetermineTimeSetForCalc implements HasTimeSpanList<Timezone>{
	
	private final List<Timezone> timeSheets;
	
	private final TimeWithDayAttr AMEndTime;

	private final TimeWithDayAttr PMStartTime;
	
	private PredetermineTime additionSet;
	
	private AttendanceTime oneDayRange;
	
	private TimeWithDayAttr startOneDayTime;

	/**
	 * 所定時間帯の時間を更新する
	 * @param dateStartTime
	 * @param rangeTimeDay
	 * @param predetermineTimeSheet
	 * @param additionSet
	 * @param timeSheets
	 */
	public PredetermineTimeSetForCalc(
			List<Timezone> timeSheets,
			TimeWithDayAttr AMEndTime,
			TimeWithDayAttr PMStartTime,
			PredetermineTime addtionSet,
            AttendanceTime oneDayRange,
			TimeWithDayAttr startOneDayTime) {
		this.timeSheets = timeSheets;
		this.AMEndTime = AMEndTime;
		this.PMStartTime = PMStartTime;
		this.additionSet = addtionSet;
		this.oneDayRange = oneDayRange;
		this.startOneDayTime = startOneDayTime;
	}
	
	/**
	 * Aggregateの所定時間から計算用所定時間クラスへの変換
	 */
	public static PredetermineTimeSetForCalc convertFromAggregatePremiumTime(WorkTimeSet predetermineTimeSet){
		return new PredetermineTimeSetForCalc(predetermineTimeSet.getPrescribedTimezoneSetting().getTimezone()
											  ,predetermineTimeSet.getPrescribedTimezoneSetting().getMorningEndTime()
											  ,predetermineTimeSet.getPrescribedTimezoneSetting().getAfternoonStartTime()
											  ,predetermineTimeSet.getAdditionSetID()
											  ,predetermineTimeSet.getRangeTimeDay()
											  ,predetermineTimeSet.getStartDateClock());
	}
	
	/**
	 * 勤務の単位を基に時間帯の開始、終了を補正
	 * @param dailyWork 1日の勤務
	 */
	public void correctPredetermineTimeSheet(DailyWork dailyWork,int workNo) {
		
		if (dailyWork.getAttendanceHolidayAttr().isHalfDayWorking()) {
			val workingTimeSheet = this.getHalfDayWorkingTimeSheetOf(dailyWork.getAttendanceHolidayAttr(),workNo);
			correctTimeSheet(workingTimeSheet.getStart(), workingTimeSheet.getEnd());
		}
	}

	
	public void correctTimeSheet(TimeWithDayAttr start, TimeWithDayAttr end) {
		  val corrected = this.extractBetween(start, end);
		  this.timeSheets.clear();
		  this.timeSheets.addAll(corrected);
	 }

	/**
	 * 午前出勤、午後出勤の判定
	 * @param attr
	 * @return
	 */
	private TimeSpanForCalc getHalfDayWorkingTimeSheetOf(AttendanceHolidayAttr attr,int workNo) {
		switch (attr) {
		case MORNING:
			return new TimeSpanForCalc(this.timeSheets.stream().filter(tc -> tc.getWorkNo() == workNo).map(tc -> tc.getStart()).collect(Collectors.toList()).get(0), this.AMEndTime);
		case AFTERNOON:
			return new TimeSpanForCalc(this.PMStartTime, this.timeSheets.stream().filter(tc -> tc.getWorkNo() == workNo).map(tc -> tc.getEnd()).collect(Collectors.toList()).get(0));
		case FULL_TIME:
		case HOLIDAY:
			return new TimeSpanForCalc(this.timeSheets.stream().filter(tc -> tc.getWorkNo() == workNo).map(tc -> tc.getStart()).collect(Collectors.toList()).get(0),
										this.timeSheets.stream().filter(tc -> tc.getWorkNo() == workNo).map(tc -> tc.getEnd()).collect(Collectors.toList()).get(0));
		default:
			throw new RuntimeException("unknown attr:" + attr);
		}
	}
	
	/**
	 * 所定時間の取得
	 * @return
	 */
	public AttendanceTime getpredetermineTime(DailyWork dailyWork) {
		switch(dailyWork.getAttendanceHolidayAttr()) {
		case FULL_TIME:
			return additionSet.getTime().getOneDay();
		case MORNING:
			return additionSet.getTime().getMorning();
		case AFTERNOON:
			return additionSet.getTime().getAfternoon();
		default:
			return new AttendanceTime(0);
		}
	}
	
	/**
	 * 所定時間設定を所定時間設定(計算用)に変換する
	 * @param master 所定時間設定
	 */
	public static PredetermineTimeSetForCalc convertMastarToCalc(WorkTimeSet master) {
		return new PredetermineTimeSetForCalc(master.getPrescribedTimezoneSetting().getTimezone(),
											  master.getPrescribedTimezoneSetting().getMorningEndTime(),
											  master.getPrescribedTimezoneSetting().getAfternoonStartTime(),
											  master.getAdditionSetID(),
											  master.getRangeTimeDay(),
											  master.getStartDateClock());
	}
	
	@Override
	public List<Timezone> getTimeSpanList() {
		return this.timeSheets;
	}
	
}
