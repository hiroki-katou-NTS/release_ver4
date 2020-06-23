package nts.uk.ctx.workflow.dom.resultrecord.status.daily;

import java.util.Optional;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;

/**
 * 指定社員の実績承認状況を取得する
 */
public class GetRouteConfirmStatusDailyTarget {
	
	public static Optional<RouteConfirmStatusDaily> get(Require require, String targetEmployeeId, GeneralDate date) {
		
		val instance = require.getAppRootInstancesDaily(targetEmployeeId, date);
		val confirm = require.getAppRootConfirmsDaily(targetEmployeeId, date);
		
		// confirmも実績と同時に作られるはず
		if (!instance.isPresent() || !confirm.isPresent()) {
			return Optional.empty();
		}
		
		return RouteConfirmStatusDaily.create(confirm, instance);
	}

	public static interface Require {

		Optional<AppRootInstance> getAppRootInstancesDaily(String targetEmployeeId, GeneralDate date);
		
		Optional<AppRootConfirm> getAppRootConfirmsDaily(String targetEmployeeId, GeneralDate date);
	}
}
