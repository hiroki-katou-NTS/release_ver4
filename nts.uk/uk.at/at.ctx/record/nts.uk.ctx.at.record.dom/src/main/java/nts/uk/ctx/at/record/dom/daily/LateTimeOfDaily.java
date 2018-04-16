package nts.uk.ctx.at.record.dom.daily;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Value;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.latetime.IntervalExemptionTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AttendanceItemDictionaryForCalc;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationRangeOfOneDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateLeaveEarlyTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateTimeSheet;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * 日別実績の遅刻時間
 * @author ken_takasu
 *
 */
@Value
public class LateTimeOfDaily {
	
	private TimeWithCalculation lateTime;
	private TimeWithCalculation lateDeductionTime;
	private WorkNo workNo;
	private TimevacationUseTimeOfDaily timePaidUseTime;
	private IntervalExemptionTime exemptionTime;
	



	public LateTimeOfDaily(TimeWithCalculation lateTime, TimeWithCalculation lateDeductionTime, WorkNo workNo,
			TimevacationUseTimeOfDaily timePaidUseTime, IntervalExemptionTime exemptionTime) {		
		this.lateTime = lateTime;
		this.lateDeductionTime = lateDeductionTime;
		this.workNo = workNo;
		this.timePaidUseTime = timePaidUseTime;
		this.exemptionTime = exemptionTime;
	}	

	
	/**
	 * 遅刻時間の計算
	 * @param oneDay
	 * @param workNo
	 * @param late
	 * @param holidayCalcMethodSet
	 * @return
	 */
	public static LateTimeOfDaily calcLateTime(CalculationRangeOfOneDay oneDay,
											   WorkNo workNo,
											   boolean late, //日別実績の計算区分.遅刻早退の自動計算設定.遅刻
											   HolidayCalcMethodSet holidayCalcMethodSet
			) {

		//勤務Noに一致する遅刻時間をListで取得する
		List<LateTimeSheet> lateTimeSheetList = oneDay.getWithinWorkingTimeSheet().isPresent()?oneDay.getWithinWorkingTimeSheet().get().getWithinWorkTimeFrame().stream()
																												 .filter(t -> new WorkNo(t.getLateTimeSheet().get().getWorkNo()).equals(workNo))
																												 .map(t -> t.getLateTimeSheet().get())
																												 .sorted((lateTimeSheet1,lateTimeSheet2) -> lateTimeSheet1.getForDeducationTimeSheet().get().getTimeSheet().getStart()
																														 .compareTo(lateTimeSheet2.getForDeducationTimeSheet().get().getTimeSheet().getStart()))
																												 .collect(Collectors.toList()):new ArrayList<>();
		LateLeaveEarlyTimeSheet forRecordTimeSheet = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(new TimeWithDayAttr(0),new TimeWithDayAttr(0),new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN,Rounding.ROUNDING_DOWN)),
																									  new TimeSpanForCalc(new TimeWithDayAttr(0),new TimeWithDayAttr(0)));

		LateLeaveEarlyTimeSheet forDeductTimeSheet = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(new TimeWithDayAttr(0),new TimeWithDayAttr(0),new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN,Rounding.ROUNDING_DOWN)),
				  new TimeSpanForCalc(new TimeWithDayAttr(0),new TimeWithDayAttr(0)));
		
		if(!lateTimeSheetList.isEmpty()) {
			//遅刻時間帯を１つの時間帯にする。
			forRecordTimeSheet = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(lateTimeSheetList.get(0).getForRecordTimeSheet().get().getTimeSheet().getStart(),
	             																  lateTimeSheetList.get(lateTimeSheetList.size()-1).getForRecordTimeSheet().get().getTimeSheet().getEnd(),
	             																  lateTimeSheetList.get(0).getForRecordTimeSheet().get().getTimeSheet().getRounding()),
															new TimeSpanForCalc(lateTimeSheetList.get(0).getForRecordTimeSheet().get().getTimeSheet().getStart(),
																				lateTimeSheetList.get(lateTimeSheetList.size()-1).getForRecordTimeSheet().get().getTimeSheet().getEnd()));
		
			forRecordTimeSheet.setDeductionTimeSheet(lateTimeSheetList.stream().flatMap(t -> t.getForRecordTimeSheet().get().getDeductionTimeSheet().stream()).collect(Collectors.toList()));
		
			forDeductTimeSheet = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(lateTimeSheetList.get(0).getForDeducationTimeSheet().get().getTimeSheet().getStart(),
																				  lateTimeSheetList.get(lateTimeSheetList.size()-1).getForDeducationTimeSheet().get().getTimeSheet().getEnd(),
																				  lateTimeSheetList.get(0).getForDeducationTimeSheet().get().getTimeSheet().getRounding()),
															new TimeSpanForCalc(lateTimeSheetList.get(0).getForDeducationTimeSheet().get().getTimeSheet().getStart(),
																			    lateTimeSheetList.get(lateTimeSheetList.size()-1).getForDeducationTimeSheet().get().getTimeSheet().getEnd()));
		
			forDeductTimeSheet.setDeductionTimeSheet(lateTimeSheetList.stream().flatMap(t -> t.getForDeducationTimeSheet().get().getDeductionTimeSheet().stream()).collect(Collectors.toList()));
		}
		
		LateTimeSheet lateTimeSheet = new LateTimeSheet(Optional.of(forRecordTimeSheet), Optional.of(forDeductTimeSheet), workNo.v(), Optional.empty());
		
		//遅刻計上時間の計算
		TimeWithCalculation lateTime = lateTimeSheet.calcForRecordTime(late);
		//遅刻控除時間の計算 
		TimeWithCalculation lateDeductionTime = lateTimeSheet.calcDedctionTime(late,NotUseAtr.To);
		
		LateTimeOfDaily lateTimeOfDaily = new LateTimeOfDaily(lateTime,
															  lateDeductionTime,
															  workNo,
															  new TimevacationUseTimeOfDaily(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)),
															  new IntervalExemptionTime(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)));
		return lateTimeOfDaily;
		
	}
	
	/**
	 * 遅刻時間のエラーチェック 
	 * @return エラーである。
	 */
	public List<EmployeeDailyPerError>  checkError(String employeeId,
												  GeneralDate targetDate,
												  String searchWord,
												  AttendanceItemDictionaryForCalc attendanceItemDictionary,
												  ErrorAlarmWorkRecordCode errorCode) {
		List<EmployeeDailyPerError> returnErrorList = new ArrayList<>();
		if(this.getLateTime().getTime().greaterThan(0)) {
			val itemId = attendanceItemDictionary.findId(searchWord+this.workNo.v());
			if(itemId.isPresent())
				returnErrorList.add(new EmployeeDailyPerError(AppContexts.user().companyCode(), employeeId, targetDate, errorCode, itemId.get()));
		}
		return returnErrorList;
	}
	
}
