package nts.uk.ctx.at.record.dom.workrecord.alarmlist.fourweekfourdayoff;

import lombok.Value;
import nts.arc.time.calendar.period.DatePeriod;

@Value
public class AlarmExtractionValue4W4D {
	private String workplaceID;
	private String employeeID;
	private DatePeriod datePeriod;
	private String classification; 
	private String alarmItem;
	private String alarmValueMessage;
	private String comment;
	private String checkedValue;
}
