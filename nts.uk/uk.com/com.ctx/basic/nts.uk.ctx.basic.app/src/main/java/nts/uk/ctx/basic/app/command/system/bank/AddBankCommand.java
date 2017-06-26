package nts.uk.ctx.basic.app.command.system.bank;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddBankCommand {
	
	private String bankCode;
	private String bankName;
	private String bankNameKana;
	private String memo;

}
