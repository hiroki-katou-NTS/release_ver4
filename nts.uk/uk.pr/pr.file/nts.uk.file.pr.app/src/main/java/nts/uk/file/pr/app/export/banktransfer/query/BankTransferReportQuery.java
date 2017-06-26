package nts.uk.file.pr.app.export.banktransfer.query;

import java.util.List;

import lombok.Data;
import nts.arc.time.GeneralDate;

@Data
public class BankTransferReportQuery {
	private List<String> fromBranchId;
	private int processingNo;
	private int processingYm;
	private GeneralDate payDate;
	private String sparePayAtr;
	private int selectedId_J_SEL_001;
	private int currentCode_J_SEL_004;
	private String transferDate;
}
