package nts.uk.ctx.at.record.app.command.bonuspay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CBPSettingCommand {
	public int action;
	
	public String bonusPaySettingCode;
}
