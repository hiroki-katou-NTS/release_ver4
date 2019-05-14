package nts.uk.ctx.at.request.dom.application.common.datawork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.output.AppCommonSettingOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.overtime.service.CheckWorkingInfoResult;
import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmployWorkType;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
//import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborConditionRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

@Stateless
public class DataWorkServiceImpl implements IDataWorkService {
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	@Inject
	private WorkTypeRepository workTypeRepository;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepository;
//	@Inject
//	private PersonalLaborConditionRepository personalLaborConditionRepository;
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSettingRepository;
	@Inject
	private OvertimeService overtimeService;

	@Override
	public DataWork getDataWork(String companyId, String sId, GeneralDate appDate,
			AppCommonSettingOutput appCommonSetting,int apptype) {
		DataWork dataWork = new DataWork();
		// アルゴリズム「社員所属雇用履歴を取得」を実行する
		ApprovalFunctionSetting approvalFunctionSetting = appCommonSetting.approvalFunctionSetting;
		if (approvalFunctionSetting == null) {
			return null;
		}
		List<AppEmploymentSetting> appEmploymentWorkType = appCommonSetting.appEmploymentWorkType;
		// 勤務種類取得
		List<String> workTypeCodes = getWorkType(companyId, sId, approvalFunctionSetting, appEmploymentWorkType,apptype);
		dataWork.setWorkTypeCodes(workTypeCodes);
		// 就業時間帯取得
		List<String> workTimeCodes = getSiftType(companyId, sId, approvalFunctionSetting);
		dataWork.setWorkTimeCodes(workTimeCodes);

		// 勤務種類就業時間帯の初期選択をセットする
		getSelectedDataWork(companyId, sId, appDate, workTypeCodes, workTimeCodes, dataWork);

		return dataWork;
	}

	/**
	 * 勤務種類取得
	 * 
	 * @param companyID
	 * @param employeeID
	 * @param requestAppDetailSetting
	 * @param appEmploymentSettings
	 * @param apptype 
	 * @return
	 */
	public List<String> getWorkType(String companyID, String employeeID,
			ApprovalFunctionSetting approvalFunctionSetting, List<AppEmploymentSetting> appEmploymentSettings, int apptype) {
		List<String> result = new ArrayList<>();
		if (approvalFunctionSetting == null) {
			return result;
		}
		// 時刻計算利用チェック
		// アルゴリズム「社員所属雇用履歴を取得」を実行する
		SEmpHistImport sEmpHistImport = employeeAdapter.getEmpHist(companyID, employeeID, GeneralDate.today());

		if (sEmpHistImport != null && !CollectionUtil.isEmpty(appEmploymentSettings)) {
			// ドメインモデル「申請別対象勤務種類」.勤務種類リストを表示する
			List<AppEmployWorkType> lstEmploymentWorkType = appEmploymentSettings.get(0).getLstWorkType();
			if (CollectionUtil.isEmpty(lstEmploymentWorkType)) {
				result = this.workTypeRepository.findNotDeprecated(companyID).stream()
				.map(x -> x.getWorkTypeCode().v()).collect(Collectors.toList());
				return result;
			}
			Collections.sort(lstEmploymentWorkType, Comparator.comparing(AppEmployWorkType::getWorkTypeCode));
			List<String> workTypeCodes = new ArrayList<>();
			lstEmploymentWorkType.forEach(x -> {
				workTypeCodes.add(x.getWorkTypeCode());
			});
			result = this.workTypeRepository.findNotDeprecatedByListCode(companyID, workTypeCodes).stream()
					.map(x -> x.getWorkTypeCode().v()).collect(Collectors.toList());
			return result;
		}
		// ドメインモデル「勤務種類」を取得
		if (ApplicationType.GO_RETURN_DIRECTLY_APPLICATION.value == apptype) {
			List<Integer> allDayAtrs = allDayAtrs();
			List<Integer> halfAtrs = halfAtrs();
			result = workTypeRepository.findWorkType(companyID, 0, allDayAtrs, halfAtrs).stream()
					.map(x -> x.getWorkTypeCode().v()).collect(Collectors.toList());
		}
		if(ApplicationType.WORK_CHANGE_APPLICATION.value == apptype){
			result = this.workTypeRepository.findNotDeprecated(companyID).stream()
					.map(x -> x.getWorkTypeCode().v()).collect(Collectors.toList());
		}
		return result;
	}

