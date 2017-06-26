package nts.uk.ctx.basic.app.command.system.bank.linebank;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TransferLineBankCommand {
	private String oldLineBankCode;
	private String newLineBankCode;
	private int allowDelete;
}
