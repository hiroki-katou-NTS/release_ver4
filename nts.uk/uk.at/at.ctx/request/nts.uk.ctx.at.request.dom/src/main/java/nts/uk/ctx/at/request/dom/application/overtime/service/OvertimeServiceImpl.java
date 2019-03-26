package nts.uk.ctx.at.request.dom.application.overtime.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationApprovalService_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.agreement.AgreementTimeStatusAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeAtr;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmployWorkType;
import nts.uk.ctx.at.request.dom.setting.employment.appemploymentsetting.AppEmploymentSetting;
import nts.uk.ctx.at.request.dom.setting.workplace.ApprovalFunctionSetting;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.employmentrules.employmenttimezone.BreakTimeZoneService;
import nts.uk.ctx.at.shared.dom.employmentrules.employmenttimezone.BreakTimeZoneSharedOutPut;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.worktime.common.DeductionTime;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.DailyWork;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeClassification;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class OvertimeServiceImpl implements OvertimeService {
	
	@Inject
	private EmployeeRequestAdapter employeeAdapter;
	@Inject
	private WorkTypeRepository workTypeRepository;
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	@Inject
	private WorkTimeSettingRepository workTimeRepository;
	@Inject
	private OvertimeRepository overTimeRepository;
	@Inject
	ApplicationApprovalService_New appRepository;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	@Inject
	private CollectAchievement collectAchievement;
	
	@Inject
	private AgreementTimeStatusAdapter agreementTimeStatusAdapter;
	@Inject
	private BasicScheduleService basicService;
	@Inject
	private BreakTimeZoneService timeService;
	
	@Override
	public int checkOvertimeAtr(String url) {
		if(url == null){
			return OverTimeAtr.ALL.value;
		}
		switch(url){
		case "0":
			return OverTimeAtr.PREOVERTIME.value;
		case "1":
			return OverTimeAtr.REGULAROVERTIME.value;
		case "2":
			return OverTimeAtr.ALL.value;
		default:
			return OverTimeAtr.ALL.value;
			
		}
//			if(url.equals("0")){
//				return OverTimeAtr.PREOVERTIME.value;
//			}else if(url.equals("1")){
//				return OverTimeAtr.REGULAROVERTIME.value;
//			}else if(url.equals("2")){
//				return OverTimeAtr.ALL.value;
//			}
//		return 2;
	}

	/* (non-Javadoc)
	 * @see nts.uk.ctx.at.request.dom.application.overtime.service.OvertimeService#getWorkType(java.lang.String, java.lang.String, java.util.Optional, java.util.Optional)
	 */
	@Override
	public List<WorkTypeOvertime> getWorkType(String companyID, String employeeID,
			ApprovalFunctionSetting approvalFunctionSetting,List<AppEmploymentSetting> appEmploymentSettings) {
		List<WorkTypeOvertime> result = new ArrayList<>();
		if(approvalFunctionSetting == null || !approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
			return result;
		}
		// 時刻計算利用チェック
		// アルゴリズム「社員所属雇用履歴を取得」を実行する 
		SEmpHistImport sEmpHistImport = employeeAdapter.getEmpHist(companyID, employeeID, GeneralDate.today());
		
		if (sEmpHistImport != null 
				&& !CollectionUtil.isEmpty(appEmploymentSettings)) {
			//ドメインモデル「申請別対象勤務種類」.勤務種類リストを表示する(hien thi list(申請別対象勤務種類))
			List<AppEmployWorkType> lstEmploymentWorkType = appEmploymentSettings.get(0).getLstWorkType();
			if(!CollectionUtil.isEmpty(lstEmploymentWorkType)) {
				Collections.sort(lstEmploymentWorkType, Comparator.comparing(AppEmployWorkType :: getWorkTypeCode));
				List<String> workTypeCodes = new ArrayList<>();
				lstEmploymentWorkType.forEach(x -> {workTypeCodes.add(x.getWorkTypeCode());});			
				result = this.workTypeRepository.findNotDeprecatedByListCode(companyID, workTypeCodes).stream()
						.map(x -> new WorkTypeOvertime(x.getWorkTypeCode().v(), x.getName().v())).collect(Collectors.toList());
				return result;
			}
		}
		List<Integer> allDayAtrs = allDayAtrs();
		List<Integer> halfAtrs = halfAtrs();
		result = workTypeRepository.findWorkType(companyID, 0, allDayAtrs, halfAtrs).stream()
				.map(x -> new WorkTypeOvertime(x.getWorkTypeCode().v(), x.getName().v())).collect(Collectors.toList());
		return result;
	}
	/**
	 * // １日の勤務＝以下に該当するもの
	 * 　出勤、休出、振出、連続勤務
	 * @return
	 */
	private List<Integer> allDayAtrs(){
		
		List<Integer> allDayAtrs = new ArrayList<>();
		//出勤
		allDayAtrs.add(0);
		//休出
		allDayAtrs.add(11);
		//振出
		allDayAtrs.add(7);
		// 連続勤務
		allDayAtrs.add(10);
		return allDayAtrs;
	}
	/**
	 * 午前 また 午後 in (休日, 振出, 年休, 出勤, 特別休暇, 欠勤, 代休, 時間消化休暇)
	 * @return
	 */
	private List<Integer> halfAtrs(){
		List<Integer> halfAtrs = new ArrayList<>();
		// 休日
		halfAtrs.add(1);
		// 振出
		halfAtrs.add(7);
		// 年休
		halfAtrs.add(2);
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
		return halfAtrs;
	}

	@Override
	public List<SiftType> getSiftType(String companyID, String employeeID,
			ApprovalFunctionSetting approvalFunctionSetting,GeneralDate baseDate) {
		List<SiftType> result = new ArrayList<>();
		if (approvalFunctionSetting != null) {
			// 時刻計算利用チェック
			if (approvalFunctionSetting.getApplicationDetailSetting().get().getTimeCalUse().equals(UseAtr.USE)) {
				// 1.職場別就業時間帯を取得
				List<String> listWorkTimeCodes = otherCommonAlgorithm.getWorkingHoursByWorkplace(companyID, employeeID,baseDate);
				
				if(!CollectionUtil.isEmpty(listWorkTimeCodes)){
					List<WorkTimeSetting> workTimes =  workTimeRepository.findByCodes(companyID,listWorkTimeCodes);
					for(WorkTimeSetting workTime : workTimes){
						SiftType siftType = new SiftType();
						siftType.setSiftCode(workTime.getWorktimeCode().toString());
						siftType.setSiftName(workTime.getWorkTimeDisplayName().getWorkTimeName().toString());
						result.add(siftType);
					}
					return result;
				}
			}
		}
		return Collections.emptyList();
	}

	/**
	 * 登録処理を実行
	 */
	@Override
	public void CreateOvertime(AppOverTime domain, Application_New newApp){
		//Register application
		appRepository.insert(newApp);
		//Register overtime
		overTimeRepository.Add(domain);
	}

	@Override
	/** 09_勤務種類就業時間帯の初期選択をセットする */
	public WorkTypeAndSiftType getWorkTypeAndSiftTypeByPersonCon(String companyID,String employeeID, GeneralDate baseDate,
			List<WorkTypeOvertime> workTypes, List<SiftType> siftTypes) {
		WorkTypeAndSiftType workTypeAndSiftType = new WorkTypeAndSiftType();
		WorkTypeOvertime workTypeOvertime = new  WorkTypeOvertime();
		SiftType siftType = new SiftType();
		if (baseDate != null) {
			AchievementOutput achievementOutput = collectAchievement.getAchievement(companyID, employeeID, baseDate);
			if(Strings.isNotBlank(achievementOutput.getWorkType().getWorkTypeCode())){
				workTypeAndSiftType.setWorkType(new WorkTypeOvertime(achievementOutput.getWorkType().getWorkTypeCode(), achievementOutput.getWorkType().getName()));
				workTypeAndSiftType.setSiftType(new SiftType(achievementOutput.getWorkTime().getWorkTimeCD(), achievementOutput.getWorkTime().getWorkTimeName()));
				String workTypeCode = workTypeAndSiftType.getWorkType().getWorkTypeCode();
				String siftCD = workTypeAndSiftType.getSiftType().getSiftCode();
				BreakTimeZoneSharedOutPut breakTime = getBreakTimes(companyID, workTypeCode, siftCD);
				workTypeAndSiftType.setBreakTimes(breakTime.getLstTimezone());
				return workTypeAndSiftType;
			}
		}
		baseDate = GeneralDate.today();
		//ドメインモデル「個人労働条件」を取得する(lay dieu kien lao dong ca nhan(個人労働条件))
		Optional<WorkingConditionItem> personalLablorCodition = workingConditionItemRepository.getBySidAndStandardDate(employeeID,baseDate);
		
		if(!personalLablorCodition.isPresent() || personalLablorCodition.get().getWorkCategory().getWeekdayTime() == null){
			if(!CollectionUtil.isEmpty(workTypes)){
				workTypeAndSiftType.setWorkType(workTypes.get(0));
			}
			if(!CollectionUtil.isEmpty(siftTypes)){
				workTypeAndSiftType.setSiftType(siftTypes.get(0));
			}
		}else{
			
			String wktypeCd = personalLablorCodition.get().getWorkCategory().getWeekdayTime().getWorkTypeCode().get()
					.v().toString();
			workTypeRepository.findByPK(companyID, wktypeCd).ifPresent(wktype -> {
				workTypeOvertime.setWorkTypeName(wktype.getName().toString());
			});
			workTypeOvertime.setWorkTypeCode(wktypeCd);
			workTypeAndSiftType.setWorkType(workTypeOvertime);
			String wkTimeCd = personalLablorCodition.get().getWorkCategory().getWeekdayTime().getWorkTimeCode().get()
					.v().toString();
			this.workTimeRepository.findByCode(companyID, wkTimeCd).ifPresent(wkTime -> {
				siftType.setSiftName(wkTime.getWorkTimeDisplayName().getWorkTimeAbName().v());
			});
			siftType.setSiftCode(wkTimeCd);
			workTypeAndSiftType.setSiftType(siftType);
		}
		//休憩時間帯を取得する
		if (workTypeAndSiftType.getWorkType() != null && workTypeAndSiftType.getSiftType() != null) {
			String workTypeCode = workTypeAndSiftType.getWorkType().getWorkTypeCode();
			String siftCD = workTypeAndSiftType.getSiftType().getSiftCode();
			BreakTimeZoneSharedOutPut breakTime = getBreakTimes(companyID, workTypeCode, siftCD);
			workTypeAndSiftType.setBreakTimes(breakTime.getLstTimezone());
		}
		return workTypeAndSiftType;
	}
	
	
	@Override
	//休憩時間帯を取得する
	public BreakTimeZoneSharedOutPut getBreakTimes(String companyID, String workTypeCode, String workTimeCode) {
		// 1日半日出勤・1日休日系の判定
		WorkStyle workStyle = this.basicService.checkWorkDay(workTypeCode);
		// 平日か休日か判断する
		WeekdayHolidayClassification weekDay = checkHolidayOrNot(workTypeCode);
		// 休憩時間帯の取得
		BreakTimeZoneSharedOutPut breakTimeZoneSharedOutPut = this.timeService.getBreakTimeZone(companyID, workTimeCode, weekDay.value, workStyle);
		Collections.sort(breakTimeZoneSharedOutPut.getLstTimezone(), new Comparator<DeductionTime>(){
			@Override
            public int compare(DeductionTime o1, DeductionTime o2) {
                return o1.getStart().v().compareTo(o2.getStart().v());
            }
		});
		return breakTimeZoneSharedOutPut;
	}

	private WeekdayHolidayClassification checkHolidayOrNot(String workTypeCd) {
		String companyId =  AppContexts.user().companyId();
		Optional<WorkType> WorkTypeOptional = this.workTypeRepository.findByPK(companyId, workTypeCd);
		if (!WorkTypeOptional.isPresent()) {
			return WeekdayHolidayClassification.WEEKDAY;
		}
		// check null?
		WorkType workType = WorkTypeOptional.get();
		DailyWork dailyWork = workType.getDailyWork();
		WorkTypeClassification oneDay = dailyWork.getOneDay();
		// 休日出勤
		if (oneDay.value == 11) {
			return WeekdayHolidayClassification.HOLIDAY;
		}
		return WeekdayHolidayClassification.WEEKDAY;
	}

	@Override
	public Integer getTime36Detail(AppOvertimeDetail appOvertimeDetail) {
		if(appOvertimeDetail.getLimitErrorTime().v() <= 0){
			return null;
		}
		return agreementTimeStatusAdapter.checkAgreementTimeStatus(
				new AttendanceTimeMonth(appOvertimeDetail.getApplicationTime().v()+appOvertimeDetail.getActualTime().v()), 
				appOvertimeDetail.getLimitAlarmTime(), 
				appOvertimeDetail.getLimitErrorTime(), 
				appOvertimeDetail.getExceptionLimitAlarmTime(), 
				appOvertimeDetail.getExceptionLimitErrorTime()).value;
	}
}
