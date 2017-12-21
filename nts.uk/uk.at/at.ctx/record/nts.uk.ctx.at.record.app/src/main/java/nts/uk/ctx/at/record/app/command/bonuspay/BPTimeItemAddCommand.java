package nts.uk.ctx.at.record.app.command.bonuspay;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class BPTimeItemAddCommand {
	private String timeItemId;
	public int useAtr;
	public String timeItemName;
	public int timeItemNo;
	public int timeItemTypeAtr;
}
