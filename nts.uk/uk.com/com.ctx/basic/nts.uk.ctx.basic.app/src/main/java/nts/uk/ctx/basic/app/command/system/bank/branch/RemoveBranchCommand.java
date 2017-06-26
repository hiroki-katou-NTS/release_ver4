package nts.uk.ctx.basic.app.command.system.bank.branch;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RemoveBranchCommand {
	private String branchId;
}
