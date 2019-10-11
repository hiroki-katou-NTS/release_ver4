package nts.uk.ctx.at.record.pub.dailyperform.appreflect;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
public class ApprovalProcessingUseSettingPub {
	private String cid;
	private boolean useDayApproverConfirm;
	private boolean useMonthApproverConfirm;
	private List<String> lstJobTitleNotUse;
	private int supervisorConfirmErrorAtr;

}
