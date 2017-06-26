package nts.uk.ctx.basic.app.command.system.bank;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RemoveListBankCommand {
	/**
	 * Map<BankCode, BranchCode>
	 */
	private List<KeyBank> bank;
}
