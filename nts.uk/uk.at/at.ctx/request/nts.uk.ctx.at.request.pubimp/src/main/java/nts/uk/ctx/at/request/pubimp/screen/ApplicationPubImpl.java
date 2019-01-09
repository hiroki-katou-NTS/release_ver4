package nts.uk.ctx.at.request.pubimp.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.UseAtr;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.common.adapter.closure.PresentClosingPeriodImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.closure.RqClosureAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.shift.businesscalendar.daycalendar.ObtainDeadlineDateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WkpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.IAppWorkChangeRepository;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSetRepository;
import nts.uk.ctx.at.request.dom.setting.company.displayname.AppDispNameRepository;
//import nts.uk.ctx.at.request.dom.setting.company.displayname.HdAppDispNameRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.ApplicationDeadline;
import nts.uk.ctx.at.request.dom.setting.request.application.ApplicationDeadlineRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.DeadlineCriteria;
import nts.uk.ctx.at.request.pub.screen.AppGroupExport;
import nts.uk.ctx.at.request.pub.screen.ApplicationDeadlineExport;
import nts.uk.ctx.at.request.pub.screen.ApplicationExport;
import nts.uk.ctx.at.request.pub.screen.ApplicationPub;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.WorkStyle;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class ApplicationPubImpl implements ApplicationPub {
	@Inject
	private ApplicationRepository_New applicationRepository_New;
	@Inject
	private AppDispNameRepository appDispNameRepository;
//	@Inject
//	private HdAppDispNameRepository hdAppDispNameRepository;
	@Inject
	private ApplicationDeadlineRepository appDeadlineRepository;
	@Inject
	private RqClosureAdapter rqClosureAdapter;
	@Inject
	private WorkplaceAdapter workplaceAdapter;
	@Inject
	private ObtainDeadlineDateAdapter obtainDeadlineDateAdapter;
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
	
	@Override
	public List<ApplicationExport> getApplicationBySID(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		String companyID = AppContexts.user().companyId();
		List<ApplicationExport> applicationExports = new ArrayList<>();
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
				ApplicationExport applicationExport = new ApplicationExport();
				applicationExport.setAppDate(app.getAppDate());
				applicationExport.setAppType(app.getAppType().value);
				applicationExport.setEmployeeID(app.getEmployeeID());
				applicationExport.setReflectState(app.getReflectionInformation().getStateReflectionReal().value);
				applicationExport.setAppTypeName(appDispNameRepository.getDisplay(app.getAppType().value).isPresent() ? appDispNameRepository.getDisplay(app.getAppType().value).get().getDispName().toString() : "" );
				applicationExports.add(applicationExport);
			} else {
				for(GeneralDate loopDate = app.getStartDate().get(); loopDate.beforeOrEquals(app.getEndDate().get()); loopDate = loopDate.addDays(1)){
					ApplicationExport applicationExport = new ApplicationExport();
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
				ApplicationExport applicationExport = new ApplicationExport();
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
					ApplicationExport applicationExport = new ApplicationExport();
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
				ApplicationExport applicationExport = new ApplicationExport();
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
						ApplicationExport applicationExport = new ApplicationExport();
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
							ApplicationExport applicationExport = new ApplicationExport();
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
							ApplicationExport applicationExport = new ApplicationExport();
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
	@Override
	public ApplicationDeadlineExport getApplicationDeadline(String companyID, Integer closureID) {
		String employeeId = AppContexts.user().employeeId();
		ApplicationDeadlineExport result = new ApplicationDeadlineExport();
		// ドメインモデル「申請締切設定」．利用区分をチェックする(check利用区分)
		Optional<ApplicationDeadline> appDeadlineOp = appDeadlineRepository.getDeadlineByClosureId(companyID,
				closureID);
		if (!appDeadlineOp.isPresent()) {
			throw new RuntimeException(
					"Not found ApplicationDeadline in table KRQST_APP_DEADLINE, closureID =" + closureID);
		}
		ApplicationDeadline appDeadline = appDeadlineOp.get();

		GeneralDate systemDate = GeneralDate.today();
		// ドメインモデル「申請締切設定」．利用区分をチェックする(check利用区分)
		if (appDeadline.getUserAtr().equals(UseAtr.NOTUSE)) {
			result.setUseApplicationDeadline(false);
			return result;
		}
		Optional<PresentClosingPeriodImport> presentClosingPeriodImport = this.rqClosureAdapter.getClosureById(companyID, closureID);
		if(presentClosingPeriodImport.isPresent()){
				GeneralDate deadline = null;
				// ドメインモデル「申請締切設定」．締切基準をチェックする
				if(appDeadline.getDeadlineCriteria().equals(DeadlineCriteria.WORKING_DAY)) {
					// アルゴリズム「社員所属職場履歴を取得」を実行する
					WkpHistImport wkpHistImport = workplaceAdapter.findWkpBySid(employeeId, systemDate);
					// アルゴリズム「締切日を取得する」を実行する
					// nếu wkpHistImport = null thì xem QA http://192.168.50.4:3000/issues/97192
					if(wkpHistImport==null || Strings.isBlank(wkpHistImport.getWorkplaceId())){
						throw new BusinessException("EA No.2110: 終了状態：申請締切日取得エラー");
					}
					deadline = obtainDeadlineDateAdapter.obtainDeadlineDate(
							presentClosingPeriodImport.get().getClosureEndDate(), 
							appDeadline.getDeadline().v(), 
							wkpHistImport.getWorkplaceId(), 
							companyID);
				} else {
					// 「申請締切設定」．締切基準が暦日
					deadline = presentClosingPeriodImport.get().getClosureEndDate().addDays(appDeadline.getDeadline().v());
				}
				result.setDateDeadline(deadline);
		}
		result.setUseApplicationDeadline(true);
		return result;
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
	@Override
	public List<AppGroupExport> getApplicationGroupBySID(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		List<ApplicationExport> appExportLst = this.getApplicationBySID(employeeID, startDate, endDate);
		return appExportLst.stream().map(x -> new AppGroupExport(x.getAppDate(),x.getAppType(),x.getEmployeeID(),x.getAppTypeName()))
				.collect(Collectors.groupingBy(x -> x.getAppDate())).entrySet().stream().map(x -> {
					return x.getValue().stream().collect(Collectors.groupingBy(y -> y.getAppType())).entrySet()
							.stream().map(y -> y.getValue().get(0)).collect(Collectors.toList()).get(0);
				}).collect(Collectors.toList());
	}

	@Override
	public List<AppGroupExport> getApplicationGroupBySID(List<String> employeeID, GeneralDate startDate,
			GeneralDate endDate) {
		List<ApplicationExport> appExportLst = this.getApplicationBySID(employeeID, startDate, endDate);
		List<AppGroupExport> result = new ArrayList<>();
		Map<Object, List<AppGroupExport>> mapDate =  appExportLst.stream()
				.map(x -> new AppGroupExport(x.getAppDate(),x.getAppType(),x.getEmployeeID(),x.getAppTypeName()))
				.collect(Collectors.groupingBy(x -> x.getAppDate()));
		mapDate.entrySet().stream().forEach(x -> {
			Map<Object, List<AppGroupExport>> mapDateType = x.getValue().stream().collect(Collectors.groupingBy(y -> y.getAppType()));
			mapDateType.entrySet().stream().forEach(y -> {
				result.add(y.getValue().get(0));
			});
		});
		return result;
	}

}
