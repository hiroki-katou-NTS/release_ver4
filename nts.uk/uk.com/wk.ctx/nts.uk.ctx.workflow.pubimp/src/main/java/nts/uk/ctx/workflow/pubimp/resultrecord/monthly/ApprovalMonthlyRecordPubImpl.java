package nts.uk.ctx.workflow.pubimp.resultrecord.monthly;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.cache.NestedMapCache;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.agent.AgentRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstanceRepository;
import nts.uk.ctx.workflow.dom.resultrecord.status.internal.RouteConfirmProgress;
import nts.uk.ctx.workflow.dom.resultrecord.status.monthly.GetRouteConfirmStatusMonthlyApprover;
import nts.uk.ctx.workflow.dom.resultrecord.status.monthly.GetRouteConfirmStatusMonthlyTarget;
import nts.uk.ctx.workflow.dom.resultrecord.status.monthly.RouteConfirmStatusMonthly;
import nts.uk.ctx.workflow.pub.resultrecord.common.ApprovalProgress;
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

	@Inject
	private AppRootConfirmRepository appRootConfirmRepository;

	@Inject
	private AppRootInstanceRepository appRootInstanceRepository; 

	@Inject
	private AgentRepository agentRepository;
	
	@Override
	public List<MonthlySubjectiveStatus> getSubjectiveStatus(
			String approverEmployeeId, List<String> targetEmployeeIds, ClosureMonth closureMonth, DatePeriod period) {
		
		val require = new RequireGetSubjectiveStatus(targetEmployeeIds, closureMonth, period);
		
		List<MonthlySubjectiveStatus> results = new ArrayList<>();
		for (String targetEmployeeId : targetEmployeeIds) {
			results.addAll(
				GetRouteConfirmStatusMonthlyApprover.get(require, approverEmployeeId, targetEmployeeId, closureMonth, period).stream()
					.map(status -> toExport(approverEmployeeId, require, closureMonth, status))
					.collect(Collectors.toList())
			);
		}
		
		return results;
	}
	
	private static MonthlySubjectiveStatus toExport(
			String approverEmployeeId,
			ApprovalMonthlyRecordPubImpl.RequireGetSubjectiveStatus require,
			ClosureMonth closureMonth,
			RouteConfirmStatusMonthly status) {
		
		return new MonthlySubjectiveStatus(
			status.getTargetEmployeeId(),
			closureMonth,
			ConvertRecordApproval.progress(status.getProgress()),
			ConvertRecordApproval.statusMonthly(
					status,
					approverEmployeeId,
					require.getReprentRequesterIds(approverEmployeeId, closureMonth.defaultPeriod().end())));
	}
	
	class RequireGetSubjectiveStatus implements GetRouteConfirmStatusMonthlyApprover.Require {

		private final String companyId = AppContexts.user().companyId();

		private NestedMapCache<String, DatePeriod, List<AppRootInstance>> cacheInstance;
		private NestedMapCache<String, ClosureMonth, AppRootConfirm> cacheConfirm;
		
		public RequireGetSubjectiveStatus(List<String> targetEmployeeIds, ClosureMonth closureMonth, DatePeriod period) {
			//cacheInstance読み込み
			// 対象者の月別実績の承認ルートを取得する（期間）
			List<AppRootInstance> approuteInstancelist = appRootInstanceRepository.findAppRootInstanceMonthlyByTarget(targetEmployeeIds, period);

			Map<String, Map<DatePeriod, List<AppRootInstance>>> dataInstance = new HashMap<String, Map<DatePeriod, List<AppRootInstance>>>();

			for (String targetEmployeeId : targetEmployeeIds) {
				List<AppRootInstance> approuteInstance = approuteInstancelist.stream()
						.filter(instance -> instance.getEmployeeID().equals(targetEmployeeId))
						.collect(Collectors.toList());
				if (!dataInstance.containsKey(targetEmployeeId)) {
					Map<DatePeriod, List<AppRootInstance>> data = new HashMap<DatePeriod, List<AppRootInstance>>();
					data.put (period, approuteInstance);
					dataInstance.put (targetEmployeeId, data); 
				}
				else {
					dataInstance.get(targetEmployeeId).put(period, approuteInstance);
				}
			}
			
			cacheInstance = NestedMapCache.preloadedAll(dataInstance);
			
			//cacheConfirm読み込み
			// 月別実績の承認状況を取得する（複数社員・単一集計期間）
			List<AppRootConfirm> approuteConfirmlist = appRootConfirmRepository.findAppRootConfirmMonthly(
					targetEmployeeIds, closureMonth).stream().collect(Collectors.toList());

			Map<String, Map<ClosureMonth, AppRootConfirm>> dataConfirm = new HashMap<String, Map<ClosureMonth, AppRootConfirm>>();
			for (AppRootConfirm approuteConfirm : approuteConfirmlist) {
				if (!dataConfirm.containsKey(approuteConfirm.getEmployeeID())) {
					Map<ClosureMonth, AppRootConfirm> data = new HashMap<ClosureMonth, AppRootConfirm>();
					data.put (closureMonth, approuteConfirm);
					dataConfirm.put (approuteConfirm.getEmployeeID(), data); 
				}
				else {
					dataConfirm.get(approuteConfirm.getEmployeeID()).put(closureMonth, approuteConfirm);
				}
			}
			
			cacheConfirm = NestedMapCache.preloadedAll(dataConfirm);
		}

		@Override
		public List<AppRootInstance> getAppRootInstancesMonthly(
				String approverId, String targetEmployeeId, ClosureMonth closureMonth, DatePeriod period) {
			val cached = cacheInstance.get(targetEmployeeId, period);
			if (cached.isPresent()) return cached.get();
			
			// 承認者の月別実績の承認ルートを取得する（対象者指定）（期間）
			return appRootInstanceRepository.findAppRootInstanceMonthlyByApproverTarget(
					approverId, Arrays.asList(targetEmployeeId), period);
		}

		@Override
		public Optional<AppRootConfirm> getAppRootConfirmsMonthly(String targetEmployeeId, ClosureMonth closureMonth) {
			val cached = cacheConfirm.get(targetEmployeeId, closureMonth);
			if (cached.isPresent()) return cached;

			// 月別実績の承認状況を取得する（単一社員・単一集計期間）
			return appRootConfirmRepository.findAppRootConfirmMonthly(targetEmployeeId, closureMonth);
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
	public List<MonthlyApprovalProgress> getApprovalProgress(List<String> targetEmployeeIds, ClosureMonth closureMonth, DatePeriod period) {

		val require = new RequireGetTargetStatus(targetEmployeeIds, closureMonth, period);
		
		List<MonthlyApprovalProgress> results = new ArrayList<>();
		
		for (String targetEmployeeId : targetEmployeeIds) {
			results.addAll(
				GetRouteConfirmStatusMonthlyTarget.get(require, targetEmployeeId, closureMonth).stream()
					.map(r -> toExport(r))
					.collect(Collectors.toList())
			);
		}
		
		return results;
	}
	
	private MonthlyApprovalProgress toExport(RouteConfirmStatusMonthly from) {
		return new MonthlyApprovalProgress(
				from.getTargetEmployeeId(),
				from.getTargetDate(),
				MAP_PROGRESS.get(from.getProgress()));
	}

	private static final Map<RouteConfirmProgress, ApprovalProgress.Progress> MAP_PROGRESS;
	static {
		MAP_PROGRESS = new HashMap<>();
		MAP_PROGRESS.put(RouteConfirmProgress.UNAPPROVED, ApprovalProgress.Progress.UNAPPROVED);
		MAP_PROGRESS.put(RouteConfirmProgress.APPROVING, ApprovalProgress.Progress.APPROVING);
		MAP_PROGRESS.put(RouteConfirmProgress.APPROVED, ApprovalProgress.Progress.APPROVED);
	}
	
	public class RequireGetTargetStatus implements GetRouteConfirmStatusMonthlyTarget.Require {

		private NestedMapCache<String, ClosureMonth, List<AppRootInstance>> cacheInstance;
		private NestedMapCache<String, ClosureMonth, AppRootConfirm> cacheConfirm;
		
		public RequireGetTargetStatus(List<String> targetEmployeeIds, ClosureMonth closureMonth, DatePeriod period) {
			//cacheInstance読み込み
			// 対象者の月別実績の承認ルートを取得する（期間）
			List<AppRootInstance> approuteInstancelist = appRootInstanceRepository.findAppRootInstanceMonthlyByTarget(targetEmployeeIds, period);

			Map<String, Map<ClosureMonth, List<AppRootInstance>>> dataInstance = new HashMap<String, Map<ClosureMonth, List<AppRootInstance>>>();
			for (String targetEmployeeId : targetEmployeeIds) {
				List<AppRootInstance> approuteInstance = approuteInstancelist.stream()
						.filter(instance -> instance.getEmployeeID().equals(targetEmployeeId))
						.collect(Collectors.toList());
				if (!dataInstance.containsKey(targetEmployeeId)) {
					Map<ClosureMonth, List<AppRootInstance>> data = new HashMap<ClosureMonth, List<AppRootInstance>>();
					data.put (closureMonth, approuteInstance);
					dataInstance.put (targetEmployeeId, data); 
				}
				else {
					dataInstance.get(targetEmployeeId).put(closureMonth, approuteInstance);
				}
			}
			
			cacheInstance = NestedMapCache.preloadedAll(dataInstance);
			
			//cacheConfirm読み込み
			// 月別実績の承認状況を取得する（複数社員・単一集計期間）
			List<AppRootConfirm> approuteConfirmlist = appRootConfirmRepository.findAppRootConfirmMonthly(
					targetEmployeeIds, closureMonth).stream().collect(Collectors.toList());

			Map<String, Map<ClosureMonth, AppRootConfirm>> dataConfirm = new HashMap<String, Map<ClosureMonth, AppRootConfirm>>();
			for (AppRootConfirm approuteConfirm : approuteConfirmlist) {
				if (!dataConfirm.containsKey(approuteConfirm.getEmployeeID())) {
					Map<ClosureMonth, AppRootConfirm> data = new HashMap<ClosureMonth, AppRootConfirm>(); 
					data.put (closureMonth, approuteConfirm);
					dataConfirm.put (approuteConfirm.getEmployeeID(), data); 
				}
				else {
					dataConfirm.get(approuteConfirm.getEmployeeID()).put(closureMonth, approuteConfirm);
				}
			}
			
			cacheConfirm = NestedMapCache.preloadedAll(dataConfirm);

		}

		@Override
		public List<AppRootInstance> getAppRootInstancesMonthly(String targetEmployeeId, ClosureMonth closureMonth) {
			val cached = cacheInstance.get(targetEmployeeId, closureMonth);
			if (cached.isPresent()) return cached.get();
			
			// 対象者の月別実績の承認ルートを取得する（期間）
			return appRootInstanceRepository.findAppRootInstanceMonthlyByTarget(Arrays.asList(targetEmployeeId), closureMonth.defaultPeriod());
		}

		@Override
		public Optional<AppRootConfirm> getAppRootConfirmsMonthly(String targetEmployeeId, ClosureMonth closureMonth) {
			val cached = cacheConfirm.get(targetEmployeeId, closureMonth);
			if (cached.isPresent()) return cached;

			// 月別実績の承認状況を取得する（単一社員・単一集計期間） 
			return appRootConfirmRepository.findAppRootConfirmMonthly(targetEmployeeId, closureMonth);
		}
	}
}
