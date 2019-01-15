package nts.uk.ctx.at.request.app.find.application.applicationlist;

/*import nts.uk.ctx.at.request.dom.setting.company.displayname.HdAppDispNameRepository;*/
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleImport;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.IAppWorkChangeRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.shr.com.context.AppContexts;

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
	private BasicScheduleService basicScheduleService;
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
		for(Application_New app : applicationExcessHoliday){
			if(!(app.getStartDate().isPresent()&&app.getEndDate().isPresent())){
				ApplicationExportDto applicationExport = new ApplicationExportDto();
				applicationExport.setAppDate(app.getAppDate());
				applicationExport.setAppType(app.getAppType().value);
				applicationExport.setEmployeeID(app.getEmployeeID());
				applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
				applicationExport.setAppTypeName(appDispNameRepository.getDisplay(app.getAppType().value).isPresent() ? appDispNameRepository.getDisplay(app.getAppType().value).get().getDispName().toString() : "" );
				applicationExports.add(applicationExport);
			} else {
				for(GeneralDate loopDate = app.getStartDate().get(); loopDate.beforeOrEquals(app.getEndDate().get()); loopDate = loopDate.addDays(1)){
					ApplicationExportDto applicationExport = new ApplicationExportDto();
					applicationExport.setAppDate(loopDate);
					applicationExport.setAppType(app.getAppType().value);
					applicationExport.setEmployeeID(app.getEmployeeID());
					applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
					applicationExport.setAppTypeName(appDispNameRepository.getDisplay(app.getAppType().value).isPresent() ? appDispNameRepository.getDisplay(app.getAppType().value).get().getDispName().toString() : "" );
					applicationExports.add(applicationExport);
				}
			}
		}
		List<Application_New> applicationHoliday = application.stream().filter(x -> x.getAppType().value == ApplicationType.ABSENCE_APPLICATION.value).collect(Collectors.toList());
		
		for(Application_New app : applicationHoliday){
			if(!(app.getStartDate().isPresent()&&app.getEndDate().isPresent())){
				Optional<AppAbsence> optAppAbsence = appAbsenceRepository.getAbsenceById(app.getCompanyID(), app.getAppID());
				ApplicationExportDto applicationExport = new ApplicationExportDto();
				applicationExport.setAppDate(app.getAppDate());
				applicationExport.setAppType(app.getAppType().value);
				applicationExport.setEmployeeID(app.getEmployeeID());
				applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
				// ドメインモデル「休暇申請種類表示名」を取得する
				applicationExport.setAppTypeName(this.getAppAbsenceName(optAppAbsence.get().getHolidayAppType().value));
				applicationExports.add(applicationExport);
			} else {
				for(GeneralDate loopDate = app.getStartDate().get(); loopDate.beforeOrEquals(app.getEndDate().get()); loopDate = loopDate.addDays(1)){
					Optional<AppAbsence> optAppAbsence = appAbsenceRepository.getAbsenceById(app.getCompanyID(), app.getAppID());
					ApplicationExportDto applicationExport = new ApplicationExportDto();
					applicationExport.setAppDate(loopDate);
					applicationExport.setAppType(app.getAppType().value);
					applicationExport.setEmployeeID(app.getEmployeeID());
					applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
					// ドメインモデル「休暇申請種類表示名」を取得する
					applicationExport.setAppTypeName(this.getAppAbsenceName(optAppAbsence.get().getHolidayAppType().value));
					applicationExports.add(applicationExport);
				}
			}
		}
		List<Application_New> appWorkChangeLst = application.stream()
				.filter(x -> x.getAppType().value == ApplicationType.WORK_CHANGE_APPLICATION.value)
				.collect(Collectors.toList());
		for(Application_New app : appWorkChangeLst){
			if(!(app.getStartDate().isPresent()&&app.getEndDate().isPresent())){
				ApplicationExportDto applicationExport = new ApplicationExportDto();
				applicationExport.setAppDate(app.getAppDate());
				applicationExport.setAppType(app.getAppType().value);
				applicationExport.setEmployeeID(app.getEmployeeID());
				applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
				applicationExport.setAppTypeName(appDispNameRepository.getDisplay(app.getAppType().value).isPresent() ? appDispNameRepository.getDisplay(app.getAppType().value).get().getDispName().toString() : "" );
				applicationExports.add(applicationExport);
			} else {
				// 申請種類＝勤務変更申請　＆　休日を除外するの場合
				AppWorkChange appWorkChange = appWorkChangeRepository.getAppworkChangeById(companyID, app.getAppID()).get();
				for(GeneralDate loopDate = app.getStartDate().get(); loopDate.beforeOrEquals(app.getEndDate().get()); loopDate = loopDate.addDays(1)){
					if(appWorkChange.getExcludeHolidayAtr()==0){
						ApplicationExportDto applicationExport = new ApplicationExportDto();
						applicationExport.setAppDate(loopDate);
						applicationExport.setAppType(app.getAppType().value);
						applicationExport.setEmployeeID(app.getEmployeeID());
						applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
						applicationExport.setAppTypeName(appDispNameRepository.getDisplay(app.getAppType().value).isPresent() ? appDispNameRepository.getDisplay(app.getAppType().value).get().getDispName().toString() : "" );
						applicationExports.add(applicationExport);
					} else {
						// Imported「勤務予定基本情報」を取得する
						Optional<ScBasicScheduleImport> opScBasicScheduleImport = scBasicScheduleAdapter.findByID(app.getEmployeeID(), loopDate);
						if(!opScBasicScheduleImport.isPresent()){
							ApplicationExportDto applicationExport = new ApplicationExportDto();
							applicationExport.setAppDate(loopDate);
							applicationExport.setAppType(app.getAppType().value);
							applicationExport.setEmployeeID(app.getEmployeeID());
							applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
							applicationExport.setAppTypeName(appDispNameRepository.getDisplay(app.getAppType().value).isPresent() ? appDispNameRepository.getDisplay(app.getAppType().value).get().getDispName().toString() : "" );
							applicationExports.add(applicationExport);
							continue;
						}
						// 1日半日出勤・1日休日系の判定
						WorkStyle workStyle = basicScheduleService.checkWorkDay(opScBasicScheduleImport.get().getWorkTypeCode());
						if(workStyle.value!=0){
							ApplicationExportDto applicationExport = new ApplicationExportDto();
							applicationExport.setAppDate(loopDate);
							applicationExport.setAppType(app.getAppType().value);
							applicationExport.setEmployeeID(app.getEmployeeID());
							applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
							applicationExport.setAppTypeName(appDispNameRepository.getDisplay(app.getAppType().value).isPresent() ? appDispNameRepository.getDisplay(app.getAppType().value).get().getDispName().toString() : "" );
							applicationExports.add(applicationExport);
						}
					}
				}
			}
		}
		
		return applicationExports;
	}
	private String getAppAbsenceName(int holidayCode){
		Optional<HdAppSet> hdAppSet = this.hdAppSetRepository.getAll();
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
		return appExportLst.stream().map(x -> new AppGroupExportDto(x.getAppDate(),x.getAppType(),x.getEmployeeID(),x.getAppTypeName()))
				.collect(Collectors.groupingBy(x -> x.getAppDate())).entrySet().stream().map(x -> {
					return x.getValue().stream().collect(Collectors.groupingBy(y -> y.getAppType())).entrySet()
							.stream().map(y -> y.getValue().get(0)).collect(Collectors.toList()).get(0);
				}).collect(Collectors.toList());
	}
}
