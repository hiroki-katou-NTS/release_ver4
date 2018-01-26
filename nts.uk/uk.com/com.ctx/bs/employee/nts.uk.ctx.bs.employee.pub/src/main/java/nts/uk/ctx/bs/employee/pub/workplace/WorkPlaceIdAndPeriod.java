package nts.uk.ctx.bs.employee.pub.workplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkPlaceIdAndPeriod {

	/** The date range. */
	// 配属期間
	private DatePeriod dateRange;

	/** The workplace id. */
	// 職場ID
	private String workplaceId;

}
