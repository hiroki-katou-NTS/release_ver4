package nts.uk.ctx.at.record.ac.workflow.daily;

import static java.util.stream.Collectors.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalProgress;
import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalSubjective;
import nts.uk.ctx.at.record.dom.workrecord.approval.daily.ApprovalProgressDaily;
import nts.uk.ctx.at.record.dom.workrecord.approval.daily.ApprovalStatusDailyAdapter;
import nts.uk.ctx.at.record.dom.workrecord.approval.daily.ApprovalSubjectiveDailyOnWorkflow;
import nts.uk.ctx.workflow.pub.resultrecord.common.ApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.daily.DailyApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.daily.DailyRecordApprovalPub;
import nts.uk.ctx.workflow.pub.resultrecord.daily.DailySubjectiveStatus;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class ApprovalStatusDailyAdapterImpl implements ApprovalStatusDailyAdapter {

	@Inject
	private DailyRecordApprovalPub pub;
	
	@Override
	public List<ApprovalProgressDaily> getProgress(List<String> targetEmployeeIds, DatePeriod period) {
		
		return pub.getApprovalProgress(targetEmployeeIds, period).stream()
				.map(p -> convert(p))
				.collect(toList());
	}
	
	private static ApprovalProgressDaily convert(DailyApprovalProgress published) {
		return new ApprovalProgressDaily(
				published.getEmployeeId(),
				published.getDate(),
				MAP_PROGRESS.get(published.getProgress()));
	}
	
	private static final Map<ApprovalProgress.Progress, RecordApprovalProgress.Progress> MAP_PROGRESS;
	static {
		MAP_PROGRESS = new HashMap<>();
		MAP_PROGRESS.put(ApprovalProgress.Progress.UNAPPROVED, RecordApprovalProgress.Progress.UNAPPROVED);
		MAP_PROGRESS.put(ApprovalProgress.Progress.APPROVING, RecordApprovalProgress.Progress.APPROVING);
		MAP_PROGRESS.put(ApprovalProgress.Progress.APPROVED, RecordApprovalProgress.Progress.APPROVED);
	}

	@Override
	public List<ApprovalSubjectiveDailyOnWorkflow> getSubjective(
			String subjectEmployeeId, List<String> targetEmployeeIds, DatePeriod period) {
		
		return pub.getSubjectiveStatus(subjectEmployeeId, targetEmployeeIds, period).stream()
				.map(p -> convert(p))
				.collect(toList());
	}

	private static ApprovalSubjectiveDailyOnWorkflow convert(DailySubjectiveStatus published) {
		return new ApprovalSubjectiveDailyOnWorkflow(
				published.getTargetEmployeeId(),
				published.getDate(),
				MAP_PROGRESS.get(published.getProgress()),
				new RecordApprovalSubjective.Subjective(
						published.getSubjective().getApproverEmployeeId(),
						published.getSubjective().isApproved(),
						published.getSubjective().isCanExecute(),
						published.getSubjective().isCanRelease()));
	}
}
