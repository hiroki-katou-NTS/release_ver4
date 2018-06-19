package nts.uk.ctx.at.request.app.find.application.holidayshipment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.app.find.application.applicationlist.AppTypeSetDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.AppEmploymentSettingDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationSettingDto;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.HolidayShipmentDto;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.TimeZoneUseDto;
import nts.uk.ctx.at.request.app.find.application.holidayshipment.dto.WorkTimeInfoDto;
import nts.uk.ctx.at.request.app.find.setting.applicationreason.ApplicationReasonDto;
import nts.uk.ctx.at.request.app.find.setting.company.applicationapprovalsetting.withdrawalrequestset.WithDrawalReqSetDto;
import nts.uk.ctx.at.request.app.find.setting.workplace.ApprovalFunctionSettingDto;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.EmploymentRootAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.EmploymentHistoryImported;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.before.BeforePrelaunchAppCommonSet;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.holidayshipment.ApplicationCombination;
import nts.uk.ctx.at.request.dom.application.holidayshipment.BreakOutType;
import nts.uk.ctx.at.request.dom.setting.applicationreason.ApplicationReasonRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset.WithDrawalReqSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.withdrawalrequestset.WithDrawalReqSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSetting;
import nts.uk.ctx.at.request.dom.setting.company.request.RequestSettingRepository;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSetting;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.RequestOfEachCompanyRepository;
import nts.uk.ctx.at.request.dom.setting.workplace.RequestOfEachWorkplaceRepository;
import nts.uk.ctx.at.shared.app.find.worktype.WorkTypeDto;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.ComSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.subst.EmpSubstVacationRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.TimezoneUse;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.AttendanceHolidayAttr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author Sonnlb
 *
 */
@Stateless
public class HolidayShipmentScreenAFinder {

	@Inject
	private BeforePrelaunchAppCommonSet beforePrelaunchAppCommonSet;
	@Inject
	private WorkplaceAdapter wkPlaceAdapter;
	@Inject
	private EmpSubstVacationRepository empSubrepo;
	@Inject
	private ComSubstVacationRepository comSubrepo;
	@Inject
	private WithDrawalReqSetRepository withDrawRepo;
	@Inject
	private ApplicationReasonRepository appResonRepo;
	@Inject
	private EmployeeRequestAdapter empAdaptor;
	@Inject
	private WorkTypeRepository wkTypeRepo;
	@Inject
	private WorkTimeSettingRepository wkTimeRepo;
	@Inject
	private PredetemineTimeSettingRepository preTimeSetRepo;
	@Inject
	private CollectAchievement collectAchievement;
	@Inject
	private ApprovalRootAdapter rootAdapter;
	@Inject
	private RequestOfEachWorkplaceRepository requestWpRepo;
	@Inject
	private RequestOfEachCompanyRepository requestComRepo;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	@Inject
	private BasicScheduleService basicService;
	@Inject
	private RequestSettingRepository reqSetRepo;
	@Inject
	private WorkingConditionRepository wkingCondRepo;
	@Inject
	private WorkingConditionItemRepository wkingCondItemRepo;
	@Inject
	private WorkTimeSettingRepository wkTimeSetRepo;
	private final ApplicationType appType = ApplicationType.COMPLEMENT_LEAVE_APPLICATION;

