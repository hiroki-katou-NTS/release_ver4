package nts.uk.ctx.at.shared.dom.adapter.workplace.config.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobTitleHistoryExport {

	private String historyId;
	
	private DatePeriod baseDate;
}
