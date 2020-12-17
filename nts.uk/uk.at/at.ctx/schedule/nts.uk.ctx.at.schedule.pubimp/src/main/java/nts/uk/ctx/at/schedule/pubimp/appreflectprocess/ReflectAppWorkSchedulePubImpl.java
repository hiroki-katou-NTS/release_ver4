package nts.uk.ctx.at.schedule.pubimp.appreflectprocess;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.task.tran.AtomTask;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.adapter.appreflect.RequestSettingAdapter;
import nts.uk.ctx.at.schedule.dom.adapter.appreflect.SCAppReflectionSetting;
import nts.uk.ctx.at.schedule.dom.appreflectprocess.change.ReflectApplicationWorkSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkScheduleRepository;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.snapshot.DailySnapshotWork;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.snapshot.DailySnapshotWorkRepository;
import nts.uk.ctx.at.schedule.pub.appreflectprocess.ReflectApplicationWorkSchedulePub;
import nts.uk.ctx.at.shared.dom.adapter.application.reflect.SHAppReflectionSetting;
import nts.uk.ctx.at.shared.dom.adapter.application.reflect.SHApplyTimeSchedulePriority;
import nts.uk.ctx.at.shared.dom.adapter.application.reflect.SHClassifyScheAchieveAtr;
import nts.uk.ctx.at.shared.dom.adapter.application.reflect.SHPriorityTimeReflectAtr;
import nts.uk.ctx.at.shared.dom.application.common.ApplicationShare;
import nts.uk.ctx.at.shared.dom.application.common.ApplicationTypeShare;
import nts.uk.ctx.at.shared.dom.application.reflect.ReflectStatusResultShare;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.cancellation.ApplicationReflectHistory;
import nts.uk.ctx.at.shared.dom.application.reflectprocess.condition.businesstrip.ReflectBusinessTripApp;
import nts.uk.ctx.at.shared.dom.dailyprocess.calc.CalculateDailyRecordServiceCenterNew;
import nts.uk.ctx.at.shared.dom.dailyprocess.calc.CalculateOption;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.DailyRecordConverter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ChangeDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ICorrectionAttendanceRule;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.directgoback.GoBackReflect;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.directgoback.GoBackReflectRepository;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.lateearlycancellation.LateEarlyCancelReflect;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.lateearlycancellation.LateEarlyCancelReflectRepository;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.stampapplication.StampAppReflect;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.stampapplication.StampAppReflectRepository;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.workchangeapp.ReflectWorkChangeApp;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.workchangeapp.ReflectWorkChangeAppRepository;
import nts.uk.ctx.at.shared.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingService;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.internal.PredetermineTimeSetForCalc;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ReflectAppWorkSchedulePubImpl implements ReflectApplicationWorkSchedulePub {

	@Inject
	private WorkTypeRepository workTypeRepo;

	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;

	@Inject
	private BasicScheduleService service;

	@Inject
	private WorkTimeSettingService workTimeSettingService;

	@Inject
	private WorkScheduleRepository workScheduleRepository;

	@Inject
	private DailyRecordConverter convertDailyRecordToAd;

	@Inject
	private ICorrectionAttendanceRule correctionAttendanceRule;

	@Inject
	private CalculateDailyRecordServiceCenterNew calculateDailyRecordServiceCenterNew;

	@Inject
	private RequestSettingAdapter requestSettingAdapter;
	
	@Inject
	private DailySnapshotWorkRepository snapshotRepo;

	@Inject
	private FlexWorkSettingRepository flexWorkSettingRepository;

	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSettingRepository;

	@Inject
	private FixedWorkSettingRepository fixedWorkSettingRepository;

	@Inject
	private FlowWorkSettingRepository flowWorkSettingRepository;

	@Inject
	private GoBackReflectRepository goBackReflectRepository;

	@Inject
	private StampAppReflectRepository stampAppReflectRepository;

	@Inject
	private LateEarlyCancelReflectRepository lateEarlyCancelReflectRepository;

	@Inject
	private ReflectWorkChangeAppRepository reflectWorkChangeAppRepository;

	@Override
	public Pair<Object, AtomTask> process(int type, Object application, GeneralDate date, Object reflectStatus, int preAppWorkScheReflectAttr) {
		String companyId = AppContexts.user().companyId();
		
		RequireImpl impl = new RequireImpl(companyId, workTypeRepo, workTimeSettingRepository, service,
				workTimeSettingService, workScheduleRepository, convertDailyRecordToAd, correctionAttendanceRule,
				calculateDailyRecordServiceCenterNew, requestSettingAdapter, snapshotRepo, flexWorkSettingRepository,
				predetemineTimeSettingRepository, fixedWorkSettingRepository, flowWorkSettingRepository,
				goBackReflectRepository, stampAppReflectRepository, lateEarlyCancelReflectRepository,
				reflectWorkChangeAppRepository);
		Pair<ReflectStatusResultShare, AtomTask> result = ReflectApplicationWorkSchedule.process(impl, companyId,
				EnumAdaptor.valueOf(type, ExecutionType.class), (ApplicationShare) application, date,
				(ReflectStatusResultShare) reflectStatus, preAppWorkScheReflectAttr);
		return Pair.of(result.getLeft(), result.getRight());
	}

	@AllArgsConstructor
	public class RequireImpl implements ReflectApplicationWorkSchedule.Require {

		private final String companyId;

		private final WorkTypeRepository workTypeRepo;

		private final WorkTimeSettingRepository workTimeSettingRepository;

		private final BasicScheduleService service;

		private final WorkTimeSettingService workTimeSettingService;

		private final WorkScheduleRepository workScheduleRepository;

		private final DailyRecordConverter convertDailyRecordToAd;

		private final ICorrectionAttendanceRule correctionAttendanceRule;

		private final CalculateDailyRecordServiceCenterNew calculateDailyRecordServiceCenterNew;

		private final RequestSettingAdapter requestSettingAdapter;
		
		private DailySnapshotWorkRepository snapshotRepo;

		private final FlexWorkSettingRepository flexWorkSettingRepository;

		private final PredetemineTimeSettingRepository predetemineTimeSettingRepository;

		private final FixedWorkSettingRepository fixedWorkSettingRepository;

		private final FlowWorkSettingRepository flowWorkSettingRepository;

		private final GoBackReflectRepository goBackReflectRepository;

		private final StampAppReflectRepository stampAppReflectRepository;

		private final LateEarlyCancelReflectRepository lateEarlyCancelReflectRepository;

		private final ReflectWorkChangeAppRepository reflectWorkChangeAppRepository;

		@Override
		public Optional<WorkType> getWorkType(String workTypeCd) {
			return workTypeRepo.findByPK(companyId, workTypeCd);
		}

		@Override
		public Optional<WorkTimeSetting> getWorkTime(String workTimeCode) {
			return workTimeSettingRepository.findByCode(companyId, workTimeCode);
		}

		@Override
		public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
			return service.checkNeededOfWorkTimeSetting(workTypeCode);
		}

		@Override
		public PredetermineTimeSetForCalc getPredeterminedTimezone(String workTypeCd, String workTimeCd,
				Integer workNo) {
			return workTimeSettingService.getPredeterminedTimezone(companyId, workTimeCd, workTypeCd, workNo);
		}

		@Override
		public void insertAppReflectHist(ApplicationReflectHistory hist) {
			// TODO Auto-generated method stub

		}

		@Override
		public Optional<WorkSchedule> get(String employeeID, GeneralDate ymd) {
			return workScheduleRepository.get(employeeID, ymd);
		}

		@Override
		public DailyRecordToAttendanceItemConverter createDailyConverter() {
			return convertDailyRecordToAd.createDailyConverter();
		}

		@Override
		public IntegrationOfDaily process(IntegrationOfDaily domainDaily, ChangeDailyAttendance changeAtt) {
			return correctionAttendanceRule.process(domainDaily, changeAtt);
		}

		@Override
		public void insertSchedule(WorkSchedule workSchedule) {
			workScheduleRepository.update(workSchedule);
		}

		@Override
		public List<IntegrationOfDaily> calculateForSchedule(ExecutionType type, CalculateOption calcOption,
				List<IntegrationOfDaily> integrationOfDaily) {
			return calculateDailyRecordServiceCenterNew.calculatePassCompanySetting(calcOption, integrationOfDaily,
					type);
		}

		@Override
		public Optional<SCAppReflectionSetting> getAppReflectionSettingSc(String companyId,
				ApplicationTypeShare appType) {
			return requestSettingAdapter.getAppReflectionSetting(companyId, appType);
		}

		@Override
		public Optional<SHAppReflectionSetting> getAppReflectionSetting(String companyId,
				ApplicationTypeShare appType) {
			return requestSettingAdapter.getAppReflectionSetting(companyId, appType)
					.map(x -> new SHAppReflectionSetting(x.getScheReflectFlg(),
							EnumAdaptor.valueOf(x.getPriorityTimeReflectFlag().value, SHPriorityTimeReflectAtr.class),
							x.getAttendentTimeReflectFlg(),
							EnumAdaptor.valueOf(x.getClassScheAchi().value, SHClassifyScheAchieveAtr.class),
							EnumAdaptor.valueOf(x.getReflecTimeofSche().value, SHApplyTimeSchedulePriority.class)));
		}

		@Override
		public Optional<WorkType> findByPK(String companyId, String workTypeCd) {
			return workTypeRepo.findByPK(companyId, workTypeCd);
		}

		@Override
		public Optional<ReflectBusinessTripApp> findReflectBusinessTripApp(String companyId) {
			return Optional.of(new ReflectBusinessTripApp(companyId));
		}

		@Override
		public FixedWorkSetting getWorkSettingForFixedWork(WorkTimeCode code) {
			return fixedWorkSettingRepository.findByKey(companyId, code.v()).get();
		}

		@Override
		public FlowWorkSetting getWorkSettingForFlowWork(WorkTimeCode code) {
			return flowWorkSettingRepository.find(companyId, code.v()).get();
		}

		@Override
		public FlexWorkSetting getWorkSettingForFlexWork(WorkTimeCode code) {
			return flexWorkSettingRepository.find(companyId, code.v()).get();
		}

		@Override
		public Optional<DailySnapshotWork> snapshot(String sid, GeneralDate ymd) {
			
			return snapshotRepo.find(sid, ymd);
		}
		
		public PredetemineTimeSetting getPredetermineTimeSetting(WorkTimeCode wktmCd) {
			return predetemineTimeSettingRepository.findByWorkTimeCode(companyId, wktmCd.v()).get();
		}

		@Override
		public Optional<ReflectWorkChangeApp> findReflectWorkCg(String companyId) {
			return reflectWorkChangeAppRepository.findByCompanyIdReflect(companyId);
		}

		@Override
		public Optional<GoBackReflect> findReflectGoBack(String companyId) {
			return goBackReflectRepository.findByCompany(companyId);
		}

		@Override
		public Optional<StampAppReflect> findReflectAppStamp(String companyId) {
			return stampAppReflectRepository.findReflectByCompanyId(companyId);
		}

		@Override
		public Optional<LateEarlyCancelReflect> findReflectArrivedLateLeaveEarly(String companyId) {
			return Optional.ofNullable(lateEarlyCancelReflectRepository.getByCompanyId(companyId));
		}

	}
}
