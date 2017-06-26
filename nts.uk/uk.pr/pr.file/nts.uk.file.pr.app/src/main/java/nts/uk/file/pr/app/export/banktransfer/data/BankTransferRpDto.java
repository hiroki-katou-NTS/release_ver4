package nts.uk.file.pr.app.export.banktransfer.data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
@AllArgsConstructor
public class BankTransferRpDto {
	private String companyCode;
	private String companyNameKana;
	private String personId;
	private String fromBranchId;
	private String fromBankNameKana;
	private String fromBranchNameKana;
	private int fromAccountAtr;
	private String fromAccountNo;
	private String toBranchId;
	private String toBankNameKana;
	private String toBranchNameKana;
	private int toAccountAtr;
	private String toAccountNo;
	private String toAccountNameKana;
	private String depCd;
	private BigDecimal paymentMoney;
	private int paymentBonusAtr;
	private int processingNo;
	private int processingYM;
	private GeneralDate payDate;
	private int sparePayAtr;
}
