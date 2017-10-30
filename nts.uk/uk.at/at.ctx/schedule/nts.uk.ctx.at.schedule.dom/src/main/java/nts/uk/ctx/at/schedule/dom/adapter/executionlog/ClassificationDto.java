package nts.uk.ctx.at.schedule.dom.adapter.executionlog;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Getter
@Setter
public class ClassificationDto {
	/** The period. */
	private DatePeriod period;

	/** The employee id. */
	private String employeeId;

	/** The job title code. */
	private String classificationCode;

	/** The job title name. */
	private String classificationName;

}