	/**
	 * start event
	 * 
	 * @param employeeID
	 * @param initDateInput
	 * @param uiType
	 * @return HolidayShipmentDto
	 */
	// Screen A Start
	public HolidayShipmentDto startPageA(String employeeID, GeneralDate initDate, int uiType) {
		employeeID = employeeID == null ? AppContexts.user().employeeId() : employeeID;
		String companyID = AppContexts.user().companyId();

		AppCommonSettingOutput appCommonSettingOutput = getAppCommonSet(companyID, employeeID, initDate);
		// アルゴリズム「起動前共通処理（新規）」を実行する
		HolidayShipmentDto result = commonProcessBeforeStart(appType, companyID, employeeID, initDate,
				appCommonSettingOutput);

		GeneralDate refDate = result.getRefDate();

		// アルゴリズム「事前事後区分の判断」を実行する
		result.setPreOrPostType(
				otherCommonAlgorithm.judgmentPrePostAtr(appType, refDate, uiType == 0 ? true : false).value);

		// アルゴリズム「社員の労働条件を取得する」を実行する
		Optional<WorkingConditionItem> wkingItem = getWorkingCondition(companyID, employeeID, refDate);

		String wkTimeCD = getWkTimeCD(wkingItem);

		result.setWkTimeCD(wkTimeCD);

		GeneralDate appDate, deadDate;

		String takingOutWkTypeCD, takingOutWkTimeCD, holiDayWkTypeCD, holidayWkTimeCD;

		appDate = deadDate = null;

		takingOutWkTypeCD = takingOutWkTimeCD = holiDayWkTypeCD = holidayWkTimeCD = null;

		// アルゴリズム「振休振出申請起動時の共通処理」を実行する
		commonProcessAtStartup(companyID, employeeID, refDate, appDate, takingOutWkTypeCD, takingOutWkTimeCD, deadDate,
				holiDayWkTypeCD, holidayWkTimeCD, result, appCommonSettingOutput);
		// アルゴリズム「勤務時間初期値の取得」を実行する
		String wkTypeCD = result.getRecWkTypes().size() > 0 ? result.getRecWkTypes().get(0).getWorkTypeCode() : "";
		setWorkTimeInfo(result, wkTimeCD, wkTypeCD, companyID);

		return result;
	}

	private Optional<WorkingConditionItem> getWorkingCondition(String companyID, String employeeID,
			GeneralDate refDate) {
		List<WorkingConditionItem> wkingItems = new ArrayList<WorkingConditionItem>();
		wkingCondRepo.getBySidAndStandardDate(companyID, employeeID, refDate).ifPresent(wkCond -> {
			wkCond.getDateHistoryItem().forEach(hisItem -> {
				wkingCondItemRepo.getByHistoryId(hisItem.identifier()).ifPresent(wkItem -> {
					wkingItems.add(wkItem);
				});
				;
			});
		});
		return CollectionUtil.isEmpty(wkingItems) ? Optional.empty() : Optional.of(wkingItems.get(0));
	}

