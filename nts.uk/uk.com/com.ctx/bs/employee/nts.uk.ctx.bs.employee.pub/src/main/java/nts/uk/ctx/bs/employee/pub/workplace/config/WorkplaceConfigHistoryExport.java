package nts.uk.ctx.bs.employee.pub.workplace.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkplaceConfigHistoryExport {
	//履歴ID
	private String historyId;
	private DatePeriod period;
}
