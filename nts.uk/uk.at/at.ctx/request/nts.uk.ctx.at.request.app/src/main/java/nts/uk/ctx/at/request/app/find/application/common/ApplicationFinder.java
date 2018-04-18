package nts.uk.ctx.at.request.app.find.application.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.i18n.I18NText;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationMetaDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationPeriodDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationRemandDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationSendDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApprovalFrameForRemandDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.DetailApproverDto;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.PesionInforImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.init.DetailAppCommonSetService;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailapplicationapproval.ApprovalTemp;
import nts.uk.ctx.at.request.dom.setting.company.mailsetting.mailapplicationapproval.ApprovalTempRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class ApplicationFinder {
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Inject
	private DetailAppCommonSetService detailAppCommonSetService;
	
	@Inject
	private EmployeeRequestAdapter employeeRequestAdapter;

	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
//	@Inject
//	private JobtitleSearchSetAdapter jobtitleSearchSetAdapter;
	
	@Inject
	private ApprovalTempRepository appRep;
	
	public List<ApplicationMetaDto> getAppbyDate(ApplicationPeriodDto dto){
		String companyID = AppContexts.user().companyId();
		return this.applicationRepository.getApplicationIdByDate(companyID, dto.getStartDate(), dto.getEndDate())
				.stream().map(c -> { return new ApplicationMetaDto(c.getAppID(), c.getAppType().value, c.getAppDate()); })
				.collect(Collectors.toList());
	}
	public ApplicationRemandDto getAppByIdForRemand(String appID){
		String companyID = AppContexts.user().companyId();
		Optional<Application_New> application_New = this.applicationRepository.findByID(companyID, appID);
		ApprovalRootContentImport_New approvalRootContentImport = approvalRootStateAdapter.getApprovalRootContent(companyID, null, null, null, appID, false);
		String applicantPosition = "主任";
		List<ApprovalFrameForRemandDto> listApprovalFrame = new ArrayList<ApprovalFrameForRemandDto>();
		approvalRootContentImport.getApprovalRootState().getListApprovalPhaseState().forEach(x -> { 
			x.getListApprovalFrame().forEach(y -> {
				List<DetailApproverDto> listApprover = new ArrayList<DetailApproverDto>();
				y.getListApprover().forEach(z -> {
					listApprover.add(new DetailApproverDto(z.getApproverID(), z.getApproverName(), z.getRepresenterID(), z.getRepresenterName(), "課長"));
				});
				listApprovalFrame.add(new ApprovalFrameForRemandDto(y.getPhaseOrder(), y.getApprovalReason(), listApprover));
			});
		});
		return ApplicationRemandDto.fromDomain(appID, application_New.get().getVersion(), approvalRootContentImport.getErrorFlag().value, applicantPosition,  application_New.isPresent() ? employeeRequestAdapter.getEmployeeInfor(application_New.map(Application_New::getEmployeeID).orElse("")) : null , listApprovalFrame);
	}
	public ApplicationSendDto getAppByIdForSend(String appID){
		String companyID = AppContexts.user().companyId();
		Optional<Application_New> application_New = this.applicationRepository.findByID(companyID, appID);
		ApprovalRootContentImport_New approvalRootContentImport = approvalRootStateAdapter.getApprovalRootContent(companyID, null, null, null, appID, false);
		List<PesionInforImport> listApproverDetail = new ArrayList<PesionInforImport>();
		approvalRootContentImport.getApprovalRootState().getListApprovalPhaseState().forEach(x -> { 
			x.getListApprovalFrame().forEach(y -> {
				y.getListApprover().forEach(z -> {
					listApproverDetail.add(employeeRequestAdapter.getEmployeeInfor(z.getApproverID()));
				});
			});
		});
		Optional<ApprovalTemp> appTemp = appRep.getAppTem();
		String appTempAsStr = "";
		if (appTemp.isPresent()){
			if (!Objects.isNull(appTemp.get().getContent())) {
				appTempAsStr = appTemp.get().getContent().v();
			}
		}
		if (application_New.isPresent()){
			Application_New app = application_New.get();
			String loginName = "D00001";
			String loginMail = "D00001@nittsusystime.co.jp";
			String empName = "D00001 name";
			String appContent = "";
			String mailContentToSend = I18NText.getText("Msg_703",
					loginName, appTempAsStr,
					GeneralDate.today().toString(), app.getAppType().nameId,
					empName, app.getAppDate().toLocalDate().toString(),
					appContent, loginName, loginMail);
			return ApplicationSendDto.fromDomain(ApplicationDto_New.fromDomain(app), mailContentToSend, approvalRootContentImport, listApproverDetail, employeeRequestAdapter.getEmployeeInfor(app.getEmployeeID()));
		}
		return null;
	}
	public ApplicationMetaDto getAppByID(String appID){
		String companyID = AppContexts.user().companyId();
		return ApplicationMetaDto.fromDomain(detailAppCommonSetService.getDetailAppCommonSet(companyID, appID));
	}
	
	public List<ApplicationMetaDto> getListAppInfo(List<String> listAppID){
		String companyID = AppContexts.user().companyId();
		return detailAppCommonSetService.getListDetailAppCommonSet(companyID, listAppID)
				.stream().map(x -> ApplicationMetaDto.fromDomain(x)).collect(Collectors.toList());
	}
}
