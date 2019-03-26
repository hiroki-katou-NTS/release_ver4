package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.ArrayList;
import java.util.Collections;

import lombok.Value;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.DailyConfirmAtr;
import nts.uk.ctx.workflow.dom.service.output.ApprovalRootStateStatus;

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
		
		private final java.util.List<AppRootRecordConfirmForQuery> list;
		
		public List(java.util.List<AppRootRecordConfirmForQuery> list) {
			this.list = Collections.unmodifiableList(list);
		}
		
		public java.util.List<ApprovalRootStateStatus> aggregate(AppRootIntermForQuery.List interms) {
			
			java.util.List<ApprovalRootStateStatus> results = new ArrayList<>();
			
			for (val confirm : this.list) {
				
				val interm = interms.find(confirm.employeeId, confirm.recordDate)
						.orElseThrow(() -> new BusinessException("Msg_1430", "承認者"));
				
				results.add(new ApprovalRootStateStatus(
						confirm.recordDate,
						confirm.employeeId,
						confirm.getConfirmStatus(interm.getFinalPhaseOrder())));
			}
			
			return results;
		}
	}
	
	@Value
	public static class Aggregation {
		private final int numberOfConfirmed;
		private final int numberOfUnconfirmed;
	}
}
