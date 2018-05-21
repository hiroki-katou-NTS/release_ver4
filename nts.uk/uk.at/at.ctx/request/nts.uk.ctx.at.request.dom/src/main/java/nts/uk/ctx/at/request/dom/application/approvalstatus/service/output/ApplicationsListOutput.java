package nts.uk.ctx.at.request.dom.application.approvalstatus.service.output;

import java.util.List;
import java.util.Optional;

import lombok.Value;
import nts.uk.ctx.at.request.dom.application.applist.service.AppCompltLeaveSync;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;

@Value
public class ApplicationsListOutput {
	List<ApprovalSttAppDetail> approvalSttAppDetail; 
	Optional<HdAppSet> lstHdAppSet;
	//List<AppCompltLeaveSync> listSync;
}
