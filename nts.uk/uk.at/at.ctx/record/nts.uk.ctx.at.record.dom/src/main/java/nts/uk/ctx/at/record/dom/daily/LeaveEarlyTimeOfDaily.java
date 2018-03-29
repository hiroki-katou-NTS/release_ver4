package nts.uk.ctx.at.record.dom.daily;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Value;
import nts.uk.ctx.at.record.dom.daily.latetime.IntervalExemptionTime;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.CalculationRangeOfOneDay;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateLeaveEarlyTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LateTimeSheet;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.LeaveEarlyTimeSheet;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZoneRounding;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.TimeSpanForCalc;
import nts.uk.ctx.at.shared.dom.common.timerounding.Rounding;
import nts.uk.ctx.at.shared.dom.common.timerounding.TimeRoundingSetting;
import nts.uk.ctx.at.shared.dom.common.timerounding.Unit;
import nts.uk.ctx.at.shared.dom.vacation.setting.addsettingofworktime.HolidayCalcMethodSet;
import nts.uk.ctx.at.shared.dom.workrule.addsettingofworktime.NotUseAtr;

/**
 * 日別実績の早退時間
 * @author ken_takasu
 *
 */
@Value
public class LeaveEarlyTimeOfDaily {
	//早退時間
	private TimeWithCalculation leaveEarlyTime;
	//早退控除時間
	private TimeWithCalculation leaveEarlyDeductionTime;
	//勤務No
	private WorkNo workNo;
	//休暇使用時間
	private TimevacationUseTimeOfDaily timePaidUseTime;
	//インターバル時間
	private IntervalExemptionTime intervalTime;
	
	
	public LeaveEarlyTimeOfDaily(TimeWithCalculation leaveEarlyTime, TimeWithCalculation lateDeductionTime, WorkNo workNo,
			TimevacationUseTimeOfDaily timePaidUseTime, IntervalExemptionTime exemptionTime) {
		this.leaveEarlyTime = leaveEarlyTime;
		this.leaveEarlyDeductionTime = lateDeductionTime;
		this.workNo = workNo;
		this.timePaidUseTime = timePaidUseTime;
		this.intervalTime = exemptionTime;
	}
	
	
	/**
	 * 早退時間の計算
	 * @param oneDay
	 * @param workNo
	 * @param late
	 * @param holidayCalcMethodSet
	 * @return
	 */
	public static LeaveEarlyTimeOfDaily calcLeaveEarlyTime(CalculationRangeOfOneDay oneDay,
											   WorkNo workNo,
											   boolean leaveEarly, //日別実績の計算区分.遅刻早退の自動計算設定.早退
											   HolidayCalcMethodSet holidayCalcMethodSet
			) {

		//勤務Noに一致する早退時間をListで取得する
		List<LeaveEarlyTimeSheet> leaveEarlyTimeSheetList = oneDay.getWithinWorkingTimeSheet().get().getWithinWorkTimeFrame().stream()
																												 .filter(t -> new WorkNo(t.getLeaveEarlyTimeSheet().get().getWorkNo()).equals(workNo))
																												 .map(t -> t.getLeaveEarlyTimeSheet().get())
																												 .sorted((leaveEarlyTimeSheet1,leaveEarlyTimeSheet2) -> leaveEarlyTimeSheet1.getForDeducationTimeSheet().get().getTimeSheet().getStart()
																														 .compareTo(leaveEarlyTimeSheet2.getForDeducationTimeSheet().get().getTimeSheet().getStart()))
																												 .collect(Collectors.toList());
		LateLeaveEarlyTimeSheet forRecordTimeSheet = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(new TimeWithDayAttr(0),new TimeWithDayAttr(0),new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN,Rounding.ROUNDING_DOWN)),
				  																 new TimeSpanForCalc(new TimeWithDayAttr(0),new TimeWithDayAttr(0)));

		LateLeaveEarlyTimeSheet forDeductTimeSheet = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(new TimeWithDayAttr(0),new TimeWithDayAttr(0),new TimeRoundingSetting(Unit.ROUNDING_TIME_1MIN,Rounding.ROUNDING_DOWN)),
																				 new TimeSpanForCalc(new TimeWithDayAttr(0),new TimeWithDayAttr(0)));
		
		
		
		if(!leaveEarlyTimeSheetList.isEmpty()) {
			//早退時間帯を１つの時間帯にする。
			forRecordTimeSheet = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(leaveEarlyTimeSheetList.get(0).getForRecordTimeSheet().get().getTimeSheet().getStart(),
																									  leaveEarlyTimeSheetList.get(leaveEarlyTimeSheetList.size()-1).getForRecordTimeSheet().get().getTimeSheet().getEnd(),
																									  leaveEarlyTimeSheetList.get(0).getForRecordTimeSheet().get().getTimeSheet().getRounding()),
																				 new TimeSpanForCalc(leaveEarlyTimeSheetList.get(0).getForRecordTimeSheet().get().getTimeSheet().getStart(),
																						 			 leaveEarlyTimeSheetList.get(leaveEarlyTimeSheetList.size()-1).getForRecordTimeSheet().get().getTimeSheet().getEnd()));
		
			forRecordTimeSheet.setDeductionTimeSheet(leaveEarlyTimeSheetList.stream().flatMap(t -> t.getForRecordTimeSheet().get().getDeductionTimeSheet().stream()).collect(Collectors.toList()));
		
			forDeductTimeSheet = new LateLeaveEarlyTimeSheet(new TimeZoneRounding(leaveEarlyTimeSheetList.get(0).getForDeducationTimeSheet().get().getTimeSheet().getStart(),
																									  leaveEarlyTimeSheetList.get(leaveEarlyTimeSheetList.size()-1).getForDeducationTimeSheet().get().getTimeSheet().getEnd(),
																									  leaveEarlyTimeSheetList.get(0).getForDeducationTimeSheet().get().getTimeSheet().getRounding()),
																				new TimeSpanForCalc(leaveEarlyTimeSheetList.get(0).getForDeducationTimeSheet().get().getTimeSheet().getStart(),
																									leaveEarlyTimeSheetList.get(leaveEarlyTimeSheetList.size()-1).getForDeducationTimeSheet().get().getTimeSheet().getEnd()));
		
			forDeductTimeSheet.setDeductionTimeSheet(leaveEarlyTimeSheetList.stream().flatMap(t -> t.getForDeducationTimeSheet().get().getDeductionTimeSheet().stream()).collect(Collectors.toList()));
		}
		
		LeaveEarlyTimeSheet leaveEarlyTimeSheet = new LeaveEarlyTimeSheet(Optional.of(forRecordTimeSheet), Optional.of(forDeductTimeSheet), workNo.v(), Optional.empty());
		
		//早退計上時間の計算
		TimeWithCalculation leaveEarlyTime = leaveEarlyTimeSheet.calcForRecordTime(leaveEarly);
		//早退控除時間の計算
		TimeWithCalculation leaveEarlyDeductionTime = leaveEarlyTimeSheet.calcDedctionTime(leaveEarly,NotUseAtr.To);
		
		LeaveEarlyTimeOfDaily LeaveEarlyTimeOfDaily = new LeaveEarlyTimeOfDaily(leaveEarlyTime,
															  					leaveEarlyDeductionTime,
															  					workNo,
															  					new TimevacationUseTimeOfDaily(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)),
															  					new IntervalExemptionTime(new AttendanceTime(0),new AttendanceTime(0),new AttendanceTime(0)));
		return LeaveEarlyTimeOfDaily;
		
	}
	
	
}
