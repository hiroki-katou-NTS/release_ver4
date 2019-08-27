package nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalsituationmanagement.approvaldaily;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.ApprovalStatusAdapter;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.dtos.ApproveRootStatusForEmpImport;
import nts.uk.ctx.at.record.dom.adapter.workflow.service.enums.ApprovalStatusForEmployee;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalsituationmanagement.export.CheckEmployeeUseApprovalMonth;

/**
 * @author thanhnx 対象月の承認が済んでいるかチェックする
 *
 */
@Stateless
public class CheckApprovalTargetMonth {

	@Inject
	private CheckEmployeeUseApprovalMonth checkEmployeeUseApprovalMonth;

	@Inject
	private ApprovalStatusAdapter approvalStatusAdapter;

	public boolean checkApprovalTargetMonth(String employeeId, GeneralDate date) {
		boolean check = checkEmployeeUseApprovalMonth.checkEmployeeUseApprovalTargetMonth(employeeId, date);
		if (!check) {
			return true;
		}
		// TODO 対応するImported「（就業．勤務実績）承認対象者の承認状況」をすべて取得する
		List<ApproveRootStatusForEmpImport> appRoots = approvalStatusAdapter
				.getApprovalByListEmplAndListApprovalRecordDateNew(Arrays.asList(date), Arrays.asList(employeeId), 1);
		if(appRoots.isEmpty()) return false;
		List<GeneralDate> dates = appRoots.stream()
				.filter(x -> x.getApprovalStatus() != ApprovalStatusForEmployee.APPROVED).map(x -> x.getAppDate())
				.collect(Collectors.toList());
		return dates.isEmpty();
	}
}
