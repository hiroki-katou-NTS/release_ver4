package nts.uk.file.pr.app.export.banktransfer;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferAReport;

public interface BankTransferRpAGenerator {
	void generator(FileGeneratorContext fileGeneratorContext, BankTransferAReport report);
}
