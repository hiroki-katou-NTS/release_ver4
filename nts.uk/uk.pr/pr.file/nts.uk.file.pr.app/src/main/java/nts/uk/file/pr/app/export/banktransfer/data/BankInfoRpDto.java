package nts.uk.file.pr.app.export.banktransfer.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankInfoRpDto {
	private String branchId;
	private String bankNameKana;
	private String branchNameKana;
	private int accountAtr;
	private String accountNo;
	private String accountNameKana;
}
