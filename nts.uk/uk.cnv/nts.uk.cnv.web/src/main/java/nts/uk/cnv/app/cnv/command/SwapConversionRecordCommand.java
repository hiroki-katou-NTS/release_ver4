package nts.uk.cnv.app.cnv.command;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SwapConversionRecordCommand {
	String category;
	String table;
	int recordNo1;
	int recordNo2;
}
