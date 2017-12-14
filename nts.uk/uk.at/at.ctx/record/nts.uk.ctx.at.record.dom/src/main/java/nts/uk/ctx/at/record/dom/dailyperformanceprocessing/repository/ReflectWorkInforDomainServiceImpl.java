package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleAdapter;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.BasicScheduleSidDto;
import nts.uk.ctx.at.record.dom.adapter.basicschedule.WorkScheduleSidImport;
import nts.uk.ctx.at.record.dom.adapter.businesscalendar.daycalendar.CalendarInfoImport;
import nts.uk.ctx.at.record.dom.adapter.businesscalendar.daycalendar.RecCalendarCompanyAdapter;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationAdapter;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationSidImport;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentAdapter;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkPlaceSidImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceDto;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.primitivevalue.ClassificationCode;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalStatusOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.calculationsetting.StampReflectionManagement;
import nts.uk.ctx.at.record.dom.calculationsetting.enums.AutoStampForFutureDayClass;
import nts.uk.ctx.at.record.dom.calculationsetting.repository.StampReflectionManagementRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.AutomaticStampSetDetailOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.TimeActualStampOutPut;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.TimeLeavingWorkOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.WorkStampOutPut;
import nts.uk.ctx.at.record.dom.editstate.repository.EditStateOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.jobtitle.affiliate.AffJobTitleAdapter;
import nts.uk.ctx.at.record.dom.jobtitle.affiliate.AffJobTitleSidImport;
import nts.uk.ctx.at.record.dom.workinformation.ScheduleTimeSheet;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.WorkInformation;
import nts.uk.ctx.at.record.dom.workinformation.enums.CalculationState;
import nts.uk.ctx.at.record.dom.workinformation.enums.NotUseAttribute;
import nts.uk.ctx.at.record.dom.workinformation.primitivevalue.WorkTimeCode;
import nts.uk.ctx.at.record.dom.workinformation.primitivevalue.WorkTypeCode;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.worklocation.WorkLocationCD;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.log.ErrMessageContent;
import nts.uk.ctx.at.record.dom.workrecord.log.ErrMessageInfo;
import nts.uk.ctx.at.record.dom.workrecord.log.ErrMessageInfoRepository;
import nts.uk.ctx.at.record.dom.workrecord.log.ErrMessageResource;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.log.enums.ExecutionType;
import nts.uk.ctx.at.record.dom.worktime.TimeActualStamp;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingOfDailyPerformance;
import nts.uk.ctx.at.record.dom.worktime.TimeLeavingWork;
import nts.uk.ctx.at.record.dom.worktime.WorkStamp;
import nts.uk.ctx.at.record.dom.worktime.enums.StampSourceInfo;
import nts.uk.ctx.at.record.dom.worktime.primitivevalue.WorkNo;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborConditionRepository;
import nts.uk.ctx.at.shared.dom.personallaborcondition.UseAtr;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.vacation.setting.compensatoryleave.EmploymentCode;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.predset.UseSetting;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeSetCheck;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeUnit;
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

	@Override
	public void reflectWorkInformation(String companyId, String employeeId, GeneralDate day,
			String empCalAndSumExecLogID, ExecutionType reCreateAttr) {

		AffiliationInforOfDailyPerfor affiliationInforOfDailyPerfor = new AffiliationInforOfDailyPerfor();

		// Get Data
		List<ErrMessageInfo> errMesInfos = new ArrayList<>();

		// Imported(就業．勤務実績)「所属職場履歴」を取得する
		Optional<AffWorkPlaceSidImport> workPlaceHasData = this.affWorkplaceAdapter.findBySidAndDate(employeeId, day);

		// ドメインモデル「日別実績の勤務情報」を削除する - rerun
		if (reCreateAttr == ExecutionType.RERUN) {
			this.workInformationRepository.delete(employeeId, day);
			// this.approvalStatusOfDailyPerforRepository.delete(employeeId,
			// day);
			this.affiliationInforOfDailyPerforRepository.delete(employeeId, day);
			// this.identificationRepository.delete(employeeId, day);
			// this.timeLeavingOfDailyPerformanceRepository.delete(employeeId,
			// day);
			// this.temporaryTimeOfDailyPerformanceRepository.delete(employeeId,
			// day);
			// this.editStateOfDailyPerformanceRepository.delete(employeeId,
			// day);
			// this.breakTimeOfDailyPerformanceRepository.delete(employeeId,
			// day);
			// this.outingTimeOfDailyPerformanceRepository.delete(employeeId,
			// day);
		}
		// ドメインモデル「日別実績の勤務情報」を取得する - not rerun
		if (!this.workInformationRepository.find(employeeId, day).isPresent()) {

			// Imported(就業．勤務実績)「所属雇用履歴」を取得する
			Optional<SyEmploymentImport> employmentHasData = this.syEmploymentAdapter.findByEmployeeId(companyId,
					employeeId, day);

			// Imported(就業．勤務実績)「所属分類履歴」を取得する
			Optional<AffClassificationSidImport> classificationHasData = this.affClassificationAdapter
					.findByEmployeeId(companyId, employeeId, day);

			// Imported(就業．勤務実績)「所属職位履歴」を取得する
			Optional<AffJobTitleSidImport> jobTitleHasData = this.affJobTitleAdapter.findByEmployeeId(employeeId, day);

			// 取得したImported(就業．勤務実績)「所属雇用履歴」が存在するか確認する
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
				affiliationInforOfDailyPerfor = new AffiliationInforOfDailyPerfor(
						new EmploymentCode(employmentHasData.get().getEmploymentCode()), employeeId,
						jobTitleHasData.get().getJobTitleId(), workPlaceHasData.get().getWorkplaceId(), day,
						new ClassificationCode(classificationHasData.get().getClassificationCode()), null);
			}
			if (errMesInfos.isEmpty()) {
				// Imported(就業.勤務実績)「社員の勤務予定管理」を取得する
				this.workschedule(companyId, employeeId, day, empCalAndSumExecLogID, affiliationInforOfDailyPerfor,
						workPlaceHasData, reCreateAttr);
			} else {
				errMesInfos.forEach(action -> {
					this.errMessageInfoRepository.add(action);
				});
			}
		}
	}

	private void workschedule(String companyId, String employeeID, GeneralDate day, String empCalAndSumExecLogID,
			AffiliationInforOfDailyPerfor affiliationInforOfDailyPerfor,
			Optional<AffWorkPlaceSidImport> workPlaceHasData, ExecutionType reCreateAttr) {

		// status
		// 正常終了 : 0
		// 中断 : 1

		List<ErrMessageInfo> errMesInfos = new ArrayList<>();

		WorkInfoOfDailyPerformance workInfoOfDailyPerformanceUpdate = new WorkInfoOfDailyPerformance();

		// 日別実績の出退勤
		TimeLeavingOfDailyPerformance timeLeavingOptional = new TimeLeavingOfDailyPerformance();

		// ドメインモデル「個人労働条件．予定管理区分」を取得する
		Optional<PersonalLaborCondition> personalLaborHasData = this.personalLaborConditionRepository
				.findById(employeeID, day);
		if (!personalLaborHasData.isPresent()) {
			ErrMessageInfo employmentErrMes = new ErrMessageInfo(employeeID, empCalAndSumExecLogID,
					new ErrMessageResource("005"), EnumAdaptor.valueOf(0, ExecutionContent.class), day,
					new ErrMessageContent(TextResource.localize("Msg_430")));
			errMesInfos.add(employmentErrMes);
			this.errMessageInfoRepository.addList(errMesInfos);
		} else {

			workInfoOfDailyPerformanceUpdate.setEmployeeId(employeeID);
			workInfoOfDailyPerformanceUpdate.setCalculationState(CalculationState.Calculated);
			workInfoOfDailyPerformanceUpdate.setYmd(day);

			if (personalLaborHasData.get().getScheduleManagementAtr() == UseAtr.USE) {

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
							new WorkInformation(basicScheduleHasData.get().getWorkTypeCode(),
									basicScheduleHasData.get().getWorkTimeCode()));
					workInfoOfDailyPerformanceUpdate
							.setRecordWorkInformation(new WorkInformation(basicScheduleHasData.get().getWorkTypeCode(),
									basicScheduleHasData.get().getWorkTimeCode()));

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

							ScheduleTimeSheet scheduleTimeSheet = new ScheduleTimeSheet(
									new BigDecimal(items.getScheduleCnt()), items.getScheduleStartClock(),
									items.getScheduleStartClock());
							scheduleTimeSheets.add(scheduleTimeSheet);
						});

						workInfoOfDailyPerformanceUpdate.setScheduleTimeSheets(scheduleTimeSheets);
					}
				}

			} else {
				// 個人情報から勤務種類と就業時間帯を写す
				// 個人情報に処理中の曜日の設定が存在するか確認する
				// 存在する - has data
				WorkInformation recordWorkInformation = new WorkInformation();
				if (personalLaborHasData.get().getWorkDayOfWeek().equals(day.dayOfWeek())) {
					// monday
					if (day.dayOfWeek() == 1) {
						// this.workInformationRepository.updateRecordWorkInfo(employeeID,
						// day,
						// personalLaborHasData.get().getWorkDayOfWeek().getMonday().get()
						// .getWorkTimeCode().get().v(),
						// personalLaborHasData.get().getWorkDayOfWeek().getMonday().get()
						// .getWorkTypeCode().v());
						recordWorkInformation.setWorkTypeCode(new WorkTypeCode(
								personalLaborHasData.get().getWorkDayOfWeek().getMonday().get().getWorkTypeCode().v()));
						recordWorkInformation.setWorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getMonday()
								.get().getWorkTimeCode().isPresent()
										? new WorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getMonday()
												.get().getWorkTimeCode().get().v())
										: new WorkTimeCode(""));
					}
					// tuesday
					else if (day.dayOfWeek() == 2) {
						recordWorkInformation.setWorkTypeCode(new WorkTypeCode(personalLaborHasData.get()
								.getWorkDayOfWeek().getTuesday().get().getWorkTypeCode().v()));
						recordWorkInformation.setWorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getTuesday()
								.get().getWorkTimeCode().isPresent()
										? new WorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getTuesday()
												.get().getWorkTimeCode().get().v())
										: new WorkTimeCode(""));
					}
					// wednesday
					else if (day.dayOfWeek() == 3) {
						recordWorkInformation.setWorkTypeCode(new WorkTypeCode(personalLaborHasData.get()
								.getWorkDayOfWeek().getWednesday().get().getWorkTypeCode().v()));
						recordWorkInformation.setWorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek()
								.getWednesday().get().getWorkTimeCode().isPresent()
										? new WorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getWednesday()
												.get().getWorkTimeCode().get().v())
										: new WorkTimeCode(""));
					}
					// thursday
					else if (day.dayOfWeek() == 4) {
						recordWorkInformation.setWorkTypeCode(new WorkTypeCode(personalLaborHasData.get()
								.getWorkDayOfWeek().getThursday().get().getWorkTypeCode().v()));
						recordWorkInformation.setWorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek()
								.getThursday().get().getWorkTimeCode().isPresent()
										? new WorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getThursday()
												.get().getWorkTimeCode().get().v())
										: new WorkTimeCode(""));
					}
					// friday
					else if (day.dayOfWeek() == 5) {
						recordWorkInformation.setWorkTypeCode(new WorkTypeCode(
								personalLaborHasData.get().getWorkDayOfWeek().getFriday().get().getWorkTypeCode().v()));
						recordWorkInformation.setWorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getFriday()
								.get().getWorkTimeCode().isPresent()
										? new WorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getFriday()
												.get().getWorkTimeCode().get().v())
										: new WorkTimeCode(""));
					}
					// saturday
					else if (day.dayOfWeek() == 6) {
						recordWorkInformation.setWorkTypeCode(new WorkTypeCode(personalLaborHasData.get()
								.getWorkDayOfWeek().getSaturday().get().getWorkTypeCode().v()));
						recordWorkInformation.setWorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek()
								.getSaturday().get().getWorkTimeCode().isPresent()
										? new WorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getSaturday()
												.get().getWorkTimeCode().get().v())
										: new WorkTimeCode(""));
					}
					// sunday
					else if (day.dayOfWeek() == 7) {
						recordWorkInformation.setWorkTypeCode(new WorkTypeCode(
								personalLaborHasData.get().getWorkDayOfWeek().getSunday().get().getWorkTypeCode().v()));
						recordWorkInformation.setWorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getSunday()
								.get().getWorkTimeCode().isPresent()
										? new WorkTimeCode(personalLaborHasData.get().getWorkDayOfWeek().getSunday()
												.get().getWorkTimeCode().get().v())
										: new WorkTimeCode(""));
					}
				}
				// 存在しない - no data
				else {
					recordWorkInformation.setWorkTypeCode(new WorkTypeCode(
							personalLaborHasData.get().getWorkCategory().getWeekdayTime().getWorkTypeCode().v()));
					recordWorkInformation.setWorkTimeCode(
							personalLaborHasData.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().isPresent()
									? new WorkTimeCode(personalLaborHasData.get().getWorkCategory().getWeekdayTime()
											.getWorkTimeCode().get().v())
									: new WorkTimeCode(""));
				}

				workInfoOfDailyPerformanceUpdate.setRecordWorkInformation(recordWorkInformation);

				// 直行直帰区分を写す - autoStampSetAtr of PersonalLaborCondition
				// 自動打刻セット区分を判断
				if (personalLaborHasData.get().getAutoStampSetAtr().value == 0) {
					String workTypeCode = workInfoOfDailyPerformanceUpdate.getRecordWorkInformation().getWorkTypeCode()
							.v();
					Optional<WorkType> workType = this.workTypeRepository.findByPK(companyId, workTypeCode);
					// 打刻の扱い方に従って、直行区分、直帰区分を更新
					if (workType.get().getDailyWork().getWorkTypeUnit() == WorkTypeUnit.OneDay) {
						if (workType.get().getWorkTypeSetList().get(0).getAttendanceTime() == WorkTypeSetCheck.CHECK) {
							workInfoOfDailyPerformanceUpdate
									.setGoStraightAtr(EnumAdaptor.valueOf(1, NotUseAttribute.class));
						} else if (workType.get().getWorkTypeSetList().get(0)
								.getAttendanceTime() == WorkTypeSetCheck.NO_CHECK) {
							workInfoOfDailyPerformanceUpdate
									.setGoStraightAtr(EnumAdaptor.valueOf(0, NotUseAttribute.class));
						}
						if (workType.get().getWorkTypeSetList().get(0).getTimeLeaveWork() == WorkTypeSetCheck.CHECK) {
							workInfoOfDailyPerformanceUpdate
									.setBackStraightAtr(EnumAdaptor.valueOf(1, NotUseAttribute.class));
						} else if (workType.get().getWorkTypeSetList().get(0)
								.getTimeLeaveWork() == WorkTypeSetCheck.NO_CHECK) {
							workInfoOfDailyPerformanceUpdate
									.setBackStraightAtr(EnumAdaptor.valueOf(0, NotUseAttribute.class));
						}
					}
				}
				;

				// カレンダー情報を取得する
				// a part of Du's team
				CalendarInfoImport calendarInfoDto = calendarCompanyAdapter.findCalendarCompany(companyId,
						affiliationInforOfDailyPerfor.getWplID(), affiliationInforOfDailyPerfor.getClsCode().v(), day);
				WorkInformation scheduleWorkInformation = new WorkInformation(calendarInfoDto.getWorkTimeCode(),
						calendarInfoDto.getWorkTypeCode());
				workInfoOfDailyPerformanceUpdate.setScheduleWorkInformation(scheduleWorkInformation);
				workInfoOfDailyPerformanceUpdate.setRecordWorkInformation(scheduleWorkInformation);

				// 所定時間帯を取得する
				PredetemineTimeSetting predetemineTimeSetting = predetemineTimeSettingRepository
						.findByWorkTimeCode(companyId, calendarInfoDto.getWorkTypeCode());

				if (predetemineTimeSetting != null) {
					List<TimezoneUse> lstTimezone = predetemineTimeSetting.getPrescribedTimezoneSetting().getLstTimezone();
					List<ScheduleTimeSheet> scheduleTimeSheets = new ArrayList<>();
					for (TimezoneUse timezone : lstTimezone) {
						if (timezone.getUseAtr() == UseSetting.USE) {
							ScheduleTimeSheet scheduleTimeSheet = new ScheduleTimeSheet(
									new BigDecimal(timezone.getWorkNo()), timezone.getStart().v(),
									timezone.getEnd().v());
							scheduleTimeSheets.add(scheduleTimeSheet);
						}
					}
					workInfoOfDailyPerformanceUpdate.setScheduleTimeSheets(scheduleTimeSheets);
				}
			}

			if (errMesInfos.isEmpty()) {
				createStamp(companyId, workInfoOfDailyPerformanceUpdate, personalLaborHasData, timeLeavingOptional, employeeID, day);
			}

		}

