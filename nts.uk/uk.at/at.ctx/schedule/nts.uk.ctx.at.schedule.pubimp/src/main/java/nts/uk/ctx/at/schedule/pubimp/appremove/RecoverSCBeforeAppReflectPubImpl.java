package nts.uk.ctx.at.schedule.pubimp.appremove;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.change.state.SCReasonNotReflect;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.change.state.SCReasonNotReflectDaily;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.change.state.SCReflectStatusResult;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.change.state.SCReflectedState;
import nts.uk.ctx.at.schedule.dom.appremove.RecoverWorkScheduleBeforeAppReflect;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkScheduleRepository;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.appremove.RecoverSCBeforeAppReflectPub;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.appremove.SCRecoverAppReflectExport;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.appremove.export.ReasonNotReflectDailyExport;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.appremove.export.ReasonNotReflectExport;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.appremove.export.ReflectStatusResultExport;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.appremove.export.ReflectedStateExport;
import nts.uk.ctx.at.shared.dom.scherec.application.common.ApplicationShare;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.ScheduleRecordClassifi;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.cancellation.ApplicationReflectHistory;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.cancellation.ApplicationReflectHistoryRepo;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.DailyRecordConverter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ChangeDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.aftercorrectatt.CorrectionAfterTimeChange;
import nts.uk.ctx.at.shared.dom.scherec.dailyprocess.calc.CalculateDailyRecordServiceCenterNew;
import nts.uk.ctx.at.shared.dom.scherec.dailyprocess.calc.CalculateOption;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * @author thanh_nx
 *
 */
@Stateless
public class RecoverSCBeforeAppReflectPubImpl implements RecoverSCBeforeAppReflectPub {

	@Inject
	private DailyRecordConverter dailyRecordConverter;

	@Inject
	private WorkingConditionRepository workingConditionRepository;

	@Inject
	private CorrectionAfterTimeChange correctionAfterTimeChange;

	@Inject
	private CalculateDailyRecordServiceCenterNew calculateDailyRecordServiceCenter;

	@Inject
	private WorkScheduleRepository workScheduleRepository;
	
	@Inject
	private ApplicationReflectHistoryRepo applicationReflectHistoryRepo;

	@Override
	public SCRecoverAppReflectExport process(Object application, GeneralDate date,
			ReflectStatusResultExport reflectStatus, NotUseAtr dbRegisterClassfi) {
		RequireImpl impl = new RequireImpl(dailyRecordConverter, workingConditionRepository, correctionAfterTimeChange,
				calculateDailyRecordServiceCenter, workScheduleRepository, applicationReflectHistoryRepo);
		val result = RecoverWorkScheduleBeforeAppReflect.process(impl, (ApplicationShare) application, date,
				convertToShare(reflectStatus), dbRegisterClassfi);
		return new SCRecoverAppReflectExport(convertToExport(result.getReflectStatus()), result.getSchedule(),
				result.getAtomTask());
	}

	private SCReflectStatusResult convertToShare(ReflectStatusResultExport reflectStatus) {

		return new SCReflectStatusResult(EnumAdaptor.valueOf(reflectStatus.getReflectStatus().value, SCReflectedState.class),
				EnumAdaptor.valueOf(reflectStatus.getReasonNotReflectWorkRecord().value, SCReasonNotReflectDaily.class),
						EnumAdaptor.valueOf(reflectStatus.getReasonNotReflectWorkSchedule().value, SCReasonNotReflect.class));
	}

	private ReflectStatusResultExport convertToExport(SCReflectStatusResult reflectStatus) {

		return new ReflectStatusResultExport(ReflectedStateExport.valueOf(reflectStatus.getReflectStatus().value),
				ReasonNotReflectDailyExport.valueOf(reflectStatus.getReasonNotReflectWorkRecord().value),
				ReasonNotReflectExport.valueOf(reflectStatus.getReasonNotReflectWorkSchedule().value));
	}

	@AllArgsConstructor
	public class RequireImpl implements RecoverWorkScheduleBeforeAppReflect.Require {

		private final DailyRecordConverter dailyRecordConverter;

		private final WorkingConditionRepository workingConditionRepository;

		private final CorrectionAfterTimeChange correctionAfterTimeChange;

		private final CalculateDailyRecordServiceCenterNew calculateDailyRecordServiceCenter;

		private final WorkScheduleRepository workScheduleRepository;

		private final ApplicationReflectHistoryRepo applicationReflectHistoryRepo;

		@Override
		public Optional<WorkingCondition> workingCondition(String companyId, String employeeId, GeneralDate baseDate) {
			return workingConditionRepository.getBySidAndStandardDate(employeeId, baseDate);
		}

		@Override
		public Optional<WorkingConditionItem> workingConditionItem(String historyId) {
			return workingConditionRepository.getWorkingConditionItem(historyId);
		}

		@Override
		public List<ApplicationReflectHistory> findAppReflectHistAfterMaxTime(String sid, GeneralDate baseDate,
				ScheduleRecordClassifi classification, boolean flgRemove, GeneralDateTime reflectionTime) {
			return applicationReflectHistoryRepo.findAppReflectHistAfterMaxTime(sid, baseDate, classification, flgRemove, reflectionTime);
		}

		@Override
		public DailyRecordToAttendanceItemConverter createDailyConverter() {
			return dailyRecordConverter.createDailyConverter();
		}

		@Override
		public List<ApplicationReflectHistory> findAppReflectHist(String sid, String appId, GeneralDate baseDate,
				ScheduleRecordClassifi classification, boolean flgRemove) {
			return applicationReflectHistoryRepo.findAppReflectHist(sid, appId, baseDate, classification, flgRemove);
		}

		@Override
		public List<ApplicationReflectHistory> findAppReflectHistDateCond(String sid, GeneralDate baseDate,
				ScheduleRecordClassifi classification, boolean flgRemove, GeneralDateTime reflectionTime) {
			return applicationReflectHistoryRepo.findAppReflectHistDateCond(sid, baseDate, classification, flgRemove, reflectionTime);
		}

		@Override
		public Optional<WorkSchedule> get(String employeeID, GeneralDate ymd) {
			return workScheduleRepository.get(employeeID, ymd);
		}

		@Override
		public List<IntegrationOfDaily> calculateForSchedule(ExecutionType type,
				List<IntegrationOfDaily> integrationOfDaily) {
			return calculateDailyRecordServiceCenter.calculatePassCompanySetting(CalculateOption.asDefault(),
					integrationOfDaily, type);
		}

		@Override
		public void insertSchedule(WorkSchedule workSchedule) {
			workScheduleRepository.delete(workSchedule.getEmployeeID(), workSchedule.getYmd());
			workScheduleRepository.insert(workSchedule);
		}

		@Override
		public void updateAppReflectHist(String sid, String appId, GeneralDate baseDate,
				ScheduleRecordClassifi classification, boolean flagRemove) {
			applicationReflectHistoryRepo.updateAppReflectHist(sid, appId, baseDate, classification, flagRemove);
		}

		@Override
		public Pair<ChangeDailyAttendance, IntegrationOfDaily> corectionAfterTimeChange(IntegrationOfDaily domainDaily,
				ChangeDailyAttendance changeAtt, Optional<WorkingConditionItem> workCondOpt) {
			return correctionAfterTimeChange.corection(domainDaily, changeAtt, workCondOpt);
		}

	}
}
