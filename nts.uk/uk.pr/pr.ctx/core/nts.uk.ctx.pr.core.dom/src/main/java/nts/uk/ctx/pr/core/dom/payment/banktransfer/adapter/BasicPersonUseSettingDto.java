package nts.uk.ctx.pr.core.dom.payment.banktransfer.adapter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasicPersonUseSettingDto {
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

	public static BasicPersonUseSettingDto createFromJavaType(int useSet, int priorityOrder, int paymentMethod,
			int partialPaySet, int fixAmountMny, int fixRate, String fromLineBankCd, String toBranchId,
			int accountAtr, String accountNo, String accountHolderKnName, String accountHolderName) {
		return new BasicPersonUseSettingDto(useSet, priorityOrder, paymentMethod, partialPaySet, fixAmountMny, fixRate,
				fromLineBankCd, toBranchId, accountAtr, accountNo, accountHolderKnName, accountHolderName);
	}

	public BasicPersonUseSettingDto(int useSet, int priorityOrder, int paymentMethod, int partialPaySet, int fixAmountMny,
			int fixRate, String fromLineBankCd, String toBranchId, int accountAtr, String accountNo,
			String accountHolderKnName, String accountHolderName) {
		super();
		this.useSet = useSet;
		this.priorityOrder = priorityOrder;
		this.paymentMethod = paymentMethod;
		this.partialPaySet = partialPaySet;
		this.fixAmountMny = fixAmountMny;
		this.fixRate = fixRate;
		this.fromLineBankCd = fromLineBankCd;
		this.toBranchId = toBranchId;
		this.accountAtr = accountAtr;
		this.accountNo = accountNo;
		this.accountHolderKnName = accountHolderKnName;
		this.accountHolderName = accountHolderName;
	}
}
