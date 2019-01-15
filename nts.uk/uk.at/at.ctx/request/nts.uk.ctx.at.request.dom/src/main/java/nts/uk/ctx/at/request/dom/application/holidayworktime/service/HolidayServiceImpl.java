package nts.uk.ctx.at.request.dom.application.holidayworktime.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.ReflectedState_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.shift.businesscalendar.specificdate.BusinessDayCalendarAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.shift.businesscalendar.specificdate.dto.BusinessDayCalendarImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.shift.businesscalendar.specificdate.dto.HolidayClsImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.holidayshipment.brkoffsupchangemng.BrkOffSupChangeMng;
import nts.uk.ctx.at.request.dom.application.holidayshipment.brkoffsupchangemng.BrkOffSupChangeMngRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTimeHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTypeHolidayWork;
//import nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmployWorkType;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.InterimRemainDataMngRegisterDateChange;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.HolidayAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class HolidayServiceImpl implements HolidayService {
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	@Inject
	private WorkTimeSettingRepository workTimeRepository;
	@Inject
	private WorkTypeRepository workTypeRepository;
	@Inject
	private ApplicationApprovalService_New appRepository;
	@Inject
	private AppHolidayWorkRepository appHolidayWorkRepository;
	@Inject
	private BusinessDayCalendarAdapter businessDayCalendarAdapter;
	@Inject
	private WorkplaceAdapter wkpAdapter;
	@Inject 
	private BrkOffSupChangeMngRepository brkOffSupChangeMngRepository;
	@Inject
	private ApplicationRepository_New applicationRepository;
	@Inject
	private InterimRemainDataMngRegisterDateChange interimRemainDataMngRegisterDateChange;
