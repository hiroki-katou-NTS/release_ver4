package nts.uk.ctx.at.request.app.find.application.common;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationMetaDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationPeriodDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationRemandDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApplicationSendDto;
import nts.uk.ctx.at.request.app.find.application.common.dto.ApprovalFrameForRemandDto;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeRequestAdapter;
<<<<<<< HEAD
import nts.uk.ctx.at.request.dom.application.common.service.application.IApplicationForRemandService;
import nts.uk.ctx.at.request.dom.application.common.service.application.IApplicationForSendService;
import nts.uk.ctx.at.request.dom.application.common.service.application.output.ApplicationForRemandOutput;
import nts.uk.ctx.at.request.dom.application.common.service.application.output.ApplicationForSendOutput;
=======
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.PesionInforImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootContentImport_New;
>>>>>>> delivery/release_user
import nts.uk.ctx.at.request.dom.application.common.service.detailscreen.init.DetailAppCommonSetService;
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
	private IApplicationForSendService appForSendService;
	
	@Inject 
	private IApplicationForRemandService appForRemandService;

	public List<ApplicationMetaDto> getAppbyDate(ApplicationPeriodDto dto) {
		String companyID = AppContexts.user().companyId();
		return this.applicationRepository.getApplicationIdByDate(companyID, dto.getStartDate(), dto.getEndDate())
				.stream().map(c -> {
					return new ApplicationMetaDto(c.getAppID(), c.getAppType().value, c.getAppDate());
				}).collect(Collectors.toList());
	}

	public ApplicationRemandDto getAppByIdForRemand(String appID) {
		ApplicationForRemandOutput appOutput = appForRemandService.getApplicationForRemand(appID);
		if (!Objects.isNull(appOutput)){
			return ApplicationRemandDto.fromDomain(appID, appOutput.getVersion(),
					appOutput.getErrorFlag(), appOutput.getApplicantPosition(),
					appOutput.getApplicant(),
					appOutput.getApprovalFrameDtoForRemand().stream().map(x ->{
						return ApprovalFrameForRemandDto.fromDomain(x);
					}).collect(Collectors.toList()));
		}
		return null;
	}

	public ApplicationSendDto getAppByIdForSend(String appID){
		ApplicationForSendOutput appOutput = appForSendService.getApplicationForSend(appID);
		if (!Objects.isNull(appOutput)){
			return ApplicationSendDto.fromDomain(ApplicationDto_New.fromDomain(appOutput.getApplication()), appOutput.getMailTemplate(), appOutput.getApprovalRoot(), appOutput.getListApprover(), employeeRequestAdapter.getEmployeeInfor(appOutput.getApplication().getEmployeeID()));
		}
		return null;
	}

	public ApplicationMetaDto getAppByID(String appID){
		String companyID = AppContexts.user().companyId();
		return ApplicationMetaDto.fromDomain(detailAppCommonSetService.getDetailAppCommonSet(companyID, appID));
	}
<<<<<<< HEAD

=======
	
>>>>>>> delivery/release_user
	public List<ApplicationMetaDto> getListAppInfo(List<String> listAppID){
		String companyID = AppContexts.user().companyId();
		return detailAppCommonSetService.getListDetailAppCommonSet(companyID, listAppID)
				.stream().map(x -> ApplicationMetaDto.fromDomain(x)).collect(Collectors.toList());
	}
}
