package nts.uk.ctx.pr.core.app.command.payment.banktransfer;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.payment.banktransfer.BankInfo;
import nts.uk.ctx.pr.core.dom.payment.banktransfer.BankTransfer;

@Data
@NoArgsConstructor
public class AddBankTransferCommand {
	private GeneralDate payDateOfScreenE;
	private int processingNoOfScreenE;
	private int sparePayAtrOfScreenE;
	private int processingYMOfScreenE;
	private String companyNameKana;
	private String personId;
	private String departmentCode;
	private GeneralDate paymentDate;
	private int paymentBonusAtr;
	private BigDecimal paymentMoney;
	private int processingNo;
	private int sparePaymentAtr;
	private int processingYM;
	private BankInfo fromBank;
	private BankInfo toBank;

	public BankTransfer toDomain(String companyCode) {
		BankTransfer domain = BankTransfer.createFromJavaType(companyCode, this.companyNameKana, this.personId,
				this.departmentCode, this.paymentDate, this.paymentBonusAtr, this.paymentMoney, this.processingNo,
				this.processingYM, this.sparePaymentAtr);
		domain.fromBank(this.getFromBank().getBranchId(), this.getFromBank().getBankNameKana(),
				this.getFromBank().getBranchNameKana(), this.getFromBank().getAccountAtr(),
				this.getFromBank().getAccountNo());
		domain.toBank(this.getToBank().getBranchId(), this.getToBank().getBankNameKana(),
				this.getToBank().getBranchNameKana(), this.getToBank().getAccountAtr(), this.getToBank().getAccountNo(),
				this.getToBank().getAccountNameKana());
		return domain;
	}
}