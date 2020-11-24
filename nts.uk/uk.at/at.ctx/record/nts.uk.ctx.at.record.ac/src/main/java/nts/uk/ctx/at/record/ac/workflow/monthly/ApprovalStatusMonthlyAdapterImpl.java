package nts.uk.ctx.at.record.ac.workflow.monthly;

import static java.util.stream.Collectors.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalProgress;
import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalSubjective;
import nts.uk.ctx.at.record.dom.workrecord.approval.monthly.ApprovalProgressMonthly;
import nts.uk.ctx.at.record.dom.workrecord.approval.monthly.ApprovalStatusMonthlyAdapter;
import nts.uk.ctx.at.record.dom.workrecord.approval.monthly.ApprovalSubjectiveMonthlyOnWorkflow;
import nts.uk.ctx.workflow.pub.resultrecord.common.ApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.monthly.MonthlyApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.monthly.MonthlyRecordApprovalPub;
import nts.uk.ctx.workflow.pub.resultrecord.monthly.MonthlySubjectiveStatus;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

@Stateless
public class ApprovalStatusMonthlyAdapterImpl implements ApprovalStatusMonthlyAdapter {

	@Inject
	private MonthlyRecordApprovalPub pub;

	@Override
	public List<ApprovalProgressMonthly> getProgress(List<String> targetEmployeeIds, ClosureMonth closureMonth, DatePeriod period) {

		return pub.getApprovalProgress(targetEmployeeIds, closureMonth, period).stream()
				.map(p -> convert(p))
				.collect(toList());
	}

	private static ApprovalProgressMonthly convert(MonthlyApprovalProgress published) {
		return new ApprovalProgressMonthly(
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
	public List<ApprovalSubjectiveMonthlyOnWorkflow> getSubjective(
			String subjectEmployeeId, List<String> targetEmployeeIds, ClosureMonth closureMonth, DatePeriod period) {

		return pub.getSubjectiveStatus(subjectEmployeeId, targetEmployeeIds, closureMonth, period).stream()
				.map(p -> convert(p))
				.collect(toList());
	}

	private static ApprovalSubjectiveMonthlyOnWorkflow convert(MonthlySubjectiveStatus published) {
		return new ApprovalSubjectiveMonthlyOnWorkflow(
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