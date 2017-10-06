package nts.uk.ctx.at.request.app.find.application.common;


import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.request.dom.application.common.adapter.bs.EmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalRootImport;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class GetDataApprovalRootOfSubjectRequest {
	
	@Inject 
	private ApprovalRootAdapter approvalRootRepo;

	
	public List<ApprovalRootOfSubjectRequestDto> getApprovalRootOfSubjectRequest(ObjApprovalRootInput objApprovalRootInput){
		String companyID = AppContexts.user().companyId();
		return this.approvalRootRepo.getApprovalRootOfSubjectRequest( companyID,
				objApprovalRootInput.getSid(), objApprovalRootInput.getEmploymentRootAtr(), 
				objApprovalRootInput.getAppType(),objApprovalRootInput.getStandardDate())
				.stream()
				.map(c->ApprovalRootOfSubjectRequestDto.fromDomain(c))
				.collect(Collectors.toList());
		
	}

}
