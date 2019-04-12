package nts.uk.ctx.workflow.pub.spr;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalComSprExport;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalPersonSprExport;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalPhaseSprExport;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalRootStateSprExport;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalWorkplaceSprExport;
import nts.uk.ctx.workflow.pub.spr.export.JudgmentSprExportNew;

/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface SprApprovalSearchPub {
	
	public List<ApprovalComSprExport> getApprovalRootCom(String companyID, GeneralDate date, Integer employmentRootAtr,
			Integer confirmRootAtr);
	
	public List<ApprovalWorkplaceSprExport> getApprovalRootWp(String companyID, GeneralDate date, Integer employmentRootAtr,
			Integer confirmRootAtr);
	
	public List<ApprovalPersonSprExport> getApprovalRootPs(String companyID, GeneralDate date, Integer employmentRootAtr,
			Integer confirmRootAtr);
	
	public List<ApprovalPhaseSprExport> getAllIncludeApprovers(String companyId, String branchId);
	
	public List<ApprovalRootStateSprExport> getAppByApproverDate(String companyID, String approverID, GeneralDate date);
	
	/**
	 * 3.指定した社員が承認できるかの判断
	 * @param companyID 会社ID
	 * @param rootStateID インスタンスID
	 * @param employeeID 社員ID
	 * @return
	 */
	public JudgmentSprExportNew judgmentTargetPersonCanApprove(String companyID, String rootStateID, String employeeID, Integer rootType);
}
