package nts.uk.ctx.bs.employee.pub.workplace.config.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class JobTitleHistoryExport {

	private String historyId;
	
	private DatePeriod baseDate;
}
