package nts.uk.ctx.pereg.dom.person.setting.selectionitem.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.history.DateHistoryItem;
import nts.uk.shr.com.history.strategic.PersistentResidentHistory;
import nts.arc.time.calendar.period.DatePeriod;

@Getter
public class SelectionHistory implements PersistentResidentHistory<DateHistoryItem, DatePeriod, GeneralDate> {

	// domain name: 選択肢履歴

	/**
	 * 会社ID
	 */
	private String companyId;

	/**
	 * 選択項目ID
	 */
	private String selectionItemId;

	/**
	 * 履歴
	 */
	private List<DateHistoryItem> dateHistoryItems;

	private SelectionHistory(String companyId, String selectionItemId, List<DateHistoryItem> dateHistoryItems) {
		super();
		this.companyId = companyId;
		this.selectionItemId = selectionItemId;
		this.dateHistoryItems = dateHistoryItems;
	}

	public static SelectionHistory createNewHistorySelection(String companyId, String selectionItemId) {
		return new SelectionHistory(companyId, selectionItemId, new ArrayList<>());
	}

	public static SelectionHistory createFullHistorySelection(String companyId, String selectionItemId,
			List<DateHistoryItem> dateHistoryItems) {
		return new SelectionHistory(companyId, selectionItemId, dateHistoryItems);
	}

	public static SelectionHistory createHistorySelection(String histId, String selectionItemId, String companyId,
			DatePeriod period) {
		DateHistoryItem dateHistoryItem = new DateHistoryItem(histId, period);
		return new SelectionHistory(companyId, selectionItemId, Arrays.asList(dateHistoryItem));
	}

	@Override
	public List<DateHistoryItem> items() {
		return dateHistoryItems;
	}

}
