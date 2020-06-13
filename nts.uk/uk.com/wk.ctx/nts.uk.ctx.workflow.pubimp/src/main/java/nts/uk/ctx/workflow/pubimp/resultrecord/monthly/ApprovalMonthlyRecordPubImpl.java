package nts.uk.ctx.workflow.pubimp.resultrecord.monthly;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.agent.AgentRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstanceRepository;
import nts.uk.ctx.workflow.dom.resultrecord.RecordRootType;
import nts.uk.ctx.workflow.dom.resultrecord.status.monthly.GetRouteConfirmStatusMonthly;
import nts.uk.ctx.workflow.pub.resultrecord.monthly.MonthlyApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.monthly.MonthlyRecordApprovalPub;
import nts.uk.ctx.workflow.pub.resultrecord.monthly.MonthlySubjectiveStatus;
import nts.uk.ctx.workflow.pubimp.resultrecord.internal.ConvertRecordApproval;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;
import nts.uk.shr.com.time.closure.ClosureMonth;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ApprovalMonthlyRecordPubImpl implements MonthlyRecordApprovalPub {
	
	@Override
	public List<MonthlySubjectiveStatus> getSubjectiveStatus(
			String subjectEmployeeId, List<String> targetEmployeeIds, ClosureMonth closureMonth) {
		
		val require = new RequireImpl();
		
		// 代行依頼者
		val representRequesterIds = require.getReprentRequesterIds(subjectEmployeeId, GeneralDate.today());
		
		return GetRouteConfirmStatusMonthly.get(require, subjectEmployeeId, targetEmployeeIds, closureMonth).stream()
				.map(status -> {
					return new MonthlySubjectiveStatus(
						status.getTargetEmployeeId(),
						status.getTargetDate(),
						ConvertRecordApproval.progress(status.getProgress()),
						ConvertRecordApproval.statusMonthly(status, subjectEmployeeId, representRequesterIds));
				})
				.collect(toList());
	}

	@Inject
	private AppRootConfirmRepository appRootConfirmRepository;

	@Inject
	private AppRootInstanceRepository appRootInstanceRepository; 

	@Inject
	private AgentRepository agentRepository;

	public class RequireImpl implements GetRouteConfirmStatusMonthly.Require {

		private final String companyId = AppContexts.user().companyId();
		
		@Override
		public List<AppRootInstance> getAppRootInstancesMonthly(String approverId, List<String> targetEmployeeIds,
				ClosureMonth closureMonth) {
			
			// 締め期間の終了日を基準日とする
			val baseDate = closureMonth.defaultPeriod().end();
			
			return appRootInstanceRepository.findByApproverEmployeePeriod(
					companyId,
					approverId,
					targetEmployeeIds,
					new DatePeriod(baseDate, baseDate),
					RecordRootType.CONFIRM_WORK_BY_MONTH);
		}

		@Override
		public List<AppRootConfirm> getAppRootConfirmsMonthly(List<String> targetEmployeeIds,
				ClosureMonth closureMonth) {
			
			return appRootConfirmRepository.findByEmpLstMonth(
					companyId,
					targetEmployeeIds,
					closureMonth.yearMonth(),
					closureMonth.closureId(),
					closureMonth.closureDate(),
					RecordRootType.CONFIRM_WORK_BY_MONTH);
		}
		
		private List<String> cacheReprentRequesterIdsDaily = null;

		@Override
		public List<String> getReprentRequesterIds(String targetEmployeeId, GeneralDate date) {
			if (cacheReprentRequesterIdsDaily != null) {
				return cacheReprentRequesterIdsDaily;
			}
			
			return cacheReprentRequesterIdsDaily = agentRepository.findAgentByPeriod(
					companyId, Arrays.asList(targetEmployeeId), date, date, 1).stream()
					.map(a -> a.getAgentID())
					.collect(toList());
		}
	}

	@Override
	public List<MonthlyApprovalProgress> getApprovalProgress(List<String> targetEmployeeIds,
			ClosureMonth closureMonth) {
		// TODO Auto-generated method stub
		return null;
	}
}
