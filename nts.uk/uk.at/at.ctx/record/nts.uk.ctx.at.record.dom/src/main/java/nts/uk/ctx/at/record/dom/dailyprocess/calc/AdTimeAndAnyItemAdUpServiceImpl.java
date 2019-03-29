package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.actualworkinghours.AttendanceTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.actualworkinghours.repository.AttendanceTimeRepository;
import nts.uk.ctx.at.record.dom.attendanceitem.StoredProcdureProcess;
import nts.uk.ctx.at.record.dom.attendanceitem.StoredProcedureFactory;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.AttendanceLeavingGateOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.PCLogOnInfoOfDaily;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.AttendanceLeavingGateOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDaily;
import nts.uk.ctx.at.record.dom.daily.optionalitemtime.AnyItemValueOfDailyRepo;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.shr.com.context.AppContexts;

/**
 * 日別実績の勤怠時間と任意項目を同時更新し、ストアドを実行するためのサービス
 * @author keisuke_hoshina
 *
 */
@Stateless
public class AdTimeAndAnyItemAdUpServiceImpl implements AdTimeAndAnyItemAdUpService {

	/*日別実績の勤怠時間*/
	@Inject
	private AttendanceTimeRepository attendanceTimeRepository;
	
	/*任意項目リポジトリ*/
	@Inject
	private AnyItemValueOfDailyRepo anyItemValueOfDailyRepo;
	
	/*ストアド実行*/
	@Inject
	private StoredProcdureProcess storedProcedureProcess;
	/*日別実績の勤務情報*/
	@Inject
	private WorkInformationRepository workInfo;
	/*日別実績のPCログオンログオフ*/
	@Inject
	private PCLogOnInfoOfDailyRepo pcLogon;
	/*日別実績の入退門*/
	@Inject
	private AttendanceLeavingGateOfDailyRepo attendanceGate;
	/*日別実績の出退勤*/
	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeave;
	
	@Inject
	private StoredProcedureFactory dbStoredProcess;
	
	@Override
	public void addAndUpdate(String empId ,GeneralDate ymd,
							Optional<AttendanceTimeOfDailyPerformance> attendanceTime, Optional<AnyItemValueOfDaily> anyItem) {
		workInfo.find(empId, ymd).ifPresent(wi -> {
			Optional<PCLogOnInfoOfDaily> pc = pcLogon.find(empId, ymd);
			Optional<AttendanceLeavingGateOfDaily> al = attendanceGate.find(empId, ymd);
			Optional<TimeLeavingOfDailyPerformance> tl = timeLeave.findByKey(empId, ymd);
			wi.changeCalcState(CalculationState.Calculated);
			IntegrationOfDaily daily = new IntegrationOfDaily(wi, null, null, Optional.empty(), pc, new ArrayList<>(),
								Optional.empty(), new ArrayList<>(), attendanceTime, Optional.empty(), tl,
								Optional.empty(), Optional.empty(), al, anyItem, new ArrayList<>(), Optional.empty(), new ArrayList<>());
			
			addAndUpdate(daily);
		});
	}
	
	@Override
	public List<IntegrationOfDaily> addAndUpdate(List<IntegrationOfDaily> daily) {
		return addAndUpdate(daily, null);
	}
	
	@Override
	public List<IntegrationOfDaily> addAndUpdate(List<IntegrationOfDaily> daily, Map<WorkTypeCode, WorkType> workTypes) {
		
		return saveOnly(runStoredProcess(daily, workTypes));
	}
	
	@Override
	public IntegrationOfDaily addAndUpdate(IntegrationOfDaily daily) {
		return addAndUpdate(Arrays.asList(daily)).get(0);
	}

	@Override
	public List<IntegrationOfDaily> runStoredProcess(List<IntegrationOfDaily> daily) {
		return runStoredProcess(daily, new HashMap<>());
	}

	@Override
	public List<IntegrationOfDaily> saveOnly(List<IntegrationOfDaily> daily) {
		String comId = AppContexts.user().companyId();
		
		daily.stream().forEach(d -> {
			//勤怠時間更新
			d.getAttendanceTimeOfDailyPerformance().ifPresent(at -> {
				attendanceTimeRepository.update(at);
			});
			//任意項目更新
			d.getAnyItemValue().ifPresent(ai -> {
				anyItemValueOfDailyRepo.persistAndUpdate(ai);
			});
			
			dbStoredProcess.runStoredProcedure(comId, d.getAttendanceTimeOfDailyPerformance(), d.getWorkInformation());
		});
		return daily;
	}

	@Override
	public List<IntegrationOfDaily> runStoredProcess(List<IntegrationOfDaily> daily,
			Map<WorkTypeCode, WorkType> workTypes) {
		return storedProcedureProcess.dailyProcessing(daily, workTypes);
	}

}
