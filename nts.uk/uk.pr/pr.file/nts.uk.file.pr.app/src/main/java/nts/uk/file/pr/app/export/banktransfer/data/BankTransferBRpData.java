package nts.uk.file.pr.app.export.banktransfer.data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTransferBRpData {
	// B_DBD_002: chua biet
	private String bankCode;
	// B_DBD_003
	private String bankName;
	// B_DBD_004: chua biet
	private String branchCode;
	// B_DBD_005
	private String branchName;
	// B_DBD_006
	private String fromAccountAtr;
	// B_DBD_007
	private String fromAccountNo;
	// B_DBD_008
	private int numPerSameType;
	// B_DBD_009
	private BigDecimal paymentMyn;
	// Unit of paymentMyn (円)
	private String unit;
	// Unit of person(人)
	private String unitPerson;
}