//	@Inject
//	private OvertimeService overtimeService;
	@Override
	public WorkTypeHolidayWork getWorkTypes(String companyID, String employeeID, List<AppEmploymentSetting> appEmploymentSettings,
			GeneralDate baseDate,Optional<WorkingConditionItem> personalLablorCodition) {
		WorkTypeHolidayWork workTypeHolidayWorks = new WorkTypeHolidayWork();
		workTypeHolidayWorks = getListWorkType(companyID, employeeID, appEmploymentSettings, baseDate, personalLablorCodition);
		// 勤務種類初期選択 :4_c.初期選択 : TODO
		if(workTypeHolidayWorks.getWorkTypeCodes() == null){
			return workTypeHolidayWorks;
		}
		getWorkType(companyID,workTypeHolidayWorks,baseDate,employeeID,personalLablorCodition);
		return workTypeHolidayWorks;
	}
	// 4_c.初期選択
	@Override
	public void getWorkType(String companyID,WorkTypeHolidayWork workTypes, GeneralDate appDate, String employeeID,Optional<WorkingConditionItem> personalLablorCodition){
		if(!personalLablorCodition.isPresent() || personalLablorCodition.get().getWorkCategory().getHolidayWork() == null){
			// 先頭の勤務種類を選択する
			if(!CollectionUtil.isEmpty(workTypes.getWorkTypeCodes())){
				workTypes.setWorkTypeCode(workTypes.getWorkTypeCodes().get(0));
			}
		}else{
			
			//Imported(申請承認)「職場ID」を取得する 
			//アルゴリズム「社員から職場を取得する」を実行する - req #30
			WkpHistImport wkp = wkpAdapter.findWkpBySid(employeeID, appDate);
			String workplaceID = "";
			if(wkp !=null){
				workplaceID = wkp.getWorkplaceId();
			}
			// 「申請日－法定外・法定内休日区分」をチェック　→Imported(申請承認)「対象日法定休日区分.法定休日区分」を取得する - req 253
			Optional<BusinessDayCalendarImport> buOptional = this.businessDayCalendarAdapter.acquiredHolidayClsOfTargetDate(companyID, workplaceID, appDate);
			if(buOptional.isPresent()){
				String workTypeCode = personalLablorCodition.get().getWorkCategory().getHolidayWork().getWorkTypeCode().get().toString();
				if(buOptional.get().holidayCls.equals(HolidayClsImport.STATUTORY_HOLIDAYS)){
					// 申請日＝＞法定内休日
					if(personalLablorCodition.get().getWorkCategory().getInLawBreakTime().isPresent()){
						workTypeCode = personalLablorCodition.get().getWorkCategory().getInLawBreakTime().get().getWorkTypeCode().get().toString();
					}
					
				}else if(buOptional.get().holidayCls.equals(HolidayClsImport.NON_STATUTORY_HOLIDAYS)){
					// 申請日＝＞法定外休日
					if(personalLablorCodition.get().getWorkCategory().getOutsideLawBreakTime().isPresent()){
						workTypeCode = personalLablorCodition.get().getWorkCategory().getOutsideLawBreakTime().get().getWorkTypeCode().get().toString();
					}
				}else if(buOptional.get().holidayCls.equals(HolidayClsImport.PUBLIC_HOLIDAY)){
					// 申請日＝＞祝日
					if(personalLablorCodition.get().getWorkCategory().getHolidayAttendanceTime().isPresent()){
						workTypeCode = personalLablorCodition.get().getWorkCategory().getHolidayAttendanceTime().get().getWorkTypeCode().get().toString();
					}
				}
				workTypes.setWorkTypeCode(workTypeCode);
			}else{
				if(!CollectionUtil.isEmpty(workTypes.getWorkTypeCodes())){
					workTypes.setWorkTypeCode(workTypes.getWorkTypeCodes().get(0));
				}
			}
		}
		
		Optional<WorkType> workType = workTypeRepository.findByPK(companyID, workTypes.getWorkTypeCode());
		if(workType.isPresent()){
			workTypes.setWorkTypeName(workType.get().getName().toString());
		}
	}
	/** 5.就業時間帯を取得する */
	@Override
	public WorkTimeHolidayWork getWorkTimeHolidayWork(String companyID, String employeeID,
			GeneralDate baseDate,Optional<WorkingConditionItem> personalLablorCodition) {
		WorkTimeHolidayWork workTimeHolidayWork = new WorkTimeHolidayWork();
		// 1.職場別就業時間帯を取得
		List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,baseDate);
		List<String> workTimes = new ArrayList<>();
		if(!CollectionUtil.isEmpty(listWorkTimeCodes)){
			listWorkTimeCodes.forEach(x -> workTimes.add(x));
		}
		workTimeHolidayWork.setWorkTimeCodes(workTimes);
		if(!personalLablorCodition.isPresent() || personalLablorCodition.get().getWorkCategory().getHolidayWork() == null){
			// 先頭の勤務種類を選択する
			if(!CollectionUtil.isEmpty(workTimeHolidayWork.getWorkTimeCodes())){
				workTimeHolidayWork.setWorkTimeCode(workTimeHolidayWork.getWorkTimeCodes().get(0));
			}
		}else{
			//ドメインモデル「個人勤務日区分別勤務.休日出勤時.就業時間帯コード」を選択する
			workTimeHolidayWork.setWorkTimeCode(personalLablorCodition.get().getWorkCategory().getHolidayWork().getWorkTimeCode().get().toString());
		}
		if(workTimeHolidayWork.getWorkTimeCode() != null){
			WorkTimeSetting workTime =  workTimeRepository.findByCode(companyID,workTimeHolidayWork.getWorkTimeCode())
					.orElseGet(()->{
						return workTimeRepository.findByCompanyId(companyID).get(0);
					});
			if(workTime != null){
				workTimeHolidayWork.setWorkTimeName(workTime.getWorkTimeDisplayName().getWorkTimeName().toString());
			}
		}
		return workTimeHolidayWork;
	}
	@Override
	public void createHolidayWork(AppHolidayWork domain, Application_New newApp) {
		//Register application
		appRepository.insert(newApp);
		// insert appHolidayWork,HolidayWorkInput
		appHolidayWorkRepository.Add(domain);
	}
	@Override
	public WorkTypeHolidayWork getListWorkType(String companyID, String employeeID,
			List<AppEmploymentSetting> appEmploymentSettings, GeneralDate appDate,
			Optional<WorkingConditionItem> personalLablorCodition) {
		WorkTypeHolidayWork workTypeHolidayWorks = new WorkTypeHolidayWork();
		// アルゴリズム「社員所属雇用履歴を取得」を実行する 
		SEmpHistImport sEmpHistImport = employeeAdapter.getEmpHist(companyID, employeeID, GeneralDate.today());
		List<String> workTypeCodes = new ArrayList<>();
		if(sEmpHistImport != null && !CollectionUtil.isEmpty(appEmploymentSettings)){
			// ドメインモデル「申請別対象勤務種類」.勤務種類リストを表示する
			AppEmploymentSetting appSet =  appEmploymentSettings.get(0);
			List<AppEmployWorkType> lstEmploymentWorkType = appSet.getLstWorkType();
			boolean isDisplay = appSet.isDisplayFlag();
			if(!CollectionUtil.isEmpty(lstEmploymentWorkType) && isDisplay) {
				Collections.sort(lstEmploymentWorkType, Comparator.comparing(AppEmployWorkType :: getWorkTypeCode));
				lstEmploymentWorkType.forEach(x -> {
					
					workTypeCodes.add(x.getWorkTypeCode());
					});
				workTypeHolidayWorks.setWorkTypeCodes(workTypeCodes);
				return workTypeHolidayWorks;
			}
		}
		////休出
		int breakDay = 11;
		// ドメインモデル「勤務種類」を取得
		List<WorkType> workrTypes = this.workTypeRepository.findWorkOneDay(companyID, 0, breakDay);
		if(!CollectionUtil.isEmpty(workrTypes)){
			workrTypes.forEach(x -> {
				workTypeCodes.add(x.getWorkTypeCode().toString());
			});
			workTypeHolidayWorks.setWorkTypeCodes(workTypeCodes);
			return workTypeHolidayWorks;
		}
		return workTypeHolidayWorks;
	}
	@Override
	// 4_a.勤務種類を取得する（法定内外休日）
	public WorkTypeHolidayWork getWorkTypeForLeaverApp(String companyID, String employeeID,
			List<AppEmploymentSetting> appEmploymentSettings, GeneralDate appDate,
			Optional<WorkingConditionItem> personalLablorCodition,Integer paramholidayCls ) {
		WorkTypeHolidayWork workTypeHolidayWorks = new WorkTypeHolidayWork();
		workTypeHolidayWorks = this.getListWorkType(companyID, employeeID, appEmploymentSettings, appDate, personalLablorCodition);
		if(CollectionUtil.isEmpty(workTypeHolidayWorks.getWorkTypeCodes())){
			return workTypeHolidayWorks;
		}
		//アルゴリズム「社員から職場を取得する」を実行する - req #30
		WkpHistImport wkp = wkpAdapter.findWkpBySid(employeeID, appDate);
		String workplaceID = "";
		if(wkp !=null){
			workplaceID = wkp.getWorkplaceId();
		}
		List<WorkType> worktypes = this.workTypeRepository.findNotDeprecatedByListCode(companyID, workTypeHolidayWorks.getWorkTypeCodes());
		// 「申請日－法定外・法定内休日区分」をチェック →Imported(申請承認)「対象日法定休日区分.法定休日区分」を取得する - req253
		Optional<BusinessDayCalendarImport> buOptional = this.businessDayCalendarAdapter
				.acquiredHolidayClsOfTargetDate(companyID, workplaceID, appDate);
		List<WorkType> workTypeFilter  = new ArrayList<>();
		if(buOptional.isPresent()) {
			if (HolidayClsImport.STATUTORY_HOLIDAYS.equals(buOptional.get().holidayCls)) {
				// 法定内休日 : filter for STATUTORY_HOLIDAYS
				workTypeFilter = worktypes.stream().filter(x -> x.getWorkTypeSet().getHolidayAtr().equals(HolidayAtr.STATUTORY_HOLIDAYS)).collect(Collectors.toList());
			} else if (HolidayClsImport.NON_STATUTORY_HOLIDAYS.equals(buOptional.get().holidayCls)) {
				// 法定外休日
				workTypeFilter = worktypes.stream().filter(x -> x.getWorkTypeSet().getHolidayAtr().equals(HolidayAtr.NON_STATUTORY_HOLIDAYS)).collect(Collectors.toList());
			} else if (HolidayClsImport.PUBLIC_HOLIDAY.equals(buOptional.get().holidayCls)) {
				// 祝日
				workTypeFilter = worktypes.stream().filter(x -> x.getWorkTypeSet().getHolidayAtr().equals(HolidayAtr.PUBLIC_HOLIDAY)).collect(Collectors.toList());
			}else{
				// 取得できない場合
				return getWorkTypeForLeaveApp(workTypeHolidayWorks,companyID);
			}
		}else {
			// 取得できない場合
			return getWorkTypeForLeaveApp(workTypeHolidayWorks,companyID);
		}
		WorkTypeHolidayWork result = new WorkTypeHolidayWork();
		if(!CollectionUtil.isEmpty(workTypeFilter)){
			List<String> workTypeCodes = new ArrayList<>();
			workTypeCodes.forEach(x -> workTypeCodes.add(x));
			result.setWorkTypeCodes(workTypeCodes);
		}
		if(!personalLablorCodition.isPresent() || personalLablorCodition.get().getWorkCategory().getHolidayWork() == null){
			return getWorkTypeForLeaveApp(result,companyID);
		}
		if(paramholidayCls == null){
			return getWorkTypeForLeaveApp(result,companyID);
		}
		// 「元の振出日－法定外・法定内休日区分」をチェック
		String workTypeCode = personalLablorCodition.get().getWorkCategory().getHolidayWork().getWorkTypeCode().toString();
		if (HolidayClsImport.STATUTORY_HOLIDAYS.value == paramholidayCls.intValue()) {
			// 申請日＝＞法定内休日
			if(personalLablorCodition.get().getWorkCategory().getInLawBreakTime().isPresent()){
				workTypeCode = personalLablorCodition.get().getWorkCategory().getInLawBreakTime().get().getWorkTypeCode().toString();
			}
		} else if (HolidayClsImport.NON_STATUTORY_HOLIDAYS.value == paramholidayCls.intValue()) {
			// 申請日＝＞法定外休日
			if(personalLablorCodition.get().getWorkCategory().getOutsideLawBreakTime().isPresent()){
				workTypeCode = personalLablorCodition.get().getWorkCategory().getOutsideLawBreakTime().get().getWorkTypeCode().toString();
			}
		} else if (HolidayClsImport.PUBLIC_HOLIDAY.value == paramholidayCls.intValue()) {
			// 申請日＝＞祝日
			if(personalLablorCodition.get().getWorkCategory().getHolidayAttendanceTime().isPresent()){
				workTypeCode = personalLablorCodition.get().getWorkCategory().getHolidayAttendanceTime().get().getWorkTypeCode().toString();
			}
		}
		result.setWorkTypeCode(workTypeCode);
		Optional<WorkType> workType = workTypeRepository.findByPK(companyID, workTypeCode);
		if(workType.isPresent()){
			result.setWorkTypeName(workType.get().getName().toString());
		}
		return result;
	}
	private WorkTypeHolidayWork getWorkTypeForLeaveApp(WorkTypeHolidayWork workTypeHoliday,String companyID){
		if(CollectionUtil.isEmpty(workTypeHoliday.getWorkTypeCodes())){
			return workTypeHoliday;
		}
		workTypeHoliday.setWorkTypeCode(workTypeHoliday.getWorkTypeCodes().get(0));
		Optional<WorkType> workType = workTypeRepository.findByPK(companyID, workTypeHoliday.getWorkTypeCode());
		if(workType.isPresent()){
			workTypeHoliday.setWorkTypeName(workType.get().getName().toString());
		}
		return workTypeHoliday;
	}
	@Override
	public void delHdWorkByAbsLeaveChange(String appID) {
		String companyID = AppContexts.user().companyId();
		
		// ドメインモデル「振休申請休出変更管理」を取得する
		Optional<BrkOffSupChangeMng> opBrkOffSupChangeMng = brkOffSupChangeMngRepository.findHolidayAppID(appID);
		if(!opBrkOffSupChangeMng.isPresent()){
			return;
		}
		BrkOffSupChangeMng brkOffSupChangeMng = opBrkOffSupChangeMng.get();
		
		// アルゴリズム「振休申請復活」を実行する (9.振休申請復活)
		Application_New application = applicationRepository.findByID(companyID, appID).get();
		// 「振休振出申請.反映情報.実績反映状態(stateReflectionReal)」を「未反映(notReflected)」に更新する
		application.getReflectionInformation().setStateReflectionReal(ReflectedState_New.NOTREFLECTED);
		applicationRepository.update(application);
		
		// ドメインモデル「振休申請休出変更管理」を削除する
		brkOffSupChangeMngRepository.remove(brkOffSupChangeMng.getRecAppID(), brkOffSupChangeMng.getAbsenceLeaveAppID());
		
		// 暫定データの登録
		interimRemainDataMngRegisterDateChange.registerDateChange(
				companyID, 
				application.getEmployeeID(), 
				Arrays.asList(application.getAppDate()));
	}
}