	/**
	 * 就業時間帯取得
	 * 
	 * @param companyID
	 * @param employeeID
	 * @param requestAppDetailSetting
	 * @return
	 */
	public List<String> getSiftType(String companyID, String employeeID,
			ApprovalFunctionSetting approvalFunctionSetting) {
		if (approvalFunctionSetting == null) {
			return Collections.emptyList();
		}
		// 1.職場別就業時間帯を取得
		List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,
				GeneralDate.today());

		if (!CollectionUtil.isEmpty(listWorkTimeCodes)) {
			List<WorkTimeSetting> workTimes = workTimeSettingRepository.findByCodes(companyID, listWorkTimeCodes);
			return workTimes.stream().map(item -> item.getWorktimeCode().v()).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	/**
	 * 勤務種類就業時間帯の初期選択をセットする
	 * 
	 * @param companyId
	 *            : 社員ID
	 * @param employeeId
	 * @param baseDate
	 *            : 基準日
	 * @param workTypes
	 * @param workTimeTypes
	 * @param selectedData
	 */
	private void getSelectedDataWork(String companyId, String employeeId, GeneralDate baseDate, List<String> workTypes,
			List<String> workTimeTypes, DataWork selectedData) {

		// ドメインモデル「個人労働条件」を取得する
		Optional<WorkingConditionItem> personalLablorCodition = workingConditionItemRepository.getBySidAndStandardDate(employeeId,baseDate);

		if (!personalLablorCodition.isPresent()
				|| !personalLablorCodition.get().getWorkCategory().getWeekdayTime().getWorkTypeCode().isPresent()) {
			if (!CollectionUtil.isEmpty(workTypes)) {
				// 先頭の勤務種類を選択する
				selectedData.setSelectedWorkTypeCd(workTypes.get(0));
				SettingWorTypekName(companyId, selectedData.getSelectedWorkTypeCd(), selectedData);
			}
			if (!CollectionUtil.isEmpty(workTimeTypes)) {
				// 先頭の就業時間帯を選択する
				selectedData.setSelectedWorkTimeCd(workTimeTypes.get(0));
				SettingWorTimekName(companyId, selectedData.getSelectedWorkTimeCd(), selectedData);
			}
		} else {
			// ドメインモデル「個人勤務日区分別勤務」．平日時．勤務種類コードを選択する
			
			Optional<WorkTypeCode> wkTypeOpt = personalLablorCodition.get().getWorkCategory().getWeekdayTime()
					.getWorkTypeCode();
			if (wkTypeOpt.isPresent()) {
				String wkTypeCd = wkTypeOpt.get().v();
				if(workTypes.contains(wkTypeCd)){
					selectedData.setSelectedWorkTypeCd(wkTypeCd);
					Optional<WorkType> workType = workTypeRepository.findByPK(companyId,wkTypeCd);
					selectedData.setSelectedWorkTypeName(workType.get().getName().v());
				} else {
					workTypes.sort(Comparator.comparing(String::toString));
					selectedData.setSelectedWorkTypeCd(workTypes.get(0));
					Optional<WorkType> workType = workTypeRepository.findByPK(companyId,selectedData.getSelectedWorkTypeCd());
					selectedData.setSelectedWorkTypeName(workType.get().getName().v());
				}
			}
			// ドメインモデル「個人勤務日区分別勤務」．平日時．就業時間帯コードを選択する
			
			String wkTimeCd = personalLablorCodition.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().get()
					.toString();
			selectedData.setSelectedWorkTimeCd(wkTimeCd);
			//12.マスタ勤務種類、就業時間帯データをチェック
			CheckWorkingInfoResult checkResult = this.overtimeService.checkWorkingInfo(companyId, null, wkTimeCd);
			if (checkResult.isWkTimeError()) {
				selectedData.setSelectedWorkTimeCd(workTimeTypes.get(0));
			}
			
			wkTimeCd = selectedData.getSelectedWorkTimeCd();
			workTimeSettingRepository.findByCode(companyId, wkTimeCd).ifPresent(wkTime -> {
				selectedData.setSelectedWorkTimeName(wkTime.getWorkTimeDisplayName().getWorkTimeName().v());
			});
			
		}
		
		// ドメイン「所定時間設定」.「所定時間帯設定」「時間帯(使用区分付き)」の開始時刻、終了時刻を取得する
		Optional<PredetemineTimeSetting> opPredetemineTimeSetting = 
				predetemineTimeSettingRepository.findByWorkTimeCode(companyId, selectedData.getSelectedWorkTimeCd());
		if(opPredetemineTimeSetting.isPresent()){
			selectedData.setStartTime1(opPredetemineTimeSetting.get().getPrescribedTimezoneSetting().getLstTimezone().get(0).getStart().v());
			selectedData.setEndTime1(opPredetemineTimeSetting.get().getPrescribedTimezoneSetting().getLstTimezone().get(0).getEnd().v());
		}
	}

	/**
	 * // １日の勤務＝以下に該当するもの 出勤、休出、振出、連続勤務
	 * 
	 * @return
	 */
	private List<Integer> allDayAtrs() {

		List<Integer> allDayAtrs = new ArrayList<>();
		// 出勤
		allDayAtrs.add(0);
		// 休出
		allDayAtrs.add(11);
		// 振出
		allDayAtrs.add(7);
		// 連続勤務
		allDayAtrs.add(10);
		return allDayAtrs;
	}

	/**
	 * 午前 また 午後 in (休日, 振出, 年休, 出勤, 特別休暇, 欠勤, 代休, 時間消化休暇)
	 * 
	 * @return
	 */
	private List<Integer> halfAtrs() {
		List<Integer> halfAtrs = new ArrayList<>();
		// 休日
		halfAtrs.add(1);
		// 振出
		halfAtrs.add(7);
		// 年休
		halfAtrs.add(8);
		// 出勤
		halfAtrs.add(0);
		// 特別休暇
		halfAtrs.add(4);
		// 欠勤
		halfAtrs.add(5);
		// 代休
		halfAtrs.add(6);
		// 時間消化休暇
		halfAtrs.add(9);
		return halfAtrs;
	}
	/**
	 * Setting worktype name
	 * @param companyId
	 * @param workTypeCode
	 * @param selectedData
	 */
	private void SettingWorTypekName(String companyId, String workTypeCode, DataWork selectedData) {
		// ドメインモデル「個人勤務日区分別勤務」．平日時．勤務種類コードを選択する
		Optional<WorkType> workType = workTypeRepository.findByPK(companyId, workTypeCode);
		selectedData.setSelectedWorkTypeCd(workTypeCode);
		if (workType.isPresent()) {
			selectedData.setSelectedWorkTypeName(workType.get().getName().v());
		}
	}
	/**
	 * Setting worktime name
	 * @param companyId
	 * @param workTimeCode
	 * @param selectedData
	 */
	private void SettingWorTimekName(String companyId, String workTimeCode, DataWork selectedData) {

		// ドメインモデル「個人勤務日区分別勤務」．平日時．就業時間帯コードを選択する
		Optional<WorkTimeSetting> workTime = workTimeSettingRepository.findByCode(companyId, workTimeCode);
		selectedData.setSelectedWorkTimeCd(workTimeCode);
		if (workTime.isPresent()) {
			selectedData.setSelectedWorkTimeName(workTime.get().getWorkTimeDisplayName().getWorkTimeName().v());
		}
	}
}
