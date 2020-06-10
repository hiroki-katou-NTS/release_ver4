package nts.uk.ctx.at.record.dom.workrecord.approval.daily;

import static nts.uk.ctx.at.record.dom.workrecord.lock.DailyRecordLockReason.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.enums.ConfirmationOfManagerOrYouself;
import nts.uk.ctx.at.record.dom.workrecord.approval.common.RecordApprovalSubjective;
import nts.uk.ctx.at.record.dom.workrecord.approval.monthly.ApprovalSubjectiveMonthlyOnWorkflow;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.ErrorAlarmWorkRecordCode;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.lock.DailyRecordLockReason;
import nts.uk.shr.com.time.closure.ClosureMonth;

public class GetApprovalSubjectiveDaily {

	public static Optional<ApprovalSubjectiveDaily> get(
			Require require,
			String approverEmployeeId,
			String targetEmployeeId,
			GeneralDate date,
			List<DailyRecordLockReason> lockReasonsOfRecord) {
		
		// 日別実績の上司承認を利用しない
		val useSet = require.getApprovalProcessingUseSetting();
		if (!useSet.getUseDayApproverConfirm()) {
			return Optional.empty();
		}
		
		ApprovalSubjectiveDailyOnWorkflow statusOnWorkflow;
		{
			val statusOnWorkflowOpt = require.getApprovalSubjectiveDailyOnWorkflow(
					approverEmployeeId, targetEmployeeId, date);
			if (!statusOnWorkflowOpt.isPresent()) {
				return Optional.empty();
			}
			
			statusOnWorkflow = statusOnWorkflowOpt.get();
		}
		
		// 実績ロック
		{
			boolean isLocked = Stream.of(LOCK_DAILY_RECORD, FIX_WORKSPACE, PAST_RECORD)
					.anyMatch(r -> lockReasonsOfRecord.contains(r));
			
			if (isLocked) {
				return Optional.of(new ApprovalSubjectiveDaily(
						approverEmployeeId, targetEmployeeId, date, statusOnWorkflow.getStatus()));
			}
		}
		
		RecordApprovalSubjective.Status status = status(
				require, approverEmployeeId, targetEmployeeId, date, useSet, statusOnWorkflow);
		
		return Optional.of(new ApprovalSubjectiveDaily(approverEmployeeId, targetEmployeeId, date, status));
	}

	private static RecordApprovalSubjective.Status status(
			Require require,
			String approverEmployeeId,
			String targetEmployeeId,
			GeneralDate date,
			ApprovalProcessingUseSetting useSet,
			ApprovalSubjectiveDailyOnWorkflow statusOnWorkflow) {
		
			boolean isApproved = statusOnWorkflow.getStatus().isApproved();
			
			boolean canExecute = statusOnWorkflow.getStatus().canExecute()
					&& canExecute(require, targetEmployeeId, date, useSet);
			
			boolean canRelease = statusOnWorkflow.getStatus().canRelease()
					&& canRelease(require, approverEmployeeId, targetEmployeeId, date, useSet);
			
			return new RecordApprovalSubjective.Status(isApproved, canExecute, canRelease);
	}
	
	private static boolean canExecute(
			Require require, String employeeId, GeneralDate date, ApprovalProcessingUseSetting useSet) {
		
		// 本人確認
		if (require.getIdentityProcessUseSet().isUseConfirmByYourself()) {
			boolean isConfirmed = require.getIdentification(employeeId, date)
					.isPresent();
			
			if (isConfirmed) {
				return false;
			}
		}
		
		// 実績エラー
		{
			boolean cannotApproveWhenError = useSet.getSupervisorConfirmErrorAtr()
					 == ConfirmationOfManagerOrYouself.CAN_NOT_CHECK;
			
			if (cannotApproveWhenError) {
				val isErrorRecord = require.getEmployeeDailyPerError(employeeId, date)
						.flatMap(r -> require.getErrorAlarmWorkRecord(r.getErrorAlarmWorkRecordCode()))
						.map(recErAl -> recErAl.getTypeAtr().isError())
						.orElse(false);
				
				if (isErrorRecord) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private static boolean canRelease(
			Require require, String approverEmployeeId, String targetEmployeeId, GeneralDate date, ApprovalProcessingUseSetting useSet) {
		
		// 月の上司承認
		if (useSet.getUseMonthApproverConfirm()) {
			
			val closureMonth = require.getClosureMonth(targetEmployeeId, date);
			
			return require.getApprovalSubjectiveMonthlyOnWorkflow(approverEmployeeId, targetEmployeeId, closureMonth)
					.map(s -> s.getStatus().canRelease())
					.orElse(true);
		}
		
		return true;
	}
	
	public static interface Require {

		/** 承認処理の利用設定 */
		ApprovalProcessingUseSetting getApprovalProcessingUseSetting();
		
		/** 本人確認処理の利用設定 */
		IdentityProcessUseSet getIdentityProcessUseSet();
		
		/** 締め年月キー */
		ClosureMonth getClosureMonth(String employeeId, GeneralDate date);
		
		/** ワークフロー上における日別実績な承認状況 */
		Optional<ApprovalSubjectiveDailyOnWorkflow> getApprovalSubjectiveDailyOnWorkflow(
				String approverEmployeeId, String targetEmployeeId, GeneralDate date);
		
		/** ワークフロー上における月別実勢駅な承認状況 */
		Optional<ApprovalSubjectiveMonthlyOnWorkflow> getApprovalSubjectiveMonthlyOnWorkflow(
				String approverEmployeeId, String targetEmployeeId, ClosureMonth closureMonth);
		
		/** 日の本人確認 */
		Optional<Identification> getIdentification(String employeeId, GeneralDate date);
		
		/** 社員の日別実績エラー一覧 */
		Optional<EmployeeDailyPerError> getEmployeeDailyPerError(String employeeId, GeneralDate date);
		
		/** 日別実績のエラーアラーム */
		Optional<ErrorAlarmWorkRecord> getErrorAlarmWorkRecord(ErrorAlarmWorkRecordCode code);
	}
}
