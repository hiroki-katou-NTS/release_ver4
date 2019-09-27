package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.holidayworktime;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonCalculateOfAppReflectParam;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime.PreOvertimeReflectService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AdTimeAndAnyItemAdUpService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.ReflectParameter;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.TimeReflectPara;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.ApplicationType;

@Stateless
public class PreHolidayWorktimeReflectServiceImpl implements PreHolidayWorktimeReflectService{
	@Inject
	private HolidayWorkReflectProcess holidayWorkProcess;
	@Inject
	private WorkUpdateService workUpdate;
	@Inject
	private PreOvertimeReflectService overTimeService;
	@Inject
	private WorkUpdateService scheWork;
	@Inject
	private AdTimeAndAnyItemAdUpService timeAndAnyItemUpService;
	@Inject
	private CommonProcessCheckService commonService;
	@Override
	public void preHolidayWorktimeReflect(HolidayWorktimePara holidayWorkPara, boolean isPre) {		
		IntegrationOfDaily daily = this.createIntegrationOfDailyStart(holidayWorkPara.getEmployeeId(), 
				holidayWorkPara.getBaseDate());
		if(isPre) {
			// 予定勤種・就時の反映
			holidayWorkProcess.updateScheWorkTimeType(holidayWorkPara.getEmployeeId(),
					holidayWorkPara.getBaseDate(), 
					holidayWorkPara.getHolidayWorkPara().getWorkTypeCode(), 
					holidayWorkPara.getHolidayWorkPara().getWorkTimeCode(), 
					holidayWorkPara.isScheReflectFlg(), isPre,
					holidayWorkPara.getScheAndRecordSameChangeFlg(),
					daily);	
		}
		
		//勤種・就時の反映
		ReflectParameter reflectInfo = new ReflectParameter(holidayWorkPara.getEmployeeId(), 
				holidayWorkPara.getBaseDate(), 
				holidayWorkPara.getHolidayWorkPara().getWorkTimeCode(), 
				holidayWorkPara.getHolidayWorkPara().getWorkTypeCode(),
				false); 
		workUpdate.updateWorkTimeTypeHoliwork(reflectInfo, false, daily);
		
		//予定開始時刻の反映
		//予定終了時刻の反映
		TimeReflectPara timeData = new TimeReflectPara(holidayWorkPara.getEmployeeId(), 
				holidayWorkPara.getBaseDate(), 
				holidayWorkPara.getHolidayWorkPara().getStartTime(), 
				holidayWorkPara.getHolidayWorkPara().getEndTime(), 
				1, 
				true, 
				true);
		if(isPre) {
			scheWork.updateScheStartEndTimeHoliday(timeData, daily);
		}
		//workRepository.updateByKey(daily.getWorkInformation());
		//開始時刻と終了時刻の反映
		if(holidayWorkPara.getHolidayWorkPara().getStartTime() != null
				&& holidayWorkPara.getHolidayWorkPara().getEndTime() != null
				&& (isPre || (!isPre && holidayWorkPara.isRecordReflectTimeFlg()))) {
			workUpdate.updateRecordStartEndTimeReflect(timeData, daily);
		}
		//休出時間の反映			
		holidayWorkProcess.reflectWorkTimeFrame(holidayWorkPara, isPre, daily);
		//事前所定外深夜時間の反映
		if(isPre) {
			workUpdate.updateTimeShiftNightHoliday(holidayWorkPara.getEmployeeId(),
					holidayWorkPara.getBaseDate(), 
					holidayWorkPara.getHolidayWorkPara().getNightTime(), 
					true, daily);	
		}
		//休憩時間を反映する
		holidayWorkProcess.reflectBreakTimeFrame(holidayWorkPara, isPre, daily);			
		CommonCalculateOfAppReflectParam calcParam = new CommonCalculateOfAppReflectParam(daily,
				holidayWorkPara.getEmployeeId(), holidayWorkPara.getBaseDate(),
				ApplicationType.BREAK_TIME_APPLICATION,
				holidayWorkPara.getHolidayWorkPara().getWorkTypeCode(),
				Optional.ofNullable(holidayWorkPara.getHolidayWorkPara().getWorkTimeCode()),
				Optional.ofNullable(holidayWorkPara.getHolidayWorkPara().getStartTime()),
				Optional.ofNullable(holidayWorkPara.getHolidayWorkPara().getEndTime()),
				isPre);
		commonService.calculateOfAppReflect(calcParam);
	}
	@Override
	public IntegrationOfDaily createIntegrationOfDailyStart(String employeeId, GeneralDate baseDate) {
		IntegrationOfDaily daily =overTimeService.calculateForAppReflect(employeeId, baseDate);
		if(!daily.getAttendanceTimeOfDailyPerformance().isPresent()
				|| !daily.getAttendanceTimeOfDailyPerformance().get().getActualWorkingTimeOfDaily().getTotalWorkingTime().getExcessOfStatutoryTimeOfDaily().getWorkHolidayTime().isPresent()) {
			AttendanceTimeOfDailyPerformance attendanceTime = AttendanceTimeOfDailyPerformance.allZeroValue(employeeId, baseDate);
			daily.setAttendanceTimeOfDailyPerformance(Optional.of(attendanceTime));
			timeAndAnyItemUpService.addAndUpdate(daily);
			//dailyTransaction.updated(employeeId, baseDate);
		}				
		return daily;
	}
}
