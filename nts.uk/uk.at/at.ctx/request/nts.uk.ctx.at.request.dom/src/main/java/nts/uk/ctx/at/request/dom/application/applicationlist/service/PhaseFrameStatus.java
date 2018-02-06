package nts.uk.ctx.at.request.dom.application.applicationlist.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalBehaviorAtrImport_New;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhaseFrameStatus {

	private ApprovalBehaviorAtrImport_New phaseStatus;
	
	private ApprovalBehaviorAtrImport_New frameStatus;
	
	private String agentId;
}
