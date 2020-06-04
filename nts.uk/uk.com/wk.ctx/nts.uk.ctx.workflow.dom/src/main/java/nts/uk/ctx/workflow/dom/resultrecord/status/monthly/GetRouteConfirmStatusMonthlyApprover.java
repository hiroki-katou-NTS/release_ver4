package nts.uk.ctx.workflow.dom.resultrecord.status.monthly;

import static java.util.stream.Collectors.*;

import java.util.List;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.util.OptionalUtil;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.status.internal.RouteInstancesMap;
import nts.uk.shr.com.time.closure.ClosureMonth;

public class GetRouteConfirmStatusMonthly {

	public static List<RouteConfirmStatusMonthly> get(
			Require require,
			String subjectEmployeeId,
			List<String> targetEmployeeIds,
			ClosureMonth closureMonth) {
		
		val routeInstances = createRouteInstancesMap(require, subjectEmployeeId, targetEmployeeIds, closureMonth);
		val confirms = require.getAppRootConfirmsMonthly(routeInstances.getActualTargetEmployeeIds(), closureMonth);
		
		return confirms.stream()
				.map(confirm -> {
					// 締め期間の終了日を基準日とする
					GeneralDate baseDate = confirm.getClosureMonth().defaultPeriod().end();
					return routeInstances.getInstance(confirm.getEmployeeID(), baseDate)
							.map(instance -> RouteConfirmStatusMonthly.create(confirm, instance));
				})
				.flatMap(OptionalUtil::stream)
				.collect(toList());
	}
	
	private static RouteInstancesMap createRouteInstancesMap(
			Require require,
			String subjectEmployeeId,
			List<String> targetEmployeeIds,
			ClosureMonth closureMonth) {
		
		// 主体社員が承認者となっているインスタンス
		val instancesSubject = require.getAppRootInstancesMonthly(subjectEmployeeId, targetEmployeeIds, closureMonth);
		
		// システム日付時点で主体社員に代行依頼している承認者達のインスタンス
		val instancesRepresent = require.getReprentRequesterIds(subjectEmployeeId, GeneralDate.today()).stream()
				.flatMap(requesterId -> require.getAppRootInstancesMonthly(requesterId, targetEmployeeIds, closureMonth).stream())
				.collect(toList());
		
		return new RouteInstancesMap(instancesSubject, instancesRepresent);
	}
	
	public static interface Require {
		
		/** 月別実績の承認ルートインスタンス */
		List<AppRootInstance> getAppRootInstancesMonthly(
				String approverId, List<String> targetEmployeeIds, ClosureMonth closureMonth);
		
		/** 月別実績の承認状況 */
		List<AppRootConfirm> getAppRootConfirmsMonthly(
				List<String> targetEmployeeIds, ClosureMonth closureMonth);
		
		/** targetEmployeeIdに対して実績の代行承認を依頼している社員のID */
		List<String> getReprentRequesterIds(String targetEmployeeId, GeneralDate date);
	}
}
