package nts.uk.ctx.at.record.dom.workrecord.approval.daily;

import java.util.List;

import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface ApprovalStatusDailyAdapter {
	
	List<ApprovalProgressDaily> getProgress(
			List<String> targetEmployeeIds, DatePeriod period);

	List<ApprovalSubjectiveDailyOnWorkflow> getSubjective(
			String subjectEmployeeId,
			List<String> targetEmployeeIds,
			DatePeriod period);
}
