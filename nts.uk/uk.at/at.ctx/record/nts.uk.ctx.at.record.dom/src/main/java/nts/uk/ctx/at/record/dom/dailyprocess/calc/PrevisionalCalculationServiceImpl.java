package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.requestlist.PrevisionalForImp;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.calculationattribute.enums.DivergenceTimeAttr;
import nts.uk.ctx.at.shared.dom.dailyattdcal.converter.DailyRecordShareFinder;
import nts.uk.ctx.at.shared.dom.dailyperformanceprocessing.AffiliationInforState;
import nts.uk.ctx.at.shared.dom.dailyperformanceprocessing.ReflectWorkInforDomainService;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.ScheduleRecordClassifi;
import nts.uk.ctx.at.shared.dom.scherec.attendanceitem.converter.util.AttendanceItemIdContainer;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalAtrOvertime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalFlexOvertimeSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalOvertimeSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalRestTimeSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.AutoCalcOfLeaveEarlySetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.TimeLimitUpperLimitSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.autocalsetting.deviationtime.AutoCalcSetOfDivergenceTime;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.WorkTimes;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.OutingTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.OutingTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.breakouting.breaking.BreakTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeActualStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.TimeChangeMeans;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkLocationCD;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.enu.DailyDomainGroup;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ChangeDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ICorrectionAttendanceRule;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.shortworktime.ShortTimeOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.shortworktime.ShortWorkingTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.CalculationState;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.NotUseAttribute;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.ScheduleTimeSheet;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailycalprocess.calculation.ManagePerCompanySet;
import nts.uk.ctx.at.shared.dom.scherec.dailyprocess.calc.CalculateOption;
import nts.uk.ctx.at.shared.dom.workrule.outsideworktime.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.shared.dom.worktime.common.TimeZone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.WorkNo;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.context.AppContexts;

