package nts.uk.ctx.at.request.app.find.application.applicationlist;

/*import nts.uk.ctx.at.request.dom.setting.company.displayname.HdAppDispNameRepository;*/
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleImport;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeAtr;
import nts.uk.ctx.at.request.dom.application.stamp.StampRequestMode;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.IAppWorkChangeRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispName;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
import nts.uk.ctx.at.request.dom.setting.company.displayname.DispName;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.at.shared.dom.worktype.service.JudgmentOneDayHoliday;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ApplicationListForScreen {
	
	@Inject
	private ApplicationRepository_New applicationRepository_New;
	@Inject
	private AppDispNameRepository appDispNameRepository;
	/*@Inject
	private HdAppDispNameRepository hdAppDispNameRepository;*/
	@Inject
	private AppAbsenceRepository appAbsenceRepository;
	@Inject
	private HdAppSetRepository hdAppSetRepository;
	@Inject
	private IAppWorkChangeRepository appWorkChangeRepository;
	@Inject
	private ScBasicScheduleAdapter scBasicScheduleAdapter;
	
	@Inject
	private JudgmentOneDayHoliday judgmentOneDayHoliday;
	
	@Inject
	public WorkTypeRepository workTypeRepo;
	/**
	 * 社員、期間に一致する申請を取得する
	 * requestList #26
	 * getApplicationBySID 
	 * @param employeeID
	 * @param startDate
	 * @param endDate
	 * @return list<ApplicationExport>
	 */
	public List<ApplicationExportDto> getApplicationBySID(List<String> employeeID, GeneralDate startDate, GeneralDate endDate){
		String companyID = AppContexts.user().companyId();
		List<ApplicationExportDto> applicationExports = new ArrayList<>();
		List<Application_New> application = this.applicationRepository_New.getApplicationBySIDs(employeeID, startDate, endDate);
		if(CollectionUtil.isEmpty(application)){
			return applicationExports;
		}
		List<Application_New> applicationExcessHoliday = application.stream()
				.filter(x -> x.getAppType().value != ApplicationType.ABSENCE_APPLICATION.value &&
							x.getAppType().value != ApplicationType.WORK_CHANGE_APPLICATION.value)
				.collect(Collectors.toList());
		List<AppDispName> allApps = appDispNameRepository.getAll(application.stream().map(c -> c.getAppType().value).distinct().collect(Collectors.toList()));
		if(!applicationExcessHoliday.isEmpty()){
			List<ScBasicScheduleImport> basicSchedules = new ArrayList<>();
			GeneralDate minD = applicationExcessHoliday.stream().map(c -> c.getStartDate().orElse(null)).filter(c -> c != null).min((c1, c2) -> c1.compareTo(c2)).get();
			GeneralDate maxD = applicationExcessHoliday.stream().map(c -> c.getEndDate().orElse(null)).filter(c -> c != null).max((c1, c2) -> c1.compareTo(c2)).get();
			if(minD != null && maxD != null){
				basicSchedules.addAll(scBasicScheduleAdapter.findByID(applicationExcessHoliday.stream().map(c -> c.getEmployeeID()).distinct().collect(Collectors.toList()), new DatePeriod(minD, maxD)));
			}
			for(Application_New app : applicationExcessHoliday){
				if((!(app.getStartDate().isPresent()&&app.getEndDate().isPresent())) || 
					app.getStartDate().get().equals(app.getEndDate().get())){
					ApplicationExportDto applicationExport = new ApplicationExportDto();
					applicationExport.setAppDate(app.getAppDate());
					applicationExport.setAppType(app.getAppType().value);
					applicationExport.setEmployeeID(app.getEmployeeID());
					applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
					applicationExport.setAppTypeName(getAppName(companyID, allApps, app.getAppType()));
					applicationExports.add(applicationExport);
				} else {
					for(GeneralDate loopDate = app.getStartDate().get(); loopDate.beforeOrEquals(app.getEndDate().get()); loopDate = loopDate.addDays(1)){
						// Imported「勤務予定基本情報」を取得する
						Optional<ScBasicScheduleImport> opScBasicScheduleImport = findBasicSchedule(basicSchedules, app.getEmployeeID(), loopDate);
						if(!opScBasicScheduleImport.isPresent()){
							ApplicationExportDto applicationExport = new ApplicationExportDto();
							applicationExport.setAppDate(loopDate);
							applicationExport.setAppType(app.getAppType().value);
							applicationExport.setEmployeeID(app.getEmployeeID());
							applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
							applicationExport.setAppTypeName(getAppName(companyID, allApps, app.getAppType()));
							applicationExports.add(applicationExport);
							continue;
						}
						// 1日休日の判定
						boolean judgment = judgmentOneDayHoliday.judgmentOneDayHoliday(companyID, opScBasicScheduleImport.get().getWorkTypeCode());
						if(!judgment){
							ApplicationExportDto applicationExport = new ApplicationExportDto();
							applicationExport.setAppDate(loopDate);
							applicationExport.setAppType(app.getAppType().value);
							applicationExport.setEmployeeID(app.getEmployeeID());
							applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
							applicationExport.setAppTypeName(getAppName(companyID, allApps, app.getAppType()));
							applicationExports.add(applicationExport);
						}
					}
				}
			}
		}
		List<Application_New> applicationHoliday = application.stream().filter(x -> x.getAppType().value == ApplicationType.ABSENCE_APPLICATION.value).collect(Collectors.toList());
		if(!applicationHoliday.isEmpty()){
			Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
			List<AppAbsence> apps = appAbsenceRepository.getAbsenceByIds(companyID, applicationHoliday.stream().map(c -> c.getAppID()).distinct().collect(Collectors.toList()));
			List<ScBasicScheduleImport> basicSchedules = new ArrayList<>();
			GeneralDate minD = applicationHoliday.stream().map(c -> c.getStartDate().orElse(null)).filter(c -> c != null).min((c1, c2) -> c1.compareTo(c2)).get();
			GeneralDate maxD = applicationHoliday.stream().map(c -> c.getEndDate().orElse(null)).filter(c -> c != null).max((c1, c2) -> c1.compareTo(c2)).get();
			if(minD != null && maxD != null){
				basicSchedules.addAll(scBasicScheduleAdapter.findByID(applicationHoliday.stream().map(c -> c.getEmployeeID()).distinct().collect(Collectors.toList()), new DatePeriod(minD, maxD)));
			}
			for(Application_New app : applicationHoliday){
				if((!(app.getStartDate().isPresent()&&app.getEndDate().isPresent())) || 
						app.getStartDate().get().equals(app.getEndDate().get())){
					Optional<AppAbsence> optAppAbsence = apps.stream().filter(c -> c.getAppID().equals(app.getAppID())).findFirst();
					ApplicationExportDto applicationExport = new ApplicationExportDto();
					applicationExport.setAppDate(app.getAppDate());
					applicationExport.setAppType(app.getAppType().value);
					applicationExport.setEmployeeID(app.getEmployeeID());
					applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
					// ドメインモデル「休暇申請種類表示名」を取得する
					applicationExport.setAppTypeName(this.getAppAbsenceName(optAppAbsence.get().getHolidayAppType().value, hdAppSet));
					applicationExports.add(applicationExport);
				} else {
					for(GeneralDate loopDate = app.getStartDate().get(); loopDate.beforeOrEquals(app.getEndDate().get()); loopDate = loopDate.addDays(1)){
						// Imported「勤務予定基本情報」を取得する
						Optional<ScBasicScheduleImport> opScBasicScheduleImport = findBasicSchedule(basicSchedules, app.getEmployeeID(), loopDate);
						if(!opScBasicScheduleImport.isPresent()){
							ApplicationExportDto applicationExport = new ApplicationExportDto();
							applicationExport.setAppDate(loopDate);
							applicationExport.setAppType(app.getAppType().value);
							applicationExport.setEmployeeID(app.getEmployeeID());
							applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
							applicationExport.setAppTypeName(getAppName(companyID, allApps, app.getAppType()));
							applicationExports.add(applicationExport);
							continue;
						}
						// 1日休日の判定
						boolean judgment = judgmentOneDayHoliday.judgmentOneDayHoliday(companyID, opScBasicScheduleImport.get().getWorkTypeCode());
						if(!judgment){
							Optional<AppAbsence> optAppAbsence = apps.stream().filter(c -> c.getAppID().equals(app.getAppID())).findFirst();
							ApplicationExportDto applicationExport = new ApplicationExportDto();
							applicationExport.setAppDate(loopDate);
							applicationExport.setAppType(app.getAppType().value);
							applicationExport.setEmployeeID(app.getEmployeeID());
							applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
							// ドメインモデル「休暇申請種類表示名」を取得する
							applicationExport.setAppTypeName(this.getAppAbsenceName(optAppAbsence.get().getHolidayAppType().value, hdAppSet));
							applicationExports.add(applicationExport);
						}
					}
				}
			}
		}
		
		List<Application_New> appWorkChangeLst = application.stream().filter(x -> x.getAppType().value == ApplicationType.WORK_CHANGE_APPLICATION.value).collect(Collectors.toList());
		if(!appWorkChangeLst.isEmpty()){
			List<AppWorkChange> appWorkChanges = new ArrayList<>();
			List<ScBasicScheduleImport> basicSchedules = new ArrayList<>();
			List<WorkType> workTypes = new ArrayList<>();
			GeneralDate minD = appWorkChangeLst.stream().map(c -> c.getStartDate().orElse(null)).filter(c -> c != null).min((c1, c2) -> c1.compareTo(c2)).get();
			GeneralDate maxD = appWorkChangeLst.stream().map(c -> c.getEndDate().orElse(null)).filter(c -> c != null).max((c1, c2) -> c1.compareTo(c2)).get();
			if(minD != null && maxD != null){
				appWorkChanges.addAll(appWorkChangeRepository.getListAppWorkChangeByID(companyID, appWorkChangeLst.stream().map(c -> c.getAppID()).distinct().collect(Collectors.toList())));
				basicSchedules.addAll(scBasicScheduleAdapter.findByID(appWorkChangeLst.stream().map(c -> c.getEmployeeID()).distinct().collect(Collectors.toList()), new DatePeriod(minD, maxD)));
				workTypes.addAll(workTypeRepo.getPossibleWorkTypeV2(companyID, basicSchedules.stream().map(c -> c.getWorkTypeCode()).distinct().collect(Collectors.toList())));
			}
			for(Application_New app : appWorkChangeLst){
				if((!(app.getStartDate().isPresent()&&app.getEndDate().isPresent())) || 
						app.getStartDate().get().equals(app.getEndDate().get())){
					ApplicationExportDto applicationExport = new ApplicationExportDto();
					applicationExport.setAppDate(app.getAppDate());
					applicationExport.setAppType(app.getAppType().value);
					applicationExport.setEmployeeID(app.getEmployeeID());
					applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
					applicationExport.setAppTypeName(getAppName(companyID, allApps, app.getAppType()));
					applicationExports.add(applicationExport);
				} else {
					// 申請種類＝勤務変更申請　＆　休日を除外するの場合
					AppWorkChange appWorkChange = appWorkChanges.stream().filter(c -> c.getAppId().equals(app.getAppID())).findFirst().get();
					for(GeneralDate loopDate = app.getStartDate().get(); loopDate.beforeOrEquals(app.getEndDate().get()); loopDate = loopDate.addDays(1)){
						if(appWorkChange.getExcludeHolidayAtr()==0){
							ApplicationExportDto applicationExport = new ApplicationExportDto();
							applicationExport.setAppDate(loopDate);
							applicationExport.setAppType(app.getAppType().value);
							applicationExport.setEmployeeID(app.getEmployeeID());
							applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
							applicationExport.setAppTypeName(getAppName(companyID, allApps, app.getAppType()));
							applicationExports.add(applicationExport);
						} else {
							// Imported「勤務予定基本情報」を取得する
							Optional<ScBasicScheduleImport> opScBasicScheduleImport = findBasicSchedule(basicSchedules, app.getEmployeeID(), loopDate);
							if(!opScBasicScheduleImport.isPresent()){
								ApplicationExportDto applicationExport = new ApplicationExportDto();
								applicationExport.setAppDate(loopDate);
								applicationExport.setAppType(app.getAppType().value);
								applicationExport.setEmployeeID(app.getEmployeeID());
								applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
								applicationExport.setAppTypeName(getAppName(companyID, allApps, app.getAppType()));
								applicationExports.add(applicationExport);
								continue;
							}
							// 1日休日の判定
							boolean judgment = judgmentOneDayHoliday.judgmentOneDayHoliday(companyID, opScBasicScheduleImport.get().getWorkTypeCode());
							if(!judgment){
								ApplicationExportDto applicationExport = new ApplicationExportDto();
								applicationExport.setAppDate(loopDate);
								applicationExport.setAppType(app.getAppType().value);
								applicationExport.setEmployeeID(app.getEmployeeID());
								applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
								applicationExport.setAppTypeName(getAppName(companyID, allApps, app.getAppType()));
								applicationExports.add(applicationExport);
							}
						}
					}
				}
			}
		}
		
		return applicationExports;
	}
	
	private Optional<ScBasicScheduleImport> findBasicSchedule(List<ScBasicScheduleImport> basicSchedules, String empId, GeneralDate loopDate) {
		return basicSchedules.stream().filter(c -> c.getEmployeeId().equals(empId) && c.getDate().equals(loopDate)).findFirst();
	}
	
	private String getAppName(String companyID, List<AppDispName> allApps, ApplicationType appType) {
		return allApps.stream().filter(c -> c.getAppType() == appType).findFirst()
														.orElseGet(() -> new AppDispName(companyID, appType, new DispName("")))
														.getDispName().toString();
	}
	
	private String getAppAbsenceName(int holidayCode, Optional<HdAppSet> hdAppSet){
		String holidayAppTypeName ="";
		if(!hdAppSet.isPresent()){
			return holidayAppTypeName;
		}
		switch (holidayCode) {
		case 0:
			holidayAppTypeName = hdAppSet.get().getYearHdName() == null ? "" : hdAppSet.get().getYearHdName().toString();
			break;
		case 1:
			holidayAppTypeName = hdAppSet.get().getObstacleName() == null ? "" : hdAppSet.get().getObstacleName().toString();
			break;
		case 2:
			holidayAppTypeName = hdAppSet.get().getAbsenteeism()== null ? "" : hdAppSet.get().getAbsenteeism().toString();
			break;
		case 3:
			holidayAppTypeName = hdAppSet.get().getSpecialVaca() == null ? "" : hdAppSet.get().getSpecialVaca().toString();
			break;
		case 4:
			holidayAppTypeName = hdAppSet.get().getYearResig() == null ? "" : hdAppSet.get().getYearResig().toString();
			break;
		case 5:
			holidayAppTypeName = hdAppSet.get().getHdName() == null ? "" : hdAppSet.get().getHdName().toString();
			break;
		case 6:
			holidayAppTypeName = hdAppSet.get().getTimeDigest() == null ? "" : hdAppSet.get().getTimeDigest().toString();
			break;
		case 7:
			holidayAppTypeName = hdAppSet.get().getFurikyuName() == null ? "" :  hdAppSet.get().getFurikyuName().toString();
			break;
		default:
			break;
		}
		return holidayAppTypeName;
	}
	
	/**
	 * 社員、期間に一致する申請をグループ化して取得する
	 * RequestList #542
	 * @param employeeID
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<AppGroupExportDto> getApplicationGroupBySID(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		List<ApplicationExportDto> appExportLst = this.getApplicationBySID(employeeID, startDate, endDate);
		List<AppGroupExportDto> result = new ArrayList<>();
		
		Map<Object, List<AppGroupExportDto>> mapDate =  appExportLst.stream()
				.map(x -> new AppGroupExportDto(x.getAppDate(),x.getAppType(),x.getEmployeeID(),x.getAppTypeName()))
				.collect(Collectors.groupingBy(x -> Pair.of(x.getAppDate(), x.getEmployeeID())));
		mapDate.entrySet().stream().forEach(x -> {
			Map<Object, List<AppGroupExportDto>> mapDateType = x.getValue().stream().collect(Collectors.groupingBy(y -> y.getAppType()));
			mapDateType.entrySet().stream().forEach(y -> {
				if(Integer.valueOf(y.getKey().toString())==ApplicationType.ABSENCE_APPLICATION.value){
					Map<Object, List<AppGroupExportDto>> mapDateTypeAbsence = y.getValue().stream().collect(Collectors.groupingBy(z -> z.getAppTypeName()));
					mapDateTypeAbsence.entrySet().stream().forEach(z -> {
						result.add(z.getValue().get(0));
					});
				} else {
					result.add(y.getValue().get(0));
				}
			});
		});
		return result;
	}
	
	/**
	 * [No.556]遷移先申請画面一覧を取得する
	 * RequestList #556
	 * @param companyID
	 * @return
	 */
	public List<AppWithDetailExportDto> getAppWithOvertimeInfo(String companyID){
		List<AppWithDetailExportDto> result = new ArrayList<>();
		// ドメインモデル「申請表示名」を取得する
		List<AppDispName> appDispNameLst = appDispNameRepository.getAll();
		for(AppDispName appDispName : appDispNameLst){
			if(appDispName.getDispName() == null){
				continue;
			}
			if(appDispName.getAppType()==ApplicationType.OVER_TIME_APPLICATION){
				// outputパラメータに残業申請のモード別の値をセットする
				result.add(new AppWithDetailExportDto(
						ApplicationType.OVER_TIME_APPLICATION.value, 
						appDispName.getDispName().v() + "申請" + " (" + OverTimeAtr.PREOVERTIME.name + ")", 
						OverTimeAtr.PREOVERTIME.value + 1,
						null));
				result.add(new AppWithDetailExportDto(
						ApplicationType.OVER_TIME_APPLICATION.value, 
						appDispName.getDispName().v() + "申請" + " (" + OverTimeAtr.REGULAROVERTIME.name + ")", 
						OverTimeAtr.REGULAROVERTIME.value + 1,
						null));
				result.add(new AppWithDetailExportDto(
						ApplicationType.OVER_TIME_APPLICATION.value, 
						appDispName.getDispName().v() + "申請" + " (" + OverTimeAtr.ALL.name + ")", 
						OverTimeAtr.ALL.value + 1,
						null));
			} else if(appDispName.getAppType()==ApplicationType.STAMP_APPLICATION){
				// outputパラメータに打刻申請のモード別の値をセットする
				result.add(new AppWithDetailExportDto(
						ApplicationType.STAMP_APPLICATION.value, 
						appDispName.getDispName().v() + "申請" + " (" + StampRequestMode.STAMP_GO_OUT_PERMIT.name + ")", 
						null,
						StampRequestMode.STAMP_GO_OUT_PERMIT.value));
				result.add(new AppWithDetailExportDto(
						ApplicationType.STAMP_APPLICATION.value, 
						appDispName.getDispName().v() + "申請" + " (" + StampRequestMode.STAMP_WORK.name + ")", 
						null,
						StampRequestMode.STAMP_WORK.value));
				result.add(new AppWithDetailExportDto(
						ApplicationType.STAMP_APPLICATION.value, 
						appDispName.getDispName().v() + "申請" + " (" + StampRequestMode.STAMP_CANCEL.name + ")", 
						null,
						StampRequestMode.STAMP_CANCEL.value));
				result.add(new AppWithDetailExportDto(
						ApplicationType.STAMP_APPLICATION.value, 
						appDispName.getDispName().v() + "申請" + " (" + StampRequestMode.STAMP_ONLINE_RECORD.name + ")", 
						null,
						StampRequestMode.STAMP_ONLINE_RECORD.value));
				result.add(new AppWithDetailExportDto(
						ApplicationType.STAMP_APPLICATION.value, 
						appDispName.getDispName().v() + "申請" + " (" + StampRequestMode.OTHER.name + ")", 
						null,
						StampRequestMode.OTHER.value));
			} else {
				// outputパラメータに値をセットする
				result.add(new AppWithDetailExportDto(appDispName.getAppType().value, appDispName.getDispName().v() + "申請", null, null));
			}
		}
		return result;
	}
}
