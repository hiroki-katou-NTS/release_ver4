package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleAdapter;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleSidDto;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.WorkBreakTimeImport;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.WorkScheduleSidImport;
import nts.uk.ctx.at.record.dom.adapter.businesscalendar.daycalendar.RecCalendarCompanyAdapter;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationAdapter;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationSidImport;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentAdapter;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentImport;
import nts.uk.ctx.at.record.dom.adapter.specificdatesetting.RecSpecificDateSettingAdapter;
import nts.uk.ctx.at.record.dom.adapter.specificdatesetting.RecSpecificDateSettingImport;
import nts.uk.ctx.at.record.dom.adapter.statusofemployee.RecStatusOfEmployeeAdapter;
import nts.uk.ctx.at.record.dom.adapter.statusofemployee.RecStatusOfEmployeeImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkPlaceSidImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceDto;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.affiliationinformation.primitivevalue.ClassificationCode;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalStatusOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeSheet;
import nts.uk.ctx.at.record.dom.breakorgoout.enums.BreakType;
import nts.uk.ctx.at.record.dom.breakorgoout.primitivevalue.BreakFrameNo;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalHolidaySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfLeaveEarlySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalOfOverTime;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalRaisingSalarySetting;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalcSetOfDivergenceTime;
import nts.uk.ctx.at.record.dom.calculationattribute.AutoCalculationSetting;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.DivergenceTimeAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.LeaveAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SalaryCalAttr;
import nts.uk.ctx.at.record.dom.calculationattribute.enums.SpecificSalaryCalAttr;
import nts.uk.ctx.at.record.dom.calculationsetting.StampReflectionManagement;
import nts.uk.ctx.at.record.dom.calculationsetting.enums.AutoStampForFutureDayClass;
import nts.uk.ctx.at.record.dom.calculationsetting.repository.StampReflectionManagementRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployee;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.BusinessTypeOfEmployeeHistory;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeEmpOfHistoryRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.businesstype.repository.BusinessTypeOfEmployeeRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.AffiliationInforState;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.AutomaticStampSetDetailOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ReflectStampOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.TimeActualStampOutPut;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.TimeLeavingWorkOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.WorkStampOutPut;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.jobtitle.affiliate.AffJobTitleAdapter;
import nts.uk.ctx.at.record.dom.jobtitle.affiliate.AffJobTitleSidImport;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrSheet;
import nts.uk.ctx.at.record.dom.raisesalarytime.enums.SpecificDateAttr;
import nts.uk.ctx.at.record.dom.raisesalarytime.primitivevalue.SpecificDateItemNo;
import nts.uk.ctx.at.record.dom.stamp.StampRepository;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkTimes;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.bonuspay.primitives.WorkingTimesheetCode;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPSettingRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.CPBonusPaySettingRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.PSBonusPaySettingRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.WPBonusPaySettingRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.WTBonusPaySettingRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.BonusPaySetting;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.CompanyBonusPaySetting;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.PersonalBonusPaySetting;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.WorkingTimesheetBonusPaySetting;
import nts.uk.ctx.at.shared.dom.bonuspay.setting.WorkplaceBonusPaySetting;
import nts.uk.ctx.at.shared.dom.common.WorkplaceId;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.configuration.DayOfWeek;
import nts.uk.ctx.at.shared.dom.ot.autocalsetting.BaseAutoCalSetting;
import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborConditionRepository;
import nts.uk.ctx.at.shared.dom.personallaborcondition.UseAtr;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
import nts.uk.ctx.at.shared.dom.workingcondition.NotUseAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.SingleDaySchedule;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workrule.overtime.AutoCalculationSetService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.getcommonset.GetCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkNo;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSet;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.TimeWithDayAttr;

@Stateless
public class ReflectWorkInforDomainServiceImpl implements ReflectWorkInforDomainService {

	@Inject
	private WorkInformationRepository workInformationRepository;

	@Inject
	private ApprovalStatusOfDailyPerforRepository approvalStatusOfDailyPerforRepository;

	@Inject
	private AffiliationInforOfDailyPerforRepository affiliationInforOfDailyPerforRepository;

	@Inject
	private IdentificationRepository identificationRepository;

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;

	@Inject
	private TemporaryTimeOfDailyPerformanceRepository temporaryTimeOfDailyPerformanceRepository;

	@Inject
	private EditStateOfDailyPerformanceRepository editStateOfDailyPerformanceRepository;

	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDailyPerformanceRepository;

	@Inject
	private OutingTimeOfDailyPerformanceRepository outingTimeOfDailyPerformanceRepository;

	@Inject
	private ErrMessageInfoRepository errMessageInfoRepository;

	@Inject
	private AffWorkplaceAdapter affWorkplaceAdapter;

	@Inject
	private AffClassificationAdapter affClassificationAdapter;

	@Inject
	private SyEmploymentAdapter syEmploymentAdapter;

	@Inject
	private AffJobTitleAdapter affJobTitleAdapter;

	@Inject
	private PersonalLaborConditionRepository personalLaborConditionRepository;

	@Inject
	private BasicScheduleAdapter basicScheduleAdapter;

	@Inject
	private WorkTypeRepository workTypeRepository;

	@Inject
	private RecCalendarCompanyAdapter calendarCompanyAdapter;

	@Inject
	private BasicScheduleService basicScheduleService;

	@Inject
	private StampReflectionManagementRepository stampReflectionManagementRepository;

	@Inject
	private ReflectStampDomainService reflectStampDomainServiceImpl;

	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSettingRepository;

	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;

	@Inject
	private StampRepository stampRepository;

	@Inject
	private BusinessTypeEmpOfHistoryRepository businessTypeEmpOfHistoryRepository;

	@Inject
	private BusinessTypeOfEmployeeRepository businessTypeOfEmployeeRepository;

	@Inject
	private RecSpecificDateSettingAdapter recSpecificDateSettingAdapter;

	@Inject
	private BPSettingRepository bPSettingRepository;

	@Inject
	private WTBonusPaySettingRepository wTBonusPaySettingRepository;

	@Inject
	private PSBonusPaySettingRepository pSBonusPaySettingRepository;

	@Inject
	private WPBonusPaySettingRepository wPBonusPaySettingRepository;

	@Inject
	private CPBonusPaySettingRepository cPBonusPaySettingRepository;

	@Inject
	private AutoCalculationSetService autoCalculationSetService;

	@Inject
	private GetCommonSet getCommonSet;

	@Inject
	private RecStatusOfEmployeeAdapter recStatusOfEmployeeAdapter;

	@Inject
	private RegisterDailyPerformanceInfoService registerDailyPerformanceInfoService;

	@Inject
	private WorkTypeOfDailyPerforRepository workTypeOfDailyPerforRepository;

	@Override
	public void reflectWorkInformation(String companyId, String employeeId, GeneralDate day,
			String empCalAndSumExecLogID, ExecutionType reCreateAttr, boolean reCreateWorkType) {

		// // pharse 2
		// // start --
		// // ドメインモデル「日別実績の勤務情報」を削除する - rerun
		// if (reCreateAttr == ExecutionType.RERUN) {
		// this.deleteDailyResult(employeeId, day);
		//
		// this.reflect(companyId, employeeId, day, empCalAndSumExecLogID,
		// reCreateAttr);
		// } else {
		// // ドメインモデル「日別実績の勤務情報」を取得する - not rerun
		// if (!this.workInformationRepository.find(employeeId,
		// day).isPresent()) {
		//
		// this.reflect(companyId, employeeId, day, empCalAndSumExecLogID,
		// reCreateAttr);
		// } else {
		// // 勤務種別変更時に再作成する
		// ExitStatus exitStatus = this.reCreateWorkType(employeeId, day,
		// empCalAndSumExecLogID, reCreateWorkType);
		//
		// if (exitStatus == ExitStatus.RECREATE) {
		// this.deleteDailyResult(employeeId, day);
		//
		// this.reflect(companyId, employeeId, day, empCalAndSumExecLogID,
		// reCreateAttr);
		// }
		// }
		// }
		// // end --

		if (reCreateAttr == ExecutionType.RERUN) {
			this.deleteDailyResult(employeeId, day);

			this.reflect(companyId, employeeId, day, empCalAndSumExecLogID, reCreateAttr);
		}

		if (!this.workInformationRepository.find(employeeId, day).isPresent()) {
			this.reflect(companyId, employeeId, day, empCalAndSumExecLogID, reCreateAttr);
		}
	}

