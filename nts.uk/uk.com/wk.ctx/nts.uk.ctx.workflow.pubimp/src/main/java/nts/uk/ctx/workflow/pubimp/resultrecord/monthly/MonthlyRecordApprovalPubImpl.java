package nts.uk.ctx.workflow.pubimp.resultrecord.monthly;

import java.util.List;

import javax.ejb.Stateless;

import nts.uk.ctx.workflow.pub.resultrecord.monthly.MonthlyApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.monthly.MonthlyRecordApprovalPub;
import nts.uk.ctx.workflow.pub.resultrecord.monthly.MonthlySubjectiveStatus;
import nts.uk.shr.com.time.closure.ClosureMonth;

@Stateless
public class MonthlyRecordApprovalPubImpl implements MonthlyRecordApprovalPub {

	@Override
	public List<MonthlySubjectiveStatus> getSubjectiveStatus(String approverEmployeeId, List<String> targetEmployeeIds,
			ClosureMonth closureMonth) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<MonthlyApprovalProgress> getApprovalProgress(List<String> targetEmployeeIds,
			ClosureMonth closureMonth) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
