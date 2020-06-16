package nts.uk.ctx.at.record.dom.workrecord.approval.monthly;

import java.util.List;

import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

public interface ApprovalStatusMonthlyAdapter {

	List<ApprovalProgressMonthly> getProgress(
			List<String> targetEmployeeIds, ClosureMonth closureMonth, DatePeriod period);
	
	List<ApprovalSubjectiveMonthlyOnWorkflow> getSubjective(
			String subjectEmployeeId,
			List<String> targetEmployeeIds,
			ClosureMonth closureMonth,
			DatePeriod period);
}
