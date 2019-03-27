package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Value;
import lombok.val;
import nts.arc.error.BusinessException;
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
		
		private final Map<GeneralDate, AppRootRecordConfirmForQuery> mapConfirms;
		
		public List(java.util.List<AppRootRecordConfirmForQuery> list) {
			this.mapConfirms = list.stream().collect(Collectors.toMap(a -> a.getRecordDate(), a -> a));
		}
		
		public java.util.List<ApprovalRootStateStatus> aggregate(
				DatePeriod period,
				String employeeId,
				AppRootIntermForQuery.List interms) {
			
			return period.datesBetween().stream().map(date -> {
				
				AppRootRecordConfirmForQuery confirm = this.mapConfirms.get(date);
				if (confirm == null) {
					return new ApprovalRootStateStatus(date, employeeId, DailyConfirmAtr.UNAPPROVED);
				}
				
				AppRootIntermForQuery interm = interms.find(employeeId, date)
						.orElseThrow(() -> new BusinessException("Msg_1430", "承認者"));
				
				return new ApprovalRootStateStatus(
						date,
						employeeId,
						confirm.getConfirmStatus(interm.getFinalPhaseOrder()));
				
			}).collect(Collectors.toList());
		}
	}
	
	@Value
	public static class Aggregation {
		private final int numberOfConfirmed;
		private final int numberOfUnconfirmed;
	}
}
