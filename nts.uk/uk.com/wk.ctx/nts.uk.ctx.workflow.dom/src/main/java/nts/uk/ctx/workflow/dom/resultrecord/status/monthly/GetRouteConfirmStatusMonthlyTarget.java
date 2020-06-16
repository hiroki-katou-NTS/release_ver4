package nts.uk.ctx.workflow.dom.resultrecord.status.monthly;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.val;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.com.time.closure.ClosureMonth;

public class GetRouteConfirmStatusMonthlyTarget {

	public static List<RouteConfirmStatusMonthly> get(
			Require require, String targetEmployeeId, ClosureMonth closureMonth) {

		val instanceApprover = require.getAppRootInstancesMonthly(targetEmployeeId, closureMonth);
		val confirm = require.getAppRootConfirmsMonthly(targetEmployeeId, closureMonth);

		if (!instanceApprover.isEmpty() || !confirm.isPresent()) {
			return Collections.emptyList();
		}

		return instanceApprover.stream()
			.map(instance -> RouteConfirmStatusMonthly.create(confirm.get(), instance))
			.collect(Collectors.toList());
	}
		
	public static interface Require {
		
		/** 月別実績の承認ルートインスタンス */
		List<AppRootInstance> getAppRootInstancesMonthly(String targetEmployeeId, ClosureMonth closureMonth);
		
		/** 月別実績の承認状況 */
		Optional<AppRootConfirm> getAppRootConfirmsMonthly(String targetEmployeeId, ClosureMonth closureMonth);
	}
}

