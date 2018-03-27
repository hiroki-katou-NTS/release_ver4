package nts.uk.ctx.at.request.dom.application.applicationlist.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
@Getter
@Setter
@AllArgsConstructor
public class ApplicationFullOutput {

	private Application_New application;
	private List<ApprovalPhaseStateImport_New> lstPhaseState;
	//UNAPPROVED:5
	//APPROVED: 4
	//CANCELED: 3
	//REMAND: 2
	//DENIAL: 1
	//-: 0
	private Integer	status;
	private String agentId;
	private String apprId;
}
