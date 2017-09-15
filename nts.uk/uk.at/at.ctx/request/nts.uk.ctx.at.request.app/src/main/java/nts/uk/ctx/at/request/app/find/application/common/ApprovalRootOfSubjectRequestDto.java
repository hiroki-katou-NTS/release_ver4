package nts.uk.ctx.at.request.app.find.application.common;

import java.util.List;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseImport;
import nts.uk.ctx.at.request.dom.application.common.service.approvalroot.output.ApprovalPhaseOutput;
import nts.uk.ctx.at.request.dom.application.common.service.approvalroot.output.ApprovalRootOutput;

@Value
public class ApprovalRootOfSubjectRequestDto {
	/** 会社ID */
	private String companyId;
	/** 職場ID */
	private String workplaceId;
	/** 承認ID */
	public String approvalId;
	/** 社員ID */
	private String employeeId;
	/** 履歴ID */
	private String historyId;
	/** 申請種類 */
	private int applicationType;
	/** 開始日 */
	private GeneralDate startDate;
	/** 完了日 */
	private GeneralDate endDate;
	/** 分岐ID */
	private String branchId;
	/** 任意項目申請ID */
	private String anyItemApplicationId;
	/** 確認ルート種類 */
	private int confirmationRootType;
	/** 就業ルート区分 */
	private int employmentRootAtr;
	
	private List<ApprovalPhaseImport> beforeApprovers;
	
	private List<ApprovalPhaseOutput> afterApprovers;
	
	/** エラーフラグ*/
	private int errorFlag;
	 
	public static ApprovalRootOfSubjectRequestDto fromDomain(ApprovalRootOutput domain ) {
		return new ApprovalRootOfSubjectRequestDto(
				domain.getCompanyId(),
				domain.getWorkplaceId(), 
				domain.getApprovalId(), 
				domain.getEmployeeId(), 
				domain.getHistoryId(), 
				domain.getApplicationType(), 
				domain.getStartDate(), 
				domain.getEndDate(), 
				domain.getBranchId(), 
				domain.getAnyItemApplicationId(), 
				domain.getConfirmationRootType(), 
				domain.getEmploymentRootAtr(), 
				domain.getBeforeApprovers(),
				domain.getAfterApprovers(),
				domain.getErrorFlag().value
				);
	}
}
