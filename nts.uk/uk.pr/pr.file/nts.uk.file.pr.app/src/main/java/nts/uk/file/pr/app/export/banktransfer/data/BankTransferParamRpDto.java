package nts.uk.file.pr.app.export.banktransfer.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
@AllArgsConstructor
public class BankTransferParamRpDto {
	private String companyCode;
	private String fromBranchId;
	private int payBonusAtr;
	private int processNo;
	private int processingYM;
	private GeneralDate payDate;
	private int sparePayAtr;
}