//		 check tay
//		 this.reflectStampDomainServiceImpl.reflectStampInfo(companyId,
//		 employeeID, day,
//		 workInfoOfDailyPerformanceUpdate, timeLeavingOptional, empCalAndSumExecLogID,reCreateAttr );

		if (errMesInfos.isEmpty()) {
			// 登録する - register - activity ⑤社員の日別実績を作成する
			// ドメインモデル「日別実績の勤務情報」を更新する - update
			// WorkInfoOfDailyPerformance
			if (this.workInformationRepository.find(employeeID, day).isPresent()) {
				this.workInformationRepository.updateByKey(workInfoOfDailyPerformanceUpdate);
			} else {
				this.workInformationRepository.insert(workInfoOfDailyPerformanceUpdate);
			}
			// ドメインモデル「日別実績の所属情報」を更新する - update
			// AffiliationInforOfDailyPerformance
			if (this.affiliationInforOfDailyPerforRepository.findByKey(employeeID, day).isPresent()) {
				this.affiliationInforOfDailyPerforRepository.updateByKey(affiliationInforOfDailyPerfor);
			} else {
				this.affiliationInforOfDailyPerforRepository.add(affiliationInforOfDailyPerfor);
			}
			// ドメインモデル「日別実績の休憩時間帯」を更新する
			// BreakTimeSheetOfDaily

			// ドメインモデル「日別実績の出退勤」を更新する - update
			// TimeLeavingOfDailyPerformance
			// timeLeavingOptional.getTimeLeavingWorks();
		}

	}

	private void createStamp(String companyId, WorkInfoOfDailyPerformance workInfoOfDailyPerformanceUpdate,
			Optional<PersonalLaborCondition> personalLaborHasData, TimeLeavingOfDailyPerformance timeLeavingOptional, String employeeID, GeneralDate day) {
		// ドメインモデル「打刻反映管理」を取得する
		Optional<StampReflectionManagement> stampReflectionManagement = this.stampReflectionManagementRepository
				.findByCid(companyId);

		// 自動打刻セットする - set new 自動打刻セット詳細
		// 自動打刻セット詳細をクリア
		AutomaticStampSetDetailOutput automaticStampSetDetailDto = new AutomaticStampSetDetailOutput();
		// ドメインモデル「個人労働条件」を取得する
		if (personalLaborHasData.get().getAutoStampSetAtr() == UseAtr.USE) {
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

						// 出勤系時刻を丸める (làm tròn thời gian 出勤)
						// param : int workTimeMethodSet, String companyId,
						// String siftCode, int superitory
						// param : 0, companyId,
						// workInfoOfDailyPerformanceUpdate.getRecordWorkInformation().getWorkTimeCode(),
						// superitory
						// TODO - requetsList newwave
						// InstantRounding instantRounding = new
						// InstantRounding();
						// int attendanceTimeAfterRouding =
						// this.roudingTime(sheet.getAttendance().v(),
						// instantRounding.getFontRearSection().value,
						// instantRounding.getRoundingTimeUnit().value);
						// int leaveTimeAfterRounding =
						// this.roudingTime(sheet.getLeaveWork().v(),
						// instantRounding.getFontRearSection().value,
						// instantRounding.getRoundingTimeUnit().value);

						// ドメインモデル「所属職場履歴」を取得する
						attendanceStampTemp.setStamp(new WorkStampOutPut(new TimeWithDayAttr(sheet.getAttendance().v()),
								sheet.getAttendance(), new WorkLocationCD("0001"),
								automaticStampSetDetailDto.getAttendanceStamp()));
						leaveStampTemp.setStamp(
								new WorkStampOutPut(new TimeWithDayAttr(sheet.getLeaveWork().v()), sheet.getLeaveWork(),
										new WorkLocationCD("0001"), automaticStampSetDetailDto.getLeavingStamp()));
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
						PredetemineTimeSetting predetemineTimeSetting = predetemineTimeSettingRepository
								.findByWorkTimeCode(companyId, workInfoOfDailyPerformanceUpdate
										.getRecordWorkInformation().getWorkTypeCode().v());

						if (predetemineTimeSetting != null) {
							List<TimezoneUse> lstTimezone = predetemineTimeSetting.getPrescribedTimezoneSetting()
									.getLstTimezone();
							for (TimezoneUse timezone : lstTimezone) {
								if (timezone.getUseAtr() == UseSetting.USE) {
									TimeLeavingWorkOutput timeLeavingWorkOutput = new TimeLeavingWorkOutput();
									timeLeavingWorkOutput.setWorkNo(new WorkNo(new BigDecimal(timezone.getWorkNo())));

									TimeActualStampOutPut attendanceTimeActualStampOutPut = new TimeActualStampOutPut();
									WorkStampOutPut actualStamp = new WorkStampOutPut();
									actualStamp.setTimeWithDay(timezone.getStart());

									TimeActualStampOutPut leaveTimeActualStampOutPut = new TimeActualStampOutPut();
									WorkStampOutPut leaveActualStamp = new WorkStampOutPut();
									leaveActualStamp.setTimeWithDay(timezone.getEnd());

									// 出勤系時刻を丸める - TODO - waiting new wave
									
									Optional<AffWorkplaceDto> affWorkplaceDto = this.affWorkplaceAdapter.findBySid(employeeID, day);
									
									if (affWorkplaceDto.isPresent()) {
										actualStamp.setLocationCode(new WorkLocationCD(affWorkplaceDto.get().getWorkplaceCode()));
										leaveActualStamp.setLocationCode(new WorkLocationCD(affWorkplaceDto.get().getWorkplaceCode()));
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
				WorkStamp actualStamp = null;
				if(item.getAttendanceStamp().getActualStamp() != null){
					actualStamp = new WorkStamp(item.getAttendanceStamp().getActualStamp().getAfterRoundingTime(),
							item.getAttendanceStamp().getActualStamp().getTimeWithDay(),
							item.getAttendanceStamp().getActualStamp().getLocationCode(),
							item.getAttendanceStamp().getActualStamp().getStampSourceInfo());
				}
				
				WorkStamp stamp = new WorkStamp(item.getAttendanceStamp().getStamp().getAfterRoundingTime(),
						item.getAttendanceStamp().getStamp().getTimeWithDay(),
						item.getAttendanceStamp().getStamp().getLocationCode(),
						item.getAttendanceStamp().getStamp().getStampSourceInfo());
				TimeActualStamp attendanceStamp = new TimeActualStamp(actualStamp, stamp,
						item.getAttendanceStamp().getNumberOfReflectionStamp());
				
				WorkStamp leaveActualStampTemp= null;
				if(item.getLeaveStamp().getActualStamp() != null){
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

				TimeActualStamp leaveStamp = new TimeActualStamp(leaveActualStampTemp, leaveStampTemp,
						item.getLeaveStamp().getNumberOfReflectionStamp());

				return new TimeLeavingWork(item.getWorkNo(), attendanceStamp, leaveStamp);
			}).collect(Collectors.toList());
			automaticStampSetDetailDto.setTimeLeavingWorks(timeLeavingWorks);

			Calendar toDay = Calendar.getInstance();
			int hour = toDay.get(Calendar.HOUR_OF_DAY);
			int minute = toDay.get(Calendar.MINUTE);
			int currentMinuteOfDay = ((hour * 60) + minute);

			// 出勤反映 = true
			// 出勤に自動打刻セットする
			if (automaticStampSetDetailDto.getAttendanceReflectAttr() == UseAtr.USE) {

				List<TimeLeavingWork> timeLeavingWorkList = new ArrayList<>();
				List<TimeLeavingWorkOutput> timeLeavingWorkOutputs = new ArrayList<>();

				// ドメインモデル「日別実績の出退勤」を取得する
				// 自動打刻セット詳細．出退勤を順次確認する
				automaticStampSetDetailDto.getTimeLeavingWorks().stream().forEach(timeLeaving -> {
					TimeLeavingWork stamp = null;
					if (timeLeavingOptional.getTimeLeavingWorks() != null) {
						stamp = timeLeavingOptional.getTimeLeavingWorks().stream()
								.filter(itemx -> itemx.getWorkNo().v().equals(timeLeaving.getWorkNo().v())).findFirst()
								.get();
					}

					if (stampReflectionManagement.get()
							.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.SET_AUTO_STAMP
							|| (stampReflectionManagement.get()
									.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.DO_NOT_SET_AUTO_STAMP
									&& timeLeaving.getAttendanceStamp().getStamp().getTimeWithDay()
											.lessThanOrEqualTo(currentMinuteOfDay))) {
						TimeLeavingWorkOutput outPut = new TimeLeavingWorkOutput();
						// 勤務NOが同じ実績．出退勤を確認する
						// 存在しない
						if (timeLeavingOptional.getTimeLeavingWorks() == null
								|| (timeLeavingOptional.getTimeLeavingWorks() != null
										&& !timeLeavingOptional.getTimeLeavingWorks().stream().anyMatch(
												item -> item.getWorkNo().v() == timeLeaving.getWorkNo().v()))) {

							// 実績．出退勤．出勤．打刻←詳細．出退勤．出勤．打刻
							TimeActualStampOutPut actualStampOutPut = new TimeActualStampOutPut();
							WorkStampOutPut actualStampTemp = new WorkStampOutPut(
									timeLeaving.getAttendanceStamp().getStamp().getAfterRoundingTime(),
									timeLeaving.getAttendanceStamp().getStamp().getTimeWithDay(),
									timeLeaving.getAttendanceStamp().getStamp().getLocationCode(),
									timeLeaving.getAttendanceStamp().getStamp().getStampSourceInfo());
							actualStampOutPut.setStamp(actualStampTemp);
							outPut.setWorkNo(timeLeaving.getWorkNo());
							outPut.setAttendanceStamp(actualStampOutPut);

							timeLeavingWorkOutputs.add(outPut);

							// this.lateCorrection(timeLeavingOptional.get().getTimeLeavingWorks().stream()
							// .filter(item ->
							// item.getWorkNo().equals(timeLeaving.getWorkNo())).findFirst().get()
							// .getAttendanceStamp());
						}
						// 存在する && 入っていない
						if ((timeLeavingOptional.getTimeLeavingWorks() != null
								&& timeLeavingOptional.getTimeLeavingWorks().stream()
										.anyMatch(item -> item.getWorkNo().v() == timeLeaving.getWorkNo().v()))
								&& (stamp != null && (stamp.getLeaveStamp() == null || (stamp.getLeaveStamp() != null
										&& stamp.getLeaveStamp().getStamp() == null)))) {

							TimeActualStamp leaveStamp = stamp.getLeaveStamp();
							WorkNo workNo = stamp.getWorkNo();
							int numberOfReflectionStamp = stamp.getAttendanceStamp().getNumberOfReflectionStamp();
							WorkStamp actualStampTemp = stamp.getAttendanceStamp().getActualStamp();
							WorkStamp stampTemp = new WorkStamp(
									timeLeaving.getAttendanceStamp().getStamp().getAfterRoundingTime(),
									timeLeaving.getAttendanceStamp().getStamp().getTimeWithDay(),
									timeLeaving.getAttendanceStamp().getStamp().getLocationCode(),
									timeLeaving.getAttendanceStamp().getStamp().getStampSourceInfo());

							TimeActualStamp attendanceStamp = new TimeActualStamp(actualStampTemp, stampTemp,
									numberOfReflectionStamp);

							stamp = new TimeLeavingWork(workNo, attendanceStamp, leaveStamp);
							
							TimeLeavingWork timeLeavingWorkOld = timeLeavingOptional.getTimeLeavingWorks().stream()
									.filter(itemx -> itemx.getWorkNo().v().equals(timeLeaving.getWorkNo().v())).findFirst()
									.get();
							timeLeavingWorkOld.setTimeLeavingWork(workNo, attendanceStamp, leaveStamp);
//							timeLeavingWorkOutputs.add(timeLeavingWorkOld);
							// this.lateCorrection(timeLeavingOptional.get().getTimeLeavingWorks().stream()
							// .filter(item ->
							// item.getWorkNo().equals(timeLeaving.getWorkNo())).findFirst().get()
							// .getAttendanceStamp());
						}
					}
					;
				});
				timeLeavingWorkList = timeLeavingWorkOutputs.stream().map(item -> {
					WorkStamp actualStamp = null;
					if(item.getAttendanceStamp().getActualStamp() != null){
						actualStamp = new WorkStamp(
								item.getAttendanceStamp().getActualStamp().getAfterRoundingTime(),
								item.getAttendanceStamp().getActualStamp().getTimeWithDay(),
								item.getAttendanceStamp().getActualStamp().getLocationCode(),
								item.getAttendanceStamp().getActualStamp().getStampSourceInfo());
					}
					
					WorkStamp workStampTemp = new WorkStamp(item.getAttendanceStamp().getStamp().getAfterRoundingTime(),
							item.getAttendanceStamp().getStamp().getTimeWithDay(),
							item.getAttendanceStamp().getStamp().getLocationCode(),
							item.getAttendanceStamp().getStamp().getStampSourceInfo());
					TimeActualStamp attendanceStamp = new TimeActualStamp(actualStamp, workStampTemp,
							item.getAttendanceStamp().getNumberOfReflectionStamp());

					WorkStamp leaveActualStampTemp = null;
					if(item.getLeaveStamp().getActualStamp() != null){
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

					TimeActualStamp leaveStamp = new TimeActualStamp(leaveActualStampTemp, leaveStampTemp,
							item.getLeaveStamp().getNumberOfReflectionStamp());

					return new TimeLeavingWork(item.getWorkNo(), attendanceStamp, leaveStamp);
				}).collect(Collectors.toList());
				timeLeavingOptional.setTimeLeavingWorks(timeLeavingWorkList);

			}

			// 退勤反映 = true
			if (automaticStampSetDetailDto.getRetirementAttr() == UseAtr.USE) {

				List<TimeLeavingWork> timeLeavingWorkLst = new ArrayList<>();
				List<TimeLeavingWorkOutput> newTimeLeavingWorkOutputs = new ArrayList<>();

				automaticStampSetDetailDto.getTimeLeavingWorks().stream().forEach(timeLeavingWork -> {

					TimeLeavingWork stamp = null;
					if (!timeLeavingOptional.getTimeLeavingWorks().isEmpty()) {
						stamp = timeLeavingOptional.getTimeLeavingWorks().stream()
								.filter(itemm -> itemm.getWorkNo().v().equals(timeLeavingWork.getWorkNo().v()))
								.findAny().get();
					}

					if (stampReflectionManagement.get()
							.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.SET_AUTO_STAMP
							|| (stampReflectionManagement.get()
									.getAutoStampForFutureDayClass() == AutoStampForFutureDayClass.DO_NOT_SET_AUTO_STAMP
									&& timeLeavingWork.getLeaveStamp().getStamp().getTimeWithDay()
											.lessThanOrEqualTo(currentMinuteOfDay))) {

						TimeLeavingWork timeLeaving = new TimeLeavingWork();
						TimeLeavingWorkOutput newOutPut = new TimeLeavingWorkOutput();
						// 勤務NOが同じ実績．出退勤を確認する
						// 存在しない
						if (timeLeavingOptional.getTimeLeavingWorks() != null
								&& !timeLeavingOptional.getTimeLeavingWorks().stream()
										.anyMatch(item -> item.getWorkNo().v() == timeLeavingWork.getWorkNo().v())) {

							// 実績．出退勤．出勤．打刻←詳細．出退勤．出勤．打刻

							TimeActualStampOutPut leaveActualStampOutPut = new TimeActualStampOutPut();
							WorkStampOutPut leaveActualStampTemp = new WorkStampOutPut(
									timeLeaving.getLeaveStamp().getStamp().getAfterRoundingTime(),
									timeLeaving.getLeaveStamp().getStamp().getTimeWithDay(),
									timeLeaving.getLeaveStamp().getStamp().getLocationCode(),
									timeLeaving.getLeaveStamp().getStamp().getStampSourceInfo());
							leaveActualStampOutPut.setStamp(leaveActualStampTemp);
							newOutPut.setWorkNo(timeLeaving.getWorkNo());
							newOutPut.setLeaveStamp(leaveActualStampOutPut);

							newTimeLeavingWorkOutputs.add(newOutPut);

							// this.lateCorrection(timeLeavingOptional.get().getTimeLeavingWorks().stream()
							// .filter(item ->
							// item.getWorkNo().equals(timeLeaving.getWorkNo())).findFirst().get()
							// .getLeaveStamp());
						}
						// 存在する && 入っていない
						if ((timeLeavingOptional.getTimeLeavingWorks() != null
								&& timeLeavingOptional.getTimeLeavingWorks().stream()
										.anyMatch(item -> item.getWorkNo().v() == timeLeavingWork.getWorkNo().v()))
								&& (stamp != null && (stamp.getLeaveStamp() == null || (stamp.getLeaveStamp() != null
										&& stamp.getLeaveStamp().getStamp() == null)))) {

							TimeActualStamp attendanceStamp = stamp.getAttendanceStamp();
							WorkNo workNo = stamp.getWorkNo();

							int numberOfReflectionStamp = stamp.getLeaveStamp().getNumberOfReflectionStamp();
							WorkStamp leaveActualStampTemp = stamp.getLeaveStamp().getActualStamp();
							WorkStamp leaveStampTemp = new WorkStamp(
									timeLeaving.getLeaveStamp().getStamp().getAfterRoundingTime(),
									timeLeaving.getLeaveStamp().getStamp().getTimeWithDay(),
									timeLeaving.getLeaveStamp().getStamp().getLocationCode(),
									timeLeaving.getLeaveStamp().getStamp().getStampSourceInfo());

							TimeActualStamp leaveStamp = new TimeActualStamp(leaveActualStampTemp, leaveStampTemp,
									numberOfReflectionStamp);

//							stamp = new TimeLeavingWork(workNo, attendanceStamp, leaveStamp);
							
							TimeLeavingWork timeLeavingWorkOld = timeLeavingOptional.getTimeLeavingWorks().stream()
									.filter(itemm -> itemm.getWorkNo().v().equals(timeLeavingWork.getWorkNo().v()))
									.findAny().get();
							timeLeavingWorkOld.setTimeLeavingWork(workNo, attendanceStamp, leaveStamp);
							
							// timeLeavingOptional.getTimeLeavingWorks().stream()
							// .filter(item ->
							// item.getWorkNo().equals(timeLeaving.getWorkNo())).findFirst().get()
							// .getLeaveStamp().setStamp(timeLeaving.getLeaveStamp().getStamp());
							// this.lateCorrection(timeLeavingOptional.get().getTimeLeavingWorks().stream()
							// .filter(item ->
							// item.getWorkNo().equals(timeLeaving.getWorkNo())).findFirst().get()
							// .getLeaveStamp());
						}
					}
				});
				timeLeavingWorkLst = newTimeLeavingWorkOutputs.stream().map(item -> {
					WorkStamp actualStamp = new WorkStamp(
							item.getAttendanceStamp().getActualStamp().getAfterRoundingTime(),
							item.getAttendanceStamp().getActualStamp().getTimeWithDay(),
							item.getAttendanceStamp().getActualStamp().getLocationCode(),
							item.getAttendanceStamp().getActualStamp().getStampSourceInfo());
					WorkStamp workStampTemp = new WorkStamp(item.getAttendanceStamp().getStamp().getAfterRoundingTime(),
							item.getAttendanceStamp().getStamp().getTimeWithDay(),
							item.getAttendanceStamp().getStamp().getLocationCode(),
							item.getAttendanceStamp().getStamp().getStampSourceInfo());
					TimeActualStamp attendanceStamp = new TimeActualStamp(actualStamp, workStampTemp,
							item.getAttendanceStamp().getNumberOfReflectionStamp());

					WorkStamp leaveActualStampTemp = new WorkStamp(
							item.getLeaveStamp().getActualStamp().getAfterRoundingTime(),
							item.getLeaveStamp().getActualStamp().getTimeWithDay(),
							item.getLeaveStamp().getActualStamp().getLocationCode(),
							item.getLeaveStamp().getActualStamp().getStampSourceInfo());
					WorkStamp leaveStampTemp = new WorkStamp(item.getLeaveStamp().getStamp().getAfterRoundingTime(),
							item.getLeaveStamp().getStamp().getTimeWithDay(),
							item.getLeaveStamp().getStamp().getLocationCode(),
							item.getLeaveStamp().getStamp().getStampSourceInfo());

					TimeActualStamp leaveStamp = new TimeActualStamp(leaveActualStampTemp, leaveStampTemp,
							item.getLeaveStamp().getNumberOfReflectionStamp());

					return new TimeLeavingWork(item.getWorkNo(), attendanceStamp, leaveStamp);
				}).collect(Collectors.toList());
				timeLeavingOptional.setTimeLeavingWorks(timeLeavingWorkLst);
			}
		}
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
