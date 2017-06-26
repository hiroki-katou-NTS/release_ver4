package nts.uk.file.pr.app.export.banktransfer.data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTransferARpData {
	// A_DBD_002
	private String bankCode;
	// A_DBD_003
	private String bankName;
	// A_DBD_004
	private String branchCode;
	// A_DBD_005
	private String branchName;
	// A_DBD_006
	private String toAccountAtr;
	// A_DBD_007
	private String toAccountNo;
	// A_DBD_008
	private String scd;
	// A_DBD_009
	private String name;
	// A_DBD_010
	private BigDecimal paymentMyn;
	
	private String unit;
}
