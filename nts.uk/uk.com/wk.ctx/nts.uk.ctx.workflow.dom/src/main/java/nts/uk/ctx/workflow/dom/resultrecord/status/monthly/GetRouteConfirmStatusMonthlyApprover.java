package nts.uk.ctx.workflow.dom.resultrecord.status.monthly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

public class GetRouteConfirmStatusMonthlyApprover {

	public static List<RouteConfirmStatusMonthly> get(
			Require require, String approverEmployeeId, String targetEmployeeId, ClosureMonth closureMonth, DatePeriod period) {
		
		// 主体社員が承認者となっているインスタンス
		val instanceApprover = require.getAppRootInstancesMonthly(approverEmployeeId, targetEmployeeId, closureMonth, period);
		val confirm = require.getAppRootConfirmsMonthly(targetEmployeeId, closureMonth);
		
		if(!confirm.isPresent()) {
			return Collections.emptyList();
		}
		
		if (!instanceApprover.isEmpty()) {
			return instanceApprover.stream()
				.map(instance -> RouteConfirmStatusMonthly.create(confirm, Optional.of(instance)).get())
				.collect(Collectors.toList());
		}
		
		// システム日付時点で主体社員に代行依頼している承認者達のインスタンス
		List<String> requesterIds = require.getReprentRequesterIds(approverEmployeeId, period.end());
		List<RouteConfirmStatusMonthly> results = new ArrayList<>();
		for (String requesterId : requesterIds) {
			List<AppRootInstance> InstanceRequester = require.getAppRootInstancesMonthly(
					requesterId, targetEmployeeId, closureMonth, period);
			
			results.addAll(
				InstanceRequester.stream()
					.map(instance -> {
						return RouteConfirmStatusMonthly.create(confirm, Optional.of(instance)).get();
					})
					.collect(Collectors.toList())
			);
		}
		return results;
	}
	
	public static interface Require {
		
		/** 月別実績の承認ルートインスタンス */
		List<AppRootInstance> getAppRootInstancesMonthly(
				String approverId, String targetEmployeeId, ClosureMonth closureMonth, DatePeriod period);
		
		/** 月別実績の承認状況 */
		Optional<AppRootConfirm> getAppRootConfirmsMonthly(
				String targetEmployeeId, ClosureMonth closureMonth);
		
		/** requestedEmployeeIdに対して実績の代行承認を依頼している社員のID */
		List<String> getReprentRequesterIds(String requestedEmployeeId, GeneralDate date);
	}
}
