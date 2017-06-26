package nts.uk.ctx.basic.app.command.system.bank.linebank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsignorCommand {
	public String code;
	public String memo;
}
