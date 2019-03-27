package nts.uk.ctx.workflow.dom.resultrecord.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.agent.Agent;
import nts.uk.ctx.workflow.dom.approvermanagement.setting.PrincipalApprovalFlg;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.CompanyApprovalRoot;
import nts.uk.ctx.workflow.dom.approvermanagement.workroot.PersonApprovalRoot;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface CreateDailyApprover {
	
	/**
	 * 指定社員の中間データを作成する
	 * @param employeeID
	 * @param rootType
	 * @param recordDate
	 */
	public AppRootInstanceContent createDailyApprover(String employeeID, RecordRootType rootType, GeneralDate recordDate, GeneralDate closureStartDate);
	
	public AppRootInstanceContent createDailyApprover512(String employeeID, RecordRootType rootType, GeneralDate recordDate, GeneralDate closureStartDate,
			Optional<PersonApprovalRoot> opPsConfirm, Optional<PersonApprovalRoot> opPsCom, 
			Optional<CompanyApprovalRoot> opComConfirm, Optional<CompanyApprovalRoot> opComCom,
			Map<String, Map<GeneralDate, Boolean>> statusLst, List<Agent> agentLst, PrincipalApprovalFlg principalApprovalFlg);
}
