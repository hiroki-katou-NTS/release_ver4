package nts.uk.ctx.bs.employee.pub.generalinfo.classification;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@AllArgsConstructor
@NoArgsConstructor
public class ExClassificationHistItemDto {
	
	private String historyId;
	
	private DatePeriod period;
	
	private String classificationCode;
	
}
