package nts.uk.file.pr.app.export.banktransfer.data;

import lombok.Data;

@Data
public class BankTransferBRpHeader {
	private String companyName;
	private String startCode;
	private String endCode;
	private String date;
	// A_DBD_001
	private String person;
}
