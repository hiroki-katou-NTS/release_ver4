package nts.uk.ctx.at.record.app.command.dailyperform.month;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.monthly.root.common.ClosureDateDto;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.IntegrationOfMonthly;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMonthDailyParam {
	
	/** 年月: 年月 */
	private Integer yearMonth;

	/** 社員ID: 社員ID */
	private String employeeId;

	/** 締めID: 締めID */
	private int closureId;

	/** 締め日: 日付 */
	private ClosureDateDto closureDate;
	
	private Optional<IntegrationOfMonthly> domainMonth;
	
	private DatePeriod datePeriod;
	
	private String messageRed;
	
	private Boolean hasFlex;
	
	private Boolean needCallCalc;

}
