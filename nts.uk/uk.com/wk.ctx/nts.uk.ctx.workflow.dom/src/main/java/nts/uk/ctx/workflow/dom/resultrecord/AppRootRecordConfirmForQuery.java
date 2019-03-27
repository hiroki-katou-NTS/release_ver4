package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Value;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.DailyConfirmAtr;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootStateStatus;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実績確認状態
 * 承認状況の照会で集計結果を出力するためのクエリ用
 */
@Value
public class AppRootRecordConfirmForQuery {

	/** 承認ルート中間データID */
	private final String rootId;
	
	private final String employeeId;
	
	private final GeneralDate recordDate;
	
	/** 確認済みフェーズがあるか */
	private final boolean existsConfirmedPhase;
	
	/**
	 * 確認済みの最終フェーズ
	 * null if existsConfirmedPhase is false
	 */
	private final Integer finalConfirmedPhase;
	
	/**
	 * 指定したフェーズが既に確認済みであればtrueを返す
	 * 
	 * @param phaseOrder
	 * @return return true if a given phase has been confirmed
	 */
	public DailyConfirmAtr getConfirmStatus(int finalPhaseOrder) {
		
		if (!this.existsConfirmedPhase) {
			return DailyConfirmAtr.UNAPPROVED;
		}
		
		if (this.finalConfirmedPhase < finalPhaseOrder) {
			return DailyConfirmAtr.ON_APPROVED;
		}

		return DailyConfirmAtr.ALREADY_APPROVED;
	}
	
	public static class List {
		
		private final Map<String, Map<GeneralDate, AppRootRecordConfirmForQuery>> mapConfirms;
		
		public List(java.util.List<AppRootRecordConfirmForQuery> list) {
			
			this.mapConfirms = new HashMap<>();
			for (val confirm : list) {
				String employeeId = confirm.getEmployeeId();
				if (!this.mapConfirms.containsKey(employeeId)) {
					this.mapConfirms.put(employeeId, new HashMap<>());
				}
				
				val mapForOneEmployee = this.mapConfirms.get(employeeId);
				mapForOneEmployee.put(confirm.getRecordDate(), confirm);
			}
		}
		
		public AggregateResult aggregate(
				DatePeriod period,
				String employeeId,
				AppRootIntermForQuery.List interms) {
			
			val results = new ArrayList<ApprovalRootStateStatus>();
			boolean isError = false;
			val mapForOneEmployee = this.mapConfirms.get(employeeId);
			
			for (val date : period.datesBetween()) {

				AppRootRecordConfirmForQuery confirm = mapForOneEmployee.get(date);
				if (confirm == null) {
					results.add(new ApprovalRootStateStatus(date, employeeId, DailyConfirmAtr.UNAPPROVED));
					continue;
				}
				
				val intermOpt = interms.find(employeeId, date);
				if (!intermOpt.isPresent()) {
					isError = true;
					continue;
				}
				
				val status = new ApprovalRootStateStatus(
						date,
						employeeId,
						confirm.getConfirmStatus(intermOpt.get().getFinalPhaseOrder()));
				results.add(status);
			}
			
			return new AggregateResult(results, isError);
		}
		
		@Value
		public static class AggregateResult {
			
			private final java.util.List<ApprovalRootStateStatus> results;
			private final boolean error;
		}
	}
	
	@Value
	public static class Aggregation {
		private final int numberOfConfirmed;
		private final int numberOfUnconfirmed;
	}
}
