package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.overtime;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.AttendanceTimeByWorkOfDaily;
import nts.uk.ctx.at.record.dom.actualworkinghours.daily.workrecord.repo.AttendanceTimeByWorkOfDailyRepository;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.repo.CalAttrOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.AttendanceLeavingGateOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerform;
import nts.uk.ctx.at.record.dom.daily.remarks.RemarksOfDailyPerformRepo;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.appreflect.CommonProcessCheckService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.shorttimework.repo.ShortTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workinformation.service.reflectprocess.WorkUpdateService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class PreOvertimeReflectServiceImpl implements PreOvertimeReflectService {
	@Inject
	private PreOvertimeReflectProcess priorProcess;
	@Inject
	private WorkInformationRepository workRepository;
	@Inject
	private StartEndTimeOffReflect startEndtimeOffReflect;
	@Inject
	private CalAttrOfDailyPerformanceRepository calAttrOfDaily;
	@Inject
	private AffiliationInforOfDailyPerforRepository affiliationInfor;
	@Inject
	private PCLogOnInfoOfDailyRepo pcLogOnInfo;
	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerError;
	@Inject
	private OutingTimeOfDailyPerformanceRepository outingTime;
	@Inject
	private WorkTypeOfDailyPerforRepository workTypeOfDailyPerforRepository;
	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDaily;
	@Inject
	private AttendanceTimeRepository attendanceTime;
	@Inject
	private AttendanceTimeByWorkOfDailyRepository attendanceTimeByWork;
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeaningOfDaily;
	@Inject
	private ShortTimeOfDailyPerformanceRepository shortTimeOfDaily;
	@Inject
	private SpecificDateAttrOfDailyPerforRepo specificDate;
	@Inject
	private AttendanceLeavingGateOfDailyRepo attendanceLeaving;
	@Inject
	private AnyItemValueOfDailyRepo anyItemValue;
	@Inject
	private EditStateOfDailyPerformanceRepository editState;
	@Inject
	private RemarksOfDailyPerformRepo remarks;
	@Inject
	private TemporaryTimeOfDailyPerformanceRepository temporary;
	@Inject
	private WorkUpdateService updateService;
	@Inject
	private CommonProcessCheckService commonService;
	@Override
	public boolean overtimeReflect(OvertimeParameter param) {
		try {
			//申請理由の反映
			updateService.reflectReason(param.getEmployeeId(), param.getDateInfo(), 
					param.getOvertimePara().getAppReason(),param.getOvertimePara().getOvertimeAtr());
			List<IntegrationOfDaily> lstDaily = this.getByOvertime(param, true);
			commonService.updateDailyAfterReflect(lstDaily);
			return true;
	
		} catch (Exception ex) {
			return false;
		}
	}


	@Override
	public IntegrationOfDaily calculateForAppReflect(String employeeId,
			GeneralDate dateData) {
		String companyId = AppContexts.user().companyId();
		Optional<WorkInfoOfDailyPerformance> optWorkInfor = workRepository.find(employeeId, dateData);
		if(!optWorkInfor.isPresent()) {
			return null;
		}
		WorkInfoOfDailyPerformance workInfor = workRepository.find(employeeId, dateData).get();
		//日別実績の計算区分
		CalAttrOfDailyPerformance calAtrrOfDailyData = calAttrOfDaily.find(employeeId, dateData);
		//日別実績の所属情報
		Optional<AffiliationInforOfDailyPerfor> findByKey = affiliationInfor.findByKey(employeeId, dateData);
		//日別実績の勤務種別
		Optional<WorkTypeOfDailyPerformance> workType = workTypeOfDailyPerforRepository.findByKey(employeeId, dateData);
		//日別実績のPCログオン情報
		Optional<PCLogOnInfoOfDaily> pcLogOnDarta = pcLogOnInfo.find(employeeId, dateData);
		//社員の日別実績エラー一覧
		List<EmployeeDailyPerError> findEror = employeeDailyPerError.findList(companyId, employeeId);
		//日別実績の外出時間帯
		Optional<OutingTimeOfDailyPerformance> findByEmployeeIdAndDate = outingTime.findByEmployeeIdAndDate(employeeId, dateData);		
		//日別実績の休憩時間帯
		List<BreakTimeOfDailyPerformance> lstBreakTime = breakTimeOfDaily.findByKey(employeeId, dateData);
		//日別実績の勤怠時間
		Optional<AttendanceTimeOfDailyPerformance> findAttendanceTime = attendanceTime.find(employeeId, dateData);
		//日別実績の作業別勤怠時間
		Optional<AttendanceTimeByWorkOfDaily> findTimeByWork = attendanceTimeByWork.find(employeeId, dateData);
		//日別実績の出退勤
		Optional<TimeLeavingOfDailyPerformance> findByKeyTimeLeaving = timeLeaningOfDaily.findByKey(employeeId, dateData);
		//日別実績の短時間勤務時間帯
		Optional<ShortTimeOfDailyPerformance> findShortTimeOfDaily = shortTimeOfDaily.find(employeeId, dateData);
		//日別実績の特定日区分
		Optional<SpecificDateAttrOfDailyPerfor> findSpecificData = specificDate.find(employeeId, dateData);
		//日別実績の入退門
		Optional<AttendanceLeavingGateOfDaily> findLeavingGate = attendanceLeaving.find(employeeId, dateData);
		//日別実績の任意項目
		Optional<AnyItemValueOfDaily> findAnyItem = anyItemValue.find(employeeId, dateData);
		//日別実績の編集状態
		List<EditStateOfDailyPerformance> lstEditState = editState.findByKey(employeeId, dateData);
		//日別実績の備考
		List<RemarksOfDailyPerform> remark = remarks.getRemarks(employeeId, dateData);
		//日別実績の臨時出退勤
		Optional<TemporaryTimeOfDailyPerformance> temporaryData = temporary.findByKey(employeeId, dateData);
		IntegrationOfDaily integration = new IntegrationOfDaily(workInfor, 
				calAtrrOfDailyData, 
				findByKey.isPresent() ? findByKey.get() : null,
				workType,
				pcLogOnDarta, 
				findEror, 
				findByEmployeeIdAndDate, 
				lstBreakTime, 
				findAttendanceTime, 
				findTimeByWork, 
				findByKeyTimeLeaving, 
				findShortTimeOfDaily, 
				findSpecificData, 
				findLeavingGate, 
				findAnyItem, 
				lstEditState,
				temporaryData,
				remark);
		return integration;
	}


	@Override
	public List<IntegrationOfDaily> getByOvertime(OvertimeParameter param, boolean isPre) {
		//予定勤種・就時反映後の予定勤種・就時を取得する
		//勤種・就時反映後の予定勤種・就時を取得する
		IntegrationOfDaily dailyInfor = this.calculateForAppReflect(param.getEmployeeId(), param.getDateInfo());
		//予定勤種・就時の反映
		priorProcess.workTimeWorkTimeUpdate(param, dailyInfor);
		//勤種・就時の反映
		boolean changeFlg = priorProcess.changeFlg(param, dailyInfor);
		//予定開始終了時刻の反映 
		priorProcess.startAndEndTimeReflectSche(param, changeFlg, dailyInfor);
		//開始終了時刻の反映
		startEndtimeOffReflect.startEndTimeOffReflect(param, dailyInfor);

		//残業時間を反映する
		//残業枠時間
		Optional<AttendanceTimeOfDailyPerformance> optAttendanceTime = dailyInfor.getAttendanceTimeOfDailyPerformance();
		if(optAttendanceTime.isPresent()) {
			//残業時間の反映
			priorProcess.getReflectOfOvertime(param, dailyInfor);
			//所定外深夜時間の反映
			priorProcess.overTimeShiftNight(param.getEmployeeId(), param.getDateInfo(), param.isTimeReflectFlg(),
					param.getOvertimePara().getOverTimeShiftNight(), dailyInfor);
			//フレックス時間の反映
			priorProcess.reflectOfFlexTime(param.getEmployeeId(), param.getDateInfo(), param.isTimeReflectFlg(),
					param.getOvertimePara().getFlexExessTime(), dailyInfor);
		}
		
		//日別実績の修正からの計算
		//○日別実績を置き換える Replace daily performance	
		List<IntegrationOfDaily> lstDaily = commonService.lstIntegrationOfDaily(dailyInfor, param.getEmployeeId(), param.getDateInfo());
		return lstDaily;
	}
	
	

}
