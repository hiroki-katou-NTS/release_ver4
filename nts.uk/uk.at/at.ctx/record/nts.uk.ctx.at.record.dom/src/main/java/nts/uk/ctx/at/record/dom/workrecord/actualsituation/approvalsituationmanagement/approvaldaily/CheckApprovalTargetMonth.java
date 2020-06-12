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

}
