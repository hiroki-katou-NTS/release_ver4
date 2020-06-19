package nts.uk.ctx.workflow.pubimp.resultrecord.daily;


import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.cache.DateHistoryCache;
import nts.arc.layer.app.cache.KeyDateHistoryCache;
import nts.arc.layer.app.cache.NestedMapCache;
import nts.arc.time.GeneralDate;
import nts.gul.collection.ListHashMap;
import nts.uk.ctx.workflow.dom.agent.AgentRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirmRepository;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstanceRepository;
import nts.uk.ctx.workflow.dom.resultrecord.status.daily.GetRouteConfirmStatusDailyApprover;
import nts.uk.ctx.workflow.dom.resultrecord.status.daily.GetRouteConfirmStatusDailyTarget;
import nts.uk.ctx.workflow.dom.resultrecord.status.daily.RouteConfirmStatusDaily;
import nts.uk.ctx.workflow.dom.resultrecord.status.internal.RouteConfirmProgress;
import nts.uk.ctx.workflow.pub.resultrecord.common.ApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.daily.DailyApprovalProgress;
import nts.uk.ctx.workflow.pub.resultrecord.daily.DailyRecordApprovalPub;
import nts.uk.ctx.workflow.pub.resultrecord.daily.DailySubjectiveStatus;
import nts.uk.ctx.workflow.pubimp.resultrecord.internal.ConvertRecordApproval;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DailyRecordApprovalPubImpl implements DailyRecordApprovalPub {

	@Inject
	private AppRootConfirmRepository appRootConfirmRepository;

	@Inject
	private AppRootInstanceRepository appRootInstanceRepository; 

	@Inject
	private AgentRepository agentRepository;

	
	@Override
	public List<DailySubjectiveStatus> getSubjectiveStatus(
			String approverEmployeeId, List<String> targetEmployeeIds, DatePeriod period) {
		
		val require = new RequireGetSubjectiveStatus(approverEmployeeId, targetEmployeeIds, period);
		
		List<DailySubjectiveStatus> results = new ArrayList<>();
		for (String targetEmployeeId : targetEmployeeIds) {
			for (GeneralDate date : period.datesBetween()) {
				GetRouteConfirmStatusDailyApprover.get(require, approverEmployeeId, targetEmployeeId, date)
					.map(status -> toExport(approverEmployeeId, require, date, status))
					.ifPresent(r -> results.add(r));
			}
		}
		
		return results;
	}

	private static DailySubjectiveStatus toExport(
			String approverEmployeeId,
			DailyRecordApprovalPubImpl.RequireGetSubjectiveStatus require,
			GeneralDate date,
			RouteConfirmStatusDaily status) {
		
		return new DailySubjectiveStatus(
			status.getTargetEmployeeId(),
			status.getTargetDate(),
			ConvertRecordApproval.progress(status.getProgress()),
			ConvertRecordApproval.statusDaily(
					status,
					approverEmployeeId,
					require.getRepresentRequesterIds(approverEmployeeId, date)));
	}

	@Override
	public List<DailySubjectiveStatus> getSubjectiveStatus(
			String approverEmployeeId, List<String> targetEmployeeIds, GeneralDate date) {
		return getSubjectiveStatus(approverEmployeeId, targetEmployeeIds, new DatePeriod(date, date));
	}


	class RequireGetSubjectiveStatus implements GetRouteConfirmStatusDailyApprover.Require {

		private final String companyId = AppContexts.user().companyId();
		
		private KeyDateHistoryCache<String, AppRootInstance> cacheInstance;
		private NestedMapCache<String, GeneralDate, AppRootConfirm> cacheConfirm;
		
		public RequireGetSubjectiveStatus(
				String approverEmployeeId, List<String> targetEmployeeIds, GeneralDate date) {
			
			DatePeriod period = new DatePeriod(date, date);
			loadCache(approverEmployeeId, targetEmployeeIds, period);
			
		}
		
		public RequireGetSubjectiveStatus(
				String approverEmployeeId, List<String> employetargetEmployeeIdseId, DatePeriod period) {
			loadCache(approverEmployeeId, employetargetEmployeeIdseId, period);
		}
		
		private void loadCache(String approverEmployeeId, List<String> targetEmployeeIds, DatePeriod period) {
			//cacheInstance読み込み
			// 承認者の日別実績の承認ルートを取得する（対象者指定）（期間）
			List<AppRootInstance> approuteInstancelist = appRootInstanceRepository.findAppRootInstanceDailyByApproverTarget(
					approverEmployeeId, targetEmployeeIds, period).stream()
						.collect(Collectors.toList());

			Map<String, List<DateHistoryCache.Entry<AppRootInstance>>> dataInstance = new HashMap<>();
			for (AppRootInstance approuteInstance : approuteInstancelist) {
				if (!dataInstance.containsKey(approuteInstance.getEmployeeID())) {
					dataInstance.put(approuteInstance.getEmployeeID(), Arrays.asList(DateHistoryCache.Entry.of(period, approuteInstance)));
				}
				else {
					dataInstance.get(approuteInstance.getEmployeeID()).add(DateHistoryCache.Entry.of(period, approuteInstance));
				}
			}
			
			cacheInstance = KeyDateHistoryCache.loaded(dataInstance);
			
			//cacheConfirm読み込み
			// 日別実績の承認状況を取得する（複数社員・期間）
			List<AppRootConfirm> approuteConfirmlist = appRootConfirmRepository.findAppRootConfirmDaily(
					targetEmployeeIds, period).stream().collect(Collectors.toList());

			Map<String, Map<GeneralDate, AppRootConfirm>> dataConfirm = new HashMap<String, Map<GeneralDate, AppRootConfirm>>();
			for (AppRootConfirm approuteConfirm : approuteConfirmlist) {
				if (!dataConfirm.containsKey(approuteConfirm.getEmployeeID())) {
					Map<GeneralDate, AppRootConfirm> data = new TreeMap<GeneralDate, AppRootConfirm>() {{ 
						put (approuteConfirm.getRecordDate(), approuteConfirm); }};
					dataConfirm.put (approuteConfirm.getEmployeeID(), data); 
				}
				else {
					dataConfirm.get(approuteConfirm.getEmployeeID()).put(approuteConfirm.getRecordDate(), approuteConfirm);
				}
			}
			
			cacheConfirm = NestedMapCache.preloadedAll(dataConfirm);
		}

		@Override
		public Optional<AppRootInstance> getAppRootInstancesDaily(
				String approverID, String targetEmployeeId, GeneralDate date) {
			
			val cached = cacheInstance.get(targetEmployeeId, date);
			if (cached.isPresent()) return cached;
			
			// 承認者の日別実績の承認ルートを取得する（対象者指定）（単一日）
			return appRootInstanceRepository.findAppRootInstanceDailyByApproverTarget(
					approverID, Arrays.asList(targetEmployeeId), date).stream().findFirst();
		}

		@Override
		public Optional<AppRootConfirm> getAppRootConfirmsDaily(String targetEmployeeId, GeneralDate date) {
			// TODO Auto-generated method stub

			val cached = cacheConfirm.get(targetEmployeeId, date);
			if (cached.isPresent()) return cached;

			// 日別実績の承認状況を取得する（単一社員・単一日）
			return appRootConfirmRepository.findAppRootConfirmDaily(targetEmployeeId, date);
		}

		private List<String> cacheReprentRequesterIdsDaily = null;
		
		@Override
		public List<String> getRepresentRequesterIds(String requestedEmployeeId, GeneralDate date) {
			
			if (cacheReprentRequesterIdsDaily != null) {
				return cacheReprentRequesterIdsDaily;
			}
			
			return cacheReprentRequesterIdsDaily = agentRepository.findAgentByPeriod(
					companyId, Arrays.asList(requestedEmployeeId), date, date, 1).stream()
					.map(a -> a.getAgentID())
					.collect(toList());
		}
	}

	
	
	
	@Override
	public List<DailyApprovalProgress> getApprovalProgress(List<String> targetEmployeeIds, GeneralDate date) {
		return getApprovalProgress(targetEmployeeIds, new DatePeriod(date, date));
	}

	@Override
	public List<DailyApprovalProgress> getApprovalProgress(List<String> targetEmployeeIds, DatePeriod period) {
		
		val require = new RequireGetApprovalProgressTarget(targetEmployeeIds, period);
		
		List<DailyApprovalProgress> results = new ArrayList<>();
		
		for (String targetEmployeeId : targetEmployeeIds) {
			for (GeneralDate date : period.datesBetween()) {
				GetRouteConfirmStatusDailyTarget.get(require, targetEmployeeId, date)
					.map(r -> toExport(r))
					.ifPresent(r -> results.add(r));
			}
		}
		
		return results;
	}
	
	private DailyApprovalProgress toExport(RouteConfirmStatusDaily from) {
		return new DailyApprovalProgress(
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
	
	class RequireGetApprovalProgressTarget implements GetRouteConfirmStatusDailyTarget.Require {

		private KeyDateHistoryCache<String, AppRootInstance> cacheInstance;
		private NestedMapCache<String, GeneralDate, AppRootConfirm> cacheConfirm;
		
		public RequireGetApprovalProgressTarget(
				List<String> targetEmployeeIds, DatePeriod period) {

			loadCache(targetEmployeeIds, period);
		}

		private void loadCache(List<String> targetEmployeeIds, DatePeriod period) {
			//cacheInstance読み込み
			// 対象者の日別実績の承認ルートを取得する（期間）
			List<AppRootInstance> approuteInstancelist = appRootInstanceRepository.findAppRootInstanceDailyByTarget(
					targetEmployeeIds, period).stream().collect(Collectors.toList());

			//Map<String, List<DateHistoryCache.Entry<AppRootInstance>>> dataInstance = new HashMap<>();
			ListHashMap<String, DateHistoryCache.Entry<AppRootInstance>> dataInstance = new ListHashMap<>();
			
			for (AppRootInstance approuteInstance : approuteInstancelist) {
				dataInstance.addElement(
						approuteInstance.getEmployeeID(),
						DateHistoryCache.Entry.of(period, approuteInstance));
			}
			
			cacheInstance = KeyDateHistoryCache.loaded(dataInstance);
			
			//cacheConfirm読み込み
			// 日別実績の承認状況を取得する（複数社員・期間）
			List<AppRootConfirm> approuteConfirmlist = appRootConfirmRepository.findAppRootConfirmDaily(
					targetEmployeeIds, period).stream().collect(Collectors.toList());

			Map<String, Map<GeneralDate, AppRootConfirm>> dataConfirm = new HashMap<String, Map<GeneralDate, AppRootConfirm>>();
			for (AppRootConfirm approuteConfirm : approuteConfirmlist) {
				if (!dataConfirm.containsKey(approuteConfirm.getEmployeeID())) {
					Map<GeneralDate, AppRootConfirm> data = new TreeMap<GeneralDate, AppRootConfirm>() {{ 
						put (approuteConfirm.getRecordDate(), approuteConfirm); }};
					dataConfirm.put (approuteConfirm.getEmployeeID(), data); 
				}
				else {
					dataConfirm.get(approuteConfirm.getEmployeeID()).put(approuteConfirm.getRecordDate(), approuteConfirm);
				}
			}

			cacheConfirm = NestedMapCache.preloadedAll(dataConfirm);
		}
		
		@Override
		public Optional<AppRootInstance> getAppRootInstancesDaily(String targetEmployeeId, GeneralDate date) {
			val cached = cacheInstance.get(targetEmployeeId, date);
			if (cached.isPresent()) return cached;
			
			// 対象者の日別実績の承認ルートを取得する（単一日）
			return appRootInstanceRepository.findAppRootInstanceDailyByTarget(
					Arrays.asList(targetEmployeeId), date).stream().findFirst();
			
		}

		@Override
		public Optional<AppRootConfirm> getAppRootConfirmsDaily(String targetEmployeeId, GeneralDate date) {
			
			val cached = cacheConfirm.get(targetEmployeeId, date);
			if (cached.isPresent()) return cached;

			// 日別実績の承認状況を取得する（単一社員・単一日）
			return appRootConfirmRepository.findAppRootConfirmDaily(targetEmployeeId, date);
		}
		
	}

}
