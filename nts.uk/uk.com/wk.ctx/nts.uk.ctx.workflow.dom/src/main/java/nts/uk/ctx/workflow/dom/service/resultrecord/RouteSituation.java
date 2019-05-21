package nts.uk.ctx.workflow.dom.service.resultrecord;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.status.RouteConfirmStatus;
/**
 * ルート状況
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RouteSituation {
	
	/**
	 * 対象日
	 */
	private GeneralDate date;
	
	/**
	 * 対象者
	 */
	private String employeeID;
	
	/**
	 * ルートの状況
	 */
	private ApproverEmpState approverEmpState;
	
	/**
	 * 承認状況
	 */
	private Optional<ApprovalStatus> approvalStatus;
	
	public static RouteSituation create(
			AppRootConfirm confirm,
			AppRootInstance instance,
			String approverId,
			List<String> representRequesterIds) {
		
		val status = RouteConfirmStatus.create(confirm, instance)
				.getStatusFor(approverId, representRequesterIds);
		
		return new RouteSituation(
				confirm.getRecordDate(),
				confirm.getEmployeeID(),
				status.getState(),
				Optional.of(status.getApprovalStatus()));
	}
}
