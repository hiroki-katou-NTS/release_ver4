package nts.uk.ctx.at.record.dom.daily;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import nts.uk.ctx.at.record.dom.dailyprocess.calc.AdTimeAndAnyItemAdUpService;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.IntegrationOfDaily;
import nts.uk.ctx.at.record.dom.editstate.EditStateOfDailyPerformance;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.record.dom.shorttimework.ShortTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.shorttimework.repo.ShortTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.worktime.TemporaryTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;

@Stateless
public class DailyRecordAdUpServiceImpl implements DailyRecordAdUpService {

	@Inject
	private WorkInformationRepository workInfoRepo;

	@Inject
	private AffiliationInforOfDailyPerforRepository affInfoRepo;

	@Inject
	private CalAttrOfDailyPerformanceRepository calAttrRepo;

	@Inject
	private WorkTypeOfDailyPerforRepository workTypeRepo;

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingRepo;

	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeRepo;

	@Inject
	private OutingTimeOfDailyPerformanceRepository outTimeRepo;

	@Inject
	private ShortTimeOfDailyPerformanceRepository shortTimeRepo;

	@Inject
	private TemporaryTimeOfDailyPerformanceRepository temporaryTimrRepo;

	@Inject
	private AttendanceLeavingGateOfDailyRepo attendanceLeavingGateRepo;

	@Inject
	private AttendanceTimeRepository attendanceTimeRepo;

	@Inject
	private SpecificDateAttrOfDailyPerforRepo specificDateAttrRepo;

	@Inject
	private EditStateOfDailyPerformanceRepository editStateRepo;

	@Inject
	private AnyItemValueOfDailyRepo anyItemRepo;

	@Inject
	private AttendanceTimeByWorkOfDailyRepository attendanceTimeWorkRepo;

	@Inject
	private PCLogOnInfoOfDailyRepo pcLogOnRepo;

	@Inject
	private RemarksOfDailyPerformRepo remarksOfDailyRepo;

	@Inject
	private AdTimeAndAnyItemAdUpService adTimeAndAnyItemAdUpService;
	
	@Inject
	private EmployeeDailyPerErrorRepository employeeErrorRepo;

	@Override
	public void adUpWorkInfo(WorkInfoOfDailyPerformance workInfo) {
		workInfoRepo.updateByKey(workInfo);
	}

	@Override
	public void adUpAffilicationInfo(AffiliationInforOfDailyPerfor affiliationInfor) {
		affInfoRepo.updateByKey(affiliationInfor);
	}

	@Override
	public void adUpCalAttr(CalAttrOfDailyPerformance calAttr) {
		calAttrRepo.update(calAttr);
	}

	@Override
	public void adUpWorkType(Optional<WorkTypeOfDailyPerformance> businessType) {
		if (!businessType.isPresent())
			return;
		workTypeRepo.update(businessType.get());
	}

	@Override
	public void adUpTimeLeaving(Optional<TimeLeavingOfDailyPerformance> attendanceLeave) {
		if (!attendanceLeave.isPresent())
			return;
		timeLeavingRepo.update(attendanceLeave.get());

	}

	@Override
	public void adUpBreakTime(List<BreakTimeOfDailyPerformance> breakTime) {
		breakTime.stream().forEach(domain -> {
			breakTimeRepo.update(domain);
		});

	}

	@Override
	public void adUpOutTime(Optional<OutingTimeOfDailyPerformance> outingTime) {
		if (!outingTime.isPresent())
			return;
		outTimeRepo.update(outingTime.get());
	}

	@Override
	public void adUpShortTime(Optional<ShortTimeOfDailyPerformance> shortTime) {
		if (!shortTime.isPresent())
			return;
		shortTimeRepo.updateByKey(shortTime.get());
	}

	@Override
	public void adUpTemporaryTime(Optional<TemporaryTimeOfDailyPerformance> tempTime) {
		if (!tempTime.isPresent())
			return;
		temporaryTimrRepo.update(tempTime.get());

	}

	@Override
	public void adUpAttendanceLeavingGate(Optional<AttendanceLeavingGateOfDaily> attendanceLeavingGate) {
		if (!attendanceLeavingGate.isPresent())
			return;
		attendanceLeavingGateRepo.update(attendanceLeavingGate.get());

	}

	@Override
	public void adUpAttendanceTime(Optional<AttendanceTimeOfDailyPerformance> attendanceTimeOfDailyPerformance) {
		if (!attendanceTimeOfDailyPerformance.isPresent())
			return;
		attendanceTimeRepo.update(attendanceTimeOfDailyPerformance.get());

	}

	@Override
	public void adUpSpecificDate(Optional<SpecificDateAttrOfDailyPerfor> specDateAttr) {
		if (!specDateAttr.isPresent())
			return;
		specificDateAttrRepo.update(specDateAttr.get());

	}

	@Override
	public void adUpEditState(List<EditStateOfDailyPerformance> editState) {
		if (editState.isEmpty())
			return;
		editState.stream().forEach(domain -> {
			editStateRepo.updateByKey(editState);
		});

	}

	@Override
	public void adUpAnyItem(Optional<AnyItemValueOfDaily> anyItemValue) {
		if (!anyItemValue.isPresent())
			return;
		anyItemRepo.update(anyItemValue.get());

	}

	@Override
	public void adUpAttendanceTimeByWork(Optional<AttendanceTimeByWorkOfDaily> attendancetimeByWork) {
		if (!attendancetimeByWork.isPresent())
			return;
		attendanceTimeWorkRepo.update(attendancetimeByWork.get());
	}

	@Override
	public void adUpPCLogOn(Optional<PCLogOnInfoOfDaily> pcLogOnInfo) {
		if (!pcLogOnInfo.isPresent())
			return;
		pcLogOnRepo.update(pcLogOnInfo.get());

	}

	@Override
	public void adUpRemark(List<RemarksOfDailyPerform> remarks) {
		if (remarks.isEmpty())
			return;
		remarks.stream().forEach(domain -> {
			remarksOfDailyRepo.update(domain);
		});
	}

	@Override
	public void adUpEmpError(List<EmployeeDailyPerError> errors, Map<String, List<GeneralDate>> mapEmpDateError, boolean hasRemoveError) {
		if (hasRemoveError) {
//			Map<String, List<GeneralDate>> mapError = errors.stream().collect(
//					Collectors.groupingBy(c -> c.getEmployeeID(), Collectors.collectingAndThen(Collectors.toList(),
//							c -> c.stream().map(q -> q.getDate()).collect(Collectors.toList()))));
			employeeErrorRepo.removeNotOTK(mapEmpDateError);
		}
		
		employeeErrorRepo.update(errors);

	}

	@Override
	public List<IntegrationOfDaily> adTimeAndAnyItemAdUp(List<IntegrationOfDaily> dailys) {
		return adTimeAndAnyItemAdUpService.saveOnly(dailys);
	}

}
