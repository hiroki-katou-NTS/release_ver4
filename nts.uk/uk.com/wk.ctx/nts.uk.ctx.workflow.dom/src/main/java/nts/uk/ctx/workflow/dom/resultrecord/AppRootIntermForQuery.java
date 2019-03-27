package nts.uk.ctx.workflow.dom.resultrecord;

import java.util.Optional;

import lombok.Value;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.collection.ListHashMap;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実績承認ルート中間データ
 * 承認状況の照会で集計結果を出力するためのクエリ用
 */
@Value
public class AppRootIntermForQuery {
	
	/** ルートID */
	private final String rootId;

	/** 対象者 */
	private final String employeeId;
	
	/** 履歴の期間 */
	private final DatePeriod period;
	
	/**
	 * 最終フェーズの番号(順序)
	 * 就業実績確認状態にこの番号の承認済みフェーズがあれば、日次Dが確認済みとみなせる
	 */
	private final int finalPhaseOrder;
	
	public static class List {
		
		private final ListHashMap<String, AppRootIntermForQuery> mapByEmployeeId;
		
		public List(java.util.List<AppRootIntermForQuery> source) {
			
			this.mapByEmployeeId = new ListHashMap<>();
			source.forEach(s -> {
				this.mapByEmployeeId.addElement(s.employeeId, s);
			});
			
		}
		
		public Optional<AppRootIntermForQuery> find(String employeeId, GeneralDate date) {
			
			val history = this.mapByEmployeeId.get(employeeId);
			if (history == null) {
				return Optional.empty();
			}
			
			return history.stream().filter(h -> h.period.contains(date)).findFirst();
		}
	}
	
}