	private void deleteDailyResult(String employeeId, GeneralDate day) {
		this.workInformationRepository.delete(employeeId, day);
		// this.approvalStatusOfDailyPerforRepository.delete(employeeId, day);
		this.affiliationInforOfDailyPerforRepository.delete(employeeId, day);
		// this.identificationRepository.delete(employeeId, day);
		this.timeLeavingOfDailyPerformanceRepository.delete(employeeId, day);
		this.temporaryTimeOfDailyPerformanceRepository.delete(employeeId, day);
		// this.editStateOfDailyPerformanceRepository.delete(employeeId,
		// day);
		// this.breakTimeOfDailyPerformanceRepository.delete(employeeId,
		// day);
		// this.outingTimeOfDailyPerformanceRepository.delete(employeeId,
		// day);
	}

	private void reflect(String companyId, String employeeId, GeneralDate day, String empCalAndSumExecLogID,
			ExecutionType reCreateAttr) {
		// 勤務種別を反映する
		WorkTypeOfDailyPerformance workTypeOfDailyPerformance = reflectWorkType(employeeId, day, empCalAndSumExecLogID);

		if (workTypeOfDailyPerformance != null) {

			val affiliationInforOfDailyPerforState = createAffiliationInforOfDailyPerfor(companyId, employeeId, day,
					empCalAndSumExecLogID);

			if (affiliationInforOfDailyPerforState.getErrMesInfos().isEmpty()) {
				// Imported(就業.勤務実績)「社員の勤務予定管理」を取得する
				this.workschedule(companyId, employeeId, day, empCalAndSumExecLogID,
						affiliationInforOfDailyPerforState.getAffiliationInforOfDailyPerfor().get(), reCreateAttr,
						workTypeOfDailyPerformance);
			} else {
				affiliationInforOfDailyPerforState.getErrMesInfos().forEach(action -> {
					this.errMessageInfoRepository.add(action);
				});
			}
		}
	}

	public ExitStatus reCreateWorkType(String employeeId, GeneralDate day, String empCalAndSumExecLogID,
			boolean reCreateWorkType) {
		ExitStatus exitStatus = ExitStatus.DO_NOT_RECREATE;

		if (reCreateWorkType == true) {
			// 社員の勤務種別を取得する
			// 対応するドメインモデル「社員の勤務種別の履歴」を取得する
			Optional<BusinessTypeOfEmployeeHistory> businessTypeOfEmployeeHistory = this.businessTypeEmpOfHistoryRepository
					.findByBaseDate(day, employeeId);

			if (businessTypeOfEmployeeHistory.isPresent()) {
				String historyId = businessTypeOfEmployeeHistory.get().getHistory().get(0).identifier();
				Optional<BusinessTypeOfEmployee> businessTypeOfEmployee = this.businessTypeOfEmployeeRepository
						.findByHistoryId(historyId);
				
				if (!businessTypeOfEmployee.isPresent()) {
					List<ErrMessageInfo> errMesInfos = new ArrayList<>();
					ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
							new ErrMessageResource("013"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
							new ErrMessageContent(TextResource.localize("Msg_1010")));
					errMesInfos.add(employmentErrMes);
					this.errMessageInfoRepository.addList(errMesInfos);
					return exitStatus;
				} else {
					Optional<WorkTypeOfDailyPerformance> workTypeOfDailyPerformance = this.workTypeOfDailyPerforRepository
							.findByKey(employeeId, day);
					if (!workTypeOfDailyPerformance.isPresent()) {
						return exitStatus;
					} else {
						if (businessTypeOfEmployee.get().getBusinessTypeCode()
								.equals(workTypeOfDailyPerformance.get().getWorkTypeCode())) {
							exitStatus = ExitStatus.RECREATE;
							return exitStatus;
						} else {
							return exitStatus;
						}
					}
				}
			} else {
				List<ErrMessageInfo> errMesInfos = new ArrayList<>();
				ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
						new ErrMessageResource("013"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
						new ErrMessageContent(TextResource.localize("Msg_1010")));
				errMesInfos.add(employmentErrMes);
				this.errMessageInfoRepository.addList(errMesInfos);
				return exitStatus;
			}
		}

		return exitStatus;
	}

