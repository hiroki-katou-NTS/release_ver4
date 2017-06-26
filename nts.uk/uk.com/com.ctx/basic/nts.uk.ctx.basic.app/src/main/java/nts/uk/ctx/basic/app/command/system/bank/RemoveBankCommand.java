package nts.uk.ctx.basic.app.command.system.bank;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RemoveBankCommand {

	private String bankCode;
}
