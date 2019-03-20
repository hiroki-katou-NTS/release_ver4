package nts.uk.ctx.bs.employee.pub.workplace;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.history.DateHistoryItem;
/**
 * 基準日以降の所属職場
 * @author lanlt
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffWorkplaceHistoryExport {
	private String sid;
	// 所属職場履歴 - list historyId, start Date, endDate
	private List<DateHistoryItem> historyItems;
	// key histId, value AffWorkplaceHistoryItemExport (所属職場履歴項目)
	private Map<String, AffWorkplaceHistoryItemExport> workplaceHistItems;
	public List<String> getHistoryIds() {
		return historyItems.stream().map(dateItem -> dateItem.identifier()).collect(Collectors.toList());
	}

}
