package nts.uk.ctx.at.request.app.find.application.common;

import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationMetaDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApprovalPhaseStateForAppDto;
import nts.uk.ctx.at.request.app.find.application.requestofearch.GetDataAppCfDetailFinder;
import nts.uk.ctx.at.request.app.find.application.requestofearch.OutputMessageDeadline;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.EmploymentRootAtr;
import nts.uk.ctx.at.request.dom.application.PrePostAtr;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SEmpHistImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.CollectApprovalRootPatternService;
import nts.uk.ctx.at.request.dom.application.common.service.newscreen.init.StartupErrorCheckService;
import nts.uk.ctx.at.request.dom.application.common.service.other.CollectAchievement;
import nts.uk.ctx.at.request.dom.application.common.service.other.DetailScreenAppDataService;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.setting.request.application.applicationsetting.ApplicationSettingRepository;
import nts.uk.ctx.at.request.dom.setting.request.application.common.BaseDateFlg;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class AppDataDateFinder {
	
	@Inject
	private GetDataAppCfDetailFinder getDataAppCfDetailFinder;
	
	@Inject
	private CollectApprovalRootPatternService approvalRootPatternService;
	
	@Inject
	private StartupErrorCheckService startupErrorCheckService;
	
	@Inject
	private ApplicationRepository_New applicationRepository_New;
	
	@Inject
	private OtherCommonAlgorithm otherCommonAlgorithm;
	
	@Inject
	private EmployeeRequestAdapter employeeAdaptor;
	
	@Inject
	private CollectAchievement collectAchievement;
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Inject
	private ApplicationSettingRepository applicationSettingRepository;
	
	@Inject
	private DetailScreenAppDataService detailScreenAppDataService;
	
	private static final String DATE_FORMAT = "yyyy/MM/dd";
	
	public AppDateDataDto getAppDataByDate(Integer appTypeValue, String appDate, Boolean isStartUp, String appID,String employeeID, int overtimeAtr){
		String companyID = AppContexts.user().companyId();
		if(employeeID == null){
			 employeeID = AppContexts.user().employeeId();
		}
		String authorCmt = Strings.EMPTY;
		GeneralDate appGeneralDate = GeneralDate.fromString(appDate, DATE_FORMAT);
		AchievementOutput achievementOutput = new AchievementOutput(appGeneralDate, null, null, null, null, null, null);
		ApprovalRootContentImport_New approvalRootContentImport = null;
		ApplicationDto_New applicationDto = null;
		PrePostAtr defaultPrePostAtr = otherCommonAlgorithm.preliminaryJudgmentProcessing(EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), appGeneralDate,0);
		if(Strings.isNotBlank(appID)){
			Application_New application = applicationRepository_New.findByID(companyID, appID).get();
			SEmpHistImport empHistImport = employeeAdaptor.getEmpHist(
					companyID, 
					application.getEmployeeID(), 
					appGeneralDate);
			if(empHistImport==null || empHistImport.getEmploymentCode()==null){
				throw new BusinessException("Msg_426");
			}
			approvalRootContentImport = approvalRootPatternService.getApprovalRootPatternService(
					companyID, 
					employeeID, 
					EmploymentRootAtr.APPLICATION, 
					EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), 
					appGeneralDate,
					appID,
					false).getApprovalRootContentImport();
			applicationDto = ApplicationDto_New.builder()
					.version(application.getVersion())
					.companyID(application.getCompanyID())
					.applicationID(application.getAppID())
					.prePostAtr(application.getPrePostAtr().value)
					.inputDate(application.getInputDate().toString(DATE_FORMAT))
					.enteredPersonSID(application.getEnteredPersonID())
					.reversionReason(application.getReversionReason().v())
					.applicationDate(application.getAppDate().toString(DATE_FORMAT))
					.applicationReason(application.getAppReason().v())
					.applicationType(application.getAppType().value)
					.applicantSID(application.getEmployeeID())
					.reflectPlanState(application.getReflectionInformation().getStateReflection().value)
					.reflectPerState(application.getReflectionInformation().getStateReflectionReal().value)
					.reflectPlanEnforce(application.getReflectionInformation().getForcedReflection().value)
					.reflectPerEnforce(application.getReflectionInformation().getForcedReflectionReal().value)
					.reflectPlanScheReason(application.getReflectionInformation().getNotReason().map(x -> x.value).orElse(null))
					.reflectPerScheReason(application.getReflectionInformation().getNotReasonReal().map(x -> x.value).orElse(null))
					.reflectPlanTime(application.getReflectionInformation().getDateTimeReflection().map(x -> x.toString(DATE_FORMAT)).orElse(null))
					.reflectPerTime(application.getReflectionInformation().getDateTimeReflectionReal().map(x -> x.toString(DATE_FORMAT)).orElse(null))
					.build();
			authorCmt = detailScreenAppDataService.getDetailScreenAppData(appID).getAuthorComment();
		} else {
			achievementOutput = collectAchievement.getAchievement(companyID, employeeID, appGeneralDate);
			if(isStartUp.equals(Boolean.TRUE)){
				approvalRootContentImport = approvalRootPatternService.getApprovalRootPatternService(
						companyID, 
						employeeID, 
						EmploymentRootAtr.APPLICATION, 
						EnumAdaptor.valueOf(appTypeValue, ApplicationType.class), 
						appGeneralDate,
						appID,
						true).getApprovalRootContentImport();
				startupErrorCheckService.startupErrorCheck(appGeneralDate, appTypeValue, approvalRootContentImport);
			} else {
				BaseDateFlg baseDateFlg = applicationSettingRepository.getApplicationSettingByComID(companyID)
								.map(x -> x.getBaseDateFlg()).orElse(BaseDateFlg.SYSTEM_DATE);
				if(baseDateFlg.equals(BaseDateFlg.APP_DATE)){
					approvalRootContentImport = approvalRootStateAdapter.getApprovalRootContent(companyID, employeeID, appTypeValue, appGeneralDate, appID, true);
				} 
			}
		}
		OutputMessageDeadline outputMessageDeadline = getDataAppCfDetailFinder.getDataConfigDetail(new ApplicationMetaDto("", appTypeValue, appGeneralDate), overtimeAtr);
		return new AppDateDataDto(
				outputMessageDeadline, 
				approvalRootContentImport == null ? null : 
					approvalRootContentImport.getApprovalRootState().getListApprovalPhaseState()
					.stream().map(x -> ApprovalPhaseStateForAppDto.fromApprovalPhaseStateImport(x)).collect(Collectors.toList()), 
				new AchievementOutput(
						achievementOutput.getDate(), 
						achievementOutput.getWorkType(), 
						achievementOutput.getWorkTime(), 
						achievementOutput.getStartTime1(), 
						achievementOutput.getEndTime1(), 
						achievementOutput.getStartTime2(), 
						achievementOutput.getEndTime2()),
				applicationDto,
				approvalRootContentImport == null ? null : approvalRootContentImport.getErrorFlag().value,
				defaultPrePostAtr.value,
				authorCmt);
	}
	
}


