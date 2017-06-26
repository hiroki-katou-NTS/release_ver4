package nts.uk.file.pr.app.export.banktransfer.data;

import lombok.Data;

@Data
public class BankTransferARpHeader {
	private String companyName;
	private String code;
	private String date;
	// A_CTR_003 || A_CTR_004
	private String state;
	// A_DBD_001
	private String person;
}
