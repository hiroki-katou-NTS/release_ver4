package nts.uk.ctx.at.function.dom.alarm.alarmdata;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ValueExtractAlarm {
	private Optional<String> workplaceID;
	private String employeeID;
	private String alarmValueDate;
	private String classification; 
	private String alarmItem;
	private String alarmValueMessage;
	private Optional<String> comment;
	public ValueExtractAlarm(String workplaceID, String employeeID, String alarmValueDate, String classification, String alarmItem, String alarmValueMessage, String comment) {
		super();
		this.workplaceID = Optional.ofNullable(workplaceID);
		this.employeeID = employeeID;
		this.alarmValueDate = alarmValueDate;
		this.classification = classification;
		this.alarmItem = alarmItem;
		this.alarmValueMessage = alarmValueMessage;
		this.comment = Optional.ofNullable(comment);
	}
	
	
}
