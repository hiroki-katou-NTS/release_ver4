package nts.uk.ctx.at.request.dom.application.overtime.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmployWorkType;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.requestofeach.RequestAppDetailSetting;
import nts.uk.ctx.at.shared.dom.worktime_old.WorkTime;
import nts.uk.ctx.at.shared.dom.worktime_old.WorkTimeRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

@Stateless
public class OvertimeServiceImpl implements OvertimeService {
	
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	@Inject
	private WorkTypeRepository workTypeRepository;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	@Inject
	private WorkTimeRepository workTimeRepository;
	@Inject
	private OvertimeRepository overTimeRepository;
	@Inject
	ApplicationRepository appRepository;
	@Override
	public int checkOvertime(String url) {
		if(url.equals("PREOVERTIME")){
			return OverTimeAtr.PREOVERTIME.value;
		}else if(url.equals("REGULAROVERTIME")){
			return OverTimeAtr.REGULAROVERTIME.value;
		}
		return OverTimeAtr.ALL.value;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService#getWorkType(java.lang.String, java.lang.String, java.util.Optional, java.util.Optional)
	 */
	@Override
	public List<WorkTypeOvertime> getWorkType(String companyID, String employeeID,
			RequestAppDetailSetting requestAppDetailSetting,List<AppEmploymentSetting> appEmploymentSettings) {
		List<WorkTypeOvertime> result = new ArrayList<>();
		List<WorkType> workTypes = new ArrayList<>();
		if (requestAppDetailSetting != null) {
			// 時刻計算利用チェック
			if (requestAppDetailSetting.getTimeCalUseAtr().value == UseAtr.USE.value) {
				// アルゴリズム「社員所属雇用履歴を取得」を実行する 
				SEmpHistImport sEmpHistImport = employeeAdapter.getEmpHist(companyID, employeeID, GeneralDate.today());
				if (sEmpHistImport != null) {
					// ドメインモデル「申請別対象勤務種類」を取得
					if (appEmploymentSettings != null && appEmploymentSettings.size() > 0) {
						
						List<String> workTypeCodes = new ArrayList<>();
						List<AppEmployWorkType> employWorkTypes = appEmploymentSettings.get(0).getLstWorkType();
						for(AppEmployWorkType appEmployWorkType : employWorkTypes){
							workTypeCodes.add(appEmployWorkType.getWorkTypeCode());
						}
						workTypes = this.workTypeRepository.findNotDeprecatedByListCode(companyID, workTypeCodes);
						result = convertWorkType(workTypes);
						result.sort((a,b) -> a.getWorkTypeCode().compareTo(b.getWorkTypeCode()));
						return result;
					}
				}
					/*
					 * ドメインモデル「勤務種類」を取得
					 */
				// １日の勤務＝以下に該当するもの
				//　出勤、休出、振出、連続勤務
				List<Integer> allDayAtrs = new ArrayList<>();
				//出勤
				allDayAtrs.add(0);
				//休出
				allDayAtrs.add(11);
				//振出
				allDayAtrs.add(7);
				// 連続勤務
				allDayAtrs.add(10);
				// 午前 また 午後 in (休日, 振出, 年休, 出勤, 特別休暇, 欠勤, 代休, 時間消化休暇)
				List<Integer> halfAtrs = new ArrayList<>();
				// 休日
				halfAtrs.add(1);
				// 振出
				halfAtrs.add(7);
				// 年休
				halfAtrs.add(8);
				// 出勤
				halfAtrs.add(0);
				//特別休暇
				halfAtrs.add(4);
				// 欠勤
				halfAtrs.add(5);
				// 代休
				halfAtrs.add(6);
				//時間消化休暇
				halfAtrs.add(9);
				
				workTypes = workTypeRepository.findWorkType(companyID, 0, allDayAtrs, halfAtrs);
				result = convertWorkType(workTypes);
				return result;
			}
		}
		
		return null;
	}

	@Override
	public List<SiftType> getSiftType(String companyID, String employeeID,
			RequestAppDetailSetting requestAppDetailSetting) {
		List<SiftType> result = new ArrayList<>();
		if (requestAppDetailSetting != null) {
			// 時刻計算利用チェック
			if (requestAppDetailSetting.getTimeCalUseAtr().value == UseAtr.USE.value) {
				// 1.職場別就業時間帯を取得
				List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID, GeneralDate.today());
				
				if(listWorkTimeCodes != null){
					List<WorkTime> workTimes =  workTimeRepository.findByCodes(companyID,listWorkTimeCodes);
					for(WorkTime workTime : workTimes){
						SiftType siftType = new SiftType();
						siftType.setSiftCode(workTime.getSiftCD().toString());
						siftType.setSiftName(workTime.getWorkTimeDisplayName().getWorkTimeName().toString());
						result.add(siftType);
					}
					return result;
				}
			}
		}
		return null;
	}

	/**
	 * 登録処理を実行
	 */
	@Override
	public void CreateOvertime(AppOverTime domain, Application newApp){
		//Register application
		appRepository.addApplication(newApp);
		//Register overtime
		overTimeRepository.Add(domain);
	}
	private List<WorkTypeOvertime> convertWorkType(List<WorkType> workTypes){
		List<WorkTypeOvertime> workTypeOvertimes = new ArrayList<>();
		for(WorkType workType : workTypes){
			WorkTypeOvertime workTypeOvertime = new WorkTypeOvertime(workType.getWorkTypeCode().toString(),workType.getName().toString());
			workTypeOvertimes.add(workTypeOvertime);
		}
		return workTypeOvertimes;
	}
	

}
