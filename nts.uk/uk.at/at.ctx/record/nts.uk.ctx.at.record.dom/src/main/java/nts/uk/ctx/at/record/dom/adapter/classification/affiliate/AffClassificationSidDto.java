package nts.uk.ctx.at.record.dom.adapter.classification.affiliate;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Getter
@Setter
public class AffClassificationSidDto {
	
	private String employeeId;
	
	private String classificationCode;
	
	private DatePeriod dateRange;

	public AffClassificationSidDto(String employeeId, String classificationCode, DatePeriod dateRange) {
		super();
		this.employeeId = employeeId;
		this.classificationCode = classificationCode;
		this.dateRange = dateRange;
	}

}
