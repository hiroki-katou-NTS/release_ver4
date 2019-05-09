package nts.uk.ctx.at.function.dom.adapter.workrecord.erroralarm.recordcheck;

import lombok.Value;
import nts.arc.time.GeneralDate;
@Value
public class ErrorRecordImport {
	private GeneralDate date;
	private String employeeId;
	private String erAlId;
	private  boolean error;
	private String checkedValue;
	
}