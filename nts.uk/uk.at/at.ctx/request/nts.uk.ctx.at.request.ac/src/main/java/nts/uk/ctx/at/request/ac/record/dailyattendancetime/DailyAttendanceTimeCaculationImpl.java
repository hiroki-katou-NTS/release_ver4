

package nts.uk.ctx.at.request.ac.record.dailyattendancetime;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.daily.TimeWithCalculation;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.DailyAttendanceTimePub;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.DailyAttendanceTimePubExport;
import nts.uk.ctx.at.record.pub.dailyprocess.attendancetime.DailyAttendanceTimePubImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculation;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.DailyAttendanceTimeCaculationImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.dailyattendancetime.TimeWithCalculationImport;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.holidaywork.HolidayWorkFrameNo;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.overtime.overtimeframe.OverTimeFrameNo;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

@Stateless
public class DailyAttendanceTimeCaculationImpl implements DailyAttendanceTimeCaculation {
	@Inject
	private DailyAttendanceTimePub dailyAttendanceTimePub;
	@Override
	public DailyAttendanceTimeCaculationImport getCalculation(String employeeID, GeneralDate ymd, String workTypeCode,
			String workTimeCode, Integer workStartTime, Integer workEndTime, Integer breakStartTime,
			Integer breakEndTime) {
		DailyAttendanceTimePubImport dailyAttendanceTimePubImport = new DailyAttendanceTimePubImport();
		dailyAttendanceTimePubImport.setEmployeeid(employeeID);
		dailyAttendanceTimePubImport.setYmd(ymd);
		dailyAttendanceTimePubImport.setWorkTypeCode(new WorkTypeCode(workTypeCode));
		dailyAttendanceTimePubImport.setWorkTimeCode(new WorkTimeCode(workTimeCode));
		dailyAttendanceTimePubImport.setWorkStartTime( new AttendanceTime(workStartTime));
		dailyAttendanceTimePubImport.setWorkEndTime(new AttendanceTime( workEndTime));
		dailyAttendanceTimePubImport.setBreakStartTime( new AttendanceTime(breakStartTime));
		dailyAttendanceTimePubImport.setBreakEndTime(new AttendanceTime( breakEndTime));
		
		DailyAttendanceTimePubExport dailyAttendanceTimePubExport = dailyAttendanceTimePub.calcDailyAttendance(dailyAttendanceTimePubImport);
		DailyAttendanceTimeCaculationImport dailyAttendanceTimeCaculationImport = new DailyAttendanceTimeCaculationImport(convertMapOverTime(dailyAttendanceTimePubExport.getOverTime()),
				convertMapHolidayWork(dailyAttendanceTimePubExport.getHolidayWorkTime()),
				convertBonusTime(dailyAttendanceTimePubExport.getBonusPayTime()),
				convertBonusTime(dailyAttendanceTimePubExport.getSpecBonusPayTime()),
				convert(dailyAttendanceTimePubExport.getFlexTime()),
				convert(dailyAttendanceTimePubExport.getMidNightTime()));
		return dailyAttendanceTimeCaculationImport;
	}
	
	/**
	 * @param timeCal
	 * @return
	 */
	private TimeWithCalculationImport convert(TimeWithCalculation timeCal){
		return new TimeWithCalculationImport(timeCal.getTime().v(), timeCal.getCalcTime().v());
	}
	
	/**
	 * @param overTime
	 * @return
	 */
	private Map<Integer,TimeWithCalculationImport> convertMapOverTime(Map<OverTimeFrameNo,TimeWithCalculation> overTime){
		Map<Integer,TimeWithCalculationImport> timeWithCal = new HashMap<>();
		for(Map.Entry<OverTimeFrameNo,TimeWithCalculation> entry : overTime.entrySet()){
			timeWithCal.put(entry.getKey().v(), convert(entry.getValue()));
		}
		return timeWithCal;
	}
	/**
	 * @param holidayWork
	 * @return
	 */
	private Map<Integer,TimeWithCalculationImport> convertMapHolidayWork(Map<HolidayWorkFrameNo,TimeWithCalculation> holidayWork){
		Map<Integer,TimeWithCalculationImport> timeWithCal = new HashMap<>();
		for(Map.Entry<HolidayWorkFrameNo,TimeWithCalculation> entry : holidayWork.entrySet()){
			timeWithCal.put(entry.getKey().v(), convert(entry.getValue()));
		}
		return timeWithCal;
	}
	/**
	 * @param bonusTime
	 * @return
	 */
	private Map<Integer,Integer> convertBonusTime(Map<Integer,AttendanceTime> bonusTime){
		Map<Integer,Integer> timeWithCal = new HashMap<>();
		for(Map.Entry<Integer,AttendanceTime> entry : bonusTime.entrySet()){
			timeWithCal.put(entry.getKey(), entry.getValue().v());
		}
		return timeWithCal;
	}
}
