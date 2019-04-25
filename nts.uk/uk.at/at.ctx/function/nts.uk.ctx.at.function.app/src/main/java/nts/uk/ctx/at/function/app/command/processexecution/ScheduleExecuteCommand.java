package nts.uk.ctx.at.function.app.command.processexecution;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDateTime;
//import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Data
@NoArgsConstructor
public class ScheduleExecuteCommand {
	/* 会社ID */
	private String companyId;
	
	/* コード */
	private String execItemCd;
	
	private String scheduleId;
	
	private GeneralDateTime nextDate;
	
	@Data
	public static class ForBatchServer {
		private ScheduleExecuteCommand command;
		private String contexts;
	}
}
