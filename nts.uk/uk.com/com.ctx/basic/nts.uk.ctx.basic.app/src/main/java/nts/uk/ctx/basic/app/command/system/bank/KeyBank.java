package nts.uk.ctx.basic.app.command.system.bank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyBank {
	private String bankCode;
	private String branchId;
}
