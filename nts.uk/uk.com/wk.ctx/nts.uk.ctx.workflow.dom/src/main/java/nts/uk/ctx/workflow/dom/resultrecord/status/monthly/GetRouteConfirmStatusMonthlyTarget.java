package nts.uk.ctx.workflow.dom.resultrecord.status.monthly;

import java.util.Optional;

import lombok.val;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.com.time.closure.ClosureMonth;

public class GetRouteConfirmStatusMonthlyTarget {

	public static Optional<RouteConfirmStatusMonthly> get(
			Require require, String targetEmployeeId, ClosureMonth closureMonth) {

		val instance = require.getAppRootInstancesMonthly(targetEmployeeId, closureMonth);
		val confirm = require.getAppRootConfirmsMonthly(targetEmployeeId, closureMonth);

		if (!instance.isPresent() || !confirm.isPresent()) {
			return Optional.empty();
		}

		return Optional.of(RouteConfirmStatusMonthly.create(confirm.get(), instance.get()));
	}
		
	public static interface Require {
		
		/** 月別実績の承認ルートインスタンス */
		Optional<AppRootInstance> getAppRootInstancesMonthly(String targetEmployeeId, ClosureMonth closureMonth);
		
		/** 月別実績の承認状況 */
		Optional<AppRootConfirm> getAppRootConfirmsMonthly(String targetEmployeeId, ClosureMonth closureMonth);
	}
}

