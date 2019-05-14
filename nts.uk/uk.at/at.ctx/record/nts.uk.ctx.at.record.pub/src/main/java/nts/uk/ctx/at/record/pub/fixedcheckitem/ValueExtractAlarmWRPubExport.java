package nts.uk.ctx.at.record.pub.fixedcheckitem;

import java.util.Optional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;

@Getter
@Setter
@NoArgsConstructor
public class ValueExtractAlarmWRPubExport {
	private Optional<String> workplaceID;
	
	private String employeeID;
	
	private GeneralDate alarmValueDate;
	
	private String classification;
	
	private String alarmItem;
	
	private String alarmValueMessage;
	
	private Optional<String> comment;
	
	private Optional<String> checkedValue;
	
    private int consecutiveDays;

	public ValueExtractAlarmWRPubExport(String workplaceID, String employeeID, GeneralDate alarmValueDate,
			String classification, String alarmItem, String alarmValueMessage, String comment, String checkedValue) {
		super();
		this.workplaceID = Optional.ofNullable(workplaceID);
		this.employeeID = employeeID;
		this.alarmValueDate = alarmValueDate;
		this.classification = classification;
		this.alarmItem = alarmItem;
		this.alarmValueMessage = alarmValueMessage;
		this.comment = Optional.ofNullable(comment);
		this.checkedValue = Optional.ofNullable(checkedValue);
	}
	
	public ValueExtractAlarmWRPubExport(String workplaceID, String employeeID, GeneralDate alarmValueDate,
			String classification, String alarmItem, String alarmValueMessage, String comment, String checkedValue, int consecutiveDays) {
		super();
		this.workplaceID = Optional.ofNullable(workplaceID);
		this.employeeID = employeeID;
		this.alarmValueDate = alarmValueDate;
		this.classification = classification;
		this.alarmItem = alarmItem;
		this.alarmValueMessage = alarmValueMessage;
		this.comment = Optional.ofNullable(comment);
		this.checkedValue = Optional.ofNullable(checkedValue);
		this.consecutiveDays = consecutiveDays;
	}
	
}
