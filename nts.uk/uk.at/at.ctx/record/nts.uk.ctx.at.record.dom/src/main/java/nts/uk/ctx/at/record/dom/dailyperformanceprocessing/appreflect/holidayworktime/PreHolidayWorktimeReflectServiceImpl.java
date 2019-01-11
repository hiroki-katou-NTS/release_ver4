package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.PreOvertimeReflectService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AdTimeAndAnyItemAdUpService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;

@Stateless
public class PreHolidayWorktimeReflectServiceImpl implements PreHolidayWorktimeReflectService{
	@Inject
	private HolidayWorkReflectProcess holidayWorkProcess;
	@Inject
	private WorkUpdateService workUpdate;
	@Inject
	private PreOvertimeReflectService overTimeService;
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private WorkUpdateService scheWork;
	@Inject
	private EditStateOfDailyPerformanceRepository dailyReposiroty;
	@Inject
	private AdTimeAndAnyItemAdUpService timeAndAnyItemUpService;
	@Inject
	private AttendanceTimeRepository attendanceTime;
	@Inject
	private CommonProcessCheckService commonService;
	@Override
	public boolean preHolidayWorktimeReflect(HolidayWorktimePara holidayWorkPara, boolean isPre) {		
		try {
			IntegrationOfDaily daily = this.createIntegrationOfDailyStart(holidayWorkPara.getEmployeeId(), 
					holidayWorkPara.getBaseDate(), holidayWorkPara.getHolidayWorkPara().getWorkTimeCode(), 
					holidayWorkPara.getHolidayWorkPara().getWorkTypeCode(), holidayWorkPara.getHolidayWorkPara().getStartTime(), 
					holidayWorkPara.getHolidayWorkPara().getEndTime());
			// 予定勤種・就時の反映
			daily = holidayWorkProcess.updateScheWorkTimeType(holidayWorkPara.getEmployeeId(),
					holidayWorkPara.getBaseDate(), 
					holidayWorkPara.getHolidayWorkPara().getWorkTypeCode(), 
					holidayWorkPara.getHolidayWorkPara().getWorkTimeCode(), 
					holidayWorkPara.isScheReflectFlg(), isPre,
					holidayWorkPara.getScheAndRecordSameChangeFlg(),
					daily);
			//勤種・就時の反映
			ReflectParameter reflectInfo = new ReflectParameter(holidayWorkPara.getEmployeeId(), 
					holidayWorkPara.getBaseDate(), 
					holidayWorkPara.getHolidayWorkPara().getWorkTimeCode(), 
					holidayWorkPara.getHolidayWorkPara().getWorkTypeCode(),
					false); 
			daily = workUpdate.updateWorkTimeTypeHoliwork(reflectInfo, false, daily);
			
			//予定開始時刻の反映
			//予定終了時刻の反映
			TimeReflectPara timeData = new TimeReflectPara(holidayWorkPara.getEmployeeId(), 
					holidayWorkPara.getBaseDate(), 
					holidayWorkPara.getHolidayWorkPara().getStartTime(), 
					holidayWorkPara.getHolidayWorkPara().getEndTime(), 
					1, 
					true, 
					true);
			daily = scheWork.updateScheStartEndTimeHoliday(timeData, daily);
			workRepository.updateByKeyFlush(daily.getWorkInformation());
			//開始時刻と終了時刻の反映
			if(holidayWorkPara.getHolidayWorkPara().getStartTime() != null
					&& holidayWorkPara.getHolidayWorkPara().getEndTime() != null) {
				TimeLeavingOfDailyPerformance timeLeaving = workUpdate.updateRecordStartEndTimeReflect(timeData);
				daily.setAttendanceLeave(Optional.of(timeLeaving));
			}
			//事前休出時間の反映
			daily = holidayWorkProcess.reflectWorkTimeFrame(holidayWorkPara.getEmployeeId(), 
					holidayWorkPara.getBaseDate(), 
					holidayWorkPara.getHolidayWorkPara().getMapWorkTimeFrame(),
					daily);
			//事前所定外深夜時間の反映
			daily = workUpdate.updateTimeShiftNightHoliday(holidayWorkPara.getEmployeeId(),
					holidayWorkPara.getBaseDate(), 
					holidayWorkPara.getHolidayWorkPara().getNightTime(), 
					true, daily);
			attendanceTime.updateFlush(daily.getAttendanceTimeOfDailyPerformance().get());
			
			List<EditStateOfDailyPerformance> lstEditState = dailyReposiroty.findByKey(holidayWorkPara.getEmployeeId(), holidayWorkPara.getBaseDate());
			daily.setEditState(lstEditState);
			
			commonService.calculateOfAppReflect(daily, holidayWorkPara.getEmployeeId(), holidayWorkPara.getBaseDate());
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	@Override
	public IntegrationOfDaily createIntegrationOfDailyStart(String employeeId, GeneralDate baseDate
			, String workTimeCode, String workTypeCode, Integer startTime, Integer endTime) {
		IntegrationOfDaily daily =overTimeService.calculateForAppReflect(employeeId, baseDate);
		if(daily == null) {
			return null;
		}
		AttendanceTimeOfDailyPerformance attendanceTime = AttendanceTimeOfDailyPerformance.allZeroValue(employeeId, baseDate);
		daily.setAttendanceTimeOfDailyPerformance(Optional.of(attendanceTime));
		timeAndAnyItemUpService.addAndUpdate(daily);		
		return daily;
	}

}
