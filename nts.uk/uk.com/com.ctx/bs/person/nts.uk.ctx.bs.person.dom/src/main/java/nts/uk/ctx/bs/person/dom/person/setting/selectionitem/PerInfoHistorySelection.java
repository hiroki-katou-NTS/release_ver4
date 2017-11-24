package nts.uk.ctx.bs.person.dom.person.setting.selectionitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@AllArgsConstructor
@Getter
@Setter

/**
 * 
 * @author tuannv
 *
 */

// 選択肢履歴ドメイン
public class PerInfoHistorySelection {
	private String histId;
	private String selectionItemId;
	private String companyId;
	private DatePeriod period;

	public static PerInfoHistorySelection createHistorySelection(String histId, String selectionItemId,
			String companyId, DatePeriod period) {

		// 選択肢履歴 パラメーター帰還
		return new PerInfoHistorySelection(histId, selectionItemId, companyId, period);
	}

	public void updateDate(DatePeriod period) {
		this.period = period;
	}
}
