package nts.uk.ctx.at.function.dom.adapter;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.com.history.DateHistoryItem;

@Getter
@Setter
@AllArgsConstructor
public class AffWorkplaceHistoryImport {
	private String sid;
	// 所属職場履歴 - list historyId, start Date, endDate
	private List<DateHistoryItem> historyItems;
	// key histId, value AffWorkplaceHistoryItemExport (所属職場履歴項目)
	private Map<String, AffWorkplaceHistoryItemImport> workplaceHistItems;
	
}
