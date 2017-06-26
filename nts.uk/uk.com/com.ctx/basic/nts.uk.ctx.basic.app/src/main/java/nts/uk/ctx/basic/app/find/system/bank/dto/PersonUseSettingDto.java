package nts.uk.ctx.basic.app.find.system.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonUseSettingDto {
	private int useSet;

	private int priorityOrder;

	private int paymentMethod;

	private int partialPaySet;

	private int fixAmountMny;

	private int fixRate;

	private String fromLineBankCd;

	private String toBranchId;

	private int accountAtr;

	private String accountNo;

	private String accountHolderKnName;

	private String accountHolderName;
	
}
