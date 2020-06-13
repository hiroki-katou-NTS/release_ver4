package nts.uk.ctx.workflow.dom.resultrecord.status.daily;

import java.util.List;
import java.util.Optional;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;

public class GetRouteConfirmStatusDailyApprover {

	public static Optional<RouteConfirmStatusDaily> get(
			Require require, String approverEmployeeId, String targetEmployeeId, GeneralDate date) {
		
		// approver自身が承認者となっているインスタンス
		val instanceApprover = require.getAppRootInstancesDaily(approverEmployeeId, targetEmployeeId, date);
		
		if (instanceApprover.isPresent()) {
			val confirm = require.getAppRootConfirmsDaily(targetEmployeeId, date);
			return Optional.of(RouteConfirmStatusDaily.create(confirm.get(), instanceApprover.get()));
		}
		
		// システム日付時点でapproverに代行依頼している承認者達のインスタンス
		return require.getRepresentRequesterIds(approverEmployeeId, GeneralDate.today()).stream()
				.map(requesterId -> require.getAppRootInstancesDaily(requesterId, targetEmployeeId, date))
				.map(instance -> {
					val confirm = require.getAppRootConfirmsDaily(targetEmployeeId, date);
					return RouteConfirmStatusDaily.create(confirm.get(), instance.get());
				})
				.findFirst();
	}
	
	public static interface Require {

		/** 日別実績の承認ルートインスタンス */
		Optional<AppRootInstance> getAppRootInstancesDaily(
				String approverEmployeeId, String targetEmployeeId, GeneralDate date);

		/** 日別実績の承認状況 */
		Optional<AppRootConfirm> getAppRootConfirmsDaily(String targetEmployeeId, GeneralDate date);
		
		/** requestedEmployeeIdに対して実績の代行承認を依頼している社員のID */
		List<String> getRepresentRequesterIds(String requestedEmployeeId, GeneralDate date);
	}
}
