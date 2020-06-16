package nts.uk.ctx.workflow.dom.resultrecord.status.monthly;

import java.util.List;
import java.util.Optional;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.com.time.closure.ClosureMonth;

public class GetRouteConfirmStatusMonthlyApprover {

	public static Optional<RouteConfirmStatusMonthly> get(
			Require require, String approverEmployeeId, String targetEmployeeId, ClosureMonth closureMonth) {
		
		// 主体社員が承認者となっているインスタンス
		val instanceApprover = require.getAppRootInstancesMonthly(approverEmployeeId, targetEmployeeId, closureMonth);

		if (instanceApprover.isPresent()) {
			val confirm = require.getAppRootConfirmsMonthly(targetEmployeeId, closureMonth);
			return Optional.of(RouteConfirmStatusMonthly.create(confirm.get(), instanceApprover.get()));
		}
		
		// システム日付時点で主体社員に代行依頼している承認者達のインスタンス
		return require.getReprentRequesterIds(approverEmployeeId, closureMonth.defaultPeriod().end()).stream()
				.map(requesterId -> require.getAppRootInstancesMonthly(requesterId, targetEmployeeId, closureMonth))
				.map(instance -> {
					val confirm = require.getAppRootConfirmsMonthly(targetEmployeeId, closureMonth);
					return RouteConfirmStatusMonthly.create(confirm.get(), instanceApprover.get());
				})
				.findFirst();
	}
	
	public static interface Require {
		
		/** 月別実績の承認ルートインスタンス */
		Optional<AppRootInstance> getAppRootInstancesMonthly(
				String approverId, String targetEmployeeId, ClosureMonth closureMonth);
		
		/** 月別実績の承認状況 */
		Optional<AppRootConfirm> getAppRootConfirmsMonthly(
				String targetEmployeeId, ClosureMonth closureMonth);
		
		/** requestedEmployeeIdに対して実績の代行承認を依頼している社員のID */
		List<String> getReprentRequesterIds(String requestedEmployeeId, GeneralDate date);
	}
}
