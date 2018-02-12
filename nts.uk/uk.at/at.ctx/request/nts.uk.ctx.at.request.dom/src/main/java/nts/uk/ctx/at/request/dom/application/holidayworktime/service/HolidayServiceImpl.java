package nts.uk.ctx.at.request.dom.application.holidayworktime.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTimeHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.service.dto.WorkTypeHolidayWork;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmployWorkType;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.shared.dom.personallaborcondition.PersonalLaborCondition;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
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
	
	@Override
	public WorkTypeHolidayWork getWorkTypes(String companyID, String employeeID, List<AppEmploymentSetting> appEmploymentSettings,
			GeneralDate baseDate,Optional<PersonalLaborCondition> personalLablorCodition) {
		WorkTypeHolidayWork workTypeHolidayWorks = new WorkTypeHolidayWork();
		workTypeHolidayWorks = getListWorkType(companyID, employeeID, appEmploymentSettings, baseDate, personalLablorCodition);
		// 勤務種類初期選択 :4_c.初期選択 : TODO
		if(workTypeHolidayWorks.getWorkTypeCodes() == null){
			return workTypeHolidayWorks;
		}
		getWorkType(workTypeHolidayWorks,baseDate,employeeID,personalLablorCodition);
		return workTypeHolidayWorks;
	}
	// 4_c.初期選択
	@Override
	public void getWorkType(WorkTypeHolidayWork workTypes, GeneralDate appDate, String employeeID,Optional<PersonalLaborCondition> personalLablorCodition){
		if(personalLablorCodition.isPresent() && personalLablorCodition.get().getWorkCategory().getWeekdayTime() == null){
			// 先頭の勤務種類を選択する
			if(!CollectionUtil.isEmpty(workTypes.getWorkTypeCodes())){
				workTypes.setWorkTypeCode(workTypes.getWorkTypeCodes().get(0));
			}
		}else{
			// 「申請日－法定外・法定内休日区分」をチェック : TODO
			
		}
	}
	/** 5.就業時間帯を取得する */
	@Override
	public WorkTimeHolidayWork getWorkTimeHolidayWork(String companyID, String employeeID,
			GeneralDate baseDate,Optional<PersonalLaborCondition> personalLablorCodition) {
		WorkTimeHolidayWork workTimeHolidayWork = new WorkTimeHolidayWork();
		// 1.職場別就業時間帯を取得
		List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,baseDate);
		List<String> workTimes = new ArrayList<>();
		if(!CollectionUtil.isEmpty(listWorkTimeCodes)){
			listWorkTimeCodes.forEach(x -> workTimes.add(x));
		}
		workTimeHolidayWork.setWorkTimeCodes(workTimes);
		if(!personalLablorCodition.isPresent() || personalLablorCodition.get().getWorkCategory().getWeekdayTime() == null){
			// 先頭の勤務種類を選択する
			if(!CollectionUtil.isEmpty(workTimeHolidayWork.getWorkTimeCodes())){
				workTimeHolidayWork.setWorkTimeCode(workTimeHolidayWork.getWorkTimeCodes().get(0));
			}
		}else{
			//ドメインモデル「個人勤務日区分別勤務.休日出勤時.就業時間帯コード」を選択する
			workTimeHolidayWork.setWorkTimeCode(personalLablorCodition.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().toString());
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
			Optional<PersonalLaborCondition> personalLablorCodition) {
		WorkTypeHolidayWork workTypeHolidayWorks = new WorkTypeHolidayWork();
		// アルゴリズム「社員所属雇用履歴を取得」を実行する 
		SEmpHistImport sEmpHistImport = employeeAdapter.getEmpHist(companyID, employeeID, GeneralDate.today());
		List<String> workTypeCodes = new ArrayList<>();
		if(sEmpHistImport != null && !CollectionUtil.isEmpty(appEmploymentSettings)){
			// ドメインモデル「申請別対象勤務種類」.勤務種類リストを表示する
			List<AppEmployWorkType> lstEmploymentWorkType = appEmploymentSettings.get(0).getLstWorkType();
			if(!CollectionUtil.isEmpty(lstEmploymentWorkType)) {
				Collections.sort(lstEmploymentWorkType, Comparator.comparing(AppEmployWorkType :: getWorkTypeCode));
				lstEmploymentWorkType.forEach(x -> {
					
					workTypeCodes.add(x.getWorkTypeCode());
					});
				workTypeHolidayWorks.setWorkTypeCodes(workTypeCodes);
			}
		}else{
			////休出
			int breakDay = 11;
			// ドメインモデル「勤務種類」を取得
			List<WorkType> workrTypes = this.workTypeRepository.findWorkOneDay(companyID, 0, breakDay);
			if(!CollectionUtil.isEmpty(workrTypes)){
				workrTypes.forEach(x -> {
					workTypeCodes.add(x.getWorkTypeCode().toString());
				});
				workTypeHolidayWorks.setWorkTypeCodes(workTypeCodes);
			}
		}
		return workTypeHolidayWorks;
	}

}
