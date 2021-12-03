package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.approval;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.change.confirm.InformationMonth;
import nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod.AggrPeriodEachActualClosure;
import nts.uk.ctx.at.record.dom.workrecord.approval.monthly.ApprovalProgressMonthly;
import nts.uk.ctx.at.record.dom.workrecord.approval.monthly.ApprovalSubjectiveMonthlyOnWorkflow;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;

/**
 * 月の情報
 */
@AllArgsConstructor
@Data
public class InformationMonthDto {
	
	public String sid;

	//締め期間
	public AggrPeriodEachActualClosure actualClosure;

	//月の本人確認
	public List<ConfirmationMonth> lstConfirmMonth;

	//承認対象者の承認状況
	public List<ApprovalProgressMonthly> lstApprovalMonthStatus;

	//基準社員の承認対象者
	public List<ApprovalSubjectiveMonthlyOnWorkflow> lstAppRootOfEmpMonth;
	
	public InformationMonthDto(String sid, AggrPeriodEachActualClosure actualClosure, List<ConfirmationMonth> lstConfirmMonth,
			List<ApprovalProgressMonthly> lstApprovalMonthStatus) {
		super();
		this.sid = sid;
		this.actualClosure = actualClosure;
		this.lstConfirmMonth = lstConfirmMonth;
		this.lstApprovalMonthStatus = lstApprovalMonthStatus;
		this.lstAppRootOfEmpMonth = new ArrayList<>();
	}
	
	public InformationMonth toDomain(){
		return new InformationMonth(actualClosure, lstConfirmMonth, lstApprovalMonthStatus, lstAppRootOfEmpMonth);
	}
	

}
