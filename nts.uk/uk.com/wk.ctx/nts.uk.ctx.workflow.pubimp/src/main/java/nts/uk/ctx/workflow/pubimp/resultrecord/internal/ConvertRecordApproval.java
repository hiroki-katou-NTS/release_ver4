package nts.uk.ctx.workflow.pubimp.resultrecord.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.val;
import nts.uk.ctx.workflow.dom.resultrecord.status.RouteConfirmStatusForOneApprover;
import nts.uk.ctx.workflow.dom.resultrecord.status.daily.RouteConfirmStatusDaily;
import nts.uk.ctx.workflow.dom.resultrecord.status.internal.RouteConfirmProgress;
import nts.uk.ctx.workflow.dom.resultrecord.status.monthly.RouteConfirmStatusMonthly;
import nts.uk.ctx.workflow.dom.service.resultrecord.ApprovalActionByEmp;
import nts.uk.ctx.workflow.dom.service.resultrecord.ReleaseDivision;
import nts.uk.ctx.workflow.pub.resultrecord.common.ApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.common.SubjectiveStatus;
import nts.uk.ctx.workflow.pub.resultrecord.common.SubjectiveStatus.Subjective;

/**
 * 実績承認状況のドメイン→Pub変換
 */
public class ConvertRecordApproval {

	public static SubjectiveStatus.Subjective statusDaily(
			RouteConfirmStatusDaily status, String subjectEmployeeId, List<String> representRequesterIds) {
		
		val subj = status.getStatusFor(subjectEmployeeId, representRequesterIds);
		
		return convert(subjectEmployeeId, subj);
	}

	public static SubjectiveStatus.Subjective statusMonthly(
			RouteConfirmStatusMonthly status, String subjectEmployeeId, List<String> representRequesterIds) {
		
		val subj = status.getStatusFor(subjectEmployeeId, representRequesterIds);
		
		return convert(subjectEmployeeId, subj);
	}
	
	private static SubjectiveStatus.Subjective convert(String subjectEmployeeId, RouteConfirmStatusForOneApprover subj) {
		
		boolean isApproved = subj.getApprovalStatus().getApprovalAction() == ApprovalActionByEmp.APPROVALED;
		boolean canApprove = subj.getApprovalStatus().getApprovalAction() != ApprovalActionByEmp.NOT_APPROVAL;
		boolean canRelease = subj.getApprovalStatus().getReleaseAtr() == ReleaseDivision.RELEASE;
		
		return new SubjectiveStatus.Subjective(subjectEmployeeId, isApproved, canApprove, canRelease);
	}
	
	public static ApprovalProgress.Progress progress(RouteConfirmProgress from) {
		return PROGRESS_MAP.get(from);
	}

	private static final Map<RouteConfirmProgress, ApprovalProgress.Progress> PROGRESS_MAP;
	static {
		PROGRESS_MAP = new HashMap<>();
		PROGRESS_MAP.put(RouteConfirmProgress.UNAPPROVED, ApprovalProgress.Progress.UNAPPROVED);
		PROGRESS_MAP.put(RouteConfirmProgress.APPROVING, ApprovalProgress.Progress.APPROVING);
		PROGRESS_MAP.put(RouteConfirmProgress.APPROVED, ApprovalProgress.Progress.APPROVED);
	}
	
}
