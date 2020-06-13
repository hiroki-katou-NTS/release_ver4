package nts.uk.ctx.workflow.pubimp.resultrecord.daily;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.pub.resultrecord.daily.DailyApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.daily.DailyRecordApprovalPub;
import nts.uk.ctx.workflow.pub.resultrecord.daily.DailySubjectiveStatus;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class DailyRecordApprovalPubImpl implements DailyRecordApprovalPub {

	@Override
	public List<DailySubjectiveStatus> getSubjectiveStatus(String approverEmployeeId, List<String> targetEmployeeIds,
			GeneralDate date) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<DailySubjectiveStatus> getSubjectiveStatus(String approverEmployeeId, List<String> targetEmployeeIds,
			DatePeriod period) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<DailyApprovalProgress> getApprovalProgress(List<String> targetEmployeeIds, GeneralDate date) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<DailyApprovalProgress> getApprovalProgress(List<String> targetEmployeeIds, DatePeriod period) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