	/**
	 * 
	 * @param employeeId
	 * @param day
	 * @return WorkTypeOfDailyPerformance
	 */
	@Override
	public WorkTypeOfDailyPerformance reflectWorkType(String employeeId, GeneralDate day,
			String empCalAndSumExecLogID) {

		List<ErrMessageInfo> errMesInfos = new ArrayList<>();

		WorkTypeOfDailyPerformance workTypeOfDailyPerformance = new WorkTypeOfDailyPerformance();

		// 対応するドメインモデル「社員の勤務種別の履歴」を取得する
		Optional<BusinessTypeOfEmployeeHistory> businessTypeOfEmployeeHistory = this.businessTypeEmpOfHistoryRepository
				.findByBaseDate(day, employeeId);

		if (businessTypeOfEmployeeHistory.isPresent()) {
			String historyId = businessTypeOfEmployeeHistory.get().getHistory().get(0).identifier();
			Optional<BusinessTypeOfEmployee> businessTypeOfEmployee = this.businessTypeOfEmployeeRepository
					.findByHistoryId(historyId);
			if (businessTypeOfEmployee.isPresent()) {
				workTypeOfDailyPerformance = new WorkTypeOfDailyPerformance(employeeId, day,
						businessTypeOfEmployee.get().getBusinessTypeCode().v());
				return workTypeOfDailyPerformance;
			} else {
				ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
						new ErrMessageResource("011"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
						new ErrMessageContent(TextResource.localize("Msg_1011")));
				errMesInfos.add(employmentErrMes);
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Importクラスを取得し日別実績の所属情報を作成する
	 * 
	 * @param companyId
	 *            the companyId
	 * @param employeeId
	 *            the employeeId
	 * @param day
	 *            the day
	 * @param empCalAndSumExecLogID
	 * @return AffiliationInforState(nts.uk.ctx.at.record.dom.dailyperformanceprocessing)
	 */
	@Override
	public AffiliationInforState createAffiliationInforOfDailyPerfor(String companyId, String employeeId,
			GeneralDate day, String empCalAndSumExecLogID) {

		// Imported(就業．勤務実績)「所属雇用履歴」を取得する
		Optional<SyEmploymentImport> employmentHasData = this.syEmploymentAdapter.findByEmployeeId(companyId,
				employeeId, day);

		// Imported(就業．勤務実績)「所属職場履歴」を取得する
		Optional<AffWorkPlaceSidImport> workPlaceHasData = this.affWorkplaceAdapter.findBySidAndDate(employeeId, day);

		// Imported(就業．勤務実績)「所属分類履歴」を取得する
		Optional<AffClassificationSidImport> classificationHasData = this.affClassificationAdapter
				.findByEmployeeId(companyId, employeeId, day);

		// Imported(就業．勤務実績)「所属職位履歴」を取得する
		Optional<AffJobTitleSidImport> jobTitleHasData = this.affJobTitleAdapter.findByEmployeeId(employeeId, day);
		// Get Data
		List<ErrMessageInfo> errMesInfos = new ArrayList<>();
		// 存在しない - no data
		if (!employmentHasData.isPresent()) {
			ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
					new ErrMessageResource("001"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
					new ErrMessageContent(TextResource.localize("Msg_426")));
			errMesInfos.add(employmentErrMes);
		}
		if (!workPlaceHasData.isPresent()) {
			ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
					new ErrMessageResource("002"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
					new ErrMessageContent(TextResource.localize("Msg_427")));
			errMesInfos.add(employmentErrMes);
		}
		if (!classificationHasData.isPresent()) {
			ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
					new ErrMessageResource("003"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
					new ErrMessageContent(TextResource.localize("Msg_428")));
			errMesInfos.add(employmentErrMes);
		}
		if (!jobTitleHasData.isPresent()) {
			ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeId, empCalAndSumExecLogID,
					new ErrMessageResource("004"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
					new ErrMessageContent(TextResource.localize("Msg_429")));
			errMesInfos.add(employmentErrMes);
		}
		// 存在する - has data
		if (employmentHasData.isPresent() && workPlaceHasData.isPresent() && classificationHasData.isPresent()
				&& jobTitleHasData.isPresent()) {
			return new AffiliationInforState(Collections.emptyList(),
					Optional.of(new AffiliationInforOfDailyPerfor(
							new EmploymentCode(employmentHasData.get().getEmploymentCode()), employeeId,
							jobTitleHasData.get().getJobTitleId(), workPlaceHasData.get().getWorkplaceId(), day,
							new ClassificationCode(classificationHasData.get().getClassificationCode()), null)));
		} else {
			return new AffiliationInforState(errMesInfos, Optional.empty());
		}
	}

	private void workschedule(String companyId, String employeeID, GeneralDate day, String empCalAndSumExecLogID,
			AffiliationInforOfDailyPerfor affiliationInforOfDailyPerfor, ExecutionType reCreateAttr,
			WorkTypeOfDailyPerformance workTypeOfDailyPerformance) {

		// status
		// 正常終了 : 0
		// 中断 : 1

		List<ErrMessageInfo> errMesInfos = new ArrayList<>();

		// result of stamp part
		ReflectStampOutput stampOutput = new ReflectStampOutput();

		WorkInfoOfDailyPerformance workInfoOfDailyPerformanceUpdate = new WorkInfoOfDailyPerformance();

		// 日別実績の出退勤
		TimeLeavingOfDailyPerformance timeLeavingOptional = new TimeLeavingOfDailyPerformance();

		// 日別実績の休憩時間帯
		Optional<BreakTimeOfDailyPerformance> breakTimeOfDailyPerformance = Optional.empty();

		// Optional<PersonalLaborCondition> personalLaborHasData =
		// this.personalLaborConditionRepository
		// .findById(employeeID, day);

		// ドメインモデル「労働条件項目．予定管理区分」を取得する
		Optional<WorkingConditionItem> workingConditionItem = this.workingConditionItemRepository
				.getBySidAndStandardDate(employeeID, day);

		if (!workingConditionItem.isPresent()) {
			ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeID, empCalAndSumExecLogID,
					new ErrMessageResource("005"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
					new ErrMessageContent(TextResource.localize("Msg_430")));
			errMesInfos.add(employmentErrMes);
			this.errMessageInfoRepository.addList(errMesInfos);
		} else {

			workInfoOfDailyPerformanceUpdate.setEmployeeId(employeeID);
			workInfoOfDailyPerformanceUpdate.setCalculationState(CalculationState.No_Calculated);
			workInfoOfDailyPerformanceUpdate.setYmd(day);
			workInfoOfDailyPerformanceUpdate.setDayOfWeek(EnumAdaptor.valueOf(day.dayOfWeek(), DayOfWeek.class));

			if (workingConditionItem.get().getScheduleManagementAtr() == NotUseAtr.USE) {

				// Imported(就業.勤務実績)「勤務予定基本情報」を取得する
				Optional<BasicScheduleSidDto> basicScheduleHasData = this.basicScheduleAdapter
						.findAllBasicSchedule(employeeID, day);
				// 勤務予定から勤務種類と就業時間帯を写す
				// 取得したImported(就業.勤務実績)「勤務予定基本情報」が存在するか確認する
				// 存在しない - no data
				if (!basicScheduleHasData.isPresent()) {
					ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeID, empCalAndSumExecLogID,
							new ErrMessageResource("006"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
							new ErrMessageContent(TextResource.localize(TextResource.localize("Msg_431"))));
					errMesInfos.add(employmentErrMes);

					this.errMessageInfoRepository.addList(errMesInfos);
				}
				// 存在する - has data
				else {
					workInfoOfDailyPerformanceUpdate.setScheduleWorkInformation(
							new WorkInformation(basicScheduleHasData.get().getWorkTimeCode(),
									basicScheduleHasData.get().getWorkTypeCode()));
					workInfoOfDailyPerformanceUpdate
							.setRecordWorkInformation(new WorkInformation(basicScheduleHasData.get().getWorkTimeCode(),
									basicScheduleHasData.get().getWorkTypeCode()));

					// 1日半日出勤・1日休日系の判定
					WorkStyle workStyle = basicScheduleService.checkWorkDay(
							workInfoOfDailyPerformanceUpdate.getRecordWorkInformation().getWorkTypeCode().v());

					if (workStyle != WorkStyle.ONE_DAY_REST) {

						// Imported(就業.勤務実績)「勤務予定時間帯」を取得する
						List<WorkScheduleSidImport> workScheduleHasData = basicScheduleHasData.get()
								.getWorkScheduleSidImports();
						// 存在しない - no data
						if (workScheduleHasData.isEmpty()) {
							ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeID, empCalAndSumExecLogID,
									new ErrMessageResource("007"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
									new ErrMessageContent(TextResource.localize("Msg_432")));
							errMesInfos.add(employmentErrMes);

							this.errMessageInfoRepository.addList(errMesInfos);
						} else {
							// copy information for employeeId has data
							List<ScheduleTimeSheet> scheduleTimeSheets = new ArrayList<>();
							workScheduleHasData.forEach(items -> {

								if (items.getBounceAtr() == 3) {
									workInfoOfDailyPerformanceUpdate.setBackStraightAtr(NotUseAttribute.Not_use);
									workInfoOfDailyPerformanceUpdate.setGoStraightAtr(NotUseAttribute.Not_use);
								} else if (items.getBounceAtr() == 2) {
									workInfoOfDailyPerformanceUpdate.setBackStraightAtr(NotUseAttribute.Not_use);
									workInfoOfDailyPerformanceUpdate.setGoStraightAtr(NotUseAttribute.Use);
								} else if (items.getBounceAtr() == 0) {
									workInfoOfDailyPerformanceUpdate.setBackStraightAtr(NotUseAttribute.Use);
									workInfoOfDailyPerformanceUpdate.setGoStraightAtr(NotUseAttribute.Not_use);
								} else if (items.getBounceAtr() == 1) {
									workInfoOfDailyPerformanceUpdate.setBackStraightAtr(NotUseAttribute.Use);
									workInfoOfDailyPerformanceUpdate.setGoStraightAtr(NotUseAttribute.Use);
								}

								ScheduleTimeSheet scheduleTimeSheet = new ScheduleTimeSheet(items.getScheduleCnt(),
										items.getScheduleStartClock(), items.getScheduleEndClock());
								scheduleTimeSheets.add(scheduleTimeSheet);
							});

							workInfoOfDailyPerformanceUpdate.setScheduleTimeSheets(scheduleTimeSheets);

							// 勤務予定の休憩時間帯を日別実績に写す
							breakTimeOfDailyPerformance = this.breakTimeOfDailyPerformanceRepository.find(employeeID,
									day, 1);

							// Imported「勤務予定休憩時間帯」を取得する
							List<WorkBreakTimeImport> workBreakTimeImportList = this.basicScheduleAdapter
									.findWorkBreakTime(employeeID, day);

							if (workBreakTimeImportList != null && !workBreakTimeImportList.isEmpty()) {
								List<BreakTimeSheet> breakTimeSheets = workBreakTimeImportList.stream().map(item -> {
									BreakTimeSheet breakTimeSheet = new BreakTimeSheet(
											new BreakFrameNo(item.getScheduleBreakCnt()), item.getScheduledStartClock(),
											item.getScheduledEndClock(), new AttendanceTime(0));
									return breakTimeSheet;
								}).collect(Collectors.toList());

								BreakTimeOfDailyPerformance breakTimeOfDailyPerformanceUpdate = new BreakTimeOfDailyPerformance(
										employeeID, EnumAdaptor.valueOf(1, BreakType.class), breakTimeSheets, day);
								breakTimeOfDailyPerformance = Optional.of(breakTimeOfDailyPerformanceUpdate);
							}
						}
					} else {
						workInfoOfDailyPerformanceUpdate.setBackStraightAtr(NotUseAttribute.Not_use);
						workInfoOfDailyPerformanceUpdate.setGoStraightAtr(NotUseAttribute.Not_use);
						workInfoOfDailyPerformanceUpdate.setScheduleTimeSheets(null);
					}
				}

			} else {
				ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeID, empCalAndSumExecLogID,
						new ErrMessageResource("012"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
						new ErrMessageContent(TextResource.localize("Msg_1120")));
				errMesInfos.add(employmentErrMes);

				this.errMessageInfoRepository.addList(errMesInfos);
				// // 個人情報から勤務種類と就業時間帯を写す
				// // 個人情報に処理中の曜日の設定が存在するか確認する
				// WorkInformation recordWorkInformation = null;
				// WorkingConditionItem workCondition =
				// workingConditionItem.get();
				// if
				// (this.workingConditionItemRepository.getBySidAndStandardDate(employeeID,
				// day).isPresent()) {
				// // monday
				// if (day.dayOfWeek() == 1) {
				// recordWorkInformation =
				// getWorkInfo(workCondition.getWorkDayOfWeek().getMonday(),
				// workCondition.getWorkCategory().getWeekdayTime());
				// }
				// // tuesday
				// else if (day.dayOfWeek() == 2) {
				// recordWorkInformation =
				// getWorkInfo(workCondition.getWorkDayOfWeek().getTuesday(),
				// workCondition.getWorkCategory().getWeekdayTime());
				// }
				// // wednesday
				// else if (day.dayOfWeek() == 3) {
				// recordWorkInformation =
				// getWorkInfo(workCondition.getWorkDayOfWeek().getWednesday(),
				// workCondition.getWorkCategory().getWeekdayTime());
				//
				// }
				// // thursday
				// else if (day.dayOfWeek() == 4) {
				// recordWorkInformation =
				// getWorkInfo(workCondition.getWorkDayOfWeek().getThursday(),
				// workCondition.getWorkCategory().getWeekdayTime());
				// }
				// // friday
				// else if (day.dayOfWeek() == 5) {
				// recordWorkInformation =
				// getWorkInfo(workCondition.getWorkDayOfWeek().getFriday(),
				// workCondition.getWorkCategory().getWeekdayTime());
				// }
				// // saturday
				// else if (day.dayOfWeek() == 6) {
				// recordWorkInformation =
				// getWorkInfo(workCondition.getWorkDayOfWeek().getSaturday(),
				// workCondition.getWorkCategory().getWeekdayTime());
				// }
				// // sunday
				// else if (day.dayOfWeek() == 7) {
				// recordWorkInformation =
				// getWorkInfo(workCondition.getWorkDayOfWeek().getSunday(),
				// workCondition.getWorkCategory().getWeekdayTime());
				// }
				// }
				//
				// workInfoOfDailyPerformanceUpdate.setRecordWorkInformation(recordWorkInformation);
				//
				// // 直行直帰区分を写す - autoStampSetAtr of PersonalLaborCondition
				// // 自動打刻セット区分を判断
				// if (workingConditionItem.get().getAutoStampSetAtr() ==
				// NotUseAtr.NOTUSE) {
				// String workTypeCode =
				// workInfoOfDailyPerformanceUpdate.getRecordWorkInformation().getWorkTypeCode()
				// .v();
				// Optional<WorkType> workType =
				// this.workTypeRepository.findByPK(companyId, workTypeCode);
				// if (workType.isPresent()) {
				// // 打刻の扱い方に従って、直行区分、直帰区分を更新
				// if
				// (workType.get().getWorkTypeSetList().get(0).getAttendanceTime()
				// == WorkTypeSetCheck.CHECK) {
				// workInfoOfDailyPerformanceUpdate
				// .setGoStraightAtr(EnumAdaptor.valueOf(1,
				// NotUseAttribute.class));
				// } else if (workType.get().getWorkTypeSetList().get(0)
				// .getAttendanceTime() == WorkTypeSetCheck.NO_CHECK) {
				// workInfoOfDailyPerformanceUpdate
				// .setGoStraightAtr(EnumAdaptor.valueOf(0,
				// NotUseAttribute.class));
				// }
				// if
				// (workType.get().getWorkTypeSetList().get(0).getTimeLeaveWork()
				// == WorkTypeSetCheck.CHECK) {
				// workInfoOfDailyPerformanceUpdate
				// .setBackStraightAtr(EnumAdaptor.valueOf(1,
				// NotUseAttribute.class));
				// } else if (workType.get().getWorkTypeSetList().get(0)
				// .getTimeLeaveWork() == WorkTypeSetCheck.NO_CHECK) {
				// workInfoOfDailyPerformanceUpdate
				// .setBackStraightAtr(EnumAdaptor.valueOf(0,
				// NotUseAttribute.class));
				// }
				// } else {
				// throw new RuntimeException("worktype hasn't data");
				// }
				// } else {
				// workInfoOfDailyPerformanceUpdate.setGoStraightAtr(EnumAdaptor.valueOf(1,
				// NotUseAttribute.class));
				// workInfoOfDailyPerformanceUpdate.setBackStraightAtr(EnumAdaptor.valueOf(1,
				// NotUseAttribute.class));
				// }
				//
				// // カレンダー情報を取得する
				// // a part of Du's team
				// CalendarInfoImport calendarInfoDto =
				// calendarCompanyAdapter.findCalendarCompany(companyId,
				// affiliationInforOfDailyPerfor.getWplID(),
				// affiliationInforOfDailyPerfor.getClsCode().v(), day);
				// WorkInformation scheduleWorkInformation = new
				// WorkInformation(calendarInfoDto.getWorkTimeCode(),
				// calendarInfoDto.getWorkTypeCode());
				// workInfoOfDailyPerformanceUpdate.setScheduleWorkInformation(scheduleWorkInformation);
				// //
				// workInfoOfDailyPerformanceUpdate.setRecordWorkInformation(scheduleWorkInformation);
				//
				// // 所定時間帯を取得する
				// Optional<PredetemineTimeSetting> predetemineTimeSetting =
				// predetemineTimeSettingRepository
				// .findByWorkTimeCode(companyId,
				// calendarInfoDto.getWorkTypeCode());
				//
				// if (predetemineTimeSetting.isPresent()) {
				// List<TimezoneUse> lstTimezone =
				// predetemineTimeSetting.get().getPrescribedTimezoneSetting()
				// .getLstTimezone();
				// List<ScheduleTimeSheet> scheduleTimeSheets = new
				// ArrayList<>();
				// for (TimezoneUse timezone : lstTimezone) {
				// if (timezone.getUseAtr() == UseSetting.USE) {
				// ScheduleTimeSheet scheduleTimeSheet = new
				// ScheduleTimeSheet(timezone.getWorkNo(),
				// timezone.getStart().v(), timezone.getEnd().v());
				// scheduleTimeSheets.add(scheduleTimeSheet);
				// }
				// }
				// workInfoOfDailyPerformanceUpdate.setScheduleTimeSheets(scheduleTimeSheets);
				// } else {
				// throw new RuntimeException("PredetemineTimeSetting has not
				// data");
				// }
			}

			if (errMesInfos.isEmpty()) {

				// pharse 2 start ----
				// 特定日を日別実績に反映する
				SpecificDateAttrOfDailyPerfor specificDateAttrOfDailyPerfor = reflectSpecificDate(companyId, employeeID,
						day, affiliationInforOfDailyPerfor.getWplID());

				// 加給設定を日別実績に反映する
				Optional<BonusPaySetting> bonusPaySetting = this.reflectBonusSetting(companyId, employeeID, day, null);
				if (bonusPaySetting.isPresent()) {
					affiliationInforOfDailyPerfor = new AffiliationInforOfDailyPerfor(
							affiliationInforOfDailyPerfor.getEmploymentCode(),
							affiliationInforOfDailyPerfor.getEmployeeId(),
							affiliationInforOfDailyPerfor.getJobTitleID(), affiliationInforOfDailyPerfor.getWplID(),
							affiliationInforOfDailyPerfor.getYmd(), affiliationInforOfDailyPerfor.getClsCode(),
							bonusPaySetting.get().getCode());
				}

				// 計算区分を日別実績に反映する
				// 自動計算設定の取得(get auto calculation setting)
				BaseAutoCalSetting baseAutoCalSetting = this.autoCalculationSetService
						.getAutoCalculationSetting(companyId, employeeID, day);

				AutoCalculationSetting flexExcessTime = new AutoCalculationSetting(
						baseAutoCalSetting.getFlexOTTime().getFlexOtTime().getCalAtr(),
						baseAutoCalSetting.getFlexOTTime().getFlexOtTime().getUpLimitORtSet());

				AutoCalRaisingSalarySetting autoCalRaisingSalarySetting = new AutoCalRaisingSalarySetting(
						SalaryCalAttr.NOT_USE, SpecificSalaryCalAttr.NOT_USE);

				// number 3
				AutoCalHolidaySetting holidayTimeSetting = new AutoCalHolidaySetting(
						new AutoCalculationSetting(baseAutoCalSetting.getRestTime().getRestTime().getCalAtr(),
								baseAutoCalSetting.getRestTime().getRestTime().getUpLimitORtSet()),
						new AutoCalculationSetting(baseAutoCalSetting.getRestTime().getLateNightTime().getCalAtr(),
								baseAutoCalSetting.getRestTime().getLateNightTime().getUpLimitORtSet()));

				// number 4
				AutoCalOfOverTime overtimeSetting = new AutoCalOfOverTime(
						new AutoCalculationSetting(baseAutoCalSetting.getNormalOTTime().getEarlyOtTime().getCalAtr(),
								baseAutoCalSetting.getNormalOTTime().getEarlyOtTime().getUpLimitORtSet()),
						new AutoCalculationSetting(baseAutoCalSetting.getNormalOTTime().getEarlyMidOtTime().getCalAtr(),
								baseAutoCalSetting.getNormalOTTime().getEarlyMidOtTime().getUpLimitORtSet()),
						new AutoCalculationSetting(baseAutoCalSetting.getNormalOTTime().getNormalOtTime().getCalAtr(),
								baseAutoCalSetting.getNormalOTTime().getNormalOtTime().getUpLimitORtSet()),
						new AutoCalculationSetting(
								baseAutoCalSetting.getNormalOTTime().getNormalMidOtTime().getCalAtr(),
								baseAutoCalSetting.getNormalOTTime().getNormalMidOtTime().getUpLimitORtSet()),
						new AutoCalculationSetting(baseAutoCalSetting.getNormalOTTime().getLegalOtTime().getCalAtr(),
								baseAutoCalSetting.getNormalOTTime().getLegalOtTime().getUpLimitORtSet()),
						new AutoCalculationSetting(baseAutoCalSetting.getNormalOTTime().getLegalMidOtTime().getCalAtr(),
								baseAutoCalSetting.getNormalOTTime().getLegalMidOtTime().getUpLimitORtSet()));

				AutoCalOfLeaveEarlySetting autoCalOfLeaveEarlySetting = new AutoCalOfLeaveEarlySetting(
						LeaveAttr.NOT_USE, LeaveAttr.NOT_USE);

				AutoCalcSetOfDivergenceTime autoCalcSetOfDivergenceTime = new AutoCalcSetOfDivergenceTime(
						DivergenceTimeAttr.NOT_USE);

				CalAttrOfDailyPerformance calAttrOfDailyPerformance = new CalAttrOfDailyPerformance(employeeID, day,
						flexExcessTime, autoCalRaisingSalarySetting, holidayTimeSetting, overtimeSetting,
						autoCalOfLeaveEarlySetting, autoCalcSetOfDivergenceTime);

				// end -----
				// 1日半日出勤・1日休日系の判定
				WorkStyle workStyle = basicScheduleService.checkWorkDay(
						workInfoOfDailyPerformanceUpdate.getRecordWorkInformation().getWorkTypeCode().v());
				if (workStyle != WorkStyle.ONE_DAY_REST) {
					createStamp(companyId, workInfoOfDailyPerformanceUpdate, workingConditionItem, timeLeavingOptional,
							employeeID, day);
					// check tay
					stampOutput = this.reflectStampDomainServiceImpl.reflectStampInfo(companyId, employeeID, day,
							workInfoOfDailyPerformanceUpdate, timeLeavingOptional, empCalAndSumExecLogID, reCreateAttr,
							breakTimeOfDailyPerformance.get());
				}

				this.registerDailyPerformanceInfoService.registerDailyPerformanceInfo(employeeID, day, stampOutput,
						affiliationInforOfDailyPerfor, workInfoOfDailyPerformanceUpdate, specificDateAttrOfDailyPerfor,
						calAttrOfDailyPerformance, workTypeOfDailyPerformance, breakTimeOfDailyPerformance.get());
			}
		}

	}

	private WorkInformation getWorkInfo(Optional<SingleDaySchedule> workingCondition, SingleDaySchedule category) {
		WorkInformation recordWorkInformation;
		if (workingCondition.isPresent()) {
			recordWorkInformation = new WorkInformation(workingCondition.get().getWorkTimeCode().orElse(null),
					workingCondition.get().getWorkTypeCode());
		} else {
			recordWorkInformation = new WorkInformation(category.getWorkTimeCode().orElse(null),
					category.getWorkTypeCode());
		}
		return recordWorkInformation;
	}

	/**
	 * 特定日を日別実績に反映する
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param day
	 * @param workPlaceID
	 * @return SpecificDateAttrOfDailyPerfor
	 */
	@Override
	public SpecificDateAttrOfDailyPerfor reflectSpecificDate(String companyId, String employeeId, GeneralDate day,
			String workPlaceID) {
		RecSpecificDateSettingImport specificDateSettingImport = this.recSpecificDateSettingAdapter
				.specificDateSettingService(companyId, workPlaceID, day);

		List<SpecificDateAttrSheet> specificDateAttrSheets = new ArrayList<>();
		IntStream.range(1, 11).forEach(i -> {
			if (specificDateSettingImport.getNumberList().contains(i)) {
				SpecificDateAttrSheet specificDateAttrSheet = new SpecificDateAttrSheet(new SpecificDateItemNo(i),
						SpecificDateAttr.USE);
				specificDateAttrSheets.add(specificDateAttrSheet);
			} else {
				SpecificDateAttrSheet specificDateAttrSheet = new SpecificDateAttrSheet(new SpecificDateItemNo(i),
						SpecificDateAttr.NOT_USE);
				specificDateAttrSheets.add(specificDateAttrSheet);
			}
		});
		SpecificDateAttrOfDailyPerfor specificDateAttrOfDailyPerfor = new SpecificDateAttrOfDailyPerfor(employeeId,
				specificDateAttrSheets, day);
		return specificDateAttrOfDailyPerfor;
	}

	private void createStamp(String companyId, WorkInfoOfDailyPerformance workInfoOfDailyPerformanceUpdate,
			Optional<WorkingConditionItem> workingConditionItem, TimeLeavingOfDailyPerformance timeLeavingOptional,
			String employeeID, GeneralDate day) {
		// ドメインモデル「打刻反映管理」を取得する
		Optional<StampReflectionManagement> stampReflectionManagement = this.stampReflectionManagementRepository
				.findByCid(companyId);

		// 自動打刻セットする - set new 自動打刻セット詳細
		// 自動打刻セット詳細をクリア
		AutomaticStampSetDetailOutput automaticStampSetDetailDto = new AutomaticStampSetDetailOutput();
		// ドメインモデル「個人労働条件」を取得する
		if (workingConditionItem.get().getAutoStampSetAtr() == NotUseAtr.USE) {
			// 出勤と退勤を反映する設定にする
			automaticStampSetDetailDto.setAttendanceReflectAttr(UseAtr.USE);
			automaticStampSetDetailDto.setAttendanceStamp(StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO);
			automaticStampSetDetailDto.setRetirementAttr(UseAtr.USE);
			automaticStampSetDetailDto.setLeavingStamp(StampSourceInfo.STAMP_AUTO_SET_PERSONAL_INFO);
		}

		// ドメインモデル「日別実績の勤務情報」を取得する
		if (workInfoOfDailyPerformanceUpdate.getGoStraightAtr() == NotUseAttribute.Use) {
			automaticStampSetDetailDto.setAttendanceReflectAttr(UseAtr.USE);
			automaticStampSetDetailDto.setAttendanceStamp(StampSourceInfo.GO_STRAIGHT);
		}
		if (workInfoOfDailyPerformanceUpdate.getBackStraightAtr() == NotUseAttribute.Use) {
			automaticStampSetDetailDto.setRetirementAttr(UseAtr.USE);
			automaticStampSetDetailDto.setLeavingStamp(StampSourceInfo.GO_STRAIGHT);
		}

		// 自動打刻セット詳細に従って自動打刻セットする
		// 自動打刻セット詳細を確認する - confirm automaticStampSetDetailDto data
		if (automaticStampSetDetailDto.getAttendanceReflectAttr() == UseAtr.USE
				|| automaticStampSetDetailDto.getRetirementAttr() == UseAtr.USE) {
			// セットする打刻詳細を取得する
			// 勤務実績の勤務情報と勤務予定の勤務情報を比較
			// 予定時間帯を自動打刻セット詳細に入れる
			// temp class
			List<TimeLeavingWorkOutput> timeLeavingWorkTemps = new ArrayList<>();
			List<TimeLeavingWork> timeLeavingWorks = new ArrayList<>();
			if (workInfoOfDailyPerformanceUpdate.getRecordWorkInformation() != null) {

				if (workInfoOfDailyPerformanceUpdate.getRecordWorkInformation().getWorkTimeCode()
						.equals(workInfoOfDailyPerformanceUpdate.getScheduleWorkInformation().getWorkTimeCode())
						&& workInfoOfDailyPerformanceUpdate.getRecordWorkInformation().getWorkTypeCode().equals(
								workInfoOfDailyPerformanceUpdate.getScheduleWorkInformation().getWorkTypeCode())) {

					// 自動打刻セット詳細．出退勤 ← 勤務予定時間帯
					workInfoOfDailyPerformanceUpdate.getScheduleTimeSheets().forEach(sheet -> {

						TimeLeavingWorkOutput timeLeavingWorkOutput = new TimeLeavingWorkOutput();
						TimeActualStampOutPut attendanceStampTemp = new TimeActualStampOutPut();
						TimeActualStampOutPut leaveStampTemp = new TimeActualStampOutPut();

						timeLeavingWorkOutput.setWorkNo(sheet.getWorkNo());

						// // 出勤系時刻を丸める (làm tròn thời gian 出勤)
						// Optional<WorkTimezoneCommonSet> workTimezoneCommonSet
						// = this.getCommonSet.get(companyId,
						// workInfoOfDailyPerformanceUpdate.getScheduleWorkInformation().getWorkTimeCode().v());
						// WorkTimezoneStampSet stampSet =
						// workTimezoneCommonSet.get().getStampSet();
						// // 出勤
						// RoundingSet atendanceRoundingSet =
						// stampSet.getRoundingSets().stream()
						// .filter(item -> item.getSection() ==
						// Superiority.ATTENDANCE).findFirst().get();
						// int attendanceTimeAfterRouding =
						// this.roudingTime(sheet.getAttendance().v(),
						// atendanceRoundingSet.getRoundingSet().getFontRearSection().value,
						// atendanceRoundingSet.getRoundingSet().getRoundingTimeUnit().value);
						// // 退勤
						// RoundingSet leavingRoundingSet =
						// stampSet.getRoundingSets().stream()
						// .filter(item -> item.getSection() ==
						// Superiority.OFFICE_WORK).findFirst().get();
						// int leaveTimeAfterRounding =
						// this.roudingTime(sheet.getLeaveWork().v(),
						// leavingRoundingSet.getRoundingSet().getFontRearSection().value,
						// leavingRoundingSet.getRoundingSet().getRoundingTimeUnit().value);

						// ドメインモデル「所属職場履歴」を取得する
						// attendanceStampTemp.setStamp(new WorkStampOutPut(new
						// TimeWithDayAttr(attendanceTimeAfterRouding),
						// sheet.getAttendance(), null,
						// automaticStampSetDetailDto.getAttendanceStamp()));
						// leaveStampTemp.setStamp(new WorkStampOutPut(new
						// TimeWithDayAttr(leaveTimeAfterRounding),
						// sheet.getLeaveWork(), null,
						// automaticStampSetDetailDto.getLeavingStamp()));
						attendanceStampTemp.setStamp(new WorkStampOutPut(new TimeWithDayAttr(sheet.getAttendance().v()),
								sheet.getAttendance(), null, automaticStampSetDetailDto.getAttendanceStamp()));
						leaveStampTemp.setStamp(new WorkStampOutPut(new TimeWithDayAttr(sheet.getLeaveWork().v()),
								sheet.getLeaveWork(), null, automaticStampSetDetailDto.getLeavingStamp()));
						timeLeavingWorkOutput.setAttendanceStamp(attendanceStampTemp);
						timeLeavingWorkOutput.setLeaveStamp(leaveStampTemp);
						timeLeavingWorkTemps.add(timeLeavingWorkOutput);
					});
				} else {
					// 出勤休日区分を確認する (Xác nhận 出勤休日区分)
					Optional<WorkType> workTypeOptional = this.workTypeRepository.findByPK(companyId,
							workInfoOfDailyPerformanceUpdate.getRecordWorkInformation().getWorkTypeCode().v());
					WorkStyle workStyle = this.basicScheduleService
							.checkWorkDay(workTypeOptional.get().getWorkTypeCode().v());
					if (!(workStyle == WorkStyle.ONE_DAY_REST)) {

						// 所定時間帯を取得する
						Optional<PredetemineTimeSetting> predetemineTimeSetting = predetemineTimeSettingRepository
								.findByWorkTimeCode(companyId, workInfoOfDailyPerformanceUpdate
										.getRecordWorkInformation().getWorkTypeCode().v());

						if (predetemineTimeSetting.isPresent()) {
							List<TimezoneUse> lstTimezone = predetemineTimeSetting.get().getPrescribedTimezoneSetting()
									.getLstTimezone();
							for (TimezoneUse timezone : lstTimezone) {
								if (timezone.getUseAtr() == UseSetting.USE) {
									TimeLeavingWorkOutput timeLeavingWorkOutput = new TimeLeavingWorkOutput();
									timeLeavingWorkOutput.setWorkNo(new WorkNo(timezone.getWorkNo()));

									TimeActualStampOutPut attendanceTimeActualStampOutPut = new TimeActualStampOutPut();
									WorkStampOutPut actualStamp = new WorkStampOutPut();
									actualStamp.setTimeWithDay(timezone.getStart());

									TimeActualStampOutPut leaveTimeActualStampOutPut = new TimeActualStampOutPut();
									WorkStampOutPut leaveActualStamp = new WorkStampOutPut();
									leaveActualStamp.setTimeWithDay(timezone.getEnd());

									// // 出勤系時刻を丸める
									// Optional<WorkTimezoneCommonSet>
									// workTimezoneCommonSet =
									// this.getCommonSet.get(companyId,
									// workInfoOfDailyPerformanceUpdate.getScheduleWorkInformation().getWorkTimeCode().v());
									// WorkTimezoneStampSet stampSet =
									// workTimezoneCommonSet.get().getStampSet();
									// // 出勤
									// RoundingSet atendanceRoundingSet =
									// stampSet.getRoundingSets().stream()
									// .filter(item -> item.getSection() ==
									// Superiority.ATTENDANCE).findFirst().get();
									// int attendanceTimeAfterRouding =
									// this.roudingTime(timezone.getStart().v(),
									// atendanceRoundingSet.getRoundingSet().getFontRearSection().value,
									// atendanceRoundingSet.getRoundingSet().getRoundingTimeUnit().value);

									// actualStamp.setAfterRoundingTime(new
									// TimeWithDayAttr(attendanceTimeAfterRouding));

									actualStamp.setAfterRoundingTime(new TimeWithDayAttr(timezone.getStart().v()));
									// // 退勤
									// RoundingSet leavingRoundingSet =
									// stampSet.getRoundingSets().stream()
									// .filter(item -> item.getSection() ==
									// Superiority.OFFICE_WORK).findFirst().get();
									// int leaveTimeAfterRounding =
									// this.roudingTime(timezone.getEnd().v(),
									// leavingRoundingSet.getRoundingSet().getFontRearSection().value,
									// leavingRoundingSet.getRoundingSet().getRoundingTimeUnit().value);

									// leaveActualStamp.setAfterRoundingTime(new
									// TimeWithDayAttr(leaveTimeAfterRounding));

									leaveActualStamp.setAfterRoundingTime(new TimeWithDayAttr(timezone.getEnd().v()));

									Optional<AffWorkplaceDto> affWorkplaceDto = this.affWorkplaceAdapter
											.findBySid(employeeID, day);

									if (affWorkplaceDto.isPresent()) {
										actualStamp.setLocationCode(null);
										leaveActualStamp.setLocationCode(null);
									}
									actualStamp.setStampSourceInfo(automaticStampSetDetailDto.getAttendanceStamp());
									leaveActualStamp.setStampSourceInfo(automaticStampSetDetailDto.getLeavingStamp());

									attendanceTimeActualStampOutPut.setStamp(actualStamp);
									leaveTimeActualStampOutPut.setStamp(leaveActualStamp);
									timeLeavingWorkOutput.setAttendanceStamp(attendanceTimeActualStampOutPut);
									timeLeavingWorkOutput.setLeaveStamp(leaveTimeActualStampOutPut);
									timeLeavingWorkTemps.add(timeLeavingWorkOutput);
								}
							}

						}
					}
				}
			}
			timeLeavingWorks = timeLeavingWorkTemps.stream().map(item -> {
				TimeActualStamp attendanceStamp = null;
				if (item.getAttendanceStamp() != null) {
					WorkStamp actualStamp = null;
					if (item.getAttendanceStamp().getActualStamp() != null) {
						actualStamp = new WorkStamp(item.getAttendanceStamp().getActualStamp().getAfterRoundingTime(),
								item.getAttendanceStamp().getActualStamp().getTimeWithDay(),
								item.getAttendanceStamp().getActualStamp().getLocationCode(),
								item.getAttendanceStamp().getActualStamp().getStampSourceInfo());
					}

					WorkStamp stamp = new WorkStamp(item.getAttendanceStamp().getStamp().getAfterRoundingTime(),
							item.getAttendanceStamp().getStamp().getTimeWithDay(),
							item.getAttendanceStamp().getStamp().getLocationCode(),
							item.getAttendanceStamp().getStamp().getStampSourceInfo());
					attendanceStamp = new TimeActualStamp(actualStamp, stamp,
							item.getAttendanceStamp().getNumberOfReflectionStamp());

				}

				TimeActualStamp leaveStamp = null;
				if (item.getLeaveStamp() != null) {
					WorkStamp leaveActualStampTemp = null;
					if (item.getLeaveStamp().getActualStamp() != null) {
						leaveActualStampTemp = new WorkStamp(
								item.getLeaveStamp().getActualStamp().getAfterRoundingTime(),
								item.getLeaveStamp().getActualStamp().getTimeWithDay(),
								item.getLeaveStamp().getActualStamp().getLocationCode(),
								item.getLeaveStamp().getActualStamp().getStampSourceInfo());
					}

					WorkStamp leaveStampTemp = new WorkStamp(item.getLeaveStamp().getStamp().getAfterRoundingTime(),
							item.getLeaveStamp().getStamp().getTimeWithDay(),
							item.getLeaveStamp().getStamp().getLocationCode(),
							item.getLeaveStamp().getStamp().getStampSourceInfo());

					leaveStamp = new TimeActualStamp(leaveActualStampTemp, leaveStampTemp,
							item.getLeaveStamp().getNumberOfReflectionStamp());
				}

				return new TimeLeavingWork(item.getWorkNo(),
						attendanceStamp == null ? null : Optional.of(attendanceStamp), Optional.ofNullable(leaveStamp));
			}).collect(Collectors.toList());
			automaticStampSetDetailDto.setTimeLeavingWorks(timeLeavingWorks);

			Calendar toDay = Calendar.getInstance();
			Date date2 = toDay.getTime();
			int hour = toDay.get(Calendar.HOUR_OF_DAY);
			int minute = toDay.get(Calendar.MINUTE);
			int currentMinuteOfDay = ((hour * 60) + minute);

			List<TimeLeavingWork> timeLeavingWorkList = new ArrayList<>();

			for (TimeLeavingWork timeLeavingWork : automaticStampSetDetailDto.getTimeLeavingWorks()) {

				TimeLeavingWork leavingStamp = null;
				if (timeLeavingOptional.getTimeLeavingWorks() != null) {
					leavingStamp = timeLeavingOptional.getTimeLeavingWorks().stream()
							.filter(itemx -> itemx.getWorkNo().v().equals(timeLeavingWork.getWorkNo().v())).findFirst()
							.get();
				}

				TimeActualStamp attendanceStamp = new TimeActualStamp();
				TimeActualStamp leaveStamp = new TimeActualStamp();

				// 出勤反映 = true
				// 出勤に自動打刻セットする
				if (automaticStampSetDetailDto.getAttendanceReflectAttr() == UseAtr.USE) {

					// set attendance
					if (stampReflectionManagement.get()
							.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.SET_AUTO_STAMP
							|| (stampReflectionManagement.get()
									.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.DO_NOT_SET_AUTO_STAMP
									&& day.date().before(date2))
							|| (stampReflectionManagement.get()
									.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.DO_NOT_SET_AUTO_STAMP
									&& day.date().equals(date2) && timeLeavingWork.getAttendanceStamp().isPresent()
									&& timeLeavingWork.getAttendanceStamp().get().getStamp().isPresent()
									&& timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay()
											.lessThanOrEqualTo(currentMinuteOfDay))) {

						if (timeLeavingOptional.getTimeLeavingWorks() == null || leavingStamp == null
								|| (leavingStamp != null && !leavingStamp.getAttendanceStamp().isPresent())
								|| (leavingStamp != null && leavingStamp.getAttendanceStamp().isPresent()
										&& leavingStamp.getAttendanceStamp().get().getStamp() == null)) {

							WorkStamp stamp = new WorkStamp(
									timeLeavingWork.getAttendanceStamp().get().getStamp().get().getAfterRoundingTime(),
									timeLeavingWork.getAttendanceStamp().get().getStamp().get().getTimeWithDay(),
									timeLeavingWork.getAttendanceStamp().get().getStamp().get().getLocationCode()
											.isPresent()
													? timeLeavingWork.getAttendanceStamp().get().getStamp().get()
															.getLocationCode().get()
													: null,
									timeLeavingWork.getAttendanceStamp().get().getStamp().get().getStampSourceInfo());
							attendanceStamp = new TimeActualStamp(null, stamp,
									timeLeavingWork.getAttendanceStamp().get().getNumberOfReflectionStamp());

						}
					}
				}

				// set leave
				// 退勤反映 = true
				if (automaticStampSetDetailDto.getRetirementAttr() == UseAtr.USE) {
					if (stampReflectionManagement.get()
							.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.SET_AUTO_STAMP
							|| (stampReflectionManagement.get()
									.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.DO_NOT_SET_AUTO_STAMP
									&& day.date().before(date2))
							|| (stampReflectionManagement.get()
									.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.DO_NOT_SET_AUTO_STAMP
									&& day.date().equals(date2) && timeLeavingWork.getLeaveStamp().isPresent()
									&& timeLeavingWork.getLeaveStamp().get().getStamp().isPresent()
									&& timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeWithDay()
											.lessThanOrEqualTo(currentMinuteOfDay))) {
						if (timeLeavingOptional.getTimeLeavingWorks() == null || leavingStamp == null
								|| (leavingStamp != null && !leavingStamp.getLeaveStamp().isPresent())
								|| (leavingStamp != null && leavingStamp.getLeaveStamp().isPresent()
										&& leavingStamp.getLeaveStamp().get().getStamp() == null)) {

							WorkStamp stamp = new WorkStamp(
									timeLeavingWork.getLeaveStamp().get().getStamp().get().getAfterRoundingTime(),
									timeLeavingWork.getLeaveStamp().get().getStamp().get().getTimeWithDay(),
									timeLeavingWork.getLeaveStamp().get().getStamp().get().getLocationCode().isPresent()
											? timeLeavingWork.getLeaveStamp().get().getStamp().get().getLocationCode()
													.get()
											: null,
									timeLeavingWork.getLeaveStamp().get().getStamp().get().getStampSourceInfo());
							leaveStamp = new TimeActualStamp(null, stamp,
									timeLeavingWork.getAttendanceStamp().get().getNumberOfReflectionStamp());

						}
					}

				}

				leavingStamp = new TimeLeavingWork(timeLeavingWork.getWorkNo(), Optional.of(attendanceStamp),
						Optional.of(leaveStamp));
				// leavingStamp.setTimeLeavingWork(timeLeavingWork.getWorkNo(),
				// Optional.of(attendanceStamp), Optional.of(leaveStamp));
				timeLeavingWorkList.add(leavingStamp);
			}
			;
			timeLeavingOptional.setWorkTimes(new WorkTimes(1));
			timeLeavingOptional.setEmployeeId(employeeID);
			timeLeavingOptional.setYmd(day);
			timeLeavingOptional.setTimeLeavingWorks(timeLeavingWorkList);
		} else {
			timeLeavingOptional = null;
		}
	}

	/**
	 * 加給設定を日別実績に反映する
	 * 
	 * @param companyId
	 * @param employeeId
	 * @param day
	 * @param workTimeCode
	 * @return
	 */
	private Optional<BonusPaySetting> reflectBonusSetting(String companyId, String employeeId, GeneralDate day,
			String workTimeCode) {

		Optional<BonusPaySetting> bonusPaySetting = Optional.empty();

		// ドメインモデル「就業時間帯加給設定」を取得
		Optional<WorkingTimesheetBonusPaySetting> workingTimesheetBonusPaySetting = this.wTBonusPaySettingRepository
				.getWTBPSetting(companyId, new WorkingTimesheetCode(workTimeCode));

		if (!workingTimesheetBonusPaySetting.isPresent()) {
			// ドメインモデル「個人加給設定」を取得
			Optional<PersonalBonusPaySetting> personalBonusPaySetting = this.pSBonusPaySettingRepository
					.getPersonalBonusPaySetting(employeeId);
			if (!personalBonusPaySetting.isPresent()) {
				// 所属職場履歴を取得
				Optional<AffWorkPlaceSidImport> workPlace = this.affWorkplaceAdapter.findBySidAndDate(employeeId, day);
				// ドメインモデル「職場加給設定」を取得
				Optional<WorkplaceBonusPaySetting> workplaceBonusPaySetting = this.wPBonusPaySettingRepository
						.getWPBPSetting(new WorkplaceId(workPlace.get().getWorkplaceId()));
				if (!workplaceBonusPaySetting.isPresent()) {
					Optional<CompanyBonusPaySetting> companyBonusPaySetting = this.cPBonusPaySettingRepository
							.getSetting(companyId);
					if (companyBonusPaySetting.isPresent()) {
						bonusPaySetting = this.bPSettingRepository.getBonusPaySetting(companyId,
								companyBonusPaySetting.get().getBonusPaySettingCode());
					}
				} else {
					bonusPaySetting = this.bPSettingRepository.getBonusPaySetting(companyId,
							workplaceBonusPaySetting.get().getBonusPaySettingCode());
				}
			} else {
				bonusPaySetting = this.bPSettingRepository.getBonusPaySetting(companyId,
						personalBonusPaySetting.get().getBonusPaySettingCode());
			}
		} else {
			bonusPaySetting = this.bPSettingRepository.getBonusPaySetting(companyId,
					workingTimesheetBonusPaySetting.get().getBonusPaySettingCode());
		}

		return bonusPaySetting;
	}

	private int roudingTime(int time, int fontRearSection, int roundingTimeUnit) {

		BigDecimal result = new BigDecimal(time).divide(new BigDecimal(roundingTimeUnit));

		if (!(result.signum() == 0 || result.scale() <= 0 || result.stripTrailingZeros().scale() <= 0)) {
			if (fontRearSection == 0) {
				result = result.setScale(0, RoundingMode.DOWN);
			} else if (fontRearSection == 1) {
				result = result.setScale(0, RoundingMode.UP);
				;
			}
		} else {
			return result.intValue();
		}
		return result.intValue();
	}

	private List<GeneralDate> getDaysBetween(GeneralDate startDate, GeneralDate endDate) {
		List<GeneralDate> daysBetween = new ArrayList<>();

		while (startDate.beforeOrEquals(endDate)) {
			daysBetween.add(startDate);
			GeneralDate temp = startDate.addDays(1);
			startDate = temp;
		}

		return daysBetween;
	}

	@Override
	public Optional<WorkInfoOfDailyPerformance> reflectHolidayOfDailyPerfor(String companyId, String employeeId,
			GeneralDate day) {
		RecStatusOfEmployeeImport recStatusOfEmployeeImport = this.recStatusOfEmployeeAdapter
				.getStatusOfEmployeeService(employeeId, day);
		Optional<WorkInfoOfDailyPerformance> workInfoOfDailyPerformance = Optional.empty();
		if (recStatusOfEmployeeImport != null) {
			if (recStatusOfEmployeeImport.getStatusOfEmployment() == 2
					|| recStatusOfEmployeeImport.getStatusOfEmployment() == 3) {
				List<WorkType> workTypeList = this.workTypeRepository.findByCompanyId(companyId);
				List<WorkType> workTypeOneDayList = workTypeList.stream().filter(x -> x.isOneDay())
						.collect(Collectors.toList());
				WorkType workTypeNeed = null;
				for (WorkType workType : workTypeOneDayList) {
					WorkTypeSet workTypeSet = workType.getWorkTypeSetByAtr(WorkAtr.OneDay);
					if (recStatusOfEmployeeImport.getStatusOfEmployment() == 2
							&& WorkTypeClassification.LeaveOfAbsence == workType.getDailyWork().getOneDay()) {
						// 日別実績の勤務種類を更新(Update Worktype của 日別実績)
						workTypeNeed = workType;
						break;
					} else if (recStatusOfEmployeeImport.getStatusOfEmployment() == 3
							&& WorkTypeClassification.Closure == workType.getDailyWork().getOneDay()) {
						// 日別実績の勤務種類を更新(Update Worktype của 日別実績)
						workTypeNeed = workType;
						break;
					}
				}

			}
		}
		return workInfoOfDailyPerformance;
	}

	@AllArgsConstructor
	/**
	 * 再作成しない : 0 再作成する : 1
	 */
	public enum ExitStatus {
		/* 再作成しない */
		DO_NOT_RECREATE(0),

		/* 再作成する */
		RECREATE(1);

		public final int value;

	}

	// private void lateCorrection(TimeActualStamp timeActualStamp) {
	// // ドメインモデル「就業時間帯の遅刻・早退設定」を取得する
	// OtherEmTimezoneLateEarlySet earlySet = new OtherEmTimezoneLateEarlySet();
	// if (earlySet.isStampExactlyTimeIsLateEarly() &&
	// !timeActualStamp.getStamp().equals(null)) {
	// timeActualStamp.setStamp(new WorkStamp(
	// new TimeWithDayAttr(timeActualStamp.getStamp().getAfterRoundingTime().v()
	// - 1),
	// new
	// TimeWithDayAttr(timeActualStamp.getStamp().getTimeWithDay().valueAsMinutes()
	// - 1),
	// timeActualStamp.getStamp().getLocationCode(),
	// timeActualStamp.getStamp().getStampSourceInfo()));
	// }
	// }

}
