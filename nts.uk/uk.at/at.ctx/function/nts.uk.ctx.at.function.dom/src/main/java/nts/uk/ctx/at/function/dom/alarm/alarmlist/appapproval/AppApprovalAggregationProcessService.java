package nts.uk.ctx.at.function.dom.alarm.alarmlist.appapproval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.WorkPlaceHistImport;
import nts.uk.ctx.at.function.dom.adapter.agent.AgentApprovalAdapter;
import nts.uk.ctx.at.function.dom.adapter.agent.AgentApprovalImport;
import nts.uk.ctx.at.function.dom.adapter.application.ApplicationAdapter;
import nts.uk.ctx.at.function.dom.adapter.application.ApplicationImport;
import nts.uk.ctx.at.function.dom.adapter.application.ApplicationStateImport;
import nts.uk.ctx.at.function.dom.adapter.application.ReflectStateImport;
import nts.uk.ctx.at.function.dom.adapter.approvalroot.ApprovalRootRecordAdapter;
import nts.uk.ctx.at.function.dom.adapter.approvalrootstate.ApprovalBehaviorAtr;
import nts.uk.ctx.at.function.dom.adapter.approvalrootstate.ApprovalRootStateAdapter;
import nts.uk.ctx.at.function.dom.adapter.approvalrootstate.ApprovalRootStateImport;
import nts.uk.ctx.at.function.dom.adapter.approvalrootstate.ApproverStateImport;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.ErAlWorkRecordCheckAdapter;
import nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck.RegulationInfoEmployeeResult;
import nts.uk.ctx.at.function.dom.alarm.alarmdata.ValueExtractAlarm;
import nts.uk.ctx.at.function.dom.alarm.alarmlist.EmployeeSearchDto;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckTargetCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.ExtractionCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.appapproval.AppApprovalAlarmCheckCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.appapproval.AppApprovalFixedCheckItem;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.appapproval.AppApprovalFixedExtractCondition;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.appapproval.AppApprovalFixedExtractConditionRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.appapproval.AppApprovalFixedExtractItem;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.appapproval.AppApprovalFixedExtractItemRepository;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckInfor;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.AlarmListCheckType;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ExtractionAlarmPeriodDate;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ExtractionResultDetail;
import nts.uk.ctx.at.shared.dom.alarmList.extractionResult.ResultOfEachCondition;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class AppApprovalAggregationProcessService {
	
	@Inject
	private ErAlWorkRecordCheckAdapter erAlWorkRecordCheckAdapter;
	
	@Inject
	private AppApprovalFixedExtractConditionRepository fixedExtractConditionRepo;
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Inject
	private ApplicationAdapter applicationAdapter;
	
	@Inject
	private AgentApprovalAdapter agentApprovalAdapter;
	
	@Inject
	private ManagedParallelWithContext parallel;
	@Inject
	private AppApprovalFixedExtractItemRepository itemReposi;
	@Inject
	private ApprovalRootRecordAdapter approvalRootAdapter;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<ValueExtractAlarm> aggregate(String companyID , List<AlarmCheckConditionByCategory> erAlCheckCondition, DatePeriod period, List<EmployeeSearchDto> employees, 
			Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		
		List<String> empIds = employees.stream().map(e -> e.getId()).collect(Collectors.toList());
		// カテゴリ別アラームチェック条件.抽出対象者の条件
		List<AlarmCheckTargetCondition> extractTargetCondition = erAlCheckCondition.stream().map(c -> c.getExtractTargetCondition()).collect(Collectors.toList());
		// 対象者をしぼり込む
		Map<String, List<RegulationInfoEmployeeResult>> targetMap = erAlWorkRecordCheckAdapter.filterEmployees(period, empIds, extractTargetCondition);
		List<String> filteredEmpIds = targetMap.entrySet().stream().map(e -> e.getValue().stream().map(r -> 
				r.getEmployeeId()).collect(Collectors.toList()))
			.flatMap(e -> e.stream()).distinct().collect(Collectors.toList());
		
		// 申請承認の固定抽出条件のアラーム値を生成する
		List<String> erAlCheckIds = erAlCheckCondition.stream().map(c -> {
			ExtractionCondition extractCond = c.getExtractionCondition();
			if (extractCond instanceof AppApprovalAlarmCheckCondition) {
				AppApprovalAlarmCheckCondition checkCond = (AppApprovalAlarmCheckCondition) extractCond;
				return checkCond.getErrorAlarmCheckId();
			}
			
			return new ArrayList<String>();
		}).flatMap(c -> c.stream()).distinct().collect(Collectors.toList());
		
		// ドメインモデル「申請承認の固定抽出条件」を取得する
		List<AppApprovalFixedExtractCondition> fixedExtractCond =  fixedExtractConditionRepo.findAll(erAlCheckIds, true);
		
		List<ValueExtractAlarm> result = Collections.synchronizedList(new ArrayList<>());
		parallel.forEach(CollectionUtil.partitionBySize(filteredEmpIds, 100), empList -> {
			synchronized(this) {
				if (shouldStop.get()) return;
			}
			
			fixedExtractCond.stream().forEach(fixedCond -> {
				/*switch (AppApprovalFixedCheckItem.valueOf(fixedCond.getNo())) {
					case NOT_APPROVED_1:
						unapprove(empList, period, fixedCond, employees, result);
						break;
					case NOT_APPROVED_2:
						unapprove(empList, period, fixedCond, employees, result);
						break;
					case NOT_APPROVED_3:
						unapprove(empList, period, fixedCond, employees, result);
						break;
					case NOT_APPROVED_4:
						unapprove(empList, period, fixedCond, employees, result);
						break;
					case NOT_APPROVED_5:
						unapprove(empList, period, fixedCond, employees, result);
						break;
					case NOT_APPROVED_COND_NOT_SATISFY:
						unapproveReflectCondNotSatisfy(empList, period, fixedCond,
								ReflectStateImport.NOT_REFLECTED, employees, result);
						break;
					case DISAPPROVE:
						unapproveReflectCondNotSatisfy(empList, period, fixedCond,
								ReflectStateImport.DENIAL, employees, result);
						break;
					case NOT_REFLECT:
						unapproveReflectCondNotSatisfy(empList, period, fixedCond, 
								ReflectStateImport.WAIT_REFLECTION, employees, result);
						break;
					case REPRESENT_APPROVE:
						agentApprove(empList, period, fixedCond, employees, result);
						break;
					case APPROVE:
						approve(empList, period, fixedCond, employees, result);
						break;
					case APPROVER_NOT_SPECIFIED:
						approverNotSpecified(empList, period, fixedCond, employees, result);
						break;
					case MISS_OT_APP:
						missAfterApp();
						break;
					case MISS_WORK_IN_HOLIDAY_APP:
						missAfterApp();
						break;
				}*/
			});
			
		});
		
		return result;
	}
	
	public class DataCheck{
		/** 申請承認の固定抽出条件	 */
		List<AppApprovalFixedExtractCondition> lstExtractCond;
		/**申請承認の固定抽出項目 */
		List<AppApprovalFixedExtractItem> lstExtractItem;
		/**	承認ルートインスタンス */
		List<ApprovalRootStateImport> lstAppRootStates;
		/**申請 */
		List<ApplicationStateImport> lstApp;
		List<ApproverStateImport> lstApproval;
		//選択した社員から代行依頼者IDを検索
		List<AgentApprovalImport> lstAgent;
		//承認者未指定
		Map<String, List<String>> mapAppRootUnregister;
		public DataCheck(String cid, List<String> lstSid, DatePeriod period, String erAlCheckIds) {
			this.lstExtractCond =  fixedExtractConditionRepo.findById(erAlCheckIds, true);
			if(this.lstExtractCond.isEmpty()) return;
			
			this.lstExtractItem = itemReposi.findAll();
			
			this.lstExtractCond.stream().forEach( x -> {
				if(this.lstAppRootStates == null &&
						(x.getNo() == AppApprovalFixedCheckItem.NOT_APPROVED_1
						|| x.getNo() == AppApprovalFixedCheckItem.NOT_APPROVED_2
						|| x.getNo() == AppApprovalFixedCheckItem.NOT_APPROVED_3
						|| x.getNo() == AppApprovalFixedCheckItem.NOT_APPROVED_4
						|| x.getNo() == AppApprovalFixedCheckItem.NOT_APPROVED_5)) {
					this.lstAppRootStates =  approvalRootStateAdapter.findByEmployeesAndPeriod(lstSid, period, 0);
				}
				if(this.lstApp == null &&
						(x.getNo() == AppApprovalFixedCheckItem.NOT_APPROVED_COND_NOT_SATISFY
						|| x.getNo() == AppApprovalFixedCheckItem.DISAPPROVE
						|| x.getNo() == AppApprovalFixedCheckItem.NOT_REFLECT)) {
					// [No.423]社員、日付リスト一致する申請を取得する
					this.lstApp = applicationAdapter.findByEmployeesAndDates(lstSid, period);
				}
				if(x.getNo() == AppApprovalFixedCheckItem.REPRESENT_APPROVE) {
					// ドメインモデル「代行承認」を取得する
					this.lstAgent = agentApprovalAdapter.findByAgentApproverAndPeriod(cid, lstSid, period, 1);
				}
				if(x.getNo() == AppApprovalFixedCheckItem.APPROVE) {
					this.lstApproval = approvalRootStateAdapter.findApprovalRootStateIds(cid, lstSid, period);
				}
				if(x.getNo() == AppApprovalFixedCheckItem.APPROVER_NOT_SPECIFIED) {
					this.mapAppRootUnregister = approvalRootAdapter.lstEmplUnregister(cid, period, lstSid);
				}
			});
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void aggregateCheck(String companyID , String erAlCheckIds,
			DatePeriod period,
			List<String> lstSid, List<WorkPlaceHistImport> lstWplHist,
			List<ResultOfEachCondition> lstResultCondition, List<AlarmListCheckInfor> lstCheckInfor,
			Consumer<Integer> counter, Supplier<Boolean> shouldStop) {
		DataCheck data = new DataCheck(companyID, lstSid, period, erAlCheckIds);
		if(data.lstExtractCond.isEmpty()) return;
		parallel.forEach(CollectionUtil.partitionBySize(lstSid, 100), empList -> {
			synchronized(this) {
				if (shouldStop.get()) return;
			}
			
			data.lstExtractCond.stream().forEach(fixedCond -> {
				lstCheckInfor.add(new AlarmListCheckInfor(String.valueOf(fixedCond.getNo().value), AlarmListCheckType.FixCheck));
				
				switch (fixedCond.getNo()) {
					case NOT_APPROVED_1:
					case NOT_APPROVED_2:
					case NOT_APPROVED_3:
					case NOT_APPROVED_4:
					case NOT_APPROVED_5:
						checkUnapprove(empList,
								period,
								fixedCond,
								lstWplHist,
								lstResultCondition,
								data,
								companyID);
						break;
					case NOT_APPROVED_COND_NOT_SATISFY:						
					case DISAPPROVE:						
					case NOT_REFLECT:
						checkApplicationState(empList,
								period,
								fixedCond, 
								lstWplHist,
								lstResultCondition,
								data);
						break;
					case REPRESENT_APPROVE:
						checkAgentApprove(empList,
								period,
								fixedCond, 
								lstWplHist,
								lstResultCondition,
								data,companyID);
						break;
					case APPROVE:
						checkShouldApprove(empList,
								period,
								fixedCond, 
								lstWplHist,
								lstResultCondition,
								data);
						break;
					case APPROVER_NOT_SPECIFIED:
						approverNotSpecified(empList,
								period,
								fixedCond, 
								lstWplHist,
								lstResultCondition,
								data);
						break;
					case MISS_OT_APP:
						missAfterApp();
						break;
					case MISS_WORK_IN_HOLIDAY_APP:
						missAfterApp();
						break;
				}
			});
			
		});
	}
	/**
	 * 未承認
	 * @param empId
	 * @param period
	 * @param approvalPhaseNo
	 */
	private void checkUnapprove(List<String> empIds, DatePeriod period, AppApprovalFixedExtractCondition fixedExtractCond,
			 List<WorkPlaceHistImport> lstWplHist,
			List<ResultOfEachCondition> lstResultCondition, DataCheck data, String cid) {
		if(data.lstAppRootStates == null || data.lstAppRootStates.isEmpty()) return;
		// 対象者と期間から承認ルートインスタンスを取得する
		List<String> lstAppId = data.lstAppRootStates.stream().map(x -> x.getRootStateID()).collect(Collectors.toList());
		List<ApplicationImport> lstApp = applicationAdapter.getAppById(cid, lstAppId);
		AppApprovalFixedExtractItem item = data.lstExtractItem.stream().filter(x -> x.getNo().equals(fixedExtractCond.getNo()))
				.collect(Collectors.toList()).get(0);
		data.lstAppRootStates.stream().forEach(r -> {
			long unapproveCount = r.getListApprovalPhaseState().stream().filter(p ->
					//INPUT.承認フェーズ番号が承認ルートインスタンス.承認フェーズインスタンス[1..5]に存在するかをチェックする
					p.getPhaseOrder() == fixedExtractCond.getNo().value
					// 承認フェーズインスタンス[INPUT.承認フェーズ番号].承認区分＝【未承認】の件数をチェックする
					&& p.getApprovalAtr() == ApprovalBehaviorAtr.UNAPPROVED.value).count();
			if (unapproveCount > 0) {
				ApplicationImport app = lstApp.stream().filter(x -> x.getAppID().equals(r.getRootStateID())).collect(Collectors.toList()).get(0);
				ExtractionAlarmPeriodDate pDate = new ExtractionAlarmPeriodDate(Optional.ofNullable(r.getApprovalRecordDate()), Optional.empty());
				
				setAlarmResult(fixedExtractCond,lstWplHist, lstResultCondition,
						item, r.getEmployeeID(), period, pDate,
						TextResource.localize("KAL010_522", String.valueOf(fixedExtractCond.getNo().value)),
						TextResource.localize("KAL010_529",app.getAppTypeName()));
			}
		});
	}
	private void setAlarmResult(AppApprovalFixedExtractCondition fixedExtractCond, List<WorkPlaceHistImport> lstWplHist,
			List<ResultOfEachCondition> lstResultCondition, AppApprovalFixedExtractItem item, String sid,
			DatePeriod period,ExtractionAlarmPeriodDate pDate, String alarmContent, String alarmTaget) {
		String wpId = lstWplHist.stream().filter(x -> x.getEmployeeId().equals(sid))
				.collect(Collectors.toList()).get(0).getLstWkpIdAndPeriod().stream()
				.filter(a -> a.getDatePeriod().start().beforeOrEquals(period.end()) && a.getDatePeriod().end().afterOrEquals(period.start()))
				.collect(Collectors.toList()).get(0).getWorkplaceId();
		ExtractionResultDetail detail = new ExtractionResultDetail(sid,
				pDate,
				item.getName(),
				alarmContent,
				GeneralDateTime.now(),
				Optional.ofNullable(wpId),
				fixedExtractCond.getMessage().isPresent() ? Optional.ofNullable(fixedExtractCond.getMessage().get().v()) : Optional.empty(),
				Optional.ofNullable(alarmTaget));
		List<ResultOfEachCondition> result = lstResultCondition.stream()
				.filter(x -> x.getCheckType() == AlarmListCheckType.FreeCheck && x.getNo().equals(String.valueOf(fixedExtractCond.getNo().value)))
				.collect(Collectors.toList());
		if(result.isEmpty()) {
			ResultOfEachCondition resultCon = new ResultOfEachCondition(AlarmListCheckType.FixCheck,
					String.valueOf(fixedExtractCond.getNo().value),
					new ArrayList<>());
			resultCon.getLstResultDetail().add(detail);
			lstResultCondition.add(resultCon);
		} else {
			ResultOfEachCondition ex = result.get(0);
			lstResultCondition.remove(ex);
			ex.getLstResultDetail().add(detail);
			lstResultCondition.add(ex);
		}
	}
	/**
	 * 申請の状況チェック
	 * @param empIds
	 * @param period
	 * @param fixedExtractCond
	 * @param lstWplHist
	 * @param lstResultCondition
	 * @param data
	 */
	private void checkApplicationState(List<String> empIds, DatePeriod period, AppApprovalFixedExtractCondition fixedExtractCond,
			List<WorkPlaceHistImport> lstWplHist,
			List<ResultOfEachCondition> lstResultCondition, DataCheck data) {
		if(data.lstApp == null || data.lstApp.isEmpty()) return;
		
		ReflectStateImport refState = ReflectStateImport.WAIT_REFLECTION;
		if(fixedExtractCond.getNo() == AppApprovalFixedCheckItem.NOT_APPROVED_COND_NOT_SATISFY) refState = ReflectStateImport.NOT_REFLECTED;
		if(fixedExtractCond.getNo() == AppApprovalFixedCheckItem.DISAPPROVE) refState = ReflectStateImport.DENIAL;
		if(fixedExtractCond.getNo() == AppApprovalFixedCheckItem.NOT_REFLECT) refState = ReflectStateImport.WAIT_REFLECTION;
		AppApprovalFixedExtractItem item = data.lstExtractItem.stream().filter(x -> x.getNo().equals(fixedExtractCond.getNo()))
				.collect(Collectors.toList()).get(0);
		for(ApplicationStateImport a: data.lstApp) {
			if(a.getReflectState() == refState.value) {
				ExtractionAlarmPeriodDate pDate = new ExtractionAlarmPeriodDate(Optional.ofNullable(a.getAppDate()), Optional.empty());
				setAlarmResult(fixedExtractCond,lstWplHist, lstResultCondition,
						item, a.getEmployeeID(), period, pDate,
						TextResource.localize("KAL010_523", a.getAppTypeName(), refState.name),
						TextResource.localize("KAL010_529", a.getAppTypeName()));
			}
		}
	}
	/**
	 * 代行者として反映待ち申請の状況をチェック
	 * @param empIds
	 * @param period
	 * @param fixedExtractCond
	 * @param lstWplHist
	 * @param lstResultCondition
	 * @param data
	 * @param cid
	 */
	private void checkAgentApprove(List<String> empIds, DatePeriod period, AppApprovalFixedExtractCondition fixedExtractCond,
			List<WorkPlaceHistImport> lstWplHist,
			List<ResultOfEachCondition> lstResultCondition, DataCheck data, String cid) {
		if(data.lstAgent == null || data.lstAgent.isEmpty()) return;
		List<String> lstApproverID = data.lstAgent.stream().map(x -> x.getApproverID()).distinct().collect(Collectors.toList());
		if(lstApproverID.isEmpty()) return;
		AppApprovalFixedExtractItem item = data.lstExtractItem.stream().filter(a -> a.getNo().equals(fixedExtractCond.getNo()))
				.collect(Collectors.toList()).get(0);
		ExtractionAlarmPeriodDate pDate = new ExtractionAlarmPeriodDate(Optional.ofNullable(period.start()), Optional.empty());
		lstApproverID.stream().forEach(x-> {
			List<String> lstAgentID = data.lstAgent.stream()
					.filter(a -> a.getApproverID().equals(x)).map(z -> z.getAgentID()).collect(Collectors.toList());
			//ドメインモデル「承認ルートインスタンス」を取得する
			List<ApprovalRootStateImport> lstAppRootStatesAgen = approvalRootStateAdapter.findByAgentApproverAndPeriod(cid, lstAgentID, period);
			List<String> lstAppId = lstAppRootStatesAgen.stream()
					.map(a -> a.getRootStateID()).collect(Collectors.toList());
			if(!lstAppId.isEmpty()) {
				//ドメインモデル「申請」を取得する
				List<ApplicationImport> lstApp = applicationAdapter.getAppById(cid, lstAppId).stream()
						.filter(a -> a.getState() == 1)
						.collect(Collectors.toList());
				if(!lstApp.isEmpty()) {
					setAlarmResult(fixedExtractCond,lstWplHist, lstResultCondition,
							item, x, period, pDate,
							TextResource.localize("KAL010_524", String.valueOf(lstApp.size())),
							TextResource.localize("KAL010_529", lstApp.stream().map(a -> a.getAppTypeName()).distinct().collect(Collectors.toList()).toString()));
				}
			}
		});
		
	}
	/**
	 * 10.要承認: 承認すべき申請をチェック
	 * @param empIds
	 * @param period
	 * @param fixedExtractCond
	 * @param employees
	 * @param extractAlarms
	 */
	private void checkShouldApprove(List<String> empIds, DatePeriod period, AppApprovalFixedExtractCondition fixedExtractCond,
			List<WorkPlaceHistImport> lstWplHist,
			List<ResultOfEachCondition> lstResultCondition, DataCheck data) {
		if(data.lstApproval == null || data.lstApproval.isEmpty()) return;
		
		List<String> lstApproverId = data.lstApproval.stream()
				.map(x -> x.getApproverId()).distinct().collect(Collectors.toList());
		AppApprovalFixedExtractItem item = data.lstExtractItem.stream().filter(a -> a.getNo().equals(fixedExtractCond.getNo()))
				.collect(Collectors.toList()).get(0);

		ExtractionAlarmPeriodDate pDate = new ExtractionAlarmPeriodDate(Optional.ofNullable(period.start()), Optional.empty());
		lstApproverId.stream().forEach(approver -> {
			int count = data.lstApproval.stream().filter(x -> x.getApproverId().equals(approver)).collect(Collectors.toList()).size();
			setAlarmResult(fixedExtractCond,lstWplHist, lstResultCondition,
					item, approver, period, pDate,
					TextResource.localize("KAL010_526", String.valueOf(count)),
					TextResource.localize("KAL010_527", String.valueOf(count)));
		});		
	}
	/**
	 * 11.承認者未指定
	 * @param empIds
	 * @param period
	 * @param fixedExtractCond
	 * @param employees
	 * @param extractAlarms
	 */
	private void approverNotSpecified(List<String> empIds, DatePeriod period, AppApprovalFixedExtractCondition fixedExtractCond,
			List<WorkPlaceHistImport> lstWplHist,
			List<ResultOfEachCondition> lstResultCondition, DataCheck data) {
		if(data.mapAppRootUnregister == null || data.mapAppRootUnregister.isEmpty()) return;
		AppApprovalFixedExtractItem item = data.lstExtractItem.stream().filter(a -> a.getNo().equals(fixedExtractCond.getNo()))
				.collect(Collectors.toList()).get(0);
		ExtractionAlarmPeriodDate pDate = new ExtractionAlarmPeriodDate(Optional.ofNullable(period.start()), Optional.empty());
		data.mapAppRootUnregister.forEach((a,b) -> {
			setAlarmResult(fixedExtractCond,lstWplHist, lstResultCondition,
					item, a, period, pDate,
					TextResource.localize("KAL010_528", period.start().toString("yyyy/MM/dd") + "～" + period.end().toString("yyyy/MM/dd")),
					TextResource.localize("KAL010_529", b.toString()));
		});
	}
	
	private void missAfterApp() {
		// TODO: JP design team'll create new request list to get 申請
	}
}
