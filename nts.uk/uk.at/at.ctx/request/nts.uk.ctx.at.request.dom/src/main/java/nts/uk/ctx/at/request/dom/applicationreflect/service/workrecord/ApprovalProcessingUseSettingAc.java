package nts.uk.ctx.at.request.dom.applicationreflect.service.workrecord;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
public class ApprovalProcessingUseSettingAc {
	private String cid;
	private boolean useDayApproverConfirm;
	private boolean useMonthApproverConfirm;
	private List<String> lstJobTitleNotUse;
	private int supervisorConfirmErrorAtr;
}
