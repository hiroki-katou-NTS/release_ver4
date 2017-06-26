package nts.uk.ctx.basic.app.command.system.bank.linebank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RemoveLineBankCommand {
	@Getter
	private String lineBankCode;
}
