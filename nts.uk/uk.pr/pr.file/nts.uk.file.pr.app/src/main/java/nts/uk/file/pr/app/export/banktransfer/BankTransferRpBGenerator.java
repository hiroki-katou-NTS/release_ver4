package nts.uk.file.pr.app.export.banktransfer;

import nts.arc.layer.infra.file.export.FileGeneratorContext;
import nts.uk.file.pr.app.export.banktransfer.data.BankTransferBReport;

public interface BankTransferRpBGenerator {
	void generator(FileGeneratorContext fileGeneratorContext, BankTransferBReport report);
}