	public AppCommonSettingOutput getAppCommonSet(String companyID, String employeeID, GeneralDate initDate) {
		int rootAtr = 1;
		// 1-1.新規画面起動前申請共通設定を取得する
		return beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID, employeeID, rootAtr, appType,
				initDate);
	}

	private void setWorkTimeInfo(HolidayShipmentDto result, String wkTimeCD, String wkTypeCD, String companyID) {
		if (wkTimeCD != null) {
			if (StringUtils.isNoneEmpty(wkTypeCD)) {
				result.setWorkTimeInfo(getWkTimeInfoInitValue(companyID, wkTypeCD, wkTimeCD));
			}

		}

	}

	public WorkTimeInfoDto changeWorkType(String workTypeCD, String wkTimeCD) {
		String companyID = AppContexts.user().companyId();
		return getWkTimeInfoInitValue(companyID, workTypeCD, wkTimeCD);

	}

	public HolidayShipmentDto changeAppDate(GeneralDate recDate, GeneralDate absDate, int comType, int uiType) {
		String companyID = AppContexts.user().companyId();
		String employeeID = AppContexts.user().employeeId();
		GeneralDate baseDate = comType == ApplicationCombination.Abs.value ? absDate : recDate;
		AppCommonSettingOutput appCommonSettingOutput = getAppCommonSet(companyID, employeeID, baseDate);
		HolidayShipmentDto output = commonProcessBeforeStart(appType, companyID, employeeID, baseDate,
				appCommonSettingOutput);
		// アルゴリズム「実績の取得」を実行する
		// AchievementOutput achievementOutput = getAchievement(companyID,
		// employeeID, baseDate);
		// アルゴリズム「申請日の変更」を実行する
		setChangeAppDateData(recDate, absDate, companyID, employeeID, uiType, output);

		return output;
	}

	public WorkTimeInfoDto getSelectedWorkingHours(String wkTypeCD, String wkTimeCD) {
		String companyID = AppContexts.user().companyId();
		return getWkTimeInfoInitValue(companyID, wkTypeCD, wkTimeCD);
	}

	private String getWkTimeCD(Optional<WorkingConditionItem> wkingItem) {
		StringBuilder builder = new StringBuilder();
		builder.append("");

		wkingItem.ifPresent(item -> {
			item.getWorkCategory().getWeekdayTime().getWorkTimeCode().ifPresent(wkIimeCd -> {
				builder.append(wkIimeCd);
			});
		});

		return builder.toString();
	}

	private void setChangeAppDateData(GeneralDate recDate, GeneralDate absDate, String companyID, String employeeID,
			int uiType, HolidayShipmentDto output) {
		// アルゴリズム「基準申請日の決定」を実行する
		GeneralDate referenceDate = DetRefDate(recDate, absDate);
		int rootAtr = EmploymentRootAtr.APPLICATION.value;
		AppCommonSettingOutput appCommonSet = beforePrelaunchAppCommonSet.prelaunchAppCommonSetService(companyID,
				employeeID, rootAtr, appType, referenceDate);

		ApplicationSetting appSet = appCommonSet.applicationSetting;
		// 承認ルート基準日をチェックする
		GeneralDate inputDate;
		if (appSet.getBaseDateFlg().equals(BaseDateFlg.APP_DATE)) {

			inputDate = referenceDate;
		} else {
			inputDate = GeneralDate.today();
		}
		// アルゴリズム「社員の対象申請の承認ルートを取得する」を実行する
		List<ApprovalRootImport> approvalRoots = rootAdapter.getApprovalRootOfSubjectRequest(companyID, employeeID,
				rootAtr, appType.value, referenceDate);
		boolean getSetting = true;
		String recWkTypeCD, recWkTimeCode, absWkTypeCD, absWkTimeCode;
		recWkTypeCD = recWkTimeCode = absWkTypeCD = absWkTimeCode = null;
		// アルゴリズム「基準日別設定の取得」を実行する
		setDateSpecificSetting(companyID, employeeID, inputDate, getSetting, recWkTypeCD, recWkTimeCode, absWkTypeCD,
				absWkTimeCode, appCommonSet, output);
		// アルゴリズム「事前事後区分の最新化」を実行する
		output.setPreOrPostType(
				otherCommonAlgorithm.judgmentPrePostAtr(appType, referenceDate, uiType == 0 ? true : false).value);

		output.setRefDate(inputDate);

	}

	public static GeneralDate DetRefDate(GeneralDate recDate, GeneralDate absDate) {
		boolean isBothDateNotNull = absDate != null && recDate != null;
		GeneralDate resultDate = null;

		if (isBothDateNotNull) {
			boolean isRecDateAfterAbsDate = recDate.after(absDate);
			if (isRecDateAfterAbsDate) {
				resultDate = absDate;
			} else {
				resultDate = recDate;
			}
		} else {
			if (recDate != null) {
				resultDate = recDate;
			}
			if (absDate != null) {
				resultDate = absDate;
			}
		}
		return resultDate;

	}

	private WorkTimeInfoDto getWkTimeInfoInitValue(String companyID, String wkTypeCode, String wkTimeCode) {
		// ドメインモデル「勤務種類」を取得する
		WorkTimeInfoDto result = new WorkTimeInfoDto();
		WorkType wkType = null;
		Optional<WorkType> wkTypeOpt = wkTypeRepo.findByPK(companyID, wkTypeCode);
		if (wkTypeOpt.isPresent()) {
			wkType = wkTypeOpt.get();

			result.setWkType(WorkTypeDto.fromDomain(wkType));
			// アルゴリズム「1日半日出勤・1日休日系の判定」を実行する
			basicService.checkWorkDay(wkTypeCode);

		}
		if (wkType != null) {

			setWkTimeZones(wkType.getAttendanceHolidayAttr(), companyID, wkTimeCode, result);

		}
		return result;

	}

	private void setWkTimeZones(AttendanceHolidayAttr wkTypeAttendance, String companyID, String wkTimeCode,
			WorkTimeInfoDto result) {

		if (!wkTypeAttendance.equals(AttendanceHolidayAttr.HOLIDAY)) {

			// アルゴリズム「所定時間帯を取得する」を実行する
			List<TimezoneUse> timeZones = getTimeZones(companyID, wkTimeCode, wkTypeAttendance);

			result.setTimezoneUseDtos(
					timeZones.stream().map(x -> TimeZoneUseDto.fromDomain(x)).collect(Collectors.toList()));

		}

	}

	@SuppressWarnings("incomplete-switch")
	private List<TimezoneUse> getTimeZones(String companyID, String wkTimeCode,
			AttendanceHolidayAttr wkTypeAttendance) {

		List<TimezoneUse> timeZones = new ArrayList<TimezoneUse>();

		// ○就業時間帯を取得
		Optional<WorkTimeSetting> workTimeOpt = wkTimeRepo.findByCode(companyID, wkTimeCode);

		if (workTimeOpt.isPresent()) {

			wkTimeCode = workTimeOpt.get().getWorktimeCode().v();
			// ○所定時間帯を取得
			Optional<PredetemineTimeSetting> preTimeSetOpt = preTimeSetRepo.findByWorkTimeCode(companyID, wkTimeCode);

			if (preTimeSetOpt.isPresent()) {

				PredetemineTimeSetting preTimeSet = preTimeSetOpt.get();

				List<TimezoneUse> timeZonesInPreTimeSet = preTimeSet.getPrescribedTimezoneSetting().getLstTimezone();

				switch (wkTypeAttendance) {
				case MORNING:
					timeZonesInPreTimeSet.stream().forEach(
							x -> x.updateEndTime(preTimeSet.getPrescribedTimezoneSetting().getMorningEndTime()));
					break;
				case AFTERNOON:
					timeZonesInPreTimeSet.stream().forEach(
							x -> x.updateStartTime(preTimeSet.getPrescribedTimezoneSetting().getAfternoonStartTime()));
					break;
				}

				timeZones = timeZonesInPreTimeSet;
			}

		}
		return timeZones;
	}

	public void commonProcessAtStartup(String companyID, String employeeID, GeneralDate refDate, GeneralDate recDate,
			String recWkTypeCD, String recWkTimeCD, GeneralDate absDate, String absWkTypeCD, String absWkTimeCD,
			HolidayShipmentDto output, AppCommonSettingOutput appSetOutput) {
		// アルゴリズム「振休振出申請設定の取得」を実行する
		setDrawReqSet(output);

		// アルゴリズム「振休振出申請定型理由の取得」を実行する

		output.setAppReasonComboItems(appResonRepo.getReasonByAppType(companyID, appType.value).stream()
				.map(x -> ApplicationReasonDto.convertToDto(x)).collect(Collectors.toList()));

		// アルゴリズム「基準日別設定の取得」を実行する
		setDateSpecificSetting(companyID, employeeID, refDate, false, recWkTypeCD, recWkTimeCD, absWkTypeCD,
				absWkTimeCD, appSetOutput, output);
		// アルゴリズム「実績の取得」を実行する
		getAchievement(companyID, employeeID, recDate);
		// アルゴリズム「実績の取得」を実行する
		getAchievement(companyID, employeeID, absDate);

	}

	private void setDrawReqSet(HolidayShipmentDto output) {
		Optional<WithDrawalReqSet> withDrawalReqSetOpt = withDrawRepo.getWithDrawalReqSet();
		if (withDrawalReqSetOpt.isPresent()) {
			output.setDrawalReqSet(WithDrawalReqSetDto.fromDomain(withDrawalReqSetOpt.get()));
		}

	}

	private AchievementOutput getAchievement(String companyID, String employeeID, GeneralDate appDate) {
		// アルゴリズム「実績取得」を実行する
		if (appDate != null) {
			return collectAchievement.getAchievement(companyID, employeeID, appDate);
		}
		return null;

	}

	public void setDateSpecificSetting(String companyID, String employeeID, GeneralDate refDate, boolean isGetSetting,
			String recWkTypeCD, String recWkTimeCD, String absWkTypeCD, String absWkTimeCD,
			AppCommonSettingOutput appCommonSet, HolidayShipmentDto output) {
		// Imported(就業.shared.組織管理.社員情報.所属雇用履歴)「所属雇用履歴」を取得する
		Optional<EmploymentHistoryImported> empImpOpt = wkPlaceAdapter.getEmpHistBySid(companyID, employeeID, refDate);
		// アルゴリズム「所属職場を含む上位職場を取得」を実行する

		if (empImpOpt.isPresent()) {

			String employmentCD = empImpOpt.get().getEmploymentCode();

			if (isGetSetting) {
				// INPUT.設定取得＝true
				// アルゴリズム「雇用別申請承認設定の取得」を実行するz
				setAppEmploymentSettings(appCommonSet, employmentCD, output);

				// アルゴリズム「申請承認機能設定の取得」を実行する
				setApprovalFunctionSetting(employeeID, refDate, output, companyID);

			}
			// アルゴリズム「振出用勤務種類の取得」を実行する
			output.setRecWkTypes(
					getWorkTypeFor(companyID, employmentCD, recWkTypeCD, appCommonSet, BreakOutType.WORKING_DAY)
							.stream().map(x -> WorkTypeDto.fromDomain(x)).collect(Collectors.toList()));

			// アルゴリズム「選択済の就業時間帯の取得」を実行する rec
			setWkHourInfoForRecApp(companyID, recWkTimeCD, output);

			// アルゴリズム「選択済の就業時間帯の取得」を実行する abs
			output.setAbsWkTypes(
					getWorkTypeFor(companyID, employmentCD, absWkTimeCD, appCommonSet, BreakOutType.HOLIDAY).stream()
							.map(x -> WorkTypeDto.fromDomain(x)).collect(Collectors.toList()));

			// アルゴリズム「振休用勤務種類の取得」を実行する
			setWkHourInfoForAbsApp(companyID, absWkTimeCD, output);

		}

	}

	private void setWkHourInfoForAbsApp(String companyID, String WkTimeCD, HolidayShipmentDto output) {
		HolidayShipmentAppDto absAppOutPut = output.getAbsApp();
		setSelectedWkHourInfo(companyID, WkTimeCD, absAppOutPut);

	}

	private void setWkHourInfoForRecApp(String companyID, String WkTimeCD, HolidayShipmentDto output) {
		HolidayShipmentAppDto recAppOutPut = output.getRecApp();
		setSelectedWkHourInfo(companyID, WkTimeCD, recAppOutPut);

	}

	private void setApprovalFunctionSetting(String employeeID, GeneralDate refDate, HolidayShipmentDto output,
			String companyID) {
		List<String> workPlaceIds = empAdaptor.findWpkIdsBySid(companyID, employeeID, refDate);
		if (!CollectionUtil.isEmpty(workPlaceIds)) {
			output.setApprovalFunctionSetting(
					ApprovalFunctionSettingDto.convertToDto(AcApprovalFuncSet(companyID, workPlaceIds)));
		}
	}

	private void setAppEmploymentSettings(AppCommonSettingOutput appCommonSet, String employmentCD,
			HolidayShipmentDto output) {

		Optional<AppEmploymentSetting> appSetOpt = appCommonSet.appEmploymentWorkType.stream()
				.filter(x -> x.getEmploymentCode().equals(employmentCD)).findFirst();

		if (appSetOpt.isPresent()) {

			addAppSetToList(appSetOpt.get(), output);

		}

	}

	private void addAppSetToList(AppEmploymentSetting appSet, HolidayShipmentDto output) {

		boolean isAppSetsNotEmpty = !CollectionUtil.isEmpty(output.getAppEmploymentSettings());

		if (isAppSetsNotEmpty) {

			output.getAppEmploymentSettings().add(AppEmploymentSettingDto.fromDomain(appSet));

		} else {

			List<AppEmploymentSettingDto> newAppSets = new ArrayList<AppEmploymentSettingDto>();

			newAppSets.add(AppEmploymentSettingDto.fromDomain(appSet));

			output.setAppEmploymentSettings(newAppSets);

		}

	}

	private void setSelectedWkHourInfo(String companyID, String wkTimeCode, HolidayShipmentAppDto appOut) {
		// アルゴリズム「就業時間帯表示情報（単体）の取得」を実行する
		boolean isTimeCdAndAppDtoNotNull = wkTimeCode != null && appOut != null;
		if (isTimeCdAndAppDtoNotNull) {
			boolean isGetHiddenItems = true;
			setWkTimeZoneDisplayInfo(companyID, wkTimeCode, isGetHiddenItems, appOut);
		}

	}

	private void setWkTimeZoneDisplayInfo(String companyID, String wkTimeCode, boolean isGetHiddenItems,
			HolidayShipmentAppDto appOut) {
		if (isGetHiddenItems) {
			Optional<WorkTimeSetting> wkTimeOpt = this.wkTimeSetRepo.findByCode(companyID, wkTimeCode);

			if (wkTimeOpt.isPresent()) {

				appOut.updateFromWkTimeSet(wkTimeOpt.get());

				wkTimeCode = wkTimeOpt.get().getWorktimeCode().v();

				Optional<PredetemineTimeSetting> preTimeSetOpt = preTimeSetRepo.findByWorkTimeCode(companyID,
						wkTimeCode);
				if (preTimeSetOpt.isPresent()) {
					appOut.updateFromPreTimeSet(preTimeSetOpt.get());
				}
			}
		}

	}

	private ApprovalFunctionSetting AcApprovalFuncSet(String companyID, List<String> wpkIds) {
		ApprovalFunctionSetting result = null;
		for (String wpID : wpkIds) {
			Optional<ApprovalFunctionSetting> wpOpt = requestWpRepo.getFunctionSetting(companyID, wpID, appType.value);
			if (wpOpt.isPresent()) {
				result = wpOpt.get();
			}
		}
		// 職場別設定なし
		Optional<ApprovalFunctionSetting> comOpt = requestComRepo.getFunctionSetting(companyID, appType.value);
		if (comOpt.isPresent()) {
			result = comOpt.get();
		}
		return result;

	}

	private List<WorkType> getWorkTypeFor(String companyID, String employmentCode, String wkTypeCD,
			AppCommonSettingOutput appCommonSet, BreakOutType workType) {

		List<WorkType> unfilteredWkTypes;
		boolean isWorkTypeIsHoliday = workType.equals(BreakOutType.HOLIDAY);
		if (isWorkTypeIsHoliday) {
			// ドメインモデル「勤務種類」を取得する
			unfilteredWkTypes = wkTypeRepo.findWorkTypeForPause(companyID);
		} else {
			// ドメインモデル「勤務種類」を取得する
			unfilteredWkTypes = wkTypeRepo.findWorkTypeForShorting(companyID);
		}
		// アルゴリズム「対象勤務種類の抽出」を実行する
		List<WorkType> outputWkTypes = extractTargetWkTypes(companyID, employmentCode, workType.value,
				unfilteredWkTypes, appCommonSet);

		boolean isWkTypeCDNotNullOrEmpty = !StringUtils.isEmpty(wkTypeCD);

		if (isWkTypeCDNotNullOrEmpty) {
			// アルゴリズム「申請済み勤務種類の存在判定と取得」を実行する
			appliedWorkType(companyID, outputWkTypes, wkTypeCD);

		}
		// sort by CD
		outputWkTypes.sort(Comparator.comparing(WorkType::getWorkTypeCode));
		return outputWkTypes;

	}

	public boolean appliedWorkType(String companyID, List<WorkType> wkTypes, String wkTypeCD) {
		boolean masterUnregistered = true;

		Optional<WorkType> WkTypeOpt = wkTypeRepo.findByPK(companyID, wkTypeCD);
		boolean isWkTypeNotExistedInList = WkTypeOpt.isPresent() && !wkTypes.contains((WkTypeOpt.get()));

		if (isWkTypeNotExistedInList) {
			wkTypes.add(WkTypeOpt.get());

			masterUnregistered = false;
		}
		return masterUnregistered;

	}

	private List<WorkType> extractTargetWkTypes(String companyID, String employmentCode, int breakOutType,
			List<WorkType> wkTypes, AppCommonSettingOutput appCommonSet) {

		// ドメインモデル「申請別対象勤務種類」を取得する
		Optional<AppEmploymentSetting> empSetOpt = appCommonSet.appEmploymentWorkType.stream()
				.filter(x -> x.getEmploymentCode().equals(employmentCode))
				.filter(x -> x.getHolidayOrPauseType() == breakOutType).findFirst();
		if (empSetOpt.isPresent()) {
			AppEmploymentSetting empSet = empSetOpt.get();
			if (empSet.isDisplayFlag()) {
				return wkTypes.stream()
						.filter(x -> empSet.getLstWorkType().stream()
								.filter(y -> y.getWorkTypeCode().equals(x.getWorkTypeCode().v())).findFirst()
								.isPresent())
						.collect(Collectors.toList());
			} else {
				return wkTypes;
			}
		} else {
			return wkTypes;
		}

	}

	public HolidayShipmentDto commonProcessBeforeStart(ApplicationType appType, String companyID, String employeeID,
			GeneralDate baseDate, AppCommonSettingOutput appCommonSettingOutput) {
		HolidayShipmentDto result = new HolidayShipmentDto();

		result.setEmployeeID(employeeID);

		result.setEmployeeName(empAdaptor.getEmployeeName(employeeID));

		result.setRefDate(appCommonSettingOutput.generalDate);

		result.setApplicationSetting(ApplicationSettingDto.convertToDto(appCommonSettingOutput.applicationSetting));

		setAppTypeSet(result, appType.value, companyID);

		result.setManualSendMailAtr(
				appCommonSettingOutput.applicationSetting.getManualSendMailAtr().value == 1 ? true : false);
		result.setSendMailWhenApprovalFlg(
				appCommonSettingOutput.appTypeDiscreteSettings.get(0).getSendMailWhenApprovalFlg().value == 1 ? true
						: false);
		result.setSendMailWhenRegisterFlg(
				appCommonSettingOutput.appTypeDiscreteSettings.get(0).getSendMailWhenRegisterFlg().value == 1 ? true
						: false);
		startupErrorCheck(employeeID, baseDate, companyID);

		return result;
	}

	private void startupErrorCheck(String employeeID, GeneralDate baseDate, String companyID) {
		// // アルゴリズム「1-4.新規画面起動時の承認ルート取得パターン」を実行する
		// ApprovalRootPattern approvalRootPattern =
		// collectApprovalRootPatternService.getApprovalRootPatternService(
		// companyID, employeeID, EmploymentRootAtr.APPLICATION, appType,
		// appCommonSettingOutput.generalDate, "",
		// true);
		// // アルゴリズム「1-5.新規画面起動時のエラーチェック」を実行する
		// startupErrorCheckService.startupErrorCheck(appCommonSettingOutput.generalDate,
		// appType.value,
		// approvalRootPattern.getApprovalRootContentImport());
		// アルゴリズム「振休管理チェック」を実行する
		// Imported(就業.shared.組織管理.社員情報.所属雇用履歴)「所属雇用履歴」を取得する
		Optional<EmploymentHistoryImported> empImpOpt = wkPlaceAdapter.getEmpHistBySid(companyID, employeeID, baseDate);
		if (empImpOpt.isPresent()) {
			// アルゴリズム「振休管理設定の取得」を実行する
			EmploymentHistoryImported empImp = empImpOpt.get();
			String emptCD = empImp.getEmploymentCode();
			Optional<EmpSubstVacation> empSubOpt = empSubrepo.findById(companyID, emptCD);
			if (empSubOpt.isPresent()) {

				EmpSubstVacation empSub = empSubOpt.get();
				boolean isNotManage = empSub.getSetting().getIsManage().equals(ManageDistinct.NO);
				if (isNotManage) {
					throw new BusinessException("Msg_323");
				}
			} else {
				Optional<ComSubstVacation> comSubOpt = comSubrepo.findById(companyID);
				boolean isNoComSubOrNotManage = comSubOpt.isPresent()
						&& comSubOpt.get().getSetting().getIsManage().equals(ManageDistinct.NO);
				if (isNoComSubOrNotManage) {
					throw new BusinessException("Msg_323");
				}

			}
		}

	}

	public void setAppTypeSet(HolidayShipmentDto result, int appType, String companyID) {
		Optional<RequestSetting> reqSetOpt = reqSetRepo.findByCompany(companyID);
		if (reqSetOpt.isPresent()) {
			RequestSetting reqSet = reqSetOpt.get();
			Optional<AppTypeSetDto> appTypeSetDtoOpt = AppTypeSetDto.convertToDto(reqSet).stream()
					.filter(x -> x.getAppType().equals(appType)).findFirst();

			if (appTypeSetDtoOpt.isPresent()) {
				result.setAppTypeSet(appTypeSetDtoOpt.get());
			}

		}

	}

}