/**
 * ????????????????????????(??????????????????????????????)
 * 
 * @author keisuke_hoshina
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PrevisionalCalculationServiceImpl implements ProvisionalCalculationService {

	@Inject
	private CalculateDailyRecordServiceCenter calculateDailyRecordServiceCenter;

	@Inject
	private ReflectWorkInforDomainService reflectWorkInforDomainServiceImpl;
	
	@Inject
	private ICorrectionAttendanceRule ICorrectionAttendanceRule;
	
	@Inject
	private DailyRecordShareFinder dailyRecordShareFinder;

	@Override
	public List<IntegrationOfDaily> calculation(List<PrevisionalForImp> impList) {
		return calculationPassCompanyCommonSetting(impList, Optional.empty());
	}
	
	@Override
	public List<IntegrationOfDaily> calculationPassCompanyCommonSetting(List<PrevisionalForImp> impList,Optional<ManagePerCompanySet> companySetting){
		List<IntegrationOfDaily> integraionList = new ArrayList<>();
		for(PrevisionalForImp imp:impList) {
			if (imp.getWorkTypeCode() == null)
				return Collections.emptyList();
			//?????????????????????????????????
			val provisionalRecord = createProvisionalDailyRecord(imp.getEmployeeId(), imp.getTargetDate(), imp.getWorkTypeCode(), imp.getWorkTimeCode(),
					imp.getTimeSheets());
			if (!provisionalRecord.isPresent())
				return Collections.emptyList();
			//??????????????????
			val provisionalDailyRecord = replaceDeductionTimeSheet(provisionalRecord.get(), imp.getBreakTimeSheets(),
				imp.getOutingTimeSheets(), imp.getShortWorkingTimeSheets(), imp.getEmployeeId(), imp.getTargetDate());
			provisionalDailyRecord.setEmployeeId(imp.getEmployeeId());
			provisionalDailyRecord.setYmd(imp.getTargetDate());
			integraionList.add(provisionalDailyRecord);
		}
		// ???????????????????????????????????????????????????????????????
		return calculateDailyRecordServiceCenter.calculateForSchedule(new CalculateOption(true, true), integraionList, companySetting);
	}

	/**
	 * ?????????????????????????????????
	 */
	private Optional<IntegrationOfDaily> createProvisionalDailyRecord(String employeeId, GeneralDate ymd,
			WorkTypeCode workTypeCode, WorkTimeCode workTimeCode, Map<Integer, TimeZone> timeSheets) {
		
		// ???????????????????????????
		//Optional<WorkInfoOfDailyPerformance> preworkInformation = workInformationRepository.find(employeeId, ymd);
		String setWorkTimeCode = null;
		if (workTimeCode != null)
			setWorkTimeCode = workTimeCode.v();
		
		Optional<IntegrationOfDaily> domainDaily = dailyRecordShareFinder.find(employeeId, ymd);
		
		WorkInfoOfDailyPerformance workInformation = new WorkInfoOfDailyPerformance(employeeId,
				new WorkInformation(workTypeCode.v(), setWorkTimeCode), CalculationState.No_Calculated,
				NotUseAttribute.Not_use, NotUseAttribute.Not_use, ymd, new ArrayList<>(), Optional.empty());
		
		// ???????????????????????????????????????
		//Optional<ShortTimeOfDailyPerformance> ShortTimeOfDailyPerformance = Optional.empty();
		// ??????????????????????????????
		// ;
		// ????????????????????????
		List<TimeLeavingWork> timeLeavingWorks = new ArrayList<>();
		for (Map.Entry<Integer, TimeZone> key : timeSheets.entrySet()) {
			WorkStamp attendance = new WorkStamp(key.getValue().getStart(),
					new WorkLocationCD("01"), TimeChangeMeans.AUTOMATIC_SET,null);
			WorkStamp leaving = new WorkStamp(key.getValue().getEnd(),
					new WorkLocationCD("01"), TimeChangeMeans.AUTOMATIC_SET,null);
			TimeActualStamp attendanceStamp = new TimeActualStamp(attendance, attendance, key.getKey());
			TimeActualStamp leavingStamp = new TimeActualStamp(leaving, leaving, key.getKey());
			TimeLeavingWork timeLeavingWork = new TimeLeavingWork(new WorkNo(key.getKey()), attendanceStamp,
					leavingStamp);

			timeLeavingWorks.add(timeLeavingWork);
		}
		TimeLeavingOfDailyPerformance timeAttendance = new TimeLeavingOfDailyPerformance(employeeId,
				new WorkTimes(timeSheets.size()), timeLeavingWorks, ymd);
		workInformation.getWorkInformation().setScheduleTimeSheets(timeLeavingWorks.stream()
				.filter(x -> x.getAttendanceTime().isPresent() && x.getLeaveTime().isPresent())
				.map(x -> new ScheduleTimeSheet(x.getWorkNo(), x.getAttendanceTime().get(), x.getLeaveTime().get()))
				.collect(Collectors.toList()));

		// ?????????????????????????????????
		val calAttrOfDailyPerformance = new CalAttrOfDailyPerformance(employeeId, ymd,
				new AutoCalFlexOvertimeSetting(
						new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS)),
				new AutoCalRaisingSalarySetting(true, true),
				new AutoCalRestTimeSetting(
						new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS),
						new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS)),
				new AutoCalOvertimeSetting(
						new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS),
						new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS),
						new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS),
						new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS),
						new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS),
						new AutoCalSetting(TimeLimitUpperLimitSetting.NOUPPERLIMIT, AutoCalAtrOvertime.CALCULATEMBOSS)),
				new AutoCalcOfLeaveEarlySetting(true, true),
				new AutoCalcSetOfDivergenceTime(DivergenceTimeAttr.USE));
		// ?????????????????????????????????
		// ??????????????????????????????????????????????????????????????????????????????????????????
		/*-----"01"????????????  --------*/
		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????
		// ??????????????????????????????????????????????????????????????????empCalAndSumExecLogID?????????????????????????????????
		val employeeState = reflectWorkInforDomainServiceImpl
				.createAffiliationInforOfDailyPerfor(AppContexts.user().companyId(), employeeId, ymd, "01");
		if (!employeeState.getAffiliationInforOfDailyPerfor().isPresent()) {
			org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
			employeeState.getErrMesInfos().forEach(tc -> { if(tc.getEmployeeID() == "001")
																log.info("??????????????????????????????????????????");
														   if(tc.getEmployeeID() == "002")
															    log.info("??????????????????????????????????????????");
														   if(tc.getEmployeeID() == "003")
															    log.info("??????????????????????????????????????????");
														   if(tc.getEmployeeID() == "004")
															   log.info("??????????????????????????????????????????");
			});
			return Optional.empty();		
		}
		
		return Optional.of(correctAttendanceRule(domainDaily, employeeId, ymd, calAttrOfDailyPerformance, employeeState,
				timeAttendance.getAttendance(), workInformation.getWorkInformation(),
				Optional.empty(), Optional.empty(),
				Optional.empty()));
	}

	//??????????????????????????????????????????
	private IntegrationOfDaily correctAttendanceRule(Optional<IntegrationOfDaily> data,
			String employeeId, GeneralDate ymd, 
			CalAttrOfDailyPerformance calAttrOfDailyPerformance, AffiliationInforState employeeState,
			TimeLeavingOfDailyAttd timeAttendance, WorkInfoOfDailyAttendance workInformation,
			Optional<BreakTimeOfDailyAttd> breakTime, Optional<OutingTimeOfDailyAttd> outingTime,
			Optional<ShortTimeOfDailyAttd> shortTime) {
		
		IntegrationOfDaily domainDaily = data.orElse(createNewDomain(employeeId, ymd, workInformation,
				calAttrOfDailyPerformance, employeeState, timeAttendance));
		boolean changeWork = setDataIntoDomain(domainDaily, timeAttendance, workInformation, calAttrOfDailyPerformance,
				employeeState, breakTime, outingTime, shortTime);
		setEditState(domainDaily);
		domainDaily = ICorrectionAttendanceRule.process(domainDaily, new ChangeDailyAttendance(
				data.map(x -> changeWork).orElse(true), false, false, true, ScheduleRecordClassifi.RECORD, false));
		return domainDaily;

	}
	
	private IntegrationOfDaily createNewDomain(String employeeId, GeneralDate ymd, WorkInfoOfDailyAttendance workInformation,
			CalAttrOfDailyPerformance calAttrOfDailyPerformance, AffiliationInforState employeeState, TimeLeavingOfDailyAttd attendance) {
		return new IntegrationOfDaily(
				employeeId,
				ymd,
				workInformation, //workInformation
				calAttrOfDailyPerformance.getCalcategory(),//calAttr
				employeeState.getAffiliationInforOfDailyPerfor().get(), //affiliationInfor
				Optional.empty(), //pcLogOnInfo
				new ArrayList<>(), //employeeError
				Optional.empty(), //outingTime
				new BreakTimeOfDailyAttd(), //breakTime
				Optional.empty(), //attendanceTimeOfDailyPerformance
				Optional.of(attendance),// attendanceLeave
				Optional.empty(), //shortTime
				Optional.empty(), //specDateAttr
				Optional.empty(), //attendanceLeavingGate
				Optional.empty(), //anyItemValue
				new ArrayList<>(), //editState
				Optional.empty(), //tempTime
				new ArrayList<>(),//remarks
				new ArrayList<>(),//ouenTime
				new ArrayList<>(),//ouenTimeSheet
				Optional.empty());
	}

	//???????????????Work???????????????????????????????????????
	private boolean setDataIntoDomain(IntegrationOfDaily data,
			TimeLeavingOfDailyAttd timeAttendance, WorkInfoOfDailyAttendance workInformation,
			CalAttrOfDailyPerformance calAttrOfDailyPerformance, AffiliationInforState employeeState,
			Optional<BreakTimeOfDailyAttd> breakTime, Optional<OutingTimeOfDailyAttd> outingTime,
			Optional<ShortTimeOfDailyAttd> shortTime) {
		
		if(breakTime.isPresent()) {
			data.setBreakTime(breakTime.get());
		}
		data.setOutingTime(outingTime);
		data.setShortTime(shortTime);
		data.setAttendanceLeave(Optional.of(timeAttendance));
		data.setCalAttr(calAttrOfDailyPerformance.getCalcategory());
		data.setAffiliationInfor(employeeState.getAffiliationInforOfDailyPerfor().get());
		if(workInformation.getRecordInfo().isSame(data.getWorkInformation().getRecordInfo())) {
			val scheduleTimeSheet = workInformation.getScheduleTimeSheets();
			data.getWorkInformation().setScheduleTimeSheets(scheduleTimeSheet);
			return false;
		}else {
			data.setWorkInformation(workInformation);
			return true;
		}
	}
	
	//??????????????????????????????
	private void setEditState(IntegrationOfDaily domain) {
		List<Integer> ITEM = AttendanceItemIdContainer.getItemIdByDailyDomains(DailyDomainGroup.ATTENDACE_LEAVE,
				DailyDomainGroup.BREAK_TIME, DailyDomainGroup.OUTING_TIME, DailyDomainGroup.SHORT_TIME);
		ITEM.addAll(Arrays.asList(28,29));
		List<EditStateOfDailyAttd> editState = ITEM.stream()
				.map(x -> new EditStateOfDailyAttd(x, EditStateSetting.HAND_CORRECTION_MYSELF))
				.collect(Collectors.toList());
		domain.setEditState(editState);
	}
	
	private IntegrationOfDaily replaceDeductionTimeSheet(IntegrationOfDaily provisionalRecord,
			List<BreakTimeSheet> breakTimeSheets, List<OutingTimeSheet> outingTimeSheets,
			List<ShortWorkingTimeSheet> shortWorkingTimeSheets, String employeeId, GeneralDate ymd) {

		provisionalRecord.setOutingTime(Optional.of(new OutingTimeOfDailyAttd(outingTimeSheets)));
		provisionalRecord.setBreakTime(new BreakTimeOfDailyAttd(breakTimeSheets));
		provisionalRecord.setShortTime(Optional.of(new ShortTimeOfDailyAttd(shortWorkingTimeSheets)));

		return provisionalRecord;
	}

	//??????????????????????????????????????????
}
