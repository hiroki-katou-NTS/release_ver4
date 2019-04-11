package nts.uk.ctx.at.shared.dom.adapter.workplace.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkplaceConfigHistoryImport {
	// 履歴ID
	private String historyId;
	private DatePeriod period;
}
