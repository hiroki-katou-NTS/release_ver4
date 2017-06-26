package nts.uk.file.pr.app.export.banktransfer.data;

import lombok.Data;

import java.util.List;

@Data
public class BankTransferBReport {
	private BankTransferBRpHeader header;
	private List<BankTransferBRpData> data;
}
