package nts.uk.ctx.at.record.dom.dailyprocess.calc.errorcheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.OutingTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.BreakTimeStampIncorrectOrderChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.BreakTimeStampLeakageChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.DoubleStampAlgorithm;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.ExitStampCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.ExitStampIncorrectOrderCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.GoingOutStampLeakageChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.GoingOutStampOrderChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.HolidayStampCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.LackOfStampingAlgorithm;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.MissingOfTemporaryStampChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.PCLogOnOffIncorrectOrderCheck;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.PClogOnOffLackOfStamp;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.StampIncorrectOrderAlgorithm;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.TemporaryDoubleStampChecking;
import nts.uk.ctx.at.record.dom.workrecord.errorsetting.algorithm.TemporaryStampOrderChecking;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DailyRecordCreateErrorAlarmServiceImpl implements DailyRecordCreateErrorAlermService{
	
	@Inject
	private LackOfStampingAlgorithm lackOfStamping;
	
	@Inject
	private StampIncorrectOrderAlgorithm stampIncorrectOrderAlgorithm;

	@Inject
	private DoubleStampAlgorithm doubleStampAlgorithm;

	@Inject
	private MissingOfTemporaryStampChecking missingOfTemporaryStampChecking;

	@Inject
	private TemporaryStampOrderChecking temporaryStampOrderChecking;

	@Inject
	private TemporaryDoubleStampChecking temporaryDoubleStampChecking;

	@Inject
	private GoingOutStampLeakageChecking goingOutStampLeakageChecking;

	@Inject
	private GoingOutStampOrderChecking goingOutStampOrderChecking;

	@Inject
	private BreakTimeStampLeakageChecking breakTimeStampLeakageChecking;

	@Inject
	private BreakTimeStampIncorrectOrderChecking breakTimeStampIncorrectOrderChecking;
	
	@Inject
	private ExitStampCheck exitStampCheck;

	@Inject
	private ExitStampIncorrectOrderCheck exitStampIncorrectOrderCheck;

	@Inject
	private PClogOnOffLackOfStamp pClogOnOffLackOfStamp;

	@Inject
	private PCLogOnOffIncorrectOrderCheck pCLogOnOffIncorrectOrderCheck;
	
	@Inject
	private HolidayStampCheck holidayStampCheck;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Override
	//????????????
	public List<EmployeeDailyPerError> lackOfTimeLeavingStamping(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		String companyId = AppContexts.user().companyId();
		String empId = integrationOfDaily.getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getYmd();
		WorkInfoOfDailyPerformance workInfoOfDailyPerformance = new WorkInfoOfDailyPerformance(empId, targetDate,
				integrationOfDaily.getWorkInformation());
		TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance = new TimeLeavingOfDailyPerformance(empId,
				targetDate, integrationOfDaily.getAttendanceLeave().orElse(new TimeLeavingOfDailyAttd()));
		// ?????????????????????????????????????????? - (?????????????????????(s??t ch???m c??ng v??o-tan l??m))
		this.lackOfStamping.lackOfStamping(companyId, empId, targetDate, workInfoOfDailyPerformance,timeLeavingOfDailyPerformance).ifPresent(c -> {
			returnList.add(c);
		});
		// ??????????????????????????????????????????
		OutingTimeOfDailyPerformance outingTime = integrationOfDaily.getOutingTime().map(c -> new OutingTimeOfDailyPerformance(empId, targetDate, c)).orElse(null);
		returnList.addAll(goingOutStampLeakageChecking.goingOutStampLeakageChecking(companyId, empId, targetDate,
				outingTime));
		// ??????????????????????????????????????????
		
		breakTimeStampLeakageChecking.breakTimeStampLeakageChecking(companyId, empId, targetDate, new BreakTimeOfDailyPerformance(empId, targetDate, integrationOfDaily.getBreakTime()))
			.ifPresent(c -> {
			returnList.add(c);
		});
		// ??????????????????????????????????????????
		TemporaryTimeOfDailyPerformance temporaryTime = integrationOfDaily.getTempTime().map(c -> new TemporaryTimeOfDailyPerformance(empId, targetDate, c)).orElse(null);
		missingOfTemporaryStampChecking.missingOfTemporaryStampChecking(companyId, empId, targetDate, temporaryTime).ifPresent(c -> {
					returnList.add(c);
				});
		
		return returnList;
	}
	
	@Override
	//????????????
	public List<EmployeeDailyPerError> lackOfTimeLeavingStampingOnlyAttendance(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		String companyId = AppContexts.user().companyId();
		String empId = integrationOfDaily.getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getYmd();
		// ??????????????????????????????????????????
		WorkInfoOfDailyPerformance workInfo = new WorkInfoOfDailyPerformance(empId, targetDate, integrationOfDaily.getWorkInformation());
		TimeLeavingOfDailyPerformance timeLeaving = new TimeLeavingOfDailyPerformance(empId,
				targetDate, integrationOfDaily.getAttendanceLeave().orElse(new TimeLeavingOfDailyAttd()));
		// ??????????????????????????????????????????
		this.lackOfStamping.lackOfStamping(companyId, empId, targetDate, workInfo,timeLeaving).ifPresent(c -> {
			returnList.add(c);
		});
		return returnList;
	}
	
	@Override
	//?????????????????????
	public List<EmployeeDailyPerError> lackOfAttendanceGateStamping(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		// ?????????????????????????????????????????????
		AttendanceLeavingGateOfDaily attendanceLeavingGateOfDaily = integrationOfDaily.getAttendanceLeavingGate()
				.isPresent()
						? new AttendanceLeavingGateOfDaily(integrationOfDaily.getEmployeeId(),
								integrationOfDaily.getYmd(), integrationOfDaily.getAttendanceLeavingGate().get())
						: null;
		returnList.add(exitStampCheck.exitStampCheck(AppContexts.user().companyId(), integrationOfDaily.getEmployeeId(),
				integrationOfDaily.getYmd(), attendanceLeavingGateOfDaily,
				integrationOfDaily.getWorkInformation() == null ? null
						: new WorkInfoOfDailyPerformance(integrationOfDaily.getEmployeeId(),
								integrationOfDaily.getYmd(), integrationOfDaily.getWorkInformation())));
		return returnList;
	}
	
	@Override
	//PC????????????????????????
	public List<EmployeeDailyPerError> lackOfAttendancePCLogOnStamping(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		// PC?????????????????????????????????????????????
		PCLogOnInfoOfDaily pCLogOnInfoOfDaily = integrationOfDaily.getPcLogOnInfo().isPresent()
				? new PCLogOnInfoOfDaily(integrationOfDaily.getEmployeeId(), integrationOfDaily.getYmd(), integrationOfDaily.getPcLogOnInfo().get()):null;
		returnList.add(this.pClogOnOffLackOfStamp.pClogOnOffLackOfStamp(AppContexts.user().companyId(),
				integrationOfDaily.getEmployeeId(), integrationOfDaily.getYmd(), 
				pCLogOnInfoOfDaily,
				integrationOfDaily.getWorkInformation() == null ? null
						: new WorkInfoOfDailyPerformance(integrationOfDaily.getEmployeeId(),
								integrationOfDaily.getYmd(), integrationOfDaily.getWorkInformation())));
		return returnList;
	}

	
	@Override
	//??????????????????
	public List<EmployeeDailyPerError> stampIncorrectOrderAlgorithm(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		// ????????????????????????????????????????????????
		stampIncorrect(integrationOfDaily).ifPresent(c -> {
			returnList.add(c);	
		});
		
		//?????????????????????????????????????????????????????????
		returnList.addAll(stampIncorrectOrderAlgorithmOtherStamp(integrationOfDaily));
		
		return returnList;
	}
	
	@Override
	public Optional<EmployeeDailyPerError> stampIncorrect(IntegrationOfDaily integrationOfDaily){
		String companyId = AppContexts.user().companyId();
		String empId = integrationOfDaily.getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getYmd();
		// ????????????????????????????????????????????????
		TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance = integrationOfDaily.getAttendanceLeave()
				.isPresent()
						? new TimeLeavingOfDailyPerformance(empId, targetDate,
								integrationOfDaily.getAttendanceLeave().get())
						: null;
		return this.stampIncorrectOrderAlgorithm.stampIncorrectOrder(companyId, empId, targetDate,
				timeLeavingOfDailyPerformance);
	}
	
	/**
	 * ????????????????????????????????????????????????
	 */
	@Override
	public List<EmployeeDailyPerError> stampIncorrectOrderAlgorithmOtherStamp(IntegrationOfDaily integrationOfDaily){
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		String companyId = AppContexts.user().companyId();
		String empId = integrationOfDaily.getEmployeeId();
		GeneralDate targetDate = integrationOfDaily.getYmd();
		// ????????????????????????????????????????????????
		OutingTimeOfDailyPerformance outingTime = integrationOfDaily.getOutingTime().isPresent()
				? new OutingTimeOfDailyPerformance(empId, targetDate, integrationOfDaily.getOutingTime().get())
				: null;
		TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance = new TimeLeavingOfDailyPerformance(empId,
				targetDate, integrationOfDaily.getAttendanceLeave().orElse(null));
		TemporaryTimeOfDailyPerformance temporaryTimeOfDailyPerformance = integrationOfDaily.getTempTime().isPresent()
				? new TemporaryTimeOfDailyPerformance(empId, targetDate, integrationOfDaily.getTempTime().get())
				: null;
		returnList.addAll(goingOutStampOrderChecking.goingOutStampOrderChecking(companyId, empId, targetDate,
				outingTime,
				timeLeavingOfDailyPerformance, 
				temporaryTimeOfDailyPerformance));
		// ????????????????????????????????????????????????
		TimeLeavingOfDailyPerformance attendanceLeave = integrationOfDaily.getAttendanceLeave().isPresent()
				? new TimeLeavingOfDailyPerformance(empId, targetDate, integrationOfDaily.getAttendanceLeave().get())
				: null;
		returnList.addAll(breakTimeStampIncorrectOrderChecking.breakTimeStampIncorrectOrderChecking(
				companyId, empId, targetDate,new BreakTimeOfDailyPerformance(empId, targetDate, integrationOfDaily.getBreakTime())));
		// ????????????????????????????????????????????????
		returnList.add(temporaryStampOrderChecking.temporaryStampOrderChecking(empId, companyId, targetDate,
				temporaryTimeOfDailyPerformance,attendanceLeave));
		// PC??????????????????????????????????????????????????????????????????
		PCLogOnInfoOfDaily pCLogOnInfoOfDaily = integrationOfDaily.getPcLogOnInfo().isPresent()
				? new PCLogOnInfoOfDaily(integrationOfDaily.getEmployeeId(), integrationOfDaily.getYmd(), integrationOfDaily.getPcLogOnInfo().get()):null;
		returnList.add(this.pCLogOnOffIncorrectOrderCheck.pCLogOnOffIncorrectOrderCheck(companyId, empId, targetDate,
				pCLogOnInfoOfDaily, timeLeavingOfDailyPerformance));
		// ???????????????????????????????????????????????????
		AttendanceLeavingGateOfDaily attendanceLeavingGateOfDaily = integrationOfDaily.getAttendanceLeavingGate()
				.isPresent()
						? new AttendanceLeavingGateOfDaily(integrationOfDaily.getEmployeeId(),
								integrationOfDaily.getYmd(), integrationOfDaily.getAttendanceLeavingGate().get())
						: null;
		returnList.addAll(exitStampIncorrectOrderCheck.exitStampIncorrectOrderCheck(companyId, empId, targetDate,
				attendanceLeavingGateOfDaily, timeLeavingOfDailyPerformance));
		return returnList;
	}

	@Override
	//????????????????????????
	public List<EmployeeDailyPerError> doubleStampAlgorithm(IntegrationOfDaily integrationOfDaily) {
		List<EmployeeDailyPerError> returnList = new ArrayList<>();
		// ??????????????????????????????????????????
		TimeLeavingOfDailyPerformance timeLeavingOfDailyPerformance =integrationOfDaily.getAttendanceLeave() == null || !integrationOfDaily.getAttendanceLeave().isPresent() ?null : new TimeLeavingOfDailyPerformance(
				integrationOfDaily.getEmployeeId(), integrationOfDaily.getYmd(),
				integrationOfDaily.getAttendanceLeave().get());
		returnList.add(this.doubleStampAlgorithm.doubleStamp(
				AppContexts.user().companyId(), integrationOfDaily.getEmployeeId(), integrationOfDaily.getYmd(), timeLeavingOfDailyPerformance));
		//??????????????????????????????????????????????????????????????????
		// ??????????????????????????????????????????
		TemporaryTimeOfDailyPerformance temporaryTimeOfDailyPerformance = integrationOfDaily.getTempTime().isPresent()
				? new TemporaryTimeOfDailyPerformance(integrationOfDaily.getEmployeeId(), integrationOfDaily.getYmd(),
						integrationOfDaily.getTempTime().get())
				: null;
		returnList.add(temporaryDoubleStampChecking.temporaryDoubleStampChecking(
				AppContexts.user().companyId(), integrationOfDaily.getEmployeeId(),
				integrationOfDaily.getYmd(),temporaryTimeOfDailyPerformance));
		
		return returnList;
	}
	
	@Override
	//????????????
	public Optional<EmployeeDailyPerError> checkHolidayStamp(IntegrationOfDaily integration) {
		//??????????????????????????????????????????????????????
		if(integration.getWorkInformation() == null
		|| integration.getWorkInformation().getRecordInfo() == null
		|| integration.getWorkInformation().getRecordInfo().getWorkTypeCode() == null)
			return Optional.empty();
		
		val workType = workTypeRepository.findByDeprecated(AppContexts.user().companyId(), integration.getWorkInformation().getRecordInfo().getWorkTypeCode().toString());
		
		if( integration.getAttendanceLeave() == null
		 ||!integration.getAttendanceLeave().isPresent()
		 ||!workType.isPresent())
			return Optional.empty();
		
		//????????????????????????
		val resultError = holidayStampCheck.holidayStamp(AppContexts.user().companyId(), 
														 integration.getEmployeeId(), 
														 integration.getYmd(), 
														 workType.get(), 
														 new TimeLeavingOfDailyPerformance(integration.getEmployeeId(), integration.getYmd(), integration.getAttendanceLeave().get()) );
		if(resultError == null) {
			return Optional.empty();
		}
		else {
			return Optional.of(resultError);
		}
	}
	

}
