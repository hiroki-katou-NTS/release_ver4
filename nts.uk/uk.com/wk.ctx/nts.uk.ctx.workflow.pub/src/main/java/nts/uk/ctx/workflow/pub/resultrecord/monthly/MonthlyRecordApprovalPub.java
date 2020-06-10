package nts.uk.ctx.workflow.pub.resultrecord.monthly;

import java.util.List;

import nts.uk.shr.com.time.closure.ClosureMonth;

/**
 * 月の実績の承認に関するPublish
 */
public interface MonthlyRecordApprovalPub {

	/**
	 * 承認者から見た実績承認の状況を取得する
	 * @param approverEmployeeId 承認者
	 * @param targetEmployeeIds 対象社員
	 * @param closureMonth 締め月
	 * @return
	 */
	List<MonthlySubjectiveStatus> getSubjectiveStatus(
			String approverEmployeeId, List<String> targetEmployeeIds, ClosureMonth closureMonth);
	
	/**
	 * 指定社員の実績承認の進捗状況を取得する
	 * @param targetEmployeeIds 対象社員
	 * @param closureMonth 締め月
	 * @return
	 */
	List<MonthlyApprovalProgress> getApprovalProgress(
			List<String> targetEmployeeIds, ClosureMonth closureMonth);
}
