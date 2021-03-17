package nts.uk.cnv.app.cnv.command;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class RegistConversionSourceCommand {
	String sourceId;
	String category;
	String sourceTableName;
	String condition;
	String memo;
}
