package nts.uk.ctx.workflow.pub.resultrecord.daily;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

public interface DailyRecordApprovalPub {

	/**
	 * 承認者から見た実績承認の状況を取得する
	 * @param approverEmployeeId 承認者
	 * @param targetEmployeeIds 対象社員
	 * @param date 年月日
	 * @return
	 */
	List<DailySubjectiveStatus> getSubjectiveStatus(
			String approverEmployeeId, List<String> targetEmployeeIds, GeneralDate date);

	/**
	 * 承認者から見た実績承認の状況を取得する
	 * @param approverEmployeeId 承認者
	 * @param targetEmployeeIds 対象社員
	 * @param period 期間
	 * @return
	 */
	List<DailySubjectiveStatus> getSubjectiveStatus(
			String approverEmployeeId, List<String> targetEmployeeIds, DatePeriod period);
	
	/**
	 * 指定社員の実績承認の進捗状況を取得する
	 * @param targetEmployeeIds 対象社員
	 * @param date 年月日
	 * @return
	 */
	List<DailyApprovalProgress> getApprovalProgress(
			List<String> targetEmployeeIds, GeneralDate date);
	
	/**
	 * 指定社員の実績承認の進捗状況を取得する
	 * @param targetEmployeeIds 対象社員
	 * @param period 期間
	 * @return
	 */
	List<DailyApprovalProgress> getApprovalProgress(
			List<String> targetEmployeeIds, DatePeriod period);
}
